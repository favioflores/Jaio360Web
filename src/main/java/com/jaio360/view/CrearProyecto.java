/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaio360.view;

/**
 *
 * @author Favio
 */
import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "crearProyecto")
@ViewScoped
public class CrearProyecto extends BaseView implements Serializable {

    private static final Log log = LogFactory.getLog(CrearProyecto.class);

    private static final long serialVersionUID = -1L;

    private String strNombre;
    private String strDescripcion;
    private String strMetodologia;
    private List<SelectItem> lstMetodologias;
    private String diaMin, diaMax;
    private Date díasDisponibles;

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
        poblarMetodologias();
        return lstMetodologias;
    }

    public void setLstMetodologias(List<SelectItem> lstMetodologias) {
        this.lstMetodologias = lstMetodologias;
    }

    public void abrirPanel() {

        Map<String, Object> options = new HashMap();
        options.put("resizable", false);
        options.put("modal", false);
        options.put("draggable", false);
        options.put("closable", true);
        options.put("closeOnEscape", true);
        options.put("responsive", true);
        options.put("modal", true);
        options.put("appendTo", true);
        options.put("dynamic", true);

        PrimeFaces.current().dialog().openDynamic("crearProyecto");

    }

    public void proyectoCreado() {

        mostrarAlertaInfo("adm.project.created", null);

    }

    public void poblarMetodologias() {

        lstMetodologias = new ArrayList();

        List<Elemento> lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_METODOLOGIAS);

        Iterator itLstElementos = lstElementos.iterator();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            if (objElemento.getElIdElementoPk().equals(Constantes.INT_ET_ESTADO_TIPO_PROYECTO_ESCALA)) {
                lstMetodologias.add(objSelectItem);
            }
        }

    }

    public void guardarProyecto(ActionEvent event) {

        try {
            UsuarioSesion objUsuarioSesion = new UsuarioSesion();
            UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();

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
