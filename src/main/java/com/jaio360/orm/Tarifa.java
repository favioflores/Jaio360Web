package com.jaio360.orm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Tarifa implements java.io.Serializable {

    private Integer taIdTarifaPk;
    private String taTxDescripcion;
    private BigDecimal taDePrecio;
    private Integer taIdTipoTarifa;
    private Date taFeCreacion;
    private Integer taIdEstado;
    private String taTxPrefijo;
    private Set contratos = new HashSet(0);

    public Integer getTaIdTarifaPk() {
        return taIdTarifaPk;
    }

    public void setTaIdTarifaPk(Integer taIdTarifaPk) {
        this.taIdTarifaPk = taIdTarifaPk;
    }

    public String getTaTxDescripcion() {
        return taTxDescripcion;
    }

    public void setTaTxDescripcion(String taTxDescripcion) {
        this.taTxDescripcion = taTxDescripcion;
    }

    public BigDecimal getTaDePrecio() {
        return taDePrecio;
    }

    public void setTaDePrecio(BigDecimal taDePrecio) {
        this.taDePrecio = taDePrecio;
    }

    public Integer getTaIdTipoTarifa() {
        return taIdTipoTarifa;
    }

    public void setTaIdTipoTarifa(Integer taIdTipoTarifa) {
        this.taIdTipoTarifa = taIdTipoTarifa;
    }

    public Date getTaFeCreacion() {
        return taFeCreacion;
    }

    public void setTaFeCreacion(Date taFeCreacion) {
        this.taFeCreacion = taFeCreacion;
    }

    public Integer getTaIdEstado() {
        return taIdEstado;
    }

    public void setTaIdEstado(Integer taIdEstado) {
        this.taIdEstado = taIdEstado;
    }

    public String getTaTxPrefijo() {
        return taTxPrefijo;
    }

    public void setTaTxPrefijo(String taTxPrefijo) {
        this.taTxPrefijo = taTxPrefijo;
    }

    public Set getContratos() {
        return contratos;
    }

    public void setContratos(Set contratos) {
        this.contratos = contratos;
    }

    public Tarifa() {
    }

    public Tarifa(String taTxDescripcion, BigDecimal taDePrecio, Date taFeCreacion, int taIdEstado) {
        this.taTxDescripcion = taTxDescripcion;
        this.taDePrecio = taDePrecio;
        this.taFeCreacion = taFeCreacion;
        this.taIdEstado = taIdEstado;
    }

    public Tarifa(String taTxDescripcion, BigDecimal taDePrecio, Date taFeCreacion, int taIdEstado, Set contratos) {
        this.taTxDescripcion = taTxDescripcion;
        this.taDePrecio = taDePrecio;
        this.taFeCreacion = taFeCreacion;
        this.taIdEstado = taIdEstado;
        this.contratos = contratos;
    }

}
