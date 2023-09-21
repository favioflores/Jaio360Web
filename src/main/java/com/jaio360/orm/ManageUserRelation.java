package com.jaio360.orm;

import java.util.Date;

public class ManageUserRelation implements java.io.Serializable {

    private Integer usIdCuentaManagerPk;
    private Usuario usuario;
    private Boolean maIsVerified;
    private Date maFeRegistro;
    private Date maFeVerificacion;
    private Date maFeVerificationExpired;
    private String maHashLinkVerificacion;

    public ManageUserRelation() {
    }

    public Integer getUsIdCuentaManagerPk() {
        return usIdCuentaManagerPk;
    }

    public void setUsIdCuentaManagerPk(Integer usIdCuentaManagerPk) {
        this.usIdCuentaManagerPk = usIdCuentaManagerPk;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getMaIsVerified() {
        return maIsVerified;
    }

    public void setMaIsVerified(Boolean maIsVerified) {
        this.maIsVerified = maIsVerified;
    }

    public Date getMaFeRegistro() {
        return maFeRegistro;
    }

    public void setMaFeRegistro(Date maFeRegistro) {
        this.maFeRegistro = maFeRegistro;
    }

    public Date getMaFeVerificacion() {
        return maFeVerificacion;
    }

    public void setMaFeVerificacion(Date maFeVerificacion) {
        this.maFeVerificacion = maFeVerificacion;
    }

    public Date getMaFeVerificationExpired() {
        return maFeVerificationExpired;
    }

    public void setMaFeVerificationExpired(Date maFeVerificationExpired) {
        this.maFeVerificationExpired = maFeVerificationExpired;
    }

    public String getMaHashLinkVerificacion() {
        return maHashLinkVerificacion;
    }

    public void setMaHashLinkVerificacion(String maHashLinkVerificacion) {
        this.maHashLinkVerificacion = maHashLinkVerificacion;
    }

}
