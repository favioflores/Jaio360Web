package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Evaluado implements Serializable{
    
    private Integer paIdParticipantePk;
    private String paTxDescripcion;
    private String paTxCorreo;
    private String paTxNombreCargo;
    private boolean paInRedCargada;
    private boolean paInRedVerificada;
    private Integer paIdTipoParticipante;
    private boolean paInAutoevaluar;
    private Integer paIdEstado;
    private String paStrEstado;
    private BigDecimal bdPorcentajeAvance;
    private Boolean boCheckFilterSeg;
    private String strDescCuestionario;
    private String strCorrectoMasivo;
    private String strObservacionMasivo;
    private Integer intCantidadRed;
    private boolean blManual;
    private boolean blEnvioCorreo;
    
    private String paTxSexo;
    private Integer paNrEdad;
    private Integer paNrTiempoTrabajo;
    private String paTxOcupacion;
    private String paTxAreaNegocio;

    public boolean isBlManual() {
        return blManual;
    }

    public boolean isBlEnvioCorreo() {
        return blEnvioCorreo;
    }

    public void setBlEnvioCorreo(boolean blEnvioCorreo) {
        this.blEnvioCorreo = blEnvioCorreo;
    }
    
    public void setBlManual(boolean blManual) {
        this.blManual = blManual;
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

    public String getStrCorrectoMasivo() {
        return strCorrectoMasivo;
    }

    public Integer getIntCantidadRed() {
        return intCantidadRed;
    }

    public void setIntCantidadRed(Integer intCantidadRed) {
        this.intCantidadRed = intCantidadRed;
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
    
    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
    }

    public Boolean getBoCheckFilterSeg() {
        return boCheckFilterSeg;
    }

    public void setBoCheckFilterSeg(Boolean boCheckFilterSeg) {
        this.boCheckFilterSeg = boCheckFilterSeg;
    }

    public BigDecimal getBdPorcentajeAvance() {
        return bdPorcentajeAvance;
    }

    public void setBdPorcentajeAvance(BigDecimal bdPorcentajeAvance) {
        this.bdPorcentajeAvance = bdPorcentajeAvance;
    }
    
    public String getPaStrEstado() {
        return paStrEstado;
    }

    public void setPaStrEstado(String paStrEstado) {
        this.paStrEstado = paStrEstado;
    }

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

    public boolean isPaInRedCargada() {
        return paInRedCargada;
    }

    public void setPaInRedCargada(boolean paInRedCargada) {
        this.paInRedCargada = paInRedCargada;
    }

    public boolean isPaInRedVerificada() {
        return paInRedVerificada;
    }

    public void setPaInRedVerificada(boolean paInRedVerificada) {
        this.paInRedVerificada = paInRedVerificada;
    }

    public Integer getPaIdTipoParticipante() {
        return paIdTipoParticipante;
    }

    public void setPaIdTipoParticipante(Integer paIdTipoParticipante) {
        this.paIdTipoParticipante = paIdTipoParticipante;
    }

    public boolean isPaInAutoevaluar() {
        return paInAutoevaluar;
    }

    public void setPaInAutoevaluar(boolean paInAutoevaluar) {
        this.paInAutoevaluar = paInAutoevaluar;
    }

    public Integer getPaIdEstado() {
        return paIdEstado;
    }

    public void setPaIdEstado(Integer paIdEstado) {
        this.paIdEstado = paIdEstado;
    }
    
}


