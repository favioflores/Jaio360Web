package com.jaio360.view;

import com.jaio360.dao.CargaAvanzadaDAO;
import com.jaio360.dao.ParametroDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RedEvaluacionDAO;
import com.jaio360.dao.RelacionDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.dao.UsuarioDAO;
import com.jaio360.domain.ErrorBean;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.EvaluadoAvan;
import com.jaio360.domain.Evaluador;
import com.jaio360.domain.RelacionAvanzada;
import com.jaio360.domain.RelacionBean;
import com.jaio360.orm.Parametro;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import com.jaio360.validator.validaCorreo;
import com.jaio360.validator.validaTextoIngresado;
import com.jaio360.validator.validaURL;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

@ManagedBean(name = "evaluadosView")
@ViewScoped
public class EvaluadosView extends BaseView implements Serializable {

    private static Log log = LogFactory.getLog(EvaluadosView.class);

    private ParametroDAO objParametroDAO = new ParametroDAO();
    private ProyectoDAO objProyectoDAO = new ProyectoDAO();
    private CargaAvanzadaDAO objCargaAvanzadaDAO = new CargaAvanzadaDAO();

    private Integer modoConfiguracion = 0;

    private boolean btnUploadDisabled = true;

    private Map hSexo;
    private Map hNO;
    private Map hAN;

    private List<ErrorBean> lstErrorAvan;

    private boolean blHabilitarSexo = false;
    private boolean blHabilitarEdad = false;
    private boolean blHabilitarTiempoEmpresa = false;
    private boolean blHabilitarNivelOcupacional = false;
    private boolean blHabilitarAreaNegocio = false;
    private List<SelectItem> lstNivelOcupacional;
    private List<SelectItem> lstAreaNegocio;

    private Integer modo;
    private String strDescripcion;
    private String strCargo;
    private String strCorreo;

    private String strSexo;
    private Integer intEdad;
    private Integer intTiempoEmpresa;
    private String strOcupacion;
    private String strAreaNegocio;

    private Integer intCorrelativo;
    private Integer idParticipantePk;
    private Boolean paInAutoevaluar;
    private List<Evaluado> lstEvaluado;
    private List<Evaluado> lstCargaMasiva;
    private Integer cantidadEvaluadosRegistrados;
    private UploadedFile file;
    private StreamedContent xlsContent;
    private Integer intCantTempCorrect;
    private Integer intCantTempIncorrect;

    /* PARA EVALUADORES */
    private Integer modoEvaluadores;
    private String strDescripcionEvaluadores;
    private String strCargoEvaluadores;
    private String strCorreoEvaluadores;
    private String strSexoEvaluadores;
    private Integer intEdadEvaluadores;
    private Integer intTiempoEmpresaEvaluadores;
    private String strOcupacionEvaluadores;
    private String strAreaNegocioEvaluadores;
    private Integer intCorrelativoEvaluadores;
    private Integer reIdParticipantePk;
    private List<Evaluador> lstEvaluadores;
    private List<Evaluador> lstCargaMasivaEvaluadores;
    private Integer cantidadEvaluadoresRegistrados;
    private UploadedFile fileEvaluadores;
    private StreamedContent xlsContentEvaluadores;
    private Integer intCantTempCorrectEvaluadores;
    private Integer intCantTempIncorrectEvaluadores;

    /* Configuración avanzada */
    private List<RelacionBean> lstAvanRelacion;
    private List<EvaluadoAvan> lstAvanPersonas;
    private StreamedContent xlsContentAvanzado;
    private UploadedFile fileAvanzado;
    private boolean blCargarCorrectoAvan = false;
    private List<RelacionAvanzada> lstRelacionAvanzadas;
    private Map mapRelacionesAvanzado = new HashMap();
    private Map mapRelacionesAbrev = new HashMap();
    private Map mapPersonasAvanzado = new HashMap();
    private Map mapRelacionesPersonasAvanzado = new HashMap();

    private Map mapPerEvaluados = new HashMap();
    private Map mapPerEvaluadores = new HashMap();

    private String strNombre;
    private String strAbreviatura;
    private String strDescripcionRelacion;
    private String strColor;
    private Integer idRelacionPk;

    private Integer intIdEstadoProyecto;

    private List<RelacionBean> lstRelacionBean;
    private List<RelacionAvanzada> lstRelacionesEvaluadoEvaluador;

    public List<RelacionAvanzada> getLstRelacionesEvaluadoEvaluador() {
        return lstRelacionesEvaluadoEvaluador;
    }

    public void setLstRelacionesEvaluadoEvaluador(List<RelacionAvanzada> lstRelacionesEvaluadoEvaluador) {
        this.lstRelacionesEvaluadoEvaluador = lstRelacionesEvaluadoEvaluador;
    }

    public Integer getIntIdEstadoProyecto() {
        return intIdEstadoProyecto;
    }

