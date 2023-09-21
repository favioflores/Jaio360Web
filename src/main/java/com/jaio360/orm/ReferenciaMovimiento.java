package com.jaio360.orm;

public class ReferenciaMovimiento implements java.io.Serializable {

    private Integer rmIdReferenciaMovPk;
    private String rmTxComentario;
    private Movimiento movimiento;
    private Integer poIdProyectoFk;

    public Integer getRmIdReferenciaMovPk() {
        return rmIdReferenciaMovPk;
    }

    public void setRmIdReferenciaMovPk(Integer rmIdReferenciaMovPk) {
        this.rmIdReferenciaMovPk = rmIdReferenciaMovPk;
    }

    public String getRmTxComentario() {
        return rmTxComentario;
    }

    public void setRmTxComentario(String rmTxComentario) {
        this.rmTxComentario = rmTxComentario;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Integer getPoIdProyectoFk() {
        return poIdProyectoFk;
    }

    public void setPoIdProyectoFk(Integer poIdProyectoFk) {
        this.poIdProyectoFk = poIdProyectoFk;
    }
    
    

    public ReferenciaMovimiento() {
    }
    
}
