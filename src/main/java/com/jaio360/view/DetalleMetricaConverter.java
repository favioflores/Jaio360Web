package com.jaio360.view;
// Generated 07/09/2014 10:02:38 PM by Hibernate Tools 3.2.1.GA


import com.jaio360.orm.DetalleMetrica;
import javax.faces.application.FacesMessage;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.orderlist.OrderList;
import org.primefaces.model.DualListModel;
 


/**
 * DetalleMetrica generated by hbm2java
 */
@FacesConverter("detalleMetricaConverter")
public class DetalleMetricaConverter  implements Converter {


    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                //ThemeService service = (ThemeService) fc.getExternalContext().getApplicationMap().get("themeService");
                //return service.getThemes().get(Integer.parseInt(value));
                final DualListModel<DetalleMetrica> dualList;
                //dualList = (DualListModel<DetalleMetrica>) ((OrderList)uic).getValue();
                OrderList obj = ((OrderList)uic);
                System.out.println(obj.getValue());
                //dualList = (DualListModel<DetalleMetrica>) ((OrderList)uic).getValue();
                List<DetalleMetrica> lst = (List<DetalleMetrica>) obj.getValue();
                 DetalleMetrica team = getObjectFromList(lst,Integer.valueOf(value));
             
            return team;
     
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }
 
    private DetalleMetrica getObjectFromList(List<DetalleMetrica> list, Integer identifier) {
        for(DetalleMetrica objDetalleMetrica : list){
            if(objDetalleMetrica.getDeIdDetalleEscalaPk().equals(identifier)){
                return objDetalleMetrica;
                 }
        }
        return null;
    }
    
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
         String string;
        
        if(object == null){
            string="";
        }else{
            try{
                string = String.valueOf(((DetalleMetrica)object).getDeIdDetalleEscalaPk());
            }catch(ClassCastException cce){
                throw new ConverterException();
            }
        }
        return string;
    }   

}


