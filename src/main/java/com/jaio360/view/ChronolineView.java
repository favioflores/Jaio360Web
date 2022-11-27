/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jaio360.view;

import com.jaio360.dao.LastUpdateDAO;
import com.jaio360.domain.LastUpdateBean;
import com.jaio360.orm.LastUpdate;
import com.jaio360.utils.Utilitarios;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author favio.flores
 */
@Named
@RequestScoped
public class ChronolineView {

    private List<LastUpdateBean> lstLastUpdateBeans;

    public List<LastUpdateBean> getLstLastUpdateBeans() {
        return lstLastUpdateBeans;
    }

    public void setLstLastUpdateBeans(List<LastUpdateBean> lstLastUpdateBeans) {
        this.lstLastUpdateBeans = lstLastUpdateBeans;
    }
    
    @PostConstruct
    public void init() {

        lstLastUpdateBeans = new ArrayList<>();

        LastUpdateDAO objLastUpdateDAO = new LastUpdateDAO();

        List<LastUpdate> lstLastUpdates = objLastUpdateDAO.obtenListaLastUpdate();

        int i = 0;
        LastUpdateBean objLastUpdateBean;

        for (LastUpdate objLastUpdate : lstLastUpdates) {
            String color = Utilitarios.generaColorHtmlPreferencial(i);

            objLastUpdateBean = new LastUpdateBean();
            objLastUpdateBean.setLuDtFecha(objLastUpdate.getLuDtFecha());
            objLastUpdateBean.setLuTxDescripcion(objLastUpdate.getLuTxDescripcion());
            objLastUpdateBean.setLuTxTipo(objLastUpdate.getLuTxTipo());
            objLastUpdateBean.setStrColor(color);

            if (objLastUpdate.getLuTxTipo().equals("NEW")) {
                objLastUpdateBean.setStrIcon("pi pi-star");
            } else if (objLastUpdate.getLuTxTipo().equals("UPGRADE")) {
                objLastUpdateBean.setStrIcon("pi pi-cog");
            } else if (objLastUpdate.getLuTxTipo().equals("BUGFIX")) {
                objLastUpdateBean.setStrIcon("pi pi-check");
            }

            lstLastUpdateBeans.add(objLastUpdateBean);

            if (i == 10) {
                break;
            }
            
            i++;

        }
    }
}
