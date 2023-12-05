package com.jaio360.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Usuario implements java.io.Serializable {

    private Integer usIdCuentaPk;
    private Ubigeo ubigeo;
    private String usIdMail;
    private String usTxContrasenia;
    private Integer usIdEstado;
    private Integer usIdTipoCuenta;
    private byte[] usBlImagenEmpresa;
    private String usTxNombreRazonsocial;
    private String usTxDescripcionEmpresa;
    private Date usFeNacimiento;
    private Integer usIdTipoDocumento;
    private String usTxDocumento;
    private Date usFeRegistro;
    private Set contratos = new HashSet(0);
    private Set historialAccesos = new HashSet(0);
    private Set proyectos = new HashSet(0);
    private Set movimientos = new HashSet(0);
    private Set manageUserRelations = new HashSet(0);
    private Set reporteGenerados = new HashSet(0);

    public Usuario() {
    }

    public Usuario(Ubigeo ubigeo, String usIdMail, String usTxContrasenia, Integer usIdEstado, Integer usIdTipoCuenta, String usTxNombreRazonsocial, String usTxDescripcionEmpresa, Date usFeNacimiento, Integer usIdTipoDocumento, String usTxDocumento, Date usFeRegistro) {
        this.ubigeo = ubigeo;
        this.usIdMail = usIdMail;
        this.usTxContrasenia = usTxContrasenia;
        this.usIdEstado = usIdEstado;
        this.usIdTipoCuenta = usIdTipoCuenta;
        this.usTxNombreRazonsocial = usTxNombreRazonsocial;
        this.usTxDescripcionEmpresa = usTxDescripcionEmpresa;
        this.usFeNacimiento = usFeNacimiento;
        this.usIdTipoDocumento = usIdTipoDocumento;
        this.usTxDocumento = usTxDocumento;
        this.usFeRegistro = usFeRegistro;
    }

    public Usuario(Ubigeo ubigeo, String usIdMail, String usTxContrasenia, Integer usIdEstado, Integer usIdTipoCuenta, byte[] usBlImagenEmpresa, String usTxNombreRazonsocial, String usTxDescripcionEmpresa, Date usFeNacimiento, Integer usIdTipoDocumento, String usTxDocumento, Date usFeRegistro, Set contratos, Set historialAccesos, Set proyectos) {
        this.ubigeo = ubigeo;
        this.usIdMail = usIdMail;
        this.usTxContrasenia = usTxContrasenia;
        this.usIdEstado = usIdEstado;
        this.usIdTipoCuenta = usIdTipoCuenta;
        this.usBlImagenEmpresa = usBlImagenEmpresa;
        this.usTxNombreRazonsocial = usTxNombreRazonsocial;
        this.usTxDescripcionEmpresa = usTxDescripcionEmpresa;
        this.usFeNacimiento = usFeNacimiento;
        this.usIdTipoDocumento = usIdTipoDocumento;
        this.usTxDocumento = usTxDocumento;
        this.usFeRegistro = usFeRegistro;
        this.contratos = contratos;
        this.historialAccesos = historialAccesos;
        this.proyectos = proyectos;
    }

    public Integer getUsIdCuentaPk() {
        return usIdCuentaPk;
    }

    public void setUsIdCuentaPk(Integer usIdCuentaPk) {
        this.usIdCuentaPk = usIdCuentaPk;
    }

    public Ubigeo getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(Ubigeo ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getUsIdMail() {
        return usIdMail;
    }

    public void setUsIdMail(String usIdMail) {
        this.usIdMail = usIdMail;
    }

    public String getUsTxContrasenia() {
        return usTxContrasenia;
    }

    public void setUsTxContrasenia(String usTxContrasenia) {
        this.usTxContrasenia = usTxContrasenia;
    }

    public Integer getUsIdEstado() {
        return usIdEstado;
    }

    public void setUsIdEstado(Integer usIdEstado) {
        this.usIdEstado = usIdEstado;
    }

    public Integer getUsIdTipoCuenta() {
        return usIdTipoCuenta;
    }

    public void setUsIdTipoCuenta(Integer usIdTipoCuenta) {
        this.usIdTipoCuenta = usIdTipoCuenta;
    }

    public byte[] getUsBlImagenEmpresa() {
        return usBlImagenEmpresa;
    }

    public void setUsBlImagenEmpresa(byte[] usBlImagenEmpresa) {
        this.usBlImagenEmpresa = usBlImagenEmpresa;
    }

    public String getUsTxNombreRazonsocial() {
        return usTxNombreRazonsocial;
    }

    public void setUsTxNombreRazonsocial(String usTxNombreRazonsocial) {
        this.usTxNombreRazonsocial = usTxNombreRazonsocial;
    }

    public String getUsTxDescripcionEmpresa() {
        return usTxDescripcionEmpresa;
    }

    public void setUsTxDescripcionEmpresa(String usTxDescripcionEmpresa) {
        this.usTxDescripcionEmpresa = usTxDescripcionEmpresa;
    }

    public Date getUsFeNacimiento() {
        return usFeNacimiento;
    }

    public void setUsFeNacimiento(Date usFeNacimiento) {
        this.usFeNacimiento = usFeNacimiento;
    }

    public Integer getUsIdTipoDocumento() {
        return usIdTipoDocumento;
    }

    public void setUsIdTipoDocumento(Integer usIdTipoDocumento) {
        this.usIdTipoDocumento = usIdTipoDocumento;
    }

    public String getUsTxDocumento() {
        return usTxDocumento;
    }

    public void setUsTxDocumento(String usTxDocumento) {
        this.usTxDocumento = usTxDocumento;
    }

    public Date getUsFeRegistro() {
        return usFeRegistro;
    }

    public void setUsFeRegistro(Date usFeRegistro) {
        this.usFeRegistro = usFeRegistro;
    }

    public Set getReporteGenerados() {
        return reporteGenerados;
    }

    public void setReporteGenerados(Set reporteGenerados) {
        this.reporteGenerados = reporteGenerados;
    }

    public Set getContratos() {
        return contratos;
    }

    public void setContratos(Set contratos) {
        this.contratos = contratos;
    }

    public Set getHistorialAccesos() {
        return historialAccesos;
    }

    public void setHistorialAccesos(Set historialAccesos) {
        this.historialAccesos = historialAccesos;
    }

    public Set getProyectos() {
        return proyectos;
    }

    public void setProyectos(Set proyectos) {
        this.proyectos = proyectos;
    }

    public Set getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Set movimientos) {
        this.movimientos = movimientos;
    }

    public Set getManageUserRelations() {
        return manageUserRelations;
    }

    public void setManageUserRelations(Set manageUserRelations) {
        this.manageUserRelations = manageUserRelations;
    }

}
