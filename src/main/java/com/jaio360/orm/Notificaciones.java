package com.jaio360.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Notificaciones implements java.io.Serializable {

    private Integer noIdNotificacionPk;
    private int noIdRefProceso;
    private int noIdTipoProceso;
    private Date noFeCreacion;
    private Date noFeEnvio;
    private int noIdEstado;
    private String noTxAsunto;
    private byte[] noTxMensaje;
    private String noTxError;
    private String noAdjunto;
    private Set destinatarioses = new HashSet(0);

    public Notificaciones() {
    }

    public Notificaciones(int noIdRefProceso, int noIdTipoProceso, Date noFeCreacion, int noIdEstado, String noTxAsunto, byte[] noTxMensaje) {
        this.noIdRefProceso = noIdRefProceso;
        this.noIdTipoProceso = noIdTipoProceso;
        this.noFeCreacion = noFeCreacion;
        this.noIdEstado = noIdEstado;
        this.noTxAsunto = noTxAsunto;
        this.noTxMensaje = noTxMensaje;
    }

    public Notificaciones(int noIdRefProceso, int noIdTipoProceso, Date noFeCreacion, Date noFeEnvio, int noIdEstado, String noTxAsunto, byte[] noTxMensaje, String noAdjunto, Set destinatarioses) {
        this.noIdRefProceso = noIdRefProceso;
        this.noIdTipoProceso = noIdTipoProceso;
        this.noFeCreacion = noFeCreacion;
        this.noFeEnvio = noFeEnvio;
        this.noIdEstado = noIdEstado;
        this.noTxAsunto = noTxAsunto;
        this.noTxMensaje = noTxMensaje;
        this.noAdjunto = noAdjunto;
        this.destinatarioses = destinatarioses;
    }

    public Integer getNoIdNotificacionPk() {
        return noIdNotificacionPk;
    }

    public void setNoIdNotificacionPk(Integer noIdNotificacionPk) {
        this.noIdNotificacionPk = noIdNotificacionPk;
    }

    public int getNoIdRefProceso() {
        return noIdRefProceso;
    }

    public void setNoIdRefProceso(int noIdRefProceso) {
        this.noIdRefProceso = noIdRefProceso;
    }

    public int getNoIdTipoProceso() {
        return noIdTipoProceso;
    }

    public void setNoIdTipoProceso(int noIdTipoProceso) {
        this.noIdTipoProceso = noIdTipoProceso;
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

    public int getNoIdEstado() {
        return noIdEstado;
    }

    public void setNoIdEstado(int noIdEstado) {
        this.noIdEstado = noIdEstado;
    }

    public String getNoTxAsunto() {
        return noTxAsunto;
    }

    public void setNoTxAsunto(String noTxAsunto) {
        this.noTxAsunto = noTxAsunto;
    }

    public byte[] getNoTxMensaje() {
        return noTxMensaje;
    }

    public void setNoTxMensaje(byte[] noTxMensaje) {
        this.noTxMensaje = noTxMensaje;
    }

    public String getNoTxError() {
        return noTxError;
    }

    public void setNoTxError(String noTxError) {
        this.noTxError = noTxError;
    }

    public String getNoAdjunto() {
        return noAdjunto;
    }

    public void setNoAdjunto(String noAdjunto) {
        this.noAdjunto = noAdjunto;
    }

    public Set getDestinatarioses() {
        return destinatarioses;
    }

    public void setDestinatarioses(Set destinatarioses) {
        this.destinatarioses = destinatarioses;
    }

}
