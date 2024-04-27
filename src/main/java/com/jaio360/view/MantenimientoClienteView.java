package com.jaio360.view;

import com.jaio360.application.MailSender;
import com.jaio360.dao.DestinatariosDAO;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.UbigeoDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.ManageUserRelation;
import com.jaio360.orm.Notificaciones;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.Ubigeo;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import static com.jaio360.view.BaseView.mostrarError;
import static com.jaio360.view.BaseView.msg;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.hibernate.HibernateException;

@ManagedBean(name = "mantenimientoClienteView")
@ViewScoped
public class MantenimientoClienteView extends BaseView implements Serializable {

    private static Logger log = Logger.getLogger(MantenimientoClienteView.class);

    private static final long serialVersionUID = -1L;

    private List<UsuarioInfo> lstUsuario;
    private List<UsuarioInfo> filteredUsuarios;
    private UsuarioInfo usuarioSeleccionado;
    private String[] tipoUsuario;
    private Integer intIdEstado;
    private Integer intIdTipoCuenta;
    private Integer intIdTipoDocumento;
    private List<SelectItem> lstEstados;
    private List<SelectItem> lstTipoCuenta;
    private List<SelectItem> lstTipoDocumento;
    private List<Ubigeo> lstPaises;
    private List<Ubigeo> lstCiudades;
    private Integer usIdCuentaPk;
    private String usIdMail;
    private String usTxContrasenia;
    private Integer usIdEstado;
    private Integer usIdTipoCuenta;
    private String usTxNombreRazonsocial;
    private String usTxDescripcionEmpresa;
    private Integer usIdTipoDocumento;
    private String usTxDocumento;
    private Boolean isEdit;
    private Integer pais;
    private Integer ciudad;
    private String strContraseniaNueva;
    private String strContraseniaReNueva;
    private Integer usIdCuentaPkCP;
    private String strContraseniaNuevaCP;
    private String strContraseniaReNuevaCP;
    private List<ProyectoInfo> lstProjectsClient;

    private ElementoDAO objElementoDAO = new ElementoDAO();
    private UsuarioDAO objUsuarioDAO = new UsuarioDAO();

    private boolean blValidNewCustomer;
    private boolean blIsRegistered;

    public List<UsuarioInfo> getLstUsuario() {
        return lstUsuario;
    }

    public void setLstUsuario(List<UsuarioInfo> lstUsuario) {
        this.lstUsuario = lstUsuario;
    }

    public List<UsuarioInfo> getFilteredUsuarios() {
        return filteredUsuarios;
    }

    public void setFilteredUsuarios(List<UsuarioInfo> filteredUsuarios) {
        this.filteredUsuarios = filteredUsuarios;
    }

