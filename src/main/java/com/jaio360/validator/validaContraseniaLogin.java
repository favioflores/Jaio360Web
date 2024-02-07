package com.jaio360.validator;

import com.jaio360.utils.Utilitarios;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
 
@FacesValidator("validaContraseniaLogin")
public class validaContraseniaLogin implements Validator {
 
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(Utilitarios.esNuloOVacio(value)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña es requerida",null));
        }
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "validaContraseniaLogin";
    }
     
}