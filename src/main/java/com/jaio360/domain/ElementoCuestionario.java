package com.jaio360.domain;

import java.io.Serializable;

public class ElementoCuestionario implements Serializable{
    
    private String strTipo;
    private String strDescripción;
    private String strColor;

    public String getStrTipo() {
        return strTipo;
    }

    public void setStrTipo(String strTipo) {
        this.strTipo = strTipo;
    }

    public String getStrDescripción() {
        return strDescripción;
    }

    public void setStrDescripción(String strDescripción) {
        this.strDescripción = strDescripción;
    }

    public String getStrColor() {
        return strColor;
    }

    public void setStrColor(String strColor) {
        this.strColor = strColor;
    }

    
    
}