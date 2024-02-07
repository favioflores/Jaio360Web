package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Categorias implements Serializable {

    private String strCategoria;
    private Integer intIdComponente;
    private List<PreguntaCerradaBean> lstPreguntasCerradas;
    private BigDecimal bdScoreRequired;
    private BigDecimal bdScoreMinRequired;
    private BigDecimal bdScoreResult;

    public Categorias(String strCategoria, Integer intIdComponente, List<PreguntaCerradaBean> lstPreguntasCerradas, BigDecimal bdScoreRequired, BigDecimal bdScoreMinRequired) {
        this.strCategoria = strCategoria;
        this.intIdComponente = intIdComponente;
        this.lstPreguntasCerradas = lstPreguntasCerradas;
        this.bdScoreRequired = bdScoreRequired;
        this.bdScoreMinRequired = bdScoreMinRequired;
    }

    public Categorias(String strCategoria, Integer intIdComponente, List<PreguntaCerradaBean> lstPreguntasCerradas, BigDecimal bdScoreRequired, BigDecimal bdScoreMinRequired, BigDecimal bdScoreResult) {
        this.strCategoria = strCategoria;
        this.intIdComponente = intIdComponente;
        this.lstPreguntasCerradas = lstPreguntasCerradas;
        this.bdScoreRequired = bdScoreRequired;
        this.bdScoreMinRequired = bdScoreMinRequired;
        this.bdScoreResult = bdScoreResult;
    }

    public Categorias(Integer intIdComponente) {
        this.intIdComponente = intIdComponente;
        this.lstPreguntasCerradas = new ArrayList<>();
    }

    public BigDecimal getBdScoreRequired() {
        return bdScoreRequired;
    }

    public void setBdScoreRequired(BigDecimal bdScoreRequired) {
        this.bdScoreRequired = bdScoreRequired;
    }

    public Integer getIntIdComponente() {
        return intIdComponente;
    }

    public String getStrCategoria() {
        return strCategoria;
    }

    public void setStrCategoria(String strCategoria) {
        this.strCategoria = strCategoria;
    }

    public List<PreguntaCerradaBean> getLstPreguntasCerradas() {
        return lstPreguntasCerradas;
    }

    public void setLstPreguntasCerradas(List<PreguntaCerradaBean> lstPreguntasCerradas) {
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

    public BigDecimal getBdScoreResult() {
        return bdScoreResult;
    }

    public void setBdScoreResult(BigDecimal bdScoreResult) {
        this.bdScoreResult = bdScoreResult;
    }

    public BigDecimal getBdScoreMinRequired() {
        return bdScoreMinRequired;
    }

    public void setBdScoreMinRequired(BigDecimal bdScoreMinRequired) {
        this.bdScoreMinRequired = bdScoreMinRequired;
    }

}
