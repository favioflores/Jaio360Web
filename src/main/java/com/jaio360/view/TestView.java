package com.jaio360.view;

import com.jaio360.dao.ElementoDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "testView")
@ViewScoped
public class TestView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(TestView.class);

    private static final long serialVersionUID = -1L;

    private String texto;
    
    @PostConstruct
    public void init(){
        ElementoDAO objElementoDAO = new ElementoDAO();
        this.texto = objElementoDAO.obtenElemento(3).getElTxDescripcion();
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    

}
