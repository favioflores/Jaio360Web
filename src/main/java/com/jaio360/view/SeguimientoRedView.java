package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.NotificacionesDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RedEvaluacionDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.EvaluadorRelacion;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "seguimientoRedView")
@ViewScoped
public class SeguimientoRedView implements Serializable{
    
    private static Log log = LogFactory.getLog(SeguimientoRedView.class);
    
    private ProyectoDAO objProyectoDAO = new ProyectoDAO();
    private RelacionDAO objRelacionDAO = new RelacionDAO();
    private NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
    
    /***************/
    /* Seguimiento */
    /***************/
    private Integer intCorrelativo;
    private Boolean paInRedCargada;
    private Boolean paInRedVerificada;
    
    private Integer intRedCargada;
    private Integer intRedRevisada;
    private Integer cantRedCargada;
    
    private Integer intIdEstadoProyecto;
    
    private Integer intCantidadVerificados;
    
    private List<Evaluado> lstParticipantesCargados;
    private List<EvaluadorRelacion> lstRed;    
    private List<EvaluadorRelacion> filtroRed;    
    private List<Relacion> lstRelaciones;
    private List<RelacionParticipante> lstRelacionParticipante;
    private StreamedContent content;

    public Integer getCantRedCargada() {
        return cantRedCargada;
    }

