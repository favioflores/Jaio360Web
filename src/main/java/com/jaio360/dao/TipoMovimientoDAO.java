package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.TipoMovimiento;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TipoMovimientoDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private static Log log = LogFactory.getLog(TipoMovimientoDAO.class);

    public long guarda(TipoMovimiento usuario) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(usuario));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public boolean actualizar(TipoMovimiento usuario) throws HibernateException {
        boolean correcto = true;
        try {
            iniciaOperacion();
            sesion.update(usuario);
            tx.commit();
        } catch (HibernateException he) {
            correcto = false;
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
        return correcto;
    }

    public void eliminar(TipoMovimiento usuario) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(usuario);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public TipoMovimiento obtenTipoMovimiento(long idUsuario) throws HibernateException {
        TipoMovimiento usuario = null;
        try {
            iniciaOperacion();
            usuario = (TipoMovimiento) sesion.get(TipoMovimientoDAO.class, (int) idUsuario);
        } finally {
            sesion.close();
        }

        return usuario;
    }

    public TipoMovimiento obtenTipoMovimiento(long idUsuario, Session sesion) throws HibernateException {
        TipoMovimiento usuario = null;
        try {
            usuario = (TipoMovimiento) sesion.get(TipoMovimientoDAO.class, (int) idUsuario);
        } catch (HibernateException ex) {
            log.error(ex);
        }

        return usuario;
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

    private void manejaExcepcion() throws HibernateException {
        tx.rollback();
    }

}
