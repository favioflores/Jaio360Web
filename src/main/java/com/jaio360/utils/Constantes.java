package com.jaio360.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.text.DateFormat;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "constantes")
@ApplicationScoped
public class Constantes {

    public static char CH_RANDOM_Z = 'Z';
    public static char CH_RANDOM_A = 'A';
    public static String UNDERLINE_COMMENT = "______________________________________________________________________________________________________________________________________________________________________________________________";
    public final static String STR_POINT = ".";
    public final static int INT_PARAMETER_ZERO_POS = 0;
    public final static int INT_PARAMETER_ONE_POS = 1;
    public static String ZERO = "0";
    public static Integer ZERO_INTEGER = 0;
    public String strApp = "Constantes";
    
    public static byte[] TRUE_BYTE = new byte[]{(byte) 1};
    public static byte[] FALSE_BYTE = new byte[]{(byte) 0};

    public static String STR_CSS = ".css";
    public static String STR_JPG = ".jpg";
    public static String STR_GIF = ".gif";
    public static String STR_PNG = ".png";
    public static String STR_JS  = ".js";
    public static String STR_HTM = ".htm";
    public static String STR_SWF = ".swf";
    
    public static Integer INT_LONGITUD_CLAVE_DEFECTO_ACTIVO = 10;
    public static Integer INT_LONGITUD_ARCHIVOS = 20;
    
    public static Integer INT_ESTADO_ELEMENTO_ACTIVO = 1;
    public static Integer INT_ET_ESTADO_NOTIFICACION_ENVIADO = 55;
    public static Integer INT_ET_ESTADO_NOTIFICACION_PENDIENTE = 56;
    public static Integer INT_ET_ESTADO_NOTIFICACION_ENVIANDO = 57;
    public static Integer INT_ET_SENDER_DOMINIO = 58;
    public static Integer INT_ET_SENDER_PUERTO_ENVIO = 59;
    public static Integer INT_ET_SENDER_PUERTO_RECEPCION = 60;
    public static Integer INT_ET_SENDER_USUARIO = 61;
    public static Integer INT_ET_SENDER_CONTRASEÑA = 62;
    
    public static Integer INT_ET_TIPO_USUARIO_ADMINISTRADOR = 3;
    public static Integer INT_ET_TIPO_USUARIO_USUARIO_MAESTRO = 4;
    public static Integer INT_ET_TIPO_USUARIO_USUARIO = 5;
    
    public static Integer INT_ET_ESTADO_USUARIO_REGISTRADO = 12;
    public static Integer INT_ET_ESTADO_USUARIO_CONFIRMADO = 13;
    public static Integer INT_ET_ESTADO_USUARIO_BLOQUEADO = 14;
    
    public static Integer INT_ET_ESTADO_CONTRATO_REGISTRADO = 19;
    public static Integer INT_ET_ESTADO_CONTRATO_CONFIRMADO = 20;
    public static Integer INT_ET_ESTADO_CONTRATO_EXPIRADO   = 21;
    public static Integer INT_ET_ESTADO_CONTRATO_BLOQUEADO  = 22;
    
    public static Integer INT_ET_ESTADO_SELECCION_REGISTRADO = 64;
    public static Integer INT_ET_ESTADO_SELECCION_EN_EJECUCION = 65;
    
    public static Integer INT_ET_ESTADO_RELACION_REGISTRADO = 66;
    public static Integer INT_ET_ESTADO_RELACION_EN_EJECUCION = 67;
    
    public static Integer INT_ET_ESTADO_EVALUADO_REGISTRADO = 68;
    public static Integer INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION = 72;
    public static Integer INT_ET_ESTADO_EVALUADO_EN_EJECUCION = 69;
    public static Integer INT_ET_ESTADO_EVALUADO_TERMINADO = 73;
    
    public static Integer INT_ET_TIPO_PARTICIPANTE_EVALUADOR = 38;
    public static Integer INT_ET_TIPO_PARTICIPANTE_EVALUADO = 39;

    public static Integer INT_ET_TIPO_COMPONENTE_CATEGORIA = 45;
    public static Integer INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA = 46;
    public static Integer INT_ET_TIPO_COMPONENTE_COMENTARIO = 48;
    public static Integer INT_ET_TIPO_COMPONENTE_PREGUNTA_ABIERTA = 47;
    
    public static Integer INT_ET_TIPO_UBIGEO_PAIS = 15;
    public static Integer INT_ET_TIPO_UBIGEO_DEPARTAMENTO = 16;
    
    public static Integer INT_ET_TIPO_METRICA_ESCALA = 50;
    public static Integer INT_ET_TIPO_METRICA_RANGO = 51;
    
