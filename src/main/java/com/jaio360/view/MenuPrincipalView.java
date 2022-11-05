package com.jaio360.view;

import com.jaio360.domain.ProyectoInfo;
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
public class MenuPrincipalView extends BaseView implements Serializable {

    private MenuModel menuPrincipal;
    private UsuarioInfo usuarioInfo;
    private ProyectoInfo proyectoInfo;
    private boolean existProyecto;

    public MenuModel getMenuPrincipal() {
        return menuPrincipal;
    }

    public void setMenuPrincipal(MenuModel menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
    }

    public UsuarioInfo getUsuarioInfo() {
        return usuarioInfo;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public ProyectoInfo getProyectoInfo() {
        return proyectoInfo;
    }

    public void setProyectoInfo(ProyectoInfo proyectoInfo) {
        this.proyectoInfo = proyectoInfo;
    }

    public boolean isExistProyecto() {
        return existProyecto;
    }

    public void setExistProyecto(boolean existProyecto) {
        this.existProyecto = existProyecto;
    }

    

    private DefaultSubMenu agregarMenu(String strNombre, String strIcon, MenuModel subMenuPrincipal) {
        try {

            DefaultSubMenu subMenu = DefaultSubMenu.builder().icon(strIcon).label(strNombre).build();

            subMenuPrincipal.getElements().add(subMenu);
            
            return subMenu;

        } catch (Exception e) {
            return null;
        }
    }
    
    private DefaultSubMenu agregarItem(String strNombre, String strURL, String strIcon, DefaultSubMenu subMenuPrincipal) {
        try {

            DefaultMenuItem item = DefaultMenuItem.builder().value(strNombre).icon(strIcon).url(strURL).build();
            subMenuPrincipal.getElements().add(item);
            return subMenuPrincipal;

        } catch (Exception e) {
            return null;
        }
    }

    public MenuPrincipalView() {

        menuPrincipal = new DefaultMenuModel();

        UsuarioSesion objUsuarioSesion = new UsuarioSesion();

        UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();

        if (objUsuarioInfo.isBoEsAdministrador()) {
            //Home
            DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", menuPrincipal);
            agregarItem(msg("ir.a.principal"), "home.jsf", "", home);
            agregarItem(msg("ir.a.bienvenida"), "bienvenida.jsf", "", home);
            //Proyectos
            DefaultSubMenu proyectos = agregarMenu(msg("projects"), "pi pi-fw pi-briefcase", menuPrincipal);
            agregarItem(msg("administrar.proyectos"), "admProyectos.jsf", "", proyectos);
            //Datos de usuarios
            DefaultSubMenu usuarios = agregarMenu(msg("users"), "pi pi-users", menuPrincipal);
            agregarItem(msg("actualizar.mis.datos"), "actMisDatos.jsf", "", usuarios);
            agregarItem(msg("actualizar.usuarios"), "mantenimientoCuenta.jsf", "", usuarios);
            agregarItem(msg("gestionar.licencias"), "mantenimientoLicencia.jsf", "", usuarios);
     
        }else if(objUsuarioInfo.isBoEsUsuarioMaestro()){
            //Home
            DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", menuPrincipal);
            agregarItem(msg("ir.a.principal"), "home.jsf", "", home);
            agregarItem(msg("ir.a.bienvenida"), "bienvenida.jsf", "", home);
            //Proyectos
            DefaultSubMenu proyectos = agregarMenu(msg("projects"), "pi pi-fw pi-briefcase", menuPrincipal);
            agregarItem(msg("administrar.proyectos"), "admProyectos.jsf", "", proyectos);
            //Datos de usuarios
            DefaultSubMenu usuarios = agregarMenu(msg("users"), "pi pi-users", menuPrincipal);
            agregarItem(msg("actualizar.mis.datos"), "actMisDatos.jsf", "", usuarios);
        }else{
            //Home
            DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", menuPrincipal);
            agregarItem(msg("ir.a.bienvenida"), "bienvenida.jsf", "", home);
            DefaultSubMenu usuarios = agregarMenu(msg("users"), "pi pi-users", menuPrincipal);
            agregarItem(msg("actualizar.mis.datos"), "actMisDatos.jsf", "", usuarios);
        }
        
    }


    @PostConstruct
    public void init() {

        usuarioInfo = Utilitarios.obtenerUsuario();
        proyectoInfo = Utilitarios.obtenerProyecto();

        if (proyectoInfo != null) {
            existProyecto = true;
        } else {
            existProyecto = false;
        }

    }

}
