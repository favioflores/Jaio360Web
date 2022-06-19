package com.jaio360.dao;

import com.jaio360.orm.Contrato;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.utils.Constantes;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ContratoDAO implements Serializable {

    private Session sesion;
    private Transaction tx;
    private Log log = LogFactory.getLog(ContratoDAO.class);

    public ContratoDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public long guardaContrato(Contrato contrato) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(contrato));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public void actualizaContrato(Contrato contrato) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.update(contrato);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public void eliminaContrato(Contrato contrato) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(contrato);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public Contrato obtenContrato(long idContrato) throws HibernateException {
        Contrato contrato = null;
        try {
            iniciaOperacion();
            contrato = (Contrato) sesion.get(Contrato.class, (int) idContrato);
        } finally {
            sesion.close();

        }

        return contrato;
    }

    public List<Contrato> obtenListaContrato() throws HibernateException {
        List<Contrato> listaContrato = null;

        try {
            iniciaOperacion();
            listaContrato = sesion.createQuery("from Contrato").list();
        } finally {
            sesion.close();

        }

        return listaContrato;
    }

    public List<Contrato> obtenListaContratoPorUsuario(Integer intIdUsuario) throws HibernateException {
        List<Contrato> listaContrato = null;

        try {
            iniciaOperacion();
            Query sql = sesion.createQuery("from Contrato co where co.usuario.usIdCuentaPk = :idUsuario and co.coNuLicenciaDisponible > 0 order by co.coIdContratoPk desc ");
            sql.setInteger("idUsuario", intIdUsuario);
            listaContrato = sql.list();
        } finally {
            sesion.close();

        }

        return listaContrato;
    }

    public List<Contrato> obtenListaContratoPorUsuarioConfirmados(Integer intIdUsuario) throws HibernateException {
        List<Contrato> listaContrato = null;

        try {
            iniciaOperacion();
            Query sql = sesion.createQuery("from Contrato co where co.usuario.usIdCuentaPk = :idUsuario and co.coIdEstado = :idConfirmado order by co.coIdContratoPk ");
            sql.setInteger("idUsuario", intIdUsuario);
            sql.setInteger("idConfirmado", Constantes.INT_ET_ESTADO_CONTRATO_CONFIRMADO);
            listaContrato = sql.list();
        } finally {
            sesion.close();
        }

        return listaContrato;
    }

    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        log.error(he);
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }
}
