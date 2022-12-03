package com.jaio360.dao;

import com.jaio360.domain.EvaluadoAvan;
import com.jaio360.domain.EvaluadorRelacion;
import com.jaio360.domain.RelacionAvanzada;
import com.jaio360.domain.RelacionBean;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.RelacionParticipanteId;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CargaAvanzadaDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private Log log = LogFactory.getLog(CargaAvanzadaDAO.class);

    public CargaAvanzadaDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }

    public boolean eliminarRegistrosAvanzado(Proyecto objProyecto) {

        boolean correcto = false;

        try {

            iniciaOperacion();

            Query query1 = sesion.createSQLQuery("delete from relacion_participante where pa_id_participante_fk in ( select pa_id_Participante_pk from participante where po_id_proyecto_fk = ? ) ");
            Query query2 = sesion.createSQLQuery("delete from relacion where po_id_proyecto_fk = ? ");
            Query query5 = sesion.createSQLQuery("delete from cuestionario_evaluado where po_id_proyecto_fk = ? ");
            Query query3 = sesion.createSQLQuery("delete from participante where po_id_proyecto_fk = ? ");
            Query query4 = sesion.createSQLQuery("delete from red_evaluacion where po_id_Proyecto_fk = ? ");
            

            query1.setInteger(0, objProyecto.getPoIdProyectoPk());
            query2.setInteger(0, objProyecto.getPoIdProyectoPk());
            query5.setInteger(0, objProyecto.getPoIdProyectoPk());
            query3.setInteger(0, objProyecto.getPoIdProyectoPk());
            query4.setInteger(0, objProyecto.getPoIdProyectoPk());

            query1.executeUpdate();
            query2.executeUpdate();
            query5.executeUpdate();
            query3.executeUpdate();
            query4.executeUpdate();

            tx.commit();
            correcto = true;

        } catch (HibernateException he) {
            manejaExcepcion(he);
            log.error(he);
            correcto = false;
        } catch (Exception e) {
            tx.rollback();
            log.error(e);
            correcto = false;
        } finally {
            sesion.close();
        }

        return correcto;

    }

    public boolean guardaRegistros(List<EvaluadoAvan> lstAvanPersonas, List<RelacionBean> lstAvanRelacion, List<RelacionAvanzada> lstRelacionAvanzadas, Map mapPersonasAvanzado, Map mapRelacionesAbrev, Proyecto objProyecto, Map mapPerEvaluados, Map mapPerEvaluadores) {

        boolean correcto = false;

        Map mRelacion = new HashMap();
        Map mParticipante = new HashMap();
        Map mRedEvaluacion = new HashMap();

        try {

            iniciaOperacion();

            // GUARDA RELACIONES
            for (RelacionBean objRelacionBean : lstAvanRelacion) {
                Relacion objRelacion = new Relacion();
                objRelacion.setReTxNombre(objRelacionBean.getStrNombre());
                objRelacion.setReTxAbreviatura(objRelacionBean.getStrAbreviatura());
                objRelacion.setReTxDescripcion(objRelacionBean.getStrDescripcion());
                objRelacion.setReColor(objRelacionBean.getStrColor());
                objRelacion.setReIdEstado(Constantes.INT_ET_ESTADO_RELACION_REGISTRADO);
                objRelacion.setProyecto(objProyecto);
                objRelacion.setReIdRelacionPk((Integer) sesion.save(objRelacion));
                mRelacion.put(Utilitarios.limpiarTexto(objRelacionBean.getStrNombre()), objRelacion);
            }

            // GUARDA EVALUADOS 
            for (EvaluadoAvan objEvaluadoAvan : lstAvanPersonas) {
                if (mapPerEvaluados.containsKey(Utilitarios.limpiarTexto(objEvaluadoAvan.getPaTxCorreo()))) {
                    Participante objParticipante = new Participante();
                    objParticipante.setPaTxDescripcion(objEvaluadoAvan.getPaTxDescripcion());
                    objParticipante.setPaTxCorreo(objEvaluadoAvan.getPaTxCorreo());
                    objParticipante.setPaTxNombreCargo(objEvaluadoAvan.getPaTxNombreCargo());
                    objParticipante.setPaInRedCargada(true);
                    objParticipante.setPaInRedVerificada(true);
                    objParticipante.setPaTxCorreo(objEvaluadoAvan.getPaTxCorreo());
                    objParticipante.setPaIdTipoParticipante(Constantes.INT_ET_TIPO_PARTICIPANTE_EVALUADO);
                    objParticipante.setPaInAutoevaluar(objEvaluadoAvan.isPaInAutoevaluar());
                    objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION);
                    objParticipante.setPaTxSexo(objEvaluadoAvan.getPaTxSexo());
                    objParticipante.setPaNrEdad(objEvaluadoAvan.getPaNrEdad());
                    objParticipante.setPaNrTiempoTrabajo(objEvaluadoAvan.getPaNrTiempoTrabajo());
                    objParticipante.setPaTxOcupacion(objEvaluadoAvan.getPaTxOcupacion());
                    objParticipante.setPaTxAreaNegocio(objEvaluadoAvan.getPaTxAreaNegocio());
                    objParticipante.setProyecto(objProyecto);
                    objParticipante.setPaTxImgUrl(objEvaluadoAvan.getPaTxImgUrl());
                    objParticipante.setPaIdParticipantePk((Integer) sesion.save(objParticipante));
                    mParticipante.put(Utilitarios.limpiarTexto(objParticipante.getPaTxCorreo()), objParticipante);
                }
            }

            // GUARDA EVALUADORES
            for (EvaluadoAvan objEvaluadoAvan : lstAvanPersonas) {
                if (mapPerEvaluadores.containsKey(Utilitarios.limpiarTexto(objEvaluadoAvan.getPaTxCorreo()))) {
                    RedEvaluacion objRedEvaluacion = new RedEvaluacion();
                    objRedEvaluacion.setReTxDescripcion(objEvaluadoAvan.getPaTxDescripcion());
                    objRedEvaluacion.setReTxCorreo(objEvaluadoAvan.getPaTxCorreo());
                    objRedEvaluacion.setReTxNombreCargo(objEvaluadoAvan.getPaTxNombreCargo());
                    objRedEvaluacion.setReTxCorreo(objEvaluadoAvan.getPaTxCorreo());
                    objRedEvaluacion.setReIdTipoParticipante(Constantes.INT_ET_TIPO_PARTICIPANTE_EVALUADOR);
                    objRedEvaluacion.setReIdEstado(Constantes.INT_ET_ESTADO_EVALUADOR_EN_PARAMETRIZACION);
                    objRedEvaluacion.setReTxSexo(objEvaluadoAvan.getPaTxSexo());
                    objRedEvaluacion.setReNrEdad(objEvaluadoAvan.getPaNrEdad());
                    objRedEvaluacion.setReNrTiempoTrabajo(objEvaluadoAvan.getPaNrTiempoTrabajo());
                    objRedEvaluacion.setReTxOcupacion(objEvaluadoAvan.getPaTxOcupacion());
                    objRedEvaluacion.setReTxAreaNegocio(objEvaluadoAvan.getPaTxAreaNegocio());
                    objRedEvaluacion.setProyecto(objProyecto);
                    objRedEvaluacion.setReIdParticipantePk((Integer) sesion.save(objRedEvaluacion));
                    mRedEvaluacion.put(Utilitarios.limpiarTexto(objRedEvaluacion.getReTxCorreo()), objRedEvaluacion);
                }
            }

            // GUARDA RED
            for (RelacionAvanzada objRelacionAvanzada : lstRelacionAvanzadas) {

                if (!Utilitarios.limpiarTexto(objRelacionAvanzada.getStrEvaluado()).equals(Utilitarios.limpiarTexto(objRelacionAvanzada.getStrEvaluador()))) {

                    RelacionParticipanteId objRelacionParticipanteId = new RelacionParticipanteId();

                    Participante objParticipante = (Participante) mParticipante.get(Utilitarios.limpiarTexto(objRelacionAvanzada.getStrEvaluado()));
                    RedEvaluacion objRedEvaluacion = (RedEvaluacion) mRedEvaluacion.get(Utilitarios.limpiarTexto(objRelacionAvanzada.getStrEvaluador()));
                    Relacion objRelacion = (Relacion) mRelacion.get(Utilitarios.limpiarTexto(objRelacionAvanzada.getStrRelacion()));

                    objRelacionParticipanteId.setPaIdParticipanteFk(objParticipante.getPaIdParticipantePk());
                    objRelacionParticipanteId.setReIdParticipanteFk(objRedEvaluacion.getReIdParticipantePk());
                    objRelacionParticipanteId.setReIdRelacionFk(objRelacion.getReIdRelacionPk());

                    RelacionParticipante objRelacionParticipante = new RelacionParticipante();
                    objRelacionParticipante.setParticipante(objParticipante);
                    objRelacionParticipante.setRedEvaluacion(objRedEvaluacion);
                    objRelacionParticipante.setRelacion(objRelacion);
                    objRelacionParticipante.setId(objRelacionParticipanteId);
                    objRelacionParticipante.setRpIdEstado(Constantes.INT_ET_ESTADO_RELACION_EDO_EDOR_REGISTRADO);

                    sesion.save(objRelacionParticipante);
                }

            }

            // CAMBIA DE ESTADO EL PROYECTO
            objProyecto.setPoIdEstado(Constantes.INT_ET_ESTADO_PROYECTO_EN_PARAMETRIZACION);
            sesion.update(objProyecto);

            tx.commit();
            correcto = true;
        } catch (HibernateException he) {
            manejaExcepcion(he);
            log.error(he);
            correcto = false;
        } catch (Exception e) {
            tx.rollback();
            log.error(e);
            correcto = false;
        } finally {
            sesion.close();
        }

        return correcto;
    }

}
