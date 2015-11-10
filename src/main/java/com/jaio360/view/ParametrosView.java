package com.jaio360.view;

import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.ParametroDAO;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Parametro;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ManagedBean(name = "parametrosView")
@ViewScoped
public class ParametrosView implements Serializable{
    
    private static Log log = LogFactory.getLog(ParametrosView.class);
    
    private boolean blSexo = false;
    private boolean blRangoEdad = false;
    private boolean blTiempoEmpresa = false;
    private boolean blNivelOcupacional = false;
    private boolean blAreaNegocio = false;
        
    private String strRangoEdad;
    private String strRangoTiempo;
    
    private List<String> job;
    private List<String> area;
    private String[] selectedJob;   
    private String[] selectedAreaNegocio;   
    
    private ParametroDAO objParametroDAO = new ParametroDAO();
    private ElementoDAO objElementoDAO = new ElementoDAO();

    public ParametrosView() {
        cargarJobs();
        cargarAreas();
        cargarDatosExistentes();
    }

    public String[] getSelectedAreaNegocio() {
        return selectedAreaNegocio;
    }

    public void setSelectedAreaNegocio(String[] selectedAreaNegocio) {
        this.selectedAreaNegocio = selectedAreaNegocio;
    }

    public boolean isBlRangoEdad() {
        return blRangoEdad;
    }

    public void setBlRangoEdad(boolean blRangoEdad) {
        this.blRangoEdad = blRangoEdad;
    }

    public boolean isBlTiempoEmpresa() {
        return blTiempoEmpresa;
    }

    public void setBlTiempoEmpresa(boolean blTiempoEmpresa) {
        this.blTiempoEmpresa = blTiempoEmpresa;
    }

    public boolean isBlNivelOcupacional() {
        return blNivelOcupacional;
    }

    public void setBlNivelOcupacional(boolean blNivelOcupacional) {
        this.blNivelOcupacional = blNivelOcupacional;
    }

    public boolean isBlAreaNegocio() {
        return blAreaNegocio;
    }

    public void setBlAreaNegocio(boolean blAreaNegocio) {
        this.blAreaNegocio = blAreaNegocio;
    }

    
    public boolean isBlSexo() {
        return blSexo;
    }

    public String getStrRangoEdad() {
        return strRangoEdad;
    }

    public void setStrRangoEdad(String strRangoEdad) {
        this.strRangoEdad = strRangoEdad;
    }

    public String getStrRangoTiempo() {
        return strRangoTiempo;
    }

    public void setStrRangoTiempo(String strRangoTiempo) {
        this.strRangoTiempo = strRangoTiempo;
    }

    public void setBlSexo(boolean blSexo) {
        this.blSexo = blSexo;
    }

    public String[] getSelectedJob() {
        return selectedJob;
    }

    public void setSelectedJob(String[] selectedJob) {
        this.selectedJob = selectedJob;
    }

    public List<String> getJob() {
        return job;
    }

    public void setJob(List<String> job) {
        this.job = job;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
    
    
    public void activarParametro(Integer intIdTipoParametro){
    
        String strPatron;
        
        Parametro objParametro = objParametroDAO.obtenParametro(Utilitarios.obtenerProyecto().getIntIdProyecto(), intIdTipoParametro);
        
        if(objParametro == null){
            
            Proyecto objProyecto = new Proyecto();
            
            objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());
            
            objParametro = new Parametro();
            objParametro.setPaIdTipoParametro(intIdTipoParametro);
            objParametro.setProyecto(objProyecto);
            
            if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)){
                if(blSexo){
                    strPatron = "FEMENINO,MASCULINO";
                    objParametro.setPaTxPatron(strPatron.getBytes());
                    objParametroDAO.guardaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)){
                if(blRangoEdad){
                    strPatron = "20,30,40,50,60,70,80,90";
                    objParametro.setPaTxPatron(strPatron.getBytes());
                    objParametroDAO.guardaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)){
                if(blTiempoEmpresa){
                    strPatron = "1,3,5,10,20,50";
                    objParametro.setPaTxPatron(strPatron.getBytes());
                    objParametroDAO.guardaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)){
                if(blNivelOcupacional){
                    strPatron = job.toString();
                    strPatron = strPatron.replace("[", "");
                    strPatron = strPatron.replace("]", "");
                    strPatron = strPatron.replace(", ", ","); 
                    objParametro.setPaTxPatron(strPatron.getBytes());
                    objParametroDAO.guardaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)){
                if(blAreaNegocio){
                    strPatron = area.toString();
                    strPatron = strPatron.replace("[", "");
                    strPatron = strPatron.replace("]", "");
                    strPatron = strPatron.replace(", ", ","); 
                    objParametro.setPaTxPatron(strPatron.getBytes());
                    objParametroDAO.guardaParametro(objParametro);
                }
            }
            
