package com.jaio360.domain;

import java.io.Serializable;

public class ErrorBean implements Serializable {

    private Integer intNum;
    private String strDesc;
    private String strCues;
    private String strLinea;

    public ErrorBean(Integer intNum, String strDesc) {
        this.intNum = intNum;
        this.strDesc = strDesc;
    }

    public ErrorBean(Integer intNum, String strDesc, String strCues, String strLinea) {
        this.intNum = intNum;
        this.strDesc = strDesc;
        this.strCues = strCues;
        this.strLinea = strLinea;
    }

    public Integer getIntNum() {
        return intNum;
    }

    public void setIntNum(Integer intNum) {
        this.intNum = intNum;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public String getStrCues() {
        return strCues;
    }

    public void setStrCues(String strCues) {
        this.strCues = strCues;
    }

    public String getStrLinea() {
        return strLinea;
    }

    public void setStrLinea(String strLinea) {
        this.strLinea = strLinea;
    }

}
