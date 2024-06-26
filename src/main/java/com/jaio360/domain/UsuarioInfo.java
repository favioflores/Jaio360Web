package com.jaio360.domain;

import com.jaio360.dao.HistorialAccesoDAO;
import com.jaio360.orm.ManageUserRelation;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.view.BaseView;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UsuarioInfo extends BaseView implements Serializable {

    private Integer intUsuarioPk;
    private String strEmail;
    private String strTipoUsuario;
    private String strFechaRegistro;
    private String strDescripcion;
    private String strUltimaConexion;
    private String strIntentosErrados;
    private String strEmpresaDesc;
    private String strDocumentoEmpresa;
    private String strEstadoDesc;
    private Integer strEstadoId;
    private Integer intIdDocumentoEmpresa;
    private Integer intIdCiudad;
    private Integer intHistorialPk;
    private String strTimeRemaining;
    private String timeClient;
    private Integer intEvaluationPreferenceView;

    private Boolean ManagingDirector = false;
    private Boolean CountryManager = false;
    private Boolean ProjectManager = false;
    private Boolean EvaluatedEvaluator = false;

    private Usuario usuario;

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

    public String getStrFechaRegistro() {
        return strFechaRegistro;
    }

    public void setStrFechaRegistro(String strFechaRegistro) {
        this.strFechaRegistro = strFechaRegistro;
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

    public String getStrEmpresaDesc() {
        return strEmpresaDesc;
    }

    public void setStrEmpresaDesc(String strEmpresaDesc) {
        this.strEmpresaDesc = strEmpresaDesc;
    }

    public String getStrDocumentoEmpresa() {
        return strDocumentoEmpresa;
    }

    public void setStrDocumentoEmpresa(String strDocumentoEmpresa) {
        this.strDocumentoEmpresa = strDocumentoEmpresa;
    }

    public String getStrEstadoDesc() {
        return strEstadoDesc;
    }

    public void setStrEstadoDesc(String strEstadoDesc) {
        this.strEstadoDesc = strEstadoDesc;
    }

    public Integer getStrEstadoId() {
        return strEstadoId;
    }

    public void setStrEstadoId(Integer strEstadoId) {
        this.strEstadoId = strEstadoId;
    }

    public Integer getIntIdDocumentoEmpresa() {
        return intIdDocumentoEmpresa;
    }

    public void setIntIdDocumentoEmpresa(Integer intIdDocumentoEmpresa) {
        this.intIdDocumentoEmpresa = intIdDocumentoEmpresa;
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

    public String getStrTimeRemaining() {
        return strTimeRemaining;
    }

    public void setStrTimeRemaining(String strTimeRemaining) {
        this.strTimeRemaining = strTimeRemaining;
    }

    public String getTimeClient() {
        return timeClient;
    }

    public void setTimeClient(String timeClient) {
        this.timeClient = timeClient;
    }

    public Integer getIntEvaluationPreferenceView() {
        return intEvaluationPreferenceView;
    }

    public void setIntEvaluationPreferenceView(Integer intEvaluationPreferenceView) {
        this.intEvaluationPreferenceView = intEvaluationPreferenceView;
    }

    public Boolean getManagingDirector() {
        return ManagingDirector;
    }

    public void setManagingDirector(Boolean ManagingDirector) {
        this.ManagingDirector = ManagingDirector;
    }

    public Boolean getCountryManager() {
        return CountryManager;
    }

    public void setCountryManager(Boolean CountryManager) {
        this.CountryManager = CountryManager;
    }

    public Boolean getProjectManager() {
        return ProjectManager;
    }

    public void setProjectManager(Boolean ProjectManager) {
        this.ProjectManager = ProjectManager;
    }

    public Boolean getEvaluatedEvaluator() {
        return EvaluatedEvaluator;
    }

    public void setEvaluatedEvaluator(Boolean EvaluatedEvaluator) {
        this.EvaluatedEvaluator = EvaluatedEvaluator;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioInfo(Usuario objUsuario, ManageUserRelation objManageUserRelation, boolean isForLogin) {

        this.intUsuarioPk = objUsuario.getUsIdCuentaPk();
        this.strEmail = objUsuario.getUsIdMail();

        this.strTipoUsuario = msg(objUsuario.getUsIdTipoCuenta().toString());
        this.strDescripcion = objUsuario.getUsTxNombreRazonsocial();
        this.strEmpresaDesc = objUsuario.getUsTxDescripcionEmpresa();
        this.strEstadoDesc = msg(objUsuario.getUsIdEstado().toString());
        this.strEstadoId = objUsuario.getUsIdEstado();
        this.intIdDocumentoEmpresa = objUsuario.getUsIdTipoDocumento();
        this.strDocumentoEmpresa = objUsuario.getUsTxDocumento();
        this.intIdCiudad = objUsuario.getUbigeo().getUbIdUbigeoPk();
        this.usuario = objUsuario;
        this.strFechaRegistro = Utilitarios.formatearFecha(objUsuario.getUsFeRegistro(), Constantes.DDMMYYYY);
        this.intEvaluationPreferenceView = 0;

        if (isForLogin) {

            HistorialAccesoDAO objHistorialAccesoDAO = new HistorialAccesoDAO();
            Date dtUltimoAcceso = objHistorialAccesoDAO.obtenUltimoAcceso(intUsuarioPk);

            if (dtUltimoAcceso != null) {
                this.strUltimaConexion = Utilitarios.formatearFecha(dtUltimoAcceso, Constantes.DDMMYYYY);
            } else {
                this.strUltimaConexion = "Sin accesos previos";
            }
            this.strIntentosErrados = "0";
            this.intHistorialPk = -1;
        } else {
            if (objManageUserRelation != null) {
                if (!objManageUserRelation.getMaIsVerified()) {
                    long diferencia = objManageUserRelation.getMaFeVerificationExpired().getTime() - (new Date()).getTime();
                    long horas = TimeUnit.MILLISECONDS.toHours(diferencia);
                    if (horas <= 0) {
                        this.strTimeRemaining = 0 + msg("hours.acro");
                    } else {
                        this.strTimeRemaining = horas + msg("hours.acro");
                    }
                } else {
                    this.strTimeRemaining = 0 + msg("hours.acro");
                }
            }
        }

        if (objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_MANAGING_DIRECTOR)) {
            this.ManagingDirector = true;
            this.CountryManager = false;
            this.ProjectManager = false;
            this.EvaluatedEvaluator = false;
        } else if (objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_COUNTRY_MANAGER)) {
            this.ManagingDirector = false;
            this.CountryManager = true;
            this.ProjectManager = false;
            this.EvaluatedEvaluator = false;
        } else if (objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_PROJECT_MANAGER)) {
            this.ManagingDirector = false;
            this.CountryManager = false;
            this.ProjectManager = true;
            this.EvaluatedEvaluator = false;
        } else {
            this.ManagingDirector = false;
            this.CountryManager = false;
            this.ProjectManager = false;
            this.EvaluatedEvaluator = true;
        }

    }

}
