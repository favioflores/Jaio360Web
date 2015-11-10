package com.jaio360.utils;

import com.jaio360.domain.ModeloContenido;
import java.io.Serializable;
import java.util.Comparator;

public class ReportSort implements Comparator<ModeloContenido>, Serializable{
     @Override
    public int compare(ModeloContenido o1, ModeloContenido o2) {
        return o1.getIntModeloPk().compareTo(o2.getIntModeloPk());
    }
}
