package xyz.manzodev.thecoffeehouse.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
    public static boolean isNotEmpty(String s){
        return (s!=null && s.trim().length()>0);
    }

    public static String formatPrice(long price){
        return  String.format(Locale.US,"%,d", price).replace(',', '.') + "đ";
    }

    public static String searchConvert(String q){
        String temp = Normalizer.normalize(q, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");

    }
}
