package com.jaio360.orm;

import java.util.HashSet;
import java.util.Set;

public class TipoMovimiento implements java.io.Serializable {

    private Integer tmIdTipoMovPk;
    private String tmTxDescripcion;

    private Set movimientos = new HashSet(0);

    public Integer getTmIdTipoMovPk() {
        return tmIdTipoMovPk;
    }

    public void setTmIdTipoMovPk(Integer tmIdTipoMovPk) {
        this.tmIdTipoMovPk = tmIdTipoMovPk;
    }

    public String getTmTxDescripcion() {
        return tmTxDescripcion;
    }

    public void setTmTxDescripcion(String tmTxDescripcion) {
        this.tmTxDescripcion = tmTxDescripcion;
    }

    public Set getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Set movimientos) {
        this.movimientos = movimientos;
    }

    public TipoMovimiento() {
    }

    public TipoMovimiento(Integer tmIdTipoMovPk) {
        this.tmIdTipoMovPk = tmIdTipoMovPk;
    }

}
