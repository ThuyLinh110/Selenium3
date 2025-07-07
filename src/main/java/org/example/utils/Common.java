package org.example.utils;


import java.util.List;
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

}
