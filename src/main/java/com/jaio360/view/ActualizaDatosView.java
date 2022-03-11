package com.jaio360.view;

import com.jaio360.application.EHCacheManager;
import com.jaio360.dao.UbigeoDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.Documento;
import com.jaio360.domain.Tema;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Elemento;
import com.jaio360.orm.Ubigeo;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@ManagedBean(name = "actualizaDatosView")
@ViewScoped
//@Named
//@RequestScoped
public class ActualizaDatosView implements Serializable{
    
    private static Log log = LogFactory.getLog(ActualizaDatosView.class);
    
    private String pNombre = "[A-Za-z0-9]"; 
    private String pCorreo = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";;
    
    private List<Ubigeo> lstPaises;
    private List<Ubigeo> lstCiudades;
    private List<Tema> lstTemas; 
    private List<Documento> lstTipoDocumento;
    
    /* PERSONALES */
    private Integer idUsuario;
    private String strNombre;
    @Email(message = "debe ser un mail válido")
    private String strCorreo;
    private Integer pais;    
    private Integer ciudad;
    /*
    private String cmbTema;

    public String getCmbTema() {
        return cmbTema;
    }

    public void setCmbTema(String cmbTema) {
        this.cmbTema = cmbTema;
    }
    */
        
    /* CONTRASEÑA */
    private String strContrasenia;
    private String strContraseniaNueva;
    private String strContraseniaReNueva;
    
    /* EMPRESA */
    private String strRazonSocial;
    private String strDocumento;
    private Integer intIdTipoDocumento;
    
