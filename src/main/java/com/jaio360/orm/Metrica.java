package com.jaio360.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;





public class Metrica implements java.io.Serializable {

    private Integer meIdMetricaPk;
    private Proyecto proyecto;
    private Integer meIdTipoMetrica;
    private Date meFeRegistro;
    private Integer meNuRango;
    private Set detalleMetricas = new HashSet(0);

    public Metrica() {
    }

    public Metrica(Proyecto proyecto, int meIdTipoMetrica, Date meFeRegistro, int meNuRango) {
        this.proyecto = proyecto;
        this.meIdTipoMetrica = meIdTipoMetrica;
        this.meFeRegistro = meFeRegistro;
        this.meNuRango = meNuRango;
    }

    public Metrica(Proyecto proyecto, int meIdTipoMetrica, Date meFeRegistro, int meNuRango, Set detalleMetricas) {
        this.proyecto = proyecto;
        this.meIdTipoMetrica = meIdTipoMetrica;
        this.meFeRegistro = meFeRegistro;
        this.meNuRango = meNuRango;
        this.detalleMetricas = detalleMetricas;
    }

    public Integer getMeIdMetricaPk() {
        return meIdMetricaPk;
    }

    public void setMeIdMetricaPk(Integer meIdMetricaPk) {
        this.meIdMetricaPk = meIdMetricaPk;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Integer getMeIdTipoMetrica() {
        return meIdTipoMetrica;
    }

    public void setMeIdTipoMetrica(Integer meIdTipoMetrica) {
        this.meIdTipoMetrica = meIdTipoMetrica;
    }

    public Date getMeFeRegistro() {
        return meFeRegistro;
    }

    public void setMeFeRegistro(Date meFeRegistro) {
        this.meFeRegistro = meFeRegistro;
    }

    public Integer getMeNuRango() {
        return meNuRango;
    }

    public void setMeNuRango(Integer meNuRango) {
        this.meNuRango = meNuRango;
    }

    public Set getDetalleMetricas() {
        return detalleMetricas;
    }

    public void setDetalleMetricas(Set detalleMetricas) {
        this.detalleMetricas = detalleMetricas;
    }

    
    
}
