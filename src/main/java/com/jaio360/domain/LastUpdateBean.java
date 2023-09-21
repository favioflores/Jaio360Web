package com.jaio360.domain;

import java.util.Date;

public class LastUpdateBean implements java.io.Serializable {

    private Integer luIdUpdatePk;
    private String luTxTipo;
    private Date luDtFecha;
    private String luTxDescripcion;
    private String strColor;
    private String strIcon;

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

    public String getStrColor() {
        return strColor;
    }

    public void setStrColor(String strColor) {
        this.strColor = strColor;
    }

    public String getStrIcon() {
        return strIcon;
    }

    public void setStrIcon(String strIcon) {
        this.strIcon = strIcon;
    }

}