    /* LOGO */
    private StreamedContent grafico;

    
    public StreamedContent getGrafico() {
        return grafico;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setGrafico(StreamedContent grafico) {
        this.grafico = grafico;
    }

    public Integer getIntIdTipoDocumento() {
        return intIdTipoDocumento;
    }

    public void setIntIdTipoDocumento(Integer intIdTipoDocumento) {
        this.intIdTipoDocumento = intIdTipoDocumento;
    }

    public List<Documento> getLstTipoDocumento() {
        return lstTipoDocumento;
    }

    public void setLstTipoDocumento(List<Documento> lstTipoDocumento) {
        this.lstTipoDocumento = lstTipoDocumento;
    }
    
    public List<Tema> getLstTemas() {
        return lstTemas;
    }

    public void setLstTemas(List<Tema> lstTemas) {
        this.lstTemas = lstTemas;
    }

    public String getpNombre() {
        return pNombre;
    }

    public void setpNombre(String pNombre) {
        this.pNombre = pNombre;
    }

    public String getpCorreo() {
        return pCorreo;
    }

    public void setpCorreo(String pCorreo) {
        this.pCorreo = pCorreo;
    }

    public static Log getLog() {
        return log;
    }

    public static void setLog(Log log) {
        ActualizaDatosView.log = log;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrCorreo() {
        return strCorreo;
    }

    public void setStrCorreo(String strCorreo) {
        this.strCorreo = strCorreo;
    }

    public String getStrContrasenia() {
        return strContrasenia;
    }

    public void setStrContrasenia(String strContrasenia) {
        this.strContrasenia = strContrasenia;
    }

    public String getStrContraseniaNueva() {
        return strContraseniaNueva;
    }

    public void setStrContraseniaNueva(String strContraseniaNueva) {
        this.strContraseniaNueva = strContraseniaNueva;
    }

    public String getStrContraseniaReNueva() {
        return strContraseniaReNueva;
    }

    public void setStrContraseniaReNueva(String strContraseniaReNueva) {
        this.strContraseniaReNueva = strContraseniaReNueva;
    }

    public String getStrRazonSocial() {
        return strRazonSocial;
    }

    public void setStrRazonSocial(String strRazonSocial) {
        this.strRazonSocial = strRazonSocial;
    }

    public String getStrDocumento() {
        return strDocumento;
    }

    public void setStrDocumento(String strDocumento) {
        this.strDocumento = strDocumento;
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

    private DefaultStreamedContent graphicText;

    public DefaultStreamedContent getGraphicText() {
        return graphicText;
    }

    public void setGraphicText(DefaultStreamedContent graphicText) {
        this.graphicText = graphicText;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        
        UploadedFile objUploadedFile = event.getFile();
            
        UsuarioDAO objUsuarioDAO = new UsuarioDAO();

        Usuario objUsuario = objUsuarioDAO.obtenUsuario(Utilitarios.obtenerUsuario().getIntUsuarioPk());

        objUsuario.setUsBlImagenEmpresa(objUploadedFile.getContent());

        boolean correcto = objUsuarioDAO.actualizaUsuario(objUsuario);
        
        if(correcto){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Actualizacion de logo",  "Logo actualizado correctamente"));
        }else{
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Actualizacion de logo",  "Ocurrio un error al actualizar el logo"));
        }

    }   
    
    @PostConstruct
    public void init(){
    
        lstPaises = new ArrayList<>();
        lstCiudades = new ArrayList<>();
        lstTipoDocumento = new ArrayList<>();
        
        idUsuario = Utilitarios.obtenerUsuario().getIntUsuarioPk();
        
        UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
        
        strNombre = objUsuarioInfo.getStrDescripcion();
        strCorreo = objUsuarioInfo.getStrEmail();
        strRazonSocial = objUsuarioInfo.getStrEmpresaDesc();
        strDocumento = objUsuarioInfo.getStrDocumentoEmpresa();
        intIdTipoDocumento = objUsuarioInfo.getIntIdDocumentoEmpresa();
        ciudad = objUsuarioInfo.getIntIdCiudad();
        
        List<Elemento> lstElementos = EHCacheManager.obtenerElementosPorDefinicion(Constantes.INT_DT_TIPO_DOCUMENTO);
        
        for (Elemento objElemento : lstElementos) {
            Documento objDocumento = new Documento();
            objDocumento.setIdTipoDocumento(objElemento.getElIdElementoPk());
            objDocumento.setStrDescDocumento(objElemento.getElTxDescripcion());
            lstTipoDocumento.add(objDocumento);
        }
        
        UbigeoDAO objUbigeoDAO = new UbigeoDAO();
        
        Ubigeo objUbigeo = objUbigeoDAO.obtenUbigeo(ciudad);
        
        pais = objUbigeo.getUbigeo().getUbIdUbigeoPk();
                
        lstPaises = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_PAIS, null);
        lstCiudades = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_DEPARTAMENTO, objUbigeo.getUbigeo().getUbIdUbigeoPk());
        

        
        
        //cargarTemas();
    }
    
    public void cargaCiudades(){
        
        lstCiudades = new ArrayList<>();
        UbigeoDAO objUbigeoDAO = new UbigeoDAO();
        lstCiudades = objUbigeoDAO.obtenListaUbigeo(Constantes.INT_ET_TIPO_UBIGEO_DEPARTAMENTO, pais); 
    
    }
  
    public void actualizaPersonales(){
    
        UsuarioDAO objUsuarioDAO = new UsuarioDAO();
        
        Usuario objUsuario = objUsuarioDAO.obtenUsuario(Utilitarios.obtenerUsuario().getIntUsuarioPk());
        
        Ubigeo objUbigeo = new Ubigeo();
        objUbigeo.setUbIdUbigeoPk(ciudad);
                
        objUsuario.setUsTxNombreRazonsocial(strNombre);
        objUsuario.setUbigeo(objUbigeo);
        objUsuario.setUsTxDocumento(strDocumento);
        
        objUsuarioDAO.actualizaUsuario(objUsuario);
        
        refrescarUsuarioSession(objUsuario);
        
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Actualización de datos",  "Se realizó la actualización correctamente"));
    }

    public void actualizaContraseña(){
        
        try {
        
            if(!strContraseniaNueva.equals(strContraseniaReNueva)){
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cambio de contraseña",  "La confirmacion de nueva contraseña no coincide"));
            }else{

                UsuarioDAO objUsuarioDAO = new UsuarioDAO();

                Usuario objUsuario = objUsuarioDAO.iniciaSesion(Utilitarios.obtenerUsuario().getStrEmail());

                 EncryptDecrypt objEncryptDecrypt = new EncryptDecrypt();

                if(strContrasenia.equals(objEncryptDecrypt.decrypt(objUsuario.getUsTxContrasenia()))){
                    
                    objUsuario.setUsTxContrasenia(objEncryptDecrypt.encrypt(strContrasenia));
                    objUsuarioDAO.actualizaUsuario(objUsuario);
                    
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Cambio de contraseña",  "Contraseña actualizada"));
                }else{
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Cambio de contraseña",  "La contraseña actual es incorrecta"));  
                }

            }
        } catch (Exception e) {
            log.error(e);
            FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Cambio de contraseña",  "La confirmacion de nueva contraseña no coincide"));
        }
    }

    public void actualizaEmpresa(){
        
        UsuarioDAO objUsuarioDAO = new UsuarioDAO();
        
        Usuario objUsuario = objUsuarioDAO.obtenUsuario(Utilitarios.obtenerUsuario().getIntUsuarioPk());
        
        objUsuario.setUsTxDescripcionEmpresa(strRazonSocial);
        objUsuario.setUsIdTipoDocumento(intIdTipoDocumento);
        objUsuario.setUsTxDocumento(strDocumento);
        
        objUsuarioDAO.actualizaUsuario(objUsuario);
        
        refrescarUsuarioSession(objUsuario);
    
    }
    
    private void cargarTemas(){
    
        lstTemas = new ArrayList<>();
        lstTemas.add(new Tema(0, "Afterdark", "afterdark"));
        lstTemas.add(new Tema(1, "Afternoon", "afternoon"));
        lstTemas.add(new Tema(2, "Afterwork", "afterwork"));
        lstTemas.add(new Tema(3, "Aristo", "aristo"));
        lstTemas.add(new Tema(4, "Black-Tie", "black-tie"));
        lstTemas.add(new Tema(5, "Blitzer", "blitzer"));
        lstTemas.add(new Tema(6, "Bluesky", "bluesky"));
        lstTemas.add(new Tema(7, "Bootstrap", "bootstrap"));
        lstTemas.add(new Tema(8, "Casablanca", "casablanca"));
        lstTemas.add(new Tema(9, "Cupertino", "cupertino"));
        lstTemas.add(new Tema(10, "Cruze", "cruze"));
        lstTemas.add(new Tema(11, "Dark-Hive", "dark-hive"));
        lstTemas.add(new Tema(12, "Delta", "delta"));
        lstTemas.add(new Tema(13, "Dot-Luv", "dot-luv"));
        lstTemas.add(new Tema(14, "Eggplant", "eggplant"));
        lstTemas.add(new Tema(15, "Excite-Bike", "excite-bike"));
        lstTemas.add(new Tema(16, "Flick", "flick"));
        lstTemas.add(new Tema(17, "Glass-X", "glass-x"));
        lstTemas.add(new Tema(18, "Home", "home"));
        lstTemas.add(new Tema(19, "Hot-Sneaks", "hot-sneaks"));
        lstTemas.add(new Tema(20, "Humanity", "humanity"));
        lstTemas.add(new Tema(21, "Le-Frog", "le-frog"));
        lstTemas.add(new Tema(22, "MetroUI", "metroui"));
        lstTemas.add(new Tema(23, "Midnight", "midnight"));
        lstTemas.add(new Tema(24, "Mint-Choc", "mint-choc"));
        lstTemas.add(new Tema(25, "Overcast", "overcast"));
        lstTemas.add(new Tema(26, "Pepper-Grinder", "pepper-grinder"));
        lstTemas.add(new Tema(27, "Redmond", "redmond"));
        lstTemas.add(new Tema(28, "Rocket", "rocket"));
        lstTemas.add(new Tema(29, "Sam", "sam"));
        lstTemas.add(new Tema(30, "Smoothness", "smoothness"));
        lstTemas.add(new Tema(31, "South-Street", "south-street"));
        lstTemas.add(new Tema(32, "Start", "start"));
        lstTemas.add(new Tema(32, "Sunny", "sunny"));
        lstTemas.add(new Tema(33, "Swanky-Purse", "swanky-purse"));
        lstTemas.add(new Tema(34, "Trontastic", "trontastic"));
        lstTemas.add(new Tema(35, "UI-Darkness", "ui-darkness"));
        lstTemas.add(new Tema(36, "UI-Lightness", "ui-lightness"));
        lstTemas.add(new Tema(37, "Vader", "vader"));
    }

    /*
    public void actualizaTema(){

        if(Utilitarios.noEsNuloOVacio(cmbTema)){
            PreferenciasView objPreferenciasView = new PreferenciasView();
            objPreferenciasView.getTheme();
            objPreferenciasView.setTheme(cmbTema);
            objPreferenciasView.changeTheme();
        }
    }
    */


    private void refrescarUsuarioSession(Usuario objUsuario){
    
        UsuarioInfo usuarioInfo = new UsuarioInfo(objUsuario);

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.removeAttribute("usuarioInfo"); 
        session.setAttribute("usuarioInfo", usuarioInfo);
                
    }
}

