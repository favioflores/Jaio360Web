package com.jaio360.orm;

public class Parametro  implements java.io.Serializable {

    private Integer paIdParametroPk;
    private Proyecto proyecto;
    private Integer paIdTipoParametro;
    private byte[] paTxPatron;

    public Integer getPaIdParametroPk() {
        return paIdParametroPk;
    }

    public void setPaIdParametroPk(Integer paIdParametroPk) {
        this.paIdParametroPk = paIdParametroPk;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Integer getPaIdTipoParametro() {
        return paIdTipoParametro;
    }

    public void setPaIdTipoParametro(Integer paIdTipoParametro) {
        this.paIdTipoParametro = paIdTipoParametro;
    }

    public byte[] getPaTxPatron() {
        return paTxPatron;
    }

    public void setPaTxPatron(byte[] paTxPatron) {
        this.paTxPatron = paTxPatron;
    }

}


