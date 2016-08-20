package com.jaio360.view;

import com.jaio360.domain.UsuarioInfo;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "menuPrincipal")
@ViewScoped
public class MenuPrincipalView implements Serializable{
    
    private MenuModel model;
    private UsuarioInfo usuarioInfo;

    public UsuarioInfo getUsuarioInfo() {
        return usuarioInfo;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }
    
    public MenuPrincipalView() {
        
        model = new DefaultMenuModel();
        
        UsuarioSesion objUsuarioSesion = new UsuarioSesion();
        
        UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();
        
        if(objUsuarioInfo.isBoEsAdministrador()){
            DefaultSubMenu smAdministrador = new DefaultSubMenu("Opciones de Administrador");
            opcionesAdministrador(smAdministrador);
            model.addElement(smAdministrador);
        }
        
        DefaultSubMenu smUsuario = new DefaultSubMenu("Opciones de Usuario");
        
        if(objUsuarioInfo.isBoEsUsuarioMaestro() || objUsuarioInfo.isBoEsAdministrador()){
            opcionesUsuarioMaestro(smUsuario);
        }
        opcionesUsuario(smUsuario);
        model.addElement(smUsuario);
        
    }
    
    public MenuModel getModel() {
        return model;
    }   
    
    private void opcionesAdministrador(DefaultSubMenu smAdministrador){

        //DefaultMenuItem item1 = new DefaultMenuItem("Panel de Indicadores");
        DefaultMenuItem item2 = new DefaultMenuItem("Mantenimiento de Tarifas");
        DefaultMenuItem item3 = new DefaultMenuItem("Mantenimiento de Cuentas");
        
        //item1.setUrl("#");
        item2.setUrl("principalMantenimientoTarifas.jsf");
        item3.setUrl("principalMantenimientoCuenta.jsf");
        
        //item1.setIcon("ui-icon-bookmark");
        item2.setIcon("ui-icon-calculator");
        item3.setIcon("ui-icon-person");
        
        //smAdministrador.addElement(item1);
        smAdministrador.addElement(item2);
        smAdministrador.addElement(item3);
        
    }
    
        private void opcionesUsuarioMaestro(DefaultSubMenu smUsuario){

        DefaultMenuItem item5 = new DefaultMenuItem("Bandeja Principal");
        
        DefaultMenuItem item2 = new DefaultMenuItem("Biblioteca de personas");
        //DefaultMenuItem item3 = new DefaultMenuItem("Buscar Proyectos");
        DefaultMenuItem item4 = new DefaultMenuItem("Biblioteca de Preguntas");
        //DefaultMenuItem item6 = new DefaultMenuItem("Facturación");
        
        item5.setUrl("principal.jsf");
        
        //item2.setOnclick("triggerHiddenEvent('crearProyecto','#{crearProyecto.abrirPanel}'); return false;");
        item2.setUrl("#");
        item4.setUrl("principalBiblioteca.jsf");
        //item6.setUrl("facturas.jsf");
        
        item5.setIcon("ui-icon-home");
        
        item2.setIcon("ui-icon-person");
        //item3.setIcon("ui-icon-search");
        item4.setIcon("ui-icon-note");
        //item6.setIcon("ui-icon-cart");
        
        //item6.setDisabled(true);
        item2.setDisabled(true);
        
        smUsuario.addElement(item5);
        
        smUsuario.addElement(item2);
        //smUsuario.addElement(item3);
        smUsuario.addElement(item4);
        //smUsuario.addElement(item6);
        
    }
        
    private void opcionesUsuario(DefaultSubMenu smUsuario){
        DefaultMenuItem item3 = new DefaultMenuItem("Actualizar Datos del Usuario");
        DefaultMenuItem item1 = new DefaultMenuItem("Bienvenida");
        DefaultMenuItem item2 = new DefaultMenuItem("Cerrar Sesion");
        
        item1.setUrl("bienvenida.jsf");
        item2.setOnclick("triggerHiddenEvent('btcerrarSesion');");
        item3.setUrl("actMisDatos.jsf"); 
        item3.setIcon("ui-icon-contact");
        item1.setIcon("ui-icon-home");
        item2.setIcon("ui-icon-extlink");
        
        smUsuario.addElement(item3);
        smUsuario.addElement(item1);
        smUsuario.addElement(item2);
        
    }
    
    @PostConstruct
    public void init(){
    
        usuarioInfo = Utilitarios.obtenerUsuario();
        PreferenciasView objPreferenciasView = new PreferenciasView();
         objPreferenciasView.getTheme();
         objPreferenciasView.setTheme("humanity");
         objPreferenciasView.changeTheme();
        
    }
    
}
