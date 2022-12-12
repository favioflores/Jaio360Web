package com.jaio360.validator;

import com.jaio360.utils.Utilitarios;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;
 
@FacesValidator("validarSex")
public class validaSex implements Validator, ClientValidator {
 
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        if(Utilitarios.esNuloOVacio(value)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "El campo de géneros se encuentra vacio.", null));
        }
        
        String strSex[] = value.toString().split(",");
        
        if(Utilitarios.esNuloOVacio(strSex) || strSex.length == 0){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los géneros no estan definidos correctamente.", null));
        }
        
        int i = 0;
        
        String temp;
        
        while(i<=strSex.length - 1){
        
            temp = strSex[i];
            
            if(Utilitarios.esNuloOVacio(temp)){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Los géneros no estan definidos correctamente.",null));
            }
            
            i++;
            
        }
        
        
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    @Override
    public String getValidatorId() {
        return "validarGenero";
    }
     
}