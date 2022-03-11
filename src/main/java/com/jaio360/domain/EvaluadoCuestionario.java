package com.jaio360.domain;

import java.io.Serializable;

/**
 *
 * @author Favio
 */
public class EvaluadoCuestionario implements Serializable{

    private Integer intIdEvaluado;
    private String strDescNombre;
    private String strCorreo;
    private String strCargo;
    private Integer intIdCuestionario;
    private Integer intIdEstadoSel;
    private String strEstadoSel;
    private String strEstadoEvaluado;
    private String strCuestionarioDesc;

    public String getStrCuestionarioDesc() {
        return strCuestionarioDesc;
    }

    public void setStrCuestionarioDesc(String strCuestionarioDesc) {
        this.strCuestionarioDesc = strCuestionarioDesc;
    }
    
    public String getStrEstadoEvaluado() {
        return strEstadoEvaluado;
    }

    public void setStrEstadoEvaluado(String strEstadoEvaluado) {
        this.strEstadoEvaluado = strEstadoEvaluado;
    }

    public Integer getIntIdEvaluado() {
        return intIdEvaluado;
    }

    public void setIntIdEvaluado(Integer intIdEvaluado) {
        this.intIdEvaluado = intIdEvaluado;
    }

    public String getStrDescNombre() {
        return strDescNombre;
    }

    public void setStrDescNombre(String strDescNombre) {
        this.strDescNombre = strDescNombre;
    }

    public String getStrCorreo() {
        return strCorreo;
    }

    public void setStrCorreo(String strCorreo) {
        this.strCorreo = strCorreo;
    }

    public String getStrCargo() {
        return strCargo;
    }

    public void setStrCargo(String strCargo) {
        this.strCargo = strCargo;
    }

    public Integer getIntIdCuestionario() {
        return intIdCuestionario;
    }

    public void setIntIdCuestionario(Integer intIdCuestionario) {
        this.intIdCuestionario = intIdCuestionario;
    }

    public Integer getIntIdEstadoSel() {
        return intIdEstadoSel;
    }

    public void setIntIdEstadoSel(Integer intIdEstadoSel) {
        this.intIdEstadoSel = intIdEstadoSel;
    }

    public String getStrEstadoSel() {
        return strEstadoSel;
    }

    public void setStrEstadoSel(String strEstadoSel) {
        this.strEstadoSel = strEstadoSel;
    }

}