package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RedEvaluacionDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.RelacionEvaluadoEvaluador;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.CuestionarioEvaluado;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.RelacionParticipanteId;
import com.jaio360.report.CuestionarioFisico;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "seguimientoProyectoView")
@ViewScoped
public class SeguimientoProyectoView implements Serializable{
    
    private static Log log = LogFactory.getLog(SeguimientoProyectoView.class);
    
    /*******************/ 
    /* Iniciar proceso */
    /*******************/
    private Integer intCantPartTodos;
    private Integer intCantPartRegistrados;
    private Integer intCantPartEjecucion;
    private Integer intCantPartParam;
    private Integer intCantPartVeri;
    private Integer intCantPartSel;
    private Integer intCantPartCuesNoEje;
    private Integer intCantPartCuesEje;
    
    private Boolean boProyectoEjecutado;
    private Integer intIdEstadoProyecto;
    
    private List<Participante> lstParticipante;
    private List<RedEvaluacion> lstRedEvaluacion;
    private List<Cuestionario> lstCuestionario;
    private List<CuestionarioEvaluado> lstCuestionarioEvaluado;
    private List<Relacion> lstRelacion;
    private List<RelacionParticipante> lstRelacionParticipante;
    
    /***************/
    /* SEGUIMIENTO */
    /***************/
    private Integer intPorcentajeGeneral;
    private List<Evaluado> lstParticipantesIniciados;
    private List<RelacionEvaluadoEvaluador> lstRelacionEvaluadoEvaluador;

    private StreamedContent fileIndividual;
    
    private Boolean flagDescargaFisico = Boolean.FALSE;
    private Boolean flagFiltrarRed = Boolean.FALSE;
    private Boolean flagComunicar = Boolean.TRUE;

    public Boolean getFlagComunicar() {
        return flagComunicar;
    }

    public void setFlagComunicar(Boolean flagComunicar) {
        this.flagComunicar = flagComunicar;
    }

    public Boolean getFlagFiltrarRed() {
        return flagFiltrarRed;
    }

    public void setFlagFiltrarRed(Boolean flagFiltrarRed) {
        this.flagFiltrarRed = flagFiltrarRed;
    }
    
    public Boolean getFlagDescargaFisico() {
        return flagDescargaFisico;
    }

    public void setFlagDescargaFisico(Boolean flagDescargaFisico) {
        this.flagDescargaFisico = flagDescargaFisico;
    }
    
    public StreamedContent getFileIndividual() {
        return fileIndividual;
    }

