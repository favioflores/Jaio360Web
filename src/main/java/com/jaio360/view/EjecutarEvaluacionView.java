package com.jaio360.view;

import com.jaio360.dao.EjecutarEvaluacionDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.ComentarioBean;
import com.jaio360.domain.EvaluacionesXEjecutar;
import com.jaio360.domain.PreguntaAbiertaBean;
import com.jaio360.domain.PreguntaCerradaBean;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Componente;
import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.RelacionParticipanteId;
import com.jaio360.orm.Resultado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author user
 */
@ManagedBean(name = "ejecutarEvaluacionView")
@ViewScoped
public class EjecutarEvaluacionView extends BaseView implements Serializable {

    private static Logger log = Logger.getLogger(EjecutarEvaluacionView.class);

    private String strDescCuestionario;
    private String strDescEvaluado;
    private String strCargoEvaluado;
    private String strCorreoEvaluado;
    private String strUrlImagen;
    private String strInstrucciones;
    private String strAgradecimiento;
    private Integer indexTocomment;

    private List<EvaluacionesXEjecutar> lstEvaluacionesXEjecutar;
    private Boolean blVisualGroup;

    private List<Componente> lstComponenteCerrada;
    private List<Componente> lstComponenteAbierta;
    private List<Componente> lstCompComentario;
    private List<DetalleMetrica> lstDetalleMetrica;
    private RelacionParticipanteId relacionParticipanteId;

    private Boolean blTerminado;

    public static int TIPO_COMPONENTE_CATEGORIA = 45;
    public static int TIPO_COMPONENTE_CERRADA = 46;
    public static int TIPO_COMPONENTE_ABIERTA = 47;
    public static int TIPO_COMPONENTE_COMENTARIO = 48;

    /**
     * ** NUEVO PREGUNTA CERRADA ***
     */
    private List<PreguntaCerradaBean> lstPreguntasCerradas;
    private String strDescripcionPreguntaCerradaActual;
    private Integer intRptaSeleccionada;
    private String[] strRptaComentario = new String[50];
    private LinkedHashMap<Integer, String> mapRespuestas;
    private Boolean isPreguntaCerradaActual;

    /**
     * ** NUEVO PREGUNTA ABIERTA ***
     */
    private List<PreguntaAbiertaBean> lstPreguntasAbiertas;
    private String strRptaPreguntaAbierta;
    private String strDescripcionPreguntaAbiertaActual;

