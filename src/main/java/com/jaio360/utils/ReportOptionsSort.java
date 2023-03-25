package com.jaio360.utils;

import java.io.Serializable;
import java.util.Comparator;
import javax.faces.model.SelectItem;

public class ReportOptionsSort implements Comparator<SelectItem>, Serializable {

    @Override
    public int compare(SelectItem o1, SelectItem o2) {
        return o1.getLabel().compareTo(o2.getLabel());
    }
}
