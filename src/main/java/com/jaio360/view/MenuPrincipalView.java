package com.jaio360.view;

import com.jaio360.dao.ElementoDAO;
import com.jaio360.domain.MenuBean;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@ManagedBean(name = "menuPrincipal")
@ViewScoped
public class MenuPrincipalView extends BaseView implements Serializable {

    private static Logger log = Logger.getLogger(MenuPrincipalView.class);

    private MenuModel menuPrincipal = new DefaultMenuModel();
    private MenuBean objMenuBean;
    private UsuarioInfo usuarioInfo;
    private ProyectoInfo proyectoInfo;
    private Boolean existProyecto;
    private ElementoDAO objElementoDAO;
    private String strMailHelp;

    public MenuBean getObjMenuBean() {
        return objMenuBean;
    }

    public void setObjMenuBean(MenuBean objMenuBean) {
        this.objMenuBean = objMenuBean;
    }

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

    public Boolean getExistProyecto() {
        return existProyecto;
    }

    public void setExistProyecto(Boolean existProyecto) {
        this.existProyecto = existProyecto;
    }

    public ElementoDAO getObjElementoDAO() {
        return objElementoDAO;
    }

    public void setObjElementoDAO(ElementoDAO objElementoDAO) {
        this.objElementoDAO = objElementoDAO;
    }

    public String getStrMailHelp() {
        return strMailHelp;
    }

