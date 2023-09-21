package com.jaio360.domain;

import com.jaio360.orm.RelacionParticipanteId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionesXEjecutar implements Serializable {

    private Integer idProyecto;
    private Integer idParticipante;
    private String strNombreEvaluado;
    private String strCorreoEvaluado;
    private String strCorreoEvaluador;
    private String strURLImagen;
    private Boolean blGrupal;
    private Integer intRptaSeleccionada;
    private String strRptaPreguntaAbierta;
    private Boolean blAutoevaluation;
    private RelacionParticipanteId relacionParticipanteId;
    private List<PreguntaCerradaBean> lstPreguntasCerradas;
    private List<PreguntaAbiertaBean> lstPreguntasAbiertas;

    public EvaluacionesXEjecutar() {
        this.lstPreguntasCerradas = new ArrayList<>();
        this.lstPreguntasAbiertas = new ArrayList<>();
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Integer getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(Integer idParticipante) {
        this.idParticipante = idParticipante;
    }

    public String getStrNombreEvaluado() {
        return strNombreEvaluado;
    }

    public void setStrNombreEvaluado(String strNombreEvaluado) {
        this.strNombreEvaluado = strNombreEvaluado;
    }

    public String getStrCorreoEvaluado() {
        return strCorreoEvaluado;
    }

    public void setStrCorreoEvaluado(String strCorreoEvaluado) {
        this.strCorreoEvaluado = strCorreoEvaluado;
    }

    public String getStrCorreoEvaluador() {
        return strCorreoEvaluador;
    }

    public void setStrCorreoEvaluador(String strCorreoEvaluador) {
        this.strCorreoEvaluador = strCorreoEvaluador;
    }

    public String getStrURLImagen() {
        return strURLImagen;
    }

    public void setStrURLImagen(String strURLImagen) {
        this.strURLImagen = strURLImagen;
    }

    public Boolean getBlGrupal() {
        return blGrupal;
    }

    public void setBlGrupal(Boolean blGrupal) {
        this.blGrupal = blGrupal;
    }

    public Integer getIntRptaSeleccionada() {
        return intRptaSeleccionada;
    }

    public void setIntRptaSeleccionada(Integer intRptaSeleccionada) {
        this.intRptaSeleccionada = intRptaSeleccionada;
    }

    public String getStrRptaPreguntaAbierta() {
        return strRptaPreguntaAbierta;
    }

    public void setStrRptaPreguntaAbierta(String strRptaPreguntaAbierta) {
        this.strRptaPreguntaAbierta = strRptaPreguntaAbierta;
    }

    public Boolean getBlAutoevaluation() {
        return blAutoevaluation;
    }

    public void setBlAutoevaluation(Boolean blAutoevaluation) {
        this.blAutoevaluation = blAutoevaluation;
    }

    public RelacionParticipanteId getRelacionParticipanteId() {
        return relacionParticipanteId;
    }

    public void setRelacionParticipanteId(RelacionParticipanteId relacionParticipanteId) {
        this.relacionParticipanteId = relacionParticipanteId;
    }

    public List<PreguntaCerradaBean> getLstPreguntasCerradas() {
        return lstPreguntasCerradas;
    }

    public void setLstPreguntasCerradas(List<PreguntaCerradaBean> lstPreguntasCerradas) {
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

    public List<PreguntaAbiertaBean> getLstPreguntasAbiertas() {
        return lstPreguntasAbiertas;
    }

    public void setLstPreguntasAbiertas(List<PreguntaAbiertaBean> lstPreguntasAbiertas) {
        this.lstPreguntasAbiertas = lstPreguntasAbiertas;
    }

}
