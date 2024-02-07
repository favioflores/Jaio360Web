package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class DosDatos implements Serializable {

    private String strDato1;
    private String strDato2;
    private String strDato3;
    private BigDecimal bdScoreMinRequired;
    private BigDecimal bdScoreRequired;
    private Integer intComponente;

    public BigDecimal getBdScoreRequired() {
        return bdScoreRequired;
    }

    public void setBdScoreRequired(BigDecimal bdScoreRequired) {
        this.bdScoreRequired = bdScoreRequired;
    }

    public String getStrDato1() {
        return strDato1;
    }

    public BigDecimal getBdScoreMinRequired() {
        return bdScoreMinRequired;
    }

    public void setBdScoreMinRequired(BigDecimal bdScoreMinRequired) {
        this.bdScoreMinRequired = bdScoreMinRequired;
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

    public DosDatos(String a, String b, String c, Integer d, BigDecimal bdScoreRequired, BigDecimal bdScoreMinRequired) {

        strDato1 = a;
        strDato2 = b;
        strDato3 = c;
        intComponente = d;
        this.bdScoreMinRequired = bdScoreMinRequired;
        this.bdScoreRequired = bdScoreRequired;

    }

}
