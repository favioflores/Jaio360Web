/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Favio
 */
public class ProyectoInfo implements Serializable{

    private Integer intIdProyecto;
    private String strDescNombre;
    private String strMotivo;
    private Integer intIdEstado;
    private String strDescEstado;
    private Integer intIdMetodologia;
    private String strDescMetodologia;
    private Date dtFechaCreacion;
    private Date dtFechaEjecucion;
    private Integer intIdCuestionario;
    private String strDescCuestionario;
    private boolean blConvocatoria = false;
    private boolean blInstrucciones = false;
    private boolean blAgradecimiento = false;
    
    private boolean boDefineArtificio;
    private String strCorreoEvaluado;
    private String strNombreEvaluado;
    private String strCorreoEvaluador;
    
    /* Para lista de evaluaciones */
    private String strDescEvaluado;
    private Integer intIdEvaluado;

    public ProyectoInfo(){
        blConvocatoria = false;
        blInstrucciones = false;
        blAgradecimiento = false;
        boDefineArtificio = false;
    }

    public Date getDtFechaEjecucion() {
        return dtFechaEjecucion;
    }

    public void setDtFechaEjecucion(Date dtFechaEjecucion) {
        this.dtFechaEjecucion = dtFechaEjecucion;
    }

    
    public String getStrNombreEvaluado() {
        return strNombreEvaluado;
    }

    public void setStrNombreEvaluado(String strNombreEvaluado) {
        this.strNombreEvaluado = strNombreEvaluado;
    }
    
    public String getStrCorreoEvaluador() {
        return strCorreoEvaluador;
    }

    public void setStrCorreoEvaluador(String strCorreoEvaluador) {
        this.strCorreoEvaluador = strCorreoEvaluador;
    }

    public String getStrMotivo() {
        return strMotivo;
    }

    public void setStrMotivo(String strMotivo) {
        this.strMotivo = strMotivo;
    }

    
    public Integer getIntIdEvaluado() {
        return intIdEvaluado;
    }

    public void setIntIdEvaluado(Integer intIdEvaluado) {
        this.intIdEvaluado = intIdEvaluado;
    }

    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
    }

    public Integer getIntIdCuestionario() {
        return intIdCuestionario;
    }

    public void setIntIdCuestionario(Integer intIdCuestionario) {
        this.intIdCuestionario = intIdCuestionario;
    }

    public String getStrDescEvaluado() {
        return strDescEvaluado;
    }

    public void setStrDescEvaluado(String strDescEvaluado) {
        this.strDescEvaluado = strDescEvaluado;
    }

    public String getStrCorreoEvaluado() {
        return strCorreoEvaluado;
    }

    public void setStrCorreoEvaluado(String strCorreoEvaluado) {
        this.strCorreoEvaluado = strCorreoEvaluado;
    }

    public boolean isBoDefineArtificio() {
        return boDefineArtificio;
    }

    public void setBoDefineArtificio(boolean boDefineArtificio) {
        this.boDefineArtificio = boDefineArtificio;
    }
    
    public Integer getIntIdProyecto() {
        return intIdProyecto;
    }

    public void setIntIdProyecto(Integer intIdProyecto) {
        this.intIdProyecto = intIdProyecto;
    }

    public String getStrDescNombre() {
        return strDescNombre;
    }

    public void setStrDescNombre(String strDescNombre) {
        this.strDescNombre = strDescNombre;
    }

    public Date getDtFechaCreacion() {
        return dtFechaCreacion;
    }

    public void setDtFechaCreacion(Date dtFechaCreacion) {
        this.dtFechaCreacion = dtFechaCreacion;
    }

    public Integer getIntIdEstado() {
        return intIdEstado;
    }

    public void setIntIdEstado(Integer intIdEstado) {
        this.intIdEstado = intIdEstado;
    }

    public String getStrDescEstado() {
        return strDescEstado;
    }

    public void setStrDescEstado(String strDescEstado) {
        this.strDescEstado = strDescEstado;
    }

    public Integer getIntIdMetodologia() {
        return intIdMetodologia;
    }

    public void setIntIdMetodologia(Integer intIdMetodologia) {
        this.intIdMetodologia = intIdMetodologia;
    }

    public String getStrDescMetodologia() {
        return strDescMetodologia;
    }

    public void setStrDescMetodologia(String strDescMetodologia) {
        this.strDescMetodologia = strDescMetodologia;
    }

    public boolean isBlConvocatoria() {
        return blConvocatoria;
    }

    public void setBlConvocatoria(boolean blConvocatoria) {
        this.blConvocatoria = blConvocatoria;
    }

    public boolean isBlInstrucciones() {
        return blInstrucciones;
    }

    public void setBlInstrucciones(boolean blInstrucciones) {
        this.blInstrucciones = blInstrucciones;
    }

    public boolean isBlAgradecimiento() {
        return blAgradecimiento;
    }

    public void setBlAgradecimiento(boolean blAgradecimiento) {
        this.blAgradecimiento = blAgradecimiento;
    }
     
    
}
