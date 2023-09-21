package com.jaio360.orm;

import java.util.HashSet;
import java.util.Set;

public class Ubigeo implements java.io.Serializable {

    private Integer ubIdUbigeoPk;
    private Ubigeo ubigeo;
    private String ubTxDescripcion;
    private Integer ubIdTipoUbigeo;
    private Set ubigeos = new HashSet(0);
    private Set usuarios = new HashSet(0);

    public Integer getUbIdUbigeoPk() {
        return ubIdUbigeoPk;
    }

    public void setUbIdUbigeoPk(Integer ubIdUbigeoPk) {
        this.ubIdUbigeoPk = ubIdUbigeoPk;
    }

    public Ubigeo getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(Ubigeo ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getUbTxDescripcion() {
        return ubTxDescripcion;
    }

    public void setUbTxDescripcion(String ubTxDescripcion) {
        this.ubTxDescripcion = ubTxDescripcion;
    }

    public Integer getUbIdTipoUbigeo() {
        return ubIdTipoUbigeo;
    }

    public void setUbIdTipoUbigeo(Integer ubIdTipoUbigeo) {
        this.ubIdTipoUbigeo = ubIdTipoUbigeo;
    }

    public Set getUbigeos() {
        return ubigeos;
    }

    public void setUbigeos(Set ubigeos) {
        this.ubigeos = ubigeos;
    }

    public Set getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set usuarios) {
        this.usuarios = usuarios;
    }

    public Ubigeo() {
    }

    public Ubigeo(Integer ubIdUbigeoPk, String ubTxDescripcion, Integer ubIdTipoUbigeo) {
        this.ubIdUbigeoPk = ubIdUbigeoPk;
        this.ubTxDescripcion = ubTxDescripcion;
        this.ubIdTipoUbigeo = ubIdTipoUbigeo;
    }

    public Ubigeo(Integer ubIdUbigeoPk, Ubigeo ubigeo, String ubTxDescripcion, Integer ubIdTipoUbigeo, Set ubigeos, Set usuarios) {
        this.ubIdUbigeoPk = ubIdUbigeoPk;
        this.ubigeo = ubigeo;
        this.ubTxDescripcion = ubTxDescripcion;
        this.ubIdTipoUbigeo = ubIdTipoUbigeo;
        this.ubigeos = ubigeos;
        this.usuarios = usuarios;
    }

}
