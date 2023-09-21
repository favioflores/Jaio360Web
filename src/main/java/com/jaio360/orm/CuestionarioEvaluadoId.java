package com.jaio360.orm;

public class CuestionarioEvaluadoId implements java.io.Serializable {

    private Integer cuIdCuestionarioFk;
    private Integer paIdParticipanteFk;
    private Integer poIdProyectoFk;

    public CuestionarioEvaluadoId() {
    }

    public Integer getCuIdCuestionarioFk() {
        return cuIdCuestionarioFk;
    }

    public void setCuIdCuestionarioFk(Integer cuIdCuestionarioFk) {
        this.cuIdCuestionarioFk = cuIdCuestionarioFk;
    }

    public Integer getPaIdParticipanteFk() {
        return paIdParticipanteFk;
    }

    public void setPaIdParticipanteFk(Integer paIdParticipanteFk) {
        this.paIdParticipanteFk = paIdParticipanteFk;
    }

    public Integer getPoIdProyectoFk() {
        return poIdProyectoFk;
    }

    public void setPoIdProyectoFk(Integer poIdProyectoFk) {
        this.poIdProyectoFk = poIdProyectoFk;
    }

    public CuestionarioEvaluadoId(Integer cuIdCuestionarioFk, Integer paIdParticipanteFk, Integer poIdProyectoFk) {
        this.cuIdCuestionarioFk = cuIdCuestionarioFk;
        this.paIdParticipanteFk = paIdParticipanteFk;
        this.poIdProyectoFk = poIdProyectoFk;
    }

}
