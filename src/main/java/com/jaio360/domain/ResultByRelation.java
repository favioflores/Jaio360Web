package com.jaio360.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResultByRelation implements Serializable {

    private String strRelationDescription;
    private BigDecimal bdScore;
    private String strCustomColor;

    public ResultByRelation(String strRelationDescription, BigDecimal bdScore, String strCustomColor) {
        this.strRelationDescription = strRelationDescription;
        this.bdScore = bdScore;
        this.strCustomColor = strCustomColor;
    }

    public String getStrRelationDescription() {
        return strRelationDescription;
    }

    public void setStrRelationDescription(String strRelationDescription) {
        this.strRelationDescription = strRelationDescription;
    }

    public BigDecimal getBdScore() {
        return bdScore;
    }

    public void setBdScore(BigDecimal bdScore) {
        this.bdScore = bdScore;
    }

    public String getStrCustomColor() {
        return strCustomColor;
    }

    public void setStrCustomColor(String strCustomColor) {
        this.strCustomColor = strCustomColor;
    }

}
