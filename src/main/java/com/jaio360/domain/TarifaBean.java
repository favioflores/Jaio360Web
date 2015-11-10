package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class TarifaBean implements Serializable{
    
    private Integer taIdTarifaPk;
    private String taTxDescripcion;
    private BigDecimal taDePrecio;
    private String taFeCreacion;
    private String taIdEstado;
    private String taTxEstado;
    private Integer taNroContratoTotal;
    private Integer taNroContratoUso;

    public Integer getTaNroContratoTotal() {
        return taNroContratoTotal;
    }

    public void setTaNroContratoTotal(Integer taNroContratoTotal) {
        this.taNroContratoTotal = taNroContratoTotal;
    }

    public Integer getTaNroContratoUso() {
        return taNroContratoUso;
    }

    public void setTaNroContratoUso(Integer taNroContratoUso) {
        this.taNroContratoUso = taNroContratoUso;
    }
    
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

    public String getTaFeCreacion() {
        return taFeCreacion;
    }

    public void setTaFeCreacion(String taFeCreacion) {
        this.taFeCreacion = taFeCreacion;
    }

    public String getTaIdEstado() {
        return taIdEstado;
    }

    public void setTaIdEstado(String taIdEstado) {
        this.taIdEstado = taIdEstado;
    }

    public String getTaTxEstado() {
        return taTxEstado;
    }

    public void setTaTxEstado(String taTxEstado) {
        this.taTxEstado = taTxEstado;
    }
    
}


