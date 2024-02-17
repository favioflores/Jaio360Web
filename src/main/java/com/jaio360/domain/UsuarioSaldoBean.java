package com.jaio360.domain;

import java.io.Serializable;

public class UsuarioSaldoBean implements Serializable {

    private Integer intUsuario;
    private String strTipoSaldo;
    private Integer intTotal;
    private Integer intDisponible;
    private Integer intReservado;
    private Integer intUtilizado;

    public Integer getIntUsuario() {
        return intUsuario;
    }

    public void setIntUsuario(Integer intUsuario) {
        this.intUsuario = intUsuario;
    }

    public String getStrTipoSaldo() {
        return strTipoSaldo;
    }

    public void setStrTipoSaldo(String strTipoSaldo) {
        this.strTipoSaldo = strTipoSaldo;
    }

    public Integer getIntTotal() {
        return intTotal;
    }

    public void setIntTotal(Integer intTotal) {
        this.intTotal = intTotal;
    }

    public Integer getIntDisponible() {
        return intDisponible;
    }

    public void setIntDisponible(Integer intDisponible) {
        this.intDisponible = intDisponible;
    }

    public Integer getIntReservado() {
        return intReservado;
    }

    public void setIntReservado(Integer intReservado) {
        this.intReservado = intReservado;
    }

    public Integer getIntUtilizado() {
        return intUtilizado;
    }

    public void setIntUtilizado(Integer intUtilizado) {
        this.intUtilizado = intUtilizado;
    }

}
