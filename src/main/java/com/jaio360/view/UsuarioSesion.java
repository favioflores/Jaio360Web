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
import com.jaio360.orm.HistorialAcceso;
import com.jaio360.orm.Notificaciones;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    public UsuarioInfo getUsuarioInfo() {
        return usuarioInfo;
    }

    public String getTimeClient() {
        return timeClient;
    }

    public Date getTimeServer() {
        return timeServer;
    }

    public void setTimeServer(Date timeServer) {
        this.timeServer = timeServer;
    }

    public void setTimeClient(String timeClient) {
        this.timeClient = timeClient;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
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
        String ipAddress = request.getRemoteAddr();

        try {

            if (usuario.isEmpty() || contraseña.isEmpty()) {
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

                    registraHistorialAcceso(objUsuario.getUsIdCuentaPk(), true, new Date(), null, null);

                    HistorialAccesoDAO objHistorialAccesoDAO = new HistorialAccesoDAO();
                    usuarioInfo.setStrIntentosErrados(objHistorialAccesoDAO.obtenIntentosFallidos(usuarioInfo.getIntUsuarioPk(), usuarioInfo.getIntHistorialPk()).toString());

                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

                    session.setAttribute("usuarioInfo", usuarioInfo);

                    FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.jsf");

                }
            }

        } catch (Exception ex) {
            mostrarError(log, ex);
        }

    }

    public void cerrarSesion() {

        try {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            UsuarioInfo objUsuarioProxy = Utilitarios.obtenerUsuarioProxy();

            if (objUsuarioProxy != null) {
                objUsuarioProxy = (UsuarioInfo) session.getAttribute("usuarioInfoProxy");
                registraHistorialAcceso(objUsuarioProxy.getIntUsuarioPk(), true, null, new Date(), objUsuarioProxy.getIntHistorialPk());
            } else {
                usuarioInfo = (UsuarioInfo) session.getAttribute("usuarioInfo");
                registraHistorialAcceso(usuarioInfo.getIntUsuarioPk(), true, null, new Date(), usuarioInfo.getIntHistorialPk());
            }

            session.invalidate();

            FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsf");

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

    private void registraHistorialAcceso(Integer intUsuarioPk, boolean status, Date dtIngreso, Date dtSalida, Integer intHistorialPk) {

        HistorialAccesoDAO objHistorialAccesoDAO = new HistorialAccesoDAO();
        HistorialAcceso objHistorialAcceso = new HistorialAcceso();

        if (intHistorialPk == null) {

            Usuario objUsuario = new Usuario();
            objUsuario.setUsIdCuentaPk(intUsuarioPk);

            objHistorialAcceso.setHaFeIngreso(dtIngreso);
            objHistorialAcceso.setUsuario(objUsuario);
            objHistorialAcceso.setHaInEstado(status);

            usuarioInfo.setIntHistorialPk(objHistorialAccesoDAO.guardaHistorialAcceso(objHistorialAcceso));

        } else {

            objHistorialAcceso = objHistorialAccesoDAO.obtenHistorialAcceso(intHistorialPk);

            objHistorialAcceso.setHaFeSalida(dtSalida);

            objHistorialAcceso.setHaInEstado(true);

            objHistorialAccesoDAO.actualizaHistorialAcceso(objHistorialAcceso);

        }

    }

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

    }

    public void ingresaSistema() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("admProyectos.jsf");
        } catch (IOException ex) {
            mostrarError(log, ex);
        }
    }

    public void timeout() throws IOException {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioInfo = (UsuarioInfo) session.getAttribute("usuarioInfo");
        session.invalidate();
        registraHistorialAcceso(usuarioInfo.getIntUsuarioPk(), true, null, new Date(), usuarioInfo.getIntHistorialPk());        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

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

}
