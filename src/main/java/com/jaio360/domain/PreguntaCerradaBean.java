package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PreguntaCerradaBean implements Serializable {

    private Integer id;
    private String strDescripcion;
    private Boolean blRespondido;
    private Integer idRespuesta;
    private BigDecimal bdScoreRequired;
    private BigDecimal bdScoreMinRequired;
    private List<ComentarioBean> lstComentarios;

    public PreguntaCerradaBean() {

    }

    public PreguntaCerradaBean(Integer id, String strDescripcion, BigDecimal bdScoreRequired, BigDecimal bdScoreMinRequired) {
        this.id = id;
        this.strDescripcion = strDescripcion;
        this.bdScoreRequired = bdScoreRequired;
        this.bdScoreMinRequired = bdScoreMinRequired;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getBdScoreRequired() {
        return bdScoreRequired;
    }

    public void setBdScoreRequired(BigDecimal bdScoreRequired) {
        this.bdScoreRequired = bdScoreRequired;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public Boolean getBlRespondido() {
        return blRespondido;
    }

    public void setBlRespondido(Boolean blRespondido) {
        this.blRespondido = blRespondido;
    }

    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public List<ComentarioBean> getLstComentarios() {
        return lstComentarios;
    }

    public void setLstComentarios(List<ComentarioBean> lstComentarios) {
        this.lstComentarios = lstComentarios;
    }

    public BigDecimal getBdScoreMinRequired() {
        return bdScoreMinRequired;
    }

    public void setBdScoreMinRequired(BigDecimal bdScoreMinRequired) {
        this.bdScoreMinRequired = bdScoreMinRequired;
    }

}
