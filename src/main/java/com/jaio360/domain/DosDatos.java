package com.jaio360.domain;

import java.io.Serializable;

public class DosDatos implements Serializable{
    
    private String strDato1;
    private String strDato2;

    public DosDatos(String a, String b){
    
        strDato1 = a;
        strDato2 = b;
        
    }
            
    public String getStrDato1() {
        return strDato1;
    }

    public void setStrDato1(String strDato1) {
        this.strDato1 = strDato1;
    }

    public String getStrDato2() {
        return strDato2;
    }

    public void setStrDato2(String strDato2) {
        this.strDato2 = strDato2;
    }

}