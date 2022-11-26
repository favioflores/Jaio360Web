package com.jaio360.domain;

import java.io.Serializable;

public class UsuarioSaldoBean implements Serializable {

    private Integer intUsuario;
    private Integer intTotalIndividual;
    private Integer intDisponibleIndividual;
    private Integer intReservadoIndividual;
    private Integer intTotalMasivo;
    private Integer intDisponibleMasivo;
    private Integer intReservadoMasivo;

    public Integer getIntUsuario() {
        return intUsuario;
    }

    public void setIntUsuario(Integer intUsuario) {
        this.intUsuario = intUsuario;
    }

    public Integer getIntTotalIndividual() {
        return intTotalIndividual;
    }

    public void setIntTotalIndividual(Integer intTotalIndividual) {
        this.intTotalIndividual = intTotalIndividual;
    }

    public Integer getIntDisponibleIndividual() {
        return intDisponibleIndividual;
    }

    public void setIntDisponibleIndividual(Integer intDisponibleIndividual) {
        this.intDisponibleIndividual = intDisponibleIndividual;
    }

    public Integer getIntReservadoIndividual() {
        return intReservadoIndividual;
    }

    public void setIntReservadoIndividual(Integer intReservadoIndividual) {
        this.intReservadoIndividual = intReservadoIndividual;
    }

    public Integer getIntTotalMasivo() {
        return intTotalMasivo;
    }

    public void setIntTotalMasivo(Integer intTotalMasivo) {
        this.intTotalMasivo = intTotalMasivo;
    }

    public Integer getIntDisponibleMasivo() {
        return intDisponibleMasivo;
    }

    public void setIntDisponibleMasivo(Integer intDisponibleMasivo) {
        this.intDisponibleMasivo = intDisponibleMasivo;
    }

    public Integer getIntReservadoMasivo() {
        return intReservadoMasivo;
    }

    public void setIntReservadoMasivo(Integer intReservadoMasivo) {
        this.intReservadoMasivo = intReservadoMasivo;
    }

    
    
}
