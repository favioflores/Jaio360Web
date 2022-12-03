package com.jaio360.view;

import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.ElementoDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.domain.EvaluadoCuestionario;
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.Participante;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Favio
 */
@ManagedBean(name = "defineCuesEvaView")
@ViewScoped
public class DefineCuesEvaView extends BaseView implements Serializable {

    private static final Log log = LogFactory.getLog(DefineCuesEvaView.class);

    private static final long serialVersionUID = -1L;

    private List<EvaluadoCuestionario> lstEvaluados;
    private List<EvaluadoCuestionario> lstEvaluadosAsignados;
    private List<Cuestionario> lstCuestionarios;
    private LinkedHashMap<String, String> mapItemsCuestionarios;
    private Integer intCantidadPendientesAsignar;
    private Integer intCantidadAsignados;
    private Integer intCantidadEvadosVeri;
    private Integer intCantidadEvadosNVeri;
    private Integer intCantidadCuesConf;
    private Integer intCantidadCuesNConf;
    private List<EvaluadoCuestionario> lstSelectedEvaluados;
    private List<EvaluadoCuestionario> lstSelectedAsignados;
    private Integer intItEstadoProyecto;
    private Integer idCuestionario;
    private ElementoDAO objElementoDAO = new ElementoDAO();

    public Integer getIntCantidadPendientesAsignar() {
        return intCantidadPendientesAsignar;
    }

    public void setIntCantidadPendientesAsignar(Integer intCantidadPendientesAsignar) {
        this.intCantidadPendientesAsignar = intCantidadPendientesAsignar;
    }

    public Integer getIntCantidadAsignados() {
        return intCantidadAsignados;
    }

    public void setIntCantidadAsignados(Integer intCantidadAsignados) {
        this.intCantidadAsignados = intCantidadAsignados;
    }

    public List<EvaluadoCuestionario> getLstSelectedAsignados() {
        return lstSelectedAsignados;
    }

    public void setLstSelectedAsignados(List<EvaluadoCuestionario> lstSelectedAsignados) {
        this.lstSelectedAsignados = lstSelectedAsignados;
    }

    public List<EvaluadoCuestionario> getLstEvaluadosAsignados() {
        return lstEvaluadosAsignados;
    }

    public void setLstEvaluadosAsignados(List<EvaluadoCuestionario> lstEvaluadosAsignados) {
        this.lstEvaluadosAsignados = lstEvaluadosAsignados;
    }

