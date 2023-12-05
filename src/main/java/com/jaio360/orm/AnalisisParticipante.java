package com.jaio360.orm;

public class AnalisisParticipante implements java.io.Serializable {

    private Integer apIdAnalisisPk;
    private ReporteGenerado reporteGenerado;
    private Integer paIdParticipanteFk;
    private String apFilename;
    private byte[] apArchivo;

    public AnalisisParticipante() {
    }

    public Integer getApIdAnalisisPk() {
        return apIdAnalisisPk;
    }

    public void setApIdAnalisisPk(Integer apIdAnalisisPk) {
        this.apIdAnalisisPk = apIdAnalisisPk;
    }

    public String getApFilename() {
        return apFilename;
    }

    public void setApFilename(String apFilename) {
        this.apFilename = apFilename;
    }

    public ReporteGenerado getReporteGenerado() {
        return reporteGenerado;
    }

    public void setReporteGenerado(ReporteGenerado reporteGenerado) {
        this.reporteGenerado = reporteGenerado;
    }

    public Integer getPaIdParticipanteFk() {
        return paIdParticipanteFk;
    }

    public void setPaIdParticipanteFk(Integer paIdParticipanteFk) {
        this.paIdParticipanteFk = paIdParticipanteFk;
    }

    public byte[] getApArchivo() {
        return apArchivo;
    }

    public void setApArchivo(byte[] apArchivo) {
        this.apArchivo = apArchivo;
    }


}
