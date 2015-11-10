package com.jaio360.validator;

import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
 
@FacesValidator("validarCorreo")
public class validaCorreo implements Validator {
 
    private Pattern pattern;
  

  
    public validaCorreo() {
        pattern = Pattern.compile(Constantes.EMAIL_PATTERN);
    }
 
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(Utilitarios.esNuloOVacio(value)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "El correo electronico es requerido"));
        }
         
        if(!pattern.matcher(value.toString().trim()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "No es un correo electronico"));
        }
    }
    
    public String valida(Object value) throws ValidatorException {
        if(Utilitarios.esNuloOVacio(value)) {
            return "vacio";
        }
         
        if(!pattern.matcher(value.toString().trim()).matches()) {
            return "no válido o con digitos especiales";
        }
        return null;
    }
    
    public String validate(Object value) throws ValidatorException {
        if(Utilitarios.esNuloOVacio(value)) {
            return "Esta vacio";
        }
         
        if(!pattern.matcher(value.toString().trim()).matches()) {
            return "No es un correo electronico";
        }
        return null;
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "validarCorreo";
    }
     
}