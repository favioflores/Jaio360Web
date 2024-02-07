package com.jaio360.domain;

import java.io.Serializable;

public class ComentarioBean implements Serializable {

    private Integer id;
    private String strDescripcion;
    private String strRespuesta;

    public ComentarioBean() {

    }

    public ComentarioBean(Integer id, String strDescripcion, String strRespuesta) {
        this.id = id;
        this.strDescripcion = strDescripcion;
        this.strRespuesta = strRespuesta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrRespuesta() {
        return strRespuesta;
    }

    public void setStrRespuesta(String strRespuesta) {
        this.strRespuesta = strRespuesta;
    }

}
