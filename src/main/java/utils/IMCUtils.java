package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class IMCUtils {
    public static BigDecimal calculateIMC(int weight, int height){
        return BigDecimal.valueOf(((double)weight/((double)height*(double)height))*10).setScale(2, RoundingMode.CEILING);
    }

    public static String interpretIMC(BigDecimal imc, LocalDate birthdate, char gender){
        return "";
    }
}
