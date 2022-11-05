package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ContratoDAO;
import com.jaio360.dao.TarifaDAO;
import com.jaio360.dao.UbigeoDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Contrato;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Tarifa;
import com.jaio360.orm.Ubigeo;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "mantenimientoCuentaView")
@ViewScoped
public class MantenimientoCuentaView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(MantenimientoCuentaView.class);

    private static final long serialVersionUID = -1L;

    private List<UsuarioInfo> lstUsuario;
    private List<UsuarioInfo> filteredUsuarios;
    private List<Contrato> lstContrato;
    private List<Tarifa> lstTarifa;
    private List<SelectItem> lstTipoContrato;
    private List<SelectItem> lstEstadoContrato;
    private UsuarioInfo usuarioSeleccionado;
    private Contrato contratoFormulario;
    private Contrato contratoSeleccionado;
    private String[] tipoUsuario;
    private String strNombre;
    private String strDescripcion;
    private String strMetodologia;
    private Integer intIdEstado;
    private Integer intIdTipoCuenta;
    private Integer intIdTipoDocumento;
    private Usuario usuarioForm;
    private List<SelectItem> lstEstados;
    private List<SelectItem> lstTipoCuenta;
    private List<SelectItem> lstTipoDocumento;
    private List<Ubigeo> lstPaises;
    private List<Ubigeo> lstCiudades;
    private Integer pais;
    private Integer ciudad;
    private boolean isEdit;

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    private UsuarioDAO objUsuarioDAO = new UsuarioDAO();

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

    public Integer getIntIdEstado() {
        return intIdEstado;
    }

    public void setIntIdEstado(Integer intIdEstado) {
        this.intIdEstado = intIdEstado;
    }

    public List<SelectItem> getLstEstados() {
        return lstEstados;
    }

    public void setLstEstados(List<SelectItem> lstEstados) {
        this.lstEstados = lstEstados;
    }

    public List<SelectItem> getLstTipoCuenta() {
        return lstTipoCuenta;
    }

    public void setLstTipoCuenta(List<SelectItem> lstTipoCuenta) {
        this.lstTipoCuenta = lstTipoCuenta;
    }

    public List<SelectItem> getLstTipoDocumento() {
        return lstTipoDocumento;
    }

    public void setLstTipoDocumento(List<SelectItem> lstTipoDocumento) {
        this.lstTipoDocumento = lstTipoDocumento;
    }

    public Integer getIntIdTipoCuenta() {
        return intIdTipoCuenta;
    }

    public void setIntIdTipoCuenta(Integer intIdTipoCuenta) {
        this.intIdTipoCuenta = intIdTipoCuenta;
    }

    public Integer getIntIdTipoDocumento() {
        return intIdTipoDocumento;
    }

    public void setIntIdTipoDocumento(Integer intIdTipoDocumento) {
        this.intIdTipoDocumento = intIdTipoDocumento;
    }

    public Usuario getUsuarioForm() {
        return usuarioForm;
    }

    public void setUsuarioForm(Usuario usuarioForm) {
        this.usuarioForm = usuarioForm;
    }

    public List<Ubigeo> getLstPaises() {
        return lstPaises;
    }

    public void setLstPaises(List<Ubigeo> lstPaises) {
        this.lstPaises = lstPaises;
    }

    public List<Ubigeo> getLstCiudades() {
        return lstCiudades;
    }

    public void setLstCiudades(List<Ubigeo> lstCiudades) {
        this.lstCiudades = lstCiudades;
    }

    public Integer getPais() {
        return pais;
    }

    public void setPais(Integer pais) {
        this.pais = pais;
    }

    public Integer getCiudad() {
        return ciudad;
    }

    public void setCiudad(Integer ciudad) {
        this.ciudad = ciudad;
    }

    public String[] getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String[] tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<UsuarioInfo> getFilteredUsuarios() {
        return filteredUsuarios;
    }

    public void setFilteredUsuarios(List<UsuarioInfo> filteredUsuarios) {
        this.filteredUsuarios = filteredUsuarios;
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
    public void init() {

        poblarEstados();
        poblarPaises();
        poblarTipoCuentas();
        poblarTipoDocumento();
        //poblarCiudades();
        obtenerListaUsuarios();

        tipoUsuario = new String[3];
        tipoUsuario[0] = EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_TIPO_USUARIO_ADMINISTRADOR);
        tipoUsuario[1] = EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_TIPO_USUARIO_USUARIO);
        tipoUsuario[2] = EHCacheManager.obtenerDescripcionElemento(Constantes.INT_ET_TIPO_USUARIO_USUARIO_MAESTRO);

        lstContrato = new ArrayList<>();
        lstTarifa = new ArrayList<>();
        lstTipoContrato = new ArrayList<>();
        lstEstadoContrato = new ArrayList<>();
        contratoFormulario = new Contrato();
        contratoFormulario.setTarifa(new Tarifa());
        contratoFormulario.setUsuario(new Usuario());

        TarifaDAO tarifaDAO = new TarifaDAO();

        
        lstTarifa = tarifaDAO.obtenListaTarifa();

        List<Tarifa> lstTemp = new ArrayList();
        for (Tarifa objTarifa : lstTarifa) {

            Tarifa objTarifaT = (Tarifa) SerializationUtils.clone(objTarifa);
            objTarifaT.setTaTxDescripcion(objTarifaT.getTaTxDescripcion() + " - " + objTarifaT.getTaDePrecio());
            lstTemp.add(objTarifaT);

        }

        lstTarifa = lstTemp;

        //EHCacheManager objEHCacheManager = new EHCacheManager();
        List<Elemento> lstElementos;
        Iterator itLstElementos;

        lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_ESTADO_CONTRATO);

        itLstElementos = lstElementos.iterator();
        lstEstadoContrato = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstEstadoContrato.add(objSelectItem);
        }

        lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_TIPO_CONTRATO);

        itLstElementos = lstElementos.iterator();
        lstTipoContrato = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstTipoContrato.add(objSelectItem);
        }
        //lstContrato = contratoDAO.obtenListaContratoPorUsuario(1);
    }
    
    private void obtenerListaUsuarios(){
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        lstUsuario = new ArrayList<>();
        
        List<Usuario> lstUsers = usuarioDAO.obtenListaUsuario();

        UsuarioInfo objUsuarioInfo;

        for (Usuario objUsuario : lstUsers) {

            objUsuarioInfo = new UsuarioInfo(objUsuario, false);
            lstUsuario.add(objUsuarioInfo);
        }

    }

    public void abrirPanel() {

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        //options.put("contentHeight", 500);
        //options.put("style", "width: auto !important");

        PrimeFaces.current().dialog().openDynamic("crearUsuario", options, null);
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

    public void editarUsuario(UsuarioInfo objUsuario) {

        if (objUsuario != null) {

            try {

                usuarioForm = objUsuario.getUsuario();
                isEdit = true;

                UbigeoDAO objUbigeoDAO = new UbigeoDAO();

                ciudad = objUsuario.getIntIdCiudad();
                pais = objUbigeoDAO.obtenPais(objUsuario.getIntIdCiudad());
                
                poblarCiudades();

            } catch (Exception ex) {
                log.error(ex);
            }
        }
    }

    public void cargarContrato(SelectEvent event) {

        UsuarioInfo usuario = (UsuarioInfo) event.getObject();
        ContratoDAO contratoDAO = new ContratoDAO();

        if (Utilitarios.noEsNuloOVacio(usuario)) {

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

    public void grabarContrato() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            if (contratoFormulario != null
                    && contratoFormulario.getUsuario() != null
                    && contratoFormulario.getUsuario().getUsIdCuentaPk().intValue() > 0) {

                ContratoDAO contratoDAO = new ContratoDAO();

                if (Utilitarios.noEsNuloOVacio(contratoFormulario.getCoIdContratoPk())) {
                    contratoDAO.actualizaContrato(contratoFormulario);
                    lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getIntUsuarioPk());
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", "Se actualizo correctamente"));
                } else {
                    int idContrato = (int) contratoDAO.guardaContrato(contratoFormulario);
                    if (idContrato > 0) {
                        lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getIntUsuarioPk());
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmación", "Se guardo correctamente"));
                    } else {
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Confirmación", "Ocurrio un error al guardar el listado"));
                    }
                }

            } else {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Mensaje", "Seleccione un usuario"));
            }

        } catch (Exception e) {
            log.error(e);
        }

    }

    public void resetFail() {

        Usuario usuario = new Usuario();
        ContratoDAO contratoDAO = new ContratoDAO();
        lstContrato = new ArrayList<>();

        if (contratoFormulario != null
                && contratoFormulario.getUsuario() != null
                && contratoFormulario.getUsuario() != null
                && Utilitarios.noEsNuloOVacio(contratoFormulario.getUsuario().getUsIdCuentaPk())) {

            usuario = contratoFormulario.getUsuario();
        }
        contratoFormulario = new Contrato();
        contratoFormulario.setUsuario(usuario);
        contratoFormulario.setTarifa(new Tarifa());
        lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getIntUsuarioPk());
    }

    public void editContrato(SelectEvent event) {

        contratoFormulario = contratoSeleccionado;
        /* Contrato contrato = (Contrato) event.getObject();
       ContratoDAO contratoDAO = new ContratoDAO();
       contratoFormulario = new Contrato();
       contratoFormulario = contratoDAO.obtenContrato(contrato.getCoIdContratoPk());
       contratoFormulario.setUsuario(usuarioSeleccionado);
       lstContrato = contratoDAO.obtenListaContratoPorUsuario(usuarioSeleccionado.getUsIdCuentaPk());*/
    }

    public void borrarContrato(Contrato contrato) {

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

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (Utilitarios.esNuloOVacio(filterText)) {
            return true;
        }

        UsuarioInfo usuario = (UsuarioInfo) value;
        return usuario.getStrDescripcion().toLowerCase().contains(filterText)
                || usuario.getStrEmail().toLowerCase().contains(filterText)
                || usuario.getStrEmpresaDesc().toLowerCase().contains(filterText)
                || usuario.getStrFechaRegistro().toLowerCase().contains(filterText)
                || usuario.getStrTipoUsuario().toLowerCase().contains(filterText);
    }

    public void guardarUsuario(ActionEvent event) {

        try {

            if (usuarioForm != null) {

                String strErrores = esValido(usuarioForm);

                if (Utilitarios.esNuloOVacio(strErrores)) {

                    UsuarioDAO usuarioDAO = new UsuarioDAO();

                    if (usuarioForm.getUsIdCuentaPk() != null) {
                        usuarioDAO.actualizaUsuario(usuarioForm);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario " + usuarioForm.getUsIdMail() + " actualizado correctamente", null));

                    } else {
                        EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();
                        usuarioForm.setUsTxContrasenia(objEncryptDecrypt.encrypt(Utilitarios.generarClave()));
                        usuarioForm.getUbigeo().setUbIdUbigeoPk(ciudad);
                        usuarioForm.setUsIdEstado(Constantes.INT_ET_ESTADO_USUARIO_REGISTRADO);
                        usuarioForm.setUsFeRegistro(new Date());
                        usuarioDAO.guardaUsuario(usuarioForm);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario creado correctamente", null));
                        
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, strErrores, null));
                }
                
                resetFormUsuario();
                obtenerListaUsuarios();

            }

        } catch (Exception e) {
            log.error(e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrio un error al guardar", null));
        }

    }
    
    private void resetFormUsuario(){
        
        usuarioForm = new Usuario();
        pais = null;
        ciudad = null;
        isEdit = false;
        
    }
            

    private void poblarPaises() {
        lstCiudades = new ArrayList<>();

        usuarioForm = new Usuario();
        usuarioForm.setUbigeo(new Ubigeo());

        UbigeoDAO objUbigeoDAO = new UbigeoDAO();
        lstPaises = new ArrayList<>();
        lstPaises = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_PAIS, null);
    }

    private void poblarEstados() {

        List<Elemento> lstElementos;
        Iterator itLstElementos;

        lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_ESTADO_USUARIO);

        itLstElementos = lstElementos.iterator();
        lstEstados = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstEstados.add(objSelectItem);
        }
    }

    public void poblarCiudades() {

        lstCiudades = new ArrayList<>();
        UbigeoDAO objUbigeoDAO = new UbigeoDAO();
        lstCiudades = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_DEPARTAMENTO, pais);

    }

    private void poblarTipoCuentas() {

        List<Elemento> lstElementos;
        Iterator itLstElementos;
        lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_TIPO_CUENTA);

        itLstElementos = lstElementos.iterator();
        lstTipoCuenta = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstTipoCuenta.add(objSelectItem);
        }
    }

    private void poblarTipoDocumento() {

        List<Elemento> lstElementos;
        Iterator itLstElementos;
        lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_TIPO_DOCUMENTO);

        itLstElementos = lstElementos.iterator();
        lstTipoDocumento = new ArrayList<>();

        while (itLstElementos.hasNext()) {
            Elemento objElemento = (Elemento) itLstElementos.next();

            SelectItem objSelectItem = new SelectItem();
            objSelectItem.setValue(objElemento.getElIdElementoPk());
            objSelectItem.setLabel(objElemento.getElTxDescripcion());

            lstTipoDocumento.add(objSelectItem);
        }
    }

    private String esValido(Usuario usuarioForm) {

        /* SI SE MODIFICA UNA CUENTA EXISTENTE */
        if (Utilitarios.noEsNuloOVacio(usuarioForm.getUsIdCuentaPk())) {

            Usuario objUsuario = objUsuarioDAO.obtenUsuario(usuarioForm.getUsIdCuentaPk());

            if (!objUsuario.getUsIdMail().toUpperCase().equals(usuarioForm.getUsIdMail().toUpperCase())) {
                if (objUsuarioDAO.iniciaSesion(usuarioForm.getUsIdMail()) != null) {
                    return "El mail ingresado ya está siendo utilizado";
                }
            }

        }

        /* SI SE CREA UNA CUENTA NUEVA*/
        return Constantes.strVacio;
    }

}
