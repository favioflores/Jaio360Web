package com.jaio360.validator;

import com.jaio360.utils.Utilitarios;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
 
@FacesValidator("validarNro")
public class validaNro implements Validator {
 
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(Utilitarios.esNuloOVacio(value)){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "No debe ser vacio"));
        }
        
        if(!Utilitarios.isNumber(value.toString(), false)){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "Debe ser numerico"));
        }
        
        Integer a = Integer.parseInt(value.toString());
        
        if(a<=0){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "No puede ser menor a cero"));
        }
        
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "validarNro";
    }
     
}