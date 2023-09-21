package com.jaio360.domain;

import java.io.Serializable;

public class RelacionAvanzada implements Serializable {

    private String strEvaluado;
    private String strEvaluadoDesc;
    private String strEvaluador;
    private String strEvaluadorDesc;
    private String strRelacion;
    private String strRelacionAbreviatura;
    private String strRelacionColor;

    public String getStrEvaluado() {
        return strEvaluado;
    }

    public void setStrEvaluado(String strEvaluado) {
        this.strEvaluado = strEvaluado;
    }

    public String getStrEvaluadoDesc() {
        return strEvaluadoDesc;
    }

    public void setStrEvaluadoDesc(String strEvaluadoDesc) {
        this.strEvaluadoDesc = strEvaluadoDesc;
    }

    public String getStrEvaluador() {
        return strEvaluador;
    }

    public void setStrEvaluador(String strEvaluador) {
        this.strEvaluador = strEvaluador;
    }

    public String getStrEvaluadorDesc() {
        return strEvaluadorDesc;
    }

    public void setStrEvaluadorDesc(String strEvaluadorDesc) {
        this.strEvaluadorDesc = strEvaluadorDesc;
    }

    public String getStrRelacion() {
        return strRelacion;
    }

    public void setStrRelacion(String strRelacion) {
        this.strRelacion = strRelacion;
    }

    public String getStrRelacionAbreviatura() {
        return strRelacionAbreviatura;
    }

    public void setStrRelacionAbreviatura(String strRelacionAbreviatura) {
        this.strRelacionAbreviatura = strRelacionAbreviatura;
    }

    public String getStrRelacionColor() {
        return strRelacionColor;
    }

    public void setStrRelacionColor(String strRelacionColor) {
        this.strRelacionColor = strRelacionColor;
    }

}
