package com.jaio360.orm;

import java.util.Date;

public class LastUpdate implements java.io.Serializable {

    private Integer luIdUpdatePk;
    private String luTxTipo;
    private Date luDtFecha;
    private String luTxDescripcion;

    public LastUpdate() {
    }

    public Integer getLuIdUpdatePk() {
        return luIdUpdatePk;
    }

    public void setLuIdUpdatePk(Integer luIdUpdatePk) {
        this.luIdUpdatePk = luIdUpdatePk;
    }

    public String getLuTxTipo() {
        return luTxTipo;
    }

    public void setLuTxTipo(String luTxTipo) {
        this.luTxTipo = luTxTipo;
    }

    public Date getLuDtFecha() {
        return luDtFecha;
    }

    public void setLuDtFecha(Date luDtFecha) {
        this.luDtFecha = luDtFecha;
    }

    public String getLuTxDescripcion() {
        return luTxDescripcion;
    }

    public void setLuTxDescripcion(String luTxDescripcion) {
        this.luTxDescripcion = luTxDescripcion;
    }

}