    public void setStrMailHelp(String strMailHelp) {
        this.strMailHelp = strMailHelp;
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

    private MenuModel agregarItem(String strNombre, String strURL, String strIcon, MenuModel defaultMenuModel, String strActionListener) {
        try {

            DefaultMenuItem item = new DefaultMenuItem();

            if (Utilitarios.noEsNuloOVacio(strNombre)) {
                item.setValue(strNombre);
            }

            if (Utilitarios.noEsNuloOVacio(strIcon)) {
                item.setIcon(strIcon);
            }

            if (Utilitarios.noEsNuloOVacio(strURL)) {
                item.setUrl(strURL);
            }

            if (Utilitarios.noEsNuloOVacio(strActionListener)) {
                item.setCommand(strActionListener);
            }

            defaultMenuModel.getElements().add(item);

            return defaultMenuModel;

        } catch (Exception e) {
            return null;
        }
    }

    public MenuPrincipalView() {

        objMenuBean = new MenuBean();

        menuPrincipal = new DefaultMenuModel();

        UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
        UsuarioInfo objUsuarioInfoProxy = Utilitarios.obtenerUsuarioProxy();

        if (objUsuarioInfoProxy != null) {
            objUsuarioInfo = objUsuarioInfoProxy;
            agregarItem(msg("menu.close.proxy.mode"), null, "pi pi-fw pi-step-backward-alt", menuPrincipal, "#{MenuPrincipal.leftModeProxy}");
            List<MenuBean> lstMenuBean = new ArrayList<>();
            lstMenuBean.add(new MenuBean(null, "pi pi-fw pi-step-backward-alt", msg("menu.close.proxy.mode"), true, null, "#{menuPrincipal.leftModeProxy()}"));
            objMenuBean.setObjLstMenuBean(lstMenuBean);
        } else {

            if (objUsuarioInfo.getManagingDirector()) {//MANAGING DIRECTOR
                //Home
                //DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", MenuPrincipal);
                //agregarItem(msg("ir.a.bienvenida"), "welcome.jsf", "", home);
                agregarItem("", "welcome.jsf", "pi pi-fw pi-home", menuPrincipal, null);

                List<MenuBean> lstMenuBean = new ArrayList<>();
                lstMenuBean.add(new MenuBean("welcome.jsf", "pi pi-fw pi-home", msg("principal"), true, null, null));

                //Proyectos
                DefaultSubMenu proyectos = agregarMenu(msg("projects"), "pi pi-fw pi-briefcase", menuPrincipal);
                //agregarItem(msg("administrar.proyectos"), "admProyectos.jsf", "", proyectos);
                MenuBean objMenuProyectos = new MenuBean(null, "pi pi-fw pi-briefcase", msg("projects"), false, new ArrayList<>(), null);
                objMenuProyectos.getObjLstMenuBean().add(new MenuBean("admProyectos.jsf", null, msg("administrar.proyectos"), true, null, null));
                lstMenuBean.add(objMenuProyectos);

                //Datos de usuarios
                DefaultSubMenu usuarios = agregarMenu(msg("my.account"), "pi pi-user", menuPrincipal);
                agregarItem(msg("actualizar.mis.datos"), "admProfile.jsf", "", usuarios);
                agregarItem(msg("actualizar.todos.usuarios"), "admAllUsers.jsf", "", usuarios);
                MenuBean objMenuUsuarios = new MenuBean(null, "pi pi-users", msg("my.account"), false, new ArrayList<>(), null);
                objMenuUsuarios.getObjLstMenuBean().add(new MenuBean("admProfile.jsf", null, msg("actualizar.mis.datos"), true, null, null));
                objMenuUsuarios.getObjLstMenuBean().add(new MenuBean("admAllUsers.jsf", null, msg("actualizar.todos.usuarios"), true, null, null));
                lstMenuBean.add(objMenuUsuarios);
                //agregarItem(msg("balance.license"), "admBalanceClient.jsf", "", usuarios);

                //DefaultSubMenu clients = agregarMenu(msg("clients"), "pi pi-users", menuPrincipal);
                //agregarItem(msg("manage.my.clients"), "admClients.jsf", "", clients);
                //agregarItem(msg("gestionar.licencias"), "admLicencias.jsf", "", clients);
                //agregarItem(msg("manage.licences.clients"), "admLicenceClient.jsf", "", clients);
                //User guide
                //agregarItem(msg("user.guide"), "guidesForUsers.jsf", "pi pi-file-pdf", menuPrincipal, null);
                //Upgrades
                //agregarItem("", "upgrades.jsf", "pi pi-info-circle", menuPrincipal, null);
                objMenuBean.setObjLstMenuBean(lstMenuBean);

            } else if (objUsuarioInfo.getCountryManager()) {//COUNTRY MANAGER

                //Home
                //DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", menuPrincipal);
                //agregarItem(msg("ir.a.bienvenida"), "welcome.jsf", "", home);
                agregarItem("", "welcome.jsf", "pi pi-fw pi-home", menuPrincipal, null);
                List<MenuBean> lstMenuBean = new ArrayList<>();
                lstMenuBean.add(new MenuBean("welcome.jsf", "pi pi-fw pi-home", msg("principal"), true, null, null));

                //Proyectos
                //DefaultSubMenu proyectos = agregarMenu(msg("projects"), "pi pi-fw pi-briefcase", menuPrincipal);
                //agregarItem(msg("administrar.proyectos"), "admProyectos.jsf", "", proyectos);
                //Datos de usuarios
                DefaultSubMenu usuarios = agregarMenu(msg("my.account"), "pi pi-user", menuPrincipal);
                agregarItem(msg("actualizar.mis.datos"), "admProfile.jsf", "", usuarios);
                //agregarItem(msg("actualizar.todos.usuarios"), "admAllUsers.jsf", "", usuarios);
                MenuBean objMenuUsuarios = new MenuBean(null, "pi pi-user", msg("my.account"), false, new ArrayList<>(), null);
                objMenuUsuarios.getObjLstMenuBean().add(new MenuBean("admProfile.jsf", null, msg("actualizar.mis.datos"), true, null, null));

                DefaultSubMenu clients = agregarMenu(msg("clients"), "pi pi-users", menuPrincipal);
                agregarItem(msg("manage.my.clients"), "admClients.jsf", "", clients);
                agregarItem(msg("manage.licences.clients"), "admLicenceClient.jsf", "", clients);
                MenuBean objMenuClientes = new MenuBean(null, "pi pi-users", msg("clients"), false, new ArrayList<>(), null);
                objMenuClientes.getObjLstMenuBean().add(new MenuBean("admClients.jsf", null, msg("manage.my.clients"), true, null, null));
                objMenuClientes.getObjLstMenuBean().add(new MenuBean("admLicenceClient.jsf", null, msg("manage.licences.clients"), true, null, null));
                lstMenuBean.add(objMenuClientes);

                //User guide
                agregarItem(msg("user.guide"), "guidesForUsers.jsf", "pi pi-file-pdf", menuPrincipal, null);
                lstMenuBean.add(new MenuBean("guidesForUsers.jsf", "pi pi-file-pdf", msg("user.guide"), true, null, null));
                //Upgrades
                //agregarItem("", "upgrades.jsf", "pi pi-info-circle", menuPrincipal, null);

                objMenuBean.setObjLstMenuBean(lstMenuBean);

            } else if (objUsuarioInfo.getProjectManager()) {//PROJECT MANAGER

                //Home
                //DefaultSubMenu home = agregarItem("", "pi pi-fw pi-home", menuPrincipal);
                agregarItem("", "welcome.jsf", "pi pi-fw pi-home", menuPrincipal, null);

                List<MenuBean> lstMenuBean = new ArrayList<>();
                lstMenuBean.add(new MenuBean("welcome.jsf", "pi pi-fw pi-home", msg("principal"), true, null, null));

                //Proyectos
                DefaultSubMenu proyectos = agregarMenu(msg("projects"), "pi pi-fw pi-briefcase", menuPrincipal);
                agregarItem(msg("administrar.proyectos"), "admProyectos.jsf", "", proyectos);
                MenuBean objMenuProyectos = new MenuBean(null, "pi pi-fw pi-briefcase", msg("projects"), false, new ArrayList<>(), null);
                objMenuProyectos.getObjLstMenuBean().add(new MenuBean("admProyectos.jsf", null, msg("administrar.proyectos"), true, null, null));
                lstMenuBean.add(objMenuProyectos);

                //Datos de usuarios
                DefaultSubMenu user = agregarMenu(msg("my.account"), "pi pi-users", menuPrincipal);
                agregarItem(msg("actualizar.mis.datos"), "admProfile.jsf", "", user);
                agregarItem(msg("balance.license"), "admBalanceClient.jsf", "", user);
                MenuBean objMenuUsuarios = new MenuBean(null, "pi pi-users", msg("my.account"), false, new ArrayList<>(), null);
                objMenuUsuarios.getObjLstMenuBean().add(new MenuBean("admProfile.jsf", null, msg("actualizar.mis.datos"), true, null, null));
                objMenuUsuarios.getObjLstMenuBean().add(new MenuBean("admBalanceClient.jsf", null, msg("balance.license"), true, null, null));
                lstMenuBean.add(objMenuUsuarios);

                //User guide
                agregarItem(msg("user.guide"), "guidesForUsers.jsf", "pi pi-file-pdf", menuPrincipal, null);
                lstMenuBean.add(new MenuBean("guidesForUsers.jsf", "pi pi-file-pdf", msg("user.guide"), true, null, null));
                //Upgrades
                //agregarItem("", "upgrades.jsf", "pi pi-cloud-upload", menuPrincipal, null);

                objMenuBean.setObjLstMenuBean(lstMenuBean);

            } else {//USER EVALUATOR / EVALUATED
                //Home
                //DefaultSubMenu home = agregarMenu("", "pi pi-fw pi-home", menuPrincipal);
                //agregarItem(msg("ir.a.bienvenida"), "welcome.jsf", "", home);
                agregarItem("", "welcome.jsf", "pi pi-fw pi-home", menuPrincipal, null);
                List<MenuBean> lstMenuBean = new ArrayList<>();
                lstMenuBean.add(new MenuBean("welcome.jsf", "pi pi-fw pi-home", msg("principal"), true, null, null));

                DefaultSubMenu usuarios = agregarMenu(msg("my.account"), "pi pi-user", menuPrincipal);
                agregarItem(msg("actualizar.mis.datos"), "admProfile.jsf", "", usuarios);
                MenuBean objMenuUsuarios = new MenuBean(null, "pi pi-user", msg("my.account"), false, new ArrayList<>(), null);
                objMenuUsuarios.getObjLstMenuBean().add(new MenuBean("admProfile.jsf", null, msg("actualizar.mis.datos"), true, null, null));
                //Participant guide
                //agregarItem(msg("user.guide"), "guidesForUsers.jsf", "pi pi-file-pdf", menuPrincipal, null);
                //Upgrades
                //agregarItem("", "upgrades.jsf", "pi pi-cloud-upload", menuPrincipal);

                objMenuBean.setObjLstMenuBean(lstMenuBean);
            }
        }
    }

    @PostConstruct
    public void init() {

        this.usuarioInfo = Utilitarios.obtenerUsuario();
        this.proyectoInfo = Utilitarios.obtenerProyecto();
        this.existProyecto = this.proyectoInfo != null;
        this.strMailHelp = null;

    }

    public void leftModeProxy() {

        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            session.setAttribute("usuarioInfo", Utilitarios.obtenerUsuarioProxy());
            session.removeAttribute("usuarioInfoProxy");
            session.removeAttribute("proyectoInfo");

            FacesContext.getCurrentInstance().getExternalContext().redirect("admClients.jsf");
        } catch (IOException ex) {
            mostrarError(log, ex);
        }

    }
}
