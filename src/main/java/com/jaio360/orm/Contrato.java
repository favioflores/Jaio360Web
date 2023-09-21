package com.jaio360.orm;

import java.util.Date;

public class Contrato implements java.io.Serializable {

    private Integer coIdContratoPk;
    private Usuario usuario;
    private Tarifa tarifa;
    private Date coFeCreacion;
    private Integer coNuLicenciaTotal;
    private Integer coIdEstado;

    public Contrato() {
    }

    public Integer getCoIdContratoPk() {
        return coIdContratoPk;
    }

    public void setCoIdContratoPk(Integer coIdContratoPk) {
        this.coIdContratoPk = coIdContratoPk;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public Date getCoFeCreacion() {
        return coFeCreacion;
    }

    public void setCoFeCreacion(Date coFeCreacion) {
        this.coFeCreacion = coFeCreacion;
    }

    public Integer getCoNuLicenciaTotal() {
        return coNuLicenciaTotal;
    }

    public void setCoNuLicenciaTotal(Integer coNuLicenciaTotal) {
        this.coNuLicenciaTotal = coNuLicenciaTotal;
    }

    public Integer getCoIdEstado() {
        return coIdEstado;
    }

    public void setCoIdEstado(Integer coIdEstado) {
        this.coIdEstado = coIdEstado;
    }

}