    public UsuarioInfo getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(UsuarioInfo usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public boolean isBlValidNewCustomer() {
        return blValidNewCustomer;
    }

    public void setBlValidNewCustomer(boolean blValidNewCustomer) {
        this.blValidNewCustomer = blValidNewCustomer;
    }

    public String[] getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String[] tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getIntIdEstado() {
        return intIdEstado;
    }

    public void setIntIdEstado(Integer intIdEstado) {
        this.intIdEstado = intIdEstado;
    }

    public Integer getIntIdTipoCuenta() {
        return intIdTipoCuenta;
    }

    public void setIntIdTipoCuenta(Integer intIdTipoCuenta) {
        this.intIdTipoCuenta = intIdTipoCuenta;
    }

    public Integer getIntIdTipoDocumento() {
        return intIdTipoDocumento;
    }

    public void setIntIdTipoDocumento(Integer intIdTipoDocumento) {
        this.intIdTipoDocumento = intIdTipoDocumento;
    }

    public List<SelectItem> getLstEstados() {
        return lstEstados;
    }

    public void setLstEstados(List<SelectItem> lstEstados) {
        this.lstEstados = lstEstados;
    }

    public List<SelectItem> getLstTipoCuenta() {
        return lstTipoCuenta;
    }

    public void setLstTipoCuenta(List<SelectItem> lstTipoCuenta) {
        this.lstTipoCuenta = lstTipoCuenta;
    }

    public List<SelectItem> getLstTipoDocumento() {
        return lstTipoDocumento;
    }

    public void setLstTipoDocumento(List<SelectItem> lstTipoDocumento) {
        this.lstTipoDocumento = lstTipoDocumento;
    }

    public List<Ubigeo> getLstPaises() {
        return lstPaises;
    }

    public void setLstPaises(List<Ubigeo> lstPaises) {
        this.lstPaises = lstPaises;
    }

    public List<Ubigeo> getLstCiudades() {
        return lstCiudades;
    }

    public void setLstCiudades(List<Ubigeo> lstCiudades) {
        this.lstCiudades = lstCiudades;
    }

    public Integer getUsIdCuentaPk() {
        return usIdCuentaPk;
    }

    public void setUsIdCuentaPk(Integer usIdCuentaPk) {
        this.usIdCuentaPk = usIdCuentaPk;
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

    public Boolean getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    public Integer getPais() {
        return pais;
    }

    public void setPais(Integer pais) {
        this.pais = pais;
    }

    public Integer getCiudad() {
        return ciudad;
    }

    public void setCiudad(Integer ciudad) {
        this.ciudad = ciudad;
    }

    public String getStrContraseniaNueva() {
        return strContraseniaNueva;
    }

    public void setStrContraseniaNueva(String strContraseniaNueva) {
        this.strContraseniaNueva = strContraseniaNueva;
    }

    public String getStrContraseniaReNueva() {
        return strContraseniaReNueva;
    }

    public void setStrContraseniaReNueva(String strContraseniaReNueva) {
        this.strContraseniaReNueva = strContraseniaReNueva;
    }

    public Integer getUsIdCuentaPkCP() {
        return usIdCuentaPkCP;
    }

    public void setUsIdCuentaPkCP(Integer usIdCuentaPkCP) {
        this.usIdCuentaPkCP = usIdCuentaPkCP;
    }

    public String getStrContraseniaNuevaCP() {
        return strContraseniaNuevaCP;
    }

    public void setStrContraseniaNuevaCP(String strContraseniaNuevaCP) {
        this.strContraseniaNuevaCP = strContraseniaNuevaCP;
    }

    public String getStrContraseniaReNuevaCP() {
        return strContraseniaReNuevaCP;
    }

    public void setStrContraseniaReNuevaCP(String strContraseniaReNuevaCP) {
        this.strContraseniaReNuevaCP = strContraseniaReNuevaCP;
    }

    public List<ProyectoInfo> getLstProjectsClient() {
        return lstProjectsClient;
    }

    public void setLstProjectsClient(List<ProyectoInfo> lstProjectsClient) {
        this.lstProjectsClient = lstProjectsClient;
    }

    public ElementoDAO getObjElementoDAO() {
        return objElementoDAO;
    }

    public void setObjElementoDAO(ElementoDAO objElementoDAO) {
        this.objElementoDAO = objElementoDAO;
    }

    public UsuarioDAO getObjUsuarioDAO() {
        return objUsuarioDAO;
    }

    public void setObjUsuarioDAO(UsuarioDAO objUsuarioDAO) {
        this.objUsuarioDAO = objUsuarioDAO;
    }

    @PostConstruct
    public void init() {

        poblarEstados();
        poblarPaises();
        poblarTipoCuentas();
        poblarTipoDocumento();
        obtenerListaUsuarios();

        blValidNewCustomer = false;
        blIsRegistered = false;

        isEdit = false;
        tipoUsuario = new String[3];
        tipoUsuario[0] = msg(Constantes.INT_ET_TIPO_USUARIO_MANAGING_DIRECTOR.toString());
        tipoUsuario[1] = msg(Constantes.INT_ET_TIPO_USUARIO_EVALUATED_EVALUATOR.toString());
        tipoUsuario[2] = msg(Constantes.INT_ET_TIPO_USUARIO_PROJECT_MANAGER.toString());

    }

    private void obtenerListaUsuarios() {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        lstUsuario = new ArrayList<>();

        List lstUsers = usuarioDAO.obtenListaClientes(Utilitarios.obtenerUsuario().getIntUsuarioPk());

        UsuarioInfo objUsuarioInfo;

        Iterator itLstUsers = lstUsers.iterator();

        while (itLstUsers.hasNext()) {

            Object obj[] = (Object[]) itLstUsers.next();

            Usuario objUsuario = (Usuario) obj[0];
            ManageUserRelation objManageUserRelation = objUsuarioDAO.obtenerManageRelationClient(Utilitarios.obtenerUsuario().getIntUsuarioPk(), objUsuario.getUsIdCuentaPk());
            objUsuarioInfo = new UsuarioInfo(objUsuario, objManageUserRelation, false);
            lstUsuario.add(objUsuarioInfo);

        }

    }

    public void usuarioCreado() {
        init();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "El usuario fue creado exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void usuarioActualizado() {
        init();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "El usuario fue actualizado exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void editarUsuario(UsuarioInfo objUsuario) {
        try {

            isEdit = true;

            UbigeoDAO objUbigeoDAO = new UbigeoDAO();

            usIdCuentaPk = objUsuario.getIntUsuarioPk();
            usIdMail = objUsuario.getStrEmail();
            usTxNombreRazonsocial = objUsuario.getStrDescripcion();
            usTxDescripcionEmpresa = objUsuario.getStrEmpresaDesc();
            usIdTipoCuenta = objUsuario.getUsuario().getUsIdTipoCuenta();
            ciudad = objUsuario.getIntIdCiudad();
            pais = objUbigeoDAO.obtenPais(objUsuario.getIntIdCiudad());

            poblarCiudades();

        } catch (HibernateException ex) {
            mostrarError(log, ex);
        }

    }

    public void changePassword(UsuarioInfo objUsuario) {
        try {

            usIdCuentaPkCP = objUsuario.getIntUsuarioPk();
            strContraseniaNuevaCP = null;
            strContraseniaReNuevaCP = null;

        } catch (HibernateException ex) {
            mostrarError(log, ex);
        }

    }

    public void eliminarCliente(UsuarioInfo objUsuario) {
        try {

            isEdit = true;

            objUsuarioDAO.eliminarRelacionCliente(Utilitarios.obtenerUsuario().getIntUsuarioPk(), objUsuario.getIntUsuarioPk());
            objUsuario.getUsuario().setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_ELIMINADO);
            objUsuarioDAO.actualizaUsuario(objUsuario.getUsuario());

            obtenerListaUsuarios();

            mostrarAlertaInfo("deleted.client.success");
        } catch (HibernateException ex) {
            mostrarError(log, ex);
        }

    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (Utilitarios.esNuloOVacio(filterText)) {
            return true;
        }

        UsuarioInfo usuario = (UsuarioInfo) value;
        return usuario.getStrDescripcion().toLowerCase().contains(filterText)
                || usuario.getStrEmail().toLowerCase().contains(filterText)
                || usuario.getStrEmpresaDesc().toLowerCase().contains(filterText)
                || usuario.getStrFechaRegistro().toLowerCase().contains(filterText)
                || usuario.getStrEstadoDesc().toLowerCase().contains(filterText)
                || usuario.getStrTipoUsuario().toLowerCase().contains(filterText);
    }

    public void guardarUsuario(ActionEvent event) {

        try {

            usIdMail = usIdMail.toLowerCase();

            if (!blIsRegistered) {

                boolean blValidAccount = verificarUsuario(false);

                if (blValidAccount) {

                    Usuario objUsuario = new Usuario();
                    EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();
                    objUsuario.setUsTxContrasenia(objEncryptDecrypt.encrypt(Utilitarios.generarClave()));
                    Ubigeo objUbigeo = new Ubigeo();
                    objUbigeo.setUbIdUbigeoPk(ciudad);
                    objUsuario.setUbigeo(objUbigeo);
                    objUsuario.setUsTxDescripcionEmpresa(usTxDescripcionEmpresa);
                    objUsuario.setUsTxNombreRazonsocial(usTxNombreRazonsocial);
                    objUsuario.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_BLOQUEADO);
                    objUsuario.setUsFeRegistro(new Date());
                    objUsuario.setUsIdTipoCuenta(Constantes.INT_ET_TIPO_USUARIO_PROJECT_MANAGER);
                    objUsuario.setUsIdMail(usIdMail);

                    objUsuario.setUsIdCuentaPk(objUsuarioDAO.guardaUsuario(objUsuario));

                    ManageUserRelation objManageUserRelation = new ManageUserRelation();
                    objManageUserRelation.setMaFeRegistro(new Date());
                    objManageUserRelation.setMaIsVerified(Boolean.FALSE);
                    renewToken(objManageUserRelation);
                    objManageUserRelation.setUsIdCuentaManagerPk(Utilitarios.obtenerUsuario().getIntUsuarioPk());
                    objManageUserRelation.setUsuario(objUsuario);

                    objUsuarioDAO.guardaCliente(objManageUserRelation);

                    mostrarAlertaInfo("created.successfully");

                    sendMailForVerification(objManageUserRelation, objUsuario);

                    mostrarAlertaWarning("required.verification");
                    resetFormUsuario();
                    obtenerListaUsuarios();

                }

            } else {

                Usuario objUsuario = objUsuarioDAO.obtenUsuarioByEmail(usIdMail);
                objUsuario.setUsTxNombreRazonsocial(usTxNombreRazonsocial);
                objUsuario.setUsTxDescripcionEmpresa(usTxDescripcionEmpresa);
                objUsuario.setUsIdTipoCuenta(Constantes.INT_ET_TIPO_USUARIO_PROJECT_MANAGER);
                objUsuario.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_BLOQUEADO);

                Ubigeo objUbigeoCiudad = new Ubigeo();
                objUbigeoCiudad.setUbIdUbigeoPk(ciudad);
                objUsuario.setUbigeo(objUbigeoCiudad);

                objUsuarioDAO.actualizaUsuario(objUsuario);

                ManageUserRelation objManageUserRelation = new ManageUserRelation();
                objManageUserRelation.setMaFeRegistro(new Date());
                objManageUserRelation.setMaIsVerified(Boolean.FALSE);
                renewToken(objManageUserRelation);
                objManageUserRelation.setUsIdCuentaManagerPk(Utilitarios.obtenerUsuario().getIntUsuarioPk());
                objManageUserRelation.setUsuario(objUsuario);

                mostrarAlertaInfo("updated");
                mostrarAlertaWarning("required.verification");

                resetFormUsuario();
                obtenerListaUsuarios();
            }

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public void actualizarUsuario(ActionEvent event) {

        try {

            Usuario objUsuario = objUsuarioDAO.obtenUsuarioByEmail(usIdMail);
            
            objUsuario.setUsTxNombreRazonsocial(usTxNombreRazonsocial);
            objUsuario.setUsTxDescripcionEmpresa(usTxDescripcionEmpresa);

            Ubigeo objUbigeoCiudad = new Ubigeo();
            objUbigeoCiudad.setUbIdUbigeoPk(ciudad);
            objUsuario.setUbigeo(objUbigeoCiudad);

            objUsuarioDAO.actualizaUsuario(objUsuario);

            mostrarAlertaInfo("updated");

            obtenerListaUsuarios();

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public boolean verificarUsuario(boolean blAvailableAlerts) {

        boolean isValidAccount = false;

        try {

            if (this.usIdMail != null) {

                this.usIdMail = Utilitarios.limpiarTextoEmail(this.usIdMail);

                if (Utilitarios.obtenerUsuario().getStrEmail().equals(this.usIdMail)) {

                    if (blAvailableAlerts) {
                        mostrarAlertaError("cannot.use.email.for.client");
                    }
                    this.isEdit = false;
                    isValidAccount = false;

                } else {

                    Usuario objUsuario = objUsuarioDAO.obtenUsuarioByEmail(this.usIdMail);

                    if (objUsuario == null) { // NUEVO

                        if (blAvailableAlerts) {
                            mostrarAlertaInfo("can.register.client");
                        }

                        this.isEdit = false;
                        isValidAccount = true;
                        this.blIsRegistered = false;

                    } else if (objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_COUNTRY_MANAGER)
                            || objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_MANAGING_DIRECTOR)) {

                        if (blAvailableAlerts) {
                            mostrarAlertaWarning("cannot.use.email.for.client");
                        }

                        isValidAccount = false;

                    } else { // YA EXISTE Y QUIERO ASIGNARLO COMO CLIENTE

                        List<ManageUserRelation> lstManageUserRelation = objUsuarioDAO.getRelations(objUsuario.getUsIdCuentaPk());

                        if (lstManageUserRelation.isEmpty()) { // NO TIENE COUNTRY MANAGER

                            if (objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_PROJECT_MANAGER)
                                    || objUsuario.getUsIdTipoCuenta().equals(Constantes.INT_ET_TIPO_USUARIO_EVALUATED_EVALUATOR)) {

                                if (blAvailableAlerts) {
                                    mostrarAlertaWarning("can.use.email.for.client");
                                }

                                UsuarioInfo objUsuarioInfo = new UsuarioInfo(objUsuario, null, false);

                                editarUsuario(objUsuarioInfo);

                                isValidAccount = true;

                                this.blIsRegistered = true;

                            } else {
                                
                                //Se requiere un flujo de aprobación para migrar la información existente del cliente al country manager
                                if (blAvailableAlerts) {
                                    mostrarAlertaError("cannot.use.email.for.client");
                                }

                                isValidAccount = false;
                            }

                        } else {

                            if (blAvailableAlerts) {
                                mostrarAlertaError("cannot.use.email.for.client");
                            }

                            isValidAccount = false;
                        }

                    }
                }

            }

            this.blValidNewCustomer = isValidAccount;

        } catch (HibernateException e) {
            mostrarError(log, e);
        }

        return isValidAccount;

    }

