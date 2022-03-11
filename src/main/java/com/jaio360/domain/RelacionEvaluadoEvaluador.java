package com.jaio360.domain;

import java.io.Serializable;

/**
 *
 * @author Favio
 */
public class RelacionEvaluadoEvaluador implements Serializable{

    private Integer intIdEvaluado;
    private String strDescEvaluado;
    private Integer intIdEvaluador;
    private String strDescEvaluador;
    private String strCorreoEvaluador;
    private Integer intIdRelacion;
    private String strDescRelacion;
    private Boolean blEvaluacionTerminada;
    private Boolean blEnvioCorreo;
    private String strColorRelacion;

    public String getStrColorRelacion() {
        return strColorRelacion;
    }

    public void setStrColorRelacion(String strColorRelacion) {
        this.strColorRelacion = strColorRelacion;
    }
    

    public Boolean getBlEnvioCorreo() {
        return blEnvioCorreo;
    }

    public void setBlEnvioCorreo(Boolean blEnvioCorreo) {
        this.blEnvioCorreo = blEnvioCorreo;
    }
    
    public String getStrCorreoEvaluador() {
        return strCorreoEvaluador;
    }

    public void setStrCorreoEvaluador(String strCorreoEvaluador) {
        this.strCorreoEvaluador = strCorreoEvaluador;
    }

    public Integer getIntIdEvaluado() {
        return intIdEvaluado;
    }

    public void setIntIdEvaluado(Integer intIdEvaluado) {
        this.intIdEvaluado = intIdEvaluado;
    }

    public String getStrDescEvaluado() {
        return strDescEvaluado;
    }

    public void setStrDescEvaluado(String strDescEvaluado) {
        this.strDescEvaluado = strDescEvaluado;
    }

    public Integer getIntIdEvaluador() {
        return intIdEvaluador;
    }

    public void setIntIdEvaluador(Integer intIdEvaluador) {
        this.intIdEvaluador = intIdEvaluador;
    }

    public String getStrDescEvaluador() {
        return strDescEvaluador;
    }

    public void setStrDescEvaluador(String strDescEvaluador) {
        this.strDescEvaluador = strDescEvaluador;
    }

    public Integer getIntIdRelacion() {
        return intIdRelacion;
    }

    public void setIntIdRelacion(Integer intIdRelacion) {
        this.intIdRelacion = intIdRelacion;
    }

    public String getStrDescRelacion() {
        return strDescRelacion;
    }

    public void setStrDescRelacion(String strDescRelacion) {
        this.strDescRelacion = strDescRelacion;
    }

    public Boolean getBlEvaluacionTerminada() {
        return blEvaluacionTerminada;
    }

    public void setBlEvaluacionTerminada(Boolean blEvaluacionTerminada) {
        this.blEvaluacionTerminada = blEvaluacionTerminada;
    }
    
}
