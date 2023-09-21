package com.jaio360.orm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Transient;

public class Componente implements java.io.Serializable {

    private Integer coIdComponentePk;
    private Cuestionario cuestionario;
    private Componente componente;
    private Integer coIdTipoComponente;
    private String coTxDescripcion;
    private Set<Componente> componentes = new HashSet<>(0);
    private Set<Resultado> resultados = new HashSet<>(0);
    private String posicion;

    public Integer getCoIdComponentePk() {
        return coIdComponentePk;
    }

    public void setCoIdComponentePk(Integer coIdComponentePk) {
        this.coIdComponentePk = coIdComponentePk;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public Integer getCoIdTipoComponente() {
        return coIdTipoComponente;
    }

    public void setCoIdTipoComponente(Integer coIdTipoComponente) {
        this.coIdTipoComponente = coIdTipoComponente;
    }

    public String getCoTxDescripcion() {
        return coTxDescripcion;
    }

    public void setCoTxDescripcion(String coTxDescripcion) {
        this.coTxDescripcion = coTxDescripcion;
    }

    public Set<Componente> getComponentes() {
        return componentes;
    }

    public void setComponentes(Set<Componente> componentes) {
        this.componentes = componentes;
    }

    public Set<Resultado> getResultados() {
        return resultados;
    }

    public void setResultados(Set<Resultado> resultados) {
        this.resultados = resultados;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public List<Componente> getStrcomponentes() {
        return Strcomponentes;
    }

    public void setStrcomponentes(List<Componente> Strcomponentes) {
        this.Strcomponentes = Strcomponentes;
    }
    @Transient
    private List<Componente> Strcomponentes = new ArrayList<Componente>();

    public Componente() {
    }

    public Componente(Cuestionario cuestionario, Integer coIdTipoComponente, String coTxDescripcion) {
        this.cuestionario = cuestionario;
        this.coIdTipoComponente = coIdTipoComponente;
        this.coTxDescripcion = coTxDescripcion;
    }

    public Componente(Cuestionario cuestionario, Componente componente, Integer coIdTipoComponente, String coTxDescripcion, Set<Componente> componentes, Set<Resultado> resultados) {
        this.cuestionario = cuestionario;
        this.componente = componente;
        this.coIdTipoComponente = coIdTipoComponente;
        this.coTxDescripcion = coTxDescripcion;
        this.componentes = componentes;
        this.resultados = resultados;
    }

}
