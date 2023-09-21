package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class RelacionBean implements Serializable {

    private String strNombre;
    private String strAbreviatura;
    private String strDescripcion;
    private String strColor;
    private Integer idRelacionPk;
    private Integer intIdEstado;
    private String strEstado;
    private Integer intCantidadUso;
    private BigDecimal dbPonderado;

    public BigDecimal getDbPonderado() {
        return dbPonderado;
    }

    public void setDbPonderado(BigDecimal dbPonderado) {
        this.dbPonderado = dbPonderado;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrAbreviatura() {
        return strAbreviatura;
    }

    public void setStrAbreviatura(String strAbreviatura) {
        this.strAbreviatura = strAbreviatura;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrColor() {
        return strColor;
    }

    public void setStrColor(String strColor) {
        this.strColor = strColor;
    }

    public Integer getIdRelacionPk() {
        return idRelacionPk;
    }

    public void setIdRelacionPk(Integer idRelacionPk) {
        this.idRelacionPk = idRelacionPk;
    }

    public Integer getIntIdEstado() {
        return intIdEstado;
    }

    public void setIntIdEstado(Integer intIdEstado) {
        this.intIdEstado = intIdEstado;
    }

    public String getStrEstado() {
        return strEstado;
    }

    public void setStrEstado(String strEstado) {
        this.strEstado = strEstado;
    }

    public Integer getIntCantidadUso() {
        return intCantidadUso;
    }

    public void setIntCantidadUso(Integer intCantidadUso) {
        this.intCantidadUso = intCantidadUso;
    }

}