    public void setFileIndividual(StreamedContent fileIndividual) {
        this.fileIndividual = fileIndividual;
    }
    
    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }

    public List<RelacionEvaluadoEvaluador> getLstRelacionEvaluadoEvaluador() {
        return lstRelacionEvaluadoEvaluador;
    }

    public void setLstRelacionEvaluadoEvaluador(List<RelacionEvaluadoEvaluador> lstRelacionEvaluadoEvaluador) {
        this.lstRelacionEvaluadoEvaluador = lstRelacionEvaluadoEvaluador;
    }

    public Integer getIntPorcentajeGeneral() {
        return intPorcentajeGeneral;
    }

    public void setIntPorcentajeGeneral(Integer intPorcentajeGeneral) {
        this.intPorcentajeGeneral = intPorcentajeGeneral;
    }

    public List<Evaluado> getLstParticipantesIniciados() {
        return lstParticipantesIniciados;
    }

    public void setLstParticipantesIniciados(List<Evaluado> lstParticipantesIniciados) {
        this.lstParticipantesIniciados = lstParticipantesIniciados;
    }
    
    public List<RelacionParticipante> getLstRelacionParticipante() {
        return lstRelacionParticipante;
    }

    public void setLstRelacionParticipante(List<RelacionParticipante> lstRelacionParticipante) {
        this.lstRelacionParticipante = lstRelacionParticipante;
    }

    public Integer getIntCantPartCuesEje() {
        return intCantPartCuesEje;
    }

    public void setIntCantPartCuesEje(Integer intCantPartCuesEje) {
        this.intCantPartCuesEje = intCantPartCuesEje;
    }

    public Integer getIntCantPartCuesNoEje() {
        return intCantPartCuesNoEje;
    }

    public void setIntCantPartCuesNoEje(Integer intCantPartCuesNoEje) {
        this.intCantPartCuesNoEje = intCantPartCuesNoEje;
    }

    public Integer getIntCantPartParam() {
        return intCantPartParam;
    }

    public void setIntCantPartParam(Integer intCantPartParam) {
        this.intCantPartParam = intCantPartParam;
    }

    public Integer getIntCantPartEjecucion() {
        return intCantPartEjecucion;
    }

    public void setIntCantPartEjecucion(Integer intCantPartEjecucion) {
        this.intCantPartEjecucion = intCantPartEjecucion;
    }

    public Integer getIntCantPartTodos() {
        return intCantPartTodos;
    }

    public void setIntCantPartTodos(Integer intCantPartTodos) {
        this.intCantPartTodos = intCantPartTodos;
    }

    public Integer getIntCantPartRegistrados() {
        return intCantPartRegistrados;
    }

    public void setIntCantPartRegistrados(Integer intCantPartRegistrados) {
        this.intCantPartRegistrados = intCantPartRegistrados;
    }

    public Integer getIntCantPartVeri() {
        return intCantPartVeri;
    }

    public void setIntCantPartVeri(Integer intCantPartVeri) {
        this.intCantPartVeri = intCantPartVeri;
    }

    public Integer getIntCantPartSel() {
        return intCantPartSel;
    }

    public void setIntCantPartSel(Integer intCantPartSel) {
        this.intCantPartSel = intCantPartSel;
    }

    public Boolean getBoProyectoEjecutado() {
        return boProyectoEjecutado;
    }

    public void setBoProyectoEjecutado(Boolean boProyectoEjecutado) {
        this.boProyectoEjecutado = boProyectoEjecutado;
    }

    public List<Participante> getLstParticipante() {
        return lstParticipante;
    }

    public void setLstParticipante(List<Participante> lstParticipante) {
        this.lstParticipante = lstParticipante;
    }

    public List<RedEvaluacion> getLstRedEvaluacion() {
        return lstRedEvaluacion;
    }

    public void setLstRedEvaluacion(List<RedEvaluacion> lstRedEvaluacion) {
        this.lstRedEvaluacion = lstRedEvaluacion;
    }

    public List<Cuestionario> getLstCuestionario() {
        return lstCuestionario;
    }

    public void setLstCuestionario(List<Cuestionario> lstCuestionario) {
        this.lstCuestionario = lstCuestionario;
    }

    public List<CuestionarioEvaluado> getLstCuestionarioEvaluado() {
        return lstCuestionarioEvaluado;
    }

    public void setLstCuestionarioEvaluado(List<CuestionarioEvaluado> lstCuestionarioEvaluado) {
        this.lstCuestionarioEvaluado = lstCuestionarioEvaluado;
    }

    public List<Relacion> getLstRelacion() {
        return lstRelacion;
    }

    public void setLstRelacion(List<Relacion> lstRelacion) {
        this.lstRelacion = lstRelacion;
    }
    
    @PostConstruct
    public void init() {

        intCantPartTodos = 0;
        intCantPartVeri = 0;
        intCantPartSel = 0;
        intCantPartRegistrados = 0;
        intCantPartEjecucion = 0;
        intCantPartParam = 0;
        intCantPartCuesNoEje = 0;
        intCantPartCuesEje = 0;
        
        Integer idProyecto = Utilitarios.obtenerProyecto().getIntIdProyecto();
        
        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        intIdEstadoProyecto = objProyecto.getPoIdEstado();
                
        if(objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION) || 
           objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_TERMINADO)){
            boProyectoEjecutado = Boolean.TRUE; 
        }else{
            boProyectoEjecutado = Boolean.FALSE;
        }
        
        
        /* DATOS DE PARTICIPANTES */
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        lstParticipante = objParticipanteDAO.obtenListaParticipanteXProyecto(idProyecto);
        
        /* DATOS DE EVALUADORES */
        RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
        lstRedEvaluacion = objRedEvaluacionDAO.obtenerListaEvaluadores(idProyecto);
        
        /* DATOS DE CUESTIONARIOS */
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
        lstCuestionario = objCuestionarioDAO.obtenListaCuestionario(idProyecto);
        lstCuestionarioEvaluado = objCuestionarioDAO.obtenListaRelacionCuestionarioEvaluado(idProyecto);
        
        /* DATOS DE RELACIONES CON EVALUADOS */
        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
        lstRelacionParticipante = objRelacionParticipanteDAO.obtenListaRelParticipanteXProyecto(idProyecto);
                
        /* DATOS DE RELACIONES */
        RelacionDAO objRelacionDAO = new RelacionDAO();
        lstRelacion = objRelacionDAO.obtenListaRelacionPorProyecto(idProyecto);
        
        if(!lstParticipante.isEmpty()){
            cargarDatosDeParticipantes(lstParticipante,lstCuestionarioEvaluado);
        }
                
        if(!lstCuestionarioEvaluado.isEmpty()){
            cargarDatosDeCuestinarioEvaluado(lstCuestionarioEvaluado);
        }
        
        
        /* SEGUIMIENTO */
        
        lstParticipantesIniciados = new ArrayList();
        
        Evaluado objEvaluado;
        
        for(Participante objParticipante : lstParticipante){
            if( objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION) ||
                objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO)){
                
                objEvaluado = new Evaluado();
                
                objEvaluado.setPaIdParticipantePk(objParticipante.getPaIdParticipantePk());
                objEvaluado.setPaIdEstado(objParticipante.getPaIdEstado());
                objEvaluado.setPaStrEstado(EHCacheManager.obtenerDescripcionElemento(objParticipante.getPaIdEstado()));
                objEvaluado.setPaIdTipoParticipante(objParticipante.getPaIdTipoParticipante());
                objEvaluado.setPaInAutoevaluar(objParticipante.getPaInAutoevaluar());
                objEvaluado.setPaInRedCargada(objParticipante.getPaInRedCargada());
                objEvaluado.setPaInRedVerificada(objParticipante.getPaInRedVerificada());
                objEvaluado.setPaTxCorreo(objParticipante.getPaTxCorreo());
                objEvaluado.setPaTxDescripcion(objParticipante.getPaTxDescripcion());
                objEvaluado.setPaTxNombreCargo(objParticipante.getPaTxNombreCargo());
                objEvaluado.setBdPorcentajeAvance(BigDecimal.ZERO);
                objEvaluado.setBoCheckFilterSeg(Boolean.FALSE);
                
                for(CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado){
                    if(objCuestionarioEvaluado.getId().getPaIdParticipanteFk().equals(objEvaluado.getPaIdParticipantePk())){
                        for(Cuestionario objCuestionario : lstCuestionario){
                            if(objCuestionario.getCuIdCuestionarioPk().equals(objCuestionarioEvaluado.getId().getCuIdCuestionarioFk())){
                                objEvaluado.setStrDescCuestionario(objCuestionario.getCuTxDescripcion());
                            }
                        }
                    }
                }
                
                lstParticipantesIniciados.add(objEvaluado);
            }
        }
        
        Map map = buscaEvaluacionesTerminadas();
        cargarEvaluaciones(map);
        
        if(!lstParticipantesIniciados.isEmpty()){
            calcularPorcentajes(map);
        }
        
    }
    
    private void calcularPorcentajes(Map map){
    
    /* Coloca porcentajes a evaluados */
        for (Evaluado objEvaluado : lstParticipantesIniciados){
        
            int i = 0;
            int c = 0;
                        
            Iterator it = map.entrySet().iterator();
        
            while (it.hasNext()) {
              Map.Entry entry = (Map.Entry) it.next();

              if(entry.getValue().equals(objEvaluado.getPaIdParticipantePk())){
                  i++;
              }

            }
            
            for(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador){
            
                if(objRelacionEvaluadoEvaluador.getIntIdEvaluado().equals(objEvaluado.getPaIdParticipantePk())){
                    c++;
                }
                
            }
            
            objEvaluado.setBdPorcentajeAvance(new BigDecimal(Math.floor(i*100/c)));
        
        }
    
    }
    
    private void cargarEvaluaciones(Map map){
        
        lstRelacionEvaluadoEvaluador = new ArrayList<>();
        
        for(RelacionParticipante objRelacionParticipante : lstRelacionParticipante){
        
            boolean apto = false;
            String strDescEvaluador = null;
            
            for(Participante objParticipante : lstParticipante){
                if(objParticipante.getPaIdParticipantePk().equals(objRelacionParticipante.getId().getPaIdParticipanteFk())){
                    if(!(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO) ||
                         objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION))){
                        
                        strDescEvaluador = objParticipante.getPaTxDescripcion();
                        apto = true;
                    }
                }
            }
            
            if(apto){
                
                RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador = new RelacionEvaluadoEvaluador();

                objRelacionEvaluadoEvaluador.setStrDescEvaluado(strDescEvaluador);
                
                objRelacionEvaluadoEvaluador.setIntIdEvaluado(objRelacionParticipante.getId().getPaIdParticipanteFk());
                objRelacionEvaluadoEvaluador.setIntIdEvaluador(objRelacionParticipante.getId().getReIdParticipanteFk());
                objRelacionEvaluadoEvaluador.setIntIdRelacion(objRelacionParticipante.getId().getReIdRelacionFk());

                String strKey = objRelacionEvaluadoEvaluador.getIntIdEvaluado()+ Constantes.strPipe + 
                        objRelacionEvaluadoEvaluador.getIntIdEvaluador() + Constantes.strPipe +
                        objRelacionEvaluadoEvaluador.getIntIdRelacion();

                objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.TRUE);
                
                if(map.containsKey(strKey)){
                    objRelacionEvaluadoEvaluador.setBlEvaluacionTerminada(Boolean.TRUE);
                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.FALSE);
                }

                for(Relacion objRelacion : lstRelacion){
                    if(objRelacion.getReIdRelacionPk().equals(objRelacionEvaluadoEvaluador.getIntIdRelacion())){
                        objRelacionEvaluadoEvaluador.setStrDescRelacion(objRelacion.getReTxNombre());
                    }
                }

                for(RedEvaluacion objRedEvaluacion : lstRedEvaluacion){
                    if(objRedEvaluacion.getReIdParticipantePk().equals(objRelacionEvaluadoEvaluador.getIntIdEvaluador())){
                        objRelacionEvaluadoEvaluador.setStrDescEvaluador(objRedEvaluacion.getReTxDescripcion());
                        objRelacionEvaluadoEvaluador.setStrCorreoEvaluador(objRedEvaluacion.getReTxCorreo());
                    }
                }
                
                lstRelacionEvaluadoEvaluador.add(objRelacionEvaluadoEvaluador);
 
                
            }
        }
        
        /* Carga autoevaluaciones */
        
        for(Participante objParticipante : lstParticipante){
        
            if(objParticipante.getPaInRedCargada().equals(true) && objParticipante.getPaInRedVerificada().equals(true) &&
               (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION) || 
                objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO))){
                
                if(objParticipante.getPaInAutoevaluar().equals(true)){
                
                    RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador = new RelacionEvaluadoEvaluador();

                    objRelacionEvaluadoEvaluador.setIntIdEvaluado(objParticipante.getPaIdParticipantePk());
                    objRelacionEvaluadoEvaluador.setIntIdEvaluador(null);
                    objRelacionEvaluadoEvaluador.setIntIdRelacion(null);

                    String strKey = objRelacionEvaluadoEvaluador.getIntIdEvaluado()+ Constantes.strPipe + 
                            objRelacionEvaluadoEvaluador.getIntIdEvaluador() + Constantes.strPipe +
                            objRelacionEvaluadoEvaluador.getIntIdRelacion();

                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.TRUE);
                    
                    if(map.containsKey(strKey)){
                        objRelacionEvaluadoEvaluador.setBlEvaluacionTerminada(Boolean.TRUE);
                        objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.FALSE);
                    }

                    objRelacionEvaluadoEvaluador.setStrDescRelacion("Uno mismo");

                    objRelacionEvaluadoEvaluador.setStrDescEvaluador(objParticipante.getPaTxDescripcion());
                    objRelacionEvaluadoEvaluador.setStrDescEvaluado(objParticipante.getPaTxDescripcion());
                    objRelacionEvaluadoEvaluador.setStrCorreoEvaluador(objParticipante.getPaTxCorreo());
                    
                    lstRelacionEvaluadoEvaluador.add(objRelacionEvaluadoEvaluador);
                    
                    
                }
            
            }
            
        }
        
    }
    
    private void cargarDatosDeCuestinarioEvaluado(List<CuestionarioEvaluado> lstCuestionarioEvaluado) {
        
        for (CuestionarioEvaluado objCuestionarioEvaluado : lstCuestionarioEvaluado){
            
            if(objCuestionarioEvaluado.getCeIdEstado().equals(Constantes.INT_ET_ESTADO_CUES_EVA_REGISTRADO)){
                intCantPartCuesNoEje++;
            }
            if(objCuestionarioEvaluado.getCeIdEstado().equals(Constantes.INT_ET_ESTADO_CUES_EVA_EN_EJECUCION)){
                intCantPartCuesEje++;
            }
        }
        
    }
    
    private void cargarDatosDeParticipantes(List<Participante> lstParticipantes, List<CuestionarioEvaluado> lstCuestionarioEvaluado) {
        
        for (Participante objParticipante : lstParticipantes){

            intCantPartTodos++;
            
            if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)){
                intCantPartRegistrados++;
            }
            
            if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION)){
                intCantPartEjecucion++;
            }

            if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)){
                intCantPartParam++;
            }
                        
            if(objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)){
                intCantPartVeri++;
            }
            
            if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION) &&
                objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)){
                
                for(CuestionarioEvaluado objCuestionarioEvaluado: lstCuestionarioEvaluado){
                    if(objCuestionarioEvaluado.getId().getPaIdParticipanteFk().equals(objParticipante.getPaIdParticipantePk())){
                        intCantPartSel++;
                    }
                }
            }

        }
        
    }
    
    public boolean verificaNotificaciones(){
        
        MensajeDAO objMensajeDAO = new MensajeDAO();
        Mensaje objMensajeA = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_CONVOCATORIA);
        Mensaje objMensajeB = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES);
        Mensaje objMensajeC = objMensajeDAO.obtenMensaje(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO);
        
        if(objMensajeA==null || objMensajeB==null || objMensajeC==null){
            return false;
        }
        
        return true;
    
    }
    
    public void iniciarProceso(){
        
        if(verificaNotificaciones()){
    
            init();

            ProyectoDAO objProyectoDAO = new ProyectoDAO();

            List lstNotificaciones = objProyectoDAO.iniciarProyecto(lstParticipante,lstRedEvaluacion,lstCuestionario,lstCuestionarioEvaluado,lstRelacion,lstRelacionParticipante);

            if(!lstNotificaciones.isEmpty()){
                Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

                if(!objProyecto.getPoIdEstado().equals(Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION)){
                    objProyecto.setPoFeEjecucion(new Date());
                    objProyecto.setPoIdEstado(Constantes.INT_ET_ESTADO_PROYECTO_EN_EJECUCION);
                    objProyectoDAO.actualizaProyecto(objProyecto);

                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Iniciar proyecto", "El proyecto fue creado exitosamente");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }else{
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Iniciar proyecto", "Ocurrio un error al iniciar el proyecto");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

            init();
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Iniciar proyecto", "No se han redactado todos los comunicados para iniciar el proyecto. Por favor volver al paso anterior");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
    }
    
    public void actListaEvaluadores(){
        
        Map map = buscaEvaluacionesTerminadas();
        
        cargarEvaluaciones(map);
                
        List<RelacionEvaluadoEvaluador> lstTemp = new ArrayList<>();
        
        for(RelacionEvaluadoEvaluador obj : lstRelacionEvaluadoEvaluador){
        
            for(Evaluado objEvaluado : lstParticipantesIniciados){
            
                if(obj.getIntIdEvaluado().equals(objEvaluado.getPaIdParticipantePk()) && objEvaluado.getBoCheckFilterSeg()){
                    
                    lstTemp.add(obj);
                    
                }
                
            }
        
        }
        
        lstRelacionEvaluadoEvaluador = lstTemp;
        
    }

    private Map buscaEvaluacionesTerminadas() {
        
        Map map = new HashMap();
        
        ResultadoDAO objResultadoDAO = new ResultadoDAO();
        
        List lstTerminados = objResultadoDAO.obtenListaResultadoGeneral(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        if(!lstTerminados.isEmpty()){

            Iterator itLstTerminados = lstTerminados.iterator();

            while(itLstTerminados.hasNext()){

                Object[] obj = (Object[]) itLstTerminados.next();

                String strKey = obj[0] + Constantes.strPipe + obj[1] + Constantes.strPipe + obj[2];

                map.put(strKey, obj[0]);
                
            }
        }
        
        return map;
        
    }

    
    public void descargarFisico(){
        
        boolean flag = false;
        
        List<String> lstArchivos = new ArrayList<>();
        
        for(Evaluado objEvaluado : lstParticipantesIniciados){
        
            if(objEvaluado.isBlManual()){
                flag = true;
                CuestionarioFisico objCuestionarioFisico = new CuestionarioFisico();
                try {
                    String archivo = objCuestionarioFisico.build(objEvaluado);
                    lstArchivos.add(archivo);
                } catch (IOException ex) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Descargar evaluación", "Ocurrio un error al descarga la evaluación");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    log.error(ex);
                }
            }
        }
        
        if(!flag){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Descargar evaluación", "Debe seleccionar al menos un evaluado");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
        
            if(!lstArchivos.isEmpty()){
                try {        
                    String ZipName = "Evaluaciones_" + Constantes.STR_EXTENSION_ZIP;
                    File objFile = new File(Constantes.STR_INBOX_DEFINITIVO + File.separator + ZipName);
                    FileOutputStream salida;
                    salida = new FileOutputStream(objFile);
                    Utilitarios.zipArchivosCualquiera(lstArchivos, salida);
                    InputStream stream = new FileInputStream(objFile.getAbsolutePath());
                    fileIndividual = new DefaultStreamedContent(stream, "application/zip", ZipName);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Descargar evaluación", "Evaluación descargada");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } catch (FileNotFoundException ex) {
                    log.error(ex);
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Descargar evaluación", "No se pudo generar documento. Comuniquese con el proveedor");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            }else{
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Descargar evaluación", "No se pudo generar documento. Comuniquese con el proveedor");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
                
    }
    
    public void revertirEvaluado(Evaluado objEvaluado){

        Session sesion = HibernateUtil.getSessionFactory().openSession(); 
        Transaction tx = sesion.beginTransaction(); 
        
        try {
            
            boolean correcto;
            /* ACTUALIZA EL ESTADO DEL CUESTIONARIO */
            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

            correcto = objCuestionarioDAO.actualizaEstadoCuestionarioXEvaluado(objEvaluado, Utilitarios.obtenerProyecto().getIntIdProyecto(),Constantes.INT_ET_ESTADO_CUES_EVA_REGISTRADO,sesion);

            if(correcto){
                /* ACTUALIZA EL ESTADO DEL EVALUADO */
                ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

                Participante objParticipante = objParticipanteDAO.obtenParticipante(objEvaluado.getPaIdParticipantePk());

                objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION);

                objParticipanteDAO.actualizaParticipante(objParticipante,sesion);

            }
            
            if(correcto){
                tx.commit();
            }else{
                tx.rollback();
            }
            
        } catch (Exception e) {
            log.error(e);
            tx.rollback();
        }
        
        init();
    
    }
    
    public void retirarRelacion(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador){
    
        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
        
        RelacionParticipante objRelacionParticipante = new RelacionParticipante();
        
        RelacionParticipanteId objRelacionParticipanteId = new RelacionParticipanteId();
        
        objRelacionParticipanteId.setPaIdParticipanteFk(objRelacionEvaluadoEvaluador.getIntIdEvaluado());
        objRelacionParticipanteId.setReIdParticipanteFk(objRelacionEvaluadoEvaluador.getIntIdEvaluador());
        objRelacionParticipanteId.setReIdRelacionFk(objRelacionEvaluadoEvaluador.getIntIdRelacion());
        
        objRelacionParticipante.setId(objRelacionParticipanteId);
        
        objRelacionParticipanteDAO.eliminaRelacionParticipante(objRelacionParticipante);
        /* VERIFICA SI NO TIENE OTRAS RELACIONES, SINO CAMBIA DE ESTADO A REGISTRADO */
        //FALTA
        
        
        init();
    
    }    
    


    public void terminarProceso(){
    
        ProyectoDAO objProyectoDAO = new ProyectoDAO();
        
        objProyectoDAO.terminarProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Terminar proyecto", "El proyecto fue concluido exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
        init();
        
    }
    
    
    public void realizaEvaluacion(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador){
            try {
            
            ProyectoInfo redSeleccionada = new ProyectoInfo();
            redSeleccionada.setBoDefineArtificio(Boolean.TRUE);
            redSeleccionada.setIntIdProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
            
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            Participante objParticipante = objParticipanteDAO.obtenParticipante(Long.valueOf(objRelacionEvaluadoEvaluador.getIntIdEvaluado()));
            
            redSeleccionada.setIntIdEvaluado(objParticipante.getPaIdParticipantePk());
            redSeleccionada.setStrCorreoEvaluado(objParticipante.getPaTxCorreo());
            redSeleccionada.setStrDescEvaluado(objParticipante.getPaTxDescripcion());
            redSeleccionada.setStrCorreoEvaluador(objRelacionEvaluadoEvaluador.getStrCorreoEvaluador());
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("evalInfo");
            session.setAttribute("evalInfo", redSeleccionada);
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("ejecutarEvaluacion.jsf");
            
        } catch (IOException ex) {
            log.debug(ex);
        }
        
    
    }

    
    public void generaExcel(){

        HSSFWorkbook xlsEvaluados = new HSSFWorkbook(); 

        HSSFSheet hoja = xlsEvaluados.createSheet("Seguimiento");

        HSSFRow row = hoja.createRow(0);

        int c = 0;
        
        HSSFCell cell0 = row.createCell(c);
        HSSFRichTextString texto0 = new HSSFRichTextString("Evaluado");
        cell0.setCellValue(texto0);

        c++;
        
        HSSFCell cell1 = row.createCell(c);
        HSSFRichTextString texto1 = new HSSFRichTextString("Evaluador");
        cell1.setCellValue(texto1);

        c++;    
        
        HSSFCell cell2 = row.createCell(c);
        HSSFRichTextString texto2 = new HSSFRichTextString("Relacion");
        cell2.setCellValue(texto2);
        
        c++;    
        HSSFCell cell3 = row.createCell(c);
        HSSFRichTextString texto3 = new HSSFRichTextString("Termino evaluacion");
        cell3.setCellValue(texto3);

        int i = 1;
        for (RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador){

            HSSFRow nextrow = hoja.createRow(i);

            int r = 0;

            nextrow.createCell(r).setCellValue(objRelacionEvaluadoEvaluador.getStrDescEvaluado());
            r++;
            nextrow.createCell(r).setCellValue(objRelacionEvaluadoEvaluador.getStrDescEvaluador());
            r++;
            nextrow.createCell(r).setCellValue(objRelacionEvaluadoEvaluador.getStrDescRelacion());
            
            r++;
            if(objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada()!=null){
                if(objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada()){
                    nextrow.createCell(r).setCellValue("SI");
                }else{
                    nextrow.createCell(r).setCellValue("NO");
                }
            }else{
                nextrow.createCell(r).setCellValue("NO");
            }
            
            i++;
            
        }
        
        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
        hoja.autoSizeColumn(2);
        hoja.autoSizeColumn(3);

        try {
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"SeguimientoRed.xls\"");

            xlsEvaluados.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            log.error(e);
        }
    }

    public void enviarRecordatorio(){
    
        boolean validaAlMenosUno = false;
        for(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador){
            if(objRelacionEvaluadoEvaluador.getBlEnvioCorreo()){
                validaAlMenosUno = true;
                continue;
            }
        }
        
        if(validaAlMenosUno){
            NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
            try {
                if(objNotificacionesDAO.guardaNotificacionesEvaluadores(lstRelacionEvaluadoEvaluador)){
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Envío de notificaciones", "Se enviaron las convocatorias correctamente");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }else{
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Envío de notificaciones", "Ocurrió un error al enviar las convocatorias");            
                    FacesContext.getCurrentInstance().addMessage(null, message);            
                }
            } catch (HibernateException ex) {
                log.error(ex);
            } catch (Exception ex) {
                log.error(ex);
            }
        }else{
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Envío de notificaciones", "Se debe seleccionar al menos un evaluado");
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }
            
    }

    public void actCheckCorreo(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador){
        
        RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluadorT = lstRelacionEvaluadoEvaluador.get(lstRelacionEvaluadoEvaluador.indexOf(objRelacionEvaluadoEvaluador));

        if(objRelacionEvaluadoEvaluadorT!=null){
        
            objRelacionEvaluadoEvaluadorT.setBlEnvioCorreo(objRelacionEvaluadoEvaluador.getBlEnvioCorreo());
            
        }
        
    }

    public void putFlagDescargaFisico(){
        
        for(Evaluado objEvaluado : lstParticipantesIniciados){
            if(flagDescargaFisico){
                objEvaluado.setBlManual(Boolean.TRUE);
            }else{
                objEvaluado.setBlManual(Boolean.FALSE);
            }
            
        }
        
    }
    
    public void putFlagFiltrarRed(){
        
        for(Evaluado objEvaluado : lstParticipantesIniciados){
            if(flagFiltrarRed){
                objEvaluado.setBoCheckFilterSeg(Boolean.TRUE);
            }else{
                objEvaluado.setBoCheckFilterSeg(Boolean.FALSE);
            }
            
        }
        
        actListaEvaluadores();
                
    }

    public void putFlagComunicar(){
        
        for(RelacionEvaluadoEvaluador objRelacionEvaluadoEvaluador : lstRelacionEvaluadoEvaluador){
            if(objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada()==null || objRelacionEvaluadoEvaluador.getBlEvaluacionTerminada().equals(Boolean.FALSE)){
                if(flagComunicar){
                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.TRUE);    
                }else{
                    objRelacionEvaluadoEvaluador.setBlEnvioCorreo(Boolean.FALSE);
                }
            }
            
        }
        
    }

    
}
