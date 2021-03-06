package com.jaio360.orm;
// Generated 21/10/2014 08:38:36 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * RedEvaluacion generated by hbm2java
 */
public class RedEvaluacion  implements java.io.Serializable {


     private Integer reIdParticipantePk;
     private Proyecto proyecto;
     private String reTxDescripcion;
     private String reTxCorreo;
     private Integer reIdTipoParticipante;
     private Integer reIdEstado;
     private String reTxNombreCargo;
     
     private String reTxSexo;
     private Integer reNrEdad;
     private Integer reNrTiempoTrabajo;
     private String reTxOcupacion;
     private String reTxAreaNegocio;
        
     private Set relacionParticipantes = new HashSet(0);
     private Integer intCorrelativo;

    public RedEvaluacion() {
    }

	
    public RedEvaluacion(Proyecto proyecto, String reTxDescripcion, String reTxCorreo, Integer reIdTipoParticipante, Integer reIdEstado) {
        this.proyecto = proyecto;
        this.reTxDescripcion = reTxDescripcion;
        this.reTxCorreo = reTxCorreo;
        this.reIdTipoParticipante = reIdTipoParticipante;
        this.reIdEstado = reIdEstado;
    }
    public RedEvaluacion(Proyecto proyecto, String reTxDescripcion, String reTxCorreo, Integer reIdTipoParticipante, Integer reIdEstado, Set relacionParticipantes) {
       this.proyecto = proyecto;
       this.reTxDescripcion = reTxDescripcion;
       this.reTxCorreo = reTxCorreo;
       this.reIdTipoParticipante = reIdTipoParticipante;
       this.reIdEstado = reIdEstado;
       this.relacionParticipantes = relacionParticipantes;
    }

    public String getReTxSexo() {
        return reTxSexo;
    }

    public void setReTxSexo(String reTxSexo) {
        this.reTxSexo = reTxSexo;
    }

    public Integer getReNrEdad() {
        return reNrEdad;
    }

    public void setReNrEdad(Integer reNrEdad) {
        this.reNrEdad = reNrEdad;
    }

    public Integer getReNrTiempoTrabajo() {
        return reNrTiempoTrabajo;
    }

    public void setReNrTiempoTrabajo(Integer reNrTiempoTrabajo) {
        this.reNrTiempoTrabajo = reNrTiempoTrabajo;
    }

    public String getReTxOcupacion() {
        return reTxOcupacion;
    }

    public void setReTxOcupacion(String reTxOcupacion) {
        this.reTxOcupacion = reTxOcupacion;
    }

    public String getReTxAreaNegocio() {
        return reTxAreaNegocio;
    }

    public void setReTxAreaNegocio(String reTxAreaNegocio) {
        this.reTxAreaNegocio = reTxAreaNegocio;
    }

    public String getReTxNombreCargo() {
        return reTxNombreCargo;
    }

    public void setReTxNombreCargo(String reTxNombreCargo) {
        this.reTxNombreCargo = reTxNombreCargo;
    }
   
    public Integer getReIdParticipantePk() {
        return this.reIdParticipantePk;
    }
    
    public void setReIdParticipantePk(Integer reIdParticipantePk) {
        this.reIdParticipantePk = reIdParticipantePk;
    }
    public Proyecto getProyecto() {
        return this.proyecto;
    }
    
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    public String getReTxDescripcion() {
        return this.reTxDescripcion;
    }
    
    public void setReTxDescripcion(String reTxDescripcion) {
        this.reTxDescripcion = reTxDescripcion;
    }
    public String getReTxCorreo() {
        return this.reTxCorreo;
    }
    
    public void setReTxCorreo(String reTxCorreo) {
        this.reTxCorreo = reTxCorreo;
    }
    public Integer getReIdTipoParticipante() {
        return this.reIdTipoParticipante;
    }
    
    public void setReIdTipoParticipante(Integer reIdTipoParticipante) {
        this.reIdTipoParticipante = reIdTipoParticipante;
    }
    public Integer getReIdEstado() {
        return this.reIdEstado;
    }
    
    public void setReIdEstado(Integer reIdEstado) {
        this.reIdEstado = reIdEstado;
    }
    public Set getRelacionParticipantes() {
        return this.relacionParticipantes;
    }
    
    public void setRelacionParticipantes(Set relacionParticipantes) {
        this.relacionParticipantes = relacionParticipantes;
    }

    public Integer getIntCorrelativo() {
        return intCorrelativo;
    }

    public void setIntCorrelativo(Integer intCorrelativo) {
        this.intCorrelativo = intCorrelativo;
    }

}


