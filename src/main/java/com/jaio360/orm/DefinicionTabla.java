package com.jaio360.orm;

import java.util.HashSet;
import java.util.Set;

public class DefinicionTabla implements java.io.Serializable {

    private Integer dtIdDefinicionPk;
    private String dtTxDescripcion;
    private String dtTxComentario;
    private Set elementos = new HashSet(0);

    public DefinicionTabla() {
    }

    public Integer getDtIdDefinicionPk() {
        return dtIdDefinicionPk;
    }

    public void setDtIdDefinicionPk(Integer dtIdDefinicionPk) {
        this.dtIdDefinicionPk = dtIdDefinicionPk;
    }

    public String getDtTxDescripcion() {
        return dtTxDescripcion;
    }

    public void setDtTxDescripcion(String dtTxDescripcion) {
        this.dtTxDescripcion = dtTxDescripcion;
    }

    public String getDtTxComentario() {
        return dtTxComentario;
    }

    public void setDtTxComentario(String dtTxComentario) {
        this.dtTxComentario = dtTxComentario;
    }

    public Set getElementos() {
        return elementos;
    }

    public void setElementos(Set elementos) {
        this.elementos = elementos;
    }

}
