package com.jaio360.orm;

public class Mensaje implements java.io.Serializable {

    private Integer meIdMensajePk;
    private Proyecto proyecto;
    private Integer meIdTipoMensaje;
    private String meTxAsunto;
    private byte[] meTxCuerpo;
    private byte[] meTxConvocatoriaTitulo;
    private byte[] meTxConvocatoriaParrafo;
    private byte[] meTxConvocatoriaURL;
    private byte[] meTxBienvenidaRecomendacion;
    private byte[] meTxBienvenidaConfidencialidad;
    private byte[] meTxBienvenidaAgradecimiento;
    private byte[] meTxAgradecimiento;

    public Mensaje() {
    }

    public Mensaje(Proyecto proyecto, int meIdTipoMensaje) {
        this.proyecto = proyecto;
        this.meIdTipoMensaje = meIdTipoMensaje;
    }

    public Mensaje(Proyecto proyecto, int meIdTipoMensaje, String meTxAsunto, byte[] meTxCuerpo) {
        this.proyecto = proyecto;
        this.meIdTipoMensaje = meIdTipoMensaje;
        this.meTxAsunto = meTxAsunto;
        this.meTxCuerpo = meTxCuerpo;
    }

    public Integer getMeIdMensajePk() {
        return meIdMensajePk;
    }

    public void setMeIdMensajePk(Integer meIdMensajePk) {
        this.meIdMensajePk = meIdMensajePk;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Integer getMeIdTipoMensaje() {
        return meIdTipoMensaje;
    }

    public void setMeIdTipoMensaje(Integer meIdTipoMensaje) {
        this.meIdTipoMensaje = meIdTipoMensaje;
    }

    public String getMeTxAsunto() {
        return meTxAsunto;
    }

    public void setMeTxAsunto(String meTxAsunto) {
        this.meTxAsunto = meTxAsunto;
    }

    public byte[] getMeTxCuerpo() {
        return meTxCuerpo;
    }

    public void setMeTxCuerpo(byte[] meTxCuerpo) {
        this.meTxCuerpo = meTxCuerpo;
    }

    public byte[] getMeTxConvocatoriaTitulo() {
        return meTxConvocatoriaTitulo;
    }

    public void setMeTxConvocatoriaTitulo(byte[] meTxConvocatoriaTitulo) {
        this.meTxConvocatoriaTitulo = meTxConvocatoriaTitulo;
    }

    public byte[] getMeTxConvocatoriaParrafo() {
        return meTxConvocatoriaParrafo;
    }

    public void setMeTxConvocatoriaParrafo(byte[] meTxConvocatoriaParrafo) {
        this.meTxConvocatoriaParrafo = meTxConvocatoriaParrafo;
    }

    public byte[] getMeTxConvocatoriaURL() {
        return meTxConvocatoriaURL;
    }

    public void setMeTxConvocatoriaURL(byte[] meTxConvocatoriaURL) {
        this.meTxConvocatoriaURL = meTxConvocatoriaURL;
    }

    public byte[] getMeTxBienvenidaRecomendacion() {
        return meTxBienvenidaRecomendacion;
    }

    public void setMeTxBienvenidaRecomendacion(byte[] meTxBienvenidaRecomendacion) {
        this.meTxBienvenidaRecomendacion = meTxBienvenidaRecomendacion;
    }

    public byte[] getMeTxBienvenidaConfidencialidad() {
        return meTxBienvenidaConfidencialidad;
    }

    public void setMeTxBienvenidaConfidencialidad(byte[] meTxBienvenidaConfidencialidad) {
        this.meTxBienvenidaConfidencialidad = meTxBienvenidaConfidencialidad;
    }

    public byte[] getMeTxBienvenidaAgradecimiento() {
        return meTxBienvenidaAgradecimiento;
    }

    public void setMeTxBienvenidaAgradecimiento(byte[] meTxBienvenidaAgradecimiento) {
        this.meTxBienvenidaAgradecimiento = meTxBienvenidaAgradecimiento;
    }

    public byte[] getMeTxAgradecimiento() {
        return meTxAgradecimiento;
    }

    public void setMeTxAgradecimiento(byte[] meTxAgradecimiento) {
        this.meTxAgradecimiento = meTxAgradecimiento;
    }

}
