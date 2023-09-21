/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.domain;

import java.io.Serializable;

public class EstadoPasosProyecto implements Serializable {

    private String strPaso;
    private String strActividad;
    private String strEstado;
    private String strUrl;

    public EstadoPasosProyecto(String strPaso, String strActividad, String strEstado, String strUrl) {
        this.strPaso = strPaso;
        this.strActividad = strActividad;
        this.strEstado = strEstado;
        this.strUrl = strUrl;
    }

    public String getStrPaso() {
        return strPaso;
    }

    public void setStrPaso(String strPaso) {
        this.strPaso = strPaso;
    }

    public String getStrActividad() {
        return strActividad;
    }

    public void setStrActividad(String strActividad) {
        this.strActividad = strActividad;
    }

    public String getStrEstado() {
        return strEstado;
    }

    public void setStrEstado(String strEstado) {
        this.strEstado = strEstado;
    }

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

}
