package com.jaio360.orm;

import java.util.HashSet;
import java.util.Set;

public class RelacionParticipante implements java.io.Serializable {

    private RelacionParticipanteId id;
    private Relacion relacion;
    private Participante participante;
    private RedEvaluacion redEvaluacion;
    private Integer rpIdEstado;
    private Set resultados = new HashSet(0);

    public RelacionParticipanteId getId() {
        return id;
    }

    public void setId(RelacionParticipanteId id) {
        this.id = id;
    }

    public Relacion getRelacion() {
        return relacion;
    }

    public void setRelacion(Relacion relacion) {
        this.relacion = relacion;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public RedEvaluacion getRedEvaluacion() {
        return redEvaluacion;
    }

    public void setRedEvaluacion(RedEvaluacion redEvaluacion) {
        this.redEvaluacion = redEvaluacion;
    }

    public Integer getRpIdEstado() {
        return rpIdEstado;
    }

    public void setRpIdEstado(Integer rpIdEstado) {
        this.rpIdEstado = rpIdEstado;
    }

    public Set getResultados() {
        return resultados;
    }

    public void setResultados(Set resultados) {
        this.resultados = resultados;
    }

    public RelacionParticipante() {
    }

    public RelacionParticipante(RelacionParticipanteId id, Relacion relacion, Participante participante, RedEvaluacion redEvaluacion) {
        this.id = id;
        this.relacion = relacion;
        this.participante = participante;
        this.redEvaluacion = redEvaluacion;
    }

    public RelacionParticipante(RelacionParticipanteId id, Relacion relacion, Participante participante, RedEvaluacion redEvaluacion, Set resultados) {
        this.id = id;
        this.relacion = relacion;
        this.participante = participante;
        this.redEvaluacion = redEvaluacion;
        this.resultados = resultados;
    }

}
