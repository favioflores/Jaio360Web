package com.jaio360.view;

import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.ManageUserRelation;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private void clean() {
        this.strEmail = null;
        this.intOne = null;
        this.intTwo = null;
        this.intThree = null;
        this.intFour = null;
        this.intFive = null;
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
}
