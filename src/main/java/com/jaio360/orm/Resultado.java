package com.jaio360.orm;

public class Resultado implements java.io.Serializable {

    private Integer reIdResultadoPk;
    private Componente componente;
    private DetalleMetrica detalleMetrica;
    private Proyecto proyecto;
    private String reTxComentario;

    private Integer reIdRelacionFk;
    private Integer reIdParticipanteFk;
    private Integer paIdParticipanteFk;
    private Integer coIdComponenteRefFk;

    public Integer getReIdResultadoPk() {
        return reIdResultadoPk;
    }

    public void setReIdResultadoPk(Integer reIdResultadoPk) {
        this.reIdResultadoPk = reIdResultadoPk;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public DetalleMetrica getDetalleMetrica() {
        return detalleMetrica;
    }

    public void setDetalleMetrica(DetalleMetrica detalleMetrica) {
        this.detalleMetrica = detalleMetrica;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getReTxComentario() {
        return reTxComentario;
    }

    public void setReTxComentario(String reTxComentario) {
        this.reTxComentario = reTxComentario;
    }

    public Integer getReIdRelacionFk() {
        return reIdRelacionFk;
    }

    public void setReIdRelacionFk(Integer reIdRelacionFk) {
        this.reIdRelacionFk = reIdRelacionFk;
    }

    public Integer getReIdParticipanteFk() {
        return reIdParticipanteFk;
    }

    public void setReIdParticipanteFk(Integer reIdParticipanteFk) {
        this.reIdParticipanteFk = reIdParticipanteFk;
    }

    public Integer getPaIdParticipanteFk() {
        return paIdParticipanteFk;
    }

    public void setPaIdParticipanteFk(Integer paIdParticipanteFk) {
        this.paIdParticipanteFk = paIdParticipanteFk;
    }

    public Integer getCoIdComponenteRefFk() {
        return coIdComponenteRefFk;
    }

    public void setCoIdComponenteRefFk(Integer coIdComponenteRefFk) {
        this.coIdComponenteRefFk = coIdComponenteRefFk;
    }

    
    public Resultado() {
    }

    public Resultado(RelacionParticipante relacionParticipante, Componente componente, Proyecto proyecto) {
        //this.relacionParticipante = relacionParticipante;
        this.componente = componente;
        this.proyecto = proyecto;
    }

    public Resultado(RelacionParticipante relacionParticipante, Componente componente, DetalleMetrica detalleMetrica, Proyecto proyecto, String reTxComentario) {
        //this.relacionParticipante = relacionParticipante;
        this.componente = componente;
        this.detalleMetrica = detalleMetrica;
        this.proyecto = proyecto;
        this.reTxComentario = reTxComentario;
    }

}
