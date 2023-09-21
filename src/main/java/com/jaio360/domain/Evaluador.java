package com.jaio360.domain;

import java.io.Serializable;

public class Evaluador implements Serializable {

    private Integer reIdParticipantePk;
    private String reTxDescripcion;
    private String reTxNombreCargo;
    private String reTxCorreo;
    private Integer reIdTipoParticipante;
    private Integer reIdEstado;
    private String reStrEstado;
    private Integer intCorrelativo;
    private String strCorrectoMasivo;
    private String strObservacionMasivo;

    private String reTxSexo;
    private Integer reNrEdad;
    private Integer reNrTiempoTrabajo;
    private String reTxOcupacion;
    private String reTxAreaNegocio;

    public Integer getReIdParticipantePk() {
        return reIdParticipantePk;
    }

    public void setReIdParticipantePk(Integer reIdParticipantePk) {
        this.reIdParticipantePk = reIdParticipantePk;
    }

    public String getReTxDescripcion() {
        return reTxDescripcion;
    }

    public void setReTxDescripcion(String reTxDescripcion) {
        this.reTxDescripcion = reTxDescripcion;
    }

    public String getReTxNombreCargo() {
        return reTxNombreCargo;
    }

    public void setReTxNombreCargo(String reTxNombreCargo) {
        this.reTxNombreCargo = reTxNombreCargo;
    }

    public String getReTxCorreo() {
        return reTxCorreo;
    }

    public void setReTxCorreo(String reTxCorreo) {
        this.reTxCorreo = reTxCorreo;
    }

    public Integer getReIdTipoParticipante() {
        return reIdTipoParticipante;
    }

    public void setReIdTipoParticipante(Integer reIdTipoParticipante) {
        this.reIdTipoParticipante = reIdTipoParticipante;
    }

    public Integer getReIdEstado() {
        return reIdEstado;
    }

    public void setReIdEstado(Integer reIdEstado) {
        this.reIdEstado = reIdEstado;
    }

    public String getReStrEstado() {
        return reStrEstado;
    }

    public void setReStrEstado(String reStrEstado) {
        this.reStrEstado = reStrEstado;
    }

    public Integer getIntCorrelativo() {
        return intCorrelativo;
    }

    public void setIntCorrelativo(Integer intCorrelativo) {
        this.intCorrelativo = intCorrelativo;
    }

    public String getStrCorrectoMasivo() {
        return strCorrectoMasivo;
    }

    public void setStrCorrectoMasivo(String strCorrectoMasivo) {
        this.strCorrectoMasivo = strCorrectoMasivo;
    }

    public String getStrObservacionMasivo() {
        return strObservacionMasivo;
    }

    public void setStrObservacionMasivo(String strObservacionMasivo) {
        this.strObservacionMasivo = strObservacionMasivo;
    }

    public String getReTxSexo() {
        return reTxSexo;
    }

    public void setReTxSexo(String reTxSexo) {
        this.reTxSexo = reTxSexo;
    }

    public Integer getReNrEdad() {
        return reNrEdad;
    }

    public void setReNrEdad(Integer reNrEdad) {
        this.reNrEdad = reNrEdad;
    }

    public Integer getReNrTiempoTrabajo() {
        return reNrTiempoTrabajo;
    }

    public void setReNrTiempoTrabajo(Integer reNrTiempoTrabajo) {
        this.reNrTiempoTrabajo = reNrTiempoTrabajo;
    }

    public String getReTxOcupacion() {
        return reTxOcupacion;
    }

    public void setReTxOcupacion(String reTxOcupacion) {
        this.reTxOcupacion = reTxOcupacion;
    }

    public String getReTxAreaNegocio() {
        return reTxAreaNegocio;
    }

    public void setReTxAreaNegocio(String reTxAreaNegocio) {
        this.reTxAreaNegocio = reTxAreaNegocio;
    }

}
