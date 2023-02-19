package com.jaio360.view;

import com.jaio360.application.MailSender;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.primefaces.PrimeFaces;

@ManagedBean(name = "redactarMensajesView")
@ViewScoped
public class RedactarMensajesView extends BaseView implements Serializable {

    private static final long serialVersionUID = -1L;

    private static Log log = LogFactory.getLog(RedactarMensajesView.class);

    private boolean blConvocatoria = false;
    private boolean blBienvenida = false;
    private boolean blAgradecimiento = false;
    private boolean blProyectoTerminado = false;
    private Integer intIdEstadoProyecto;
    private String strPreview;
    private String correoExtra;
    private List<String> lstCorreosExtra;
    private String strPreviewConvocatoriaTemplate;
    private String strPreviewBienvenidaTemplate;
    private String strPreviewAgradecimientoTemplate;

    private String ID_CONVOCATORIA = Constantes.INT_ET_NOTIFICACION_CONVOCATORIA.toString();
    private String ID_INSTRUCCIONES = Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES.toString();
    private String ID_AGRADECIMIENTO = Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO.toString();

    /* CAMPOS DE CONVOCATORIA */
    private String strAsuntoConvocatoria;
    private String strTituloConvocatoria;
    private String strURLLogoCliente;
    private String strParrafoConvocatoria;

    /* BIENVENIDA */
    private String strBienvenidaRecomendaciones;
    private String strBienvenidaConfidencialidad;
    private String strBienvenidaAgradecimiento;

    /* BIENVENIDA */
    private String strAgradecimiento;

    private String strContenido;

    public String getStrPreviewAgradecimientoTemplate() {
        return strPreviewAgradecimientoTemplate;
    }

    public void setStrPreviewAgradecimientoTemplate(String strPreviewAgradecimientoTemplate) {
        this.strPreviewAgradecimientoTemplate = strPreviewAgradecimientoTemplate;
    }

    public String getStrAgradecimiento() {
        return strAgradecimiento;
    }

    public String getStrURLLogoCliente() {
        return strURLLogoCliente;
    }

    public void setStrURLLogoCliente(String strURLLogoCliente) {
        this.strURLLogoCliente = strURLLogoCliente;
    }

    public void setStrAgradecimiento(String strAgradecimiento) {
        this.strAgradecimiento = strAgradecimiento;
    }

    public String getStrPreviewBienvenidaTemplate() {
        return strPreviewBienvenidaTemplate;
    }

    public void setStrPreviewBienvenidaTemplate(String strPreviewBienvenidaTemplate) {
        this.strPreviewBienvenidaTemplate = strPreviewBienvenidaTemplate;
    }

    public String getStrBienvenidaRecomendaciones() {
        return strBienvenidaRecomendaciones;
    }

    public void setStrBienvenidaRecomendaciones(String strBienvenidaRecomendaciones) {
        this.strBienvenidaRecomendaciones = strBienvenidaRecomendaciones;
    }

    public String getStrBienvenidaConfidencialidad() {
        return strBienvenidaConfidencialidad;
    }

    public void setStrBienvenidaConfidencialidad(String strBienvenidaConfidencialidad) {
        this.strBienvenidaConfidencialidad = strBienvenidaConfidencialidad;
    }

    public String getStrBienvenidaAgradecimiento() {
        return strBienvenidaAgradecimiento;
    }

    public void setStrBienvenidaAgradecimiento(String strBienvenidaAgradecimiento) {
        this.strBienvenidaAgradecimiento = strBienvenidaAgradecimiento;
    }

    public String getStrAsuntoConvocatoria() {
        return strAsuntoConvocatoria;
    }

    public void setStrAsuntoConvocatoria(String strAsuntoConvocatoria) {
        this.strAsuntoConvocatoria = strAsuntoConvocatoria;
    }

    public String getStrContenido() {
        return strContenido;
    }

    public void setStrContenido(String strContenido) {
        this.strContenido = strContenido;
    }

    public String getID_CONVOCATORIA() {
        return ID_CONVOCATORIA;
    }

    public void setID_CONVOCATORIA(String ID_CONVOCATORIA) {
        this.ID_CONVOCATORIA = ID_CONVOCATORIA;
    }

