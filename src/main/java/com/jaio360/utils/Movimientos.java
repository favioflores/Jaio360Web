package com.jaio360.utils;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "movimientos")
@ApplicationScoped
public class Movimientos {

    public static Integer MOV_COMPRA_LICENCIA_INDIVIDUAL = 1001;
    public static Integer MOV_COMPRA_LICENCIA_MASIVA = 1002;
    public static Integer MOV_RESERVA_LICENCIA_INDIVIDUAL = 1003;
    public static Integer MOV_RESERVA_LICENCIA_MASIVA = 1004;
    public static Integer MOV_EJECUTA_LICENCIA_INDIVIDUAL = 1005;
    public static Integer MOV_EJECUTA_LICENCIA_MASIVA = 1006;
    public static Integer MOV_LIBERAR_LICENCIA_INDIVIDUAL = 1007;
    public static Integer MOV_LIBERAR_LICENCIA_MASIVA = 1008;

}
