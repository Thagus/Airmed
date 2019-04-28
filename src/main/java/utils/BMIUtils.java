package utils;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class BMIUtils {
    private static Map<Integer, StatisticalValue> maleBMI20;
    private static Map<Integer, StatisticalValue> femaleBMI20;
    private static NormalDistribution normalDistribution;

    public static void init(){
        normalDistribution = new NormalDistribution();
        maleBMI20 = new HashMap<>();
        femaleBMI20 = new HashMap<>();

        InputStream inputStream = null;
        try  {
            URL fileURL = BMIUtils.class.getClassLoader().getResource("data/bmiagerev.csv");

            if(fileURL==null){
                System.out.println("Cannot load bmiagrerev file");
                return;
            }

            File file = new File(fileURL.getFile());
            inputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if(values[0].equals("1")){
                    maleBMI20.put(Integer.parseInt(values[1]), new StatisticalValue(Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4])));
                }
                else {
                    femaleBMI20.put(Integer.parseInt(values[1]), new StatisticalValue(Double.parseDouble(values[2]), Double.parseDouble(values[3]), Double.parseDouble(values[4])));
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static BigDecimal calculateBMI(int weight, int height){
        return BigDecimal.valueOf(((double)weight/((double)height*(double)height))*10).setScale(2, RoundingMode.CEILING);
    }

    public static String interpretBMI(BigDecimal bmi, LocalDate birthdate, char gender){
        int ageMonths = (int) ChronoUnit.MONTHS.between(birthdate, LocalDate.now());

        if(ageMonths>240){
            if (bmi.compareTo(BigDecimal.valueOf(185, 1)) < 0){
                return "Bajo peso";
            }
            else if (bmi.compareTo(BigDecimal.valueOf(249, 1)) < 0){
                return "Peso saludable";
            }
            else if (bmi.compareTo(BigDecimal.valueOf(299, 1)) < 0) {
                return "Sobrepeso";
            }
            else if (bmi.compareTo(BigDecimal.valueOf(349, 1)) < 0) {
                return "Obesidad I";
            }
            else if (bmi.compareTo(BigDecimal.valueOf(399, 1)) < 0) {
                return "Obesidad II";
            }
            else if (bmi.compareTo(BigDecimal.valueOf(499, 1)) < 0) {
                return "Obesidad III";
            }
            else {
                return "Obesidad IV";
            }
        }
        else if (ageMonths>=24){
            StatisticalValue statisticalValue = null;
            if(gender=='M'){
                statisticalValue = maleBMI20.get(ageMonths);
            }
            else {
                statisticalValue = femaleBMI20.get(ageMonths);
            }

            double zScore = 0;

            if(statisticalValue.getBoxCoxTransformation()==0){
                zScore = Math.log(bmi.doubleValue()/statisticalValue.getMedian())/statisticalValue.getVariation();
            }
            else {
                zScore = (Math.pow(bmi.doubleValue()/statisticalValue.getMedian(), statisticalValue.getBoxCoxTransformation())-1.0)/
                        (statisticalValue.getBoxCoxTransformation()*statisticalValue.getVariation());
            }

            int x = (int) (normalDistribution.cumulativeProbability(zScore)*100);

            String status;

            if(x<5){
                status = "Bajo peso";
            }
            else if(x<85){
                status = "Normal";
            }
            else if(x<95){
                status = "Sobrepeso";
            }
            else {
                status = "Obeso";
            }

            return status + " p" + x;
        }
        return "";
    }


}

class StatisticalValue {
    private double median;
    private double variation;
    private double boxCoxTransformation;

    StatisticalValue(double boxCoxTransformation, double median, double variation) {
        this.median = median;
        this.variation = variation;
        this.boxCoxTransformation = boxCoxTransformation;
    }

    double getMedian() {
        return median;
    }

    double getVariation() {
        return variation;
    }

    double getBoxCoxTransformation() {
        return boxCoxTransformation;
    }
}