    public String getID_INSTRUCCIONES() {
        return ID_INSTRUCCIONES;
    }

    public void setID_INSTRUCCIONES(String ID_INSTRUCCIONES) {
        this.ID_INSTRUCCIONES = ID_INSTRUCCIONES;
    }

    public String getID_AGRADECIMIENTO() {
        return ID_AGRADECIMIENTO;
    }

    public void setID_AGRADECIMIENTO(String ID_AGRADECIMIENTO) {
        this.ID_AGRADECIMIENTO = ID_AGRADECIMIENTO;
    }

    public String getStrTituloConvocatoria() {
        return strTituloConvocatoria;
    }

    public void setStrTituloConvocatoria(String strTituloConvocatoria) {
        this.strTituloConvocatoria = strTituloConvocatoria;
    }

    public String getStrParrafoConvocatoria() {
        return strParrafoConvocatoria;
    }

    public void setStrParrafoConvocatoria(String strParrafoConvocatoria) {
        this.strParrafoConvocatoria = strParrafoConvocatoria;
    }

    public String getStrPreviewConvocatoriaTemplate() {
        return strPreviewConvocatoriaTemplate;
    }

    public void setStrPreviewConvocatoriaTemplate(String strPreviewConvocatoriaTemplate) {
        this.strPreviewConvocatoriaTemplate = strPreviewConvocatoriaTemplate;
    }

    public String getCorreoExtra() {
        return correoExtra;
    }

    public void setCorreoExtra(String correoExtra) {
        this.correoExtra = correoExtra;
    }

    public List<String> getLstCorreosExtra() {
        return lstCorreosExtra;
    }

