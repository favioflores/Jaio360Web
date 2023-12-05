package com.jaio360.dao;

import com.jaio360.domain.ProyectoInfo;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.ReporteGenerado;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Utilitarios;
import com.jaio360.view.BaseView;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ReporteGeneradoDAO extends BaseView implements Serializable {

    private Session sesion;
    private Transaction tx;

    private Logger log = Logger.getLogger(ReporteGeneradoDAO.class);

    public ReporteGeneradoDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public long guardaReporteGenerado(ReporteGenerado objReporteGenerado) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(objReporteGenerado));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public void actualizaReporteGenerado(ReporteGenerado objReporteGenerado) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.update(objReporteGenerado);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public ReporteGenerado obtenReporteGenerado(long idReporteGenerado) throws HibernateException {
        ReporteGenerado objReporteGenerado = null;
        try {
            iniciaOperacion();
            objReporteGenerado = (ReporteGenerado) sesion.get(ReporteGenerado.class, (int) idReporteGenerado);
        } finally {
            sesion.close();
        }

        return objReporteGenerado;
    }

    public List<ReporteGenerado> obtenListaReporteGenerado(Integer idUsuario, Integer idProyecto) throws HibernateException {
        List<ReporteGenerado> listaReporteGenerado = new ArrayList<>();

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from ReporteGenerado where usuario.usIdCuentaPk = ? and proyecto.poIdProyectoPk = ? order by rgReportePk desc ");
            query.setInteger(0, idUsuario);
            query.setInteger(1, idProyecto);
            listaReporteGenerado = query.list();

            for (ReporteGenerado objReporteGenerado : listaReporteGenerado) {
                objReporteGenerado.setRgStrEstado(msg(objReporteGenerado.getRgEstado().toString()));
                objReporteGenerado.setRgTxTimeElapsed(Utilitarios.calcularDiferenciaDeFechas(objReporteGenerado.getRgDtFechaRegistro(), new Date()));
                objReporteGenerado.setRgTxFechaEliminacion(Utilitarios.calcularDiferenciaDeFechas(new Date(), objReporteGenerado.getRgDtFechaExpiracion()));
            }

        } catch (HibernateException e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            sesion.close();
        }

        return listaReporteGenerado;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }

}
