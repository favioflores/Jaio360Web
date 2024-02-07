package com.jaio360.domain;

import java.io.Serializable;
import javax.persistence.Temporal;

public class ColumnModel implements Serializable {

    private String header;
    private String property;
    private String type;
    private Class<?> klazz;

    public ColumnModel(String header, String property, Class klazz) {
        this.header = header;
        this.property = property;
        this.klazz = klazz;
        initType();
    }

    public String getHeader() {
        return header;
    }

    public String getProperty() {
        return property;
    }

    public String getType() {
        return type;
    }

    public Class<?> getKlazz() {
        return klazz;
    }

    private void initType() {
        if (Temporal.class.isAssignableFrom(klazz)) {
            type = "date";
        } else if (klazz.isEnum()) {
            type = "enum";
        }
    }

}
