/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.application;

import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.orm.Destinatarios;
import com.jaio360.orm.Notificaciones;
import com.jaio360.utils.Constantes;
import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
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
public class MailSender extends Thread implements Serializable{
    
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
    public void run(){
        
        while(true){
            
            try {
                
                if(lstCorreos.isEmpty()){
                    
                    log.debug("Buscando mails");
                    
                    /* Busca nuevos correos */
                    
                    lstCorreos = objNotificacionesDAO.obtieneNotificaciones(new Date(),20);
                    
                    if(!lstCorreos.isEmpty()){
                        
                        log.debug("Notificaciones encontradas " + lstCorreos.size());

                        realizarEnvios(lstCorreos);
                        
                        lstCorreos.clear();

                    }
                    this.sleep(30000);
                    
                }else{

                    log.debug("Ocurrio un error");
                
                    this.stop();
                    
                    /* Enviar Alerta por BD */

                }
                
            } catch (InterruptedException ex) {
                log.error(ex);
            }
        }
        
    }

    private void realizarEnvios(List lstCorreos) throws InterruptedException {
        
        Iterator itLstCorreos = lstCorreos.iterator();
        
        boolean boEnvioCorrecto = true;
        
        while(itLstCorreos.hasNext()){


            if(transport == null || !transport.isConnected()){
                conectarCorreoExterno();
                break;
            }
            
            if(transport.isConnected()){
                
                Object[] obj = (Object[]) itLstCorreos.next();
                
                Notificaciones objNotificaciones = (Notificaciones) obj[0];
                
                boEnvioCorrecto = enviaNotificaciones(objNotificaciones, (List<Destinatarios>) obj[1]);

                if(boEnvioCorrecto){
                    objNotificaciones.setNoIdEstado(Constantes.INT_ET_ESTADO_NOTIFICACION_ENVIADO);
                    objNotificaciones.setNoFeEnvio(new java.sql.Date(new Date().getTime()));
                    objNotificacionesDAO.actualizaNotificacion(objNotificaciones);
                }else{
                    this.sleep(30000);
                }
                
            }
             
        }
        
    }
    
    
    public boolean enviaNotificaciones (Notificaciones objNotificaciones, List <Destinatarios> lstDestinatarios){
        
        try {

            byte[] bdata = objNotificaciones.getNoTxMensaje();
            String mensaje = new String(bdata);            
            
            MimeMessage message = new MimeMessage(session);
             
            message.setSubject(objNotificaciones.getNoTxAsunto(), "UTF-8");

            message.setFrom(new InternetAddress(strUsuario,"Jaio360 Notificaciones"));
            
            Iterator itLstDestinatarios = lstDestinatarios.iterator();
            
            while(itLstDestinatarios.hasNext()){
                Destinatarios objDestinatarios = (Destinatarios) itLstDestinatarios.next();
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(objDestinatarios.getDeTxMail()));
            }
             
            // COVER WRAP
            MimeBodyPart wrap = new MimeBodyPart();

            // ALTERNATIVE TEXT/HTML CONTENT
            MimeMultipart cover = new MimeMultipart("alternative");
            
            MimeBodyPart html = new MimeBodyPart();

            html.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
            html.setHeader("Content-Transfer-Encoding", "quoted-printable");
            html.setContent(mensaje, "text/html; charset=utf-8");
            
            cover.addBodyPart(html);
            
            wrap.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
            wrap.setHeader("Content-Transfer-Encoding", "quoted-printable");
            wrap.setContent( cover, "text/plain; charset=utf-8" ); 

            MimeMultipart content = new MimeMultipart("related");
            message.setContent(content,"UTF-8");
            content.addBodyPart(wrap);
            
            
            if(objNotificaciones.getNoAdjunto() != null){
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(objNotificaciones.getNoAdjunto())));
                adjunto.setFileName(adjunto.getDataHandler().getName());
                cover.addBodyPart(adjunto);
            }
            

            
            // SEND THE MESSAGE
            message.setSentDate(new Date());
            

            this.transport.sendMessage(message, message.getAllRecipients());
            
            return true;
            
        } catch (Exception e){
            log.error(e);
            //return false;
        }
        
        return true;
        
    }
    
    private void conectarCorreoExterno(){
        
        try {
            
            strDominio = cache.obtenerValor1Elemento(Constantes.INT_ET_SENDER_DOMINIO).trim();
            strPuerto = cache.obtenerValor1Elemento(Constantes.INT_ET_SENDER_PUERTO_ENVIO).trim();
            strUsuario = cache.obtenerValor1Elemento(Constantes.INT_ET_SENDER_USUARIO).trim();
            strContraseña = cache.obtenerValor1Elemento(Constantes.INT_ET_SENDER_CONTRASEÑA).trim();
            
            Properties props = new Properties();            
            props.put("mail.smtp.host", "box1035.bluehost.com");
            props.put("mail.smtp.socketFactory.port", strPuerto);
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.starttls.enable", "false");
            props.setProperty("mail.smtp.port", strPuerto);
            props.setProperty("mail.smtp.user", strUsuario);
            props.setProperty("mail.smtp.auth", "true");

            Session se = Session.getDefaultInstance(props, null);
            //se.setDebug(true);
            
            Transport tr = se.getTransport("smtp");
           
            tr.connect(strUsuario,strContraseña);
            
            log.debug("Conectado : " + tr.isConnected());
            
            this.session = se;
            this.transport = tr;

        } catch (Exception e){
            log.error(e);
        }

    }
    
}
