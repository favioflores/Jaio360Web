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
 
@FacesValidator("validarRango")
public class validaRango implements Validator, ClientValidator {
 
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        
        if(Utilitarios.esNuloOVacio(value)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "El campo de rangos se encuentr vacio."));
        }
        
        String strTempRangos[] = value.toString().split(",");
        
        if(Utilitarios.esNuloOVacio(strTempRangos)){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "Los rangos no estan definidos correctamente."));
        }
        
        if(strTempRangos.length<=1){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "Se debe definir al menos dos intervalos."));
        }
        
        int i = 0;
        
        String temp;

        Integer anterior = 0;
        
        while(i<=strTempRangos.length - 1){
        
            temp = strTempRangos[i];
            /*
            if(temp.length!=2){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "Los rangos no estan definidos correctamente."));
            }
            */
            
            if(Utilitarios.esNuloOVacio(temp)){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "Los rangos no estan definidos correctamente."));
            }
            
            if(Utilitarios.isNumber(temp,false)!=true){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "Los rangos no estan definidos correctamente."));
            }
            
            Integer a = Integer.parseInt(temp);
            /*
            if(a>=b){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "Los rangos no estan definidos correctamente."));
            }*/
            
            if(a<anterior){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validar rango", "Los rangos no estan definidos correctamente."));
            }
            
            anterior = a;
            
            i++;
            
        }
        
        
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }
 
    @Override
    public String getValidatorId() {
        return "validarRango";
    }
     
}