package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ContratoDAO;
import com.jaio360.dao.TarifaDAO;
import com.jaio360.domain.TarifaBean;
import com.jaio360.orm.Contrato;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Tarifa;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name = "mantenimientoTarifaView")
@ViewScoped
public class MantenimientoTarifaView implements Serializable{
    
    private static Log log = LogFactory.getLog(MantenimientoTarifaView.class); 
    
    private String strDescripcion;
    private BigDecimal bdPrecio;
    private List<SelectItem> lstEstados;
    private List<TarifaBean> lstTarifas;
    
    private TarifaDAO objTarifaDAO = new TarifaDAO();
    private ContratoDAO objContratoDAO = new ContratoDAO();

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public BigDecimal getBdPrecio() {
        return bdPrecio;
    }

    public void setBdPrecio(BigDecimal bdPrecio) {
        this.bdPrecio = bdPrecio;
    }
    
    public List<TarifaBean> getLstTarifas() {
        return lstTarifas;
    }

    public void setLstTarifas(List<TarifaBean> lstTarifas) {
        this.lstTarifas = lstTarifas;
    }

    public List<SelectItem> getLstEstados() {
        return lstEstados;
    }

    public void setLstEstados(List<SelectItem> lstEstados) {
        this.lstEstados = lstEstados;
    }
    
    @PostConstruct
    public void init(){
        
        try {
         
            lstTarifas = obtenerListaTarifas();
            lstEstados = obtieneEstados();
        
        } catch (Exception e) {
            log.error(e);
        }
        
    }
   
    private List<TarifaBean> obtenerListaTarifas() {
        
        List<Contrato> lstContratos = objContratoDAO.obtenListaContrato();
        List<Tarifa> lstTarifas = objTarifaDAO.obtenListaTarifa();
        List<TarifaBean> lstTarifaBeans = new ArrayList();

        int intContratosEnUso = 0;
        int intContratosTotal = 0;
        
        TarifaBean objTarifaBean;
        
        for(Tarifa objTarifa : lstTarifas){
        
            intContratosEnUso = 0;
            intContratosTotal = 0;
                    
            objTarifaBean = new TarifaBean();
            objTarifaBean.setTaIdTarifaPk(objTarifa.getTaIdTarifaPk());
            objTarifaBean.setTaFeCreacion(Utilitarios.formatearFecha(objTarifa.getTaFeCreacion(),Constantes.DDMMYYYY));
            objTarifaBean.setTaIdEstado(objTarifa.getTaIdEstado() + "");
            objTarifaBean.setTaDePrecio(objTarifa.getTaDePrecio());
            objTarifaBean.setTaTxEstado(EHCacheManager.obtenerDescripcionElemento(objTarifa.getTaIdEstado()));
            objTarifaBean.setTaTxDescripcion(objTarifa.getTaTxDescripcion());
            
            for(Contrato objContrato : lstContratos){
                if(objContrato.getTarifa().getTaIdTarifaPk().equals(objTarifaBean.getTaIdTarifaPk())){
                    intContratosTotal++;
                    if(objContrato.getCoIdEstado().equals(Constantes.INT_ET_ESTADO_CONTRATO_CONFIRMADO)){
                        intContratosEnUso++;
                    }
                }
            }
            
            objTarifaBean.setTaNroContratoTotal(intContratosTotal);
            objTarifaBean.setTaNroContratoUso(intContratosEnUso);
            
            lstTarifaBeans.add(objTarifaBean);
        }
        
        return lstTarifaBeans;
    }

    private List<SelectItem> obtieneEstados(){
        
        List<SelectItem> lstSelectItems = new ArrayList();
                
        List<Elemento> lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_ESTADOS_TARIFA);
        
        for(Elemento objElemento : lstElementos){
            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());    
            lstSelectItems.add(objSelectItem);
        }
        
        return lstSelectItems;
    
    }
    
    public void editarTarifa(RowEditEvent event) {
        
        try {

            TarifaBean objTarifaBean = (TarifaBean) event.getObject();
            
            Tarifa objTarifa = objTarifaDAO.obtenTarifa(objTarifaBean.getTaIdTarifaPk());
            
            objTarifa.setTaTxDescripcion(objTarifaBean.getTaTxDescripcion());
            objTarifa.setTaDePrecio(objTarifaBean.getTaDePrecio());
            
            objTarifaDAO.actualizaTarifa(objTarifa);
            
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Actualizar",  "Tarifa actualizada exitosamente"));
            
        } catch (Exception e) {
            log.equals(e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Actualizar",  "Ocurrio un error al actualizar la tarifa"));
        }

    }
     
    
    public void borrarTarifa(Integer idTarifa){
        
        try {

            Tarifa objTarifa = new Tarifa();
            objTarifa.setTaIdTarifaPk(idTarifa);
            objTarifaDAO.eliminaTarifa(objTarifa);
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminar",  "Tarifa borrada exitosamente"));
            
        } catch (Exception e) {
            log.equals(e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Eliminar",  "Ocurrio un error al borrar la tarifa"));
        }

    }

    public void expirarTarifa(Integer idTarifa){
        
        try {

            Tarifa objTarifa = objTarifaDAO.obtenTarifa(idTarifa.longValue());
            objTarifa.setTaIdEstado(Constantes.INT_ET_ESTADO_TARIFA_EXPIRADO);
            objTarifaDAO.actualizaTarifa(objTarifa);
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Expirar",  "Tarifa expirada exitosamente"));
            
        } catch (Exception e) {
            log.equals(e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Expirar",  "Ocurrio un error al expirar la tarifa"));
        }

    }
        
    public void confirmaTarifa(Integer idTarifa){
        
        try {

            Tarifa objTarifa = objTarifaDAO.obtenTarifa(idTarifa.longValue());
            objTarifa.setTaIdEstado(Constantes.INT_ET_ESTADO_TARIFA_CONFIRMADO);
            objTarifaDAO.actualizaTarifa(objTarifa);
            init();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmar",  "Tarifa confirmada exitosamente"));
            
        } catch (Exception e) {
            log.equals(e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Confirmar",  "Ocurrio un error al confirmar la tarifa"));
        }

    }
        
    public void guardarTarifa(){
    
        try {
            
            Tarifa objTarifa = new Tarifa();
            
            objTarifa.setTaTxDescripcion(strDescripcion);
            objTarifa.setTaDePrecio(bdPrecio);
            objTarifa.setTaIdEstado(Constantes.INT_ET_ESTADO_TARIFA_REGISTRADO);
            objTarifa.setTaFeCreacion(Utilitarios.getCurrentDate());
            
            objTarifaDAO.guardaTarifa(objTarifa);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Confirmación",  "Se guardo correctamente"));
            init();
            
            strDescripcion = "";
            bdPrecio = null;
            
        } catch (Exception e) {

            log.error(e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Confirmación",  "Ocurrio un error al guardar"));
        }
    
    }
 
}