    public static Integer INT_ET_ESTADO_EVALUADOR_REGISTRADO = 70;
    public static Integer INT_ET_ESTADO_EVALUADOR_EN_PARAMETRIZACION = 75;
    public static Integer INT_ET_ESTADO_EVALUADOR_EN_EJECUCION = 71;
    public static Integer INT_ET_ESTADO_EVALUADOR_EN_PAUSA = 75;
    public static Integer INT_ET_ESTADO_EVALUADOR_TERMINADO = 76;
    
    public static Integer INT_ET_ESTADO_RELACION_EDO_EDOR_REGISTRADO = 79;
    public static Integer INT_ET_ESTADO_RELACION_EDO_EDOR_TERMINADO = 80;
    
    public static Integer INT_DT_ESTADOS_TARIFA = 9;
    public static Integer INT_DT_METODOLOGIAS = 11;
    public static Integer INT_DT_ESTADOS_PROYECTO = 12;
    public static Integer INT_DT_TIPO_DOCUMENTO = 3;
    public static Integer INT_DT_ESTADO_USUARIO = 5;
    public static Integer INT_DT_TIPO_CUENTA = 2;
    public static Integer INT_DT_ROLES = 27;
    public static Integer INT_DT_AREAS = 28;
    
    public static Integer INT_ET_ESTADO_TARIFA_REGISTRADO = 25;
    public static Integer INT_ET_ESTADO_TARIFA_CONFIRMADO = 26;
    public static Integer INT_ET_ESTADO_TARIFA_EXPIRADO   = 27;
    
    public static Integer INT_ET_ESTADO_TIPO_PROYECTO_ESCALA = 30;
    public static Integer INT_ET_ESTADO_TIPO_PROYECTO_ELECCION = 31;
    
    public static Integer INT_ET_TIPO_TARIFA_INDIVIDUAL = 202;
    public static Integer INT_ET_TIPO_TARIFA_MASIVO = 203;
    
    public static Integer INT_ET_ESTADO_PROYECTO_REGISTRADO = 32;
    public static Integer INT_ET_ESTADO_PROYECTO_EN_PARAMETRIZACION = 33;
    public static Integer INT_ET_ESTADO_PROYECTO_EN_EJECUCION = 34;
    public static Integer INT_ET_ESTADO_PROYECTO_MODIFICADO = 35;
    public static Integer INT_ET_ESTADO_PROYECTO_PAUSADO = 36;
    public static Integer INT_ET_ESTADO_PROYECTO_TERMINADO = 37;
    public static Integer INT_ET_ESTADO_PROYECTO_ELIMINADO = 201;

    public static Integer INT_ET_ESTADO_CUESTIONARIO_REGISTRADO = 43;
    public static Integer INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO = 44;
    public static Integer INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION = 77;
    
    public static Integer INT_ET_ESTADO_CUES_EVA_REGISTRADO = 64;
    public static Integer INT_ET_ESTADO_CUES_EVA_EN_EJECUCION = 65;
    public static Integer INT_ET_ESTADO_CUES_EVA_TERMINADO = 65;
    
    public static Integer INT_ET_NOTIFICACION_CONVOCATORIA_RED = 81;
    public static Integer INT_ET_NOTIFICACION_CONVOCATORIA = 52;
    public static Integer INT_ET_NOTIFICACION_INSTRUCCIONES = 53;
    public static Integer INT_ET_NOTIFICACION_AGRADECIMIENTO = 54;
    
    public static String STR_ET_NOTIFICACION_AGRADECIMIENTO = "54";
    
    public static Integer INT_ET_NOTIFICACION_CLAVE = 63;
    public static Integer INT_ET_MODELO_NOTIFICACION_CARGA_RED = 74;
    public static Integer INT_ET_MODELO_NOTIFICACION_CONVOCATORIA = 78;

    public static Integer INT_ET_TIPO_PARAMETRO_SEXO = 82;
    public static Integer INT_ET_TIPO_PARAMETRO_EDAD = 83;
    public static Integer INT_ET_TIPO_PARAMETRO_TIEMPO = 84;
    public static Integer INT_ET_TIPO_PARAMETRO_NIVEL = 85;
    public static Integer INT_ET_TIPO_PARAMETRO_AREA = 86;
    
    public static String strEspacio = " ";
    public static String strDobleEspacio = "  ";
    public static String strVacio = "";
    public static String strPipe = "|";
    public static String STR_UNDERLINE = "_";
    public static String DDMMYYYY_HH24_MI_SS = "dd/MM/yyyy HH:mm:ss";
    public static String DDMMYYYYHH24MISS = "ddMMyyyyHHmmssS";
    public static String HH24_MI_DDMMYYYY = "HH:mm dd/MM/yyyy";
    public static String DDMMYYYY = "dd/MM/yyyy";
    
    public static String STR_EXTENSION_PDF = ".pdf";
    public static String STR_EXTENSION_PNG = ".png";
    public static String STR_EXTENSION_ZIP = ".zip";
    public static String STR_EXTENSION_XLS = ".xls";
    
