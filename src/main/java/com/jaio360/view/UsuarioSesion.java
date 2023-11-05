package com.jaio360.view;

import com.jaio360.application.MailSender;
import com.jaio360.dao.DestinatariosDAO;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.HistorialAccesoDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.Notificaciones;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import static com.jaio360.utils.Utilitarios.registraHistorialAcceso;
import static com.jaio360.view.BaseView.mostrarError;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpServletRequest;
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

@ManagedBean(name = "usuarioSesion")
@ViewScoped
public class UsuarioSesion extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(UsuarioSesion.class);

    private static final long serialVersionUID = -1L;

    private String usuario;
    private String contraseña;
    private String timeClient;
    private Date timeServer;
    private UsuarioInfo usuarioInfo;
    private ElementoDAO objElementoDAO = new ElementoDAO();

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTimeClient() {
        return timeClient;
    }

    public void setTimeClient(String timeClient) {
        this.timeClient = timeClient;
    }

    public Date getTimeServer() {
        return timeServer;
    }

    public void setTimeServer(Date timeServer) {
        this.timeServer = timeServer;
    }

    public UsuarioInfo getUsuarioInfo() {
        return usuarioInfo;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public ElementoDAO getObjElementoDAO() {
        return objElementoDAO;
    }

    public void setObjElementoDAO(ElementoDAO objElementoDAO) {
        this.objElementoDAO = objElementoDAO;
    }

    public void abandonarSistema(ActionEvent event) {

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Saliendo", "Muchas gracias por su visita");

        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    @PostConstruct
    public void init() {

        timeServer = new Date();

    }

    public void iniciarSesion() throws Exception {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        String strNavegador = request.getHeader("user-agent");
        String ipAddress = request.getRemoteAddr();

        try {

            if (Utilitarios.esNuloOVacio(usuario) || Utilitarios.esNuloOVacio(contraseña.isEmpty())) {
                mostrarAlertaError("login.error");
            } else {
                UsuarioDAO objUsuarioDAO = new UsuarioDAO();

                EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                Usuario objUsuario = objUsuarioDAO.iniciaSesion(usuario.trim());

                if (objUsuario == null) {

                    usuario = Constantes.strVacio;
                    contraseña = Constantes.strVacio;

                    mostrarAlertaError("login.error");

                } else if (!contraseña.trim().equals(objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()))) {

                    usuario = Constantes.strVacio;
                    contraseña = Constantes.strVacio;

                    usuarioInfo = new UsuarioInfo(objUsuario, null, true);

                    mostrarAlertaError("login.error");

                } else {

                    usuarioInfo = new UsuarioInfo(objUsuario, null, true);
                    usuarioInfo.setTimeClient(this.timeClient);

                    Utilitarios.registraHistorialAcceso(usuarioInfo, objUsuario.getUsIdCuentaPk(), true, new Date(), null, null);

                    HistorialAccesoDAO objHistorialAccesoDAO = new HistorialAccesoDAO();
                    usuarioInfo.setStrIntentosErrados(objHistorialAccesoDAO.obtenIntentosFallidos(usuarioInfo.getIntUsuarioPk(), usuarioInfo.getIntHistorialPk()).toString());

                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioInfo", usuarioInfo);

                    session.setAttribute("usuarioInfo", usuarioInfo);

                    System.out.println("La sesión se abrió automaticamente a las " + new Date());

                    FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.jsf");

                }
            }

        } catch (Exception ex) {
            mostrarError(log, ex);
        }

    }

    @PreDestroy
    private void sesionCerrada() {

        System.out.println("La sesión se cerro automaticamente a las " + new Date());

    }

    public void enviarClaveCorreo() {

        try {

            UsuarioDAO objUsuarioDAO = new UsuarioDAO();

            Usuario objUsuario = objUsuarioDAO.iniciaSesion(usuario.trim());

            if (objUsuario != null) {

                if (objUsuario.getUsIdEstado().equals(Constantes.INT_ET_ESTADO_USUARIO_BLOQUEADO)) {
                    mostrarAlertaError("account.blocked");
                } else {

                    sendMailForRecovery(objUsuario);
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

    }

    private void sendMailForRecovery(Usuario objUsuario) {

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
            context.put("TEMPLATEPASSWORDTITULO", msg("TEMPLATEPASSWORDTITULO"));
            context.put("TEMPLATEPASSWORDHELLO", msg("TEMPLATEPASSWORDHELLO"));
            context.put("TEMPLATEPASSWORDPARRAFO1", msg("TEMPLATEPASSWORDPARRAFO1"));
            context.put("TEMPLATEPASSWORDPARRAFO2", msg("TEMPLATEPASSWORDPARRAFO2"));
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
            objNotificaciones.setNoTxAsunto(msg("recovery.password.title"));
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

    public void timeout() throws IOException {

        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            UsuarioInfo objUsuarioProxy = Utilitarios.obtenerUsuarioProxy();

            if (objUsuarioProxy != null) {
                objUsuarioProxy = (UsuarioInfo) session.getAttribute("usuarioInfoProxy");
                registraHistorialAcceso(objUsuarioProxy, objUsuarioProxy.getIntUsuarioPk(), true, null, new Date(), objUsuarioProxy.getIntHistorialPk());
            } else {
                usuarioInfo = (UsuarioInfo) session.getAttribute("usuarioInfo");
                registraHistorialAcceso(usuarioInfo, usuarioInfo.getIntUsuarioPk(), true, null, new Date(), usuarioInfo.getIntHistorialPk());
            }

            session.invalidate();

            FacesContext.getCurrentInstance().getExternalContext().redirect("sesionExpirada.jsf");

        } catch (IOException ex) {
            if (usuarioInfo == null) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");
                } catch (IOException exx) {
                    mostrarError(log, exx);
                }
            } else {
                mostrarError(log, ex);
            }

        }
        
                
    }

    public void test() {
        try {
            this.usuario = "favito.flores@gmail.com";
            this.contraseña = "1234";

            UsuarioDAO objUsuarioDAO = new UsuarioDAO();

            Usuario objUsuario = objUsuarioDAO.iniciaSesion(usuario.trim());
            usuarioInfo = new UsuarioInfo(objUsuario, null, true);
            usuarioInfo.setTimeClient(this.timeClient);

            System.out.println("GMT client: " + this.timeClient);

            Date currentTime = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm:ss a zZ");

            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            System.out.println("GMT server: " + sdf.format(currentTime));

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("proyectoInfo");
            ProyectoInfo objP = new ProyectoInfo();
            objP.setIntIdProyecto(147);
            objP.setStrDescNombre("Prueba");
            objP.setIntIdEstado(37);
            session.setAttribute("proyectoInfo", objP);

            session.setAttribute("usuarioInfo", usuarioInfo);
            FacesContext.getCurrentInstance().getExternalContext().redirect("stepSix.jsf");

        } catch (Exception ex) {
            mostrarError(log, ex);
        }
    }

    public void cerrarSistema() {

        try {
            Utilitarios.cerrarSesion(Utilitarios.obtenerUsuario());
        } catch (Exception ex) {
            mostrarError(log, ex);
        }

    }

}
