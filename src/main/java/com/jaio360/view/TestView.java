package com.jaio360.view;

import com.jaio360.dao.ElementoDAO;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "TestView")
@ViewScoped


public class TestView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(TestView.class);

    private static final long serialVersionUID = -1L;

    private String texto; 
    
    @PostConstruct
    public void init() {
        ElementoDAO objElementoDAO = new ElementoDAO();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioInfo", "hora");

        texto = "Hola";
        Map<String, Object> sessions = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

        for (Map.Entry<String, Object> entry : sessions.entrySet()) {

            //UsuarioInfo objUsuarioInfo = (UsuarioInfo) entry.getValue().getAttribute("usuarioInfo");
            //if(entry instanceof TestView)
            System.out.println("Key = " + entry.getKey()
                    + ", Value = " + entry.getValue());
        }

    }

    @PreDestroy
    private void sesionCerrada() {

        System.out.println("La sesión se cerro automaticamente a las " + new Date());

    }

}
