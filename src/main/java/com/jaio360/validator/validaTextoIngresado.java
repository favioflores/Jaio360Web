package com.jaio360.validator;

import com.jaio360.utils.Utilitarios;
import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
 
@FacesValidator("validaTextoIngresado")
public class validaTextoIngresado implements Validator {
 
    private Pattern pattern;
  
    private static final String PATTERN = "^[a-zA-Z0-9\\s-ñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ()¿?´,.()]+$";
  
    public validaTextoIngresado() {
        pattern = Pattern.compile(PATTERN);
    }
 
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(Utilitarios.esNuloOVacio(value)){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, component.getAttributes().get("label") + ": " + "No debe ser vacio", null));
        }
         
        if(!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, component.getAttributes().get("label") + ": " + "Tiene digitos no permitidos", null));
        }
    }
    
    public String validar(String strTexto){
        if(Utilitarios.esNuloOVacio(strTexto)){
            return "vacio";
        }
        if(!pattern.matcher(strTexto).matches()) {
            return "que contiene digitos no permitidos";
        }
        return null;
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "validaTextoIngresado";
    }
     
}