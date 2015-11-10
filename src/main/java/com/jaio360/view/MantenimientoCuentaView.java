package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ContratoDAO;
import com.jaio360.dao.TarifaDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Contrato;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Tarifa;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "mantenimientoCuentaView")
@ViewScoped
public class MantenimientoCuentaView implements Serializable{
    
    private static Log log = LogFactory.getLog(MantenimientoCuentaView.class); 
    
    private static final long serialVersionUID = -1L;
    
    private List<UsuarioInfo> lstUsuario;
    private List<Contrato> lstContrato;
    private List<Tarifa> lstTarifa;
    private List<SelectItem> lstTipoContrato;
    private List<SelectItem> lstEstadoContrato;
    private UsuarioInfo usuarioSeleccionado;
    private Contrato contratoFormulario;
    private Contrato contratoSeleccionado;
    private String[] tipoUsuario;

    public String[] getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String[] tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<UsuarioInfo> getLstUsuario() {
        return lstUsuario;
    }

    public void setLstUsuario(List<UsuarioInfo> lstUsuario) {
        this.lstUsuario = lstUsuario;
    }

    public UsuarioInfo getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(UsuarioInfo usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<Contrato> getLstContrato() {
        return lstContrato;
    }

    public void setLstContrato(List<Contrato> lstContrato) {
        this.lstContrato = lstContrato;
    }

    public List<Tarifa> getLstTarifa() {
        return lstTarifa;
    }

    public void setLstTarifa(List<Tarifa> lstTarifa) {
        this.lstTarifa = lstTarifa;
    }

    public Contrato getContratoFormulario() {
        return contratoFormulario;
    }

    public void setContratoFormulario(Contrato contratoFormulario) {
        this.contratoFormulario = contratoFormulario;
    }

    public List<SelectItem> getLstTipoContrato() {
        return lstTipoContrato;
    }

    public void setLstTipoContrato(List<SelectItem> lstTipoContrato) {
        this.lstTipoContrato = lstTipoContrato;
    }

    public List<SelectItem> getLstEstadoContrato() {
        return lstEstadoContrato;
    }

    public void setLstEstadoContrato(List<SelectItem> lstEstadoContrato) {
        this.lstEstadoContrato = lstEstadoContrato;
    }

    public Contrato getContratoSeleccionado() {
        return contratoSeleccionado;
    }

    public void setContratoSeleccionado(Contrato contratoSeleccionado) {
        this.contratoSeleccionado = contratoSeleccionado;
    }
    
    
    
   @PostConstruct
   public void init(){
       
       tipoUsuario = new String[3];
       tipoUsuario[0] = EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_TIPO_USUARIO_ADMINISTRADOR);
       tipoUsuario[1] = EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_TIPO_USUARIO_USUARIO);
       tipoUsuario[2] = EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_TIPO_USUARIO_USUARIO_MAESTRO);
       
        lstUsuario = new ArrayList<>();
        lstContrato = new ArrayList<>();
        lstTarifa = new ArrayList<>();
        lstTipoContrato = new ArrayList<>();
        lstEstadoContrato = new ArrayList<>();
        contratoFormulario = new Contrato();
        contratoFormulario.setTarifa(new Tarifa());
        contratoFormulario.setUsuario(new Usuario());
       
       
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        TarifaDAO tarifaDAO = new TarifaDAO();
        
        List<Usuario> lstUsers = usuarioDAO.obtenListaUsuario();
        
        UsuarioInfo objUsuarioInfo;
        
        for(Usuario objUsuario : lstUsers){

            objUsuarioInfo = new UsuarioInfo(objUsuario,false);
            lstUsuario.add(objUsuarioInfo);
        }
        
        
        
        lstTarifa = tarifaDAO.obtenListaTarifa();
        
        List<Tarifa> lstTemp = new ArrayList();
        for(Tarifa objTarifa : lstTarifa){
            
            Tarifa objTarifaT = (Tarifa) SerializationUtils.clone(objTarifa);
            objTarifaT.setTaTxDescripcion(objTarifaT.getTaTxDescripcion() +  " - " + objTarifaT.getTaDePrecio());
            lstTemp.add(objTarifaT);
            
        }
        
        lstTarifa = lstTemp;
        
        //EHCacheManager objEHCacheManager = new EHCacheManager();
        List<Elemento> lstElementos;
        Iterator itLstElementos;
        
        lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_ESTADO_CONTRATO);
        
        itLstElementos = lstElementos.iterator();
        lstEstadoContrato = new ArrayList<>();
              
        while(itLstElementos.hasNext()){
            Elemento objElemento = (Elemento) itLstElementos.next(); 
            
            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());
            
            lstEstadoContrato.add(objSelectItem);
        }
        
        
        lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_TIPO_CONTRATO);
        
        itLstElementos = lstElementos.iterator();
        lstTipoContrato = new ArrayList<>();
              
        while(itLstElementos.hasNext()){
            Elemento objElemento = (Elemento) itLstElementos.next(); 
            
            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());
            
            lstTipoContrato.add(objSelectItem);
        }
        //lstContrato = contratoDAO.obtenListaContratoPorUsuario(1);
   }

    public void abrirPanel() {
        
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        //options.put("contentHeight", 500);
        //options.put("style", "width: auto !important");
         
        RequestContext.getCurrentInstance().openDialog("crearUsuario", options, null);
    }
    
    
    public void usuarioCreado() {
        init();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "El usuario fue creado exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void usuarioActualizado() {
        init();
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "El usuario fue actualizado exitosamente");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
   
   public void editarUsuario(UsuarioInfo usuario){
        
       if(usuario != null){
              
            //try{
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                session.removeAttribute("usuarioSeleccionado"); 
                session.setAttribute("usuarioSeleccionado", usuario.getUsuario());
                //FacesContext.getCurrentInstance().getExternalContext().redirect("crearUsuario.jsf");
                Map<String,Object> options = new HashMap<>();
                options.put("modal", true);
                options.put("resizable", false);
                RequestContext.getCurrentInstance().openDialog("crearUsuario", options, null);
            
           /* } catch (IOException ex) {
                log.debug(ex);
            }*/

       }
   }
   
   public void cargarContrato(SelectEvent event){
       
       UsuarioInfo usuario = (UsuarioInfo) event.getObject();
       ContratoDAO contratoDAO = new ContratoDAO();
       
       if(Utilitarios.noEsNuloOVacio(usuario)){
          
          resetFail();
          contratoFormulario.setUsuario(usuarioSeleccionado.getUsuario());
          lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuario.getIntUsuarioPk());
          /*
          ContratoDAO contratoDAO = new ContratoDAO();
          //Usuario usuFormulario = new Usuario();
          usuarioSeleccionado.setUsIdCuentaPk(usuario.getUsIdCuentaPk());
          usuarioSeleccionado.setUsIdMail(usuario.getUsIdMail());
          contratoFormulario.setUsuario(usuarioSeleccionado);
          lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuario.getUsIdCuentaPk());*/
       }
       
        
    }
   
   public void grabarContrato(){
        
        FacesContext context = FacesContext.getCurrentInstance();

        try{
            if(contratoFormulario != null 
                && contratoFormulario.getUsuario() != null
                && contratoFormulario.getUsuario().getUsIdCuentaPk().intValue() > 0 ){
                
                ContratoDAO contratoDAO = new ContratoDAO();
                
                if(Utilitarios.noEsNuloOVacio(contratoFormulario.getCoIdContratoPk())){
                    contratoDAO.actualizaContrato(contratoFormulario);
                    lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getIntUsuarioPk());
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmación",  "Se actualizo correctamente"));
                }else{
                    int idContrato = (int)contratoDAO.guardaContrato(contratoFormulario);
                    if(idContrato > 0){
                    lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getIntUsuarioPk());
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmación",  "Se guardo correctamente"));
                    }else{
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Confirmación",  "Ocurrio un error al guardar el listado"));
                    }
                }
                

                
            }else{
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Mensaje",  "Seleccione un usuario"));
            }
            
           
        }catch(Exception e){
            log.error(e);
        }   
        
    }
    
    public void resetFail(){
        
         Usuario usuario = new Usuario();
         ContratoDAO contratoDAO = new ContratoDAO();
         lstContrato = new ArrayList<>();
         
         if(contratoFormulario != null 
                && contratoFormulario.getUsuario() != null
                && contratoFormulario.getUsuario() != null
                && Utilitarios.noEsNuloOVacio(contratoFormulario.getUsuario().getUsIdCuentaPk()) ){
             
             usuario = contratoFormulario.getUsuario();
         }
         contratoFormulario = new Contrato();
         contratoFormulario.setUsuario(usuario);
         contratoFormulario.setTarifa(new Tarifa());
         lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getIntUsuarioPk());
    }
    
    public void editContrato(SelectEvent event){
       
       contratoFormulario = contratoSeleccionado;
        /* Contrato contrato = (Contrato) event.getObject();
       ContratoDAO contratoDAO = new ContratoDAO();
       contratoFormulario = new Contrato();
       contratoFormulario = contratoDAO.obtenContrato(contrato.getCoIdContratoPk());
       contratoFormulario.setUsuario(usuarioSeleccionado);
       lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getUsIdCuentaPk());*/
    }
    
    public void borrarContrato(Contrato contrato){
    
        try {
            
            ContratoDAO contratoDAO = new ContratoDAO();
            Usuario usuario = contrato.getUsuario();
            contratoDAO.eliminaContrato(contrato);
            
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Borrar contrato", "El contrato se eliminó correctamente");
            FacesContext.getCurrentInstance().addMessage(null, message);
        
            //init();
            resetFail();
            lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuario.getUsIdCuentaPk());
            
        } catch (Exception ex) {
            log.error(ex);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Borrar contrato", "Ocurrio un error al realizar esta accion. Por favor comunicate con el administrador");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
}
