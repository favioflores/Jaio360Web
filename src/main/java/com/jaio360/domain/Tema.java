package com.jaio360.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tema implements Serializable {

    private Integer Id;
    private String strNombre;
    private String strImagen;

    public Tema(Integer Id, String strNombre, String strImagen){
    this.Id = Id;
    this.strNombre = strNombre;
    this.strImagen = strImagen;
    }
    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrImagen() {
        return strImagen;
    }

    public void setStrImagen(String strImagen) {
        this.strImagen = strImagen;
    }
    
}