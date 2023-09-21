package com.jaio360.orm;

public class Destinatarios implements java.io.Serializable {

    private Integer deIdDestinatarioPk;
    private Notificaciones notificaciones;
    private String deTxMail;
    private String deTxMailCc;

    public Destinatarios() {
    }

    public Destinatarios(Notificaciones notificaciones, String deTxMail, String deTxMailCc) {
        this.notificaciones = notificaciones;
        this.deTxMail = deTxMail;
        this.deTxMailCc = deTxMailCc;
    }

    public Integer getDeIdDestinatarioPk() {
        return deIdDestinatarioPk;
    }

    public void setDeIdDestinatarioPk(Integer deIdDestinatarioPk) {
        this.deIdDestinatarioPk = deIdDestinatarioPk;
    }

    public Notificaciones getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(Notificaciones notificaciones) {
        this.notificaciones = notificaciones;
    }

    public String getDeTxMail() {
        return deTxMail;
    }

    public void setDeTxMail(String deTxMail) {
        this.deTxMail = deTxMail;
    }

    public String getDeTxMailCc() {
        return deTxMailCc;
    }

    public void setDeTxMailCc(String deTxMailCc) {
        this.deTxMailCc = deTxMailCc;
    }

}
