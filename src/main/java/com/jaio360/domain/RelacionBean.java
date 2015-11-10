/*
 * Copyright 2010 Prime Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaio360.domain;

import java.io.Serializable;

public class RelacionBean implements Serializable {

    private String strNombre;
    private String strAbreviatura;
    private String strDescripcion;
    private String strColor;
    private Integer idRelacionPk;
    private Integer intIdEstado;
    private String strEstado;
    private Integer intCantidadUso;

    public Integer getIntCantidadUso() {
        return intCantidadUso;
    }

    public void setIntCantidadUso(Integer intCantidadUso) {
        this.intCantidadUso = intCantidadUso;
    }
    
    public RelacionBean() {
    }

    public String getStrAbreviatura() {
        return strAbreviatura;
    }

    public void setStrAbreviatura(String strAbreviatura) {
        this.strAbreviatura = strAbreviatura;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrColor() {
        return strColor;
    }

    public void setStrColor(String strColor) {
        this.strColor = strColor;
    }

    public Integer getIdRelacionPk() {
        return idRelacionPk;
    }

    public void setIdRelacionPk(Integer idRelacionPk) {
        this.idRelacionPk = idRelacionPk;
    }   

    public Integer getIntIdEstado() {
        return intIdEstado;
    }

    public void setIntIdEstado(Integer intIdEstado) {
        this.intIdEstado = intIdEstado;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrEstado() {
        return strEstado;
    }

    public void setStrEstado(String strEstado) {
        this.strEstado = strEstado;
    }
 
    
}