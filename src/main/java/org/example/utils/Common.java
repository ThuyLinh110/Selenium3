package org.example.utils;


import java.util.List;
import java.util.stream.Collectors;

public class Common {
    public static <T> List<T> sort(List<T> list) {
        return list.stream().sorted().collect(Collectors.toList());
    }
}