    public void setIntIdEstadoProyecto(Integer intIdEstadoProyecto) {
        this.intIdEstadoProyecto = intIdEstadoProyecto;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrAbreviatura() {
        return strAbreviatura;
    }

    public void setStrAbreviatura(String strAbreviatura) {
        this.strAbreviatura = strAbreviatura;
    }

    public String getStrDescripcionRelacion() {
        return strDescripcionRelacion;
    }

    public void setStrDescripcionRelacion(String strDescripcionRelacion) {
        this.strDescripcionRelacion = strDescripcionRelacion;
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

    public List<RelacionBean> getLstRelacionBean() {
        return lstRelacionBean;
    }

    public void setLstRelacionBean(List<RelacionBean> lstRelacionBean) {
        this.lstRelacionBean = lstRelacionBean;
    }

    public boolean isBtnUploadDisabled() {
        return btnUploadDisabled;
    }

    public void setBtnUploadDisabled(boolean btnUploadDisabled) {
        this.btnUploadDisabled = btnUploadDisabled;
    }

    public Map getMapRelacionesPersonasAvanzado() {
        return mapRelacionesPersonasAvanzado;
    }

    public void setMapRelacionesPersonasAvanzado(Map mapRelacionesPersonasAvanzado) {
        this.mapRelacionesPersonasAvanzado = mapRelacionesPersonasAvanzado;
    }

    public List<RelacionAvanzada> getLstRelacionAvanzadas() {
        return lstRelacionAvanzadas;
    }

    public void setLstRelacionAvanzadas(List<RelacionAvanzada> lstRelacionAvanzadas) {
        this.lstRelacionAvanzadas = lstRelacionAvanzadas;
    }

    public List<ErrorBean> getLstErrorAvan() {
        return lstErrorAvan;
    }

    public boolean isBlCargarCorrectoAvan() {
        return blCargarCorrectoAvan;
    }

    public void setBlCargarCorrectoAvan(boolean blCargarCorrectoAvan) {
        this.blCargarCorrectoAvan = blCargarCorrectoAvan;
    }

    public void setLstErrorAvan(List<ErrorBean> lstErrorAvan) {
        this.lstErrorAvan = lstErrorAvan;
    }

    public List<EvaluadoAvan> getLstAvanPersonas() {
        return lstAvanPersonas;
    }

    public void setLstAvanPersonas(List<EvaluadoAvan> lstAvanPersonas) {
        this.lstAvanPersonas = lstAvanPersonas;
    }

    public StreamedContent getXlsContentAvanzado() {
        return xlsContentAvanzado;
    }

    public void setXlsContentAvanzado(StreamedContent xlsContentAvanzado) {
        this.xlsContentAvanzado = xlsContentAvanzado;
    }

    public UploadedFile getFileAvanzado() {
        return fileAvanzado;
    }

    public void setFileAvanzado(UploadedFile fileAvanzado) {
        this.fileAvanzado = fileAvanzado;
    }

    public List<RelacionBean> getLstAvanRelacion() {
        return lstAvanRelacion;
    }

    public void setLstAvanRelacion(List<RelacionBean> lstAvanRelacion) {
        this.lstAvanRelacion = lstAvanRelacion;
    }

    public Integer getModoConfiguracion() {
        return modoConfiguracion;
    }

    public void setModoConfiguracion(Integer modoConfiguracion) {
        this.modoConfiguracion = modoConfiguracion;
    }

    public String getStrSexo() {
        return strSexo;
    }

    public void setStrSexo(String strSexo) {
        this.strSexo = strSexo;
    }

    public Integer getIntEdad() {
        return intEdad;
    }

    public void setIntEdad(Integer intEdad) {
        this.intEdad = intEdad;
    }

    public Integer getIntTiempoEmpresa() {
        return intTiempoEmpresa;
    }

    public void setIntTiempoEmpresa(Integer intTiempoEmpresa) {
        this.intTiempoEmpresa = intTiempoEmpresa;
    }

    public String getStrOcupacion() {
        return strOcupacion;
    }

    public void setStrOcupacion(String strOcupacion) {
        this.strOcupacion = strOcupacion;
    }

    public String getStrAreaNegocio() {
        return strAreaNegocio;
    }

    public void setStrAreaNegocio(String strAreaNegocio) {
        this.strAreaNegocio = strAreaNegocio;
    }

    public String getStrSexoEvaluadores() {
        return strSexoEvaluadores;
    }

    public void setStrSexoEvaluadores(String strSexoEvaluadores) {
        this.strSexoEvaluadores = strSexoEvaluadores;
    }

    public Integer getIntEdadEvaluadores() {
        return intEdadEvaluadores;
    }

    public void setIntEdadEvaluadores(Integer intEdadEvaluadores) {
        this.intEdadEvaluadores = intEdadEvaluadores;
    }

    public Integer getIntTiempoEmpresaEvaluadores() {
        return intTiempoEmpresaEvaluadores;
    }

    public void setIntTiempoEmpresaEvaluadores(Integer intTiempoEmpresaEvaluadores) {
        this.intTiempoEmpresaEvaluadores = intTiempoEmpresaEvaluadores;
    }

    public String getStrOcupacionEvaluadores() {
        return strOcupacionEvaluadores;
    }

    public void setStrOcupacionEvaluadores(String strOcupacionEvaluadores) {
        this.strOcupacionEvaluadores = strOcupacionEvaluadores;
    }

    public String getStrAreaNegocioEvaluadores() {
        return strAreaNegocioEvaluadores;
    }

    public void setStrAreaNegocioEvaluadores(String strAreaNegocioEvaluadores) {
        this.strAreaNegocioEvaluadores = strAreaNegocioEvaluadores;
    }

    public String getStrCargoEvaluadores() {
        return strCargoEvaluadores;
    }

    public boolean isBlHabilitarSexo() {
        return blHabilitarSexo;
    }

    public void setBlHabilitarSexo(boolean blHabilitarSexo) {
        this.blHabilitarSexo = blHabilitarSexo;
    }

    public boolean isBlHabilitarEdad() {
        return blHabilitarEdad;
    }

    public void setBlHabilitarEdad(boolean blHabilitarEdad) {
        this.blHabilitarEdad = blHabilitarEdad;
    }

    public List<SelectItem> getLstNivelOcupacional() {
        return lstNivelOcupacional;
    }

    public void setLstNivelOcupacional(List<SelectItem> lstNivelOcupacional) {
        this.lstNivelOcupacional = lstNivelOcupacional;
    }

    public List<SelectItem> getLstAreaNegocio() {
        return lstAreaNegocio;
    }

    public void setLstAreaNegocio(List<SelectItem> lstAreaNegocio) {
        this.lstAreaNegocio = lstAreaNegocio;
    }

    public boolean isBlHabilitarTiempoEmpresa() {
        return blHabilitarTiempoEmpresa;
    }

    public void setBlHabilitarTiempoEmpresa(boolean blHabilitarTiempoEmpresa) {
        this.blHabilitarTiempoEmpresa = blHabilitarTiempoEmpresa;
    }

    public boolean isBlHabilitarNivelOcupacional() {
        return blHabilitarNivelOcupacional;
    }

    public void setBlHabilitarNivelOcupacional(boolean blHabilitarNivelOcupacional) {
        this.blHabilitarNivelOcupacional = blHabilitarNivelOcupacional;
    }

    public boolean isBlHabilitarAreaNegocio() {
        return blHabilitarAreaNegocio;
    }

    public void setBlHabilitarAreaNegocio(boolean blHabilitarAreaNegocio) {
        this.blHabilitarAreaNegocio = blHabilitarAreaNegocio;
    }

    public void setStrCargoEvaluadores(String strCargoEvaluadores) {
        this.strCargoEvaluadores = strCargoEvaluadores;
    }

    public Integer getModoEvaluadores() {
        return modoEvaluadores;
    }

    public void setModoEvaluadores(Integer modoEvaluadores) {
        this.modoEvaluadores = modoEvaluadores;
    }

    public String getStrDescripcionEvaluadores() {
        return strDescripcionEvaluadores;
    }

    public void setStrDescripcionEvaluadores(String strDescripcionEvaluadores) {
        this.strDescripcionEvaluadores = strDescripcionEvaluadores;
    }

    public String getStrCorreoEvaluadores() {
        return strCorreoEvaluadores;
    }

    public void setStrCorreoEvaluadores(String strCorreoEvaluadores) {
        this.strCorreoEvaluadores = strCorreoEvaluadores;
    }

    public Integer getIntCorrelativoEvaluadores() {
        return intCorrelativoEvaluadores;
    }

    public void setIntCorrelativoEvaluadores(Integer intCorrelativoEvaluadores) {
        this.intCorrelativoEvaluadores = intCorrelativoEvaluadores;
    }

    public Integer getReIdParticipantePk() {
        return reIdParticipantePk;
    }

    public void setReIdParticipantePk(Integer reIdParticipantePk) {
        this.reIdParticipantePk = reIdParticipantePk;
    }

    public List<Evaluador> getLstEvaluadores() {
        return lstEvaluadores;
    }

    public void setLstEvaluadores(List<Evaluador> lstEvaluadores) {
        this.lstEvaluadores = lstEvaluadores;
    }

    public List<Evaluador> getLstCargaMasivaEvaluadores() {
        return lstCargaMasivaEvaluadores;
    }

    public void setLstCargaMasivaEvaluadores(List<Evaluador> lstCargaMasivaEvaluadores) {
        this.lstCargaMasivaEvaluadores = lstCargaMasivaEvaluadores;
    }

    public Integer getCantidadEvaluadoresRegistrados() {
        return cantidadEvaluadoresRegistrados;
    }

    public void setCantidadEvaluadoresRegistrados(Integer cantidadEvaluadoresRegistrados) {
        this.cantidadEvaluadoresRegistrados = cantidadEvaluadoresRegistrados;
    }

    public UploadedFile getFileEvaluadores() {
        return fileEvaluadores;
    }

    public void setFileEvaluadores(UploadedFile fileEvaluadores) {
        this.fileEvaluadores = fileEvaluadores;
    }

    public StreamedContent getXlsContentEvaluadores() {
        return xlsContentEvaluadores;
    }

    public void setXlsContentEvaluadores(StreamedContent xlsContentEvaluadores) {
        this.xlsContentEvaluadores = xlsContentEvaluadores;
    }

    public Integer getIntCantTempCorrectEvaluadores() {
        return intCantTempCorrectEvaluadores;
    }

    public void setIntCantTempCorrectEvaluadores(Integer intCantTempCorrectEvaluadores) {
        this.intCantTempCorrectEvaluadores = intCantTempCorrectEvaluadores;
    }

    public Integer getIntCantTempIncorrectEvaluadores() {
        return intCantTempIncorrectEvaluadores;
    }

    public void setIntCantTempIncorrectEvaluadores(Integer intCantTempIncorrectEvaluadores) {
        this.intCantTempIncorrectEvaluadores = intCantTempIncorrectEvaluadores;
    }

    public Integer getIntCantTempIncorrect() {
        return intCantTempIncorrect;
    }

    public Integer getModo() {
        return modo;
    }

    public void setModo(Integer modo) {
        this.modo = modo;
    }

    public void setIntCantTempIncorrect(Integer intCantTempIncorrect) {
        this.intCantTempIncorrect = intCantTempIncorrect;
    }

    public Integer getIntCantTempCorrect() {
        return intCantTempCorrect;
    }

    public void setIntCantTempCorrect(Integer intCantTempCorrect) {
        this.intCantTempCorrect = intCantTempCorrect;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Integer getCantidadEvaluadosRegistrados() {
        return cantidadEvaluadosRegistrados;
    }

    public void setCantidadEvaluadosRegistrados(Integer cantidadEvaluadosRegistrados) {
        this.cantidadEvaluadosRegistrados = cantidadEvaluadosRegistrados;
    }

    public List<Evaluado> getLstCargaMasiva() {
        return lstCargaMasiva;
    }

    public void setLstCargaMasiva(List<Evaluado> lstCargaMasiva) {
        this.lstCargaMasiva = lstCargaMasiva;
    }

    public String getStrDescripcion() {
        return strDescripcion;
    }

    public void setStrDescripcion(String strDescripcion) {
        this.strDescripcion = strDescripcion;
    }

    public String getStrCargo() {
        return strCargo;
    }

    public void setStrCargo(String strCargo) {
        this.strCargo = strCargo;
    }

    public String getStrCorreo() {
        return strCorreo;
    }

    public void setStrCorreo(String strCorreo) {
        this.strCorreo = strCorreo;
    }

    public Integer getIdParticipantePk() {
        return idParticipantePk;
    }

    public void setIdParticipantePk(Integer idParticipantePk) {
        this.idParticipantePk = idParticipantePk;
    }

    public Integer getIntCorrelativo() {
        return intCorrelativo;
    }

    public void setIntCorrelativo(Integer intCorrelativo) {
        this.intCorrelativo = intCorrelativo;
    }

    public Boolean getPaInAutoevaluar() {
        return paInAutoevaluar;
    }

    public void setPaInAutoevaluar(Boolean paInAutoevaluar) {
        this.paInAutoevaluar = paInAutoevaluar;
    }

    public List<Evaluado> getLstEvaluado() {
        return lstEvaluado;
    }

    public void setLstEvaluado(List<Evaluado> lstEvaluado) {
        this.lstEvaluado = lstEvaluado;
    }

    public StreamedContent getXlsContent() {
        return xlsContent;
    }

    public void setXlsContent(StreamedContent xlsContent) {
        this.xlsContent = xlsContent;
    }

    public EvaluadosView() {
        this.lstEvaluado = new ArrayList<>();
    }

    @PostConstruct
    public void init() {

        habilitarParametros();

        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

        List<Participante> lstParticipantes = objParticipanteDAO.obtenListaParticipanteXProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        this.blCargarCorrectoAvan = false;
        this.lstRelacionesEvaluadoEvaluador = new ArrayList<>();
        this.lstEvaluado = new ArrayList<>();
        this.cantidadEvaluadosRegistrados = 0;
        this.paInAutoevaluar = true;
        this.lstAvanPersonas = new ArrayList<>();
        this.lstAvanRelacion = new ArrayList<>();
        this.lstRelacionAvanzadas = new ArrayList<>();
        this.lstErrorAvan = new ArrayList<>();

        if (!lstParticipantes.isEmpty()) {

            for (Participante objParticipante : lstParticipantes) {

                Evaluado evaluado = new Evaluado();

                evaluado.setPaIdParticipantePk(objParticipante.getPaIdParticipantePk());
                evaluado.setPaIdTipoParticipante(objParticipante.getPaIdTipoParticipante());
                evaluado.setPaInAutoevaluar(objParticipante.getPaInAutoevaluar());
                evaluado.setPaInRedCargada(objParticipante.getPaInRedCargada());
                evaluado.setPaInRedVerificada(objParticipante.getPaInRedVerificada());
                evaluado.setPaTxCorreo(objParticipante.getPaTxCorreo());
                evaluado.setPaTxDescripcion(objParticipante.getPaTxDescripcion());
                evaluado.setPaTxNombreCargo(objParticipante.getPaTxNombreCargo());
                evaluado.setPaIdEstado(objParticipante.getPaIdEstado());
                evaluado.setPaStrEstado(msg(objParticipante.getPaIdEstado().toString()));

                evaluado.setPaTxSexo(objParticipante.getPaTxSexo());
                evaluado.setPaNrEdad(objParticipante.getPaNrEdad());
                evaluado.setPaNrTiempoTrabajo(objParticipante.getPaNrTiempoTrabajo());
                evaluado.setPaTxOcupacion(objParticipante.getPaTxOcupacion());
                evaluado.setPaTxAreaNegocio(objParticipante.getPaTxAreaNegocio());
                evaluado.setPaTxImgUrl(objParticipante.getPaTxImgUrl());

                if (evaluado.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)) {
                    this.cantidadEvaluadosRegistrados++;
                }

                this.lstEvaluado.add(evaluado);

            }

        }

        /* Evaluadores */
        RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();

        List<RedEvaluacion> lstRedEvaluacion = objRedEvaluacionDAO.obtenListaRedEvaluacion(Utilitarios.obtenerProyecto().getIntIdProyecto());

        this.lstEvaluadores = new ArrayList<>();
        this.cantidadEvaluadoresRegistrados = 0;

        if (!lstRedEvaluacion.isEmpty()) {

            int i = 0;

            for (RedEvaluacion objRedEvaluacion : lstRedEvaluacion) {

                Evaluador evaluador = new Evaluador();

                evaluador.setReIdParticipantePk(objRedEvaluacion.getReIdParticipantePk());
                evaluador.setReTxDescripcion(objRedEvaluacion.getReTxDescripcion());
                evaluador.setReTxCorreo(objRedEvaluacion.getReTxCorreo());
                evaluador.setReTxNombreCargo(objRedEvaluacion.getReTxNombreCargo());
                evaluador.setReIdTipoParticipante(objRedEvaluacion.getReIdTipoParticipante());
                evaluador.setReIdEstado(objRedEvaluacion.getReIdEstado());
                evaluador.setReStrEstado(msg(objRedEvaluacion.getReIdEstado().toString()));
                evaluador.setReTxSexo(objRedEvaluacion.getReTxSexo());
                evaluador.setReNrEdad(objRedEvaluacion.getReNrEdad());
                evaluador.setReNrTiempoTrabajo(objRedEvaluacion.getReNrTiempoTrabajo());
                evaluador.setReTxOcupacion(objRedEvaluacion.getReTxOcupacion());
                evaluador.setReTxAreaNegocio(objRedEvaluacion.getReTxAreaNegocio());

                evaluador.setIntCorrelativo(i);
                i++;

                if (evaluador.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO)) {
                    this.cantidadEvaluadoresRegistrados++;
                }

                this.lstEvaluadores.add(evaluador);

            }

        }

        /**
         * **
         * CARGA RELACIONES
         */
        this.lstRelacionBean = new ArrayList();

        Proyecto proyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        intIdEstadoProyecto = proyecto.getPoIdEstado();

        RelacionDAO objRelacionDAO = new RelacionDAO();

        List<Relacion> lstRelacion = objRelacionDAO.obtenListaRelacionPorProyecto(proyecto);

        RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();

        for (Relacion obj : lstRelacion) {

            RelacionBean objRelacionBean = new RelacionBean();
            objRelacionBean.setStrNombre(obj.getReTxNombre());
            objRelacionBean.setStrAbreviatura(obj.getReTxAbreviatura());
            objRelacionBean.setStrColor(obj.getReColor());
            objRelacionBean.setStrDescripcion(obj.getReTxDescripcion());
            objRelacionBean.setIdRelacionPk(obj.getReIdRelacionPk());
            objRelacionBean.setIntIdEstado(obj.getReIdEstado());
            objRelacionBean.setStrEstado(msg(obj.getReIdEstado().toString()));

            objRelacionBean.setIntCantidadUso(objRelacionParticipanteDAO.existeRelacionesXRelacion(obj.getReIdRelacionPk()));

            lstRelacionBean.add(objRelacionBean);
        }

        if (!lstEvaluado.isEmpty() && !lstEvaluadores.isEmpty() && !lstRelacionBean.isEmpty()) {

            List lstRedCompleta = objParticipanteDAO.obtenerRedCompletaXProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

            Iterator itLstRedCompleta = lstRedCompleta.iterator();

            RelacionAvanzada objRelacionAvanzada;

            while (itLstRedCompleta.hasNext()) {

                Object obj[] = (Object[]) itLstRedCompleta.next();

                objRelacionAvanzada = new RelacionAvanzada();

                objRelacionAvanzada.setStrEvaluado(obj[1].toString());
                objRelacionAvanzada.setStrEvaluadoDesc(obj[0].toString());
                objRelacionAvanzada.setStrEvaluador(obj[3].toString());
                objRelacionAvanzada.setStrEvaluadorDesc(obj[2].toString());
                objRelacionAvanzada.setStrRelacion(obj[4].toString());
                objRelacionAvanzada.setStrRelacionAbreviatura(obj[5].toString());
                objRelacionAvanzada.setStrRelacionColor(obj[6].toString());

                lstRelacionesEvaluadoEvaluador.add(objRelacionAvanzada);
            }

        }

    }

    public void agregarEvaluado() {

        FacesContext context = FacesContext.getCurrentInstance();

        Integer error = buscarLista(this.strDescripcion, this.strCargo, this.strCorreo, this.paInAutoevaluar, false);

        Evaluado evaluado = new Evaluado();

        evaluado.setPaTxDescripcion(this.strDescripcion.trim());
        evaluado.setPaTxNombreCargo(this.strCargo.trim());
        evaluado.setPaTxCorreo(this.strCorreo.trim().toLowerCase());
        evaluado.setPaTxSexo(this.strSexo);
        evaluado.setPaNrEdad(this.intEdad);
        evaluado.setPaNrTiempoTrabajo(this.intTiempoEmpresa);
        evaluado.setPaTxOcupacion(this.strOcupacion);
        evaluado.setPaTxAreaNegocio(this.strAreaNegocio);
        evaluado.setPaInAutoevaluar(this.paInAutoevaluar);
        evaluado.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO);
        evaluado.setPaStrEstado(msg(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO.toString()));

        if (error == null) {

            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            evaluado.setPaIdParticipantePk(objParticipanteDAO.guardaParticipante(creaParticipante(evaluado)));

            this.lstEvaluado.add(evaluado);

            calculaIndicadores();

            mostrarAlertaInfo("step1.evaluado.agregado.correctamente", null);

            incluirComoEvaluador(evaluado.getPaTxDescripcion(), evaluado.getPaTxNombreCargo(), evaluado.getPaTxCorreo(), evaluado.getPaTxSexo(), evaluado.getPaNrEdad(), evaluado.getPaNrTiempoTrabajo(), evaluado.getPaTxOcupacion(), evaluado.getPaTxAreaNegocio(), false);

            this.resetFail();

        } else {
            evaluado = determinaError(evaluado, error);
            mostrarAlertaError(evaluado.getStrObservacionMasivo(), null);
        }

    }

