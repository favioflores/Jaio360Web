package com.jaio360.orm;

import java.util.HashSet;
import java.util.Set;





public class Participante implements java.io.Serializable {

    private Integer paIdParticipantePk;
    private Proyecto proyecto;
    private String paTxDescripcion;
    private String paTxCorreo;
    private String paTxNombreCargo;
    private Boolean paInRedCargada;
    private Boolean paInRedVerificada;
    private Integer paIdTipoParticipante;
    private Boolean paInAutoevaluar;
    private Integer paIdEstado;
    private String paTxSexo;
    private Integer paNrEdad;
    private Integer paNrTiempoTrabajo;
    private String paTxOcupacion;
    private String paTxAreaNegocio;
    private Boolean paInAnalizado;
    private String paTxImgUrl;

    private Set relacionParticipantes = new HashSet(0);
    private Set cuestionarioEvaluados = new HashSet(0);

    public Participante() {
    }

    public Participante(Proyecto proyecto, String paTxDescripcion, String paTxCorreo, String paTxNombreCargo, int paIdTipoParticipante, int paIdEstado) {
        this.proyecto = proyecto;
        this.paTxDescripcion = paTxDescripcion;
        this.paTxCorreo = paTxCorreo;
        this.paTxNombreCargo = paTxNombreCargo;
        this.paIdTipoParticipante = paIdTipoParticipante;
        this.paIdEstado = paIdEstado;
    }

    public Participante(Proyecto proyecto, String paTxDescripcion, String paTxCorreo, String paTxNombreCargo, Boolean paInRedCargada, Boolean paInRedVerificada, int paIdTipoParticipante, Boolean paInAutoevaluar, int paIdEstado, Set relacionParticipantes, Set cuestionarioEvaluados) {
        this.proyecto = proyecto;
        this.paTxDescripcion = paTxDescripcion;
        this.paTxCorreo = paTxCorreo;
        this.paTxNombreCargo = paTxNombreCargo;
        this.paInRedCargada = paInRedCargada;
        this.paInRedVerificada = paInRedVerificada;
        this.paIdTipoParticipante = paIdTipoParticipante;
        this.paInAutoevaluar = paInAutoevaluar;
        this.paIdEstado = paIdEstado;
        this.relacionParticipantes = relacionParticipantes;
        this.cuestionarioEvaluados = cuestionarioEvaluados;
    }

    public Integer getPaIdParticipantePk() {
        return paIdParticipantePk;
    }

    public void setPaIdParticipantePk(Integer paIdParticipantePk) {
        this.paIdParticipantePk = paIdParticipantePk;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getPaTxDescripcion() {
        return paTxDescripcion;
    }

    public void setPaTxDescripcion(String paTxDescripcion) {
        this.paTxDescripcion = paTxDescripcion;
    }

    public String getPaTxCorreo() {
        return paTxCorreo;
    }

    public void setPaTxCorreo(String paTxCorreo) {
        this.paTxCorreo = paTxCorreo;
    }

    public String getPaTxNombreCargo() {
        return paTxNombreCargo;
    }

    public void setPaTxNombreCargo(String paTxNombreCargo) {
        this.paTxNombreCargo = paTxNombreCargo;
    }

    public Boolean getPaInRedCargada() {
        return paInRedCargada;
    }

    public void setPaInRedCargada(Boolean paInRedCargada) {
        this.paInRedCargada = paInRedCargada;
    }

    public Boolean getPaInRedVerificada() {
        return paInRedVerificada;
    }

    public void setPaInRedVerificada(Boolean paInRedVerificada) {
        this.paInRedVerificada = paInRedVerificada;
    }

    public Integer getPaIdTipoParticipante() {
        return paIdTipoParticipante;
    }

    public void setPaIdTipoParticipante(Integer paIdTipoParticipante) {
        this.paIdTipoParticipante = paIdTipoParticipante;
    }

    public Boolean getPaInAutoevaluar() {
        return paInAutoevaluar;
    }

    public void setPaInAutoevaluar(Boolean paInAutoevaluar) {
        this.paInAutoevaluar = paInAutoevaluar;
    }

    public Integer getPaIdEstado() {
        return paIdEstado;
    }

    public void setPaIdEstado(Integer paIdEstado) {
        this.paIdEstado = paIdEstado;
    }

    public String getPaTxSexo() {
        return paTxSexo;
    }

    public void setPaTxSexo(String paTxSexo) {
        this.paTxSexo = paTxSexo;
    }

    public Integer getPaNrEdad() {
        return paNrEdad;
    }

    public void setPaNrEdad(Integer paNrEdad) {
        this.paNrEdad = paNrEdad;
    }

    public Integer getPaNrTiempoTrabajo() {
        return paNrTiempoTrabajo;
    }

    public void setPaNrTiempoTrabajo(Integer paNrTiempoTrabajo) {
        this.paNrTiempoTrabajo = paNrTiempoTrabajo;
    }

    public String getPaTxOcupacion() {
        return paTxOcupacion;
    }

    public void setPaTxOcupacion(String paTxOcupacion) {
        this.paTxOcupacion = paTxOcupacion;
    }

    public String getPaTxAreaNegocio() {
        return paTxAreaNegocio;
    }

    public void setPaTxAreaNegocio(String paTxAreaNegocio) {
        this.paTxAreaNegocio = paTxAreaNegocio;
    }

    public Boolean getPaInAnalizado() {
        return paInAnalizado;
    }

    public void setPaInAnalizado(Boolean paInAnalizado) {
        this.paInAnalizado = paInAnalizado;
    }

    public String getPaTxImgUrl() {
        return paTxImgUrl;
    }

    public void setPaTxImgUrl(String paTxImgUrl) {
        this.paTxImgUrl = paTxImgUrl;
    }

    public Set getRelacionParticipantes() {
        return relacionParticipantes;
    }

    public void setRelacionParticipantes(Set relacionParticipantes) {
        this.relacionParticipantes = relacionParticipantes;
    }

    public Set getCuestionarioEvaluados() {
        return cuestionarioEvaluados;
    }

    public void setCuestionarioEvaluados(Set cuestionarioEvaluados) {
        this.cuestionarioEvaluados = cuestionarioEvaluados;
    }

    
}
