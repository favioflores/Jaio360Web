/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.domain;

import java.io.Serializable;

/**
 *
 * @author Favio
 */
public class ModeloContenido implements Serializable{

    private Integer intModeloPk;
    private String strDescModelo;
    private String imgUrl;
    private String strDescripcionUso;
    private String strTipoArchivo;

    public ModeloContenido(Integer intModeloPk, String strDescModelo, String imgUrl, String strDescripcionUso, String strTipoArchivo) {
        this.intModeloPk = intModeloPk;
        this.strDescModelo = strDescModelo;
        this.imgUrl = imgUrl;
        this.strDescripcionUso = strDescripcionUso;
        this.strTipoArchivo = strTipoArchivo;
    }

    public Integer getIntModeloPk() {
        return intModeloPk;
    }

    public void setIntModeloPk(Integer intModeloPk) {
        this.intModeloPk = intModeloPk;
    }

    public String getStrDescModelo() {
        return strDescModelo;
    }

    public void setStrDescModelo(String strDescModelo) {
        this.strDescModelo = strDescModelo;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStrDescripcionUso() {
        return strDescripcionUso;
    }

    public void setStrDescripcionUso(String strDescripcionUso) {
        this.strDescripcionUso = strDescripcionUso;
    }

    public String getStrTipoArchivo() {
        return strTipoArchivo;
    }

    public void setStrTipoArchivo(String strTipoArchivo) {
        this.strTipoArchivo = strTipoArchivo;
    }
    
}

