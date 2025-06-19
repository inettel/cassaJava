package ru.inettel.cassa;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ksork on 03.07.17.
 */
public class Test {
    public static void main(String[] args) {
       String address = "пгт. Малышева, ул.Культуры, 3";
        List<String> addressList = Arrays.asList(address.split("\\s"));
        System.out.println(addressList);
        List<String> resultList = addressList.stream()
                .filter(x -> !x.isEmpty())
                .filter(x -> !x.equals(","))
                .filter(x -> !x.equals("ул."))
                .filter(x -> !x.equals("пгт"))
                .filter(x -> !x.equals("пгт."))
                .map(x -> x.replaceAll(",", ""))
                .map(x -> x.replaceAll("ул\\.", ""))
                .map(x -> x.replaceAll("марта", "8 Марта"))
                .collect(Collectors.toList());
        System.out.println(resultList);
    }
}
