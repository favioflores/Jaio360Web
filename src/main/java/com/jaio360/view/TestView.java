package com.jaio360.view;

import com.jaio360.dao.ElementoDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.report.FontsReport;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "testView")
@ViewScoped
public class TestView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(TestView.class);

    private static final long serialVersionUID = -1L;

    private String texto;

    @PostConstruct
    public void init() {
        ElementoDAO objElementoDAO = new ElementoDAO();

        //Map<String, HttpSession> sessions = null;
        //sessions = HttpSessionCollector./);
        
        texto = "";
        Map<String,Object> sessions = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

        for (Map.Entry<String, Object> entry : sessions.entrySet()) {
            
            //UsuarioInfo objUsuarioInfo = (UsuarioInfo) entry.getValue().getAttribute("usuarioInfo");
            System.out.println("Key = " + entry.getKey() +
                             ", Value = " + entry.getValue());
        }
        
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public void generarReporte(){
        FontsReport obj = new FontsReport();
    }

}
