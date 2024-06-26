package com.jaio360.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Cuestionario implements java.io.Serializable {

    private Integer cuIdCuestionarioPk;
    private Proyecto proyecto;
    private String cuTxDescripcion;
    private Date cuFeRegistro;
    private Integer cuIdEstado;
    private Set cuestionarioEvaluados = new HashSet(0);
    private Set componentes = new HashSet(0);

    public Cuestionario() {
    }

    public Cuestionario(Integer cuIdCuestionarioPk, String cuTxDescripcion) {
        this.cuIdCuestionarioPk = cuIdCuestionarioPk;
        this.cuTxDescripcion = cuTxDescripcion;
    }

    public Cuestionario(Proyecto proyecto, String cuTxDescripcion, Date cuFeRegistro, Integer cuIdEstado) {
        this.proyecto = proyecto;
        this.cuTxDescripcion = cuTxDescripcion;
        this.cuFeRegistro = cuFeRegistro;
        this.cuIdEstado = cuIdEstado;
    }

    public Cuestionario(Proyecto proyecto, String cuTxDescripcion, Date cuFeRegistro, Integer cuIdEstado, Set cuestionarioEvaluados, Set componentes) {
        this.proyecto = proyecto;
        this.cuTxDescripcion = cuTxDescripcion;
        this.cuFeRegistro = cuFeRegistro;
        this.cuIdEstado = cuIdEstado;
        this.cuestionarioEvaluados = cuestionarioEvaluados;
        this.componentes = componentes;
    }

    public Integer getCuIdCuestionarioPk() {
        return cuIdCuestionarioPk;
    }

    public void setCuIdCuestionarioPk(Integer cuIdCuestionarioPk) {
        this.cuIdCuestionarioPk = cuIdCuestionarioPk;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getCuTxDescripcion() {
        return cuTxDescripcion;
    }

    public void setCuTxDescripcion(String cuTxDescripcion) {
        this.cuTxDescripcion = cuTxDescripcion;
    }

    public Date getCuFeRegistro() {
        return cuFeRegistro;
    }

    public void setCuFeRegistro(Date cuFeRegistro) {
        this.cuFeRegistro = cuFeRegistro;
    }

    public Integer getCuIdEstado() {
        return cuIdEstado;
    }

    public void setCuIdEstado(Integer cuIdEstado) {
        this.cuIdEstado = cuIdEstado;
    }

    public Set getCuestionarioEvaluados() {
        return cuestionarioEvaluados;
    }

    public void setCuestionarioEvaluados(Set cuestionarioEvaluados) {
        this.cuestionarioEvaluados = cuestionarioEvaluados;
    }

    public Set getComponentes() {
        return componentes;
    }

    public void setComponentes(Set componentes) {
        this.componentes = componentes;
    }

}
