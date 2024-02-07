package com.jaio360.domain;

import java.io.Serializable;
import java.util.List;

public class MenuBean implements Serializable {

    private String url;
    private String icon;
    private String value;
    private Boolean blMenuItem;
    private String actionListener;
    private List<MenuBean> objLstMenuBean;

    public MenuBean(String url, String icon, String value, Boolean blMenuItem, List<MenuBean> objLstMenuBean, String actionListener) {
        this.url = url;
        this.icon = icon;
        this.value = value;
        this.blMenuItem = blMenuItem;
        this.objLstMenuBean = objLstMenuBean;
        this.actionListener = actionListener;
    }

    public MenuBean() {
    }

    public String getActionListener() {
        return actionListener;
    }

    public void setActionListener(String actionListener) {
        this.actionListener = actionListener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getBlMenuItem() {
        return blMenuItem;
    }

    public void setBlMenuItem(Boolean blMenuItem) {
        this.blMenuItem = blMenuItem;
    }

    public List<MenuBean> getObjLstMenuBean() {
        return objLstMenuBean;
    }

    public void setObjLstMenuBean(List<MenuBean> objLstMenuBean) {
        this.objLstMenuBean = objLstMenuBean;
    }

}
