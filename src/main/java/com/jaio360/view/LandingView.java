package com.jaio360.view;

import com.jaio360.application.MailSender;
import com.jaio360.dao.DestinatariosDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.Notificaciones;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import static com.jaio360.view.BaseView.msg;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class LandingView extends BaseView implements Serializable {

    private static Logger log = Logger.getLogger(LandingView.class);

    private static final long serialVersionUID = -1L;

    private String strName;
    private String strEmail;
    private String strBusinessName;
    private String strPhone;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getStrBusinessName() {
        return strBusinessName;
    }

    public void setStrBusinessName(String strBusinessName) {
        this.strBusinessName = strBusinessName;
    }

    public String getStrPhone() {
        return strPhone;
    }

    public void setStrPhone(String strPhone) {
        this.strPhone = strPhone;
    }

    @PostConstruct
    public void init() {
        reset();
    }

    private void reset() {
        this.strName = Constantes.strVacio;
        this.strEmail = Constantes.strVacio;
        this.strBusinessName = Constantes.strVacio;
        this.strPhone = Constantes.strVacio;
    }

    public void enviarCorreo() {

        try {

            if (Utilitarios.noEsNuloOVacio(this.strName)
                    && Utilitarios.noEsNuloOVacio(this.strEmail)
                    && Utilitarios.noEsNuloOVacio(this.strBusinessName)
                    && Utilitarios.noEsNuloOVacio(this.strPhone)) {

                String strContent;

                NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();

                Notificaciones objNotificacion = new Notificaciones();
                objNotificacion.setNoFeCreacion(new Date());
                objNotificacion.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_PENDIENTE);
                objNotificacion.setNoIdRefProceso(0);
                objNotificacion.setNoIdTipoProceso(0);
                objNotificacion.setNoTxAsunto(msg("contact.web"));

                strContent = this.strName + Constantes.strPipe
                        + this.strBusinessName + Constantes.strPipe
                        + this.strEmail + Constantes.strPipe
                        + this.strPhone;

                objNotificacion.setNoTxMensaje(Utilitarios.encodeUTF8(strContent));
                objNotificacion.setNoIdNotificacionPk(objNotificacionesDAO.guardaNotificacion(objNotificacion));

                DestinatariosDAO objDestinatariosDao = new DestinatariosDAO();

                Destinatarios objDestinatarios = new Destinatarios();
                objDestinatarios.setDeTxMail("favio@jaio360.com");
                objDestinatarios.setNotificaciones(objNotificacion);

                objDestinatariosDao.guardaDestinatarios(objDestinatarios);

                objDestinatarios.setDeTxMail("jesus@jaio360.com");
                objDestinatarios.setNotificaciones(objNotificacion);

                objDestinatariosDao.guardaDestinatarios(objDestinatarios);

                MailSender objMailSender = new MailSender();
                objMailSender.enviarNotificacion(objNotificacion);

                mostrarAlertaInfo(msg("contact.response.ok"));

                reset();
            } else {
                mostrarAlertaError(msg("contact.empty.value.form"));
            }

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

}
