package com.jaio360.dao;

import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.view.EjecutarEvaluacionView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EjecutarEvaluacionDAO implements Serializable {

    private Session sesion;
    private Transaction tx;
    private Logger log = Logger.getLogger(EjecutarEvaluacionDAO.class);

    public EjecutarEvaluacionDAO() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    /*
    public long guardaRedEvaluacion(RedEvaluacion redEvaluacion) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(redEvaluacion)) ; 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        }  

        return id; 
    }  

    public void actualizaRedEvaluacion(RedEvaluacion redEvaluacion) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(redEvaluacion); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    public void eliminaRedEvaluacion(RedEvaluacion redEvaluacion) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(redEvaluacion); 
            tx.commit(); 
        } catch (HibernateException he) 
        { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  

    public RedEvaluacion obtenRedEvaluacion(long idRedEvaluacion) throws HibernateException 
    { 
        RedEvaluacion redEvaluacion = null;  
        try 
        { 
            iniciaOperacion(); 
            redEvaluacion = (RedEvaluacion) sesion.get(RedEvaluacion.class, (int)idRedEvaluacion); 
        } finally 
        { 
            sesion.close(); 
        }  

        return redEvaluacion; 
    }  

    public List<RedEvaluacion> obtenListaRedEvaluacion(Integer intProyectoPk) throws HibernateException 
    { 
        List<RedEvaluacion> listaRedEvaluacion = null;  

        try 
        { 
            iniciaOperacion(); 
            Query query = sesion.createQuery("from RedEvaluacion r where r.proyecto.poIdProyectoPk = ? "); 
            
            query.setInteger(0, intProyectoPk);
            //query.setBoolean(1, true);
            //query.setBoolean(2, true);
            
            listaRedEvaluacion = query.list();
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaRedEvaluacion; 
    }  
     */
    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        tx = sesion.beginTransaction();
    }

    private void manejaExcepcion(HibernateException he) throws HibernateException {
        tx.rollback();
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he);
    }

    /*
    public List<RedEvaluacion> obtenerListaEvaluadores(Integer intProyectoPk) throws HibernateException {
        
        List<RedEvaluacion> listaRedEvaluacion = null;  

        try{
            
            iniciaOperacion(); 
            Query query = sesion.createQuery("from RedEvaluacion r where r.proyecto.poIdProyectoPk = ? order by r.reIdParticipantePk "); 
            
            query.setInteger(0, intProyectoPk);
            
            listaRedEvaluacion = query.list();
            
        }catch (Exception e){
            log.error(e);
        }finally { 
            sesion.close(); 
        }  

        return listaRedEvaluacion; 
    }
     */
    
    public List obtenerComponenteTipoXEmail(Integer intProyectoPk, String strMail, Integer intTipoComp) throws HibernateException {
        
        List<Componente> listaComponente = new ArrayList<>();

        try 
        { 
            iniciaOperacion();
            
            String sql = " select com.CO_ID_COMPONENTE_PK,                                      " +
                        "         com.CU_ID_CUESTIONARIO_FK,                                    " +
                        "         com.CO_ID_COMPONENTE_REF_FK,                                  " +
                        "         com.CO_ID_TIPO_COMPONENTE,                                    " +
                        "         com.CO_TX_DESCRIPCION                                         " +
                        "   from participante pa                                                " +
                        " 	  inner join cuestionario_evaluado cuev                         " +
                        " 	  on (pa.PA_ID_PARTICIPANTE_PK = cuev.PA_ID_PARTICIPANTE_FK     " +
                        " 	  and pa.PO_ID_PROYECTO_FK = cuev.PO_ID_PROYECTO_FK)            " +
                        " 	  inner join componente com                                     " +
                        " 	  on (com.CU_ID_CUESTIONARIO_FK = cuev.CU_ID_CUESTIONARIO_FK)   " +
                        "   where pa.PO_ID_PROYECTO_FK = ?                                      " +
                        "     and pa.PA_TX_CORREO = ?                                  ";
            
            if(intTipoComp != null){
                sql += "    and com.CO_ID_TIPO_COMPONENTE = ?                                  "; 
            }                                    
            
            
            
            Query query = sesion.createSQLQuery(sql);
            
            query.setInteger(0, intProyectoPk);
            query.setString(1, strMail);
            if(intTipoComp != null){
                query.setInteger(2, intTipoComp);
            } 
            
            Iterator iterator= query.list().iterator();
            while(iterator.hasNext()){
                Object[] tuple= (Object[]) iterator.next();
                
                Componente comp = new Componente();
                
                comp.setCoIdComponentePk((Integer)tuple[0]);
                
                Cuestionario cuest = new Cuestionario();
                cuest.setCuIdCuestionarioPk((Integer)tuple[1]);
                comp.setCuestionario(cuest);
                
                if((tuple[2]) != null){
                    Componente compRef = new Componente();
                    compRef.setCoIdComponentePk((Integer)tuple[2]);
                    comp.setComponente(compRef);
                }
                                
                comp.setCoIdTipoComponente((Integer)tuple[3]);
                comp.setCoTxDescripcion((String)tuple[4]);
                listaComponente.add(comp);
                
            }
            
        } finally 
        { 
            sesion.close(); 
        }  

        return listaComponente; 
    }
    
    public List obtenerComponenteTipoXCustionario(Integer intProyectoPk, Integer intCuestionarioPk, Integer intTipoComp) throws HibernateException {

        List<Componente> listaComponente = new ArrayList<>();

        try {
            iniciaOperacion();

            String sql = " select distinct com.CO_ID_COMPONENTE_PK,                                      "
                    + "         com.CU_ID_CUESTIONARIO_FK,                                    "
                    + "         com.CO_ID_COMPONENTE_REF_FK,                                  "
                    + "         com.CO_ID_TIPO_COMPONENTE,                                    "
                    + "         com.CO_TX_DESCRIPCION                                         "
                    + "   from cuestionario_evaluado cuev                         "
                    + " 	  inner join componente com                                     "
                    + " 	  on (com.CU_ID_CUESTIONARIO_FK = cuev.CU_ID_CUESTIONARIO_FK)   "
                    + "   where cuev.PO_ID_PROYECTO_FK = ?                                      "
                    + "     and cuev.CU_ID_CUESTIONARIO_FK = ?                                  ";

            if (intTipoComp != null) {
                sql += "    and com.CO_ID_TIPO_COMPONENTE = ?                                  ";
            }

            Query query = sesion.createSQLQuery(sql);

            query.setInteger(0, intProyectoPk);
            query.setInteger(1, intCuestionarioPk);

            if (intTipoComp != null) {
                query.setInteger(2, intTipoComp);
            }

            Iterator iterator = query.list().iterator();
            while (iterator.hasNext()) {
                Object[] tuple = (Object[]) iterator.next();

                Componente comp = new Componente();

                comp.setCoIdComponentePk((Integer) tuple[0]);

                Cuestionario cuest = new Cuestionario();
                cuest.setCuIdCuestionarioPk((Integer) tuple[1]);
                comp.setCuestionario(cuest);

                if ((tuple[2]) != null) {
                    Componente compRef = new Componente();
                    compRef.setCoIdComponentePk((Integer) tuple[2]);
                    comp.setComponente(compRef);
                }

                comp.setCoIdTipoComponente((Integer) tuple[3]);
                comp.setCoTxDescripcion((String) tuple[4]);
                listaComponente.add(comp);

            }

        } finally {
            sesion.close();
        }

        return listaComponente;
    }

    public List<DetalleMetrica> obtenerDetalleMetrica(Integer intProyectoPk) throws HibernateException {

        List<DetalleMetrica> listaDetalleMetrica = null;

        try {

            iniciaOperacion();
            Query query = sesion.createQuery("from DetalleMetrica deme where deme.metrica.proyecto.poIdProyectoPk = ? order by deme.deNuOrden ");

            query.setInteger(0, intProyectoPk);

            listaDetalleMetrica = query.list();

        } catch (Exception e) {
            log.error(e);
        } finally {
            sesion.close();
        }

        return listaDetalleMetrica;
    }

}
