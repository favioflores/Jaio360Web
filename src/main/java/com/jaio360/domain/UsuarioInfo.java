/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.domain;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.HistorialAccesoDAO;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Favio
 */
public class UsuarioInfo implements Serializable{

    private Integer intUsuarioPk;
    private String strEmail;
    private String strTipoUsuario;
    private String strFechaRegistro;
    private String strDescripcion;
    private String strUltimaConexion;
    private String strIntentosErrados;
    private String strEmpresaDesc;
    private String strDocumentoEmpresa;
    private Integer intIdDocumentoEmpresa;
    private Integer intIdCiudad;
    private Integer intHistorialPk;
    private boolean boEsAdministrador = false;
    private boolean boEsUsuarioMaestro = false;
    private boolean boEsUsuarioEvaluador = false;
    private boolean boEsUsuarioEvaluado = false;
    private Usuario usuario;

    public String getStrFechaRegistro() {
        return strFechaRegistro;
    }

    public void setStrFechaRegistro(String strFechaRegistro) {
        this.strFechaRegistro = strFechaRegistro;
    }

    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getIntIdCiudad() {
        return intIdCiudad;
    }

    public void setIntIdCiudad(Integer intIdCiudad) {
        this.intIdCiudad = intIdCiudad;
    }

    
    public Integer getIntHistorialPk() {
        return intHistorialPk;
    }

    public void setIntHistorialPk(Integer intHistorialPk) {
        this.intHistorialPk = intHistorialPk;
    }

