package com.jaio360.orm;

import java.util.HashSet;
import java.util.Set;

public class DetalleMetrica implements java.io.Serializable {

    private Integer deIdDetalleEscalaPk;
    private Metrica metrica;
    private Integer deNuOrden;
    private String deTxValor;
    private Set resultados = new HashSet(0);

    public Integer getDeIdDetalleEscalaPk() {
        return deIdDetalleEscalaPk;
    }

    public void setDeIdDetalleEscalaPk(Integer deIdDetalleEscalaPk) {
        this.deIdDetalleEscalaPk = deIdDetalleEscalaPk;
    }

    public Metrica getMetrica() {
        return metrica;
    }

    public void setMetrica(Metrica metrica) {
        this.metrica = metrica;
    }

    public Integer getDeNuOrden() {
        return deNuOrden;
    }

    public void setDeNuOrden(Integer deNuOrden) {
        this.deNuOrden = deNuOrden;
    }

    public String getDeTxValor() {
        return deTxValor;
    }

    public void setDeTxValor(String deTxValor) {
        this.deTxValor = deTxValor;
    }

    public Set getResultados() {
        return resultados;
    }

    public void setResultados(Set resultados) {
        this.resultados = resultados;
    }

    public DetalleMetrica() {
    }

    public DetalleMetrica(Metrica metrica, int deNuOrden, String deTxValor) {
        this.metrica = metrica;
        this.deNuOrden = deNuOrden;
        this.deTxValor = deTxValor;
    }

    public DetalleMetrica(Metrica metrica, int deNuOrden, String deTxValor, Set resultados) {
        this.metrica = metrica;
        this.deNuOrden = deNuOrden;
        this.deTxValor = deTxValor;
        this.resultados = resultados;
    }

}