    public void setLstCorreosExtra(List<String> lstCorreosExtra) {
        this.lstCorreosExtra = lstCorreosExtra;
    }

    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }

    public String getStrPreview() {
        return strPreview;
    }

    public void setStrPreview(String strPreview) {
        this.strPreview = strPreview;
    }

    public boolean isBlProyectoTerminado() {
        return blProyectoTerminado;
    }

    public void setBlProyectoTerminado(boolean blProyectoTerminado) {
        this.blProyectoTerminado = blProyectoTerminado;
    }

    public boolean isBlConvocatoria() {
        return blConvocatoria;
    }

    public void setBlConvocatoria(boolean blConvocatoria) {
        this.blConvocatoria = blConvocatoria;
    }

    public boolean isBlBienvenida() {
        return blBienvenida;
    }

    public void setBlBienvenida(boolean blBienvenida) {
        this.blBienvenida = blBienvenida;
    }

    public boolean isBlAgradecimiento() {
        return blAgradecimiento;
    }

    public void setBlAgradecimiento(boolean blAgradecimiento) {
        this.blAgradecimiento = blAgradecimiento;
    }

    public void notiticacionCreada() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Creación de notificacion", "La notificación se guardo exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void abrirPanelComunicados(Integer intIdTipoNotificacion, Integer recordatorio) {

        Map<String, Object> options = new HashMap();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentWidth", 600);
        //options.put("style", "width: auto !important");

        Map<String, List<String>> params = new HashMap();
        List<String> list1 = new ArrayList();
        list1.add(intIdTipoNotificacion.toString());
        List<String> list2 = new ArrayList();
        list2.add(recordatorio.toString());
        params.put("strTipoNotificacion", list1);
        params.put("strRecordatorio", list2);

        PrimeFaces.current().dialog().openDynamic("crearComunicacion", options, params);
    }

    @PostConstruct
    public void init() {

        lstCorreosExtra = new ArrayList<String>();
        strPreview = Constantes.strVacio;

        Integer intIdProyecto = Utilitarios.obtenerProyecto().getIntIdProyecto();

        MensajeDAO objMensajeDAO = new MensajeDAO();

        List<Mensaje> lstMensajes = objMensajeDAO.obtenListaMensaje(intIdProyecto);

        for (Mensaje objMensaje : lstMensajes) {
            if (objMensaje.getMeIdTipoMensaje().equals(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA)) {
                try {
                    blConvocatoria = true;
                    strPreviewConvocatoriaTemplate = Utilitarios.decodeUTF8(objMensaje.getMeTxCuerpo());
                    strAsuntoConvocatoria = objMensaje.getMeTxAsunto();
                    strTituloConvocatoria = Utilitarios.decodeUTF8(objMensaje.getMeTxConvocatoriaTitulo());
                    if(Utilitarios.noEsNuloOVacio(objMensaje.getMeTxConvocatoriaURL())){
                        strURLLogoCliente = Utilitarios.decodeUTF8(objMensaje.getMeTxConvocatoriaURL());
                    }
                    strParrafoConvocatoria = Utilitarios.decodeUTF8(objMensaje.getMeTxConvocatoriaParrafo());
                } catch (Exception e) {
                    blConvocatoria = false;
                }
            } else if (objMensaje.getMeIdTipoMensaje().equals(Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES)) {
                try {
                    blBienvenida = true;
                    strPreviewBienvenidaTemplate = Utilitarios.decodeUTF8(objMensaje.getMeTxCuerpo());
                    strBienvenidaRecomendaciones = Utilitarios.decodeUTF8(objMensaje.getMeTxBienvenidaRecomendacion());
                    strBienvenidaConfidencialidad = Utilitarios.decodeUTF8(objMensaje.getMeTxBienvenidaConfidencialidad());
                    strBienvenidaAgradecimiento = Utilitarios.decodeUTF8(objMensaje.getMeTxBienvenidaAgradecimiento());
                } catch (Exception e) {
                    blBienvenida = false;
                }
            } else if (objMensaje.getMeIdTipoMensaje().equals(Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO)) {
                try {
                    blAgradecimiento = true;
                    strPreviewAgradecimientoTemplate = Utilitarios.decodeUTF8(objMensaje.getMeTxCuerpo());
                    strAgradecimiento = Utilitarios.decodeUTF8(objMensaje.getMeTxAgradecimiento());
                } catch (Exception e) {
                    blAgradecimiento = false;
                }

            }
        }

        ProyectoDAO objProyectoDAO = new ProyectoDAO();

        Proyecto objProyecto = objProyectoDAO.obtenProyecto(intIdProyecto);

        if (objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_TERMINADO)) {
            blProyectoTerminado = true;
        }

        this.correoExtra = Utilitarios.obtenerUsuario().getStrEmail();

        intIdEstadoProyecto = objProyecto.getPoIdEstado();

        if (!blConvocatoria) {
            setDataConvocatoria();
            armarTemplateConvocatoria(false);
            guardarNotificacion(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA.toString());
        } else {
            armarTemplateConvocatoria(true);
        }

        if (!blBienvenida) {
            setDataBienvenida();
            armarTemplateBienvenida(false);
            guardarNotificacion(Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES.toString());
        } else {
            armarTemplateBienvenida(true);
        }

        if (!blAgradecimiento) {
            setDataAgradecimiento();
            armarTemplateAgradecimiento(false);
            guardarNotificacion(Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO.toString());
        } else {
            armarTemplateAgradecimiento(true);
        }

    }

    private void limpiarTextoConvocatoria() {
        this.strAsuntoConvocatoria = this.strAsuntoConvocatoria.trim();
        this.strTituloConvocatoria = this.strTituloConvocatoria.trim();
        this.strParrafoConvocatoria = this.strParrafoConvocatoria.trim();
        this.strURLLogoCliente = this.strURLLogoCliente.trim();
    }

    private void limpiarTextoBienvenida() {
        this.strBienvenidaRecomendaciones = this.strBienvenidaRecomendaciones.trim();
        this.strBienvenidaConfidencialidad = this.strBienvenidaConfidencialidad.trim();
        this.strBienvenidaAgradecimiento = this.strBienvenidaAgradecimiento.trim();
    }

    public void previewConvocatoria() {
        limpiarTextoConvocatoria();
        armarTemplateConvocatoria(true);
        guardarNotificacion(Constantes.INT_ET_NOTIFICACION_CONVOCATORIA.toString());
    }

    public void previewBienvenida() {
        limpiarTextoBienvenida();
        armarTemplateBienvenida(true);
        guardarNotificacion(Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES.toString());
    }

    public void previewAgradecimiento() {
        this.strAgradecimiento = this.strAgradecimiento.trim();
        armarTemplateAgradecimiento(true);
        guardarNotificacion(Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO.toString());
    }

    private void setDataConvocatoria() {
        this.strAsuntoConvocatoria = msg("step4.announcement.text1");
        this.strTituloConvocatoria = msg("step4.announcement.text2");
        this.strParrafoConvocatoria = msg("step4.announcement.text3");
        this.strURLLogoCliente = msg("step4.announcement.text4");
    }

    private void setDataBienvenida() {
        this.strBienvenidaRecomendaciones = msg("step4.welcome.text1");
        this.strBienvenidaConfidencialidad = msg("step4.welcome.text2");
        this.strBienvenidaAgradecimiento = msg("step4.welcome.text3");
    }

    private void setDataAgradecimiento() {
        this.strAgradecimiento = msg("step4.regards.text1");
    }

    public void armarTemplateConvocatoria(boolean isPreview) {

        try {

            Properties props = new Properties();
            props.setProperty("resource.loader", "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.setProperty("input.encoding", CharEncoding.UTF_8);

            VelocityEngine ve = new VelocityEngine();

            ve.init(props);
            //ve.init();

            Template t = ve.getTemplate("/templatesVelocity/TemplateConvocatoria.vm");

            VelocityContext context = new VelocityContext();

            if (isPreview) {

                context.put("NOMBRE", "Joanquin Inga Solaza");
                //context.put("TIEMPO", Utilitarios.obtieneFechaSistema(Constantes.FORMAT_DATE_FULL));
                context.put("CUENTA", "example@gmail.com");
                context.put("CLAVE", "49SCNO28#");
                context.put("URL", "#");

            }

            //context.put("TEMPLATECONVOCATORIALABELSENDED", msg("template.convocatoria.label.sended"));
            context.put("TEMPLATECONVOCATORIALABELHELLO", msg("template.convocatoria.label.hello"));
            context.put("TEMPLATECONVOCATORIALABELUSER", msg("template.convocatoria.label.user"));
            context.put("TEMPLATECONVOCATORIALABELPASSWORD", msg("template.convocatoria.label.password"));
            context.put("TEMPLATECONVOCATORIALABELSINGIN", msg("template.convocatoria.label.singin"));
            //context.put("TEMPLATECONVOCATORIAFOOTER", msg("template.convocatoria.footer"));

            context.put("TITULO", strTituloConvocatoria);
            context.put("PARRAFO", strParrafoConvocatoria);
            context.put("URLLOGO", strURLLogoCliente);

            StringWriter out = new StringWriter();
            t.merge(context, out);

            strPreviewConvocatoriaTemplate = out.toString();

        } catch (MethodInvocationException | ParseErrorException | ResourceNotFoundException e) {
            mostrarError(log, e);
        }

    }

    public void armarTemplateAgradecimiento(boolean isPreview) {

        try {

            Properties props = new Properties();
            props.setProperty("resource.loader", "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.setProperty("input.encoding", CharEncoding.UTF_8);

            VelocityEngine ve = new VelocityEngine();

            ve.init(props);
            //ve.init();

            Template t = ve.getTemplate("/templatesVelocity/TemplateAgradecimiento.vm");

            VelocityContext context = new VelocityContext();

            if (isPreview) {

                context.put("NOMBRE", "Joanquin Inga Solaza");

            }

            context.put("TEMPLATEREGARDSLABELTHANK", msg("template.regards.label.thank"));
            context.put("PARRAFO_AGRADECIMIENTO", strAgradecimiento);

            StringWriter out = new StringWriter();
            t.merge(context, out);

            strPreviewAgradecimientoTemplate = out.toString();

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public void armarTemplateBienvenida(boolean isPreview) {

        try {

            Properties props = new Properties();
            props.setProperty("resource.loader", "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.setProperty("input.encoding", CharEncoding.UTF_8);

            VelocityEngine ve = new VelocityEngine();

            ve.init(props);
            //ve.init();

            Template t = ve.getTemplate("/templatesVelocity/TemplateBienvenida.vm");

            VelocityContext context = new VelocityContext();

            if (isPreview) {

                context.put("NOMBRE", "Joanquin Inga Solaza");

            }

            context.put("TEMPLATEWELCOMELABELWELCOME", msg("template.welcome.label.welcome"));
            context.put("TEMPLATEWELCOMELABELRECOMENDATIONS", msg("template.welcome.label.recomendations"));
            context.put("TEMPLATEWELCOMELABELCONFIDENTIAL", msg("template.welcome.label.confidential"));

            context.put("PARRAFO_RECOMENDACION", strBienvenidaRecomendaciones);
            context.put("PARRAFO_CONFIDENCIALIDAD", strBienvenidaConfidencialidad);
            context.put("PARRAFO_AGRADECIMIENTO", strBienvenidaAgradecimiento);
            context.put("URL", "#");

            StringWriter out = new StringWriter();
            t.merge(context, out);

            strPreviewBienvenidaTemplate = out.toString();

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public void preview(Integer intTipoNotificacion) {

        MensajeDAO objMensajeDAO = new MensajeDAO();

        List<Mensaje> lstMensajes = objMensajeDAO.obtenListaMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto());

        for (Mensaje objMensaje : lstMensajes) {
            if (objMensaje.getMeIdTipoMensaje().equals(intTipoNotificacion)) {
                byte[] bdata = objMensaje.getMeTxCuerpo();
                strPreview = Utilitarios.decodeUTF8(bdata);
            }
        }

    }

    public void enviarmeMensajeria() {

        try {

            if (Utilitarios.noEsNuloOVacio(this.correoExtra)) {

                NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();

                lstCorreosExtra = new ArrayList<>();

                lstCorreosExtra.add(this.correoExtra);

                if (objNotificacionesDAO.guardarmeComunicados(lstCorreosExtra, strPreviewConvocatoriaTemplate)) {
                    MailSender objMailSender = new MailSender();
                    objMailSender.enviarListaDeNotificacionesProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                    mostrarAlertaInfo("sended.correctly");

                    this.correoExtra = Constantes.strVacio;

                } else {
                    mostrarAlertaFatal("error.was.occurred");
                }

            } else {
                mostrarAlertaError("error.email.invalid");
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    public void agregaCorreoExtra() {
        correoExtra = correoExtra.toLowerCase();
        if (!lstCorreosExtra.contains(correoExtra)) {
            lstCorreosExtra.add(correoExtra);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregar correo", "Correo agregado correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
            correoExtra = "";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Agregar correo", "El correo ingresado ya existe en la lista");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void eliminarCorreoExtra(String correoExtra) {
        lstCorreosExtra.remove(correoExtra);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eliminar correo", "Correo se eliminó correctamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private boolean contenidoValido(String texto, String campo) {

        if (Utilitarios.esNuloOVacio(texto)) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El contenido del campo " + campo + " no puede estar vacío", null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return false;

        }

        return true;
    }

    public void guardarNotificacion(String strIdNotificacion) {

        ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

        try {

            boolean blContenidoOk;
            Mensaje objMensaje = new Mensaje();
            Proyecto objProyecto = new Proyecto();
            MensajeDAO objMensajeDAO = new MensajeDAO();

            //NOTIFICACION CONVOCATORIA
            if (strIdNotificacion.equals(this.ID_CONVOCATORIA)) {

                //blContenidoOk = contenidoValido(strTituloConvocatoria, "título") && contenidoValido(strParrafoConvocatoria, "párrafo");
                if (strPreviewConvocatoriaTemplate.contains(strTituloConvocatoria) && strPreviewConvocatoriaTemplate.contains(strParrafoConvocatoria)) {
                    blContenidoOk = true;
                } else {
                    mostrarAlertaError("step4.preview.first");
                    blContenidoOk = false;
                }

                if (blContenidoOk) {

                    if (blConvocatoria) {
                        objMensaje = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_CONVOCATORIA);
                    }

                    objMensaje.setMeIdTipoMensaje(Integer.parseInt(strIdNotificacion));
                    objMensaje.setMeTxAsunto(strAsuntoConvocatoria);
                    objMensaje.setMeTxConvocatoriaTitulo(Utilitarios.encodeUTF8(strTituloConvocatoria));
                    objMensaje.setMeTxConvocatoriaParrafo(Utilitarios.encodeUTF8(strParrafoConvocatoria));
                    objMensaje.setMeTxConvocatoriaURL(Utilitarios.encodeUTF8(strURLLogoCliente));

                    armarTemplateConvocatoria(false);
                    objMensaje.setMeTxCuerpo(Utilitarios.encodeUTF8(strPreviewConvocatoriaTemplate));

                    objProyecto.setPoIdProyectoPk(objProyectoInfo.getIntIdProyecto());

                    objMensaje.setProyecto(objProyecto);

                    if (blConvocatoria) {
                        objMensajeDAO.actualizaMensaje(objMensaje);
                    } else {
                        objMensajeDAO.guardaMensaje(objMensaje);
                    }

                    armarTemplateConvocatoria(true);
                    if (blConvocatoria) {
                        mostrarAlertaInfo("success");
                    }

                }
            } else if (strIdNotificacion.equals(this.ID_INSTRUCCIONES)) {

                blContenidoOk = contenidoValido(strBienvenidaRecomendaciones, "recomendaciones")
                        && contenidoValido(strBienvenidaConfidencialidad, "confidencialidad")
                        && contenidoValido(strBienvenidaAgradecimiento, "agradecimiento");

                if (strPreviewBienvenidaTemplate.contains(strBienvenidaRecomendaciones)
                        && strPreviewBienvenidaTemplate.contains(strBienvenidaConfidencialidad)
                        && strPreviewBienvenidaTemplate.contains(strBienvenidaAgradecimiento)) {

                    blContenidoOk = true;

                } else {
                    mostrarAlertaError("step4.preview.first");
                    blContenidoOk = false;
                }

                if (blContenidoOk) {

                    if (blBienvenida) {
                        objMensaje = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES);
                    }

                    objMensaje.setMeIdTipoMensaje(Integer.parseInt(strIdNotificacion));

                    objMensaje.setMeTxBienvenidaRecomendacion(Utilitarios.encodeUTF8(strBienvenidaRecomendaciones));
                    objMensaje.setMeTxBienvenidaConfidencialidad(Utilitarios.encodeUTF8(strBienvenidaConfidencialidad));
                    objMensaje.setMeTxBienvenidaAgradecimiento(Utilitarios.encodeUTF8(strBienvenidaAgradecimiento));

                    armarTemplateBienvenida(false);
                    objMensaje.setMeTxCuerpo(Utilitarios.encodeUTF8(strPreviewBienvenidaTemplate));

                    objProyecto.setPoIdProyectoPk(objProyectoInfo.getIntIdProyecto());

                    objMensaje.setProyecto(objProyecto);

                    if (blBienvenida) {
                        objMensajeDAO.actualizaMensaje(objMensaje);
                    } else {
                        objMensajeDAO.guardaMensaje(objMensaje);
                    }

                    armarTemplateBienvenida(true);
                    if (blBienvenida) {
                        mostrarAlertaInfo("success");
                    }
                }
            } else if (strIdNotificacion.equals(this.ID_AGRADECIMIENTO)) {

                blContenidoOk = contenidoValido(strAgradecimiento, "agradecimiento");

                if (strPreviewAgradecimientoTemplate.contains(strAgradecimiento)) {

                    blContenidoOk = true;

                } else {
                    mostrarAlertaError("step4.preview.first");
                    blContenidoOk = false;
                }

                if (blContenidoOk) {

                    if (blAgradecimiento) {
                        objMensaje = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO);
                    }

                    objMensaje.setMeIdTipoMensaje(Integer.parseInt(strIdNotificacion));

                    objMensaje.setMeTxAgradecimiento(Utilitarios.encodeUTF8(strAgradecimiento));

                    armarTemplateAgradecimiento(false);
                    objMensaje.setMeTxCuerpo(Utilitarios.encodeUTF8(strPreviewAgradecimientoTemplate));

                    objProyecto.setPoIdProyectoPk(objProyectoInfo.getIntIdProyecto());

                    objMensaje.setProyecto(objProyecto);

                    if (blAgradecimiento) {
                        objMensajeDAO.actualizaMensaje(objMensaje);
                    } else {
                        objMensajeDAO.guardaMensaje(objMensaje);
                    }

                    armarTemplateAgradecimiento(true);
                    if (blAgradecimiento) {
                        mostrarAlertaInfo("success");
                    }
                }
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");

        }
    }

}
