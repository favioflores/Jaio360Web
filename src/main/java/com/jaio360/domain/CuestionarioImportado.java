package com.jaio360.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CuestionarioImportado implements Serializable {

    private Integer idCuestionario;
    private String strDescCuestionario;
    private List<Categorias> lstCategorias;
    private List<ComentarioBean> lstComentarios;
    private List<PreguntaAbiertaBean> lstPreguntasAbiertas;

    public CuestionarioImportado() {
        this.lstCategorias = new ArrayList<>();
        this.lstComentarios = new ArrayList<>();
        this.lstPreguntasAbiertas = new ArrayList<>();
    }

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

    public List<ComentarioBean> getLstComentarios() {
        return lstComentarios;
    }

    public void setLstComentarios(List<ComentarioBean> lstComentarios) {
        this.lstComentarios = lstComentarios;
    }

    public List<PreguntaAbiertaBean> getLstPreguntasAbiertas() {
        return lstPreguntasAbiertas;
    }

    public void setLstPreguntasAbiertas(List<PreguntaAbiertaBean> lstPreguntasAbiertas) {
        this.lstPreguntasAbiertas = lstPreguntasAbiertas;
    }

}
