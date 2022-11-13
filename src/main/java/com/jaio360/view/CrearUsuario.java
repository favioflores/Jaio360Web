package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.UbigeoDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Ubigeo;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
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
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Proxy;
import org.primefaces.PrimeFaces;

/**
 *
 * @author
 */
@ManagedBean(name = "crearUsuario")
@ViewScoped
@Proxy(lazy = false)
public class CrearUsuario extends BaseView implements Serializable {

    private static final Log log = LogFactory.getLog(CrearUsuario.class);

    private static final long serialVersionUID = -1L;

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

    private UsuarioDAO objUsuarioDAO = new UsuarioDAO();
    private ElementoDAO objElementoDAO = new ElementoDAO();

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

        lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_ESTADO_USUARIO);

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

    private void poblarCiudades() {

        lstCiudades = new ArrayList<>();
        UbigeoDAO objUbigeoDAO = new UbigeoDAO();
        lstCiudades = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_DEPARTAMENTO, pais);

    }

    private void poblarTipoCuentas() {

        List<Elemento> lstElementos;
        Iterator itLstElementos;
        lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_TIPO_CUENTA);

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
        lstElementos = objElementoDAO.obtenListaElementoXDefinicion(Constantes.INT_DT_TIPO_DOCUMENTO);

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

    @PostConstruct
    public void init() {

        poblarEstados();
        poblarPaises();
        poblarTipoCuentas();
        poblarTipoDocumento();

    }

    public void guardarUsuario(ActionEvent event) {

        try {

            if (usuarioForm != null) {

                String strErrores = esValido(usuarioForm);

                if (Utilitarios.esNuloOVacio(strErrores)) {

                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    Ubigeo ubigeo = new Ubigeo();
                    ubigeo.setUbIdUbigeoPk(pais);
                    usuarioForm.getUbigeo().setUbigeo(ubigeo);

                    EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                    usuarioForm.setUsTxContrasenia(objEncryptDecrypt.encrypt(usuarioForm.getUsTxContrasenia()));

                    if (usuarioForm.getUsIdCuentaPk() != null) {
                        usuarioDAO.actualizaUsuario(usuarioForm);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirmación", "Se actualizo correctamente"));

                    } else {
                        usuarioDAO.guardaUsuario(usuarioForm);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Confirmación", "Se guardo correctamente"));
                    }
                    usuarioForm = null;
                    //RequestContext.getCurrentInstance().closeDialog("crearUsuario");
                    PrimeFaces.current().dialog().closeDynamic("crearUsuario");

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", strErrores));
                }

            }

        } catch (Exception e) {
            log.error(e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fallo!", "Ocurrio un error al guardar el proyecto"));
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

    public void editarUsuario(UsuarioInfo usuario) {

        if (usuario != null) {

            //try{
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("usuarioSeleccionado");
            session.setAttribute("usuarioSeleccionado", usuario.getUsuario());
            //FacesContext.getCurrentInstance().getExternalContext().redirect("crearUsuario.jsf");
            Map<String, Object> options = new HashMap<>();
            options.put("modal", true);
            options.put("resizable", false);
            PrimeFaces.current().dialog().openDynamic("crearUsuario", options, null);

            /* } catch (IOException ex) {
                log.debug(ex);
            }*/
        }
    }

}
