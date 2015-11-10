package com.jaio360.domain;

import java.io.Serializable;

/**
 *
 * @author Favio
 */
public class EvaluadorRelacion implements Serializable{

    private Integer intIdEvaluador;
    private Integer intIdRelacion;
    private String strDescNombre;
    private String strCorreo;
    private String strDescRelacion;
    private String strCargo;

    public String getStrCargo() {
        return strCargo;
    }

    public void setStrCargo(String strCargo) {
        this.strCargo = strCargo;
    }
    
    public Integer getIntIdEvaluador() {
        return intIdEvaluador;
    }

    public void setIntIdEvaluador(Integer intIdEvaluador) {
        this.intIdEvaluador = intIdEvaluador;
    }

    public Integer getIntIdRelacion() {
        return intIdRelacion;
    }

    public void setIntIdRelacion(Integer intIdRelacion) {
        this.intIdRelacion = intIdRelacion;
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

    public String getStrDescRelacion() {
        return strDescRelacion;
    }

    public void setStrDescRelacion(String strDescRelacion) {
        this.strDescRelacion = strDescRelacion;
    }
    
}
