package com.jaio360.dao;

import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.LastUpdate;
import com.jaio360.orm.Usuario;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.EncryptDecrypt;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LastUpdateDAO implements Serializable {

    private Session sesion;
    private Transaction tx;

    private static Logger log = Logger.getLogger(LastUpdateDAO.class);

    public List<LastUpdate> obtenListaLastUpdate() throws HibernateException {
        List<LastUpdate> listaLastUpdate = null;
        try {
            iniciaOperacion();
            listaLastUpdate = sesion.createQuery("from LastUpdate order by luDtFecha desc, luTxTipo desc ").setMaxResults(15).list();
        } finally {
            sesion.close();
        }

        return listaLastUpdate;
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
