package com.activiti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class middleTest {
    public static void main(String[] args) {
        List<Double> doubleList = new ArrayList<>();
        doubleList.add(25d);
        doubleList.add(12d);
        doubleList.add(23d);
        doubleList.add(2d);
        doubleList.add(7d);
        Collections.sort(doubleList);
        double finalGrade;
        if (doubleList.size() % 2 == 0) {
            int a = doubleList.size() / 2-1;
            finalGrade = (doubleList.get(a) + doubleList.get(a + 1)) / 2.0;
        } else {
            int a = (doubleList.size() / 2);
            finalGrade = doubleList.get(a);
        }
        System.out.println(finalGrade);
    }
}
