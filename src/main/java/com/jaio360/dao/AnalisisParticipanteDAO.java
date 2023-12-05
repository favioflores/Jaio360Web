package com.jaio360.dao;

import com.jaio360.orm.AnalisisParticipante;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.view.BaseView;
import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AnalisisParticipanteDAO extends BaseView implements Serializable {

    private Session sesion;
    private Transaction tx;

    private Logger log = Logger.getLogger(AnalisisParticipanteDAO.class);

    public AnalisisParticipanteDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public long guardaAnalisisParticipante(AnalisisParticipante objAnalisisParticipante) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(objAnalisisParticipante));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }
    
    public List<AnalisisParticipante> obtenListaAnalisisParticipante(Integer idReporte) throws HibernateException {
        List<AnalisisParticipante> listaAnalisisParticipante = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from AnalisisParticipante ap where ap.reporteGenerado.rgReportePk = ? order by ap.apIdAnalisisPk ");
            query.setInteger(0, idReporte);
            listaAnalisisParticipante = query.list();

        } catch (HibernateException e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            sesion.close();
        }

        return listaAnalisisParticipante;
    }

}
