package com.jaio360.orm;
// Generated 21/10/2014 08:38:36 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Contrato generated by hbm2java
 */
public class Contrato  implements java.io.Serializable {


     private Integer coIdContratoPk;
     private Usuario usuario;
     private Tarifa tarifa;
     private Date coFeCreacion;
     private Integer coNuLicenciaTotal;
     private Integer coIdEstado;

    public Contrato() {
    }
   
    public Integer getCoIdContratoPk() {
        return this.coIdContratoPk;
    }
    
    public void setCoIdContratoPk(Integer coIdContratoPk) {
        this.coIdContratoPk = coIdContratoPk;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Tarifa getTarifa() {
        return this.tarifa;
    }
    
    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }
    public Date getCoFeCreacion() {
        return this.coFeCreacion;
    }
    
    public void setCoFeCreacion(Date coFeCreacion) {
        this.coFeCreacion = coFeCreacion;
    }
    public Integer getCoNuLicenciaTotal() {
        return this.coNuLicenciaTotal;
    }
    
    public void setCoNuLicenciaTotal(Integer coNuLicenciaTotal) {
        this.coNuLicenciaTotal = coNuLicenciaTotal;
    }

    public Integer getCoIdEstado() {
        return this.coIdEstado;
    }
    
    public void setCoIdEstado(Integer coIdEstado) {
        this.coIdEstado = coIdEstado;
    }

}


