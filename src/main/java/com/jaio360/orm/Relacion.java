package com.jaio360.orm;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Relacion implements java.io.Serializable {

    private Integer reIdRelacionPk;
    private Proyecto proyecto;
    private String reTxNombre;
    private String reTxDescripcion;
    private int reNuOrden;
    private String reTxAbreviatura;
    private String reColor;
    private Integer reIdEstado;
    private BigDecimal reDePonderacion;
    private Set relacionParticipantes = new HashSet(0);

    public Integer getReIdRelacionPk() {
        return reIdRelacionPk;
    }

    public void setReIdRelacionPk(Integer reIdRelacionPk) {
        this.reIdRelacionPk = reIdRelacionPk;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getReTxNombre() {
        return reTxNombre;
    }

    public void setReTxNombre(String reTxNombre) {
        this.reTxNombre = reTxNombre;
    }

    public String getReTxDescripcion() {
        return reTxDescripcion;
    }

    public void setReTxDescripcion(String reTxDescripcion) {
        this.reTxDescripcion = reTxDescripcion;
    }

    public int getReNuOrden() {
        return reNuOrden;
    }

    public void setReNuOrden(int reNuOrden) {
        this.reNuOrden = reNuOrden;
    }

    public String getReTxAbreviatura() {
        return reTxAbreviatura;
    }

    public void setReTxAbreviatura(String reTxAbreviatura) {
        this.reTxAbreviatura = reTxAbreviatura;
    }

    public String getReColor() {
        return reColor;
    }

    public void setReColor(String reColor) {
        this.reColor = reColor;
    }

    public Integer getReIdEstado() {
        return reIdEstado;
    }

    public void setReIdEstado(Integer reIdEstado) {
        this.reIdEstado = reIdEstado;
    }

    public Set getRelacionParticipantes() {
        return relacionParticipantes;
    }

    public void setRelacionParticipantes(Set relacionParticipantes) {
        this.relacionParticipantes = relacionParticipantes;
    }

    public BigDecimal getReDePonderacion() {
        return reDePonderacion;
    }

    public void setReDePonderacion(BigDecimal reDePonderacion) {
        this.reDePonderacion = reDePonderacion;
    }
    
    public Relacion() {
    }

    public Relacion(Integer reIdRelacionPk, String reTxNombre) {
        this.reIdRelacionPk = reIdRelacionPk;
        this.reTxNombre = reTxNombre;
    }

    public Relacion(Proyecto proyecto, String reTxNombre, String reTxDescripcion, int reNuOrden, String reTxAbreviatura, String reColor, Integer reIdEstado) {
        this.proyecto = proyecto;
        this.reTxNombre = reTxNombre;
        this.reTxDescripcion = reTxDescripcion;
        this.reNuOrden = reNuOrden;
        this.reTxAbreviatura = reTxAbreviatura;
        this.reColor = reColor;
        this.reIdEstado = reIdEstado;
    }

    public Relacion(Proyecto proyecto, String reTxNombre, String reTxDescripcion, int reNuOrden, String reTxAbreviatura, String reColor, int reIdEstado, Set relacionParticipantes) {
        this.proyecto = proyecto;
        this.reTxNombre = reTxNombre;
        this.reTxDescripcion = reTxDescripcion;
        this.reNuOrden = reNuOrden;
        this.reTxAbreviatura = reTxAbreviatura;
        this.reColor = reColor;
        this.reIdEstado = reIdEstado;
        this.relacionParticipantes = relacionParticipantes;
    }

}
