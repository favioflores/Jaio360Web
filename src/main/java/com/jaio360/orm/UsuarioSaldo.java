package com.jaio360.orm;

public class UsuarioSaldo implements java.io.Serializable {

    private Integer usIdCuentaPk;
    private Integer usNrTotalIndividual;
    private Integer usNrDisponibleIndividual;
    private Integer usNrReservadoIndividual;
    private Integer usNrUsadoIndividual;
    private Integer usNrTotalMasivo;
    private Integer usNrDisponibleMasivo;
    private Integer usNrReservadoMasivo;
    private Integer usNrUsadoMasivo;

    public UsuarioSaldo() {
        this.usNrTotalIndividual = 0;
        this.usNrDisponibleIndividual = 0;
        this.usNrReservadoIndividual = 0;
        this.usNrTotalMasivo = 0;
        this.usNrDisponibleMasivo = 0;
        this.usNrReservadoMasivo = 0;
        this.usNrUsadoIndividual = 0;
        this.usNrUsadoMasivo = 0;
    }

    public Integer getUsIdCuentaPk() {
        return usIdCuentaPk;
    }

    public void setUsIdCuentaPk(Integer usIdCuentaPk) {
        this.usIdCuentaPk = usIdCuentaPk;
    }

    public Integer getUsNrTotalIndividual() {
        return usNrTotalIndividual;
    }

    public void setUsNrTotalIndividual(Integer usNrTotalIndividual) {
        this.usNrTotalIndividual = usNrTotalIndividual;
    }

    public Integer getUsNrDisponibleIndividual() {
        return usNrDisponibleIndividual;
    }

    public void setUsNrDisponibleIndividual(Integer usNrDisponibleIndividual) {
        this.usNrDisponibleIndividual = usNrDisponibleIndividual;
    }

    public Integer getUsNrReservadoIndividual() {
        return usNrReservadoIndividual;
    }

    public void setUsNrReservadoIndividual(Integer usNrReservadoIndividual) {
        this.usNrReservadoIndividual = usNrReservadoIndividual;
    }

    public Integer getUsNrUsadoIndividual() {
        return usNrUsadoIndividual;
    }

    public void setUsNrUsadoIndividual(Integer usNrUsadoIndividual) {
        this.usNrUsadoIndividual = usNrUsadoIndividual;
    }

    public Integer getUsNrTotalMasivo() {
        return usNrTotalMasivo;
    }

    public void setUsNrTotalMasivo(Integer usNrTotalMasivo) {
        this.usNrTotalMasivo = usNrTotalMasivo;
    }

    public Integer getUsNrDisponibleMasivo() {
        return usNrDisponibleMasivo;
    }

    public void setUsNrDisponibleMasivo(Integer usNrDisponibleMasivo) {
        this.usNrDisponibleMasivo = usNrDisponibleMasivo;
    }

    public Integer getUsNrReservadoMasivo() {
        return usNrReservadoMasivo;
    }

    public void setUsNrReservadoMasivo(Integer usNrReservadoMasivo) {
        this.usNrReservadoMasivo = usNrReservadoMasivo;
    }

    public Integer getUsNrUsadoMasivo() {
        return usNrUsadoMasivo;
    }

    public void setUsNrUsadoMasivo(Integer usNrUsadoMasivo) {
        this.usNrUsadoMasivo = usNrUsadoMasivo;
    }

}
