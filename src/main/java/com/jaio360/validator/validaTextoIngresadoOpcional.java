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
 
@FacesValidator("validaTextoIngresadoOpcional")
public class validaTextoIngresadoOpcional implements Validator {
 
    private Pattern pattern;
  
    private static final String PATTERN = "^[a-zA-Z0-9\\s-ñÑáéíóúÁÉÍÓÚäëïöüÄËÏÖÜ()¿?,.()]+$";
  
    public validaTextoIngresadoOpcional() {
        pattern = Pattern.compile(PATTERN);
    }
 
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(Utilitarios.noEsNuloOVacio(value)){
            if(!pattern.matcher(value.toString()).matches()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "Tiene digitos no permitidos"));
            }
        }
    }
 
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    public String getValidatorId() {
        return "validaTextoIngresado";
    }
     
}