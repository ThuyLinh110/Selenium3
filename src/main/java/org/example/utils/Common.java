package org.example.utils;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Common {
    public static <T> List<T> sort(List<T> list) {
        return list.stream().sorted().collect(Collectors.toList());
    }

    public static Integer convertMoneyToNumber(String money) {
        String numeric = money.replaceAll("[^0-9]", "");
        return Integer.parseInt(numeric);
    }

    public static List<String> lowerCaseList(List<String> stringList) {
        return stringList.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    public static String formatWithCommas(int number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static Locale getLocale() {
        String country = (String) YamlUtils.getProperty("config.country");
        String language = (String) YamlUtils.getProperty("config.language");
        return new Locale.Builder()
                .setLanguage(language)
                .setRegion(country)
                .build();
    }

}
