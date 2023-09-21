/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.view;

/**
 *
 * @author Favio
 */
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "crearProyecto")
@ViewScoped
public class CrearProyecto extends BaseView implements Serializable {

    private static final Log log = LogFactory.getLog(CrearProyecto.class);

    private String strNombre;
    private String strDescripcion;
    private String strMetodologia;
    private List<SelectItem> lstMetodologias;
    private String diaMin, diaMax;
    private Date díasDisponibles;

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrMetodologia() {
        return strMetodologia;
    }

    public void setStrMetodologia(String strMetodologia) {
        this.strMetodologia = strMetodologia;
    }

    public List<SelectItem> getLstMetodologias() {
        return lstMetodologias;
    }

    public void setLstMetodologias(List<SelectItem> lstMetodologias) {
        this.lstMetodologias = lstMetodologias;
    }

    public String getDiaMin() {
        return diaMin;
    }

    public void setDiaMin(String diaMin) {
        this.diaMin = diaMin;
    }

    public String getDiaMax() {
        return diaMax;
    }

    public void setDiaMax(String diaMax) {
        this.diaMax = diaMax;
    }

    public Date getDíasDisponibles() {
        return díasDisponibles;
    }

    public void setDíasDisponibles(Date díasDisponibles) {
        this.díasDisponibles = díasDisponibles;
    }

    
    public void proyectoCreado() {

        mostrarAlertaInfo("adm.project.created");

    }
    
    public void guardarProyecto(ActionEvent event) {

        try {
            UsuarioSesion objUsuarioSesion = new UsuarioSesion();
            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();

            ProyectoDAO objProyectoDAO = new ProyectoDAO();

            Proyecto objProyecto = new Proyecto();

            objProyecto.setPoTxDescripcion(this.strNombre);
            objProyecto.setPoFeRegistro(new Date());
            objProyecto.setPoIdEstado(Constantes.INT_ET_ESTADO_PROYECTO_REGISTRADO);
            objProyecto.setPoIdMetodologia(30);
            objProyecto.setPoTxMotivo(this.getStrDescripcion());
            objProyecto.setPoInOculto(Boolean.FALSE);

            Usuario objUsuario = new Usuario();
            objUsuario.setUsIdCuentaPk(objUsuarioInfo.getIntUsuarioPk());
            objProyecto.setUsuario(objUsuario);

            objProyectoDAO.guardaProyecto(objProyecto);

            strNombre = Constantes.strVacio;
            strDescripcion = Constantes.strVacio;
            strMetodologia = Constantes.strVacio;
            
            proyectoCreado();
            
        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    @PostConstruct
    public void init() {
        this.diaMin = Utilitarios.formatearFecha(Utilitarios.obtenerFechaHoraSistema(), Constantes.DDMMYYYY);
        this.diaMax = Utilitarios.formatearFecha(Utilitarios.sumarRestarDiasFecha(Utilitarios.obtenerFechaHoraSistema(), 60), Constantes.DDMMYYYY);
    }

}
