package com.jaio360.orm;
// Generated 21/10/2014 08:38:36 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Participante generated by hbm2java
 */
public class Participante  implements java.io.Serializable {


     private Integer paIdParticipantePk;
     private Proyecto proyecto;
     private String paTxDescripcion;
     private String paTxCorreo;
     private String paTxNombreCargo;
     private Boolean paInRedCargada;
     private Boolean paInRedVerificada;
     private Integer paIdTipoParticipante;
     private Boolean paInAutoevaluar;
     private Integer paIdEstado;
     private String paTxSexo;
     private Integer paNrEdad;
     private Integer paNrTiempoTrabajo;
     private String paTxOcupacion;
     private String paTxAreaNegocio;
     private Integer paNrAnalisisDescarga;
     
     private Set relacionParticipantes = new HashSet(0);
     private Set cuestionarioEvaluados = new HashSet(0);

    public Participante() {
    }

    public Integer getPaNrAnalisisDescarga() {
        return paNrAnalisisDescarga;
    }

    public void setPaNrAnalisisDescarga(Integer paNrAnalisisDescarga) {
        this.paNrAnalisisDescarga = paNrAnalisisDescarga;
    }

	
    public Participante(Proyecto proyecto, String paTxDescripcion, String paTxCorreo, String paTxNombreCargo, int paIdTipoParticipante, int paIdEstado) {
        this.proyecto = proyecto;
        this.paTxDescripcion = paTxDescripcion;
        this.paTxCorreo = paTxCorreo;
        this.paTxNombreCargo = paTxNombreCargo;
        this.paIdTipoParticipante = paIdTipoParticipante;
        this.paIdEstado = paIdEstado;
    }
    public Participante(Proyecto proyecto, String paTxDescripcion, String paTxCorreo, String paTxNombreCargo, Boolean paInRedCargada, Boolean paInRedVerificada, int paIdTipoParticipante, Boolean paInAutoevaluar, int paIdEstado, Set relacionParticipantes, Set cuestionarioEvaluados) {
       this.proyecto = proyecto;
       this.paTxDescripcion = paTxDescripcion;
       this.paTxCorreo = paTxCorreo;
       this.paTxNombreCargo = paTxNombreCargo;
       this.paInRedCargada = paInRedCargada;
       this.paInRedVerificada = paInRedVerificada;
       this.paIdTipoParticipante = paIdTipoParticipante;
       this.paInAutoevaluar = paInAutoevaluar;
       this.paIdEstado = paIdEstado;
       this.relacionParticipantes = relacionParticipantes;
       this.cuestionarioEvaluados = cuestionarioEvaluados;
    }

    public Integer getPaIdTipoParticipante() {
        return paIdTipoParticipante;
    }

    public void setPaIdTipoParticipante(Integer paIdTipoParticipante) {
        this.paIdTipoParticipante = paIdTipoParticipante;
    }

    public String getPaTxSexo() {
        return paTxSexo;
    }

    public void setPaTxSexo(String paTxSexo) {
        this.paTxSexo = paTxSexo;
    }

    public Integer getPaNrEdad() {
        return paNrEdad;
    }

    public void setPaNrEdad(Integer paNrEdad) {
        this.paNrEdad = paNrEdad;
    }

    public Integer getPaNrTiempoTrabajo() {
        return paNrTiempoTrabajo;
    }

    public void setPaNrTiempoTrabajo(Integer paNrTiempoTrabajo) {
        this.paNrTiempoTrabajo = paNrTiempoTrabajo;
    }

    public String getPaTxOcupacion() {
        return paTxOcupacion;
    }

    public void setPaTxOcupacion(String paTxOcupacion) {
        this.paTxOcupacion = paTxOcupacion;
    }

    public String getPaTxAreaNegocio() {
        return paTxAreaNegocio;
    }

    public void setPaTxAreaNegocio(String paTxAreaNegocio) {
        this.paTxAreaNegocio = paTxAreaNegocio;
    }
    
    
   
    public Integer getPaIdParticipantePk() {
        return this.paIdParticipantePk;
    }
    
    public void setPaIdParticipantePk(Integer paIdParticipantePk) {
        this.paIdParticipantePk = paIdParticipantePk;
    }
    public Proyecto getProyecto() {
        return this.proyecto;
    }
    
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    public String getPaTxDescripcion() {
        return this.paTxDescripcion;
    }
    
    public void setPaTxDescripcion(String paTxDescripcion) {
        this.paTxDescripcion = paTxDescripcion;
    }
    public String getPaTxCorreo() {
        return this.paTxCorreo;
    }
    
    public void setPaTxCorreo(String paTxCorreo) {
        this.paTxCorreo = paTxCorreo;
    }
    public String getPaTxNombreCargo() {
        return this.paTxNombreCargo;
    }
    
    public void setPaTxNombreCargo(String paTxNombreCargo) {
        this.paTxNombreCargo = paTxNombreCargo;
    }
    public Boolean getPaInRedCargada() {
        return this.paInRedCargada;
    }
    
    public void setPaInRedCargada(Boolean paInRedCargada) {
        this.paInRedCargada = paInRedCargada;
    }
    public Boolean getPaInRedVerificada() {
        return this.paInRedVerificada;
    }
    
    public void setPaInRedVerificada(Boolean paInRedVerificada) {
        this.paInRedVerificada = paInRedVerificada;
    }
    
    
    public Boolean getPaInAutoevaluar() {
        return this.paInAutoevaluar;
    }
    
    public void setPaInAutoevaluar(Boolean paInAutoevaluar) {
        this.paInAutoevaluar = paInAutoevaluar;
    }
    public Integer getPaIdEstado() {
        return this.paIdEstado;
    }
    
    public void setPaIdEstado(Integer paIdEstado) {
        this.paIdEstado = paIdEstado;
    }
    public Set getRelacionParticipantes() {
        return this.relacionParticipantes;
    }
    
    public void setRelacionParticipantes(Set relacionParticipantes) {
        this.relacionParticipantes = relacionParticipantes;
    }
    public Set getCuestionarioEvaluados() {
        return this.cuestionarioEvaluados;
    }
    
    public void setCuestionarioEvaluados(Set cuestionarioEvaluados) {
        this.cuestionarioEvaluados = cuestionarioEvaluados;
    }




}


