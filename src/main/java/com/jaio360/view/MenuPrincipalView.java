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

    private MenuModel agregarItem(String strNombre, String strURL, String strIcon, MenuModel defaultMenuModel) {
        try {

            DefaultMenuItem item = DefaultMenuItem.builder().value(strNombre).icon(strIcon).url(strURL).build();
            defaultMenuModel.getElements().add(item);
            return defaultMenuModel;

        } catch (Exception e) {
            return null;
        }
    }

    public MenuPrincipalView() {

        menuPrincipal = new DefaultMenuModel();

        UsuarioSesion objUsuarioSesion = new UsuarioSesion();

        UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();

        if (objUsuarioInfo.isBoEsAdministrador()) {//MANAGING DIRECTOR
            //Home
            //DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", menuPrincipal);
            //agregarItem(msg("ir.a.bienvenida"), "welcome.jsf", "", home);
            agregarItem("", "welcome.jsf", "pi pi-fw pi-home", menuPrincipal);
            //Proyectos
            DefaultSubMenu proyectos = agregarMenu(msg("projects"), "pi pi-fw pi-briefcase", menuPrincipal);
            agregarItem(msg("administrar.proyectos"), "admProyectos.jsf", "", proyectos);
            //Datos de usuarios
            DefaultSubMenu usuarios = agregarMenu(msg("users"), "pi pi-users", menuPrincipal);
            agregarItem(msg("actualizar.mis.datos"), "admProfile.jsf", "", usuarios);
            agregarItem(msg("actualizar.todos.usuarios"), "admAllUsers.jsf", "", usuarios);
            agregarItem(msg("gestionar.licencias"), "admLicencias.jsf", "", usuarios);
            //User guide
            agregarItem(msg("user.guide"), "userGuide.jsf", "pi pi-file-pdf", menuPrincipal);
            agregarItem(msg("participant.guide"), "participantGuide.jsf", "pi pi-file-pdf", menuPrincipal);
            //Upgrades
            agregarItem("", "upgrades.jsf", "pi pi-info-circle", menuPrincipal);
            
        } else if (false) {//COUNTRY MANAGER
            
        } else if (objUsuarioInfo.isBoEsUsuarioMaestro()) {//PROJECT MANAGER
            //Home
            //DefaultSubMenu home = agregarItem("", "pi pi-fw pi-home", menuPrincipal);
            agregarItem("", "welcome.jsf", "pi pi-fw pi-home", menuPrincipal);
            //Proyectos
            DefaultSubMenu proyectos = agregarMenu(msg("projects"), "pi pi-fw pi-briefcase", menuPrincipal);
            agregarItem(msg("administrar.proyectos"), "admProyectos.jsf", "", proyectos);
            //Datos de usuarios
            DefaultSubMenu usuarios = agregarMenu(msg("users"), "pi pi-users", menuPrincipal);
            agregarItem(msg("actualizar.mis.datos"), "admProfile.jsf", "", usuarios);
            //User guide
            agregarItem(msg("user.guide"), "userGuide.jsf", "pi pi-file-pdf", menuPrincipal);
            agregarItem(msg("participant.guide"), "participantGuide.jsf", "pi pi-file-pdf", menuPrincipal);
            //Upgrades
            agregarItem("", "upgrades.jsf", "pi pi-cloud-upload", menuPrincipal);
        } else {//USER EVALUATOR / EVALUATED
            //Home
            //DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", menuPrincipal);
            //agregarItem(msg("ir.a.bienvenida"), "welcome.jsf", "", home);
            agregarItem("", "welcome.jsf", "pi pi-fw pi-home", menuPrincipal);
            DefaultSubMenu usuarios = agregarMenu(msg("users"), "pi pi-users", menuPrincipal);
            agregarItem(msg("actualizar.mis.datos"), "admProfile.jsf", "", usuarios);
            //Participant guide
            agregarItem(msg("participant.guide"), "participantGuide.jsf", "pi pi-file-pdf", menuPrincipal);
            //Upgrades
            //agregarItem("", "upgrades.jsf", "pi pi-cloud-upload", menuPrincipal);
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