    public void renewToken(ManageUserRelation objManageUserRelation) {
        try {
            objManageUserRelation.setMaFeVerificationExpired(Utilitarios.sumarRestarHorasFecha(new Date(), Constantes.INT_MAX_HOURS_WAITING_FOR_VERIFICATION));
            objManageUserRelation.setMaHashLinkVerificacion(Utilitarios.generateToken() + "");
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void resendMailForVerification(UsuarioInfo objUsuario) {
        try {
            ManageUserRelation objManageUserRelation = objUsuarioDAO.verifyClientForVerification(objUsuario.getStrEmail());
            renewToken(objManageUserRelation);
            objUsuarioDAO.actualizaManageUserRelation(objManageUserRelation);
            sendMailForVerification(objManageUserRelation, objUsuario.getUsuario());
        } catch (HibernateException e) {
            mostrarError(log, e);
        }

    }

    private void sendMailForVerification(ManageUserRelation objManageUserRelation, Usuario objUsuario) {

        try {

            Properties props = new Properties();
            props.setProperty("resource.loader", "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.setProperty("input.encoding", CharEncoding.UTF_8);

            VelocityEngine ve = new VelocityEngine();

            ve.init(props);
            //ve.init();

            Template t = ve.getTemplate("/templatesVelocity/TemplateVerifyAccount.vm");

            VelocityContext context = new VelocityContext();

            context.put("NOMBRE", objUsuario.getUsTxNombreRazonsocial());
            context.put("TOKEN", objManageUserRelation.getMaHashLinkVerificacion());
            context.put("TEMPLATEVERIFICACIONTITULO", msg("TEMPLATEVERIFICACIONTITULO"));
            context.put("TEMPLATEVERIFICACIONHELLO", msg("TEMPLATEVERIFICACIONHELLO"));
            context.put("TEMPLATEVERIFICACIONPARRAFO1", msg("TEMPLATEVERIFICACIONPARRAFO1"));
            context.put("TEMPLATEVERIFICACIONPARRAFO2", msg("TEMPLATEVERIFICACIONPARRAFO2"));
            context.put("TEMPLATEVERIFICACIONPARRAFO3", msg("TEMPLATEVERIFICACIONPARRAFO3"));
            context.put("TEMPLATEVERIFICACIONPARRAFO4", msg("TEMPLATEVERIFICACIONPARRAFO4"));
            context.put("TEMPLATEVERIFICACIONPARRAFO5", msg("TEMPLATEVERIFICACIONPARRAFO5"));
            context.put("TEMPLATEVERIFICACIONPARRAFO6", msg("TEMPLATEVERIFICACIONPARRAFO6"));
            context.put("TEMPLATEVERIFICACIONLINK", msg("TEMPLATEVERIFICACIONLINK"));
            context.put("URL", objElementoDAO.obtenElemento(Constantes.INT_ET_URL_AMBIENTE).getElTxValor1());
            context.put("EMAIL", objUsuario.getUsIdMail());
            context.put("COUNTRYMANAGER", Utilitarios.obtenerUsuario().getStrDescripcion());

            StringWriter out = new StringWriter();
            t.merge(context, out);

            NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();

            Notificaciones objNotificaciones = new Notificaciones();
            objNotificaciones.setNoFeCreacion(new Date());
            objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
            objNotificaciones.setNoIdRefProceso(0);
            objNotificaciones.setNoIdTipoProceso(0);
            objNotificaciones.setNoTxAsunto(msg("verify.account.title"));
            objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(out.toString()));
            objNotificaciones.setNoIdNotificacionPk(objNotificacionesDAO.guardaNotificacion(objNotificaciones));

            DestinatariosDAO objDestinatariosDao = new DestinatariosDAO();

            Destinatarios objDestinatarios = new Destinatarios();
            objDestinatarios.setDeTxMail(objUsuario.getUsIdMail());
            objDestinatarios.setNotificaciones(objNotificaciones);

            objDestinatariosDao.guardaDestinatarios(objDestinatarios);

            MailSender objMailSender = new MailSender();
            objMailSender.enviarNotificacion(objNotificaciones);

        } catch (MethodInvocationException | ParseErrorException | ResourceNotFoundException e) {
            mostrarError(log, e);
        }

    }

    public void resetFormUsuario() {

        this.usIdCuentaPk = null;
        this.usIdMail = null;
        this.usTxContrasenia = null;
        this.usIdEstado = null;
        this.usIdTipoCuenta = null;
        this.usTxNombreRazonsocial = "";
        this.usTxDescripcionEmpresa = null;
        this.usIdTipoDocumento = null;
        this.usTxDocumento = null;
        this.pais = null;
        this.ciudad = null;
        this.strContraseniaNueva = null;
        this.strContraseniaReNueva = null;
        this.isEdit = false;
        this.blValidNewCustomer = false;

    }

    public void getListProjectClient(UsuarioInfo objUsuario) {

        try {
            this.lstProjectsClient = new ArrayList<>();
            this.usuarioSeleccionado = objUsuario;

            ProyectoDAO objProyectoDAO = new ProyectoDAO();

            List<Proyecto> lstProyectos = objProyectoDAO.obtenListaProyectosPorUsuario(
                    objUsuario.getIntUsuarioPk(),
                    null,
                    null, null, null,
                    null,
                    null, null, null, null);

            ProyectoInfo objProyectoInfo;

            for (Proyecto objProyecto : lstProyectos) {

                objProyectoInfo = new ProyectoInfo();

                objProyectoInfo = Utilitarios.setearProyecto(objProyecto, objProyectoInfo);

                this.lstProjectsClient.add(objProyectoInfo);
            }

        } catch (HibernateException e) {
            mostrarError(log, e);
        }

    }

    public void resetFormChangePassword() {

        usIdCuentaPkCP = null;
        strContraseniaNuevaCP = null;
        strContraseniaReNuevaCP = null;

    }

    private void poblarPaises() {
        lstCiudades = new ArrayList<>();

        ciudad = null;

        UbigeoDAO objUbigeoDAO = new UbigeoDAO();
        lstPaises = new ArrayList<>();
        lstPaises = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_PAIS, null);
    }

    private void poblarEstados() {

        List<Elemento> lstElementos;
        Iterator itLstElementos;

        lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_ESTADO_USUARIO);

        itLstElementos = lstElementos.iterator();
        lstEstados = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstEstados.add(objSelectItem);
        }
    }

    public void poblarCiudades() {

        lstCiudades = new ArrayList<>();
        UbigeoDAO objUbigeoDAO = new UbigeoDAO();
        lstCiudades = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_DEPARTAMENTO, pais);

    }

    private void poblarTipoCuentas() {

        List<Elemento> lstElementos;
        Iterator itLstElementos;
        lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_TIPO_CUENTA);

        itLstElementos = lstElementos.iterator();
        lstTipoCuenta = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstTipoCuenta.add(objSelectItem);
        }
    }

    private void poblarTipoDocumento() {

        List<Elemento> lstElementos;
        Iterator itLstElementos;
        lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_TIPO_DOCUMENTO);

        itLstElementos = lstElementos.iterator();
        lstTipoDocumento = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstTipoDocumento.add(objSelectItem);
        }
    }

    private String esValido(Usuario usuarioForm) {

        /* SI SE MODIFICA UNA CUENTA EXISTENTE */
        if (Utilitarios.esNuloOVacio(usuarioForm.getUsIdCuentaPk())) {

            Usuario objUsuario = objUsuarioDAO.obtenUsuarioByEmail(usuarioForm.getUsIdMail());

            if (objUsuario != null) {
                return "El mail ingresado ya está siendo utilizado";
            }

        }

        /* SI SE CREA UNA CUENTA NUEVA*/
        return Constantes.strVacio;
    }

    public void actualizaContraseña() {

        try {

            if (!strContraseniaNuevaCP.equals(strContraseniaReNuevaCP)) {
                mostrarAlertaError("must.be.same.password=");
            } else {

                Usuario objUsuario = objUsuarioDAO.obtenUsuario(usIdCuentaPkCP);

                EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                objUsuario.setUsTxContrasenia(objEncryptDecrypt.encrypt(strContraseniaNuevaCP));
                objUsuarioDAO.actualizaUsuario(objUsuario);

                mostrarAlertaInfo("password.changed.success");

                this.usIdCuentaPkCP = null;
                this.strContraseniaNuevaCP = null;
                this.strContraseniaReNuevaCP = null;

            }
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void goToProject(ProyectoInfo objProjectInfo) {

        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            Utilitarios.goToProject(objProjectInfo, usuarioSeleccionado, Utilitarios.obtenerUsuario(), session);

        } catch (Exception ex) {
            mostrarError(log, ex);
        }
    }
}
