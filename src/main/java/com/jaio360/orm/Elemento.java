package com.jaio360.orm;

public class Elemento implements java.io.Serializable {

    private Integer elIdElementoPk;
    private DefinicionTabla definicionTabla;
    private int elInActivo;
    private int elNuOrden;
    private String elTxDescripcion;
    private Integer elInUsaEscala;
    private String elTxValor1;
    private byte[] elCadena;

    public Integer getElIdElementoPk() {
        return elIdElementoPk;
    }

    public void setElIdElementoPk(Integer elIdElementoPk) {
        this.elIdElementoPk = elIdElementoPk;
    }

    public DefinicionTabla getDefinicionTabla() {
        return definicionTabla;
    }

    public void setDefinicionTabla(DefinicionTabla definicionTabla) {
        this.definicionTabla = definicionTabla;
    }

    public int getElInActivo() {
        return elInActivo;
    }

    public void setElInActivo(int elInActivo) {
        this.elInActivo = elInActivo;
    }

    public int getElNuOrden() {
        return elNuOrden;
    }

    public void setElNuOrden(int elNuOrden) {
        this.elNuOrden = elNuOrden;
    }

    public String getElTxDescripcion() {
        return elTxDescripcion;
    }

    public void setElTxDescripcion(String elTxDescripcion) {
        this.elTxDescripcion = elTxDescripcion;
    }

    public Integer getElInUsaEscala() {
        return elInUsaEscala;
    }

    public void setElInUsaEscala(Integer elInUsaEscala) {
        this.elInUsaEscala = elInUsaEscala;
    }

    public String getElTxValor1() {
        return elTxValor1;
    }

    public void setElTxValor1(String elTxValor1) {
        this.elTxValor1 = elTxValor1;
    }

    public byte[] getElCadena() {
        return elCadena;
    }

    public void setElCadena(byte[] elCadena) {
        this.elCadena = elCadena;
    }

    public Elemento() {
    }

    public Elemento(DefinicionTabla definicionTabla, int elInActivo, int elNuOrden, String elTxDescripcion) {
        this.definicionTabla = definicionTabla;
        this.elInActivo = elInActivo;
        this.elNuOrden = elNuOrden;
        this.elTxDescripcion = elTxDescripcion;
    }

    public Elemento(DefinicionTabla definicionTabla, int elInActivo, int elNuOrden, String elTxDescripcion, Integer elInUsaEscala, String elTxValor1, byte[] elCadena) {
        this.definicionTabla = definicionTabla;
        this.elInActivo = elInActivo;
        this.elNuOrden = elNuOrden;
        this.elTxDescripcion = elTxDescripcion;
        this.elInUsaEscala = elInUsaEscala;
        this.elTxValor1 = elTxValor1;
        this.elCadena = elCadena;
    }

}
