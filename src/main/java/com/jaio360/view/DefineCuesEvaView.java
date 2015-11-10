package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.EvaluadoCuestionario;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "defineCuesEvaView")
@ViewScoped
public class DefineCuesEvaView implements Serializable{
    
    private static Log log = LogFactory.getLog(DefineCuesEvaView.class);
    
    private static final long serialVersionUID = -1L;
    
    private List<EvaluadoCuestionario> lstEvaluados;
    
    private List<Cuestionario> lstCuestionarios;
    
    private Integer intCantidadTotalEvados;
    private Integer intCantidadEvadosVeri;
    private Integer intCantidadEvadosNVeri;
    private Integer intCantidadCuesConf;
    private Integer intCantidadCuesNConf;
    
    private Integer intItEstadoProyecto;

    public Integer getIntItEstadoProyecto() {
        return intItEstadoProyecto;
    }

    public void setIntItEstadoProyecto(Integer intItEstadoProyecto) {
        this.intItEstadoProyecto = intItEstadoProyecto;
    }

    public Integer getIntCantidadTotalEvados() {
        return intCantidadTotalEvados;
    }

    public void setIntCantidadTotalEvados(Integer intCantidadTotalEvados) {
        this.intCantidadTotalEvados = intCantidadTotalEvados;
    }

    public Integer getIntCantidadEvadosVeri() {
        return intCantidadEvadosVeri;
    }

    public void setIntCantidadEvadosVeri(Integer intCantidadEvadosVeri) {
        this.intCantidadEvadosVeri = intCantidadEvadosVeri;
    }

    public Integer getIntCantidadEvadosNVeri() {
        return intCantidadEvadosNVeri;
    }

    public void setIntCantidadEvadosNVeri(Integer intCantidadEvadosNVeri) {
        this.intCantidadEvadosNVeri = intCantidadEvadosNVeri;
    }

    public Integer getIntCantidadCuesConf() {
        return intCantidadCuesConf;
    }

    public void setIntCantidadCuesConf(Integer intCantidadCuesConf) {
        this.intCantidadCuesConf = intCantidadCuesConf;
    }

    public Integer getIntCantidadCuesNConf() {
        return intCantidadCuesNConf;
    }

    public void setIntCantidadCuesNConf(Integer intCantidadCuesNConf) {
        this.intCantidadCuesNConf = intCantidadCuesNConf;
    }
    
    public List<EvaluadoCuestionario> getLstEvaluados() {
        return lstEvaluados;
    }

    public void setLstEvaluados(List<EvaluadoCuestionario> lstEvaluados) {
        this.lstEvaluados = lstEvaluados;
    }

    public List<Cuestionario> getLstCuestionarios() {
        return lstCuestionarios;
    }

    public void setLstCuestionarios(List<Cuestionario> lstCuestionarios) {
        this.lstCuestionarios = lstCuestionarios;
    }
    
