package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ItemBelowRequired implements Serializable {

    private String strQuestionDescription;
    private String strCategoryDescription;
    private BigDecimal bdScore;
    private BigDecimal bdScoreAuto;
    private BigDecimal bdScoreRequired;
    private BigDecimal bdScoreMinRequired;
    private String strColorResult;
    private List<ResultByRelation> lstResultByRelation;

    public ItemBelowRequired(String strQuestionDescription, String strCategoryDescription, BigDecimal bdScore, BigDecimal bdScoreAuto, BigDecimal bdScoreRequired, BigDecimal bdScoreMinRequired, String strColorResult, List<ResultByRelation> lstResultByRelation) {
        this.strQuestionDescription = strQuestionDescription;
        this.strCategoryDescription = strCategoryDescription;
        this.bdScore = bdScore;
        this.bdScoreAuto = bdScoreAuto;
        this.bdScoreRequired = bdScoreRequired;
        this.bdScoreMinRequired = bdScoreMinRequired;
        this.strColorResult = strColorResult;
        this.lstResultByRelation = lstResultByRelation;
    }

    public String getStrColorResult() {
        return strColorResult;
    }

    public void setStrColorResult(String strColorResult) {
        this.strColorResult = strColorResult;
    }

    public String getStrQuestionDescription() {
        return strQuestionDescription;
    }

    public void setStrQuestionDescription(String strQuestionDescription) {
        this.strQuestionDescription = strQuestionDescription;
    }

    public String getStrCategoryDescription() {
        return strCategoryDescription;
    }

    public void setStrCategoryDescription(String strCategoryDescription) {
        this.strCategoryDescription = strCategoryDescription;
    }

    public BigDecimal getBdScore() {
        return bdScore;
    }

    public void setBdScore(BigDecimal bdScore) {
        this.bdScore = bdScore;
    }

    public BigDecimal getBdScoreAuto() {
        return bdScoreAuto;
    }

    public void setBdScoreAuto(BigDecimal bdScoreAuto) {
        this.bdScoreAuto = bdScoreAuto;
    }

    public BigDecimal getBdScoreRequired() {
        return bdScoreRequired;
    }

    public void setBdScoreRequired(BigDecimal bdScoreRequired) {
        this.bdScoreRequired = bdScoreRequired;
    }

    public List<ResultByRelation> getLstResultByRelation() {
        return lstResultByRelation;
    }

    public void setLstResultByRelation(List<ResultByRelation> lstResultByRelation) {
        this.lstResultByRelation = lstResultByRelation;
    }

    public BigDecimal getBdScoreMinRequired() {
        return bdScoreMinRequired;
    }

    public void setBdScoreMinRequired(BigDecimal bdScoreMinRequired) {
        this.bdScoreMinRequired = bdScoreMinRequired;
    }

}
