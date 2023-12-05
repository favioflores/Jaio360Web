package com.jaio360.orm;

import com.jaio360.domain.ProyectoInfo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ReporteGenerado implements java.io.Serializable {

    private Integer rgReportePk;
    private Usuario usuario;
    private Integer rgEstado;
    private ProyectoInfo proyectoInfo;
    private Proyecto proyecto;
    private String rgStrEstado;
    private Boolean rgblWeighted;
    private BigDecimal rgBlPorcentajeAvance;
    private Date rgDtFechaRegistro;
    private Date rgDtFechaUltMod;
    private String rgTxNombreArchivo;
    private String rgTxTimeElapsed;
    private Date rgDtFechaExpiracion;
    private String rgTxFechaEliminacion;
    private String rgTxTipoAnalisis;
    private String rgTxEsPonderado;
    private Byte[] rgTxParametros;

    private Set analisisParticipantes = new HashSet(0);

    public ReporteGenerado() {
    }

    public BigDecimal getRgBlPorcentajeAvance() {
        return rgBlPorcentajeAvance;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public ProyectoInfo getProyectoInfo() {
        return proyectoInfo;
    }

    public void setProyectoInfo(ProyectoInfo proyectoInfo) {
        this.proyectoInfo = proyectoInfo;
    }

    public void setRgBlPorcentajeAvance(BigDecimal rgBlPorcentajeAvance) {
        this.rgBlPorcentajeAvance = rgBlPorcentajeAvance;
    }

    public Boolean getRgblWeighted() {
        return rgblWeighted;
    }

    public void setRgblWeighted(Boolean rgblWeighted) {
        this.rgblWeighted = rgblWeighted;
    }

    public Integer getRgReportePk() {
        return rgReportePk;
    }

    public Date getRgDtFechaUltMod() {
        return rgDtFechaUltMod;
    }

    public String getRgStrEstado() {
        return rgStrEstado;
    }

    public void setRgStrEstado(String rgStrEstado) {
        this.rgStrEstado = rgStrEstado;
    }

    public void setRgDtFechaUltMod(Date rgDtFechaUltMod) {
        this.rgDtFechaUltMod = rgDtFechaUltMod;
    }

    public void setRgReportePk(Integer rgReportePk) {
        this.rgReportePk = rgReportePk;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getRgEstado() {
        return rgEstado;
    }

    public void setRgEstado(Integer rgEstado) {
        this.rgEstado = rgEstado;
    }

    public Date getRgDtFechaRegistro() {
        return rgDtFechaRegistro;
    }

    public void setRgDtFechaRegistro(Date rgDtFechaRegistro) {
        this.rgDtFechaRegistro = rgDtFechaRegistro;
    }

    public String getRgTxNombreArchivo() {
        return rgTxNombreArchivo;
    }

    public void setRgTxNombreArchivo(String rgTxNombreArchivo) {
        this.rgTxNombreArchivo = rgTxNombreArchivo;
    }

    public Set getAnalisisParticipantes() {
        return analisisParticipantes;
    }

    public void setAnalisisParticipantes(Set analisisParticipantes) {
        this.analisisParticipantes = analisisParticipantes;
    }

    public String getRgTxTimeElapsed() {
        return rgTxTimeElapsed;
    }

    public void setRgTxTimeElapsed(String rgTxTimeElapsed) {
        this.rgTxTimeElapsed = rgTxTimeElapsed;
    }

    public Date getRgDtFechaExpiracion() {
        return rgDtFechaExpiracion;
    }

    public void setRgDtFechaExpiracion(Date rgDtFechaExpiracion) {
        this.rgDtFechaExpiracion = rgDtFechaExpiracion;
    }

    public String getRgTxFechaEliminacion() {
        return rgTxFechaEliminacion;
    }

    public void setRgTxFechaEliminacion(String rgTxFechaEliminacion) {
        this.rgTxFechaEliminacion = rgTxFechaEliminacion;
    }

    public String getRgTxTipoAnalisis() {
        return rgTxTipoAnalisis;
    }

    public void setRgTxTipoAnalisis(String rgTxTipoAnalisis) {
        this.rgTxTipoAnalisis = rgTxTipoAnalisis;
    }

    public String getRgTxEsPonderado() {
        return rgTxEsPonderado;
    }

    public void setRgTxEsPonderado(String rgTxEsPonderado) {
        this.rgTxEsPonderado = rgTxEsPonderado;
    }

    public Byte[] getRgTxParametros() {
        return rgTxParametros;
    }

    public void setRgTxParametros(Byte[] rgTxParametros) {
        this.rgTxParametros = rgTxParametros;
    }


}
