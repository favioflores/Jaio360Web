package com.jaio360.orm;

public class RelacionParticipanteId implements java.io.Serializable {

    private Integer reIdRelacionFk;
    private Integer reIdParticipanteFk;
    private Integer paIdParticipanteFk;

    public Integer getReIdRelacionFk() {
        return reIdRelacionFk;
    }

    public void setReIdRelacionFk(Integer reIdRelacionFk) {
        this.reIdRelacionFk = reIdRelacionFk;
    }

    public Integer getReIdParticipanteFk() {
        return reIdParticipanteFk;
    }

    public void setReIdParticipanteFk(Integer reIdParticipanteFk) {
        this.reIdParticipanteFk = reIdParticipanteFk;
    }

    public Integer getPaIdParticipanteFk() {
        return paIdParticipanteFk;
    }

    public void setPaIdParticipanteFk(Integer paIdParticipanteFk) {
        this.paIdParticipanteFk = paIdParticipanteFk;
    }

    
    public RelacionParticipanteId() {
    }

    public RelacionParticipanteId(Integer reIdRelacionFk, Integer reIdParticipanteFk, Integer paIdParticipanteFk) {
        this.reIdRelacionFk = reIdRelacionFk;
        this.reIdParticipanteFk = reIdParticipanteFk;
        this.paIdParticipanteFk = paIdParticipanteFk;
    }

}
