package com.jaio360.view;

import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.EjecutarEvaluacionDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.dao.ResultadoDAO;
import com.jaio360.domain.ComentarioBean;
import com.jaio360.domain.PreguntaAbiertaBean;
import com.jaio360.domain.PreguntaCerradaBean;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
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
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.primefaces.component.outputpanel.OutputPanel;

/**
 *
 * @author user
 */
@ManagedBean(name = "ejecutarEvaluacionView")
@ViewScoped
public class EjecutarEvaluacionView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(EjecutarEvaluacionView.class);

    private String strDescCuestionario;
    private String strDescEvaluado;
    private String strInstrucciones;
    private String strAgradecimiento;
    private List<Componente> lstComponenteCerrada;
    private List<Componente> lstComponenteAbierta;
    private List<Componente> lstCompComentario;
    private List<DetalleMetrica> lstDetalleMetrica;
    private RelacionParticipanteId relacionParticipanteId;

    private OutputPanel objOutputPanelCerrada;
    private OutputPanel objOutputPanelAbierta;

    private boolean blTerminado;

    public static int TIPO_COMPONENTE_CATEGORIA = 45;
    public static int TIPO_COMPONENTE_CERRADA = 46;
    public static int TIPO_COMPONENTE_ABIERTA = 47;
    public static int TIPO_COMPONENTE_COMENTARIO = 48;

    public static int TIPO_METODOLOGIA_ESCALA = 30;
    public static int TIPO_METODOLOGIA_ELECCION_FORZADA = 31;

    private int number2;

    /**
     * ** NUEVO PREGUNTA CERRADA ***
     */
    private List<PreguntaCerradaBean> lstPreguntasCerradas;
    private String strDescripcionPreguntaCerradaActual;
    private Integer intRptaSeleccionada;
    private String[] strRptaComentario = new String[50];
    private LinkedHashMap<String, String> mapRespuestas;
    private boolean isPreguntaCerradaActual;

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

    public String getStrDescripcionPreguntaAbiertaActual() {
        return strDescripcionPreguntaAbiertaActual;
    }

    public void setStrDescripcionPreguntaAbiertaActual(String strDescripcionPreguntaAbiertaActual) {
        this.strDescripcionPreguntaAbiertaActual = strDescripcionPreguntaAbiertaActual;
    }

    public String getStrRptaPreguntaAbierta() {
        return strRptaPreguntaAbierta;
    }

    public void setStrRptaPreguntaAbierta(String strRptaPreguntaAbierta) {
        this.strRptaPreguntaAbierta = strRptaPreguntaAbierta;
    }

    public boolean isIsPreguntaCerradaActual() {
        return isPreguntaCerradaActual;
    }

    public void setIsPreguntaCerradaActual(boolean isPreguntaCerradaActual) {
        this.isPreguntaCerradaActual = isPreguntaCerradaActual;
    }

    public List<PreguntaAbiertaBean> getLstPreguntasAbiertas() {
        return lstPreguntasAbiertas;
    }

    public void setLstPreguntasAbiertas(List<PreguntaAbiertaBean> lstPreguntasAbiertas) {
        this.lstPreguntasAbiertas = lstPreguntasAbiertas;
    }

    public Integer getIntNroPreguntasActual() {
        return intNroPreguntasActual;
    }

    public void setIntNroPreguntasActual(Integer intNroPreguntasActual) {
        this.intNroPreguntasActual = intNroPreguntasActual;
    }

    public Integer getIntNroTotalPreguntas() {
        return intNroTotalPreguntas;
    }

    public void setIntNroTotalPreguntas(Integer intNroTotalPreguntas) {
        this.intNroTotalPreguntas = intNroTotalPreguntas;
    }

    public Integer getIntNroTotalPreguntasRespondidas() {
        return intNroTotalPreguntasRespondidas;
    }

    public void setIntNroTotalPreguntasRespondidas(Integer intNroTotalPreguntasRespondidas) {
        this.intNroTotalPreguntasRespondidas = intNroTotalPreguntasRespondidas;
    }

    public String getStrDescripcionPreguntaCerradaActual() {
        return strDescripcionPreguntaCerradaActual;
    }

    public void setStrDescripcionPreguntaCerradaActual(String strDescripcionPreguntaCerradaActual) {
        this.strDescripcionPreguntaCerradaActual = strDescripcionPreguntaCerradaActual;
    }

    public LinkedHashMap<String, String> getMapRespuestas() {
        return mapRespuestas;
    }

    public List<PreguntaCerradaBean> getLstPreguntasCerradas() {
        return lstPreguntasCerradas;
    }

    public void setLstPreguntasCerradas(List<PreguntaCerradaBean> lstPreguntasCerradas) {
        this.lstPreguntasCerradas = lstPreguntasCerradas;
    }

    public void setMapRespuestas(LinkedHashMap<String, String> mapRespuestas) {
        this.mapRespuestas = mapRespuestas;
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

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public String getStrDescEvaluado() {
        return strDescEvaluado;
    }

    public void setStrDescEvaluado(String strDescEvaluado) {
        this.strDescEvaluado = strDescEvaluado;
    }

    public String getStrAgradecimiento() {
        return strAgradecimiento;
    }

    public void setStrAgradecimiento(String strAgradecimiento) {
        this.strAgradecimiento = strAgradecimiento;
    }

    public boolean isBlTerminado() {
        return blTerminado;
    }

    public void setBlTerminado(boolean blTerminado) {
        this.blTerminado = blTerminado;
    }

    public String getStrInstrucciones() {
        return strInstrucciones;
    }

    public void setStrInstrucciones(String strInstrucciones) {
        this.strInstrucciones = strInstrucciones;
    }

    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
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

    public List<Componente> getLstCompComentario() {
        return lstCompComentario;
    }

    public void setLstCompComentario(List<Componente> lstCompComentario) {
        this.lstCompComentario = lstCompComentario;
    }

    public OutputPanel getObjOutputPanelCerrada() {
        return objOutputPanelCerrada;
    }

    public void setObjOutputPanelCerrada(OutputPanel objOutputPanelCerrada) {
        this.objOutputPanelCerrada = objOutputPanelCerrada;
    }

    public OutputPanel getObjOutputPanelAbierta() {
        return objOutputPanelAbierta;
    }

    public void setObjOutputPanelAbierta(OutputPanel objOutputPanelAbierta) {
        this.objOutputPanelAbierta = objOutputPanelAbierta;
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

        if (this.intNroPreguntasActual < lstPreguntasCerradas.size() - 1) {
            isPreguntaCerradaActual = true;
        } else {
            isPreguntaCerradaActual = false;
        }

        guardarResultado();

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

        if (lstPreguntasCerradas.get(this.intNroPreguntasActual).isBlRespondido()) {
            //Obtener respuesta previamente respondida
            this.strDescripcionPreguntaCerradaActual = lstPreguntasCerradas.get(this.intNroPreguntasActual).getStrDescripcion();
            this.intRptaSeleccionada = lstPreguntasCerradas.get(this.intNroPreguntasActual).getIdRespuesta();
        } else {
            //Se muestra la pregunta sin responder
            this.strDescripcionPreguntaCerradaActual = lstPreguntasCerradas.get(this.intNroPreguntasActual).getStrDescripcion();
            this.intRptaSeleccionada = null;
        }

        PreguntaCerradaBean objPreguntaCerradaBean = this.lstPreguntasCerradas.get(this.intNroPreguntasActual);
        int i = 0;

        for (ComentarioBean objComentarioBean : objPreguntaCerradaBean.getLstComentarios()) {
            this.strRptaComentario[i] = objComentarioBean.getStrRespuesta();
            i++;
        }

    }

    private void cargarPreguntaAbierta(Integer intNroPreguntaAbierta) {

        this.intNroPreguntasActual = this.intNroPreguntasActual + intNroPreguntaAbierta;

        strRptaPreguntaAbierta = Constantes.strVacio;

        //Obtener respuesta previamente respondida
        this.strDescripcionPreguntaAbiertaActual = lstPreguntasAbiertas.get(this.intNroPreguntasActual - this.lstPreguntasCerradas.size()).getStrDescripcion();

        if (Utilitarios.noEsNuloOVacio(lstPreguntasAbiertas.get(this.intNroPreguntasActual - this.lstPreguntasCerradas.size()).getStrRespuesta())) {
            this.strRptaPreguntaAbierta = lstPreguntasAbiertas.get(this.intNroPreguntasActual - this.lstPreguntasCerradas.size()).getStrRespuesta();
        }

    }

    public void grabarRespuestaCerradaActual() {
        if (Utilitarios.noEsNuloOVacio(this.intRptaSeleccionada)) {
            lstPreguntasCerradas.get(this.intNroPreguntasActual).setIdRespuesta(this.intRptaSeleccionada);
            lstPreguntasCerradas.get(this.intNroPreguntasActual).setBlRespondido(true);
        } else {
            lstPreguntasCerradas.get(this.intNroPreguntasActual).setIdRespuesta(null);
            lstPreguntasCerradas.get(this.intNroPreguntasActual).setBlRespondido(false);
        }

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

    }

    public void grabarRespuestaAbiertaActual() {
        if (Utilitarios.noEsNuloOVacio(this.strRptaPreguntaAbierta)) {
            lstPreguntasAbiertas.get(this.intNroPreguntasActual - this.lstPreguntasCerradas.size()).setStrRespuesta(this.strRptaPreguntaAbierta);
        }
    }

    public EjecutarEvaluacionView() {

        try {

            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();

            if (objProyectoInfo.isBoDefineArtificio()) {
                strDescEvaluado = "Usted está evaluando a "
                        + objProyectoInfo.getStrNombreEvaluado();
            } else {
                strDescEvaluado = "Usted está evaluando a " + objProyectoInfo.getStrNombreEvaluado();
            }

            EjecutarEvaluacionDAO eEvaluadoDAO = new EjecutarEvaluacionDAO();

            ProyectoDAO proyectoDAO = new ProyectoDAO();

            proyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

            this.lstComponenteCerrada = eEvaluadoDAO.obtenerComponenteTipo(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado(), TIPO_COMPONENTE_CERRADA);
            this.lstComponenteAbierta = eEvaluadoDAO.obtenerComponenteTipo(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado(), TIPO_COMPONENTE_ABIERTA);
            this.lstDetalleMetrica = eEvaluadoDAO.obtenerDetalleMetrica(objProyectoInfo.getIntIdProyecto());
            this.lstCompComentario = eEvaluadoDAO.obtenerComponenteTipo(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado(), TIPO_COMPONENTE_COMENTARIO);

            this.intNroTotalPreguntas = lstComponenteCerrada.size() + lstComponenteAbierta.size();
            this.intNroPreguntasActual = 0;

            /**
             * OBTENER RESPUESTAS
             */
            mapRespuestas = new LinkedHashMap<>();

            for (DetalleMetrica objDetalleMetrica : lstDetalleMetrica) {
                mapRespuestas.put(objDetalleMetrica.getDeTxValor(), objDetalleMetrica.getDeIdDetalleEscalaPk().toString());
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
            if (!objProyectoInfo.isBoDefineArtificio()) {
                this.strDescCuestionario = objProyectoInfo.getStrDescCuestionario();
                this.relacionParticipanteId = relacionParticipanteDAO.obtenRelacionParticipanteId(objProyectoInfo, objUsuarioInfo.getStrEmail(), objProyectoInfo.getStrCorreoEvaluado());
            } else {
                CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
                Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objProyectoInfo.getIntIdEvaluado());
                objProyectoInfo.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                this.strDescCuestionario = objCuestionario.getCuTxDescripcion();
                this.relacionParticipanteId = relacionParticipanteDAO.obtenRelacionParticipanteId(objProyectoInfo, objProyectoInfo.getStrCorreoEvaluador(), objProyectoInfo.getStrCorreoEvaluado());
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
            log.error(e);
            if (Utilitarios.obtenerEvaluacion() == null) {
                UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
                try {
                    if (objUsuarioInfo.isBoEsAdministrador() || objUsuarioInfo.isBoEsUsuarioMaestro()) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("stepFive.jsf");
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
                    }
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }

    }

    public void guardarResultado() {

        try {
            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

            Integer idProyecto = Utilitarios.obtenerEvaluacion().getIntIdProyecto();
            Integer IdEvaluado = Utilitarios.obtenerEvaluacion().getIntIdEvaluado();

            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();

            if (session.getAttribute("evalInfo") == null) {
                if (objUsuarioInfo.isBoEsAdministrador() || objUsuarioInfo.isBoEsUsuarioMaestro()) {
                    if (objProyectoInfo.isBoDefineArtificio()) {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("stepFive.jsf");
                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
                    }
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
                }
            } else {


                /* GUARDA RESPUESTAS PREGUNTAS CERRADAS */
                for (PreguntaCerradaBean objPreguntaCerradaBean : this.lstPreguntasCerradas) {
                    if (objPreguntaCerradaBean.isBlRespondido()) {
                        guardarResultadoEval(objPreguntaCerradaBean.getId().toString(), objPreguntaCerradaBean.getIdRespuesta().toString(), null, idProyecto, IdEvaluado, null);
                    }

                    for (ComentarioBean objComentarioBean : objPreguntaCerradaBean.getLstComentarios()) {
                        if (Utilitarios.noEsNuloOVacio(objComentarioBean.getStrRespuesta())) {
                            guardarResultadoEval(objComentarioBean.getId().toString(), null, objComentarioBean.getStrRespuesta(), idProyecto, IdEvaluado, objPreguntaCerradaBean.getId().toString());
                        }
                    }
                }

                /* GUARDA RESPUESTAS PREGUNTAS ABIERTAS */
                for (PreguntaAbiertaBean objPreguntaAbiertaBean : lstPreguntasAbiertas) {
                    if (Utilitarios.noEsNuloOVacio(objPreguntaAbiertaBean.getStrRespuesta())) {
                        this.guardarResultadoEval(objPreguntaAbiertaBean.getId().toString(), null, objPreguntaAbiertaBean.getStrRespuesta(), idProyecto, IdEvaluado, null);
                    }
                }

                /* ACTUALIZA EVALUACIÓN A TERMINADO */
                if (relacionParticipanteId != null) {

                    RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
                    RelacionParticipante objRelacionParticipante = objRelacionParticipanteDAO.obtenRelacionParticipante(relacionParticipanteId);
                    objRelacionParticipante.setRpIdEstado(Constantes.INT_ET_ESTADO_RELACION_EDO_EDOR_TERMINADO);
                    objRelacionParticipanteDAO.actualizaRelacionParticipante(objRelacionParticipante);

                } else {
                    ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
                    Participante objParticipante = objParticipanteDAO.obtenParticipante(IdEvaluado);
                    objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
                    objParticipanteDAO.actualizaParticipante(objParticipante);
                }

                blTerminado = true;

            }
        } catch (IOException ex) {
            log.debug(ex);
        }
    }

    public void salir() {
        try {
            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();

            if (objUsuarioInfo.isBoEsAdministrador() || objUsuarioInfo.isBoEsUsuarioMaestro()) {
                if (objProyectoInfo.isBoDefineArtificio()) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("stepFive.jsf");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
            }
            
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.removeAttribute("evalInfo");

        } catch (IOException ex) {
            log.debug(ex);
        }
    }

    public void guardarResultadoEval(String idComponentePk, String idDetalleMetrica, String txtComentario, Integer idProyecto, Integer idEvaluado, String idComponentePreguntaPk) {
        Resultado resultado = new Resultado();
        ResultadoDAO resultadoDAO = new ResultadoDAO();

        Componente componente = new Componente();
        componente.setCoIdComponentePk(Integer.parseInt(idComponentePk));

        DetalleMetrica detalleMetrica = new DetalleMetrica();
        if (Utilitarios.noEsNuloOVacio(idDetalleMetrica)) {
            detalleMetrica.setDeIdDetalleEscalaPk(Integer.parseInt(idDetalleMetrica));
            resultado.setDetalleMetrica(detalleMetrica);
        }

        resultado.setComponente(componente);
        if (Utilitarios.noEsNuloOVacio(txtComentario)) {
            resultado.setReTxComentario(txtComentario);
        }

        if (Utilitarios.noEsNuloOVacio(idComponentePreguntaPk)) {
            resultado.setCoIdComponenteRefFk(Integer.parseInt(idComponentePreguntaPk));
        }

        if (relacionParticipanteId != null) {
            resultado.setPaIdParticipanteFk(relacionParticipanteId.getPaIdParticipanteFk());
            resultado.setReIdParticipanteFk(relacionParticipanteId.getReIdParticipanteFk());
            resultado.setReIdRelacionFk(relacionParticipanteId.getReIdRelacionFk());
        } else {
            resultado.setPaIdParticipanteFk(idEvaluado);
        }

        Proyecto objProyecto = new Proyecto();
        objProyecto.setPoIdProyectoPk(idProyecto);

        resultado.setProyecto(objProyecto);

        //RelacionParticipante relacionParticipante = new RelacionParticipante();
        //relacionParticipante.setId(this.relacionParticipanteId);
        //resultado.setRelacionParticipante(relacionParticipante);
        resultadoDAO.guardaResultado(resultado);

    }

    public void pressButtonRadio(Integer idPresionado) {

        this.intRptaSeleccionada = idPresionado;

    }

}
