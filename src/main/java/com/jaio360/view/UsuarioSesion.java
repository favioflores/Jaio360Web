package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.DestinatariosDAO;
import com.jaio360.dao.HistorialAccesoDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.HistorialAcceso;
import com.jaio360.orm.Notificaciones;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "usuarioSesion")
@ViewScoped
public class UsuarioSesion implements Serializable{
    
    private static Log log = LogFactory.getLog(UsuarioSesion.class);
            
    private static final long serialVersionUID = -1L;
     
    private String usuario;
     
    private String contraseña;
    
    private UsuarioInfo usuarioInfo;
 
    public String getUsuario() {
        return usuario;
    }

    public UsuarioInfo getUsuarioInfo() {
        return usuarioInfo;
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

    public void iniciarSesion(ActionEvent event) throws Exception {

        FacesMessage message = null;
        
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String captha = params.get("g-recaptcha-response");
        
        boolean blValido = false;
        
        //if(validaConexionGoogle()){
        if(false){
            if(captchaInvalido(captha)){
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de inicio de sesion", "Captcha invalido");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }else{
                blValido = true;
            }
        }else{
            blValido = true;
        }
        
        if(blValido){
         
            try {

            if(usuario.isEmpty() || contraseña.isEmpty()){
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de inicio de sesion", "Usuario y contraseña requeridos");
            }else{
                UsuarioDAO objUsuarioDAO = new UsuarioDAO();

                EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                Usuario objUsuario = objUsuarioDAO.iniciaSesion(usuario.trim());

                if(objUsuario == null) {

                    usuario = Constantes.strVacio;
                    contraseña = Constantes.strVacio;
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de inicio de sesion", "Usuario o contraseña incorrectos");

                }else if(!contraseña.trim().equals(objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()))){

                    usuario = Constantes.strVacio;
                    contraseña = Constantes.strVacio;

                    usuarioInfo = new UsuarioInfo(objUsuario);

                    registraHistorialAcceso(objUsuario.getUsIdCuentaPk(), true, new Date(), null, null);
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error de inicio de sesion", "Usuario o contraseña incorrectos");

                } else {

                    usuarioInfo = new UsuarioInfo(objUsuario);

                    registraHistorialAcceso(objUsuario.getUsIdCuentaPk(), true, new Date(), null, null);

                    HistorialAccesoDAO objHistorialAccesoDAO = new HistorialAccesoDAO();
                    usuarioInfo.setStrIntentosErrados(objHistorialAccesoDAO.obtenIntentosFallidos(usuarioInfo.getIntUsuarioPk(), usuarioInfo.getIntHistorialPk()).toString());

                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                   
                    //session.setMaxInactiveInterval(Constantes.INTERVAL_SESSION);
                    session.setAttribute("usuarioInfo", usuarioInfo);

                    FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");

                }
            }

            } catch (Exception ex) {
                log.error(ex);
            }

            if(message!=null){
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }
    
    public void cerrarSesion(){ 
    
        try {        
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            usuarioInfo = (UsuarioInfo) session.getAttribute("usuarioInfo");
            session.invalidate();
            registraHistorialAcceso(usuarioInfo.getIntUsuarioPk(), true, null, new Date(), usuarioInfo.getIntHistorialPk());
            FacesContext.getCurrentInstance().getExternalContext().redirect("iniciar.jsf");

        } catch (IOException ex) {
            log.error(ex);
        }
        
    }

    private void registraHistorialAcceso(Integer intUsuarioPk, boolean status, Date dtIngreso, Date dtSalida, Integer intHistorialPk) {
        
        HistorialAccesoDAO objHistorialAccesoDAO = new HistorialAccesoDAO();
        HistorialAcceso objHistorialAcceso = new HistorialAcceso();        
        
        if(intHistorialPk == null){
            
            Usuario objUsuario = new Usuario();
            objUsuario.setUsIdCuentaPk(intUsuarioPk);

            objHistorialAcceso.setHaFeIngreso(dtIngreso);
            objHistorialAcceso.setUsuario(objUsuario);
            objHistorialAcceso.setHaInEstado(status);

            usuarioInfo.setIntHistorialPk(objHistorialAccesoDAO.guardaHistorialAcceso(objHistorialAcceso));
            
        }else{
            
            objHistorialAcceso = objHistorialAccesoDAO.obtenHistorialAcceso(intHistorialPk);
            
            objHistorialAcceso.setHaFeSalida(dtSalida);
            
            objHistorialAcceso.setHaInEstado(true);
            
            objHistorialAccesoDAO.actualizaHistorialAcceso(objHistorialAcceso);
            
        }
        
    }
    
    public UsuarioInfo obtenerUsuarioInfo(){
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        
        return (UsuarioInfo) session.getAttribute("usuarioInfo");
        
    }
    
    public void enviarClaveCorreo(){
        
        FacesMessage message;
                
        
        try {
                
            Map<String, String> params = FacesContext.getCurrentInstance()
                    .getExternalContext().getRequestParameterMap();
            String captha = params.get("g-recaptcha-response");

            if(captchaInvalido(captha)){
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Captcha invalido");
            }else{


                UsuarioDAO objUsuarioDAO = new UsuarioDAO();

                Usuario objUsuario = objUsuarioDAO.iniciaSesion(usuario.trim());

                if(objUsuario != null){

                    EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                    String strNotificacion = EHCacheManager.obtenerCadenaElemento(Constantes.INT_ET_NOTIFICACION_CLAVE);

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
                    objNotificaciones.setNoTxAsunto("Notificacion de clave");
                    objNotificaciones.setNoTxMensaje(strNotificacion.getBytes());

                    objNotificaciones.setNoIdNotificacionPk(objNotificacionesDAO.guardaNotificacion(objNotificaciones));

                    DestinatariosDAO objDestinatariosDao = new DestinatariosDAO();

                    Destinatarios objDestinatarios = new Destinatarios();
                    objDestinatarios.setDeTxMail(usuario.trim());
                    objDestinatarios.setNotificaciones(objNotificaciones);

                    objDestinatariosDao.guardaDestinatarios(objDestinatarios);

                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Envio de correo", "La notificación fue enviada");

                }else{

                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Verifique el correo ingresado");

                }
                this.usuario = Constantes.strVacio;

            }
         
            FacesContext.getCurrentInstance().addMessage(null, message);
            
        } catch (Exception ex) {
            log.error(ex);
        }
        
    }
    
    public void abrirPanel() {
        
        Map<String,Object> options = new HashMap();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentWidth", 400);
        //options.put("style", "max-width: 400px;");
            
        RequestContext.getCurrentInstance().openDialog("clave", options, null);
    }
    
    public void ingresaSistema(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("principal.jsf"); 
        } catch (IOException ex) {
            log.error(ex);
        }
        
    }

    private boolean captchaInvalido(String str) throws Exception {

        String url = "https://www.google.com/recaptcha/api/siteverify?secret=6LeGgf4SAAAAAOfMo7YjjuDgNdRwsVG3HE5z2hp8&response="+ str;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        
        String inputLine;

        String rpta = "";
        
        while ((inputLine = in.readLine()) != null) {
                rpta += inputLine;
        }
        
        in.close();
        
        
        
        if(rpta.indexOf("true")>0){
            return false;
        }
        
        return true;

    }


    private boolean validaConexionGoogle() throws Exception {

        try {
            
        
        String url = "https://www.google.com/";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.connect();
        
        
        } catch (Exception e) {
            log.error(e); 
            return false;
        }
        return true;

    }

    public void timeout() throws IOException {
        
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioInfo = (UsuarioInfo) session.getAttribute("usuarioInfo");
        session.invalidate();
        registraHistorialAcceso(usuarioInfo.getIntUsuarioPk(), true, null, new Date(), usuarioInfo.getIntHistorialPk());        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

    }
    
}
