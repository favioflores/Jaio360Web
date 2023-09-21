package com.jaio360.domain;

import java.io.Serializable;
import java.util.Date;

public class MovimientoBean implements Serializable {

    private Integer intIdMovimiento;
    private String strDescMovimiento;
    private Date dtCreacion;
    private Integer intCantidad;

    public Integer getIntIdMovimiento() {
        return intIdMovimiento;
    }

    public void setIntIdMovimiento(Integer intIdMovimiento) {
        this.intIdMovimiento = intIdMovimiento;
    }

    public String getStrDescMovimiento() {
        return strDescMovimiento;
    }

    public void setStrDescMovimiento(String strDescMovimiento) {
        this.strDescMovimiento = strDescMovimiento;
    }

    public Date getDtCreacion() {
        return dtCreacion;
    }

    public void setDtCreacion(Date dtCreacion) {
        this.dtCreacion = dtCreacion;
    }

    public Integer getIntCantidad() {
        return intCantidad;
    }

    public void setIntCantidad(Integer intCantidad) {
        this.intCantidad = intCantidad;
    }

}
