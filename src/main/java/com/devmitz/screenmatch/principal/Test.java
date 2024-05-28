package com.devmitz.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> nomes = Arrays.asList("Felipe", "João", "Iasmin");
        nomes.stream()
                .sorted()
                .limit(2)
                .filter(n -> n.startsWith("F"))
                .map(n -> n.toLowerCase())
                .forEach(System.out::println);
    }
}
