package com.jaio360.domain;

import java.io.Serializable;

public class Documento implements Serializable{
    
    private Integer idTipoDocumento;
    private String strDescDocumento;

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getStrDescDocumento() {
        return strDescDocumento;
    }

    public void setStrDescDocumento(String strDescDocumento) {
        this.strDescDocumento = strDescDocumento;
    }
           
}