    public UsuarioInfo(Usuario objUsuario) {
        
        this.intUsuarioPk = objUsuario.getUsIdCuentaPk();
        this.strEmail = objUsuario.getUsIdMail();
        
        this.strTipoUsuario = EHCacheManager.obtenerDescripcionElemento(objUsuario.getUsIdTipoCuenta());
        this.strDescripcion = objUsuario.getUsTxNombreRazonsocial();
        this.strEmpresaDesc = objUsuario.getUsTxDescripcionEmpresa();
        this.intIdDocumentoEmpresa = objUsuario.getUsIdTipoDocumento();
        this.strDocumentoEmpresa = objUsuario.getUsTxDocumento();
        this.intIdCiudad = objUsuario.getUbigeo().getUbIdUbigeoPk();
        HistorialAccesoDAO objHistorialAccesoDAO = new HistorialAccesoDAO();
        Date dtUltimoAcceso = objHistorialAccesoDAO.obtenUltimoAcceso(intUsuarioPk);
        
        if(dtUltimoAcceso != null){
            this.strUltimaConexion = Utilitarios.formatearFecha(dtUltimoAcceso,Constantes.DDMMYYYY);
        }else{
            this.strUltimaConexion = "Sin accesos previos";
        }
        this.strIntentosErrados = "0";
        this.intHistorialPk = -1;
        
        if(objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_ADMINISTRADOR)){
            this.boEsAdministrador = true;
            this.boEsUsuarioMaestro= false;
            this.boEsUsuarioEvaluado = false;
            this.boEsUsuarioEvaluador = false;
        }else if(objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_USUARIO_MAESTRO)){
            this.boEsAdministrador = false;
            this.boEsUsuarioMaestro= true;
            this.boEsUsuarioEvaluado = false;
            this.boEsUsuarioEvaluador = false;
        }else{
            this.boEsAdministrador = false;
            this.boEsUsuarioMaestro= false;
            this.boEsUsuarioEvaluado = true;
            this.boEsUsuarioEvaluador = true;
        }
        
    }

    public UsuarioInfo(Usuario objUsuario , boolean flag) {
        
        this.intUsuarioPk = objUsuario.getUsIdCuentaPk();
        this.strEmail = objUsuario.getUsIdMail();
        
        this.strTipoUsuario = EHCacheManager.obtenerDescripcionElemento(objUsuario.getUsIdTipoCuenta());
        this.strDescripcion = objUsuario.getUsTxNombreRazonsocial();
        this.strEmpresaDesc = objUsuario.getUsTxDescripcionEmpresa();
        this.intIdDocumentoEmpresa = objUsuario.getUsIdTipoDocumento();
        this.strDocumentoEmpresa = objUsuario.getUsTxDocumento();
        this.intIdCiudad = objUsuario.getUbigeo().getUbIdUbigeoPk();
        this.usuario = objUsuario;
        this.strFechaRegistro = Utilitarios.formatearFecha(objUsuario.getUsFeRegistro(),Constantes.DDMMYYYY);
        
        if(objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_ADMINISTRADOR)){
            this.boEsAdministrador = true;
            this.boEsUsuarioMaestro= false;
            this.boEsUsuarioEvaluado = false;
            this.boEsUsuarioEvaluador = false;
        }else if(objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_USUARIO_MAESTRO)){
            this.boEsAdministrador = false;
            this.boEsUsuarioMaestro= true;
            this.boEsUsuarioEvaluado = false;
            this.boEsUsuarioEvaluador = false;
        }else{
            this.boEsAdministrador = false;
            this.boEsUsuarioMaestro= false;
            this.boEsUsuarioEvaluado = true;
            this.boEsUsuarioEvaluador = true;
        }
    
        this.usuario = objUsuario;
    }
    
    public Integer getIntIdDocumentoEmpresa() {
        return intIdDocumentoEmpresa;
    }

    public void setIntIdDocumentoEmpresa(Integer intIdDocumentoEmpresa) {
        this.intIdDocumentoEmpresa = intIdDocumentoEmpresa;
    }

    public String getStrDocumentoEmpresa() {
        return strDocumentoEmpresa;
    }

    public void setStrDocumentoEmpresa(String strDocumentoEmpresa) {
        this.strDocumentoEmpresa = strDocumentoEmpresa;
    }
    

    public Integer getIntUsuarioPk() {
        return intUsuarioPk;
    }

    public void setIntUsuarioPk(Integer intUsuarioPk) {
        this.intUsuarioPk = intUsuarioPk;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrTipoUsuario() {
        return strTipoUsuario;
    }

    public void setStrTipoUsuario(String strTipoUsuario) {
        this.strTipoUsuario = strTipoUsuario;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrUltimaConexion() {
        return strUltimaConexion;
    }

    public void setStrUltimaConexion(String strUltimaConexion) {
        this.strUltimaConexion = strUltimaConexion;
    }

    public String getStrIntentosErrados() {
        return strIntentosErrados;
    }

    public void setStrIntentosErrados(String strIntentosErrados) {
        this.strIntentosErrados = strIntentosErrados;
    }

    public boolean isBoEsAdministrador() {
        return boEsAdministrador;
    }

    public void setBoEsAdministrador(boolean boEsAdministrador) {
        this.boEsAdministrador = boEsAdministrador;
    }

    public boolean isBoEsUsuarioMaestro() {
        return boEsUsuarioMaestro;
    }

    public void setBoEsUsuarioMaestro(boolean boEsUsuarioMaestro) {
        this.boEsUsuarioMaestro = boEsUsuarioMaestro;
    }

    public boolean isBoEsUsuarioEvaluador() {
        return boEsUsuarioEvaluador;
    }

    public void setBoEsUsuarioEvaluador(boolean boEsUsuarioEvaluador) {
        this.boEsUsuarioEvaluador = boEsUsuarioEvaluador;
    }

    public boolean isBoEsUsuarioEvaluado() {
        return boEsUsuarioEvaluado;
    }

    public void setBoEsUsuarioEvaluado(boolean boEsUsuarioEvaluado) {
        this.boEsUsuarioEvaluado = boEsUsuarioEvaluado;
    }

    public String getStrEmpresaDesc() {
        return strEmpresaDesc;
    }

    public void setStrEmpresaDesc(String strEmpresaDesc) {
        this.strEmpresaDesc = strEmpresaDesc;
    }

}
