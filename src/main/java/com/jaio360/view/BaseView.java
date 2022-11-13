package com.jaio360.view;

import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.tools.view.VelocityViewServlet;


public abstract class BaseView extends VelocityViewServlet implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Log logBase = LogFactory.getLog(BaseView.class);

    public static final Severity ERROR = FacesMessage.SEVERITY_ERROR;
    public static final Severity INFO = FacesMessage.SEVERITY_INFO;
    public static final Severity WARN = FacesMessage.SEVERITY_WARN;
    public static final Severity FATAL = FacesMessage.SEVERITY_FATAL;

    public static String msg(String key, Object... params) {

        String result = null;

        try {

            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
            result = bundle.getString(key);

            if (Utilitarios.noEsNuloOVacio(params)) {
                MessageFormat mf = new MessageFormat(bundle.getString(key));
                result = mf.format(params, new StringBuffer(), null).toString();
            }

        } catch (MissingResourceException e) {
            logBase.error(e);
        }
        return result;
    }

    public boolean existeMsg(String key) {
        
        String result = null;
        
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ResourceBundle bundle = context.getApplication().getResourceBundle(context, "msg");
            result = bundle.getString(key);
            
            
        } catch (MissingResourceException e) {
            return false;
        }
        return true;
    }

    public void mostrarAlerta(Severity severity, String key, Log log, Exception e, Object... params) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(severity, msg(key, params), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

                if (Utilitarios.noEsNuloOVacio(e)) {
                    log.error(e);
                }
            } else {
                FacesMessage message = new FacesMessage(severity, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }
    
    public void mostrarAlertaInfo(String key, Object... params) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg(key, params), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }
    
    public void mostrarAlertaInfo(String key) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg(key, null), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }

    public void mostrarAlertaError(String key, Object... params) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg(key, params), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }
    
    public void mostrarAlertaError(String key) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg(key, null), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }
    
    public void mostrarAlertaFatal(String key) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg(key, null), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }

    public void mostrarAlertaFatal(String key, Object... params) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg(key, params), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }

    public void mostrarAlertaWarning(String key, Object... params) {
        try {
            if (existeMsg(key)) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, msg(key, params), null);
                FacesContext.getCurrentInstance().addMessage(null, message);

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, key, null);
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } catch (Exception ex) {
            logBase.error(ex);
            mostrarError(ex);
        }
    }
        
    private void mostrarError(Exception e) {
        try {
            FacesMessage message = new FacesMessage(FATAL, msg("error.was.occurred", null), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logBase.error(ex);
        }
    }
    
    public void mostrarError(Log log, Exception e) {
        try {
            FacesMessage message = new FacesMessage(FATAL, msg("error.was.occurred", null), null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            log.error(e);
        } catch (Exception ex) {
            logBase.error(ex);
        }
    }

}