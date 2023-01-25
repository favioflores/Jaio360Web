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

@FacesValidator("validaURLNotRequired")
public class validaURLNotRequired implements Validator {

    private Pattern pattern;

    public validaURLNotRequired() {
        pattern = Pattern.compile(Constantes.URL_PATTERN);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (Utilitarios.noEsNuloOVacio(value)) {

            if (!pattern.matcher(value.toString().trim()).matches()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un URL válido", null));
            }

        }

    }

    public Map<String, Object> getMetadata() {
        return null;
    }

    public String getValidatorId() {
        return "validarCorreo";
    }

}
