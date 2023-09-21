package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Evaluado implements Serializable {

    private Integer paIdParticipantePk;
    private String paTxDescripcion;
    private String paTxCorreo;
    private String paTxNombreCargo;
    private Boolean paInRedCargada;
    private Boolean paInRedVerificada;
    private Integer paIdTipoParticipante;
    private Boolean paInAutoevaluar;
    private Integer paIdEstado;
    private String paStrEstado;
    private BigDecimal bdPorcentajeAvance;
    private Boolean boCheckFilterSeg;
    private String strDescCuestionario;
    private String strCorrectoMasivo;
    private String strObservacionMasivo;
    private Integer intCantidadRed;
    private Boolean blManual;
    private Boolean blEnvioCorreo;
    private Boolean blAnalizado;
    private Integer inAnalizado;

    private String paTxSexo;
    private Integer paNrEdad;
    private Integer paNrTiempoTrabajo;
    private String paTxOcupacion;
    private String paTxAreaNegocio;
    private String paTxImgUrl;

    private Integer intNumberEvaluators;
    private Integer intNumberEvaluationFinished;

    public Integer getPaIdParticipantePk() {
        return paIdParticipantePk;
    }

    public void setPaIdParticipantePk(Integer paIdParticipantePk) {
        this.paIdParticipantePk = paIdParticipantePk;
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

    public String getPaStrEstado() {
        return paStrEstado;
    }

    public void setPaStrEstado(String paStrEstado) {
        this.paStrEstado = paStrEstado;
    }

    public BigDecimal getBdPorcentajeAvance() {
        return bdPorcentajeAvance;
    }

    public void setBdPorcentajeAvance(BigDecimal bdPorcentajeAvance) {
        this.bdPorcentajeAvance = bdPorcentajeAvance;
    }

    public Boolean getBoCheckFilterSeg() {
        return boCheckFilterSeg;
    }

    public void setBoCheckFilterSeg(Boolean boCheckFilterSeg) {
        this.boCheckFilterSeg = boCheckFilterSeg;
    }

    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
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

    public Integer getIntCantidadRed() {
        return intCantidadRed;
    }

    public void setIntCantidadRed(Integer intCantidadRed) {
        this.intCantidadRed = intCantidadRed;
    }

    public Boolean getBlManual() {
        return blManual;
    }

    public void setBlManual(Boolean blManual) {
        this.blManual = blManual;
    }

    public Boolean getBlEnvioCorreo() {
        return blEnvioCorreo;
    }

    public void setBlEnvioCorreo(Boolean blEnvioCorreo) {
        this.blEnvioCorreo = blEnvioCorreo;
    }

    public Boolean getBlAnalizado() {
        return blAnalizado;
    }

    public void setBlAnalizado(Boolean blAnalizado) {
        this.blAnalizado = blAnalizado;
    }

    public Integer getInAnalizado() {
        return inAnalizado;
    }

    public void setInAnalizado(Integer inAnalizado) {
        this.inAnalizado = inAnalizado;
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

    public String getPaTxImgUrl() {
        return paTxImgUrl;
    }

    public void setPaTxImgUrl(String paTxImgUrl) {
        this.paTxImgUrl = paTxImgUrl;
    }

    public Integer getIntNumberEvaluators() {
        return intNumberEvaluators;
    }

    public void setIntNumberEvaluators(Integer intNumberEvaluators) {
        this.intNumberEvaluators = intNumberEvaluators;
    }

    public Integer getIntNumberEvaluationFinished() {
        return intNumberEvaluationFinished;
    }

    public void setIntNumberEvaluationFinished(Integer intNumberEvaluationFinished) {
        this.intNumberEvaluationFinished = intNumberEvaluationFinished;
    }

}
