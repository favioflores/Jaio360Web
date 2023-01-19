/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.application;

import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Notificaciones;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

/**
 *
 * @author Favio
 */
public class MailSender extends Thread implements Serializable {

    private Log log = LogFactory.getLog(MailSender.class);

    private NotificacionesDAO objNotificacionesDAO;
    private Integer idProyecto;

    public MailSender() {
        objNotificacionesDAO = new NotificacionesDAO();
    }

    public MailSender(Integer idProyecto) {
        objNotificacionesDAO = new NotificacionesDAO();
        this.idProyecto = idProyecto;
    }

    @Override
    public void run() {

        List lstNotificaciones = objNotificacionesDAO.obtieneNotificaciones(this.idProyecto);

        Iterator itLstNotificaciones = lstNotificaciones.iterator();

        while (itLstNotificaciones.hasNext()) {
            Object[] obj = (Object[]) itLstNotificaciones.next();
            enviaCorreo((Notificaciones) obj[0], (List<Destinatarios>) obj[1]);
        }

    }

    public void enviarListaDeNotificacionesProyecto(Integer idProyecto) {
        MailSender thread = new MailSender(idProyecto);
        thread.start();
    }

    public void enviarNotificacion(Notificaciones objNotificaciones) {
        List lstNotificaciones = objNotificacionesDAO.obtieneNotificaciones(objNotificaciones);

        Iterator itLstNotificaciones = lstNotificaciones.iterator();

        while (itLstNotificaciones.hasNext()) {
            Object[] obj = (Object[]) itLstNotificaciones.next();
            enviaCorreo((Notificaciones) obj[0], (List<Destinatarios>) obj[1]);
        }
    }

    private void enviaCorreo(Notificaciones objNotificaciones, List<Destinatarios> lstDestinatarios) {

        boolean blSended = false;

        try {

            ElementoDAO objElementoDAO = new ElementoDAO();

            Elemento objElementoDominio = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_DOMINIO);
            Elemento objElementoPuerto = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_PUERTO_ENVIO);
            Elemento objElementoUsuario = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_USUARIO);
            Elemento objElementoContrasenia = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_CONTRASEÑA);
            Elemento objElementoFrom = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_FROM);

            String strDominio = objElementoDominio.getElTxValor1();
            String strPuerto = objElementoPuerto.getElTxValor1();
            String strUsuario = objElementoUsuario.getElTxValor1();
            String strContraseña = objElementoContrasenia.getElTxValor1();

            Properties props = new Properties();

            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", strPuerto);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, null);

            byte[] bdata = objNotificaciones.getNoTxMensaje();
            String mensaje = Utilitarios.decodeUTF8(bdata);
            String subject = objNotificaciones.getNoTxAsunto();

            MimeMessage message = new MimeMessage(session);

            message.setSubject(subject, "UTF-8");

            message.setFrom(new InternetAddress(objElementoFrom.getElTxValor1(), objElementoFrom.getElTxDescripcion()));

            for (Destinatarios objDestinatarios : lstDestinatarios) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(objDestinatarios.getDeTxMail()));
            }

            // COVER WRAP
            MimeBodyPart wrap = new MimeBodyPart();

            // ALTERNATIVE TEXT/HTML CONTENT
            MimeMultipart cover = new MimeMultipart("alternative");

            MimeBodyPart html = new MimeBodyPart();

            html.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
            html.setHeader("Content-Transfer-Encoding", "quoted-printable");
            html.setContent(mensaje, "text/html; charset=utf-8");

            cover.addBodyPart(html);

            wrap.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
            wrap.setHeader("Content-Transfer-Encoding", "quoted-printable");
            wrap.setContent(cover, "text/plain; charset=utf-8");

            MimeMultipart content = new MimeMultipart("related");
            message.setContent(content, "UTF-8");
            content.addBodyPart(wrap);

            // SEND THE MESSAGE
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtp");

            transport.connect(strDominio, Integer.parseInt(strPuerto), strUsuario, strContraseña);

            transport.sendMessage(message, message.getAllRecipients());

            transport.close();

            blSended = true;

        } catch (UnsupportedEncodingException | NumberFormatException | MessagingException | HibernateException e) {
            log.error(e.getLocalizedMessage(), e);
            objNotificaciones.setNoTxError(e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage());
            blSended = false;
        } finally {
            if (blSended) {
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_ENVIADO);
            } else {
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_CON_ERROR);
            }
            objNotificaciones.setNoFeEnvio(new java.sql.Date(new Date().getTime()));

            objNotificacionesDAO.actualizaNotificacion(objNotificaciones);
        }

    }

}
