package com.jaio360.domain;

import java.io.Serializable;

public class BibliotecaInfo implements Serializable {

    private String strNroElemento;
    private String strDescMetodologia;
    private String strDescElemento;
    private String strTipoElemento;
    private String strExtension;
    private String strCuestionarios;
    private String strProyectos;
    private String strFechaProyecto;

    public String getStrNroElemento() {
        return strNroElemento;
    }

    public void setStrNroElemento(String strNroElemento) {
        this.strNroElemento = strNroElemento;
    }

    public String getStrDescMetodologia() {
        return strDescMetodologia;
    }

    public void setStrDescMetodologia(String strDescMetodologia) {
        this.strDescMetodologia = strDescMetodologia;
    }

    public String getStrDescElemento() {
        return strDescElemento;
    }

    public void setStrDescElemento(String strDescElemento) {
        this.strDescElemento = strDescElemento;
    }

    public String getStrTipoElemento() {
        return strTipoElemento;
    }

    public void setStrTipoElemento(String strTipoElemento) {
        this.strTipoElemento = strTipoElemento;
    }

    public String getStrExtension() {
        return strExtension;
    }

    public void setStrExtension(String strExtension) {
        this.strExtension = strExtension;
    }

    public String getStrCuestionarios() {
        return strCuestionarios;
    }

    public void setStrCuestionarios(String strCuestionarios) {
        this.strCuestionarios = strCuestionarios;
    }

    public String getStrProyectos() {
        return strProyectos;
    }

    public void setStrProyectos(String strProyectos) {
        this.strProyectos = strProyectos;
    }

    public String getStrFechaProyecto() {
        return strFechaProyecto;
    }

    public void setStrFechaProyecto(String strFechaProyecto) {
        this.strFechaProyecto = strFechaProyecto;
    }

}
