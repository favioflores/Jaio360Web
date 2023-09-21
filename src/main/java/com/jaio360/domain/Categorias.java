package com.jaio360.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Categorias implements Serializable {

    private String strCategoria;
    private Integer intIdComponente;
    private List<PreguntaCerradaBean> lstPreguntasCerradas;

    public Categorias(String strCategoria, Integer intIdComponente, List<PreguntaCerradaBean> lstPreguntasCerradas) {
        this.strCategoria = strCategoria;
        this.intIdComponente = intIdComponente;
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

    public Categorias(Integer intIdComponente) {
        this.intIdComponente = intIdComponente;
        this.lstPreguntasCerradas = new ArrayList<>();
    }

    public Integer getIntIdComponente() {
        return intIdComponente;
    }

    public String getStrCategoria() {
        return strCategoria;
    }

    public void setStrCategoria(String strCategoria) {
        this.strCategoria = strCategoria;
    }

    public List<PreguntaCerradaBean> getLstPreguntasCerradas() {
        return lstPreguntasCerradas;
    }

    public void setLstPreguntasCerradas(List<PreguntaCerradaBean> lstPreguntasCerradas) {
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

}
