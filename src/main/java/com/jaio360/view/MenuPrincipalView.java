package com.jaio360.view;

import com.jaio360.domain.UsuarioInfo;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.component.divider.Divider;
import org.primefaces.component.separator.UISeparator;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSeparator;
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
        
        DefaultMenuItem item = DefaultMenuItem.builder()
                    .value("Home")
                    .icon("pi pi-fw pi-home")
                    .url("home.jsf")
                    //.command("#{menuView.save}")
                    //.update("messages")
                    .build();
        
        
        model.getElements().add(item);
        
       
                
        if(objUsuarioInfo.isBoEsAdministrador()){

            DefaultSubMenu smUsuarioProyectos = DefaultSubMenu.builder().label("Proyectos").icon("pi pi-fw pi-file").build();
            opcionesUsuarioProyectos(smUsuarioProyectos);
            model.getElements().add(smUsuarioProyectos);
            
            DefaultSubMenu smUsuarioBiblioteca = DefaultSubMenu.builder().label("Biblioteca").icon("pi pi-fw pi-file").build();
            opcionesUsuarioBiblioteca(smUsuarioBiblioteca);
            model.getElements().add(smUsuarioBiblioteca);
            
            DefaultSubMenu smAdministrador = DefaultSubMenu.builder().label("Administración").build();
            opcionesAdministrador(smAdministrador);
            model.getElements().add(smAdministrador);
                        
            DefaultMenuItem item3 = DefaultMenuItem.builder()
                                .value("Actualizar datos")
                                .icon("pi pi-fw pi-user-edit")
                                .url("actMisDatos.jsf")
                                //.command("#{menuView.save}")
                                //.update("messages")
                                .build();

            model.getElements().add(item3);

        }else if(objUsuarioInfo.isBoEsUsuarioMaestro()){
            
            DefaultMenuItem smUsuarioProyectos = DefaultMenuItem.builder()
                                            .value("Administrar Proyectos")
                                            .icon("pi pi-fw pi-briefcase")
                                            .url("admProyectos.jsf")
                                            .build();
            
                    
            //opcionesUsuarioProyectos(smUsuarioProyectos);
            model.getElements().add(smUsuarioProyectos);
            /*
            DefaultSubMenu smUsuarioBiblioteca = DefaultSubMenu.builder().label("Biblioteca").icon("pi pi-fw pi-file").build();
            opcionesUsuarioBiblioteca(smUsuarioBiblioteca);
            model.getElements().add(smUsuarioBiblioteca);
            */
            
            DefaultMenuItem item3 = DefaultMenuItem.builder()
                                .value("Actualizar datos")
                                .icon("pi pi-fw pi-user-edit")
                                .url("actMisDatos.jsf")
                                //.command("#{menuView.save}")
                                //.update("messages")
                                .build();

            model.getElements().add(item3);
        
        }else{
            
            DefaultMenuItem item3 = DefaultMenuItem.builder()
                                .value("Actualizar datos")
                                .icon("pi pi-fw pi-user-edit")
                                .url("actMisDatos.jsf")
                                //.command("#{menuView.save}")
                                //.update("messages")
                                .build();

            model.getElements().add(item3);
            
        }
        
    }
    
    public MenuModel getModel() {
        return model;
    }   
    
    private void opcionesAdministrador(DefaultSubMenu smAdministrador){
        /*
        DefaultMenuItem item2 = DefaultMenuItem.builder()
                                .value("Mantenimiento de Tarifas")
                                .icon("ui-icon-calculator")
                                //.ajax(false)
                                .url("principalMantenimientoTarifas.jsf")
                                //.command("#{menuView.save}")
                                //.update("messages")
                                .build();
        */
        DefaultMenuItem item3 = DefaultMenuItem.builder()
                                .value("Mantenimiento de Cuentas")
                                .icon("pi pi-fw pi-folder")
                                //.ajax(false)
                                .url("principalMantenimientoCuenta.jsf")
                                //.command("#{menuView.save}")
                                //.update("messages")
                                .build();
        
        //smAdministrador.getElements().add(item2);
        smAdministrador.getElements().add(item3);
        
    }
    
        private void opcionesUsuarioBiblioteca(DefaultSubMenu smUsuario){

        DefaultMenuItem item4 = DefaultMenuItem.builder()
                                .value("Preguntas")
                                .icon("pi pi-fw pi-folder")
                                //.ajax(false)
                                .url("principalBiblioteca.jsf")
                                //.command("#{menuView.save}")
                                //.update("messages")
                                .build();

        smUsuario.getElements().add(item4);       
        
    }
        
    private void opcionesUsuarioProyectos(DefaultSubMenu smUsuario){

        /*
        DefaultMenuItem item6 = DefaultMenuItem.builder()
                                .value("Nuevo proyecto")
                                .icon("pi pi-fw pi-plus")
                                //.ajax(false)
                                //.url("principalBiblioteca.jsf")
                                .url("crearProyecto.jsf")
                                //.update("messages")
                                .build();
        
        DefaultMenuItem item5 = DefaultMenuItem.builder()
                                .value("Buscar proyecto")
                                .icon("pi pi-fw pi-search")
                                //.ajax(false)
                                .url("principal.jsf")
                                //.command("#{menuView.save}")
                                //.update("messages")
                                .build();
        
        
        smUsuario.getElements().add(item6);
        smUsuario.getElements().add(item5);      
        */
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
