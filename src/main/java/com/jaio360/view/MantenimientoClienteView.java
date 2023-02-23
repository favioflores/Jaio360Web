package com.jaio360.view;

import com.jaio360.application.MailSender;
import com.jaio360.dao.DestinatariosDAO;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.UbigeoDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.ManageUserRelation;
import com.jaio360.orm.Notificaciones;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "mantenimientoClienteView")
@ViewScoped
public class MantenimientoClienteView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(MantenimientoClienteView.class);

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
    private boolean isEdit;
    private Integer pais;
    private Integer ciudad;
    private String strContraseniaNueva;
    private String strContraseniaReNueva;

    private ElementoDAO objElementoDAO = new ElementoDAO();
    private UsuarioDAO objUsuarioDAO = new UsuarioDAO();

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

    public List<UsuarioInfo> getLstUsuario() {
        return lstUsuario;
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

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @PostConstruct
    public void init() {

        poblarEstados();
        poblarPaises();
        poblarTipoCuentas();
        poblarTipoDocumento();
        obtenerListaUsuarios();

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

    public void abrirPanel() {

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        //options.put("contentHeight", 500);
        //options.put("style", "width: auto !important");

        PrimeFaces.current().dialog().openDynamic("crearUsuario", options, null);
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
            usTxDescripcionEmpresa = objUsuario.getStrDescripcion();
            usTxNombreRazonsocial = objUsuario.getStrEmpresaDesc();
            usIdTipoCuenta = objUsuario.getUsuario().getUsIdTipoCuenta();
            ciudad = objUsuario.getIntIdCiudad();
            pais = objUbigeoDAO.obtenPais(objUsuario.getIntIdCiudad());

            poblarCiudades();

        } catch (Exception ex) {
            mostrarError(log, ex);
        }

    }

    public void eliminarCliente(UsuarioInfo objUsuario) {
        try {

            isEdit = true;

            UsuarioDAO objUsuarioDAO = new UsuarioDAO();

            objUsuarioDAO.eliminarRelacionCliente(Utilitarios.obtenerUsuario().getIntUsuarioPk(), objUsuario.getIntUsuarioPk());
            objUsuarioDAO.eliminaUsuario(objUsuario.getUsuario());

            mostrarAlertaInfo("deleted.client.success");
        } catch (Exception ex) {
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

            Usuario objUsuario = objUsuarioDAO.obtenUsuarioByEmail(usIdMail);

            if (usIdCuentaPk == null && objUsuario == null) { // NUEVO

                objUsuario = new Usuario();
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

            } else if (usIdCuentaPk == null && objUsuario != null) { // YA EXISTE Y QUIERO CREARLO

                mostrarAlertaError(msg("mail.used.by.other"));

            } else if (usIdCuentaPk != null && objUsuario != null) { // QUIERO ACTUALIZARLO

                objUsuario.setUsTxNombreRazonsocial(usTxDescripcionEmpresa);
                objUsuario.setUsTxDescripcionEmpresa(usTxNombreRazonsocial);
                objUsuario.setUsIdTipoCuenta(usIdTipoCuenta);
                Ubigeo objUbigeoCiudad = new Ubigeo();
                objUbigeoCiudad.setUbIdUbigeoPk(ciudad);
                objUsuario.setUbigeo(objUbigeoCiudad);

                objUsuarioDAO.actualizaUsuario(objUsuario);

                mostrarAlertaInfo("updated");

                resetFormUsuario();
                obtenerListaUsuarios();

            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

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
            sendMailForVerification(objManageUserRelation, objUsuario.getUsuario());
        } catch (Exception e) {
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

    /*
    public void enviarClaveCorreo() {

        try {

            UsuarioDAO objUsuarioDAO = new UsuarioDAO();

            Usuario objUsuario = objUsuarioDAO.iniciaSesion(usuario.trim());

            if (objUsuario != null) {

                if (objUsuario.getUsIdEstado().equals(Constantes.INT_ET_ESTADO_USUARIO_BLOQUEADO)) {
                    mostrarAlertaError("account.blocked");
                } else {

                    EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                    String strNotificacion = Utilitarios.decodeUTF8(objElementoDAO.obtenElemento(Constantes.INT_ET_NOTIFICACION_CLAVE).getElCadena());

                    Utilitarios objUtilitarios = new Utilitarios();

                    strNotificacion = strNotificacion.replace("#%DATO.MENSAJE", objUtilitarios.formatearFecha(new Date(), Constantes.HH24_MI_DDMMYYYY));
                    strNotificacion = strNotificacion.replace("#%USUARIO.MENSAJE", usuario.trim());
                    strNotificacion = strNotificacion.replace("#%CLAVE.MENSAJE", objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()));

                    NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();

                    Notificaciones objNotificaciones = new Notificaciones();
                    objNotificaciones.setNoFeCreacion(new Date());
                    objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                    objNotificaciones.setNoIdRefProceso(0);
                    objNotificaciones.setNoIdTipoProceso(0);
                    objNotificaciones.setNoTxAsunto("Notificación de clave");
                    objNotificaciones.setNoTxMensaje(Utilitarios.encodeUTF8(strNotificacion));

                    objNotificaciones.setNoIdNotificacionPk(objNotificacionesDAO.guardaNotificacion(objNotificaciones));

                    DestinatariosDAO objDestinatariosDao = new DestinatariosDAO();

                    Destinatarios objDestinatarios = new Destinatarios();
                    objDestinatarios.setDeTxMail(usuario.trim());
                    objDestinatarios.setNotificaciones(objNotificaciones);

                    objDestinatariosDao.guardaDestinatarios(objDestinatarios);

                    MailSender objMailSender = new MailSender();
                    objMailSender.enviarNotificacion(objNotificaciones);

                    mostrarAlertaInfo("sended.mail");

                }

            } else {

                mostrarAlertaError("account.not.exist");

            }
            this.usuario = Constantes.strVacio;

        } catch (Exception ex) {
            mostrarError(log, ex);
            mostrarAlertaFatal("error.was.occurred");
        }

    }*/
    public void resetFormUsuario() {

        usIdCuentaPk = null;
        usIdMail = null;
        usTxContrasenia = null;
        usIdEstado = null;
        usIdTipoCuenta = null;
        usTxNombreRazonsocial = "";
        usTxDescripcionEmpresa = null;
        usIdTipoDocumento = null;
        usTxDocumento = null;
        pais = null;
        ciudad = null;
        strContraseniaNueva = null;
        strContraseniaReNueva = null;
        isEdit = false;

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

            if (!strContraseniaNueva.equals(strContraseniaReNueva)) {
                mostrarAlertaError("must.be.same.password=");
            } else {

                Usuario objUsuario = objUsuarioDAO.obtenUsuario(usIdCuentaPk);

                EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                objUsuario.setUsTxContrasenia(objEncryptDecrypt.encrypt(strContraseniaNueva));
                objUsuarioDAO.actualizaUsuario(objUsuario);

                mostrarAlertaInfo("password.changed.success");

                resetFormUsuario();

            }
        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }
    }
}