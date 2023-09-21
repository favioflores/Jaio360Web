package com.jaio360.orm;

import java.math.BigDecimal;

public class ResultadoInfo implements java.io.Serializable {

    private BigDecimal deNuOrden;
    private String deTxValor;
    private String deNomRelacion;

    public BigDecimal getDeNuOrden() {
        return deNuOrden;
    }

    public void setDeNuOrden(BigDecimal deNuOrden) {
        this.deNuOrden = deNuOrden;
    }

    public String getDeTxValor() {
        return deTxValor;
    }

    public void setDeTxValor(String deTxValor) {
        this.deTxValor = deTxValor;
    }

    public String getDeNomRelacion() {
        return deNomRelacion;
    }

    public void setDeNomRelacion(String deNomRelacion) {
        this.deNomRelacion = deNomRelacion;
    }

    
}
