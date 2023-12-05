package com.jaio360.dao;

import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Mensaje;
import java.io.Serializable;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MensajeDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private static Logger log = Logger.getLogger(MensajeDAO.class);

    public MensajeDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public long guardaMensaje(Mensaje mensaje) throws HibernateException {
        long id = 0;

        try {
            iniciaOperacion();
            id = Long.valueOf((Integer) sesion.save(mensaje));
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }

        return id;
    }

    public void actualizaMensaje(Mensaje mensaje) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.update(mensaje);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public void eliminaMensaje(Mensaje mensaje) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(mensaje);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    public Mensaje obtenMensaje(Integer intIdProyecto, Integer intTipoNotificacion) throws HibernateException {
        Mensaje mensaje = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Mensaje m where m.meIdTipoMensaje = ? and m.proyecto.poIdProyectoPk = ?");

            query.setInteger(0, intTipoNotificacion);
            query.setInteger(1, intIdProyecto);

            mensaje = (Mensaje) query.uniqueResult();

        } catch (HibernateException e) {
            log.error(e);
        } finally {
            sesion.close();
        }

        return mensaje;
    }

    public Mensaje obtenMensaje(Integer intIdProyecto, Integer intTipoNotificacion, Session sesion) throws HibernateException {
        Mensaje mensaje = null;

        try {
            Query query = sesion.createQuery("from Mensaje m where m.meIdTipoMensaje = ? and m.proyecto.poIdProyectoPk = ?");

            query.setInteger(0, intTipoNotificacion);
            query.setInteger(1, intIdProyecto);

            mensaje = (Mensaje) query.uniqueResult();

        } catch (HibernateException e) {
            log.error(e);
        }

        return mensaje;
    }

    public List<Mensaje> obtenListaMensaje(Integer intIdProyecto) throws HibernateException {
        List<Mensaje> listaMensaje = null;

        try {
            iniciaOperacion();
            Query query = sesion.createQuery("from Mensaje m where m.proyecto.poIdProyectoPk = ? ");

            query.setInteger(0, intIdProyecto);

            listaMensaje = query.list();

        } finally {
            sesion.close();
        }

        return listaMensaje;
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