    /**
     * ** GENERAL ***
     */
    private Integer intNroTotalPreguntas;
    private Integer intNroPreguntasActual;
    private Integer intNroTotalPreguntasRespondidas;

    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
    }

    public String getStrDescEvaluado() {
        return strDescEvaluado;
    }

    public void setStrDescEvaluado(String strDescEvaluado) {
        this.strDescEvaluado = strDescEvaluado;
    }

    public String getStrCargoEvaluado() {
        return strCargoEvaluado;
    }

    public void setStrCargoEvaluado(String strCargoEvaluado) {
        this.strCargoEvaluado = strCargoEvaluado;
    }

    public String getStrCorreoEvaluado() {
        return strCorreoEvaluado;
    }

    public void setStrCorreoEvaluado(String strCorreoEvaluado) {
        this.strCorreoEvaluado = strCorreoEvaluado;
    }

    public String getStrUrlImagen() {
        return strUrlImagen;
    }

    public void setStrUrlImagen(String strUrlImagen) {
        this.strUrlImagen = strUrlImagen;
    }

    public String getStrInstrucciones() {
        return strInstrucciones;
    }

    public void setStrInstrucciones(String strInstrucciones) {
        this.strInstrucciones = strInstrucciones;
    }

    public String getStrAgradecimiento() {
        return strAgradecimiento;
    }

    public void setStrAgradecimiento(String strAgradecimiento) {
        this.strAgradecimiento = strAgradecimiento;
    }

    public Integer getIndexTocomment() {
        return indexTocomment;
    }

    public void setIndexTocomment(Integer indexTocomment) {
        this.indexTocomment = indexTocomment;
    }

    public List<EvaluacionesXEjecutar> getLstEvaluacionesXEjecutar() {
        return lstEvaluacionesXEjecutar;
    }

    public void setLstEvaluacionesXEjecutar(List<EvaluacionesXEjecutar> lstEvaluacionesXEjecutar) {
        this.lstEvaluacionesXEjecutar = lstEvaluacionesXEjecutar;
    }

    public Boolean getBlVisualGroup() {
        return blVisualGroup;
    }

    public void setBlVisualGroup(Boolean blVisualGroup) {
        this.blVisualGroup = blVisualGroup;
    }

    public List<Componente> getLstComponenteCerrada() {
        return lstComponenteCerrada;
    }

    public void setLstComponenteCerrada(List<Componente> lstComponenteCerrada) {
        this.lstComponenteCerrada = lstComponenteCerrada;
    }

    public List<Componente> getLstComponenteAbierta() {
        return lstComponenteAbierta;
    }

    public void setLstComponenteAbierta(List<Componente> lstComponenteAbierta) {
        this.lstComponenteAbierta = lstComponenteAbierta;
    }

    public List<Componente> getLstCompComentario() {
        return lstCompComentario;
    }

    public void setLstCompComentario(List<Componente> lstCompComentario) {
        this.lstCompComentario = lstCompComentario;
    }

    public List<DetalleMetrica> getLstDetalleMetrica() {
        return lstDetalleMetrica;
    }

    public void setLstDetalleMetrica(List<DetalleMetrica> lstDetalleMetrica) {
        this.lstDetalleMetrica = lstDetalleMetrica;
    }

    public RelacionParticipanteId getRelacionParticipanteId() {
        return relacionParticipanteId;
    }

    public void setRelacionParticipanteId(RelacionParticipanteId relacionParticipanteId) {
        this.relacionParticipanteId = relacionParticipanteId;
    }

    public Boolean getBlTerminado() {
        return blTerminado;
    }

    public void setBlTerminado(Boolean blTerminado) {
        this.blTerminado = blTerminado;
    }

    public static int getTIPO_COMPONENTE_CATEGORIA() {
        return TIPO_COMPONENTE_CATEGORIA;
    }

    public static void setTIPO_COMPONENTE_CATEGORIA(int TIPO_COMPONENTE_CATEGORIA) {
        EjecutarEvaluacionView.TIPO_COMPONENTE_CATEGORIA = TIPO_COMPONENTE_CATEGORIA;
    }

    public static int getTIPO_COMPONENTE_CERRADA() {
        return TIPO_COMPONENTE_CERRADA;
    }

    public static void setTIPO_COMPONENTE_CERRADA(int TIPO_COMPONENTE_CERRADA) {
        EjecutarEvaluacionView.TIPO_COMPONENTE_CERRADA = TIPO_COMPONENTE_CERRADA;
    }

    public static int getTIPO_COMPONENTE_ABIERTA() {
        return TIPO_COMPONENTE_ABIERTA;
    }

    public static void setTIPO_COMPONENTE_ABIERTA(int TIPO_COMPONENTE_ABIERTA) {
        EjecutarEvaluacionView.TIPO_COMPONENTE_ABIERTA = TIPO_COMPONENTE_ABIERTA;
    }

    public static int getTIPO_COMPONENTE_COMENTARIO() {
        return TIPO_COMPONENTE_COMENTARIO;
    }

    public static void setTIPO_COMPONENTE_COMENTARIO(int TIPO_COMPONENTE_COMENTARIO) {
        EjecutarEvaluacionView.TIPO_COMPONENTE_COMENTARIO = TIPO_COMPONENTE_COMENTARIO;
    }

    public List<PreguntaCerradaBean> getLstPreguntasCerradas() {
        return lstPreguntasCerradas;
    }

    public void setLstPreguntasCerradas(List<PreguntaCerradaBean> lstPreguntasCerradas) {
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

    public String getStrDescripcionPreguntaCerradaActual() {
        return strDescripcionPreguntaCerradaActual;
    }

    public void setStrDescripcionPreguntaCerradaActual(String strDescripcionPreguntaCerradaActual) {
        this.strDescripcionPreguntaCerradaActual = strDescripcionPreguntaCerradaActual;
    }

    public Integer getIntRptaSeleccionada() {
        return intRptaSeleccionada;
    }

    public void setIntRptaSeleccionada(Integer intRptaSeleccionada) {
        this.intRptaSeleccionada = intRptaSeleccionada;
    }

    public String[] getStrRptaComentario() {
        return strRptaComentario;
    }

    public void setStrRptaComentario(String[] strRptaComentario) {
        this.strRptaComentario = strRptaComentario;
    }

    public LinkedHashMap<Integer, String> getMapRespuestas() {
        return mapRespuestas;
    }

    public void setMapRespuestas(LinkedHashMap<Integer, String> mapRespuestas) {
        this.mapRespuestas = mapRespuestas;
    }

    public Boolean getIsPreguntaCerradaActual() {
        return isPreguntaCerradaActual;
    }

    public void setIsPreguntaCerradaActual(Boolean isPreguntaCerradaActual) {
        this.isPreguntaCerradaActual = isPreguntaCerradaActual;
    }

    public List<PreguntaAbiertaBean> getLstPreguntasAbiertas() {
        return lstPreguntasAbiertas;
    }

    public void setLstPreguntasAbiertas(List<PreguntaAbiertaBean> lstPreguntasAbiertas) {
        this.lstPreguntasAbiertas = lstPreguntasAbiertas;
    }

    public String getStrRptaPreguntaAbierta() {
        return strRptaPreguntaAbierta;
    }

    public void setStrRptaPreguntaAbierta(String strRptaPreguntaAbierta) {
        this.strRptaPreguntaAbierta = strRptaPreguntaAbierta;
    }

    public String getStrDescripcionPreguntaAbiertaActual() {
        return strDescripcionPreguntaAbiertaActual;
    }

    public void setStrDescripcionPreguntaAbiertaActual(String strDescripcionPreguntaAbiertaActual) {
        this.strDescripcionPreguntaAbiertaActual = strDescripcionPreguntaAbiertaActual;
    }

    public Integer getIntNroTotalPreguntas() {
        return intNroTotalPreguntas;
    }

    public void setIntNroTotalPreguntas(Integer intNroTotalPreguntas) {
        this.intNroTotalPreguntas = intNroTotalPreguntas;
    }

    public Integer getIntNroPreguntasActual() {
        return intNroPreguntasActual;
    }

    public void setIntNroPreguntasActual(Integer intNroPreguntasActual) {
        this.intNroPreguntasActual = intNroPreguntasActual;
    }

    public Integer getIntNroTotalPreguntasRespondidas() {
        return intNroTotalPreguntasRespondidas;
    }

    public void setIntNroTotalPreguntasRespondidas(Integer intNroTotalPreguntasRespondidas) {
        this.intNroTotalPreguntasRespondidas = intNroTotalPreguntasRespondidas;
    }

    public void siguientePregunta() {

        if (this.intNroPreguntasActual < lstPreguntasCerradas.size()) {
            isPreguntaCerradaActual = true;
            //guardar respuesta a pregunta cerrada respondida
            grabarRespuestaCerradaActual();
        } else {
            isPreguntaCerradaActual = false;
            //guardar respuesta a pregunta cerrada respondida
            grabarRespuestaAbiertaActual();
        }

        if (this.intNroPreguntasActual < lstPreguntasCerradas.size() - 1) {
            isPreguntaCerradaActual = true;
            //cargar proxima pregunta cerrada
            cargarPreguntaCerrada(+1);
        } else {
            isPreguntaCerradaActual = false;
            cargarPreguntaAbierta(+1);
        }

    }

    public void ultimaPregunta() {

        if (this.intNroPreguntasActual < lstPreguntasCerradas.size()) {
            isPreguntaCerradaActual = true;
            //guardar respuesta a pregunta cerrada respondida
            grabarRespuestaCerradaActual();
        } else {
            isPreguntaCerradaActual = false;
            //guardar respuesta a pregunta cerrada respondida
            grabarRespuestaAbiertaActual();
        }

        isPreguntaCerradaActual = this.intNroPreguntasActual < lstPreguntasCerradas.size() - 1;

        guardarResultadoFinal();

    }

    public void previaPregunta() {

        if (this.intNroPreguntasActual < lstPreguntasCerradas.size()) {
            isPreguntaCerradaActual = true;
            //guardar respuesta respondida
            grabarRespuestaCerradaActual();
        } else {
            isPreguntaCerradaActual = false;
            grabarRespuestaAbiertaActual();
        }

        if (this.intNroPreguntasActual <= lstPreguntasCerradas.size()) {
            isPreguntaCerradaActual = true;
            //cargar proxima
            cargarPreguntaCerrada(-1);
        } else {
            isPreguntaCerradaActual = false;
            cargarPreguntaAbierta(-1);
        }

    }

    private void cargarPreguntaCerrada(Integer intNroPreguntaCerrada) {

        this.intNroPreguntasActual = this.intNroPreguntasActual + intNroPreguntaCerrada;

        Arrays.fill(this.strRptaComentario, Constantes.strVacio);

        for (EvaluacionesXEjecutar objEvaluacionesXEjecutar : lstEvaluacionesXEjecutar) {

            try {
                PreguntaCerradaBean objPreguntaCerradaBean = objEvaluacionesXEjecutar.getLstPreguntasCerradas().get(this.intNroPreguntasActual);
                if (objPreguntaCerradaBean.getIdRespuesta() != null) {
                    objEvaluacionesXEjecutar.setIntRptaSeleccionada(objPreguntaCerradaBean.getIdRespuesta());
                } else {
                    objEvaluacionesXEjecutar.setIntRptaSeleccionada(null);
                }
            } catch (Exception e) {
                objEvaluacionesXEjecutar.setIntRptaSeleccionada(null);
            }

        }

        this.strDescripcionPreguntaCerradaActual = lstPreguntasCerradas.get(this.intNroPreguntasActual).getStrDescripcion();

        /*
        PreguntaCerradaBean objPreguntaCerradaBean = this.lstPreguntasCerradas.get(this.intNroPreguntasActual);
        int i = 0;

        for (ComentarioBean objComentarioBean : objPreguntaCerradaBean.getLstComentarios()) {
            this.strRptaComentario[i] = objComentarioBean.getStrRespuesta();
            i++;
        }
        
         */
    }

    private void cargarPreguntaAbierta(Integer intNroPreguntaAbierta) {

        this.intNroPreguntasActual = this.intNroPreguntasActual + intNroPreguntaAbierta;

        try {
            for (EvaluacionesXEjecutar objEvaluacionesXEjecutar : lstEvaluacionesXEjecutar) {

                try {

                    PreguntaAbiertaBean objPreguntaAbiertaBean = objEvaluacionesXEjecutar.getLstPreguntasAbiertas().get(this.intNroPreguntasActual - this.lstPreguntasCerradas.size());

                    if (Utilitarios.noEsNuloOVacio(objPreguntaAbiertaBean.getStrRespuesta())) {
                        objEvaluacionesXEjecutar.setStrRptaPreguntaAbierta(objPreguntaAbiertaBean.getStrRespuesta());
                    } else {
                        objEvaluacionesXEjecutar.setStrRptaPreguntaAbierta(null);
                    }

                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            mostrarError(log, e);
        }

        this.strDescripcionPreguntaAbiertaActual = lstPreguntasAbiertas.get(this.intNroPreguntasActual - this.lstPreguntasCerradas.size()).getStrDescripcion();

    }

    public void grabarRespuestaCerradaActual() {

        try {
            for (EvaluacionesXEjecutar objEvaluacionesXEjecutar : lstEvaluacionesXEjecutar) {

                if (Utilitarios.noEsNuloOVacio(objEvaluacionesXEjecutar.getIntRptaSeleccionada())) {
                    
                    log.error("User: " + Utilitarios.obtenerUsuario().getStrEmail() + " grabarRespuestaCerradaActual() " + objEvaluacionesXEjecutar.getIntRptaSeleccionada());

                    PreguntaCerradaBean objPreguntaCerradaBean = objEvaluacionesXEjecutar.getLstPreguntasCerradas().get(this.intNroPreguntasActual);

                    //BeanUtils.copyProperties(objPreguntaCerradaBean, lstPreguntasCerradas.get(this.intNroPreguntasActual));
                    objPreguntaCerradaBean.setIdRespuesta(objEvaluacionesXEjecutar.getIntRptaSeleccionada());
                    objPreguntaCerradaBean.setBlRespondido(true);

                    /*
                    try {
                        objEvaluacionesXEjecutar.getLstPreguntasCerradas().remove(this.intNroPreguntasActual.intValue());
                    } catch (Exception e) {
                    }
                    objEvaluacionesXEjecutar.getLstPreguntasCerradas().add(this.intNroPreguntasActual, objPreguntaCerradaBean);
                     */
                    objEvaluacionesXEjecutar.setIntRptaSeleccionada(null);
                } else {
                    
                    log.error("User: " + Utilitarios.obtenerUsuario().getStrEmail() + " grabarRespuestaCerradaActual() null");
                    
                    PreguntaCerradaBean objPreguntaCerradaBean = objEvaluacionesXEjecutar.getLstPreguntasCerradas().get(this.intNroPreguntasActual);

                    //BeanUtils.copyProperties(objPreguntaCerradaBean, lstPreguntasCerradas.get(this.intNroPreguntasActual));
                    objPreguntaCerradaBean.setIdRespuesta(null);
                    objPreguntaCerradaBean.setBlRespondido(false);

                    /*
                    try {
                        objEvaluacionesXEjecutar.getLstPreguntasCerradas().remove(this.intNroPreguntasActual.intValue());
                    } catch (Exception e) {
                    }
                    objEvaluacionesXEjecutar.getLstPreguntasCerradas().add(this.intNroPreguntasActual, objPreguntaCerradaBean);
                     */
                    objEvaluacionesXEjecutar.setIntRptaSeleccionada(null);
                }
            }

            /*

        PreguntaCerradaBean objPreguntaCerradaBean = lstPreguntasCerradas.get(intNroPreguntasActual);

        List<ComentarioBean> lstComentarioBean;

        ComentarioBean objComentarioBean;

        for (int i = 0; i < lstCompComentario.size(); i++) {
            String strComentario = strRptaComentario[i];
            if (Utilitarios.noEsNuloOVacio(strComentario)) {
                lstComentarioBean = objPreguntaCerradaBean.getLstComentarios();
                objComentarioBean = lstComentarioBean.get(i);
                objComentarioBean.setStrRespuesta(strComentario);
            }
        }
             */
        } catch (Exception ex) {
            mostrarError(log, ex);
        }
    }

    public void grabarRespuestaAbiertaActual() {
        try {
            for (EvaluacionesXEjecutar objEvaluacionesXEjecutar : lstEvaluacionesXEjecutar) {

                PreguntaAbiertaBean objPreguntaAbiertaBean = new PreguntaAbiertaBean();

                BeanUtils.copyProperties(objPreguntaAbiertaBean, lstPreguntasAbiertas.get(this.intNroPreguntasActual - this.lstPreguntasCerradas.size()));

                objPreguntaAbiertaBean.setStrRespuesta(objEvaluacionesXEjecutar.getStrRptaPreguntaAbierta());

                try {
                    objEvaluacionesXEjecutar.getLstPreguntasAbiertas().remove((this.intNroPreguntasActual - this.lstPreguntasCerradas.size()));
                } catch (Exception e) {
                }
                objEvaluacionesXEjecutar.getLstPreguntasAbiertas().add(this.intNroPreguntasActual - this.lstPreguntasCerradas.size(), objPreguntaAbiertaBean);
                objEvaluacionesXEjecutar.setStrRptaPreguntaAbierta(null);

            }
        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    @PostConstruct
    public void init() {

        try {

            /* SE HACE 5 A MAS EVALUADOS A LA VEZ*/
            blVisualGroup = true;

            lstEvaluacionesXEjecutar = new ArrayList<>();

            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();

            this.lstEvaluacionesXEjecutar = objProyectoInfo.getLstEvaluacionesXEjecutar();

            if (objProyectoInfo.getBoDefineArtificio()) {
                strDescEvaluado = ""
                        + objProyectoInfo.getStrNombreEvaluado();
            } else {
                strDescEvaluado = objProyectoInfo.getStrNombreEvaluado();
            }

            this.strUrlImagen = "";//objProyectoInfo.getStrURLImagen();
            this.strCargoEvaluado = objProyectoInfo.getStrCargoEvaluado();
            this.strCorreoEvaluado = objProyectoInfo.getStrCorreoEvaluado();

            EjecutarEvaluacionDAO eEvaluadoDAO = new EjecutarEvaluacionDAO();

            this.lstComponenteCerrada = eEvaluadoDAO.obtenerComponenteTipoXCustionario(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getIntIdCuestionario(), TIPO_COMPONENTE_CERRADA);
            this.lstComponenteAbierta = eEvaluadoDAO.obtenerComponenteTipoXCustionario(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getIntIdCuestionario(), TIPO_COMPONENTE_ABIERTA);
            this.lstDetalleMetrica = eEvaluadoDAO.obtenerDetalleMetrica(objProyectoInfo.getIntIdProyecto());
            this.lstCompComentario = eEvaluadoDAO.obtenerComponenteTipoXCustionario(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getIntIdCuestionario(), TIPO_COMPONENTE_COMENTARIO);

            this.intNroTotalPreguntas = lstComponenteCerrada.size() + lstComponenteAbierta.size();
            this.intNroPreguntasActual = 0;

            /**
             * OBTENER RESPUESTAS
             */
            mapRespuestas = new LinkedHashMap<>();

            int i = 1;
            for (DetalleMetrica objDetalleMetrica : lstDetalleMetrica) {
                mapRespuestas.put(i, objDetalleMetrica.getDeIdDetalleEscalaPk().toString());
                i++;
            }

            isPreguntaCerradaActual = !lstComponenteCerrada.isEmpty();

            lstPreguntasCerradas = new ArrayList<>();

            for (Componente objComponentePreguntaCerrada : lstComponenteCerrada) {

                PreguntaCerradaBean objPreguntaCerradaBean = new PreguntaCerradaBean();

                objPreguntaCerradaBean.setId(objComponentePreguntaCerrada.getCoIdComponentePk());
                objPreguntaCerradaBean.setStrDescripcion(objComponentePreguntaCerrada.getCoTxDescripcion());
                objPreguntaCerradaBean.setLstComentarios(new ArrayList<>());

                for (Componente objComponenteComentario : lstCompComentario) {
                    ComentarioBean objComentarioBean = new ComentarioBean();
                    objComentarioBean.setId(objComponenteComentario.getCoIdComponentePk());
                    objComentarioBean.setStrDescripcion(objComponenteComentario.getCoTxDescripcion());
                    objPreguntaCerradaBean.getLstComentarios().add(objComentarioBean);
                }

                lstPreguntasCerradas.add(objPreguntaCerradaBean);

            }

            this.strDescripcionPreguntaCerradaActual = lstPreguntasCerradas.get(0).getStrDescripcion();

            lstPreguntasAbiertas = new ArrayList<>();

            for (Componente objComponentePreguntaAbierta : lstComponenteAbierta) {

                PreguntaAbiertaBean objPreguntaAbiertaBean = new PreguntaAbiertaBean();

                objPreguntaAbiertaBean.setId(objComponentePreguntaAbierta.getCoIdComponentePk());
                objPreguntaAbiertaBean.setStrDescripcion(objComponentePreguntaAbierta.getCoTxDescripcion());

                lstPreguntasAbiertas.add(objPreguntaAbiertaBean);

            }

            this.strDescripcionPreguntaAbiertaActual = lstPreguntasAbiertas.get(0).getStrDescripcion();

            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();

            RelacionParticipanteDAO relacionParticipanteDAO = new RelacionParticipanteDAO();
            int a = 0;
            for (EvaluacionesXEjecutar objEvaluacionesXEjecutar : lstEvaluacionesXEjecutar) {
                if (!objEvaluacionesXEjecutar.getBlAutoevaluation()) {
                    objEvaluacionesXEjecutar.setRelacionParticipanteId(relacionParticipanteDAO.obtenRelacionParticipanteId(objProyectoInfo, objEvaluacionesXEjecutar.getStrCorreoEvaluador(), objEvaluacionesXEjecutar.getIdParticipante()));
                }

                objEvaluacionesXEjecutar.getLstPreguntasCerradas().clear();

                for (PreguntaCerradaBean objPreguntaCerradaBean : lstPreguntasCerradas) {

                    PreguntaCerradaBean objPreguntaCerradaBean1 = new PreguntaCerradaBean();
                    objPreguntaCerradaBean1.setId(objPreguntaCerradaBean.getId());
                    objPreguntaCerradaBean1.setStrDescripcion(objPreguntaCerradaBean.getStrDescripcion());

                    objPreguntaCerradaBean1.setLstComentarios(new ArrayList<>());

                    for (Componente objComponenteComentario : lstCompComentario) {
                        ComentarioBean objComentarioBean = new ComentarioBean();
                        objComentarioBean.setId(objComponenteComentario.getCoIdComponentePk());
                        objComentarioBean.setStrDescripcion(objComponenteComentario.getCoTxDescripcion());
                        objPreguntaCerradaBean1.getLstComentarios().add(objComentarioBean);
                    }

                    objEvaluacionesXEjecutar.getLstPreguntasCerradas().add(objPreguntaCerradaBean1);

                }

                a++;

            }

            MensajeDAO objMensajeDAO = new MensajeDAO();
            Mensaje objMensajeInstrucciones = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES);
            Mensaje objMensajeAgradecimiento = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO);

            byte[] bdataInstruccion = objMensajeInstrucciones.getMeTxCuerpo();
            byte[] bdataAgradecimiento = objMensajeAgradecimiento.getMeTxCuerpo();

            this.strInstrucciones = Utilitarios.decodeUTF8(bdataInstruccion);
            this.strInstrucciones = this.strInstrucciones.replace("$NOMBRE", objProyectoInfo.getStrNombreEvaluador());

            this.strAgradecimiento = Utilitarios.decodeUTF8(bdataAgradecimiento);
            this.strAgradecimiento = this.strAgradecimiento.replace("$NOMBRE", objProyectoInfo.getStrNombreEvaluador());

        } catch (Exception e) {
            mostrarError(log, e);
            if (Utilitarios.obtenerEvaluacion() == null) {
                UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
                try {
                    if (objUsuarioInfo.getManagingDirector() || objUsuarioInfo.getCountryManager() || objUsuarioInfo.getProjectManager()) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("stepFive.jsf");
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.jsf");
                    }
                } catch (Exception ex) {
                    mostrarError(log, ex);
                }
            }
        }

    }

    public void guardarResultadoFinal() {

        try {
            log.debug("guardarResultadoFinal()");

            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();

            if (session.getAttribute("evalInfo") == null) {
                if (objUsuarioInfo.getManagingDirector() || objUsuarioInfo.getCountryManager() || objUsuarioInfo.getProjectManager()) {
                    if (objProyectoInfo.getBoDefineArtificio()) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("stepFive.jsf");
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.jsf");
                    }
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.jsf");
                }
            } else {

                log.debug("Evaluaciones por grabar: " + this.lstEvaluacionesXEjecutar.size());

                List<EvaluacionesXEjecutar> lstPreGrabado = new ArrayList<>(this.lstEvaluacionesXEjecutar);

                this.lstEvaluacionesXEjecutar.clear();

                ResultadoDAO objResultadoDAO = new ResultadoDAO();

                blTerminado = objResultadoDAO.guardarResultadoFinal(lstPreGrabado, objProyectoInfo.getIntIdProyecto(), new Date());

                if(blTerminado){
                    salir();
                }else{
                    mostrarAlertaError("error.was.occurred");
                }

            }

        } catch (Exception ex) {
            mostrarError(log, ex);
        }
    }

    public void salir() {
        try {
            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();

            if (objUsuarioInfo.getManagingDirector() || objUsuarioInfo.getCountryManager() || objUsuarioInfo.getProjectManager()) {
                if (objProyectoInfo.getBoDefineArtificio()) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepFive.jsf");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.jsf");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("welcome.jsf");
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("evalInfo");

        } catch (Exception ex) {
            mostrarError(log, ex);
        }
    }

    public void pressButtonRadio(Integer idPresionado) {

        this.intRptaSeleccionada = idPresionado;

    }

    public void generateComment(int index) {
        try {

            this.indexTocomment = index;

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void saveTempComment() {
        try {

            EvaluacionesXEjecutar objEvaluacionesXEjecutar = this.lstEvaluacionesXEjecutar.get(this.indexTocomment);
            objEvaluacionesXEjecutar.getIdProyecto();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

}
