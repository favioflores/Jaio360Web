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
import com.sun.media.jfxmedia.logging.Logger;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Favio
 */
public class MailSender extends Thread implements Serializable {

    private List lstCorreos = new ArrayList();

    private Log log = LogFactory.getLog(MailSender.class);

    private NotificacionesDAO objNotificacionesDAO;

    private EHCacheManager cache;

    private Transport transport;

    private Session session;

    String strDominio;
    String strPuerto;
    String strUsuario;
    String strContraseña;

    public MailSender() {
        objNotificacionesDAO = new NotificacionesDAO();
        cache = new EHCacheManager();
    }

    @Override
    public void run() {

        while (true) {

            try {

                if (lstCorreos.isEmpty()) {

                    /* Busca nuevos correos */
                    lstCorreos = objNotificacionesDAO.obtieneNotificaciones(new Date(), 20);

                    if (!lstCorreos.isEmpty()) {

                        realizarEnvios(lstCorreos);

                        lstCorreos.clear();

                    }
                    MailSender.sleep(30000);

                } else {

                    log.debug("Ocurrio un error");

                    this.stop();
                }

            } catch (InterruptedException ex) {
                log.error(ex.getLocalizedMessage(), ex);
            }
        }

    }

    private void realizarEnvios(List lstCorreos) throws InterruptedException {

        Iterator itLstCorreos = lstCorreos.iterator();

        boolean boEnvioCorrecto = true;

        while (itLstCorreos.hasNext()) {

            Object[] obj = (Object[]) itLstCorreos.next();

            Notificaciones objNotificaciones = (Notificaciones) obj[0];

            boEnvioCorrecto = enviaNotificaciones(objNotificaciones, (List<Destinatarios>) obj[1]);

            if (boEnvioCorrecto) {
                objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_ENVIADO);
                objNotificaciones.setNoFeEnvio(new java.sql.Date(new Date().getTime()));
                objNotificacionesDAO.actualizaNotificacion(objNotificaciones);
            } else {
                MailSender.sleep(30000);
            }

        }

    }

    public boolean enviaNotificaciones(Notificaciones objNotificaciones, List<Destinatarios> lstDestinatarios) {

        try {

            ElementoDAO objElementoDAO = new ElementoDAO();

            Elemento objElementoDominio = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_DOMINIO);
            Elemento objElementoPuerto = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_PUERTO_ENVIO);
            Elemento objElementoUsuario = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_USUARIO);
            Elemento objElementoContrasenia = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_CONTRASEÑA);

            strDominio = objElementoDominio.getElTxValor1();
            strPuerto = objElementoPuerto.getElTxValor1();
            strUsuario = objElementoUsuario.getElTxValor1();
            strContraseña = objElementoContrasenia.getElTxValor1();

            Properties props = new Properties();

            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", strPuerto);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, null);

            byte[] bdata = objNotificaciones.getNoTxMensaje();
            String mensaje = new String(Utilitarios.decodeUTF8(bdata));
            String subject = objNotificaciones.getNoTxAsunto();

            MimeMessage message = new MimeMessage(session);

            message.setSubject(subject, "UTF-8");
            
            message.setFrom(new InternetAddress("favio.flores.olaza@gmail.com", "JAIO 360"));

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

        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }

    private void conectarCorreoExterno() {

        try {

            ElementoDAO objElementoDAO = new ElementoDAO();

            Elemento objElementoDominio = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_DOMINIO);
            Elemento objElementoPuerto = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_PUERTO_ENVIO);
            Elemento objElementoUsuario = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_USUARIO);
            Elemento objElementoContrasenia = objElementoDAO.obtenElemento(Constantes.INT_ET_SENDER_CONTRASEÑA);

            strDominio = objElementoDominio.getElTxValor1();
            strPuerto = objElementoPuerto.getElTxValor1();
            strUsuario = objElementoUsuario.getElTxValor1();
            strContraseña = objElementoContrasenia.getElTxValor1();

            Properties props = new Properties();

            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", strPuerto);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props, null);

            Transport transport = session.getTransport("smtp");

            transport.connect(strDominio, Integer.parseInt(strPuerto), strUsuario, strContraseña);

            this.session = session;

            this.transport = transport;

        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

    }

}
