package com.jaio360.domain;

import com.jaio360.orm.Destinatarios;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NotificacionBean implements Serializable {

    private Integer noIdNotificacionPk;
    private Date noFeCreacion;
    private Date noFeEnvio;
    private String noTxEstado;
    private String noTxAsunto;
    private String noTxError;
    private List<String> lstDestinatarios;

    public List<String> getLstDestinatarios() {
        return lstDestinatarios;
    }

    public void setLstDestinatarios(List<String> lstDestinatarios) {
        this.lstDestinatarios = lstDestinatarios;
    }

    public Integer getNoIdNotificacionPk() {
        return noIdNotificacionPk;
    }

    public void setNoIdNotificacionPk(Integer noIdNotificacionPk) {
        this.noIdNotificacionPk = noIdNotificacionPk;
    }

    public Date getNoFeCreacion() {
        return noFeCreacion;
    }

    public void setNoFeCreacion(Date noFeCreacion) {
        this.noFeCreacion = noFeCreacion;
    }

    public Date getNoFeEnvio() {
        return noFeEnvio;
    }

    public void setNoFeEnvio(Date noFeEnvio) {
        this.noFeEnvio = noFeEnvio;
    }

    public String getNoTxEstado() {
        return noTxEstado;
    }

    public void setNoTxEstado(String noTxEstado) {
        this.noTxEstado = noTxEstado;
    }

    public String getNoTxAsunto() {
        return noTxAsunto;
    }

    public void setNoTxAsunto(String noTxAsunto) {
        this.noTxAsunto = noTxAsunto;
    }

    public String getNoTxError() {
        return noTxError;
    }

    public void setNoTxError(String noTxError) {
        this.noTxError = noTxError;
    }

}
