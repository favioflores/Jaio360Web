/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Favio
 */
public class ProyectoInfo implements Serializable{

    private static final long serialVersionUID = -1L;
    
    private Integer intIdProyecto;
    private String strDescNombre;
    private String strMotivo;
    private Integer intIdEstado;
    private String strDescEstado;
    private Integer intIdMetodologia;
    private String strDescMetodologia;
    private LocalDate dtFechaCreacion;
    private Date dtFechaEjecucion;
    private Integer intIdCuestionario;
    private String strDescCuestionario;
    private boolean blConvocatoria = false;
    private boolean blInstrucciones = false;
    private boolean blAgradecimiento = false;
    
    private boolean boDefineArtificio;
    private String strCorreoEvaluado;
    private String strNombreEvaluado;
    private String strCargoEvaluado;
    private String strCorreoEvaluador;
    private String strNombreEvaluador;
    private String strRelacion;
    private String strRelacionColor;
    
    /* Para lista de evaluaciones */
    private Integer intIdEvaluado;
    private Integer intCantidadEvaluaciones;
    private boolean blGrupal;
    private List<EvaluacionesXEjecutar> lstEvaluacionesXEjecutar;
    
    private boolean boOculto;

    public ProyectoInfo(){
        blConvocatoria = false;
        blInstrucciones = false;
        blAgradecimiento = false;
        boDefineArtificio = false;
        lstEvaluacionesXEjecutar = new ArrayList<>();
    }

    public String getStrCargoEvaluado() {
        return strCargoEvaluado;
    }

    public void setStrCargoEvaluado(String strCargoEvaluado) {
        this.strCargoEvaluado = strCargoEvaluado;
    }

    public Integer getIntCantidadEvaluaciones() {
        return intCantidadEvaluaciones;
    }

    public void setIntCantidadEvaluaciones(Integer intCantidadEvaluaciones) {
        this.intCantidadEvaluaciones = intCantidadEvaluaciones;
    }
    
    
    public String getStrRelacion() {
        return strRelacion;
    }

    public void setStrRelacion(String strRelacion) {
        this.strRelacion = strRelacion;
    }

    public String getStrRelacionColor() {
        return strRelacionColor;
    }

    public void setStrRelacionColor(String strRelacionColor) {
        this.strRelacionColor = strRelacionColor;
    }

    public String getStrNombreEvaluador() {
        return strNombreEvaluador;
    }

    public void setStrNombreEvaluador(String strNombreEvaluador) {
        this.strNombreEvaluador = strNombreEvaluador;
    }

    public boolean isBoOculto() {
        return boOculto;
    }

    public void setBoOculto(boolean boOculto) {
        this.boOculto = boOculto;
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

    public String getStrCorreoEvaluado() {
        return strCorreoEvaluado;
    }

    public boolean isBlGrupal() {
        return blGrupal;
    }

    public void setBlGrupal(boolean blGrupal) {
        this.blGrupal = blGrupal;
    }

    public List<EvaluacionesXEjecutar> getLstEvaluacionesXEjecutar() {
        return lstEvaluacionesXEjecutar;
    }

    public void setLstEvaluacionesXEjecutar(List<EvaluacionesXEjecutar> lstEvaluacionesXEjecutar) {
        this.lstEvaluacionesXEjecutar = lstEvaluacionesXEjecutar;
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

    public LocalDate  getDtFechaCreacion() {
        return dtFechaCreacion;
    }

    public void setDtFechaCreacion(LocalDate  dtFechaCreacion) {
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
