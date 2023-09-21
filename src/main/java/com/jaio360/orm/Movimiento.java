package com.jaio360.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Movimiento implements java.io.Serializable {

    private Integer moIdMovimientoPk;
    private TipoMovimiento tipoMovimiento;
    private Usuario usuario;
    private Date moFeCreacion;
    private Integer moInCantidad;
    private Set referenciaMovimientos = new HashSet(0);

    public Integer getMoIdMovimientoPk() {
        return moIdMovimientoPk;
    }

    public void setMoIdMovimientoPk(Integer moIdMovimientoPk) {
        this.moIdMovimientoPk = moIdMovimientoPk;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getMoFeCreacion() {
        return moFeCreacion;
    }

    public void setMoFeCreacion(Date moFeCreacion) {
        this.moFeCreacion = moFeCreacion;
    }

    public Integer getMoInCantidad() {
        return moInCantidad;
    }

    public void setMoInCantidad(Integer moInCantidad) {
        this.moInCantidad = moInCantidad;
    }

    public Set getReferenciaMovimientos() {
        return referenciaMovimientos;
    }

    public void setReferenciaMovimientos(Set referenciaMovimientos) {
        this.referenciaMovimientos = referenciaMovimientos;
    }

    
}