    public Integer getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(Integer idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public List<EvaluadoCuestionario> getLstSelectedEvaluados() {
        return lstSelectedEvaluados;
    }

    public void setLstSelectedEvaluados(List<EvaluadoCuestionario> lstSelectedEvaluados) {
        this.lstSelectedEvaluados = lstSelectedEvaluados;
    }

    public Integer getIntItEstadoProyecto() {
        return intItEstadoProyecto;
    }

    public LinkedHashMap<String, String> getMapItemsCuestionarios() {
        return mapItemsCuestionarios;
    }

    public void setMapItemsCuestionarios(LinkedHashMap<String, String> mapItemsCuestionarios) {
        this.mapItemsCuestionarios = mapItemsCuestionarios;
    }

    public void setIntItEstadoProyecto(Integer intItEstadoProyecto) {
        this.intItEstadoProyecto = intItEstadoProyecto;
    }

    public Integer getIntCantidadEvadosVeri() {
        return intCantidadEvadosVeri;
    }

    public void setIntCantidadEvadosVeri(Integer intCantidadEvadosVeri) {
        this.intCantidadEvadosVeri = intCantidadEvadosVeri;
    }

    public Integer getIntCantidadEvadosNVeri() {
        return intCantidadEvadosNVeri;
    }

    public void setIntCantidadEvadosNVeri(Integer intCantidadEvadosNVeri) {
        this.intCantidadEvadosNVeri = intCantidadEvadosNVeri;
    }

    public Integer getIntCantidadCuesConf() {
        return intCantidadCuesConf;
    }

    public void setIntCantidadCuesConf(Integer intCantidadCuesConf) {
        this.intCantidadCuesConf = intCantidadCuesConf;
    }

    public Integer getIntCantidadCuesNConf() {
        return intCantidadCuesNConf;
    }

    public void setIntCantidadCuesNConf(Integer intCantidadCuesNConf) {
        this.intCantidadCuesNConf = intCantidadCuesNConf;
    }

    public List<EvaluadoCuestionario> getLstEvaluados() {
        return lstEvaluados;
    }

    public void setLstEvaluados(List<EvaluadoCuestionario> lstEvaluados) {
        this.lstEvaluados = lstEvaluados;
    }

    public List<Cuestionario> getLstCuestionarios() {
        return lstCuestionarios;
    }

    public void setLstCuestionarios(List<Cuestionario> lstCuestionarios) {
        this.lstCuestionarios = lstCuestionarios;
    }

    @PostConstruct
    public void init() {

        this.lstEvaluados = new ArrayList<>();
        this.lstEvaluadosAsignados = new ArrayList<>();
        this.lstCuestionarios = new ArrayList<>();
        this.lstSelectedEvaluados = new ArrayList<>();

        ProyectoInfo objProyectoInfo = Utilitarios.obtenerProyecto();

        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

        ProyectoDAO objProyectoDAO = new ProyectoDAO();

        List<Cuestionario> lstCuestionarios = objCuestionarioDAO.obtenListaCuestionario(Utilitarios.obtenerProyecto().getIntIdProyecto());

        mapItemsCuestionarios = new LinkedHashMap<>();

        for (Cuestionario objCuestionario : lstCuestionarios) {
            mapItemsCuestionarios.put(objCuestionario.getCuTxDescripcion(), objCuestionario.getCuIdCuestionarioPk().toString());
        }

        List<Participante> lstEvaluados = objParticipanteDAO.obtenListaParticipanteXProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        if (!lstEvaluados.isEmpty()) {

            EvaluadoCuestionario objEvaluadoCuestionario;

            /* Busca selecciones grabadas anterioremente */
            HashMap map = obtenerSeleccionAnterior(objCuestionarioDAO, objProyectoInfo);

            for (Participante objParticipante : lstEvaluados) {

                if (objParticipante.getPaInRedCargada().equals(Boolean.TRUE)
                        && objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)
                        && !objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO)) {

                    objEvaluadoCuestionario = new EvaluadoCuestionario();

                    objEvaluadoCuestionario.setIntIdEvaluado(objParticipante.getPaIdParticipantePk());
                    objEvaluadoCuestionario.setStrDescNombre(objParticipante.getPaTxDescripcion());
                    objEvaluadoCuestionario.setStrCargo(objParticipante.getPaTxNombreCargo());
                    objEvaluadoCuestionario.setStrCorreo(objParticipante.getPaTxCorreo());
                    objEvaluadoCuestionario.setStrEstadoEvaluado(msg(objParticipante.getPaIdEstado().toString()));
                    objEvaluadoCuestionario.setIntIdEstadoSel(Constantes.INT_ET_ESTADO_SELECCION_REGISTRADO);
                    objEvaluadoCuestionario.setStrCuestionarioDesc("--");
                    objEvaluadoCuestionario.setPaTxImgUrl(objParticipante.getPaTxImgUrl());
                    objEvaluadoCuestionario.setStrEstadoSel(msg(Constantes.INT_ET_ESTADO_SELECCION_REGISTRADO.toString()));

                    if (!map.isEmpty()) {
                        if (map.containsKey(objEvaluadoCuestionario.getIntIdEvaluado())) {

                            String[] strTemp = (String[]) map.get(objEvaluadoCuestionario.getIntIdEvaluado());
                            objEvaluadoCuestionario.setIntIdCuestionario(Integer.parseInt(strTemp[0]));

                            for (Map.Entry<String, String> objMap : mapItemsCuestionarios.entrySet()) {
                                if (objMap.getValue().equals(objEvaluadoCuestionario.getIntIdCuestionario().toString())) {
                                    objEvaluadoCuestionario.setStrCuestionarioDesc(objMap.getKey());
                                }
                            }

                            objEvaluadoCuestionario.setIntIdEstadoSel(Integer.parseInt(strTemp[1]));
                            objEvaluadoCuestionario.setStrEstadoSel(msg(strTemp[1]));

                            this.lstEvaluadosAsignados.add(objEvaluadoCuestionario);

                            continue;

                        }
                    }

                    this.lstEvaluados.add(objEvaluadoCuestionario);

                }

            }

        }