    public void setCantRedCargada(Integer cantRedCargada) {
        this.cantRedCargada = cantRedCargada;
    }

    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }

    public Integer getIntCantidadVerificados() {
        return intCantidadVerificados;
    }

    public void setIntCantidadVerificados(Integer intCantidadVerificados) {
        this.intCantidadVerificados = intCantidadVerificados;
    }
    
    public Integer getIntCorrelativo() {
        return intCorrelativo;
    }

    public void setIntCorrelativo(Integer intCorrelativo) {
        this.intCorrelativo = intCorrelativo;
    }

    public Boolean getPaInRedCargada() {
        return paInRedCargada;
    }

    public void setPaInRedCargada(Boolean paInRedCargada) {
        this.paInRedCargada = paInRedCargada;
    }

    public Boolean getPaInRedVerificada() {
        return paInRedVerificada;
    }

    public void setPaInRedVerificada(Boolean paInRedVerificada) {
        this.paInRedVerificada = paInRedVerificada;
    }

    public List<Evaluado> getLstParticipantesCargados() {
        return lstParticipantesCargados;
    }

    public void setLstParticipantesCargados(List<Evaluado> lstParticipantesCargados) {
        this.lstParticipantesCargados = lstParticipantesCargados;
    }

    public List<RelacionParticipante> getLstRelacionParticipante() {
        return lstRelacionParticipante;
    }

    public void setLstRelacionParticipante(List<RelacionParticipante> lstRelacionParticipante) {
        this.lstRelacionParticipante = lstRelacionParticipante;
    }

    public List<EvaluadorRelacion> getLstRed() {
        return lstRed;
    }

    public void setLstRed(List<EvaluadorRelacion> lstRed) {
        this.lstRed = lstRed;
    }

    public List<EvaluadorRelacion> getFiltroRed() {
        return filtroRed;
    }

    public void setFiltroRed(List<EvaluadorRelacion> filtroRed) {
        this.filtroRed = filtroRed;
    }

    public List<Relacion> getLstRelaciones() {
        return lstRelaciones;
    }

    public void setLstRelaciones(List<Relacion> lstRelaciones) {
        this.lstRelaciones = lstRelaciones;
    }

    public Integer getIntRedCargada() {
        return intRedCargada;
    }

    public void setIntRedCargada(Integer intRedCargada) {
        this.intRedCargada = intRedCargada;
    }

    public Integer getIntRedRevisada() {
        return intRedRevisada;
    }

    public void setIntRedRevisada(Integer intRedRevisada) {
        this.intRedRevisada = intRedRevisada;
    }
        
    public StreamedContent getContent() {
        return content;
    }

    public void setContent(StreamedContent content) {
        this.content = content;
    }
    
    /***************/
    /* Iniciar Red */
    /***************/
    private Integer intNumEvadoReg;
    private Integer intNumEvadoPam;
    private Integer intNumEvadoEje;
    private Integer intNumEvadorReg;
    private Integer intNumEvadorPam;
    private Integer intNumEvadorEje;

    public Integer getIntNumEvadorPam() {
        return intNumEvadorPam;
    }

    public void setIntNumEvadorPam(Integer intNumEvadorPam) {
        this.intNumEvadorPam = intNumEvadorPam;
    }

    public Integer getIntNumEvadoReg() {
        return intNumEvadoReg;
    }

    public void setIntNumEvadoReg(Integer intNumEvadoReg) {
        this.intNumEvadoReg = intNumEvadoReg;
    }

    public Integer getIntNumEvadoPam() {
        return intNumEvadoPam;
    }

    public void setIntNumEvadoPam(Integer intNumEvadoPam) {
        this.intNumEvadoPam = intNumEvadoPam;
    }

    public Integer getIntNumEvadoEje() {
        return intNumEvadoEje;
    }

    public void setIntNumEvadoEje(Integer intNumEvadoEje) {
        this.intNumEvadoEje = intNumEvadoEje;
    }

    public Integer getIntNumEvadorReg() {
        return intNumEvadorReg;
    }

    public void setIntNumEvadorReg(Integer intNumEvadorReg) {
        this.intNumEvadorReg = intNumEvadorReg;
    }

    public Integer getIntNumEvadorEje() {
        return intNumEvadorEje;
    }

    public void setIntNumEvadorEje(Integer intNumEvadorEje) {
        this.intNumEvadorEje = intNumEvadorEje;
    }
    
    @PostConstruct
    public void init() {

        lstParticipantesCargados = new ArrayList();
        cantRedCargada = 0;
        
        intIdEstadoProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto()).getPoIdEstado();
                
        lstRelaciones = objRelacionDAO.obtenListaRelacionPorProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
                
        cargarResumenParticipantes();
        cargarListaParticipantesCargados();
        calculaPorcentajes();
        
    }
    
    public void onRowSelect(SelectEvent event) {
        Evaluado selectedItem = (Evaluado) event.getObject();
        
        RelacionParticipanteDAO relacionParticipanteDAO = new RelacionParticipanteDAO();        
        
        this.lstRed = relacionParticipanteDAO.obtenListaEvaluadorRelacion(selectedItem.getPaIdParticipantePk());
        
        //EvaluadorRelacion red = this.lstRed.get(0);
    }
    
    public void grabarLista(){
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        try{
            
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            
            Participante objParticipante;
            
            Proyecto objProyecto = new Proyecto();
            
            objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());
            
            for (Evaluado obj:lstParticipantesCargados){  
                
                objParticipante = new Participante();

                objParticipante.setPaIdParticipantePk(obj.getPaIdParticipantePk());
                objParticipante.setPaIdEstado(obj.getPaIdEstado());
                objParticipante.setPaIdTipoParticipante(obj.getPaIdTipoParticipante());
                objParticipante.setPaInAutoevaluar(obj.isPaInAutoevaluar());
                objParticipante.setPaInRedCargada(obj.isPaInRedCargada());
                objParticipante.setPaInRedVerificada(obj.isPaInRedVerificada());
                objParticipante.setPaTxCorreo(obj.getPaTxCorreo());
                objParticipante.setPaTxDescripcion(obj.getPaTxDescripcion());
                objParticipante.setPaTxNombreCargo(obj.getPaTxNombreCargo());
                
                objParticipante.setPaTxSexo(obj.getPaTxSexo());
                objParticipante.setPaTxAreaNegocio(obj.getPaTxAreaNegocio());
                objParticipante.setPaTxOcupacion(obj.getPaTxOcupacion());
                objParticipante.setPaNrEdad(obj.getPaNrEdad());
                objParticipante.setPaNrTiempoTrabajo(obj.getPaNrTiempoTrabajo());
                
                objParticipante.setProyecto(objProyecto);

                objParticipanteDAO.actualizaParticipante(objParticipante);

            }
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmacion",  "Verificacion guardada") );
            
        }catch(Exception e){
            log.error(e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Fallo!","Ocurrio un error al guardar la Relación"));
        }   
        this.calculaPorcentajes();
    }
    
    public void calculaPorcentajes(){
        
        intRedCargada = 0;
        intRedRevisada = 0;
        
        int cantRedCargada = 0;
        int cantRedRevisada = 0;
        int total = this.lstParticipantesCargados.size();
        
        for (Evaluado objEvaluado : this.lstParticipantesCargados){  
            
            if(objEvaluado.isPaInRedCargada()){
                cantRedCargada++;
            }            
            if(objEvaluado.isPaInRedVerificada()){
                cantRedRevisada++;
            }
        }
        
        if(total>0){
            this.cantRedCargada = cantRedCargada;
            this.intRedCargada = (int)Math.floor(cantRedCargada*100/total);
            this.intRedRevisada = (int)Math.floor(cantRedRevisada*100/total);
        }
        
    }
    /*
    public void generarArchivo(){
        try {
            this.content = new DefaultStreamedContent(new FileInputStream(
                       //new File("D:\\evaluados.xls")),"application/xls","evaluados");
                       new File("D:\\jhon.xls")),"application/xls","evaluados");
           }
           catch(Exception e){
               // Nothing
           }
    }
    */
    
    
    public void iniciarRed(){
    
        FacesContext context = FacesContext.getCurrentInstance();

        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
        UsuarioDAO objUsuarioDAO = new UsuarioDAO();
       
        /* CREAMOS LISTA DE USUARIOS EVALUANTES */
        Map hUsuarios = obtieneUsuarios();
        
        List<Participante> lstParticipante = objParticipanteDAO.obtenListaParticipanteXEstado(Utilitarios.obtenerProyecto().getIntIdProyecto(),Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO, null);
        List<RedEvaluacion> lstRedEvaluacion = objRedEvaluacionDAO.obtenListaRedEvaluacionXEstado(Utilitarios.obtenerProyecto().getIntIdProyecto(),Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO);
        
        Map hEvaluados = new HashMap();
        
        for (Participante objParticipante : lstParticipante){
            if(!hEvaluados.containsKey(objParticipante.getPaTxCorreo())){
                hEvaluados.put(objParticipante.getPaTxCorreo(),objParticipante.getPaTxDescripcion());
            }
        }
        
        List<Usuario> lstUsuariosGrabados = objUsuarioDAO.crearCuentasParaProyecto(hEvaluados, hUsuarios);
        
        boolean correcto = true;
                    
        if(lstUsuariosGrabados.size()>0 && !lstParticipante.isEmpty()){
            
            /* CAMBIA EL ESTADO A LOS EVALUADOS */
            correcto = objParticipanteDAO.actualizarParticipantesRegistrados(Utilitarios.obtenerProyecto().getIntIdProyecto(), lstParticipante);
        
            if(correcto){
                
                /* REGISTRA NOTIFICACIONES */
                registranotificaciones(lstUsuariosGrabados);
            
                //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Proceso de inicio de carga",  "Proceso iniciado correctamente"));  
                
            }else{
                
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Proceso de inicio de carga",  "Ocurrio un error al iniciar el proceso. Intenta nuevamente"));
                
            }
            
        }else{
            if(lstParticipante.size()>0){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Proceso de inicio de carga",  "Ocurrio un error al iniciar el proceso. Intenta nuevamente"));  
            }
        }

        if(!lstRedEvaluacion.isEmpty()){
            correcto = objRedEvaluacionDAO.actualizarRedEvaluacionRegistrados(Utilitarios.obtenerProyecto().getIntIdProyecto(), lstRedEvaluacion);
            
        }
        
        if(correcto){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Proceso de inicio de carga",  "Proceso iniciado correctamente"));  
        }else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Proceso de inicio de carga",  "Ocurrio un error al iniciar el proceso. Intenta nuevamente"));
        }
        
        init();
        
    }
    
    private Map obtieneUsuarios(){

        UsuarioDAO objUsuarioDAO = new UsuarioDAO();
        
        List<Usuario> lstUsuarios = objUsuarioDAO.obtenListaUsuario();
        
        Map hUsuarios = new HashMap();
        
        for (Usuario objUsuario : lstUsuarios){
            hUsuarios.put(objUsuario.getUsIdMail(), objUsuario);
        }

        return hUsuarios;
    }

    private void registranotificaciones(List<Usuario> lstUsuariosGrabados) {
        
        try {
            
            NotificacionesDAO objNotificacionesDAO = new NotificacionesDAO();
            objNotificacionesDAO.guardaNotificacionesEvaluados(lstUsuariosGrabados);

            //message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Envio de correo", "La notificación se envió exitosamente");
        } catch (HibernateException ex) {
            Logger.getLogger(SeguimientoRedView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SeguimientoRedView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    private void cargarListaParticipantesCargados() {
        
        lstParticipantesCargados = new ArrayList<>();
        
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        
        List<Participante> lstParticipantesCargados = objParticipanteDAO.obtenListaParticipanteXProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
        
        Evaluado objEvaluado;
        
        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
        
        for(Participante objParticipante : lstParticipantesCargados){

            if(!objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)){
                
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
                objEvaluado.setPaTxSexo(objParticipante.getPaTxSexo());
                objEvaluado.setPaTxAreaNegocio(objParticipante.getPaTxAreaNegocio());
                objEvaluado.setPaTxOcupacion(objParticipante.getPaTxOcupacion());
                objEvaluado.setPaNrEdad(objParticipante.getPaNrEdad());
                objEvaluado.setPaNrTiempoTrabajo(objParticipante.getPaNrTiempoTrabajo());
                if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION) &&
                   objParticipante.getPaInRedCargada().equals(Boolean.FALSE)){
                    objEvaluado.setBlEnvioCorreo(Boolean.TRUE);
                }else{
                    objEvaluado.setBlEnvioCorreo(Boolean.FALSE);
                }
                
                objEvaluado.setBoCheckFilterSeg(Boolean.TRUE);
                
                objEvaluado.setIntCantidadRed(objRelacionParticipanteDAO.obtenListaRelParticipante(objParticipante.getPaIdParticipantePk()).size());
                
                this.lstParticipantesCargados.add(objEvaluado);
            }
            
        }
       
        actContCheckVerificado();
        calculaPorcentajes();
        
    }

    private void cargarResumenParticipantes() {
        
        intNumEvadoReg = 0;
        intNumEvadoPam = 0;
        intNumEvadoEje = 0;
        intNumEvadorReg = 0;
        intNumEvadorPam = 0;
        intNumEvadorEje = 0;
        
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        
        List<Participante> lstParticipantes = objParticipanteDAO.obtenListaParticipanteXProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        if(!lstParticipantes.isEmpty()){
            
            for (Participante objParticipante : lstParticipantes){
        
                if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)){
                    intNumEvadoReg++;
                }else if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)){
                    intNumEvadoPam++;
                }else if(objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION)){
                    intNumEvadoEje++;
                }
        
            }
            
        }
        
        RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
        
        List<RedEvaluacion> lstRedEvaluacion = objRedEvaluacionDAO.obtenerListaEvaluadores(Utilitarios.obtenerProyecto().getIntIdProyecto());

        if(!lstRedEvaluacion.isEmpty()){
            
            for (RedEvaluacion objRedEvaluacion : lstRedEvaluacion){
        
                if(objRedEvaluacion.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO)){
                    intNumEvadorReg++;
                }else if(objRedEvaluacion.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_EN_PARAMETRIZACION)){
                    intNumEvadorPam++;
                }else if(objRedEvaluacion.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_EN_EJECUCION)){
                    intNumEvadorEje++;
                }
        
            }
            
        }
        
    }

    public void refrescarIndicadores(){
        cargarListaParticipantesCargados();
        cargarResumenParticipantes();
    }
    
    public void actContCheckVerificado(){
        
        intCantidadVerificados = 0;
        
        Integer c = 0;
        
        for(Evaluado objEvaluado : lstParticipantesCargados){
            if(objEvaluado.isPaInRedVerificada()){
                intCantidadVerificados++;
            }
            if(!objEvaluado.isPaInRedVerificada() || objEvaluado.isPaInRedCargada()){
                intCantidadVerificados++;
            }
            if(!objEvaluado.isPaInRedVerificada() && !objEvaluado.isPaInRedCargada()){
                c++;
            }
        }
        
        if(c.equals(lstParticipantesCargados.size())){
            intCantidadVerificados = 0; 
        }
        
    }
    
    public void actContCheckCargado(Integer intIdEvaluado){
        
        for(Evaluado objEvaluado : lstParticipantesCargados){
            if(objEvaluado.getPaIdParticipantePk().equals(intIdEvaluado)){
                objEvaluado.setPaInRedVerificada(Boolean.FALSE);
            }
        }
        
    }
    
    public void irRed(Evaluado objEvaluado){
        
        try {
            
            ProyectoInfo redSeleccionada = new ProyectoInfo();
            redSeleccionada.setBoDefineArtificio(Boolean.TRUE);
            redSeleccionada.setIntIdProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());
            redSeleccionada.setStrCorreoEvaluado(objEvaluado.getPaTxCorreo());
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("redInfo");
            session.setAttribute("redInfo", redSeleccionada);
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("defineRed.jsf");
            
        } catch (IOException ex) {
            log.debug(ex);
        }
        
    }
    
    public void colocaRedVerificada(Evaluado objEvaluado){
        
        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
        
        List<RelacionParticipante> lstRelacionParticipantes = objRelacionParticipanteDAO.obtenListaRelParticipante(objEvaluado.getPaIdParticipantePk());
        
        if(lstRelacionParticipantes.isEmpty()){
            objEvaluado.setPaInRedCargada(false);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al seleccionar carga de red",  "Al menos debe tener una relación") );
        
        }else{
        
            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            
            Participante objParticipante = objParticipanteDAO.obtenParticipante(objEvaluado.getPaIdParticipantePk());
            
            objParticipante.setPaInRedCargada(objEvaluado.isPaInRedCargada());
            
            if(!objEvaluado.isPaInRedCargada()){
                objParticipante.setPaInRedVerificada(false);
            }
            
            objParticipanteDAO.actualizaParticipante(objParticipante);
            
            init();
        }
        
    }
    
    public void revertirEvaluado(Evaluado objEvaluado){
        
        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        
        boolean correcto = objParticipanteDAO.revertirEvaluadoRed(objEvaluado.getPaIdParticipantePk());
        
        if(correcto){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Reversion de evaluado",  "El evaluado fue retirado del proceso de definición de redes") );
        }else{
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Reversion de evaluado",  "Ocurrio un error al intentar retirar al evaluado de la definición de redes") );
        }
        
        init();
        
    }
    
    public void actCheckCorreo(Evaluado objEvaluado){
        for(Evaluado objEvaluadoT : lstParticipantesCargados){
        
            if(objEvaluado.getPaIdParticipantePk().equals(objEvaluadoT.getPaIdParticipantePk())){
                
                objEvaluadoT.setBlEnvioCorreo(objEvaluado.isBlEnvioCorreo());
                
            }
        
        }
    }

     public void abrirPanel(Integer intIdTipoNotificacion, Integer recordatorio) {
        
        boolean correcto = false;
        for(Evaluado objEvaluado : lstParticipantesCargados){
            if(objEvaluado.isBlEnvioCorreo()){
                correcto = true;
            }
        }
        
        if(correcto){
            Map<String,Object> options = new HashMap();
            options.put("modal", true);
            options.put("resizable", false);
            options.put("contentWidth", 600);
            //options.put("style", "width: auto !important");

            Map<String,List<String>> params = new HashMap();
            List<String> list1 = new ArrayList();
            list1.add(intIdTipoNotificacion.toString());
            List<String> list2 = new ArrayList();
            list2.add(recordatorio.toString());
            params.put("strTipoNotificacion", list1);
            params.put("strRecordatorio", list2);

            List<String> list3 = new ArrayList();
            for(Evaluado objEvaluado : lstParticipantesCargados){
                if(objEvaluado.isBlEnvioCorreo()){
                    list3.add(objEvaluado.getPaTxCorreo());
                }
            }
            params.put("evaluados", list3);

            PrimeFaces.current().dialog().openDynamic("crearNotificacion", options, params);
        }else{
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Envio de comunicados",  "Es necesario seleccionar al menos un evaluado.") );
        }
    }
     
}
