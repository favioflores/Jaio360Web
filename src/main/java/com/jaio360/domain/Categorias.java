package com.jaio360.domain;

import java.io.Serializable;
import java.util.List;

public class Categorias implements Serializable{
    
    private String strCategoria;
    private List<String> lstPreguntasCerradas;
    
    public Categorias(String strCategoria, List<String> lstPreguntasCerradas) {
        this.strCategoria = strCategoria;
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

    public String getStrCategoria() {
        return strCategoria;
    }

    public void setStrCategoria(String strCategoria) {
        this.strCategoria = strCategoria;
    }

    public List<String> getLstPreguntasCerradas() {
        return lstPreguntasCerradas;
    }

    public void setLstPreguntasCerradas(List<String> lstPreguntasCerradas) {
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

}