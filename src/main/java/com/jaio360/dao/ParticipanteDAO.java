package com.jaio360.dao;

import com.jaio360.domain.Evaluado;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.CuestionarioEvaluado;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RedEvaluacion;
import com.jaio360.orm.Relacion;
import com.jaio360.utils.Constantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ParticipanteDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private Log log = LogFactory.getLog(ParticipanteDAO.class);

    public ParticipanteDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public boolean guardaParticipante(List<Evaluado> lstEvaluados, Integer intIdProyecto) throws HibernateException {

        boolean correcto = true;

        try {

            iniciaOperacion();

            /* BORRA LOS PARTICIPANTES EN ESTADO REGISTRADO Y SUS RELACIONES */
            Query query = sesion.createQuery("delete from Participante p where p.proyecto.poIdProyectoPk = ? and p.paIdEstado not in (?,?,?) ");
            query.setInteger(0, intIdProyecto);
            query.setInteger(1, Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION);
            query.setInteger(2, Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION);
            query.setInteger(3, Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
            query.executeUpdate();

            Proyecto objProyecto = new Proyecto();
            objProyecto.setPoIdProyectoPk(intIdProyecto);

            Participante objParticipante;

            for (Evaluado obj : lstEvaluados) {

                if (obj.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_EJECUCION)
                        || obj.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION)
                        || obj.getPaIdEstado().equals(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO)) {
                    continue;
                }

                objParticipante = new Participante();

                objParticipante.setPaIdTipoParticipante(Constantes.INT_ET_TIPO_PARTICIPANTE_EVALUADO);
                objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO);
                objParticipante.setPaInAutoevaluar(obj.isPaInAutoevaluar());
                objParticipante.setPaInRedCargada(obj.isPaInRedCargada());
                objParticipante.setPaInRedVerificada(obj.isPaInRedVerificada());
                objParticipante.setPaTxCorreo(obj.getPaTxCorreo());
                objParticipante.setPaTxDescripcion(obj.getPaTxDescripcion());
                objParticipante.setPaTxNombreCargo(obj.getPaTxNombreCargo());
                objParticipante.setPaTxSexo(obj.getPaTxSexo());
                objParticipante.setPaNrEdad(obj.getPaNrEdad());
                objParticipante.setPaNrTiempoTrabajo(obj.getPaNrTiempoTrabajo());
                objParticipante.setPaTxOcupacion(obj.getPaTxOcupacion());
                objParticipante.setPaTxAreaNegocio(obj.getPaTxAreaNegocio());
                objParticipante.setProyecto(objProyecto);

                sesion.save(objParticipante);

            }

            tx.commit();

        } catch (HibernateException he) {
            manejaExcepcion(he);
            log.error(he);
            correcto = false;
        } finally {
            sesion.close();
        }

        return correcto;
    }

    public boolean revertirEvaluadoRed(Integer intIdEvaluado) throws HibernateException {

        boolean correcto = true;

        try {

            Participante objParticipante = obtenParticipante(intIdEvaluado);
            objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_REGISTRADO);
            objParticipante.setPaInRedCargada(false);
            objParticipante.setPaInRedVerificada(false);

            iniciaOperacion();

            /* BORRA LOS PARTICIPANTES EN ESTADO REGISTRADO Y SUS RELACIONES */
            //Query query = sesion.createSQLQuery("delete from relacion_participante where PA_ID_PARTICIPANTE_FK = ? ");
            //query.setInteger(0, intIdEvaluado);
            //query.executeUpdate();
            sesion.update(objParticipante);

            tx.commit();

        } catch (HibernateException he) {
            manejaExcepcion(he);
            log.error(he);
            correcto = false;
        } finally {
            sesion.close();
        }

        return correcto;
    }

    public boolean actualizaParticipante(Participante participante) throws HibernateException {

        boolean correcto = true;
        try {
            iniciaOperacion();
            sesion.update(participante);
            tx.commit();
            //tx.rollback();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            correcto = false;
        } finally {
            sesion.close();
        }

        return correcto;
    }

    public boolean actualizaParticipante(Participante participante, Session sesion) throws HibernateException {

        boolean correcto = true;
        try {
            sesion.update(participante);
        } catch (HibernateException he) {
            correcto = false;
        }

        return correcto;
    }

    public void eliminaParticipanteRelaciones(Participante participante) throws HibernateException {
        try {
            iniciaOperacion();
            /* BORRA LOS PARTICIPANTES EN ESTADO REGISTRADO Y SUS RELACIONES */
            Query query = sesion.createSQLQuery("delete from relacion_participante where PA_ID_PARTICIPANTE_FK = ? ");
            query.setInteger(0, participante.getPaIdParticipantePk());
            query.executeUpdate();

            sesion.delete(participante);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            log.error(he);
        } finally {
            sesion.close();
        }
    }

    public void eliminaParticipante(Participante participante) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(participante);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            log.error(he);
        } finally {
            sesion.close();
        }
    }

    public Participante obtenParticipante(long idParticipante) throws HibernateException {
        Participante participante = null;
        try {
            iniciaOperacion();
            participante = (Participante) sesion.get(Participante.class, (int) idParticipante);
        } finally {
            sesion.close();
        }

        return participante;
    }

    public List<Participante> obtenListaParticipanteXProyecto(Integer intProyectoPk) throws HibernateException {
        List<Participante> listaParticipante = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Participante p where p.proyecto.poIdProyectoPk = ? ");

            query.setInteger(0, intProyectoPk);

            listaParticipante = query.list();

        } finally {
            sesion.close();
        }

        return listaParticipante;
    }

    public List<Participante> obtenListaParticipanteXEstado(Integer intProyectoPk, Integer intEstadoParticipante1, Integer intEstadoParticipante2) throws HibernateException {
        List<Participante> listaParticipante = new ArrayList<>();

        try {
            iniciaOperacion();
            String str = "from Participante p where p.proyecto.poIdProyectoPk = ? and p.paIdEstado in (?,?) ";

            Query query = sesion.createQuery(str);

            query.setInteger(0, intProyectoPk);
            query.setInteger(1, intEstadoParticipante1);
            query.setParameter(2, intEstadoParticipante2);

            listaParticipante = query.list();

        } finally {
            sesion.close();
        }

        return listaParticipante;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }

    public Integer guardaParticipante(Participante objParticipante) throws HibernateException {
        Integer id = 0;

        try {
            iniciaOperacion();
            id = (Integer) sesion.save(objParticipante);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public Participante obtenParticipantePorProyecto(Integer intProyectoPk, String strMail) throws HibernateException {

        Participante objParticipante = null;

        try {

            iniciaOperacion();

            Query query = sesion.createQuery("from Participante p where p.proyecto.poIdProyectoPk = ? and p.paTxCorreo = ? ");

            query.setInteger(0, intProyectoPk);
            query.setString(1, strMail);

            objParticipante = (Participante) query.uniqueResult();

        } finally {
            sesion.close();
        }

        return objParticipante;
    }

    public List obtenerNroParticipantes(Integer intProyectoPk) throws HibernateException {

        try {

            iniciaOperacion();

            String sql
                    = "  select idParticipante, sum(cantidad_participantes) from ( "
                    + "select p.PA_ID_PARTICIPANTE_PK as idParticipante, "
                    + "1 as cantidad_participantes "
                    + "from jaio.participante p "
                    + "where p.PA_ID_ESTADO in (72) "
                    + "and p.PO_ID_PROYECTO_FK = :intProyectoPk "
                    + "and p.PA_IN_AUTOEVALUAR = true "
                    + "union ALL "
                    + "select p.PA_ID_PARTICIPANTE_PK as idParticipante, "
                    + "count(rp.RE_ID_PARTICIPANTE_FK) as cantidad_participantes "
                    + "from jaio.participante p, jaio.relacion_participante rp "
                    + "where p.PA_ID_ESTADO in (72) "
                    + "and p.PO_ID_PROYECTO_FK = :intProyectoPk "
                    + "and p.PA_ID_PARTICIPANTE_PK = rp.PA_ID_PARTICIPANTE_FK "
                    + "and rp.RP_ID_ESTADO = 79 group by p.PA_ID_PARTICIPANTE_PK ) datos group by idParticipante ";

            Query query = sesion.createSQLQuery(sql);

            query.setInteger("intProyectoPk", intProyectoPk);

            return query.list();

        } finally {
            sesion.close();
        }

    }

    public List obtenerRedCompletaXProyecto(Integer intProyectoPk) throws HibernateException {

        try {

            iniciaOperacion();

            String sql
                    = "  SELECT\n"
                    + "	p.PA_TX_DESCRIPCION,\n"
                    + "	p.PA_TX_CORREO,\n"
                    + "	re.RE_TX_DESCRIPCION,\n"
                    + "	re.RE_TX_CORREO,\n"
                    + "	r.RE_TX_NOMBRE,\n"
                    + "	r.RE_TX_ABREVIATURA,\n"
                    + "	r.RE_COLOR\n"
                    + "FROM\n"
                    + "	participante p,\n"
                    + "	relacion_participante rp,\n"
                    + "	red_evaluacion re,\n"
                    + "	relacion r\n"
                    + "where\n"
                    + "	p.PO_ID_PROYECTO_FK = :intProyectoPk \n"
                    + "	and p.PA_ID_PARTICIPANTE_PK = rp.PA_ID_PARTICIPANTE_FK\n"
                    + "	and rp.RE_ID_PARTICIPANTE_FK = re.RE_ID_PARTICIPANTE_PK\n"
                    + "	and rp.RE_ID_RELACION_FK = r.RE_ID_RELACION_PK  ";

            Query query = sesion.createSQLQuery(sql);

            query.setInteger("intProyectoPk", intProyectoPk);

            return query.list();

        } finally {
            sesion.close();
        }

    }

    public List<Participante> obtenListaParticipanteConfirmados(Integer intProyectoPk) throws HibernateException {

        List<Participante> listaParticipante = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Participante p where p.proyecto.poIdProyectoPk = ? and p.paInRedCargada = ? and p.paInRedVerificada = ? and p.paIdEstado = ? ");

            query.setInteger(0, intProyectoPk);
            query.setBoolean(1, true);
            query.setBoolean(2, true);
            query.setInteger(3, Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION);

            listaParticipante = query.list();

        } finally {
            sesion.close();
        }

        return listaParticipante;

    }

    public boolean actualizarParticipantesRegistrados(Integer intIdProyecto, List<Participante> lstParticipantes) {

        boolean correcto = true;

        try {

            iniciaOperacion();

            for (Participante objParticipante : lstParticipantes) {

                objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_EN_PARAMETRIZACION);
                sesion.update(objParticipante);

            }

            tx.commit();

        } catch (HibernateException he) {
            manejaExcepcion(he);
            correcto = false;
        } finally {
            sesion.close();
        }

        return correcto;
    }

}
