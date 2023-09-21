package com.jaio360.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class DatosReporte implements Serializable {

    private String strID;
    private String strNombreProyecto;
    private String strNombreEvaluado;
    private Integer intEvaluado;
    private String strDescripcionProyecto;
    private String strDescripcion;
    private String strURLCliente;
    private String strEmailEvaluado;
    private Integer intIdCuestionario;
    private String strCuestionario;
    private Boolean blDefinitivo;
    private LinkedHashMap mapRelaciones;
    private Integer intMaxRango;
    private Boolean blWeighted;

    public Boolean getBlWeighted() {
        return blWeighted;
    }

    public void setBlWeighted(Boolean blWeighted) {
        this.blWeighted = blWeighted;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getStrNombreProyecto() {
        return strNombreProyecto;
    }

    public Integer getIntEvaluado() {
        return intEvaluado;
    }

    public void setIntEvaluado(Integer intEvaluado) {
        this.intEvaluado = intEvaluado;
    }

    public void setStrNombreProyecto(String strNombreProyecto) {
        this.strNombreProyecto = strNombreProyecto;
    }

    public String getStrNombreEvaluado() {
        return strNombreEvaluado;
    }

    public void setStrNombreEvaluado(String strNombreEvaluado) {
        this.strNombreEvaluado = strNombreEvaluado;
    }

    public String getStrDescripcionProyecto() {
        return strDescripcionProyecto;
    }

    public void setStrDescripcionProyecto(String strDescripcionProyecto) {
        this.strDescripcionProyecto = strDescripcionProyecto;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrURLCliente() {
        return strURLCliente;
    }

    public void setStrURLCliente(String strURLCliente) {
        this.strURLCliente = strURLCliente;
    }

    public String getStrEmailEvaluado() {
        return strEmailEvaluado;
    }

    public void setStrEmailEvaluado(String strEmailEvaluado) {
        this.strEmailEvaluado = strEmailEvaluado;
    }

    public Integer getIntIdCuestionario() {
        return intIdCuestionario;
    }

    public void setIntIdCuestionario(Integer intIdCuestionario) {
        this.intIdCuestionario = intIdCuestionario;
    }

    public String getStrCuestionario() {
        return strCuestionario;
    }

    public void setStrCuestionario(String strCuestionario) {
        this.strCuestionario = strCuestionario;
    }

    public Boolean getBlDefinitivo() {
        return blDefinitivo;
    }

    public void setBlDefinitivo(Boolean blDefinitivo) {
        this.blDefinitivo = blDefinitivo;
    }

    public LinkedHashMap getMapRelaciones() {
        return mapRelaciones;
    }

    public void setMapRelaciones(LinkedHashMap mapRelaciones) {
        this.mapRelaciones = mapRelaciones;
    }

    public Integer getIntMaxRango() {
        return intMaxRango;
    }

    public void setIntMaxRango(Integer intMaxRango) {
        this.intMaxRango = intMaxRango;
    }

}
