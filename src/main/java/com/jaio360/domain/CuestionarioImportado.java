package com.jaio360.domain;

import java.io.Serializable;
import java.util.List;

public class CuestionarioImportado  implements Serializable {

    private Integer idCuestionario;
    private String strDescCuestionario;
    private List<Categorias> lstCategorias;
    private List<String> lstComentarios;
    private List<String> lstPreguntasAbiertas;

    public Integer getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(Integer idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    
    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
    }

    public List<Categorias> getLstCategorias() {
        return lstCategorias;
    }

    public void setLstCategorias(List<Categorias> lstCategorias) {
        this.lstCategorias = lstCategorias;
    }

    public List<String> getLstComentarios() {
        return lstComentarios;
    }

    public void setLstComentarios(List<String> lstComentarios) {
        this.lstComentarios = lstComentarios;
    }

    public List<String> getLstPreguntasAbiertas() {
        return lstPreguntasAbiertas;
    }

    public void setLstPreguntasAbiertas(List<String> lstPreguntasAbiertas) {
        this.lstPreguntasAbiertas = lstPreguntasAbiertas;
    }

}
