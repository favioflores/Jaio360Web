package com.jaio360.orm;

public class CuestionarioEvaluado implements java.io.Serializable {

    private CuestionarioEvaluadoId id;
    private Integer ceIdEstado;
    private Cuestionario cuestionario;
    private Participante participante;
    private Proyecto proyecto;

    public CuestionarioEvaluadoId getId() {
        return id;
    }

    public void setId(CuestionarioEvaluadoId id) {
        this.id = id;
    }

    public Integer getCeIdEstado() {
        return ceIdEstado;
    }

    public void setCeIdEstado(Integer ceIdEstado) {
        this.ceIdEstado = ceIdEstado;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public CuestionarioEvaluado() {
    }

    public CuestionarioEvaluado(CuestionarioEvaluadoId id, Integer ceIdEstado, Cuestionario cuestionario, Participante participante, Proyecto proyecto) {
        this.id = id;
        this.cuestionario = cuestionario;
        this.participante = participante;
        this.proyecto = proyecto;
        this.ceIdEstado = ceIdEstado;
    }

}
