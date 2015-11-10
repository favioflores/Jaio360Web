package com.jaio360.domain;

import java.io.Serializable;
import java.util.Map;

public class DatosReporte implements Serializable {

    private String strNombre;
    private String strID;
    private String strNombreEvaluado;
    private String strDescripcion;
    private Integer intIdCuestionario;
    private String strCuestionario;
    private boolean blDefinitivo;
    private Map mapRelaciones;
    private Integer intMaxRango;

    public Integer getIntIdCuestionario() {
        return intIdCuestionario;
    }

    public void setIntIdCuestionario(Integer intIdCuestionario) {
        this.intIdCuestionario = intIdCuestionario;
    }

    public Integer getIntMaxRango() {
        return intMaxRango;
    }

    public void setIntMaxRango(Integer intMaxRango) {
        this.intMaxRango = intMaxRango;
    }
    
    public boolean isBlDefinitivo() {
        return blDefinitivo;
    }

    public void setBlDefinitivo(boolean blDefinitivo) {
        this.blDefinitivo = blDefinitivo;
    }

    public String getStrCuestionario() {
        return strCuestionario;
    }

    public void setStrCuestionario(String strCuestionario) {
        this.strCuestionario = strCuestionario;
    }
    
    public Map getMapRelaciones() {
        return mapRelaciones;
    }

    public void setMapRelaciones(Map mapRelaciones) {
        this.mapRelaciones = mapRelaciones;
    }

    public String getStrNombreEvaluado() {
        return strNombreEvaluado;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public void setStrNombreEvaluado(String strNombreEvaluado) {
        this.strNombreEvaluado = strNombreEvaluado;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }
    
}