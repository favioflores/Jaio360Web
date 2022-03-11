package com.jaio360.validator;

import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;
 
@FacesValidator("validaNombreCompleto")
public class validaNombreCompleto implements Validator, ClientValidator  {
 
    private Pattern pattern;
  
    private static final String EMAIL_PATTERN = "^[a-zA-Z\\s]+$";
  
    public validaNombreCompleto() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }
 
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value == null) {
            return;
        }
         
        if(!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "Tiene digitos no permitidos"));
        }
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getValidatorId() {
        return "custom.validaNombreCompleto";
    }
     
}