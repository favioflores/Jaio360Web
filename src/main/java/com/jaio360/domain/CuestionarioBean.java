package com.jaio360.domain;

import com.jaio360.orm.*;
import java.io.Serializable;
import java.util.Date;

public class CuestionarioBean  implements Serializable {

    private Integer cuIdCuestionarioPk;
    private Proyecto proyecto;
    private String cuTxDescripcion;
    private Date cuFeRegistro;
    private Integer cuIdEstado;
    private String cuDescEstado;
    private boolean boConfirmado;
    private Integer intCantidadCategorias;
    private Integer intCantidadPreguntasCerradas;
    private Integer intCantidadComentarios;
    private Integer intCantidadPreguntasAbiertas;

    public Integer getIntCantidadPreguntasCerradas() {
        return intCantidadPreguntasCerradas;
    }

    public void setIntCantidadPreguntasCerradas(Integer intCantidadPreguntasCerradas) {
        this.intCantidadPreguntasCerradas = intCantidadPreguntasCerradas;
    }

    public Integer getIntCantidadComentarios() {
        return intCantidadComentarios;
    }

    public void setIntCantidadComentarios(Integer intCantidadComentarios) {
        this.intCantidadComentarios = intCantidadComentarios;
    }

    public Integer getIntCantidadCategorias() {
        return intCantidadCategorias;
    }

    public void setIntCantidadCategorias(Integer intCantidadCategorias) {
        this.intCantidadCategorias = intCantidadCategorias;
    }

    public Integer getIntCantidadPreguntasAbiertas() {
        return intCantidadPreguntasAbiertas;
    }

    public void setIntCantidadPreguntasAbiertas(Integer intCantidadPreguntasAbiertas) {
        this.intCantidadPreguntasAbiertas = intCantidadPreguntasAbiertas;
    }
    
    public boolean isBoConfirmado() {
        return boConfirmado;
    }

    public void setBoConfirmado(boolean boConfirmado) {
        this.boConfirmado = boConfirmado;
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

    public String getCuDescEstado() {
        return cuDescEstado;
    }

    public void setCuDescEstado(String cuDescEstado) {
        this.cuDescEstado = cuDescEstado;
    }

}



