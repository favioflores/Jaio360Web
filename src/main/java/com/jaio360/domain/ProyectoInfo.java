package com.jaio360.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProyectoInfo implements Serializable {

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
    private Boolean blConvocatoria = false;
    private Boolean blInstrucciones = false;
    private Boolean blAgradecimiento = false;
    private Boolean boDefineArtificio;
    private String strCorreoEvaluado;
    private String strNombreEvaluado;
    private String strCargoEvaluado;
    private String strCorreoEvaluador;
    private String strNombreEvaluador;
    private String strRelacion;
    private String strRelacionColor;
    private Integer intIdEvaluado;
    private Integer intCantidadEvaluaciones;
    private Boolean blGrupal;
    private List<EvaluacionesXEjecutar> lstEvaluacionesXEjecutar;
    private Boolean boOculto;

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

    public String getStrMotivo() {
        return strMotivo;
    }

    public void setStrMotivo(String strMotivo) {
        this.strMotivo = strMotivo;
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

    public LocalDate getDtFechaCreacion() {
        return dtFechaCreacion;
    }

    public void setDtFechaCreacion(LocalDate dtFechaCreacion) {
        this.dtFechaCreacion = dtFechaCreacion;
    }

    public Date getDtFechaEjecucion() {
        return dtFechaEjecucion;
    }

    public void setDtFechaEjecucion(Date dtFechaEjecucion) {
        this.dtFechaEjecucion = dtFechaEjecucion;
    }

    public Integer getIntIdCuestionario() {
        return intIdCuestionario;
    }

    public void setIntIdCuestionario(Integer intIdCuestionario) {
        this.intIdCuestionario = intIdCuestionario;
    }

    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
    }

    public Boolean getBlConvocatoria() {
        return blConvocatoria;
    }

    public void setBlConvocatoria(Boolean blConvocatoria) {
        this.blConvocatoria = blConvocatoria;
    }

    public Boolean getBlInstrucciones() {
        return blInstrucciones;
    }

    public void setBlInstrucciones(Boolean blInstrucciones) {
        this.blInstrucciones = blInstrucciones;
    }

    public Boolean getBlAgradecimiento() {
        return blAgradecimiento;
    }

    public void setBlAgradecimiento(Boolean blAgradecimiento) {
        this.blAgradecimiento = blAgradecimiento;
    }

    public Boolean getBoDefineArtificio() {
        return boDefineArtificio;
    }

    public void setBoDefineArtificio(Boolean boDefineArtificio) {
        this.boDefineArtificio = boDefineArtificio;
    }

    public String getStrCorreoEvaluado() {
        return strCorreoEvaluado;
    }

    public void setStrCorreoEvaluado(String strCorreoEvaluado) {
        this.strCorreoEvaluado = strCorreoEvaluado;
    }

    public String getStrNombreEvaluado() {
        return strNombreEvaluado;
    }

    public void setStrNombreEvaluado(String strNombreEvaluado) {
        this.strNombreEvaluado = strNombreEvaluado;
    }

    public String getStrCargoEvaluado() {
        return strCargoEvaluado;
    }

    public void setStrCargoEvaluado(String strCargoEvaluado) {
        this.strCargoEvaluado = strCargoEvaluado;
    }

    public String getStrCorreoEvaluador() {
        return strCorreoEvaluador;
    }

    public void setStrCorreoEvaluador(String strCorreoEvaluador) {
        this.strCorreoEvaluador = strCorreoEvaluador;
    }

    public String getStrNombreEvaluador() {
        return strNombreEvaluador;
    }

    public void setStrNombreEvaluador(String strNombreEvaluador) {
        this.strNombreEvaluador = strNombreEvaluador;
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

    public Integer getIntIdEvaluado() {
        return intIdEvaluado;
    }

    public void setIntIdEvaluado(Integer intIdEvaluado) {
        this.intIdEvaluado = intIdEvaluado;
    }

    public Integer getIntCantidadEvaluaciones() {
        return intCantidadEvaluaciones;
    }

    public void setIntCantidadEvaluaciones(Integer intCantidadEvaluaciones) {
        this.intCantidadEvaluaciones = intCantidadEvaluaciones;
    }

    public Boolean getBlGrupal() {
        return blGrupal;
    }

    public void setBlGrupal(Boolean blGrupal) {
        this.blGrupal = blGrupal;
    }

    public List<EvaluacionesXEjecutar> getLstEvaluacionesXEjecutar() {
        return lstEvaluacionesXEjecutar;
    }

    public void setLstEvaluacionesXEjecutar(List<EvaluacionesXEjecutar> lstEvaluacionesXEjecutar) {
        this.lstEvaluacionesXEjecutar = lstEvaluacionesXEjecutar;
    }

    public Boolean getBoOculto() {
        return boOculto;
    }

    public void setBoOculto(Boolean boOculto) {
        this.boOculto = boOculto;
    }

    public ProyectoInfo() {
        blConvocatoria = false;
        blInstrucciones = false;
        blAgradecimiento = false;
        boDefineArtificio = false;
        lstEvaluacionesXEjecutar = new ArrayList<>();
    }

}