    public static String PASO_0 = "0";
    public static String PASO_1 = "1";
    public static String PASO_2 = "2";
    public static String PASO_3 = "3";
    public static String PASO_4 = "4";
    public static String PASO_5 = "5";
    public static String PASO_6 = "6";
    public static String PASO_7 = "7";
    
    public static Integer INT_PASO_1 = 1;
    public static Integer INT_PASO_2 = 2;
    public static Integer INT_PASO_3 = 3;
    public static Integer INT_PASO_4 = 4;
    public static Integer INT_PASO_5 = 5;
    public static Integer INT_PASO_6 = 6;
    
    public static int FIRST_EQUAL_SECOND = 0;
    public static int FIRST_GREATER = 1;
    public static int SECOND_GREATER = -1;
    
    
    
    /************/
    /* REPORTES */
    /************/
    
    public static Integer INT_REPORTE_GRUPAL_CARATULA = 0;
    public static Integer INT_REPORTE_GRUPAL_RESPUESTAS = 10;
    public static Integer INT_REPORTE_GRUPAL_SUMARIO_X_CATEGORIA = 11;
    
    public static Integer INT_REPORTE_GRUPAL_NIVEL_DE_PARTICIPACION = 12;
    public static Integer INT_REPORTE_GRUPAL_PROMEDIO_GENERAL_POR_COMPETENCIA = 11;
    public static Integer INT_REPORTE_GRUPAL_PROMEDIO_GENERAL_POR_PREGUNTA = 12;
    public static Integer INT_REPORTE_GRUPAL_PROMEDIO_POR_PREGUNTAS_ORDENADO_DESCENDENTE = 13;
    public static Integer INT_REPORTE_GRUPAL_PERSONAS_CON_MEJOR_PUNTJE_GENERAL = 14;
    public static Integer INT_REPORTE_GRUPAL_PERSONAS_CON_MENOR_PUNTAJE_GENERAL = 15;
    public static Integer INT_REPORTE_GRUPAL_RESUMEN_DE_PROMEDIO = 16;

    public static Integer INT_REPORTE_GRUPAL_RESUMEN_DE_EVALUADOS_POR_RELACIONES = 22;
        
    
    public static Integer INT_REPORTE_INDIVIDUAL_CARATULA = 0;
    public static Integer INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA = 1;
    public static Integer INT_REPORTE_INDIVIDUAL_SUMARIO_X_CATEGORIA_MISMO = 2;
    public static Integer INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_CATEGORIA = 3;
    public static Integer INT_REPORTE_INDIVIDUAL_CALIFICACION_X_ITEM_PROMEDIO = 4;
    public static Integer INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_OTROS = 5;
    public static Integer INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_OTROS = 6;
    public static Integer INT_REPORTE_INDIVIDUAL_ITEM_ALTA_CALIFICACION_MISMO = 7;
    public static Integer INT_REPORTE_INDIVIDUAL_ITEM_BAJA_CALIFICACION_MISMO = 8;
    public static Integer INT_REPORTE_INDIVIDUAL_PREGUNTAS_ABIERTAS = 9;
    
    
  
    public static String STR_INBOX_PRELIMINAR = File.separator + "var" + File.separator + "tmp" + File.separator + "InboxPreliminar";
    public static String STR_INBOX_DEFINITIVO = File.separator + "var" + File.separator + "tmp" + File.separator + "InboxDefinitivo";
    
    //public static String STR_INBOX_PRELIMINAR = File.separator + "D://InboxPreliminar";
    //public static String STR_INBOX_DEFINITIVO = File.separator + "D://InboxDefinitivo";
    
    public static int FORMAT_DATE_SHORT = DateFormat.SHORT;
    public static int FORMAT_DATE_MEDIUM = DateFormat.MEDIUM;
    public static int FORMAT_DATE_LONG = DateFormat.LONG;
    public static int FORMAT_DATE_FULL = DateFormat.FULL;
    
    
    
    
    public static String INT_PARAM_GRAF_MEDIDA = "graficoMedida";
    
    // Para contratos
    public static Integer INT_DT_ESTADO_CONTRATO = 7;
    public static Integer INT_DT_TIPO_CONTRATO = 8;

    
    
    public static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
    
    public static Integer INTERVAL_SESSION = 30 * 60;
    
    
    /*IMPORTAR CUESTIONARIO*/
    public static String LINEA_CUESTIONARIO = "CUE";
    public static String LINEA_CATEGORIA = "CAT";
    public static String LINEA_PREGUNTA_CERRADA = "PRC";
    public static String LINEA_COMENTARIO = "COM";
    public static String LINEA_PREGUNTA_ABIERTA = "PRA";

    
    public static Charset UTF8_CHARSET = Charset.forName("UTF-8");

    public static String ASTERISCO = "*";
    public static String TODO_TEXTO = "%%";
    

    public static Integer ET_DIAS_BUSQUEDAS = 100;
}