            cargarDatosExistentes();
            
        }else{
        
            if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)){
                if(!blSexo){
                    objParametroDAO.eliminaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)){
                if(!blRangoEdad){
                    strRangoEdad="";
                    objParametroDAO.eliminaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)){
                if(!blTiempoEmpresa){
                    strRangoTiempo="";
                    objParametroDAO.eliminaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)){
                if(!blNivelOcupacional){
                    selectedJob = null;
                    objParametroDAO.eliminaParametro(objParametro);
                }
            }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)){
                if(!blAreaNegocio){
                    selectedAreaNegocio = null;
                    objParametroDAO.eliminaParametro(objParametro);
                }
            }
        
        }
    
    }

    private void cargarJobs() {
        job = new ArrayList();
        List<Elemento> lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_ROLES);
        
        for(Elemento objElemento : lstElementos){
            job.add(objElemento.getElTxDescripcion());
        }
    }

    private void cargarAreas() {
        area = new ArrayList();
        
        List<Elemento> lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_AREAS);
        
        for(Elemento objElemento : lstElementos){
            area.add(objElemento.getElTxDescripcion());
        }
    }

    private void cargarDatosExistentes() {
        
        List<Parametro> lstParametros = objParametroDAO.obtenListaParametros(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        for(Parametro objParametro : lstParametros){
            if(Utilitarios.noEsNuloOVacio(objParametro.getPaTxPatron())){
                
                if(objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)){
                    blSexo = true;
                }else if(objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)){
                    blRangoEdad = true;
                    strRangoEdad = new String(objParametro.getPaTxPatron());
                }else if(objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)){
                    blTiempoEmpresa = true;
                    strRangoTiempo = new String(objParametro.getPaTxPatron());
                }else if(objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)){
                    blAreaNegocio = true;
                    String temp = new String(objParametro.getPaTxPatron());
                    selectedAreaNegocio = temp.split(",");
                }else if(objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)){
                    blNivelOcupacional = true;
                    String temp = new String(objParametro.getPaTxPatron());
                    selectedJob = temp.split(",");
                }
                
            }
        }
    }

    public void guardarPreferencia(Integer intIdTipoParametro){
        
        String strPatron = "";
        
        Parametro objParametro = objParametroDAO.obtenParametro(Utilitarios.obtenerProyecto().getIntIdProyecto(), intIdTipoParametro);
        
        if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)){
            
            strPatron = strRangoEdad;
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Parametro guardado correctamente",  "Parametro guardado correctamente"));
            
        }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)){
            int i = 0;
            while(i<selectedAreaNegocio.length){ 
                if(i==0){
                    strPatron = selectedAreaNegocio[i];
                }else{
                    strPatron += "," + selectedAreaNegocio[i];
                }
                i++;
            }
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Parametro guardado correctamente",  "Parametro guardado correctamente"));
                    
        }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)){
            int i = 0;
            while(i<selectedJob.length){
                if(i==0){
                    strPatron = selectedJob[i];
                }else{
                    strPatron += "," + selectedJob[i];
                }
                i++;
            }
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Parametro guardado correctamente",  "Parametro guardado correctamente"));
            
        }else if(intIdTipoParametro.equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)){
            
            strPatron = strRangoTiempo;
            objParametro.setPaTxPatron(strPatron.getBytes());
            objParametroDAO.actualizaParametro(objParametro);
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Parametro guardado correctamente",  "Parametro guardado correctamente"));
            
        }
    
        cargarDatosExistentes();
    
    }

    
}