    private void calculaIndicadores() {

        this.cantidadEvaluadosRegistrados = 0;

        for (Evaluado objEvaluado : lstEvaluado) {
            if (objEvaluado.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)) {
                this.cantidadEvaluadosRegistrados++;
            }
        }

    }

    private Participante creaParticipante(Evaluado objEvaluado) {

        Participante objParticipante = new Participante();

        objParticipante.setPaIdTipoParticipante(Constantes.INT_ET_TIPO_PARTICIPANTE_EVALUADO);
        objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO);
        objParticipante.setPaInAutoevaluar(objEvaluado.isPaInAutoevaluar());
        objParticipante.setPaInRedCargada(objEvaluado.isPaInRedCargada());
        objParticipante.setPaInRedVerificada(objEvaluado.isPaInRedVerificada());
        objParticipante.setPaTxCorreo(objEvaluado.getPaTxCorreo());
        objParticipante.setPaTxDescripcion(objEvaluado.getPaTxDescripcion());
        objParticipante.setPaTxNombreCargo(objEvaluado.getPaTxNombreCargo());
        objParticipante.setPaTxSexo(objEvaluado.getPaTxSexo());
        objParticipante.setPaNrEdad(objEvaluado.getPaNrEdad());
        objParticipante.setPaNrTiempoTrabajo(objEvaluado.getPaNrTiempoTrabajo());
        objParticipante.setPaTxOcupacion(objEvaluado.getPaTxOcupacion());
        objParticipante.setPaTxAreaNegocio(objEvaluado.getPaTxAreaNegocio());

        Proyecto objProyecto = new Proyecto();
        objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());

        objParticipante.setProyecto(objProyecto);

        return objParticipante;

    }

    public Integer buscarLista(String strDescripcion, String strCargo, String strCorreo, Boolean paInAutoevaluar, Boolean Masivo) {

        String strDesc = Utilitarios.retirarEspacios(strDescripcion);
        String strCorr = Utilitarios.retirarEspacios(strCorreo);

        for (Evaluado obj : lstEvaluado) {
            if (Utilitarios.retirarEspacios(obj.getPaTxDescripcion()).toUpperCase().equals(strDesc.toUpperCase())) {
                return 1; //"El evaluado ingresado ya se encuentra agregado";
            }
            if (Utilitarios.retirarEspacios(obj.getPaTxCorreo()).toUpperCase().equals(strCorr.toUpperCase())) {
                if (Masivo) {
                    if (obj.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)) {
                        return 2;//"Registro ya existe y será actualizado"
                    } else {
                        return 3;//"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                    }
                } else {
                    return 5; //"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                }
            }
        }

        return null;
    }

    public Integer buscarListaModifica(Integer idParticipante, String strDescripcion, String strCargo, String strCorreo, Boolean paInAutoevaluar, Boolean Masivo) {

        String strDesc = Utilitarios.retirarEspacios(strDescripcion);
        String strCorr = Utilitarios.retirarEspacios(strCorreo);

        for (Evaluado obj : lstEvaluado) {

            if (!idParticipante.equals(obj.getPaIdParticipantePk())) {
                if (Utilitarios.retirarEspacios(obj.getPaTxDescripcion()).toUpperCase().equals(strDesc.toUpperCase())) {
                    return 1; //"El evaluado ingresado ya se encuentra agregado";
                }
                if (Utilitarios.retirarEspacios(obj.getPaTxCorreo()).toUpperCase().equals(strCorr.toUpperCase())) {
                    if (Masivo) {
                        if (obj.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)) {
                            return 2;//"Registro ya existe y será actualizado"
                        } else {
                            return 3;//"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                        }
                    } else {
                        return 5; //"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                    }
                }
            }
        }

        return null;
    }

    public Integer validarParametros(String strSexo, String strEdad, String strTiempoEmpresa, String strOcupacion, String strAreaNegocio) {

        String strSX = strSexo;
        String strED = strEdad;
        String strTE = strTiempoEmpresa;
        String strOC = strOcupacion;
        String strAN = strAreaNegocio;

        /*SEXO*/
        if (blHabilitarSexo) {
            if (Utilitarios.esNuloOVacio(strSX)) {
                return 6;
            }
            if (!hSexo.containsKey(strSX.trim().toUpperCase())) {
                return 6;
            }
        }
        /*EDAD*/
        if (blHabilitarEdad) {
            if (Utilitarios.esNuloOVacio(strED)) {
                return 7;
            }
            if (!Utilitarios.isNumber(strED, false)) {
                return 7;
            }
            BigDecimal bl = new BigDecimal(strED);
            Integer edad = bl.intValue();
            if (edad <= 0) {
                return 7;
            }
        }
        /*TIEMPO EMPRESA*/
        if (blHabilitarTiempoEmpresa) {
            if (Utilitarios.esNuloOVacio(strTE)) {
                return 8;
            }
            if (!Utilitarios.isNumber(strTE, false)) {
                return 8;
            }
            BigDecimal bl = new BigDecimal(strTE);
            Integer tiempoEmpresa = bl.intValue();
            if (tiempoEmpresa <= 0) {
                return 8;
            }
        }
        /*NIVEL OCUPACIONAL*/
        if (blHabilitarNivelOcupacional) {
            if (Utilitarios.esNuloOVacio(strOC)) {
                return 9;
            }
            if (!hNO.containsKey(strOC.trim().toUpperCase())) {
                return 9;
            }
        }
        if (blHabilitarAreaNegocio) {
            /*AREA NEGOCIO*/
            if (Utilitarios.esNuloOVacio(strAN)) {
                return 10;
            }
            if (!hAN.containsKey(strAN.trim().toUpperCase())) {
                return 10;
            }
        }

        return null;
    }

    public void resetFail() {
        this.strDescripcion = Constantes.strVacio;
        this.strCargo = Constantes.strVacio;
        this.strCorreo = Constantes.strVacio;
        this.strSexo = Constantes.strVacio;
        this.intEdad = null;
        this.intTiempoEmpresa = null;
        this.strOcupacion = Constantes.strVacio;
        this.strAreaNegocio = Constantes.strVacio;
        this.idParticipantePk = null;
        this.paInAutoevaluar = Boolean.TRUE;
        this.intCorrelativo = null;
        this.lstCargaMasiva = new ArrayList();
        this.file = null;
        this.xlsContent = null;

        this.strDescripcionEvaluadores = Constantes.strVacio;
        this.strCargoEvaluadores = Constantes.strVacio;
        this.strCorreoEvaluadores = Constantes.strVacio;
        this.strSexoEvaluadores = Constantes.strVacio;
        this.intEdadEvaluadores = null;
        this.intTiempoEmpresaEvaluadores = null;
        this.strOcupacionEvaluadores = Constantes.strVacio;
        this.strAreaNegocioEvaluadores = Constantes.strVacio;
        this.reIdParticipantePk = null;
        this.intCorrelativoEvaluadores = null;
        this.lstCargaMasivaEvaluadores = new ArrayList();
        this.fileEvaluadores = null;
        this.xlsContentEvaluadores = null;
    }

    public void eliminarLista(Evaluado objEvaluado) {

        int i = 0;
        boolean blEncontro = false;

        for (Evaluado obj : this.lstEvaluado) {
            if (obj.getPaIdParticipantePk().equals(objEvaluado.getPaIdParticipantePk())) {
                blEncontro = true;
                break;
            }
            i++;
        }

        if (blEncontro) {

            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            Participante objParticipante = objParticipanteDAO.obtenParticipante(objEvaluado.getPaIdParticipantePk());

            objParticipanteDAO.eliminaParticipanteRelaciones(objParticipante);

            this.cantidadEvaluadosRegistrados--;
            this.lstEvaluado.remove(i);

        }

        mostrarAlertaInfo("step1.evaluado.borrado.correctamente", null);

        resetFail();
    }

    public void grabarLista() {

        try {

            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
            boolean correcto = objParticipanteDAO.guardaParticipante(this.lstEvaluado, Utilitarios.obtenerProyecto().getIntIdProyecto());

            if (correcto) {
                msg("success", null);
            } else {
                msg("error.saving", null);
            }

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public void modificarEvaluado(Evaluado objEvaluado) {

        this.modo = 1;

        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Modificar evaluado",
                "Cualquier cambio será actualizado en la lista de evaluados"));

        this.strDescripcion = objEvaluado.getPaTxDescripcion();
        this.strCargo = objEvaluado.getPaTxNombreCargo();
        this.strCorreo = objEvaluado.getPaTxCorreo();
        this.strSexo = objEvaluado.getPaTxSexo();
        this.intEdad = objEvaluado.getPaNrEdad();
        this.intTiempoEmpresa = objEvaluado.getPaNrTiempoTrabajo();
        this.strOcupacion = objEvaluado.getPaTxOcupacion();
        this.strAreaNegocio = objEvaluado.getPaTxAreaNegocio();
        this.idParticipantePk = objEvaluado.getPaIdParticipantePk();
        this.paInAutoevaluar = objEvaluado.isPaInAutoevaluar();

    }

    public void generaExcel() {

        HSSFWorkbook xlsEvaluados = new HSSFWorkbook();

        HSSFSheet hoja = xlsEvaluados.createSheet(msg("evaluated", null));

        HSSFRow row = hoja.createRow(0);

        int c = 0;

        HSSFCell cell0 = row.createCell(c);
        HSSFRichTextString texto0 = new HSSFRichTextString(msg("description", null));
        cell0.setCellValue(texto0);

        c++;

        HSSFCell cell1 = row.createCell(c);
        HSSFRichTextString texto1 = new HSSFRichTextString(msg("work.range", null));
        cell1.setCellValue(texto1);

        c++;

        HSSFCell cell2 = row.createCell(c);
        HSSFRichTextString texto2 = new HSSFRichTextString(msg("email", null));
        cell2.setCellValue(texto2);

        if (blHabilitarSexo) {
            c++;
            HSSFCell cell3 = row.createCell(c);
            HSSFRichTextString texto3 = new HSSFRichTextString(msg("sex", null));
            cell3.setCellValue(texto3);
        }

        if (blHabilitarEdad) {
            c++;
            HSSFCell cell4 = row.createCell(c);
            HSSFRichTextString texto4 = new HSSFRichTextString(msg("age", null));
            cell4.setCellValue(texto4);
        }

        if (blHabilitarTiempoEmpresa) {
            c++;
            HSSFCell cell5 = row.createCell(c);
            HSSFRichTextString texto5 = new HSSFRichTextString(msg("hiring.time", null));
            cell5.setCellValue(texto5);
        }

        if (blHabilitarNivelOcupacional) {
            c++;
            HSSFCell cell6 = row.createCell(c);
            HSSFRichTextString texto6 = new HSSFRichTextString(msg("work.range", null));
            cell6.setCellValue(texto6);
        }

        if (blHabilitarAreaNegocio) {
            c++;
            HSSFCell cell7 = row.createCell(c);
            HSSFRichTextString texto7 = new HSSFRichTextString(msg("working.area", null));
            cell7.setCellValue(texto7);
        }

        c++;
        HSSFCell cell8 = row.createCell(c);
        HSSFRichTextString texto8 = new HSSFRichTextString(msg("autoevaluation", null));
        cell8.setCellValue(texto8);

        HSSFCellStyle myStyle = xlsEvaluados.createCellStyle();

        HSSFFont hSSFFont = xlsEvaluados.createFont();
        hSSFFont.setBold(true);

        myStyle.setFont(hSSFFont);

        row.setRowStyle(myStyle);

        int i = 1;
        for (Evaluado objEvaluado : lstEvaluado) {

            if (objEvaluado.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)) {
                HSSFRow nextrow = hoja.createRow(i);

                int r = 0;

                nextrow.createCell(r).setCellValue(objEvaluado.getPaTxDescripcion());
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.getPaTxNombreCargo());
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.getPaTxCorreo());

                if (blHabilitarSexo) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluado.getPaTxSexo());
                }
                if (blHabilitarEdad) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluado.getPaNrEdad());
                }
                if (blHabilitarTiempoEmpresa) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluado.getPaNrTiempoTrabajo());
                }
                if (blHabilitarNivelOcupacional) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluado.getPaTxOcupacion());
                }
                if (blHabilitarAreaNegocio) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluado.getPaTxAreaNegocio());
                }
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.isPaInAutoevaluar() == true ? msg("a.indicator.autoevaluation") : null);
                i++;
            }

        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
        hoja.autoSizeColumn(2);
        hoja.autoSizeColumn(3);
        hoja.autoSizeColumn(4);
        hoja.autoSizeColumn(5);
        hoja.autoSizeColumn(6);
        hoja.autoSizeColumn(7);
        hoja.autoSizeColumn(8);

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + msg("evaluated", null) + ".xls\"");

            xlsEvaluados.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void generaExcelRespuesta() {

        HSSFWorkbook xlsEvaluados = new HSSFWorkbook();

        HSSFSheet hoja = xlsEvaluados.createSheet(msg("evaluated", null));

        HSSFRow row = hoja.createRow(0);

        int c = 0;

        HSSFCell cell0 = row.createCell(c);
        HSSFRichTextString texto0 = new HSSFRichTextString(msg("description", null));
        cell0.setCellValue(texto0);

        c++;
        HSSFCell cell1 = row.createCell(c);
        HSSFRichTextString texto1 = new HSSFRichTextString(msg("work.range", null));
        cell1.setCellValue(texto1);

        c++;
        HSSFCell cell2 = row.createCell(c);
        HSSFRichTextString texto2 = new HSSFRichTextString(msg("mail", null));
        cell2.setCellValue(texto2);

        if (blHabilitarSexo) {
            c++;
            HSSFCell cell3 = row.createCell(c);
            HSSFRichTextString texto3 = new HSSFRichTextString(msg("sex", null));
            cell3.setCellValue(texto3);
        }

        if (blHabilitarEdad) {
            c++;
            HSSFCell cell4 = row.createCell(c);
            HSSFRichTextString texto4 = new HSSFRichTextString(msg("age", null));
            cell4.setCellValue(texto4);
        }

        if (blHabilitarTiempoEmpresa) {
            c++;
            HSSFCell cell5 = row.createCell(c);
            HSSFRichTextString texto5 = new HSSFRichTextString(msg("hiring.time", null));
            cell5.setCellValue(texto5);
        }

        if (blHabilitarNivelOcupacional) {
            c++;
            HSSFCell cell6 = row.createCell(c);
            HSSFRichTextString texto6 = new HSSFRichTextString(msg("work.range", null));
            cell6.setCellValue(texto6);
        }

        if (blHabilitarAreaNegocio) {
            c++;
            HSSFCell cell7 = row.createCell(c);
            HSSFRichTextString texto7 = new HSSFRichTextString(msg("working.area", null));
            cell7.setCellValue(texto7);
        }

        c++;
        HSSFCell cell8 = row.createCell(c);
        HSSFRichTextString texto8 = new HSSFRichTextString(msg("autoevaluate", null));
        cell8.setCellValue(texto8);

        c++;
        HSSFCell cell9 = row.createCell(c);
        HSSFRichTextString texto9 = new HSSFRichTextString(msg("observation", null));
        cell9.setCellValue(texto9);

        HSSFCellStyle myStyle = xlsEvaluados.createCellStyle();

        HSSFFont hSSFFont = xlsEvaluados.createFont();
        hSSFFont.setBold(true);

        myStyle.setFont(hSSFFont);

        row.setRowStyle(myStyle);

        int i = 1;
        for (Evaluado objEvaluado : lstCargaMasiva) {

            int r = 0;

            HSSFRow nextrow = hoja.createRow(i);
            nextrow.createCell(r).setCellValue(objEvaluado.getPaTxDescripcion());
            r++;
            nextrow.createCell(r).setCellValue(objEvaluado.getPaTxNombreCargo());
            r++;
            nextrow.createCell(r).setCellValue(objEvaluado.getPaTxCorreo());

            if (blHabilitarSexo) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.getPaTxSexo());
            }
            if (blHabilitarEdad) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.getPaNrEdad());
            }
            if (blHabilitarTiempoEmpresa) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.getPaNrTiempoTrabajo());
            }
            if (blHabilitarNivelOcupacional) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.getPaTxOcupacion());
            }
            if (blHabilitarAreaNegocio) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluado.getPaTxAreaNegocio());
            }

            r++;
            nextrow.createCell(r).setCellValue(objEvaluado.isPaInAutoevaluar() == true ? msg("a.indicator.autoevaluation") : null);
            r++;
            nextrow.createCell(r).setCellValue(objEvaluado.getStrObservacionMasivo());
            i++;

        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
        hoja.autoSizeColumn(2);
        hoja.autoSizeColumn(3);
        hoja.autoSizeColumn(4);
        hoja.autoSizeColumn(5);
        hoja.autoSizeColumn(6);
        hoja.autoSizeColumn(7);
        hoja.autoSizeColumn(8);
        hoja.autoSizeColumn(9);
        hoja.autoSizeColumn(10);

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + msg("evaluated", null) + ".xls\"");

            xlsEvaluados.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void leeExcel(FileUploadEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();

        intCantTempCorrect = 0;
        intCantTempIncorrect = 0;

        if (event.getFile() == null) {
            mostrarAlertaError("file.empty", null);
        } else {

            HSSFWorkbook xlsEvaluados = null;

            try {

                xlsEvaluados = new HSSFWorkbook(event.getFile().getInputStream());

                HSSFSheet sheet = xlsEvaluados.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();

                rowIterator.next();

                lstCargaMasiva = new ArrayList();

                Evaluado objEvaluado;

                while (rowIterator.hasNext()) {

                    Row row = rowIterator.next();

                    int c = 0;

                    String strDescripcion;
                    strDescripcion = Utilitarios.obtieneDatoCelda(row, c);

                    String strCargo;
                    c++;
                    strCargo = Utilitarios.obtieneDatoCelda(row, c);

                    String strCorreo;
                    c++;
                    strCorreo = Utilitarios.obtieneDatoCelda(row, c);

                    String strSexo = null;
                    if (blHabilitarSexo) {
                        c++;
                        strSexo = Utilitarios.obtieneDatoCelda(row, c);
                    }

                    String strEdad = null;
                    if (blHabilitarEdad) {
                        c++;
                        strEdad = Utilitarios.obtieneDatoCelda(row, c);
                    }

                    String strTiempoEmpresa = null;
                    if (blHabilitarTiempoEmpresa) {
                        c++;
                        strTiempoEmpresa = Utilitarios.obtieneDatoCelda(row, c);;
                    }

                    String strOcupacion = null;
                    if (blHabilitarNivelOcupacional) {
                        c++;
                        strOcupacion = Utilitarios.obtieneDatoCelda(row, c);
                    }

                    String strAreaNegocio = null;
                    if (blHabilitarAreaNegocio) {
                        c++;
                        strAreaNegocio = Utilitarios.obtieneDatoCelda(row, c);
                    }

                    String strAutoevaluar;
                    c++;
                    strAutoevaluar = Utilitarios.obtieneDatoCelda(row, c);

                    objEvaluado = new Evaluado();

                    objEvaluado.setPaTxDescripcion(strDescripcion);
                    objEvaluado.setPaTxNombreCargo(strCargo);
                    objEvaluado.setPaTxCorreo(strCorreo);
                    objEvaluado.setPaTxSexo(strSexo);

                    if (Utilitarios.noEsNuloOVacio(strEdad)) {
                        if (Utilitarios.isNumber(strEdad, false)) {
                            BigDecimal bd = new BigDecimal(strEdad);
                            objEvaluado.setPaNrEdad(bd.intValue());
                        } else {
                            objEvaluado.setPaNrEdad(0);
                        }
                    } else {
                        objEvaluado.setPaNrEdad(0);
                    }

                    if (Utilitarios.noEsNuloOVacio(strTiempoEmpresa)) {
                        if (Utilitarios.isNumber(strTiempoEmpresa, false)) {
                            BigDecimal bd = new BigDecimal(strTiempoEmpresa);
                            objEvaluado.setPaNrTiempoTrabajo(bd.intValue());
                        } else {
                            objEvaluado.setPaNrTiempoTrabajo(0);
                        }
                    } else {
                        objEvaluado.setPaNrTiempoTrabajo(0);
                    }

                    objEvaluado.setPaTxOcupacion(strOcupacion);
                    objEvaluado.setPaTxAreaNegocio(strAreaNegocio);

                    if (Utilitarios.noEsNuloOVacio(strAutoevaluar) && strAutoevaluar.toUpperCase().equals(msg("a.indicator.autoevaluation").toUpperCase())) {
                        objEvaluado.setPaInAutoevaluar(true);
                    } else {
                        objEvaluado.setPaInAutoevaluar(false);
                    }

                    Integer error;

                    error = buscarLista(strDescripcion, strCargo, strCorreo, null, true);

                    if (Utilitarios.esNuloOVacio(error)) {
                        error = buscarListaTemporal(strDescripcion, strCorreo);
                    }

                    if (Utilitarios.esNuloOVacio(error)) {
                        error = validarParametros(strSexo, strEdad, strTiempoEmpresa, strOcupacion, strAreaNegocio);
                    }

                    if (Utilitarios.esNuloOVacio(error)) {
                        intCantTempCorrect++;
                        objEvaluado.setStrCorrectoMasivo("good");
                    } else {
                        intCantTempIncorrect++;
                        objEvaluado = determinaError(objEvaluado, error);
                    }

                    lstCargaMasiva.add(objEvaluado);

                }

                file = null;

            } catch (IOException e) {
                mostrarError(log, e);
                mostrarAlertaError("file.error");
            } catch (NoSuchElementException e) {
                mostrarError(log, e);
                mostrarAlertaError("file.structure.incorrect");
            } catch (NullPointerException e) {
                mostrarError(log, e);
                mostrarAlertaError("file.data.incomplete");
            }
        }
    }

    private Evaluado determinaError(Evaluado objEvaluado, Integer error) {

        String icon = null, mensaje = null;

        switch (error) {
            case 0:
                icon = "error";
                mensaje = msg("error.cell.empty");
                break;
            case 1:
                icon = "error";
                mensaje = msg("error.evaluated.duplicated");
                break;
            case 2:
                icon = "alert";
                mensaje = msg("error.evaluated.duplicated.rewrited");
                break;
            case 3:
                icon = "error";
                mensaje = msg("error.evaluated.duplicated.rewrited");
                break;
            case 4:
                icon = "error";
                mensaje = msg("error.email.invalid");
                break;
            case 5:
                icon = "error";
                mensaje = msg("error.email.registrered.previously");
                break;
            case 6:
                icon = "error";
                mensaje = msg("error.sex.invalid");
                break;
            case 7:
                icon = "error";
                mensaje = msg("error.age.invalid");
                break;
            case 8:
                icon = "error";
                mensaje = msg("error.hiring.time.invalid");
                break;
            case 9:
                icon = "error";
                mensaje = msg("error.work.range.invalid");
                break;
            case 10:
                icon = "error";
                mensaje = msg("error.working.area.invalid");
                break;
            default:
                break;
        }

        objEvaluado.setStrCorrectoMasivo(icon);
        objEvaluado.setStrObservacionMasivo(mensaje);

        return objEvaluado;

    }

    public Integer buscarListaTemporal(String strDescripcion, String strCorreo) {

        String strDesc = Utilitarios.retirarEspacios(strDescripcion);
        String strCorr = Utilitarios.retirarEspacios(strCorreo);

        if (Utilitarios.esNuloOVacio(strCorr) || Utilitarios.esNuloOVacio(strDesc)) {
            return 0;//"Contiene datos vacios";
        }

        for (Evaluado obj : lstCargaMasiva) {
            if (Utilitarios.retirarEspacios(obj.getPaTxDescripcion()).toUpperCase().equals(strDesc.toUpperCase())) {
                return 1;//"El evaluado ingresado ya se encuentra agregado";
            }
            if (Utilitarios.retirarEspacios(obj.getPaTxCorreo()).toUpperCase().equals(strCorr.toUpperCase())) {
                return 3;//"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
            }
        }

        if (!Pattern.compile(Constantes.EMAIL_PATTERN).matcher(strCorr).matches()) {
            return 4;//"No es un correo electronico";
        }
        return null;

    }

    public void cargarListaEvaluados() {

        FacesContext context = FacesContext.getCurrentInstance();

        int countEvaluadorReg = 0;

        try {

            for (Evaluado objEvaluado : lstCargaMasiva) {

                if (objEvaluado.getStrCorrectoMasivo().equals("good")) {

                    Integer error = buscarLista(objEvaluado.getPaTxDescripcion(), objEvaluado.getPaTxNombreCargo(), objEvaluado.getPaTxCorreo(), objEvaluado.isPaInAutoevaluar(), false);

                    Evaluado nuevoEvaluado = new Evaluado();

                    nuevoEvaluado.setPaTxDescripcion(objEvaluado.getPaTxDescripcion());
                    nuevoEvaluado.setPaTxNombreCargo(objEvaluado.getPaTxNombreCargo());
                    nuevoEvaluado.setPaTxCorreo(objEvaluado.getPaTxCorreo());
                    nuevoEvaluado.setPaTxSexo(objEvaluado.getPaTxSexo());
                    nuevoEvaluado.setPaNrEdad(objEvaluado.getPaNrEdad());
                    nuevoEvaluado.setPaNrTiempoTrabajo(objEvaluado.getPaNrTiempoTrabajo());
                    nuevoEvaluado.setPaTxOcupacion(objEvaluado.getPaTxOcupacion());
                    nuevoEvaluado.setPaTxAreaNegocio(objEvaluado.getPaTxAreaNegocio());
                    nuevoEvaluado.setPaInAutoevaluar(objEvaluado.isPaInAutoevaluar());
                    nuevoEvaluado.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO);
                    nuevoEvaluado.setPaStrEstado(msg(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO.toString()));

                    if (error == null) {

                        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
                        nuevoEvaluado.setPaIdParticipantePk(objParticipanteDAO.guardaParticipante(creaParticipante(nuevoEvaluado)));
                        objEvaluado.setStrObservacionMasivo(msg("processed"));

                        this.lstEvaluado.add(nuevoEvaluado);

                        boolean good = incluirComoEvaluador(nuevoEvaluado.getPaTxDescripcion(), nuevoEvaluado.getPaTxNombreCargo(), nuevoEvaluado.getPaTxCorreo(), nuevoEvaluado.getPaTxSexo(), nuevoEvaluado.getPaNrEdad(), nuevoEvaluado.getPaNrTiempoTrabajo(), nuevoEvaluado.getPaTxOcupacion(), nuevoEvaluado.getPaTxAreaNegocio(), true);

                        if (good) {
                            countEvaluadorReg++;
                        }

                    } else {
                        objEvaluado = determinaError(objEvaluado, error);
                    }

                }
            }
            if (countEvaluadorReg > 0) {
                mostrarAlertaInfo("info.add.verify");
            } else {
                mostrarAlertaError("error.evaluted.not.saved");
            }

            calculaIndicadores();

            intCantTempCorrect = 0;

            mostrarAlertaInfo("process.upload.was.ok");

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void actualizarEvaluado() {
        strCorreo = strCorreo.toLowerCase();
        Integer error = buscarListaModifica(idParticipantePk, strDescripcion, strCargo, strCorreo, paInAutoevaluar, false);

        String correoAnterior;
        if (error == null) {

            ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();

            Participante objParticipante = objParticipanteDAO.obtenParticipante(this.idParticipantePk);

            correoAnterior = objParticipante.getPaTxCorreo().toLowerCase();

            objParticipante.setPaTxDescripcion(this.strDescripcion);
            objParticipante.setPaTxNombreCargo(this.strCargo);
            objParticipante.setPaTxCorreo(this.strCorreo);
            objParticipante.setPaTxSexo(this.strSexo);
            objParticipante.setPaNrEdad(this.intEdad);
            objParticipante.setPaNrTiempoTrabajo(this.intTiempoEmpresa);
            objParticipante.setPaTxOcupacion(this.strOcupacion);
            objParticipante.setPaTxAreaNegocio(this.strAreaNegocio);
            objParticipante.setPaInAutoevaluar(this.paInAutoevaluar);

            if (!correoAnterior.equals(this.strCorreo)) {
                objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO);
            }

            objParticipanteDAO.actualizaParticipante(objParticipante);

            for (Evaluado objEvaluado : lstEvaluado) {

                if (objEvaluado.getPaIdParticipantePk().equals(this.idParticipantePk)) {

                    objEvaluado.setPaTxDescripcion(this.strDescripcion);
                    objEvaluado.setPaTxNombreCargo(this.strCargo);
                    objEvaluado.setPaTxCorreo(this.strCorreo);
                    objEvaluado.setPaTxSexo(this.strSexo);
                    objEvaluado.setPaNrEdad(this.intEdad);
                    objEvaluado.setPaNrTiempoTrabajo(this.intTiempoEmpresa);
                    objEvaluado.setPaTxOcupacion(this.strOcupacion);
                    objEvaluado.setPaTxAreaNegocio(this.strAreaNegocio);
                    objEvaluado.setPaInAutoevaluar(this.paInAutoevaluar);
                    objEvaluado.setPaIdEstado(objParticipante.getPaIdEstado());
                    objEvaluado.setPaStrEstado(msg(objParticipante.getPaIdEstado().toString()));
                }

            }

            mostrarAlertaInfo("updated");

            for (Evaluador objEvaluador : lstEvaluadores) {

                if (objEvaluador.getReTxCorreo().toLowerCase().equals(correoAnterior)) {

                    RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();

                    RedEvaluacion objRedEvaluacion = objRedEvaluacionDAO.obtenRedEvaluacion(objEvaluador.getReIdParticipantePk());

                    if (!this.strCorreo.equals(objRedEvaluacion.getReTxCorreo().toLowerCase())) {
                        objRedEvaluacion.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO);
                    }

                    objRedEvaluacion.setReTxDescripcion(this.strDescripcion);
                    objRedEvaluacion.setReTxNombreCargo(this.strCargo);
                    objRedEvaluacion.setReTxCorreo(this.strCorreo);
                    objRedEvaluacion.setReTxSexo(this.strSexo);
                    objRedEvaluacion.setReNrEdad(this.intEdad);
                    objRedEvaluacion.setReNrTiempoTrabajo(this.intTiempoEmpresa);
                    objRedEvaluacion.setReTxOcupacion(this.strOcupacion);
                    objRedEvaluacion.setReTxAreaNegocio(this.strAreaNegocio);

                    objRedEvaluacionDAO.actualizaRedEvaluacion(objRedEvaluacion);

                    objEvaluador.setReTxDescripcion(this.strDescripcion);
                    objEvaluador.setReTxNombreCargo(this.strCargo);
                    objEvaluador.setReTxCorreo(this.strCorreo);
                    objEvaluador.setReTxSexo(this.strSexo);
                    objEvaluador.setReNrEdad(this.intEdad);
                    objEvaluador.setReNrTiempoTrabajo(this.intTiempoEmpresa);
                    objEvaluador.setReTxOcupacion(this.strOcupacion);
                    objEvaluador.setReTxAreaNegocio(this.strAreaNegocio);
                    objEvaluador.setReIdEstado(objRedEvaluacion.getReIdEstado());
                    objEvaluador.setReStrEstado(msg(objRedEvaluacion.getReIdEstado().toString()));

                    mostrarAlertaInfo("evaluated.was.updated");

                }

            }

            resetFail();

        } else {
            Evaluado objEvaluado = new Evaluado();
            objEvaluado = determinaError(objEvaluado, error);
            mostrarAlertaError(objEvaluado.getStrObservacionMasivo());

        }

    }

    public boolean agregarEvaluadores(boolean esMasivoEvaluado) {

        Evaluador evaluador;

        Integer error = buscarListaEvaluadores(this.strDescripcionEvaluadores, this.strCargoEvaluadores, this.strCorreoEvaluadores.toLowerCase(), false);

        evaluador = new Evaluador();

        evaluador.setIntCorrelativo(this.lstEvaluadores.size());
        evaluador.setReTxDescripcion(this.strDescripcionEvaluadores.trim());
        evaluador.setReTxNombreCargo(this.strCargoEvaluadores.trim());
        evaluador.setReTxCorreo(this.strCorreoEvaluadores.trim().toLowerCase());
        evaluador.setReTxSexo(this.strSexoEvaluadores.trim());
        evaluador.setReNrEdad(this.intEdadEvaluadores);
        evaluador.setReNrTiempoTrabajo(this.intTiempoEmpresaEvaluadores);
        evaluador.setReTxOcupacion(this.strOcupacionEvaluadores.trim());
        evaluador.setReTxAreaNegocio(this.strAreaNegocioEvaluadores.trim());
        evaluador.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO);
        evaluador.setReStrEstado(msg(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO.toString()));

        if (error == null) {

            RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
            evaluador.setReIdParticipantePk(objRedEvaluacionDAO.guardaRedEvaluacion(creaRedEvaluacionEvaluadores(evaluador)));

            this.lstEvaluadores.add(evaluador);

            calculaIndicadores();

            if (!esMasivoEvaluado) {
                mostrarAlertaInfo("evaluator.added.correctly");
            }
            this.resetFail();

            return true;

        } else {
            evaluador = determinaErrorEvaluadores(evaluador, error);
            if (!esMasivoEvaluado) {
                mostrarAlertaError(evaluador.getStrObservacionMasivo());
            }
            return false;
        }

    }

    public Integer buscarListaEvaluadores(Integer idRedEvaluacion, String strDescripcion, String strCargo, String strCorreo, Boolean Masivo) {

        String strDesc = Utilitarios.retirarEspacios(strDescripcion);
        String strCorr = Utilitarios.retirarEspacios(strCorreo);

        for (Evaluador obj : lstEvaluadores) {
            if (!idRedEvaluacion.equals(obj.getReIdParticipantePk())) {

                if (Utilitarios.retirarEspacios(obj.getReTxDescripcion()).toUpperCase().equals(strDesc.toUpperCase())) {
                    return 1; //"El evaluado ingresado ya se encuentra agregado";
                }
                if (Utilitarios.retirarEspacios(obj.getReTxCorreo()).toUpperCase().equals(strCorr.toUpperCase())) {
                    if (Masivo) {
                        if (obj.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO)) {
                            return 2;//"Registro ya existe y será actualizado"
                        } else {
                            return 3;//"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                        }
                    } else {
                        return 5; //"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                    }
                }
            }
        }

        return null;
    }

    public Integer buscarListaEvaluadores(String strDescripcion, String strCargo, String strCorreo, Boolean Masivo) {

        String strDesc = Utilitarios.retirarEspacios(strDescripcion);
        String strCorr = Utilitarios.retirarEspacios(strCorreo);

        for (Evaluador obj : lstEvaluadores) {

            if (Utilitarios.retirarEspacios(obj.getReTxDescripcion()).toUpperCase().equals(strDesc.toUpperCase())) {
                return 1; //"El evaluado ingresado ya se encuentra agregado";
            }
            if (Utilitarios.retirarEspacios(obj.getReTxCorreo()).toUpperCase().equals(strCorr.toUpperCase())) {
                if (Masivo) {
                    if (obj.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO)) {
                        return 2;//"Registro ya existe y será actualizado"
                    } else {
                        return 3;//"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                    }
                } else {
                    return 5; //"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
                }
            }

        }

        return null;
    }

    public void resetFailEvaluadores() {
        this.strDescripcionEvaluadores = Constantes.strVacio;
        this.strCargoEvaluadores = Constantes.strVacio;
        this.strCorreoEvaluadores = Constantes.strVacio;

        this.strSexoEvaluadores = Constantes.strVacio;
        this.intEdadEvaluadores = null;
        this.intTiempoEmpresaEvaluadores = null;
        this.strOcupacionEvaluadores = Constantes.strVacio;
        this.strAreaNegocioEvaluadores = Constantes.strVacio;

        this.reIdParticipantePk = null;
        this.intCorrelativoEvaluadores = null;
        this.lstCargaMasivaEvaluadores = new ArrayList();
        this.fileEvaluadores = null;
        this.xlsContentEvaluadores = null;
    }

    public void eliminarListaEvaluadores(Evaluador objEvaluador) {

        int i = 0;
        boolean blEncontro = false;

        for (Evaluador obj : this.lstEvaluadores) {
            if (obj.getReIdParticipantePk().equals(objEvaluador.getReIdParticipantePk())) {
                blEncontro = true;
                break;
            }
            i++;
        }

        if (blEncontro) {

            RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
            RedEvaluacion objRedEvaluacion = objRedEvaluacionDAO.obtenRedEvaluacion(objEvaluador.getReIdParticipantePk());

            objRedEvaluacionDAO.eliminaRedEvaluacion(objRedEvaluacion);

            this.cantidadEvaluadoresRegistrados--;
            this.lstEvaluadores.remove(i);

        }
        mostrarAlertaInfo("deleted");

        resetFail();

    }

    public void grabarListaEvaluador() {

        try {

            RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
            boolean correcto = objRedEvaluacionDAO.guardaRedEvaluacion(this.lstEvaluadores, Utilitarios.obtenerProyecto().getIntIdProyecto());

            if (correcto) {
                init();
                mostrarAlertaInfo("success");
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public void modificarEvaluador(Evaluador objEvaluador) {

        this.modoEvaluadores = 1;

        this.strDescripcionEvaluadores = objEvaluador.getReTxDescripcion();
        this.strCargoEvaluadores = objEvaluador.getReTxNombreCargo();
        this.strCorreoEvaluadores = objEvaluador.getReTxCorreo();
        this.strSexoEvaluadores = objEvaluador.getReTxSexo();
        this.intEdadEvaluadores = objEvaluador.getReNrEdad();
        this.intTiempoEmpresaEvaluadores = objEvaluador.getReNrTiempoTrabajo();
        this.strOcupacionEvaluadores = objEvaluador.getReTxOcupacion();
        this.strAreaNegocioEvaluadores = objEvaluador.getReTxAreaNegocio();
        this.reIdParticipantePk = objEvaluador.getReIdParticipantePk();

    }

    public void generaExcelEvaluadores() {

        HSSFWorkbook xlsEvaluadores = new HSSFWorkbook();

        HSSFSheet hoja = xlsEvaluadores.createSheet(msg("evaluator"));

        HSSFRow row = hoja.createRow(0);

        int c = 0;

        HSSFCell cell0 = row.createCell(c);
        HSSFRichTextString texto0 = new HSSFRichTextString(msg("description"));
        cell0.setCellValue(texto0);

        c++;
        HSSFCell cell1 = row.createCell(c);
        HSSFRichTextString texto1 = new HSSFRichTextString(msg("work.range"));
        cell1.setCellValue(texto1);

        c++;
        HSSFCell cell2 = row.createCell(c);
        HSSFRichTextString texto2 = new HSSFRichTextString(msg("email"));
        cell2.setCellValue(texto2);

        if (blHabilitarSexo) {
            c++;
            HSSFCell cell3 = row.createCell(c);
            HSSFRichTextString texto3 = new HSSFRichTextString(msg("sex"));
            cell3.setCellValue(texto3);
        }

        if (blHabilitarEdad) {
            c++;
            HSSFCell cell4 = row.createCell(c);
            HSSFRichTextString texto4 = new HSSFRichTextString(msg("age"));
            cell4.setCellValue(texto4);
        }

        if (blHabilitarTiempoEmpresa) {
            c++;
            HSSFCell cell5 = row.createCell(c);
            HSSFRichTextString texto5 = new HSSFRichTextString(msg("hiring.time"));
            cell5.setCellValue(texto5);
        }

        if (blHabilitarNivelOcupacional) {
            c++;
            HSSFCell cell6 = row.createCell(c);
            HSSFRichTextString texto6 = new HSSFRichTextString(msg("work.range"));
            cell6.setCellValue(texto6);
        }

        if (blHabilitarAreaNegocio) {
            c++;
            HSSFCell cell7 = row.createCell(c);
            HSSFRichTextString texto7 = new HSSFRichTextString(msg("working.area"));
            cell7.setCellValue(texto7);
        }

        HSSFCellStyle myStyle = xlsEvaluadores.createCellStyle();

        HSSFFont hSSFFont = xlsEvaluadores.createFont();
        hSSFFont.setBold(true);

        myStyle.setFont(hSSFFont);

        row.setRowStyle(myStyle);

        int i = 1;
        for (Evaluador objEvaluador : lstEvaluadores) {

            if (objEvaluador.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO)) {
                HSSFRow nextrow = hoja.createRow(i);
                int r = 0;
                nextrow.createCell(r).setCellValue(objEvaluador.getReTxDescripcion());
                r++;
                nextrow.createCell(r).setCellValue(objEvaluador.getReTxNombreCargo());
                r++;
                nextrow.createCell(r).setCellValue(objEvaluador.getReTxCorreo());
                if (blHabilitarSexo) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluador.getReTxSexo());
                }
                if (blHabilitarEdad) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluador.getReNrEdad());
                }
                if (blHabilitarTiempoEmpresa) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluador.getReNrTiempoTrabajo());
                }
                if (blHabilitarNivelOcupacional) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluador.getReTxOcupacion());
                }
                if (blHabilitarAreaNegocio) {
                    r++;
                    nextrow.createCell(r).setCellValue(objEvaluador.getReTxAreaNegocio());
                }
                i++;
            }

        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
        hoja.autoSizeColumn(2);
        hoja.autoSizeColumn(3);
        hoja.autoSizeColumn(4);
        hoja.autoSizeColumn(5);
        hoja.autoSizeColumn(6);
        hoja.autoSizeColumn(7);

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + msg("evaluator") + ".xls\"");

            xlsEvaluadores.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void generaExcelRespuestaEvaluadores() {

        HSSFWorkbook xlsEvaluadores = new HSSFWorkbook();

        HSSFSheet hoja = xlsEvaluadores.createSheet(msg("evaluator"));

        int c = 0;

        HSSFRow row = hoja.createRow(0);

        HSSFCell cell0 = row.createCell(c);
        HSSFRichTextString texto0 = new HSSFRichTextString(msg("description"));
        cell0.setCellValue(texto0);

        c++;
        HSSFCell cell1 = row.createCell(c);
        HSSFRichTextString texto1 = new HSSFRichTextString(msg("work.range"));
        cell1.setCellValue(texto1);

        c++;
        HSSFCell cell2 = row.createCell(c);
        HSSFRichTextString texto2 = new HSSFRichTextString(msg("email"));
        cell2.setCellValue(texto2);

        if (blHabilitarSexo) {
            c++;
            HSSFCell cell3 = row.createCell(c);
            HSSFRichTextString texto3 = new HSSFRichTextString(msg("sex"));
            cell3.setCellValue(texto3);
        }

        if (blHabilitarEdad) {
            c++;
            HSSFCell cell4 = row.createCell(c);
            HSSFRichTextString texto4 = new HSSFRichTextString(msg("age"));
            cell4.setCellValue(texto4);
        }

        if (blHabilitarTiempoEmpresa) {
            c++;
            HSSFCell cell5 = row.createCell(c);
            HSSFRichTextString texto5 = new HSSFRichTextString(msg("hiring.time"));
            cell5.setCellValue(texto5);
        }

        if (blHabilitarNivelOcupacional) {
            c++;
            HSSFCell cell6 = row.createCell(c);
            HSSFRichTextString texto6 = new HSSFRichTextString(msg("work.range"));
            cell6.setCellValue(texto6);
        }

        if (blHabilitarAreaNegocio) {
            c++;
            HSSFCell cell7 = row.createCell(c);
            HSSFRichTextString texto7 = new HSSFRichTextString(msg("working.area"));
            cell7.setCellValue(texto7);
        }

        HSSFCell cell8 = row.createCell(8);
        HSSFRichTextString texto8 = new HSSFRichTextString(msg("observation"));
        cell8.setCellValue(texto8);

        HSSFCellStyle myStyle = xlsEvaluadores.createCellStyle();

        HSSFFont hSSFFont = xlsEvaluadores.createFont();
        hSSFFont.setBold(true);

        myStyle.setFont(hSSFFont);

        row.setRowStyle(myStyle);

        int i = 1;
        for (Evaluador objEvaluador : lstCargaMasivaEvaluadores) {
            int r = 0;
            HSSFRow nextrow = hoja.createRow(i);
            nextrow.createCell(r).setCellValue(objEvaluador.getReTxDescripcion());
            r++;
            nextrow.createCell(r).setCellValue(objEvaluador.getReTxNombreCargo());
            r++;
            nextrow.createCell(r).setCellValue(objEvaluador.getReTxCorreo());
            if (blHabilitarSexo) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluador.getReTxSexo());
            }
            if (blHabilitarEdad) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluador.getReNrEdad());
            }
            if (blHabilitarTiempoEmpresa) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluador.getReNrTiempoTrabajo());
            }
            if (blHabilitarNivelOcupacional) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluador.getReTxOcupacion());
            }
            if (blHabilitarAreaNegocio) {
                r++;
                nextrow.createCell(r).setCellValue(objEvaluador.getReTxAreaNegocio());
            }
            r++;
            nextrow.createCell(3).setCellValue(objEvaluador.getStrObservacionMasivo());
            i++;

        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
        hoja.autoSizeColumn(2);
        hoja.autoSizeColumn(3);
        hoja.autoSizeColumn(4);
        hoja.autoSizeColumn(5);
        hoja.autoSizeColumn(6);
        hoja.autoSizeColumn(7);
        hoja.autoSizeColumn(8);

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + msg("evaluator") + ".xls\"");

            xlsEvaluadores.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void leeExcelEvaluadores(FileUploadEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();

        intCantTempCorrectEvaluadores = 0;
        intCantTempIncorrectEvaluadores = 0;

        if (event.getFile() == null) {
            mostrarAlertaError("file.empty");
        } else {

            HSSFWorkbook xlsEvaluadores = null;

            try {

                xlsEvaluadores = new HSSFWorkbook(event.getFile().getInputStream());

                HSSFSheet sheet = xlsEvaluadores.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();

                Row cabecera = rowIterator.next();

                if (validaCabecera(cabecera, true)) {
                    mostrarAlertaError("file.structure.incorrect");
                } else {

                    lstCargaMasivaEvaluadores = new ArrayList();

                    Evaluador objEvaluador;

                    while (rowIterator.hasNext()) {

                        Row row = rowIterator.next();

                        int c = 0;

                        String strDescripcion;
                        strDescripcion = Utilitarios.obtieneDatoCelda(row, c);

                        String strCargo;
                        c++;
                        strCargo = Utilitarios.obtieneDatoCelda(row, c);

                        String strCorreo;
                        c++;
                        strCorreo = Utilitarios.obtieneDatoCelda(row, c);

                        String strSexo = null;
                        if (blHabilitarSexo) {
                            c++;
                            strSexo = Utilitarios.obtieneDatoCelda(row, c);;
                        }

                        String strEdad = null;
                        if (blHabilitarEdad) {
                            c++;
                            strEdad = Utilitarios.obtieneDatoCelda(row, c);
                        }

                        String strTiempoEmpresa = null;
                        if (blHabilitarTiempoEmpresa) {
                            c++;
                            strTiempoEmpresa = Utilitarios.obtieneDatoCelda(row, c);
                        }

                        String strOcupacion = null;
                        if (blHabilitarNivelOcupacional) {
                            c++;
                            strOcupacion = Utilitarios.obtieneDatoCelda(row, c);
                        }

                        String strAreaNegocio = null;
                        if (blHabilitarAreaNegocio) {
                            c++;
                            strAreaNegocio = Utilitarios.obtieneDatoCelda(row, c);
                        }

                        objEvaluador = new Evaluador();

                        objEvaluador.setReTxDescripcion(strDescripcion);
                        objEvaluador.setReTxNombreCargo(strCargo);
                        objEvaluador.setReTxCorreo(strCorreo);
                        objEvaluador.setReTxSexo(strSexo);

                        if (Utilitarios.noEsNuloOVacio(strEdad)) {
                            if (Utilitarios.isNumber(strEdad, false)) {
                                BigDecimal bd = new BigDecimal(strEdad);
                                objEvaluador.setReNrEdad(bd.intValue());
                            } else {
                                objEvaluador.setReNrEdad(0);
                            }
                        } else {
                            objEvaluador.setReNrEdad(0);
                        }

                        if (Utilitarios.noEsNuloOVacio(strTiempoEmpresa)) {
                            if (Utilitarios.isNumber(strEdad, false)) {
                                BigDecimal bd = new BigDecimal(strTiempoEmpresa);
                                objEvaluador.setReNrTiempoTrabajo(bd.intValue());
                            } else {
                                objEvaluador.setReNrTiempoTrabajo(0);
                            }
                        } else {
                            objEvaluador.setReNrTiempoTrabajo(0);
                        }

                        objEvaluador.setReTxOcupacion(strOcupacion);
                        objEvaluador.setReTxAreaNegocio(strAreaNegocio);

                        Integer error;

                        error = buscarListaEvaluadores(strDescripcion, strCargo, strCorreo, true);

                        if (Utilitarios.esNuloOVacio(error)) {
                            error = buscarListaTemporalEvaluadores(strDescripcion, strCargo, strCorreo);
                        }

                        if (Utilitarios.esNuloOVacio(error)) {
                            error = validarParametros(strSexo, strEdad, strTiempoEmpresa, strOcupacion, strAreaNegocio);
                        }

                        if (Utilitarios.esNuloOVacio(error)) {
                            intCantTempCorrectEvaluadores++;
                            objEvaluador.setStrCorrectoMasivo("good");
                        } else {
                            intCantTempIncorrectEvaluadores++;
                            objEvaluador = determinaErrorEvaluadores(objEvaluador, error);
                        }

                        lstCargaMasivaEvaluadores.add(objEvaluador);

                    }

                    fileEvaluadores = null;

                }

            } catch (IOException e) {
                mostrarError(log, e);
                mostrarAlertaError("file.error");
            } catch (NoSuchElementException e) {
                mostrarError(log, e);
                mostrarAlertaError("file.structure.incorrect");
            } catch (NullPointerException e) {
                mostrarError(log, e);
                mostrarAlertaError("file.data.incomplete");
            }
        }
    }

    private Evaluador determinaErrorEvaluadores(Evaluador objEvaluador, Integer error) {

        String icon = null, mensaje = null;

        if (error.equals(0)) {
            icon = "error";
            mensaje = msg("error.cell.empty");
        } else if (error.equals(1)) {
            icon = "error";
            mensaje = msg("evaluator.exist.in.file");
        } else if (error.equals(2)) {
            icon = "alert";
            mensaje = msg("register.was.rewrited");
        } else if (error.equals(3)) {
            icon = "error";
            mensaje = msg("email.duplicated");
        } else if (error.equals(4)) {
            icon = "error";
            mensaje = msg("error.email.invalid");
        } else if (error.equals(5)) {
            icon = "error";
            mensaje = msg("error.email.registrered.previously");
        }

        objEvaluador.setStrCorrectoMasivo(icon);
        objEvaluador.setStrObservacionMasivo(mensaje);

        return objEvaluador;

    }

    public Integer buscarListaTemporalEvaluadores(String strDescripcion, String strCargo, String strCorreo) {

        String strDesc = Utilitarios.retirarEspacios(strDescripcion);
        String strCorr = Utilitarios.retirarEspacios(strCorreo);
        String strCarg = Utilitarios.retirarEspacios(strCargo);

        if (Utilitarios.esNuloOVacio(strCorr) || Utilitarios.esNuloOVacio(strDesc) || Utilitarios.esNuloOVacio(strCarg)) {
            return 0;//"Contiene datos vacios";
        }

        for (Evaluador obj : lstCargaMasivaEvaluadores) {
            if (Utilitarios.retirarEspacios(obj.getReTxDescripcion()).toUpperCase().equals(strDesc.toUpperCase())) {
                return 1;//"El evaluado ingresado ya se encuentra agregado";
            }
            if (Utilitarios.retirarEspacios(obj.getReTxCorreo()).toUpperCase().equals(strCorr.toUpperCase())) {
                return 3;//"El correo ingresado esta siendo usado por " + obj.getPaTxDescripcion();
            }
        }

        if (!Pattern.compile(Constantes.EMAIL_PATTERN).matcher(strCorr).matches()) {
            return 4;//"No es un correo electronico";
        }
        return null;

    }

    public void cargarListaEvaluadores() {

        try {

            for (Evaluador objEvaluador : lstCargaMasivaEvaluadores) {

                if (objEvaluador.getStrCorrectoMasivo().equals("good")) {

                    Integer error = buscarListaEvaluadores(objEvaluador.getReTxDescripcion(), objEvaluador.getReTxNombreCargo(), objEvaluador.getReTxCorreo(), false);

                    Evaluador nuevoEvaluador = new Evaluador();

                    nuevoEvaluador.setReTxDescripcion(objEvaluador.getReTxDescripcion());
                    nuevoEvaluador.setReTxNombreCargo(objEvaluador.getReTxNombreCargo());
                    nuevoEvaluador.setReTxCorreo(objEvaluador.getReTxCorreo());
                    nuevoEvaluador.setReTxSexo(objEvaluador.getReTxSexo());
                    nuevoEvaluador.setReNrEdad(objEvaluador.getReNrEdad());
                    nuevoEvaluador.setReNrTiempoTrabajo(objEvaluador.getReNrTiempoTrabajo());
                    nuevoEvaluador.setReTxOcupacion(objEvaluador.getReTxOcupacion());
                    nuevoEvaluador.setReTxAreaNegocio(objEvaluador.getReTxAreaNegocio());
                    nuevoEvaluador.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO);
                    nuevoEvaluador.setReStrEstado(msg(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO.toString()));

                    if (error == null) {

                        RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();
                        nuevoEvaluador.setReIdParticipantePk(objRedEvaluacionDAO.guardaRedEvaluacion(creaRedEvaluacionEvaluadores(nuevoEvaluador)));
                        objEvaluador.setStrObservacionMasivo("Procesado");

                        this.lstEvaluadores.add(nuevoEvaluador);

                    } else {
                        objEvaluador = determinaErrorEvaluadores(objEvaluador, error);
                    }

                }
            }

            calculaIndicadoresEvaluadores();

            intCantTempCorrect = 0;

            mostrarAlertaInfo("process.upload.was.ok");

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("process.upload.has.error");
        }
    }

    private void calculaIndicadoresEvaluadores() {

        this.cantidadEvaluadoresRegistrados = 0;

        for (Evaluador objEvaluador : lstEvaluadores) {
            if (objEvaluador.getReIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO)) {
                this.cantidadEvaluadoresRegistrados++;
            }
        }

    }

    private RedEvaluacion creaRedEvaluacionEvaluadores(Evaluador objEvaluador) {

        RedEvaluacion objRedEvaluacion = new RedEvaluacion();

        objRedEvaluacion.setReIdTipoParticipante(Constantes.INT_ET_TIPO_PARTICIPANTE_EVALUADOR);
        objRedEvaluacion.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_REGISTRADO);
        objRedEvaluacion.setReTxCorreo(objEvaluador.getReTxCorreo().toLowerCase());
        objRedEvaluacion.setReTxNombreCargo(objEvaluador.getReTxNombreCargo());
        objRedEvaluacion.setReTxDescripcion(objEvaluador.getReTxDescripcion());
        objRedEvaluacion.setReTxSexo(objEvaluador.getReTxSexo());
        objRedEvaluacion.setReNrEdad(objEvaluador.getReNrEdad());
        objRedEvaluacion.setReNrTiempoTrabajo(objEvaluador.getReNrTiempoTrabajo());
        objRedEvaluacion.setReTxOcupacion(objEvaluador.getReTxOcupacion());
        objRedEvaluacion.setReTxAreaNegocio(objEvaluador.getReTxAreaNegocio());

        Proyecto objProyecto = new Proyecto();
        objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());

        objRedEvaluacion.setProyecto(objProyecto);

        return objRedEvaluacion;

    }

    public void actualizarEvaluadores() {
        strCorreoEvaluadores = strCorreoEvaluadores.toLowerCase();
        Integer error = buscarListaEvaluadores(this.reIdParticipantePk, strDescripcionEvaluadores, strCargoEvaluadores, strCorreoEvaluadores, false);

        if (error == null) {
            RedEvaluacionDAO objRedEvaluacionDAO = new RedEvaluacionDAO();

            RedEvaluacion objRedEvaluacion = objRedEvaluacionDAO.obtenRedEvaluacion(this.reIdParticipantePk);

            objRedEvaluacion.setReTxDescripcion(strDescripcionEvaluadores);
            objRedEvaluacion.setReTxNombreCargo(strCargoEvaluadores);
            objRedEvaluacion.setReTxCorreo(strCorreoEvaluadores);
            objRedEvaluacion.setReTxSexo(strSexoEvaluadores);
            objRedEvaluacion.setReNrEdad(intEdadEvaluadores);
            objRedEvaluacion.setReNrTiempoTrabajo(intTiempoEmpresaEvaluadores);
            objRedEvaluacion.setReTxOcupacion(strOcupacionEvaluadores);
            objRedEvaluacion.setReTxAreaNegocio(strAreaNegocioEvaluadores);

            objRedEvaluacionDAO.actualizaRedEvaluacion(objRedEvaluacion);

            for (Evaluador objEvaluador : lstEvaluadores) {

                if (objEvaluador.getReIdParticipantePk().equals(this.reIdParticipantePk)) {

                    objEvaluador.setReTxDescripcion(strDescripcionEvaluadores);
                    objEvaluador.setReTxNombreCargo(strCargoEvaluadores);
                    objEvaluador.setReTxCorreo(strCorreoEvaluadores);
                    objEvaluador.setReTxSexo(strSexoEvaluadores);
                    objEvaluador.setReNrEdad(intEdadEvaluadores);
                    objEvaluador.setReNrTiempoTrabajo(intTiempoEmpresaEvaluadores);
                    objEvaluador.setReTxOcupacion(strOcupacionEvaluadores);
                    objEvaluador.setReTxAreaNegocio(strAreaNegocioEvaluadores);

                }

            }

            mostrarAlertaInfo("updated");

            resetFail();

        } else {
            Evaluador objEvaluador = new Evaluador();
            objEvaluador = determinaErrorEvaluadores(objEvaluador, error);

            mostrarAlertaError(objEvaluador.getStrObservacionMasivo());

        }
    }

    private boolean incluirComoEvaluador(String paTxDescripcion, String strCargoEvaluadores, String paTxCorreo, String paTxSexo, Integer paNrEdad, Integer paNrTiempoTrabajo, String paTxOcupacion, String paTxAreaNegocio, boolean esMasivoEvaluado) {
        this.strDescripcionEvaluadores = paTxDescripcion;
        this.strCargoEvaluadores = strCargoEvaluadores;
        this.strCorreoEvaluadores = paTxCorreo.toLowerCase();
        this.strSexoEvaluadores = paTxSexo;
        this.intEdadEvaluadores = paNrEdad;
        this.intTiempoEmpresaEvaluadores = paNrTiempoTrabajo;
        this.strOcupacionEvaluadores = paTxOcupacion;
        this.strAreaNegocioEvaluadores = paTxAreaNegocio;
        init();
        return agregarEvaluadores(esMasivoEvaluado);

    }

    private void habilitarParametros() {

        hSexo = new HashMap();
        hNO = new HashMap();
        hAN = new HashMap();

        blHabilitarAreaNegocio = false;
        blHabilitarEdad = false;
        blHabilitarNivelOcupacional = false;
        blHabilitarSexo = false;
        blHabilitarTiempoEmpresa = false;

        List<Parametro> lstParametros = objParametroDAO.obtenListaParametros(Utilitarios.obtenerProyecto().getIntIdProyecto());

        for (Parametro objParametro : lstParametros) {

            if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_EDAD)) {
                blHabilitarEdad = true;
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_AREA)) {
                blHabilitarAreaNegocio = true;
                lstAreaNegocio = new ArrayList<>();
                byte[] bdata = objParametro.getPaTxPatron();
                String data = new String(bdata);
                String[] strDatos = data.split(",");
                int i = 0;
                while (i < strDatos.length) {
                    SelectItem objSelectItem = new SelectItem();
                    objSelectItem.setValue(strDatos[i]);
                    objSelectItem.setLabel(strDatos[i]);
                    lstAreaNegocio.add(objSelectItem);
                    hAN.put(strDatos[i].toUpperCase(), strDatos[i]);
                    i++;
                }
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_NIVEL)) {
                blHabilitarNivelOcupacional = true;
                lstNivelOcupacional = new ArrayList<>();
                byte[] bdata = objParametro.getPaTxPatron();
                String data = new String(bdata);
                String[] strDatos = data.split(",");
                int i = 0;
                while (i < strDatos.length) {
                    SelectItem objSelectItem = new SelectItem();
                    objSelectItem.setValue(strDatos[i]);
                    objSelectItem.setLabel(strDatos[i]);
                    lstNivelOcupacional.add(objSelectItem);
                    hNO.put(strDatos[i].toUpperCase(), strDatos[i]);
                    i++;
                }
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_SEXO)) {
                blHabilitarSexo = true;
                hSexo.put(msg("male"), msg("male"));
                hSexo.put(msg("female"), msg("female"));
            } else if (objParametro.getPaIdTipoParametro().equals(Constantes.INT_ET_TIPO_PARAMETRO_TIEMPO)) {
                blHabilitarTiempoEmpresa = true;
            }

        }

    }

    private boolean validaCabecera(Row cabecera, boolean flag) {

        if (flag) {

        } else {

        }

        return false;
    }

    public void enableUploadButton() {
        btnUploadDisabled = false;
    }

    public void leeExcelAvanzado() {

        habilitarParametros();

        lstErrorAvan = new ArrayList();
        lstAvanPersonas = new ArrayList();
        lstAvanRelacion = new ArrayList();
        lstRelacionAvanzadas = new ArrayList<>();
        mapRelacionesAvanzado = new HashMap();
        mapRelacionesAbrev = new HashMap();
        mapPersonasAvanzado = new HashMap();
        mapRelacionesPersonasAvanzado = new HashMap();
        mapPerEvaluados = new HashMap();
        mapPerEvaluadores = new HashMap();

        if (fileAvanzado == null) {

            mostrarAlertaError("search.file.first");
        } else {

            HSSFWorkbook xlsAvanzado = null;

            try {

                xlsAvanzado = new HSSFWorkbook(fileAvanzado.getInputStream());

                validaEstructuraAvanzado(xlsAvanzado);

                if (lstErrorAvan.isEmpty()) {

                    validaTextoIngresado objvalidaTextoIngresado = new validaTextoIngresado();
                    validaCorreo objvalidaCorreo = new validaCorreo();
                    validaURL objValidaUrl = new validaURL();

                    /**
                     * *************
                     */
                    /* LEE PERSONAS */
                    /**
                     * *************
                     */
                    HSSFSheet sheetPersonas = xlsAvanzado.getSheetAt(0);
                    Iterator<Row> rowIteratorPersonas = sheetPersonas.iterator();
                    rowIteratorPersonas.next();

                    while (rowIteratorPersonas.hasNext()) {
                        Row row = rowIteratorPersonas.next();
                        procesaFilaPersonasAvanzado(row, objvalidaTextoIngresado, objvalidaCorreo, objValidaUrl);
                    }

                    /**
                     * ***************
                     */
                    /* LEE RELACIONES */
                    /**
                     * ***************
                     */
                    HSSFSheet sheetRelaciones = xlsAvanzado.getSheetAt(1);
                    Iterator<Row> rowIteratorRelaciones = sheetRelaciones.iterator();
                    rowIteratorRelaciones.next();

                    while (rowIteratorRelaciones.hasNext()) {
                        Row row = rowIteratorRelaciones.next();
                        procesaFilaRelacionesAvanzado(row, objvalidaTextoIngresado, objvalidaCorreo);
                    }

                    /**
                     * PROCESAR AUTOEVALUACIONES
                     */
                    boolean blAlMenosUnAutoevaluado = false;
                    for (EvaluadoAvan objEvaluadoAvan : this.lstAvanPersonas) {

                        if (!mapPerEvaluados.containsKey(Utilitarios.limpiarTexto(objEvaluadoAvan.getPaTxCorreo()))) {
                            mapPerEvaluados.put(Utilitarios.limpiarTexto(objEvaluadoAvan.getPaTxCorreo()), objEvaluadoAvan.getPaTxCorreo());
                        }

                        if (objEvaluadoAvan.isPaInAutoevaluar()) {
                            blAlMenosUnAutoevaluado = true;
                        }
                    }

                    if (this.lstRelacionAvanzadas.isEmpty() && !blAlMenosUnAutoevaluado) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("error.at.least.person.to.evaluate")));
                    }

                }

                fileAvanzado = null;

                if (!lstErrorAvan.isEmpty() && lstAvanPersonas.isEmpty() && lstAvanRelacion.isEmpty()) {
                    blCargarCorrectoAvan = false;
                    mostrarAlertaFatal("file.has.data.errors");
                } else if (!lstErrorAvan.isEmpty()) {
                    blCargarCorrectoAvan = true;
                    mostrarAlertaInfo("file.has.data.warnings");
                } else {
                    blCargarCorrectoAvan = true;
                    mostrarAlertaInfo("file.was.processed.successfully");
                }

            } catch (IOException e) {
                mostrarError(log, e);
                mostrarAlertaFatal("file.error");
            } catch (NoSuchElementException e) {
                mostrarError(log, e);
                mostrarAlertaFatal("file.structure.incorrect");
            } catch (NullPointerException e) {
                mostrarError(log, e);
                mostrarAlertaFatal("file.data.incomplete");
            }
        }

    }

    private void validaEstructuraAvanzado(HSSFWorkbook xlsAvanzado) {

        HSSFSheet sheetPersonas = null;
        HSSFSheet sheetRelacion = null;

        boolean blLibrosOk = true;
        boolean blCabPersonasOk = true;
        boolean blCabRelacionOk = true;

        try {
            sheetPersonas = xlsAvanzado.getSheetAt(0);
            sheetRelacion = xlsAvanzado.getSheetAt(1);
        } catch (Exception e) {
            blLibrosOk = false;
            mostrarError(log, e);
            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("file.error.template")));
        }

        if (blLibrosOk) {

            Iterator<Row> rowIteratorPersonas = null;
            Iterator<Row> rowIteratorRelacion = null;

            try {
                rowIteratorPersonas = sheetPersonas.iterator();
                if (!rowIteratorPersonas.hasNext()) {
                    blCabPersonasOk = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("file.error.template")));
                }
            } catch (Exception e) {
                blCabPersonasOk = false;
                log.error(e);
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("file.error.template")));
            }

            try {
                rowIteratorRelacion = sheetRelacion.iterator();
                if (!rowIteratorRelacion.hasNext()) {
                    blCabRelacionOk = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("file.error.template")));
                }
            } catch (Exception e) {
                blCabRelacionOk = false;
                log.error(e);
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("file.error.template")));
            }

            if (blCabPersonasOk && blCabRelacionOk) {

                /**
                 * ****************************
                 */
                /* VALIDA CABECERA DE PERSONAS */
                /**
                 * ****************************
                 */
                Row row = rowIteratorPersonas.next();

                int c = 0;

                //DESCRIPCION
                try {
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.description")));
                    } else {
                        if (!strDato.trim().equals(msg("description"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.description")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.description")));
                }

                //CARGO
                try {
                    c++;
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.role")));
                    } else {
                        if (!strDato.trim().equals(msg("role"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.role")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.role")));
                }

                //CORREO
                try {
                    c++;
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.email")));
                    } else {
                        if (!strDato.trim().equals(msg("email"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.email")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.email")));
                }

                //URL IMAGEN
                try {
                    c++;
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.url.image")));
                    } else {
                        if (!strDato.trim().equals(msg("url.image"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.url.image")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.url.image")));
                }

                //SEXO
                if (blHabilitarSexo) {
                    try {
                        c++;
                        String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        if (Utilitarios.esNuloOVacio(strDato)) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.sex")));
                        } else {
                            if (!strDato.trim().equals(msg("sex"))) {
                                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.sex")));
                            }
                        }
                    } catch (NoSuchElementException | NullPointerException e) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.sex")));
                    }
                }

                //EDAD
                if (blHabilitarEdad) {
                    try {
                        c++;
                        String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        if (Utilitarios.esNuloOVacio(strDato)) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.age")));
                        } else {
                            if (!strDato.trim().equals(msg("age"))) {
                                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.age")));
                            }
                        }
                    } catch (NoSuchElementException | NullPointerException e) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.age")));
                    }
                }

                //TIEMPO EN LA EMPRESA
                if (blHabilitarTiempoEmpresa) {
                    try {
                        c++;
                        String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        if (Utilitarios.esNuloOVacio(strDato)) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.hiring.time")));
                        } else {
                            if (!strDato.trim().equals(msg("hiring.time"))) {
                                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.hiring.time")));
                            }
                        }
                    } catch (NoSuchElementException | NullPointerException e) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.hiring.time")));
                    }
                }

                //NIVEL OCUPACIONAL
                if (blHabilitarNivelOcupacional) {
                    try {
                        c++;
                        String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        if (Utilitarios.esNuloOVacio(strDato)) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.work.range")));
                        } else {
                            if (!strDato.trim().equals(msg("work.range"))) {
                                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.work.range")));
                            }
                        }
                    } catch (NoSuchElementException | NullPointerException e) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.work.range")));
                    }
                }

                //AREA DEL NEGOCIO
                if (blHabilitarAreaNegocio) {
                    try {
                        c++;
                        String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                        if (Utilitarios.esNuloOVacio(strDato)) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.working.area")));
                        } else {
                            if (!strDato.trim().equals(msg("working.area"))) {
                                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.working.area")));
                            }
                        }
                    } catch (NoSuchElementException | NullPointerException e) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.working.area")));
                    }
                }

                //AUTOEVALUACION
                try {
                    c++;
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.autoevaluate")));
                    } else {
                        if (!strDato.trim().equals(msg("autoevaluate"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.autoevaluate")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.autoevaluate")));
                }

                /**
                 * ******************************
                 */
                /* VALIDA CABECERA DE RELACIONES */
                /**
                 * ******************************
                 */
                row = rowIteratorRelacion.next();

                c = 0;

                //EVALUADO
                try {
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.evaluated")));
                    } else {
                        if (!strDato.trim().equals(msg("evaluated"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.evaluated")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.evaluated")));
                }

                //RELACION
                try {
                    c++;
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.relationship")));
                    } else {
                        if (!strDato.trim().equals(msg("relationship"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.relationship")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.relationship")));
                }

                //EVALUADOR
                try {
                    c++;
                    String strDato = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                    if (Utilitarios.esNuloOVacio(strDato)) {
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.evaluator")));
                    } else {
                        if (!strDato.trim().equals(msg("evaluator"))) {
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.evaluator")));
                        }
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("cell") + " " + Utilitarios.columnExcel(c) + " " + msg("error.title.evaluator")));
                }

                /**
                 * **********************************
                 */
                /* VALIDA SI TIENEN AL MENOS UNA FILA*/
                /**
                 * **********************************
                 */
                try {
                    rowIteratorPersonas.next();
                } catch (Exception e) {
                    mostrarError(log, e);
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("error.title.persons.empty")));
                }
            }

        }

    }

    private void procesaFilaPersonasAvanzado(Row row, validaTextoIngresado objvalidaTextoIngresado, validaCorreo objvalidaCorreo, validaURL objValidaURL) {

        boolean blRegistroOK = true;
        int c = 0;
        List<ErrorBean> lstErrorAvan = new ArrayList<>();

        /* DESCRIPCION */
        String strDescripcion = null;
        try {
            String strTemp = null;
            if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, c);
                String strError = objvalidaTextoIngresado.validar(strTemp);
                if (Utilitarios.esNuloOVacio(strError)) {
                    strDescripcion = strTemp.trim();
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + msg("error.persons.has.description") + " " + strError));
                }
            } else {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.description.empty.not.text")));
            }

        } catch (NoSuchElementException | NullPointerException e) {
            blRegistroOK = false;
            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.description.empty.not.text")));
        }

        /* CARGO */
        String strCargo = null;
        c++;
        try {
            String strTemp = null;
            if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, c);
                if (Utilitarios.noEsNuloOVacio(strTemp)) {
                    String strError = objvalidaTextoIngresado.validar(strTemp);
                    if (Utilitarios.esNuloOVacio(strError)) {
                        strCargo = Utilitarios.limpiarTexto(strTemp).trim();
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.role") + " " + strError));
                    }
                }

            } else {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.role.empty.not.text")));
            }

        } catch (NoSuchElementException | NullPointerException e) {
            //blRegistroOK = false;
            //lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(),msg("row") + " "+(row.getRowNum()+1)+" del libro \"Personas\" tiene el campo \"Cargo\" vacio o en un formato diferente de texto"));
        }

        /* CORREO */
        String strCorreo = null;
        c++;
        try {
            String strTemp = null;
            if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, c);
                String strError = objvalidaCorreo.validate(strTemp);
                if (Utilitarios.esNuloOVacio(strError)) {
                    strCorreo = strTemp.trim().toLowerCase();
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.email") + " " + strError));
                }
            } else {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.email.empty.not.text")));
            }

        } catch (NoSuchElementException | NullPointerException e) {
            blRegistroOK = false;
            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.email.empty.not.text")));
        }

        /* URL IMAGEN */
        String strURLImagen = null;
        c++;
        try {
            String strTemp = null;
            if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, c);
                
                if (Utilitarios.noEsNuloOVacio(strTemp)) {
                    String strError = objValidaURL.validate(strTemp);
                    if (Utilitarios.esNuloOVacio(strError)) {
                        strURLImagen = strTemp.trim();
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.url.not.valid.url") + " " + strError));
                    }
                }
            } else {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.url.not.valid.url")));
            }

        } catch (NoSuchElementException | NullPointerException e) {
            blRegistroOK = false;
            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.url.not.valid.url")));
        }

        String strSexo = null;
        if (blHabilitarSexo) {
            c++;
            try {
                String strTemp = null;
                if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                    strTemp = Utilitarios.obtieneDatoCelda(row, c);
                    if (Utilitarios.noEsNuloOVacio(strTemp)) {
                        strTemp = Utilitarios.limpiarTexto(strTemp);
                        if (strTemp.equals(msg("male").toUpperCase()) || strTemp.equals(msg("female").toUpperCase())) {
                            strSexo = strTemp.trim();
                        } else {
                            blRegistroOK = false;
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.sex")));
                        }
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.sex.empty.not.text")));
                    }
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.sex.empty.not.text")));
                }

            } catch (NoSuchElementException | NullPointerException e) {
                blRegistroOK = false;
                strSexo = Constantes.strVacio;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.sex.empty.not.text")));
            }
        }

        Integer intEdad = null;
        if (blHabilitarEdad) {
            c++;
            try {
                String strTemp = null;
                if (row.getCell(c).getCellType() == CellType.NUMERIC || row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                    strTemp = Utilitarios.obtieneDatoCelda(row, c);
                    if (Utilitarios.isNumber(strTemp, false)) {
                        BigDecimal bd = new BigDecimal(strTemp);
                        if (bd.intValue() > 0) {
                            intEdad = bd.intValue();
                        } else {
                            blRegistroOK = false;
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.age.invalid")));
                        }
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.sex.empty.not.numeric")));
                    }

                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.sex.empty.not.numeric")));
                }

            } catch (NoSuchElementException | NullPointerException e) {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.sex.empty.not.numeric")));
            }
        }

        Integer intTiempoEmpresa = null;
        if (blHabilitarTiempoEmpresa) {
            c++;
            try {
                String strTemp = null;
                if (row.getCell(c).getCellType() == CellType.NUMERIC || row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                    strTemp = Utilitarios.obtieneDatoCelda(row, c);
                    if (Utilitarios.isNumber(strTemp, false)) {
                        BigDecimal bd = new BigDecimal(strTemp.trim());
                        if (bd.intValue() > 0) {
                            intTiempoEmpresa = bd.intValue();
                        } else {
                            blRegistroOK = false;
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.hiring.invalid")));
                        }
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.hiring.empty.not.numeric")));
                    }
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.hiring.empty.not.numeric")));
                }

            } catch (NoSuchElementException | NullPointerException e) {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.hiring.empty.not.numeric")));
            }
        }

        String strOcupacion = null;
        if (blHabilitarNivelOcupacional) {
            c++;
            try {
                String strTemp = null;
                if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                    strTemp = Utilitarios.obtieneDatoCelda(row, c);
                    if (Utilitarios.noEsNuloOVacio(strTemp)) {
                        strTemp = Utilitarios.limpiarTexto(strTemp);
                        if (!hNO.containsKey(strTemp.trim())) {
                            blRegistroOK = false;
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workrange.invalid")));
                        } else {
                            strOcupacion = hNO.get(strTemp.trim()).toString();
                        }
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workrange.empty.not.text")));
                    }
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workrange.empty.not.text")));
                }

            } catch (NoSuchElementException | NullPointerException e) {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workrange.empty.not.text")));
            }
        }

        String strAreaNegocio = null;
        if (blHabilitarAreaNegocio) {
            c++;
            try {
                String strTemp = null;
                if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                    strTemp = Utilitarios.obtieneDatoCelda(row, c);
                    if (Utilitarios.noEsNuloOVacio(strTemp)) {
                        strTemp = Utilitarios.limpiarTexto(strTemp);
                        if (!hAN.containsKey(strTemp.trim())) {
                            blRegistroOK = false;
                            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workingarea.invalid")));
                        } else {
                            strAreaNegocio = hAN.get(strTemp.trim()).toString();
                        }
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workingarea.empty.not.text")));
                    }
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workingarea.empty.not.text")));
                }

            } catch (NoSuchElementException | NullPointerException e) {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.workingarea.empty.not.text")));
            }
        }

        boolean blAutoevaluar = false;
        c++;
        try {
            String strTemp = null;
            if (row.getCell(c).getCellType() == CellType.STRING || row.getCell(c).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, c);
            }

            if (Utilitarios.noEsNuloOVacio(strTemp)) {
                if (Utilitarios.limpiarTexto(strTemp).equals(msg("a.indicator.autoevaluation").toUpperCase())) {
                    blAutoevaluar = true;
                }
            }
        } catch (NoSuchElementException | NullPointerException e) {
        }

        if (blRegistroOK) {

            String strKey = Utilitarios.limpiarTexto(strCorreo);

            if (mapPersonasAvanzado.containsKey(strKey)) {
                this.lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.autoevaluate.duplicated")));
            } else {

                EvaluadoAvan objEvaluado = new EvaluadoAvan();

                objEvaluado.setPaTxDescripcion(strDescripcion);
                objEvaluado.setPaTxNombreCargo(strCargo);
                objEvaluado.setPaTxCorreo(strCorreo);
                objEvaluado.setPaTxSexo(strSexo);
                objEvaluado.setPaNrEdad(intEdad);
                objEvaluado.setPaNrTiempoTrabajo(intTiempoEmpresa);
                objEvaluado.setPaTxOcupacion(strOcupacion);
                objEvaluado.setPaTxAreaNegocio(strAreaNegocio);
                objEvaluado.setPaInAutoevaluar(blAutoevaluar);
                objEvaluado.setPaTxImgUrl(strURLImagen);

                mapPersonasAvanzado.put(strKey, objEvaluado);
                lstAvanPersonas.add(objEvaluado);

            }
        } else {
            String strKey = Utilitarios.limpiarTexto(strCorreo);
            if (mapPersonasAvanzado.containsKey(strKey)) {
                this.lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.persons.has.autoevaluate.duplicated")));
            } else {
                this.lstErrorAvan.addAll(lstErrorAvan);
            }
        }

    }

    private void procesaFilaRelacionesAvanzado(Row row, validaTextoIngresado objvalidaTextoIngresado, validaCorreo objvalidaCorreo) {

        boolean blRegistroOK = true;

        /* RELACION */
        String strRelacion = null;
        try {
            String strTemp = null;
            if (row.getCell(1).getCellType() == CellType.STRING || row.getCell(1).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, 1);
                if (Utilitarios.limpiarTexto(strTemp).isEmpty()) {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.has.relationship.empty")));
                } else {
                    strRelacion = strTemp;
                    procesaRelacion(strRelacion, row);
                }
            } else {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.has.relationship.empty")));
            }
        } catch (NoSuchElementException | NullPointerException e) {
            blRegistroOK = false;
            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.has.relationship.empty")));
        }

        /* EVALUADO */
        String strCorreoEvaluado = null;
        try {
            String strTemp = null;
            if (row.getCell(0).getCellType() == CellType.STRING || row.getCell(0).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, 0);
                String strError = objvalidaCorreo.validate(strTemp);
                if (Utilitarios.esNuloOVacio(strError)) {
                    if (mapPersonasAvanzado.containsKey(Utilitarios.limpiarTexto(strTemp))) {
                        strCorreoEvaluado = strTemp.trim().toLowerCase();
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.not.exists")));
                    }
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.column.evaluated") + " " + strError));
                }
            } else {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.column.evaluated.emptyortext")));
            }

        } catch (NoSuchElementException | NullPointerException e) {
            blRegistroOK = false;
            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.column.evaluated.emptyortext")));
        }

        /* EVALUADOR */
        String strCorreoEvaluador = null;
        try {
            String strTemp = null;
            if (row.getCell(2).getCellType() == CellType.STRING || row.getCell(2).getCellType() == CellType.BLANK) {
                strTemp = Utilitarios.obtieneDatoCelda(row, 2);
                String strError = objvalidaCorreo.validate(strTemp);
                if (Utilitarios.esNuloOVacio(strError)) {
                    if (mapPersonasAvanzado.containsKey(Utilitarios.limpiarTexto(strTemp))) {
                        strCorreoEvaluador = strTemp.trim().toLowerCase();
                    } else {
                        blRegistroOK = false;
                        lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.not.exists")));
                    }
                } else {
                    blRegistroOK = false;
                    lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.column.evaluated") + strError));
                }
            } else {
                blRegistroOK = false;
                lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.column.evaluated.emptyortext")));
            }

        } catch (NoSuchElementException | NullPointerException e) {
            blRegistroOK = false;
            lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.email.column.evaluated.emptyortext")));
        }

        try {

            if (blRegistroOK) {

                if (Utilitarios.limpiarTexto(strCorreoEvaluado).equals(Utilitarios.limpiarTexto(strCorreoEvaluador))) {

                    this.lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.notcorrect.use.autoevaluate.mark")));

                } else {

                    String strKey = Utilitarios.limpiarTexto(strCorreoEvaluado)
                            + Utilitarios.limpiarTexto(strCorreoEvaluador);

                    if (mapRelacionesPersonasAvanzado.containsKey(strKey)) {
                        this.lstErrorAvan.add(new ErrorBean(lstErrorAvan.size(), msg("row") + " " + (row.getRowNum() + 1) + " " + msg("error.network.duplicated.review.combination")));
                    } else {

                        if (!mapPerEvaluados.containsKey(Utilitarios.limpiarTexto(strCorreoEvaluado))) {
                            mapPerEvaluados.put(Utilitarios.limpiarTexto(strCorreoEvaluado), strCorreoEvaluado);
                        }
                        if (!mapPerEvaluadores.containsKey(Utilitarios.limpiarTexto(strCorreoEvaluador))) {
                            mapPerEvaluadores.put(Utilitarios.limpiarTexto(strCorreoEvaluador), strCorreoEvaluador);
                        }

                        RelacionAvanzada objRelacionAvanzada = new RelacionAvanzada();
                        objRelacionAvanzada.setStrEvaluado(strCorreoEvaluado);
                        objRelacionAvanzada.setStrEvaluadoDesc(((EvaluadoAvan) mapPersonasAvanzado.get(Utilitarios.limpiarTexto(strCorreoEvaluado))).getPaTxDescripcion());
                        objRelacionAvanzada.setStrEvaluador(strCorreoEvaluador);
                        objRelacionAvanzada.setStrEvaluadorDesc(((EvaluadoAvan) mapPersonasAvanzado.get(Utilitarios.limpiarTexto(strCorreoEvaluador))).getPaTxDescripcion());
                        objRelacionAvanzada.setStrRelacion(strRelacion);
                        objRelacionAvanzada.setStrRelacionAbreviatura(mapRelacionesAbrev.get(Utilitarios.limpiarTexto(strRelacion)).toString());
                        objRelacionAvanzada.setStrRelacionColor(((RelacionBean) mapRelacionesAvanzado.get(Utilitarios.limpiarTexto(strRelacion))).getStrColor());

                        mapRelacionesPersonasAvanzado.put(strKey, objRelacionAvanzada);
                        lstRelacionAvanzadas.add(objRelacionAvanzada);

                    }

                }

            }

        } catch (Exception e) {
            mostrarError(log, e);
        }

    }

    public RelacionBean procesaRelacion(String strNombre, Row row) {

        String strKEY = Utilitarios.limpiarTexto(strNombre);

        RelacionBean objRelacionBean = null;

        if (!mapRelacionesAvanzado.containsKey(strKEY)) {

            objRelacionBean = new RelacionBean();
            objRelacionBean.setStrNombre(strKEY);
            objRelacionBean.setStrDescripcion(strKEY);
            String strAbrev = strKEY.substring(0, 3);

            int a = 0;
            while (mapRelacionesAbrev.containsValue(strAbrev)) {
                strAbrev = strAbrev + a;
            }

            objRelacionBean.setStrAbreviatura(strAbrev);
            objRelacionBean.setStrColor(Utilitarios.generaColorHtmlPreferencial(mapRelacionesAvanzado.size()));
            objRelacionBean.setIntIdEstado(Constantes.INT_ET_ESTADO_RELACION_REGISTRADO);

            mapRelacionesAbrev.put(strKEY, strAbrev);
            mapRelacionesAvanzado.put(strKEY, objRelacionBean);
            lstAvanRelacion.add(objRelacionBean);

        }

        return objRelacionBean;
    }

    public void onTabChange(TabChangeEvent event) {
        this.lstAvanPersonas = new ArrayList<>();
        this.lstAvanRelacion = new ArrayList<>();
        this.lstRelacionAvanzadas = new ArrayList<>();
        this.lstErrorAvan = new ArrayList<>();
        this.blCargarCorrectoAvan = false;
        this.fileAvanzado = null;
        this.mapRelacionesAvanzado = new HashMap();
        this.mapRelacionesAbrev = new HashMap();
        this.mapPersonasAvanzado = new HashMap();
        this.mapRelacionesPersonasAvanzado = new HashMap();

    }

    public void iniciarRedDeCargaAvanzada() {

        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        UsuarioDAO objUsuarioDAO = new UsuarioDAO();

        /* CREAMOS LISTA DE USUARIOS EVALUANTES */
        List<Participante> lstParticipante = objParticipanteDAO.obtenListaParticipanteXEstado(Utilitarios.obtenerProyecto().getIntIdProyecto(), Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION, null);

        List<String> lstCorreos = new ArrayList<>();

        Map hEvaluados = new HashMap();

        for (Participante objParticipante : lstParticipante) {
            if (!hEvaluados.containsKey(objParticipante.getPaTxCorreo())) {
                lstCorreos.add(objParticipante.getPaTxCorreo());
                hEvaluados.put(objParticipante.getPaTxCorreo(), objParticipante.getPaTxDescripcion());
            }
        }

        Map hUsuarios = obtieneUsuarios(lstCorreos);

        objUsuarioDAO.crearCuentasParaProyecto(hEvaluados, hUsuarios);

    }

    public void eliminarCargar() {

        try {

            Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

            objCargaAvanzadaDAO.eliminarRegistrosAvanzado(objProyecto);

            mostrarAlertaInfo("deleted");
            mostrarAlertaWarning("remember.assigned.assessments.again");

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

        init();
    }

    private Map obtieneUsuarios(List<String> lstCorreos) {

        UsuarioDAO objUsuarioDAO = new UsuarioDAO();

        List lstGrupos = ListUtils.partition(lstCorreos, 50);

        Iterator itLstGrupos = lstGrupos.iterator();

        List<Usuario> lstUsuarios;

        Map hUsuarios = new HashMap();

        while (itLstGrupos.hasNext()) {

            lstUsuarios = objUsuarioDAO.obtenListaUsuario((List) itLstGrupos.next());

            for (Usuario objUsuario : lstUsuarios) {
                hUsuarios.put(objUsuario.getUsIdMail(), objUsuario);
            }

        }

        return hUsuarios;
    }

    public void actualizaColorTemporal(RelacionBean objRelacionBean) {

        boolean flag = false;
        try {

            for (RelacionAvanzada objRelacionAvanzada : lstRelacionAvanzadas) {

                if (objRelacionBean.getStrNombre().toUpperCase().equals(objRelacionAvanzada.getStrRelacion().toUpperCase())
                        && objRelacionBean.getStrAbreviatura().toUpperCase().equals(objRelacionAvanzada.getStrRelacionAbreviatura().toUpperCase())) {
                    objRelacionAvanzada.setStrRelacionColor(objRelacionBean.getStrColor());
                    flag = true;
                }

            }

            if (flag) {
                mostrarAlertaInfo("color.was.updated");
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }
    }

    public void actualizaColorDefinitivo(RelacionBean objRelacionBean) {

        try {

            RelacionDAO objRelacionDAO = new RelacionDAO();

            Relacion objRelacion = objRelacionDAO.obtenRelacion(objRelacionBean.getIdRelacionPk());

            objRelacion.setReColor(objRelacionBean.getStrColor());

            objRelacionDAO.actualizaRelacion(objRelacion);

            init();

            mostrarAlertaInfo("color.was.updated");

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }
    }

    public void actualizarRelacionDefinitivo(CellEditEvent event) {

        try {

            if (Utilitarios.noEsNuloOVacio(event.getNewValue())) {

                RelacionBean objRelacionBean = this.lstRelacionBean.get(event.getRowIndex());

                RelacionDAO objRelacionDAO = new RelacionDAO();

                Relacion objRelacion = objRelacionDAO.obtenRelacion(objRelacionBean.getIdRelacionPk());

                objRelacion.setReTxAbreviatura(event.getNewValue().toString().toUpperCase());

                objRelacionDAO.actualizaRelacion(objRelacion);

                init();

                mostrarAlertaInfo("abbreviation.was.updated");

            } else {
                mostrarAlertaError("adm.least.value", null);
            }
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void actualizarRelacionTemporal(CellEditEvent event) {

        boolean flag = false;

        try {

            if (Utilitarios.noEsNuloOVacio(event.getNewValue())) {

                RelacionBean objRelacionBean = this.lstAvanRelacion.get(event.getRowIndex());

                for (RelacionAvanzada objRelacionAvanzada : lstRelacionAvanzadas) {

                    if (objRelacionBean.getStrNombre().toUpperCase().equals(objRelacionAvanzada.getStrRelacion().toUpperCase())) {
                        objRelacionAvanzada.setStrRelacionAbreviatura(event.getNewValue().toString().toUpperCase());
                        flag = true;
                    }

                }

                if (flag) {
                    objRelacionBean.setStrAbreviatura(event.getNewValue().toString().toUpperCase());

                    mostrarAlertaInfo("abbreviation.was.updated");
                } else {
                    mostrarAlertaFatal("error.was.occurred");
                }

            } else {
                mostrarAlertaError("adm.least.value", null);
            }
        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

    public void cargarListaAvanzado() {

        CargaAvanzadaDAO objCargaAvanzadaDAO = new CargaAvanzadaDAO();

        Proyecto objProyecto = objProyectoDAO.obtenProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        boolean correcto = objCargaAvanzadaDAO.guardaRegistros(lstAvanPersonas, lstAvanRelacion, lstRelacionAvanzadas, mapPersonasAvanzado, mapRelacionesAbrev, objProyecto, mapPerEvaluados, mapPerEvaluadores);

        if (correcto) {
            iniciarRedDeCargaAvanzada();
            init();
            mostrarAlertaInfo("configuration.save.success");

        } else {
            mostrarAlertaError("error.was.occurred");
        }

        lstAvanPersonas.clear();
        lstAvanRelacion.clear();
        lstRelacionAvanzadas.clear();
        mapPersonasAvanzado.clear();
        mapRelacionesPersonasAvanzado.clear();
        mapRelacionesAbrev.clear();
        mapPerEvaluados.clear();
        mapPerEvaluadores.clear();

    }

    public void generaModeloAzanvado() {

        HSSFWorkbook xlsEvaluados = new HSSFWorkbook();

        HSSFSheet hojaPersonas = xlsEvaluados.createSheet(msg("persons"));
        HSSFSheet hojaRedEvaluacion = xlsEvaluados.createSheet(msg("network"));

        HSSFRow row = hojaPersonas.createRow(0);
        HSSFRow rowR = hojaRedEvaluacion.createRow(0);

        int c = 0;

        HSSFCell cell0 = row.createCell(c);
        HSSFRichTextString texto0 = new HSSFRichTextString(msg("description"));
        cell0.setCellValue(texto0);

        c++;

        HSSFCell cell1 = row.createCell(c);
        HSSFRichTextString texto1 = new HSSFRichTextString(msg("role"));
        cell1.setCellValue(texto1);

        c++;

        HSSFCell cell2 = row.createCell(c);
        HSSFRichTextString texto2 = new HSSFRichTextString(msg("email"));
        cell2.setCellValue(texto2);

        c++;

        HSSFCell cell9 = row.createCell(c);
        HSSFRichTextString texto9 = new HSSFRichTextString(msg("url.image"));
        cell9.setCellValue(texto9);

        if (blHabilitarSexo) {
            c++;
            HSSFCell cell3 = row.createCell(c);
            HSSFRichTextString texto3 = new HSSFRichTextString(msg("sex"));
            cell3.setCellValue(texto3);
        }

        if (blHabilitarEdad) {
            c++;
            HSSFCell cell4 = row.createCell(c);
            HSSFRichTextString texto4 = new HSSFRichTextString(msg("age"));
            cell4.setCellValue(texto4);
        }

        if (blHabilitarTiempoEmpresa) {
            c++;
            HSSFCell cell5 = row.createCell(c);
            HSSFRichTextString texto5 = new HSSFRichTextString(msg("hiring.time"));
            cell5.setCellValue(texto5);
        }

        if (blHabilitarNivelOcupacional) {
            c++;
            HSSFCell cell6 = row.createCell(c);
            HSSFRichTextString texto6 = new HSSFRichTextString(msg("work.range"));
            cell6.setCellValue(texto6);
        }

        if (blHabilitarAreaNegocio) {
            c++;
            HSSFCell cell7 = row.createCell(c);
            HSSFRichTextString texto7 = new HSSFRichTextString(msg("working.area"));
            cell7.setCellValue(texto7);
        }

        c++;
        HSSFCell cell8 = row.createCell(c);
        HSSFRichTextString texto8 = new HSSFRichTextString(msg("autoevaluate"));
        cell8.setCellValue(texto8);

        HSSFCellStyle myStyle = xlsEvaluados.createCellStyle();

        HSSFFont hSSFFont = xlsEvaluados.createFont();

        hSSFFont.setBold(true);

        myStyle.setFont(hSSFFont);

        row.setRowStyle(myStyle);

        hojaPersonas.autoSizeColumn(0);
        hojaPersonas.autoSizeColumn(1);
        hojaPersonas.autoSizeColumn(2);
        hojaPersonas.autoSizeColumn(3);
        hojaPersonas.autoSizeColumn(4);
        hojaPersonas.autoSizeColumn(5);
        hojaPersonas.autoSizeColumn(6);
        hojaPersonas.autoSizeColumn(7);
        hojaPersonas.autoSizeColumn(8);
        hojaPersonas.autoSizeColumn(9);

        HSSFCell cellR0 = rowR.createCell(0);
        HSSFRichTextString textoR0 = new HSSFRichTextString(msg("evaluated"));
        cellR0.setCellValue(textoR0);

        HSSFCell cellR1 = rowR.createCell(1);
        HSSFRichTextString textoR1 = new HSSFRichTextString(msg("relationship"));
        cellR1.setCellValue(textoR1);

        HSSFCell cellR2 = rowR.createCell(2);
        HSSFRichTextString textoR2 = new HSSFRichTextString(msg("evaluator"));
        cellR2.setCellValue(textoR2);

        myStyle.setFont(hSSFFont);

        rowR.setRowStyle(myStyle);

        hojaRedEvaluacion.autoSizeColumn(0);
        hojaRedEvaluacion.autoSizeColumn(1);
        hojaRedEvaluacion.autoSizeColumn(2);

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + msg("advance.configuration") + ".xls\"");

            xlsEvaluados.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            mostrarError(log, e);
        }
    }

}
