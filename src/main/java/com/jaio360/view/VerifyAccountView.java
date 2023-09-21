package com.jaio360.view;

import com.jaio360.application.MailSender;
import com.jaio360.dao.DestinatariosDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.ManageUserRelation;
import com.jaio360.orm.Notificaciones;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.hibernate.HibernateException;

@ManagedBean(name = "verifyAccountView")
@ViewScoped

public class VerifyAccountView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(VerifyAccountView.class);

    private static final long serialVersionUID = -1L;

    private String strEmail;
    private String intOne;
    private String intTwo;
    private String intThree;
    private String intFour;
    private String intFive;

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getIntOne() {
        return intOne;
    }

    public void setIntOne(String intOne) {
        this.intOne = intOne;
    }

    public String getIntTwo() {
        return intTwo;
    }

    public void setIntTwo(String intTwo) {
        this.intTwo = intTwo;
    }

    public String getIntThree() {
        return intThree;
    }

    public void setIntThree(String intThree) {
        this.intThree = intThree;
    }

    public String getIntFour() {
        return intFour;
    }

    public void setIntFour(String intFour) {
        this.intFour = intFour;
    }

    public String getIntFive() {
        return intFive;
    }

    public void setIntFive(String intFive) {
        this.intFive = intFive;
    }

    @PostConstruct
    public void init() {
        verifyAccountExists();
    }

    public void verifyAccount() {
        try {

            UsuarioDAO objUsuarioDAO = new UsuarioDAO();

            ManageUserRelation objManageUserRelation = objUsuarioDAO.verifyClientForVerification(this.strEmail);

            if (objManageUserRelation != null) { // Existe
                // Si ya está verificado
                if (objManageUserRelation.getMaIsVerified()) {
                    mostrarAlertaError(msg("verify.alert.verified"));
                } else {
                    // El token no está vigente
                    if (objManageUserRelation.getMaFeVerificationExpired().before(new Date())) {
                        mostrarAlertaError(msg("verify.alert.expired"));
                    } else {
                        // valida el token
                        String strToken = this.intOne + this.intTwo + this.intThree + this.intFour + this.intFive;

                        if (objManageUserRelation.getMaHashLinkVerificacion().equals(strToken)) {

                            objManageUserRelation.setMaFeVerificacion(new Date());
                            objManageUserRelation.setMaIsVerified(Boolean.TRUE);

                            Usuario objUsuario = objUsuarioDAO.obtenUsuarioByEmail(this.strEmail);
                            objUsuario.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_CONFIRMADO);

                            objUsuarioDAO.actualizaCliente(objManageUserRelation, objUsuario);
                            objUsuarioDAO.actualizaUsuario(objUsuario);

                            sendMailWithPassword(objUsuario);

                            //Redirige a la pantalla con mensaje que cuenta no existe
                            FacesContext.getCurrentInstance().getExternalContext().redirect("accountVerifiedSuccess.jsf");

                            //Enviar correo al Country Manager
                        } else {
                            mostrarAlertaError(msg("verify.alert.error.token"));
                        }
                    }

                }

            } else { // No existe
                mostrarAlertaError(msg("verify.alert.not.found"));
            }

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void verifyAccountExists() {

        try {

            Map<String, String> params = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap();

            this.strEmail = params.get("email");

            if (Utilitarios.noEsNuloOVacio(this.strEmail)) {

                UsuarioDAO objUsuarioDAO = new UsuarioDAO();

                ManageUserRelation objManageUserRelation = objUsuarioDAO.verifyClientForVerification(this.strEmail);

                if (objManageUserRelation != null) { // Existe
                    // Si ya está verificado
                    if (objManageUserRelation.getMaIsVerified()) {
                        //Redirige a la pantalla con mensaje de que cuenta está verificada
                        FacesContext.getCurrentInstance().getExternalContext().redirect("accountVerified.jsf");
                    }
                } else { // No existe
                    //Redirige a la pantalla con mensaje que cuenta no existe
                    FacesContext.getCurrentInstance().getExternalContext().redirect("accountNotExist.jsf");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
            }

        } catch (IOException | HibernateException e) {
            mostrarError(log, e);
        }
    }

    public UsuarioInfo obtenerUsuarioInfo() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        return (UsuarioInfo) session.getAttribute("usuarioInfo");

    }

    private void sendMailWithPassword(Usuario objUsuario) {

        try {

            Properties props = new Properties();
            props.setProperty("resource.loader", "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.setProperty("input.encoding", CharEncoding.UTF_8);

            VelocityEngine ve = new VelocityEngine();

            ve.init(props);
            //ve.init();

            Template t = ve.getTemplate("/templatesVelocity/TemplatePasswordRecovery.vm");

            VelocityContext context = new VelocityContext();

            context.put("NOMBRE", objUsuario.getUsTxNombreRazonsocial());

            EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

            context.put("PASSWORD", objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()));
            context.put("TEMPLATEPASSWORDTITULO", msg("send.TEMPLATEPASSWORDTITULO"));
            context.put("TEMPLATEPASSWORDHELLO", msg("TEMPLATEPASSWORDHELLO"));
            context.put("TEMPLATEPASSWORDPARRAFO1", msg("send.TEMPLATEPASSWORDPARRAFO1"));
            context.put("TEMPLATEPASSWORDPARRAFO2", msg("send.TEMPLATEPASSWORDPARRAFO2"));
            context.put("TEMPLATEPASSWORDPARRAFO3", msg("TEMPLATEPASSWORDPARRAFO3"));
            context.put("TEMPLATEPASSWORDPARRAFO4", msg("TEMPLATEPASSWORDPARRAFO4"));
            context.put("TEMPLATEPASSWORDPARRAFO5", msg("TEMPLATEPASSWORDPARRAFO5"));
            context.put("TEMPLATEPASSWORDPARRAFO6", msg("TEMPLATEPASSWORDPARRAFO6"));

            StringWriter out = new StringWriter();
            t.merge(context, out);

            NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();

            Notificaciones objNotificaciones = new Notificaciones();
            objNotificaciones.setNoFeCreacion(new Date());
            objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
            objNotificaciones.setNoIdRefProceso(0);
            objNotificaciones.setNoIdTipoProceso(0);
            objNotificaciones.setNoTxAsunto(msg("send.password.title"));
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
        } catch (Exception ex) {
            mostrarError(log, ex);
        }

    }
}