        calcularResumen();

    }

    private HashMap obtenerSeleccionAnterior(CuestionarioDAO objCuestionarioDAO, ProyectoInfo objProyectoInfo) {

        HashMap map = new HashMap();

        List lstSeleccionAnt = objCuestionarioDAO.obtenerSeleccionAnterior(objProyectoInfo.getIntIdProyecto());

        if (!lstSeleccionAnt.isEmpty()) {

            Iterator itLstSeleccionAnt = lstSeleccionAnt.iterator();

            while (itLstSeleccionAnt.hasNext()) {

                Object[] obj = (Object[]) itLstSeleccionAnt.next();
                String[] strTemp = new String[2];
                strTemp[0] = Integer.toString((Integer) obj[1]); //Id Cuestionario
                strTemp[1] = Integer.toString((Integer) obj[2]); //estado
                map.put(obj[0], strTemp); //KEY --> Id Participante

            }

        }

        return map;
    }

    public void addEvaluadoToCuestionario() {

        try {

            if (!this.lstSelectedEvaluados.isEmpty() && !Utilitarios.esNuloOVacio(this.idCuestionario)) {
                String strCuestionarioDesc = Constantes.strVacio;
                Integer intCuestionarioId = null;

                for (Map.Entry<String, String> objMap : mapItemsCuestionarios.entrySet()) {

                    if (objMap.getValue().equals(this.idCuestionario.toString())) {
                        strCuestionarioDesc = objMap.getKey();
                        intCuestionarioId = Integer.parseInt(objMap.getValue());
                    }

                }

                for (EvaluadoCuestionario objEvaluadoCuestionario : this.lstSelectedEvaluados) {

                    objEvaluadoCuestionario.setStrCuestionarioDesc(strCuestionarioDesc);
                    objEvaluadoCuestionario.setIntIdCuestionario(intCuestionarioId);
                }

                mostrarAlertaInfo("saved.temp");

                calcularResumen();

                this.lstSelectedEvaluados.clear();

            } else {
                if (this.lstSelectedEvaluados.isEmpty()) {
                    mostrarAlertaError("must.select.at.least.evaluated");
                }
                if (Utilitarios.esNuloOVacio(this.idCuestionario)) {
                    mostrarAlertaError("must.select.an.evaluation");
                }
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    public void deleteAsignadoOfCuestionario() {

        try {

            if (!this.lstSelectedEvaluados.isEmpty()) {

                for (EvaluadoCuestionario objEvaluadoCuestionario : this.lstSelectedEvaluados) {

                    objEvaluadoCuestionario.setIntIdCuestionario(null);
                    objEvaluadoCuestionario.setStrCuestionarioDesc("--");

                }

                mostrarAlertaInfo("step3.unassign.success");

                this.lstSelectedEvaluados.clear();

                calcularResumen();

            } else {
                if (this.lstSelectedEvaluados.isEmpty()) {
                    mostrarAlertaError("must.select.at.least.evaluated");
                }
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    public void guardarRelacionCuestionarios() {

        try {

            boolean flagGuardado = false;

            CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

            flagGuardado = objCuestionarioDAO.guardaSeleccion(lstEvaluados, Utilitarios.obtenerProyecto().getIntIdProyecto());

            if (flagGuardado) {
                mostrarAlertaInfo("success");
                init();
            } else {
                mostrarAlertaFatal("error.was.occurred");
            }

        } catch (Exception e) {
            mostrarError(log, e);
            mostrarAlertaFatal("error.was.occurred");
        }

    }

    private void calcularResumen() {

        intCantidadAsignados = 0;
        intCantidadPendientesAsignar = 0;

        for (EvaluadoCuestionario objEvaluadoCuestionario : lstEvaluados) {

            if (Utilitarios.esNuloOVacio(objEvaluadoCuestionario.getIntIdCuestionario())) {
                intCantidadPendientesAsignar++;
            } else {
                intCantidadAsignados++;
            }
        }

    }

    private void calcularContadores() {

        intCantidadEvadosVeri = 0;
        intCantidadEvadosNVeri = 0;
        intCantidadCuesConf = 0;
        intCantidadCuesNConf = 0;

        ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
        CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();

        List<Participante> lstParticipantes = objParticipanteDAO.obtenListaParticipanteXProyecto(Utilitarios.obtenerProyecto().getIntIdProyecto());

        for (Participante objParticipante : lstParticipantes) {

            if (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)
                    && objParticipante.getPaInRedVerificada().equals(Boolean.TRUE)) {
                intCantidadEvadosVeri++;
            }
            if (objParticipante.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)
                    && objParticipante.getPaInRedVerificada().equals(Boolean.FALSE)) {
                intCantidadEvadosNVeri++;
            }
        }

        List<Cuestionario> lstCuestionario = objCuestionarioDAO.obtenListaCuestionario(Utilitarios.obtenerProyecto().getIntIdProyecto());

        for (Cuestionario objCuestionario : lstCuestionario) {
            if (objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO)
                    || objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_EN_EJECUCION)) {
                intCantidadCuesConf++;
            }
            if (objCuestionario.getCuIdEstado().equals(Constantes.INT_ET_ESTADO_CUESTIONARIO_REGISTRADO)) {
                intCantidadCuesNConf++;
            }
        }

    }

}
