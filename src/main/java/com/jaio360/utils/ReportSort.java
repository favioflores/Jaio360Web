package com.jaio360.utils;

import java.io.Serializable;
import java.util.Comparator;

public class ReportSort implements Comparator<Integer>, Serializable {

    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
    }
}
