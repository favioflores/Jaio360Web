package com.jaio360.domain;

import java.io.Serializable;

public class DosDatos implements Serializable {

    private String strDato1;
    private String strDato2;
    private String strDato3;
    private Integer intComponente;

    public String getStrDato1() {
        return strDato1;
    }

    public void setStrDato1(String strDato1) {
        this.strDato1 = strDato1;
    }

    public String getStrDato2() {
        return strDato2;
    }

    public void setStrDato2(String strDato2) {
        this.strDato2 = strDato2;
    }

    public String getStrDato3() {
        return strDato3;
    }

    public void setStrDato3(String strDato3) {
        this.strDato3 = strDato3;
    }

    public Integer getIntComponente() {
        return intComponente;
    }

    public void setIntComponente(Integer intComponente) {
        this.intComponente = intComponente;
    }

    public DosDatos(String a, String b, String c, Integer d) {

        strDato1 = a;
        strDato2 = b;
        strDato3 = c;
        intComponente = d;

    }

}
