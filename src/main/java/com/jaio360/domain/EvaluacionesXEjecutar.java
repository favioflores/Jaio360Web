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
    private boolean blGrupal;
    private Integer intRptaSeleccionada;
    private String strRptaPreguntaAbierta;
    private boolean blAutoevaluation;
    private RelacionParticipanteId relacionParticipanteId;
    private List<PreguntaCerradaBean> lstPreguntasCerradas;
    private List<PreguntaAbiertaBean> lstPreguntasAbiertas;

    public EvaluacionesXEjecutar() {
        this.lstPreguntasCerradas = new ArrayList<>();
        this.lstPreguntasAbiertas = new ArrayList<>();
    }

    public String getStrCorreoEvaluador() {
        return strCorreoEvaluador;
    }

    public void setStrCorreoEvaluador(String strCorreoEvaluador) {
        this.strCorreoEvaluador = strCorreoEvaluador;
    }
    
    

    public RelacionParticipanteId getRelacionParticipanteId() {
        return relacionParticipanteId;
    }

    public boolean isBlAutoevaluation() {
        return blAutoevaluation;
    }

    public void setBlAutoevaluation(boolean blAutoevaluation) {
        this.blAutoevaluation = blAutoevaluation;
    }
    

    public void setRelacionParticipanteId(RelacionParticipanteId relacionParticipanteId) {
        this.relacionParticipanteId = relacionParticipanteId;
    }

    
    public String getStrRptaPreguntaAbierta() {
        return strRptaPreguntaAbierta;
    }

    public void setStrRptaPreguntaAbierta(String strRptaPreguntaAbierta) {
        this.strRptaPreguntaAbierta = strRptaPreguntaAbierta;
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

    public Integer getIntRptaSeleccionada() {
        return intRptaSeleccionada;
    }

    public void setIntRptaSeleccionada(Integer intRptaSeleccionada) {
        this.intRptaSeleccionada = intRptaSeleccionada;
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

    public String getStrCorreoEvaludo() {
        return strCorreoEvaluado;
    }

    public void setStrCorreoEvaluado(String strCorreoEvaluado) {
        this.strCorreoEvaluado = strCorreoEvaluado;
    }

    public String getStrURLImagen() {
        return strURLImagen;
    }

    public void setStrURLImagen(String strURLImagen) {
        this.strURLImagen = strURLImagen;
    }

    public boolean isBlGrupal() {
        return blGrupal;
    }

    public void setBlGrupal(boolean blGrupal) {
        this.blGrupal = blGrupal;
    }

}
