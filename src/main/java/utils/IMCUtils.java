package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

public class IMCUtils {
    public static BigDecimal calculateIMC(int weight, int height){
        return BigDecimal.valueOf(((double)weight/((double)height*(double)height))*10).setScale(2, RoundingMode.CEILING);
    }

    public static String interpretIMC(BigDecimal imc, LocalDate birthdate, char gender){
        Period age = Period.between(birthdate, LocalDate.now());

        if(age.getYears()>=20){
            if (imc.compareTo(BigDecimal.valueOf(185, 1)) < 0){
                return "Bajo peso";
            }
            else if (imc.compareTo(BigDecimal.valueOf(249, 1)) < 0){
                return "Peso saludable";
            }
            else if (imc.compareTo(BigDecimal.valueOf(299, 1)) < 0) {
                return "Sobrepeso";
            }
            else if (imc.compareTo(BigDecimal.valueOf(349, 1)) < 0) {
                return "Obesidad I";
            }
            else if (imc.compareTo(BigDecimal.valueOf(399, 1)) < 0) {
                return "Obesidad II";
            }
            else if (imc.compareTo(BigDecimal.valueOf(499, 1)) < 0) {
                return "Obesidad III";
            }
            else {
                return "Obesidad IV";
            }
        }
        return "";
    }
}