    @PostConstruct
    public void init() {
        
        lstEvaluados = new ArrayList<>();
        lstCuestionarios = new ArrayList<>();
        
        ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();
        
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        
        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        
        Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
        intItEstadoProyecto = objProyecto.getPoIdEstado();
        
        List<Participante> lstEvaluados = objParticipanteDAO.obtenListaParticipanteXProyecto(objProyectoInfo.getIntIdProyecto());
        
        if(!lstEvaluados.isEmpty()){
            
            List<EvaluadoCuestionario> lstTemporal = new ArrayList<>();
            
            EvaluadoCuestionario objEvaluadoCuestionario;
            
            /* Busca selecciones grabadas anterioremente */
            HashMap map = obtenerSeleccionAnterior(objCuestionarioDAO, objProyectoInfo);
            
            for (Participante objParticipante : lstEvaluados) {
                
                if( objParticipante.getPaInRedCargada().equals(Boolean.TRUE)&&
                    objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)&&
                    !objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)){
                    
                    objEvaluadoCuestionario = new EvaluadoCuestionario();

                    objEvaluadoCuestionario.setIntIdEvaluado(objParticipante.getPaIdParticipantePk());
                    objEvaluadoCuestionario.setStrDescNombre(objParticipante.getPaTxDescripcion());
                    objEvaluadoCuestionario.setStrCargo(objParticipante.getPaTxNombreCargo());
                    objEvaluadoCuestionario.setStrCorreo(objParticipante.getPaTxCorreo());
                    objEvaluadoCuestionario.setStrEstadoEvaluado(EHCacheManager.obtenerDescripcionElemento(objParticipante.getPaIdEstado())); 
                    objEvaluadoCuestionario.setIntIdEstadoSel(Constantes.INT_ET_ESTADO_SELECCION_REGISTRADO);
                    objEvaluadoCuestionario.setStrEstadoSel(EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_ESTADO_SELECCION_REGISTRADO));

                    if(!map.isEmpty()){
                        if(map.containsKey(objEvaluadoCuestionario.getIntIdEvaluado())){
                            String[] strTemp = (String[]) map.get(objEvaluadoCuestionario.getIntIdEvaluado());
                            objEvaluadoCuestionario.setIntIdCuestionario(Integer.parseInt(strTemp[0]));
                            objEvaluadoCuestionario.setIntIdEstadoSel(Integer.parseInt(strTemp[1]));
                            objEvaluadoCuestionario.setStrEstadoSel(EHCacheManager.obtenerDescripcionElemento(Integer.parseInt(strTemp[1])));
                        }
                    }

                    lstTemporal.add(objEvaluadoCuestionario);

                }
                
            }
            
            this.lstEvaluados = lstTemporal;
            
        }
        
        lstCuestionarios.add(new Cuestionario(0,"Seleccione un valor"));
        
        List<Cuestionario> lstCuestionarios = objCuestionarioDAO.obtenListaCuestionario(objProyectoInfo.getIntIdProyecto());
        
        for(Cuestionario objCuestionario : lstCuestionarios){
            if(!objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO)){
                this.lstCuestionarios.add(objCuestionario);
            }
        }
        
        calcularContadores();
        
    }
    
    private HashMap obtenerSeleccionAnterior(CuestionarioDAO objCuestionarioDAO, ProyectoInfo objProyectoInfo) {
        
        HashMap map = new HashMap();
        
        List lstSeleccionAnt = objCuestionarioDAO.obtenerSeleccionAnterior(objProyectoInfo.getIntIdProyecto());
        
        if(!lstSeleccionAnt.isEmpty()){
         
            Iterator itLstSeleccionAnt = lstSeleccionAnt.iterator();
            
            while(itLstSeleccionAnt.hasNext()){
            
                Object[] obj = (Object[]) itLstSeleccionAnt.next();
                String[] strTemp = new String[2];
                strTemp[0] = Integer.toString((Integer)obj[1]);
                strTemp[1] = Integer.toString((Integer)obj[2]);
                map.put(obj[0], strTemp);
            
            }
            
        }
        
        return map;
    }
    
    public void guardarRelacionCuestionarios(){
    
        FacesMessage message;
        
        try {

            boolean flagSeleccion = false;
            boolean flagGuardado  = false;

            /*
            for (EvaluadoCuestionario objEvaluadoCuestionario:lstEvaluados){
                
                if(objEvaluadoCuestionario.getIntIdCuestionario() != null && 
                   objEvaluadoCuestionario.getIntIdCuestionario() > 0 && 
                   objEvaluadoCuestionario.getIntIdEstadoSel()!= Constantes.INT_ET_ESTADO_SELECCION_EN_EJECUCION){
                    
                    flagSeleccion = true;
                }
            }

            */ 
            
            //if(flagSeleccion){

                CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

                flagGuardado = objCuestionarioDAO.guardaSeleccion(lstEvaluados, Utilitarios.obtenerProyecto().getIntIdProyecto());

                if(flagGuardado){
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmacion", "Se Guardo exitosamente");
                }else{
                    message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Confirmacion", "Ocurrio un grabe error al guardar");
                }

            //}else{
            //    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar seleccion", "Debe realizar al menos una seleccion");
            //}

        } catch (Exception e) {
            log.error(e);
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar relaciones", "Ocurrio un grabe error al guardar");
        }
        
        FacesContext.getCurrentInstance().addMessage(null, message);
    
    }

    private void calcularContadores() {
        
        intCantidadTotalEvados = 0;
        intCantidadEvadosVeri = 0;
        intCantidadEvadosNVeri = 0;
        intCantidadCuesConf = 0;
        intCantidadCuesNConf = 0;
        
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        
        List<Participante> lstParticipantes = objParticipanteDAO.obtenListaParticipanteXProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        for (Participante objParticipante : lstParticipantes){
            intCantidadTotalEvados++;
            if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION) &&
                objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)){
                intCantidadEvadosVeri++;
            }
            if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION) &&
                objParticipante.getPaInRedVerificada().equals(Boolean.FALSE)){
                intCantidadEvadosNVeri++;
            }
        }
        
        List<Cuestionario> lstCuestionario = objCuestionarioDAO.obtenListaCuestionario(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        for(Cuestionario objCuestionario : lstCuestionario){
            if(objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO) || 
               objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION) ){
                intCantidadCuesConf++;
            }
            if(objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO)){
                intCantidadCuesNConf++;
            }
        }
        
    }
    
}
