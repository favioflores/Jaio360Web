package com.jaio360.domain;

import java.io.Serializable;

/**
 *
 * @author Favio
 */
public class RelacionAvanzada implements Serializable{

    private String strEvaluado;
    private String strEvaluador;
    private String strRelacion;

    public String getStrEvaluado() {
        return strEvaluado;
    }

    public void setStrEvaluado(String strEvaluado) {
        this.strEvaluado = strEvaluado;
    }

    public String getStrEvaluador() {
        return strEvaluador;
    }

    public void setStrEvaluador(String strEvaluador) {
        this.strEvaluador = strEvaluador;
    }

    public String getStrRelacion() {
        return strRelacion;
    }

    public void setStrRelacion(String strRelacion) {
        this.strRelacion = strRelacion;
    }
    
}
