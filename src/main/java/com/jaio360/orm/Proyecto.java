package com.jaio360.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Proyecto implements java.io.Serializable {

    private Integer poIdProyectoPk;
    private Usuario usuario;
    private String poTxDescripcion;
    private Integer poIdEstado;
    private Date poFeRegistro;
    private Date poFeEjecucion;
    private Integer poIdMetodologia;
    private String poTxMotivo;
    private Boolean poInOculto;
    private Set redEvaluacions = new HashSet(0);
    private Set relacions = new HashSet(0);
    private Set metricas = new HashSet(0);
    private Set cuestionarios = new HashSet(0);
    private Set cuestionarioEvaluados = new HashSet(0);
    private Set resultados = new HashSet(0);
    private Set participantes = new HashSet(0);
    private Set mensajes = new HashSet(0);

    public Integer getPoIdProyectoPk() {
        return poIdProyectoPk;
    }

    public void setPoIdProyectoPk(Integer poIdProyectoPk) {
        this.poIdProyectoPk = poIdProyectoPk;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPoTxDescripcion() {
        return poTxDescripcion;
    }

    public void setPoTxDescripcion(String poTxDescripcion) {
        this.poTxDescripcion = poTxDescripcion;
    }

    public Integer getPoIdEstado() {
        return poIdEstado;
    }

    public void setPoIdEstado(Integer poIdEstado) {
        this.poIdEstado = poIdEstado;
    }

    public Date getPoFeRegistro() {
        return poFeRegistro;
    }

    public void setPoFeRegistro(Date poFeRegistro) {
        this.poFeRegistro = poFeRegistro;
    }

    public Date getPoFeEjecucion() {
        return poFeEjecucion;
    }

    public void setPoFeEjecucion(Date poFeEjecucion) {
        this.poFeEjecucion = poFeEjecucion;
    }

    public Integer getPoIdMetodologia() {
        return poIdMetodologia;
    }

    public void setPoIdMetodologia(Integer poIdMetodologia) {
        this.poIdMetodologia = poIdMetodologia;
    }

    public String getPoTxMotivo() {
        return poTxMotivo;
    }

    public void setPoTxMotivo(String poTxMotivo) {
        this.poTxMotivo = poTxMotivo;
    }

    public Boolean getPoInOculto() {
        return poInOculto;
    }

    public void setPoInOculto(Boolean poInOculto) {
        this.poInOculto = poInOculto;
    }

    public Set getRedEvaluacions() {
        return redEvaluacions;
    }

    public void setRedEvaluacions(Set redEvaluacions) {
        this.redEvaluacions = redEvaluacions;
    }

    public Set getRelacions() {
        return relacions;
    }

    public void setRelacions(Set relacions) {
        this.relacions = relacions;
    }

    public Set getMetricas() {
        return metricas;
    }

    public void setMetricas(Set metricas) {
        this.metricas = metricas;
    }

    public Set getCuestionarios() {
        return cuestionarios;
    }

    public void setCuestionarios(Set cuestionarios) {
        this.cuestionarios = cuestionarios;
    }

    public Set getCuestionarioEvaluados() {
        return cuestionarioEvaluados;
    }

    public void setCuestionarioEvaluados(Set cuestionarioEvaluados) {
        this.cuestionarioEvaluados = cuestionarioEvaluados;
    }

    public Set getResultados() {
        return resultados;
    }

    public void setResultados(Set resultados) {
        this.resultados = resultados;
    }

    public Set getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Set participantes) {
        this.participantes = participantes;
    }

    public Set getMensajes() {
        return mensajes;
    }

    public void setMensajes(Set mensajes) {
        this.mensajes = mensajes;
    }

    
    public Proyecto() {
    }

    public Proyecto(Usuario usuario, String poTxDescripcion, Integer poIdEstado, Date poFeRegistro) {
        this.usuario = usuario;
        this.poTxDescripcion = poTxDescripcion;
        this.poIdEstado = poIdEstado;
        this.poFeRegistro = poFeRegistro;
    }

    public Proyecto(Usuario usuario, String poTxDescripcion, Integer poIdEstado, Date poFeRegistro, Date poFeEjecucion, Integer poIdMetodologia, Set redEvaluacions, Set relacions, Set metricas, Set cuestionarios, Set cuestionarioEvaluados, Set resultados, Set participantes, Set mensajes) {
        this.usuario = usuario;
        this.poTxDescripcion = poTxDescripcion;
        this.poIdEstado = poIdEstado;
        this.poFeRegistro = poFeRegistro;
        this.poFeEjecucion = poFeEjecucion;
        this.poIdMetodologia = poIdMetodologia;
        this.redEvaluacions = redEvaluacions;
        this.relacions = relacions;
        this.metricas = metricas;
        this.cuestionarios = cuestionarios;
        this.cuestionarioEvaluados = cuestionarioEvaluados;
        this.resultados = resultados;
        this.participantes = participantes;
        this.mensajes = mensajes;
    }

}
