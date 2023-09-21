package com.jaio360.orm;

import java.util.Date;

public class HistorialAcceso implements java.io.Serializable {

    private Integer haIdHistorialPk;
    private Usuario usuario;
    private Date haFeIngreso;
    private Date haFeSalida;
    private Boolean haInEstado;

    public HistorialAcceso() {
    }

    public Integer getHaIdHistorialPk() {
        return haIdHistorialPk;
    }

    public void setHaIdHistorialPk(Integer haIdHistorialPk) {
        this.haIdHistorialPk = haIdHistorialPk;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getHaFeIngreso() {
        return haFeIngreso;
    }

    public void setHaFeIngreso(Date haFeIngreso) {
        this.haFeIngreso = haFeIngreso;
    }

    public Date getHaFeSalida() {
        return haFeSalida;
    }

    public void setHaFeSalida(Date haFeSalida) {
        this.haFeSalida = haFeSalida;
    }

    public Boolean getHaInEstado() {
        return haInEstado;
    }

    public void setHaInEstado(Boolean haInEstado) {
        this.haInEstado = haInEstado;
    }

    public HistorialAcceso(Usuario usuario, Date haFeIngreso, boolean haInEstado) {
        this.usuario = usuario;
        this.haFeIngreso = haFeIngreso;
        this.haInEstado = haInEstado;
    }

    public HistorialAcceso(Usuario usuario, Date haFeIngreso, Date haFeSalida, boolean haInEstado) {
        this.usuario = usuario;
        this.haFeIngreso = haFeIngreso;
        this.haFeSalida = haFeSalida;
        this.haInEstado = haInEstado;
    }

}
