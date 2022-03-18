package com.jaio360.dao;

import com.jaio360.domain.Categorias;
import com.jaio360.domain.CuestionarioImportado;
import com.jaio360.domain.Evaluado;
import com.jaio360.domain.EvaluadoCuestionario;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.CuestionarioEvaluado;
import com.jaio360.orm.CuestionarioEvaluadoId;
import com.jaio360.orm.HibernateUtil;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CuestionarioDAO implements Serializable
{  
    private Session sesion; 
    private Transaction tx;  

    private Log log = LogFactory.getLog(CuestionarioDAO.class);
    
    public long guardaCuestionario(Cuestionario cuestionario) throws HibernateException 
    { 
        long id = 0;  

        try 
        { 
            iniciaOperacion(); 
            id = Long.valueOf((Integer)sesion.save(cuestionario)) ; 
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

    public void actualizaCuestionario(Cuestionario cuestionario) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.update(cuestionario); 
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

    public void eliminaCuestionario(Cuestionario cuestionario) throws HibernateException 
    { 
        try 
        { 
            iniciaOperacion(); 
            sesion.delete(cuestionario); 
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
    
    public boolean eliminaCuestionarioById(Integer intIdCuestionario) throws HibernateException { 
        
        try { 
            
            iniciaOperacion(); 
            
            Query query1 = sesion.createQuery("delete from Componente c where c.cuestionario.cuIdCuestionarioPk = ? ");
            Query query2 = sesion.createQuery("delete from CuestionarioEvaluado c where c.cuestionario.cuIdCuestionarioPk = ? ");
            Query query3 = sesion.createQuery("delete from Cuestionario c where c.cuIdCuestionarioPk = ? ");
            
            query1.setInteger(0, intIdCuestionario);
            query2.setInteger(0, intIdCuestionario);
            query3.setInteger(0, intIdCuestionario);
            
            query1.executeUpdate();
            query2.executeUpdate();
            query3.executeUpdate();
            
            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            return false;
        } finally { 
            sesion.close(); 
        } 
        return true;
        
    }  
    public boolean eliminaCuestionarioTotal() throws HibernateException { 
        
        try { 
            
            iniciaOperacion(); 
            
            Query query1 = sesion.createQuery("delete from Componente c where c.cuestionario.cuIdCuestionarioPk in "
                    + "(select cu.cuIdCuestionarioPk from Cuestionario cu where cu.proyecto.poIdProyectoPk = ? ) ");
            Query query2 = sesion.createQuery("delete from CuestionarioEvaluado c where c.cuestionario.cuIdCuestionarioPk in"
                    + "(select cu.cuIdCuestionarioPk from Cuestionario cu where cu.proyecto.poIdProyectoPk = ? )");
            Query query3 = sesion.createQuery("delete from Cuestionario c where c.proyecto.poIdProyectoPk = ? ");
            
            query1.setInteger(0, Utilitarios.obtenerProyecto().getIntIdProyecto());
            query2.setInteger(0, Utilitarios.obtenerProyecto().getIntIdProyecto());
            query3.setInteger(0, Utilitarios.obtenerProyecto().getIntIdProyecto());
            
            query1.executeUpdate();
            query2.executeUpdate();
            query3.executeUpdate();
            
            tx.commit(); 
            
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            return false;
        } finally { 
            sesion.close(); 
        } 
        return true;
        
    }  
    
    public Cuestionario obtenCuestionario(long idCuestionario) throws HibernateException 
    { 
        Cuestionario cuestionario = null;  
        try 
        { 
            iniciaOperacion(); 
            cuestionario = (Cuestionario) sesion.get(Cuestionario.class, (int)idCuestionario); 
        } finally 
        { 
            sesion.close(); 
        }  

        return cuestionario; 
    }  

    private void iniciaOperacion() throws HibernateException 
    { 
        sesion = HibernateUtil.getSessionFactory().openSession(); 
        tx = sesion.beginTransaction(); 
    }  

    private void manejaExcepcion(HibernateException he) throws HibernateException 
    { 
        tx.rollback(); 
        throw new HibernateException("Ocurrió un error en la capa de acceso a datos", he); 
    } 
    
    public List obtenItemsBiblioteca(String strCorreoUsuario, String strMetodologia, String strEstadoProyecto) throws HibernateException { 
        
        List lstItems = new ArrayList();

        try {
            
            iniciaOperacion(); 
            
            String tempQuery = "SELECT PO.PO_ID_METODOLOGIA, " +
                               "	PO.PO_ID_PROYECTO_PK, " +
                               " 	PO.PO_TX_DESCRIPCION, " +
                               " 	PO.PO_FE_REGISTRO, " +
                               " 	CU.CU_ID_CUESTIONARIO_PK, " +
                               " 	CU.CU_TX_DESCRIPCION, " +
                               "	CO.CO_ID_COMPONENTE_PK, " +
                               "  	CO.CO_ID_TIPO_COMPONENTE, " +
                               "  	CO.CO_TX_DESCRIPCION c1 , " +
                               "        PADRE.CO_TX_DESCRIPCION c2 " +
                               "  FROM PROYECTO PO, " +
                               "       USUARIO US, " +
                               "       CUESTIONARIO CU, " +
                               "       COMPONENTE CO LEFT JOIN COMPONENTE PADRE ON CO.CO_ID_COMPONENTE_REF_FK = PADRE.CO_ID_COMPONENTE_PK " +
                               " WHERE PO.US_ID_CUENTA_FK = US.US_ID_CUENTA_PK " +
                               "   AND US.US_ID_MAIL = :p_correo " +
                               "   AND CU.PO_ID_PROYECTO_FK = PO.PO_ID_PROYECTO_PK ";
                               if(Utilitarios.noEsNuloOVacio(strMetodologia)){
                                tempQuery += "   AND PO.PO_ID_METODOLOGIA = :p_metodologia ";
                               }
                               if(Utilitarios.noEsNuloOVacio(strEstadoProyecto)){
                                tempQuery += "   AND PO.PO_ID_ESTADO = :p_estado ";
                               }
                               tempQuery += "   AND CO.CU_ID_CUESTIONARIO_FK = CU.CU_ID_CUESTIONARIO_PK " +
                               " ORDER BY 1,2,4,5,7 ";
            
            Query query = sesion.createSQLQuery(tempQuery)
                    .addScalar("PO_ID_METODOLOGIA")
                    .addScalar("PO_ID_PROYECTO_PK")
                    .addScalar("PO_TX_DESCRIPCION")
                    .addScalar("PO_FE_REGISTRO")
                    .addScalar("CU_ID_CUESTIONARIO_PK")
                    .addScalar("CU_TX_DESCRIPCION")
                    .addScalar("CO_ID_COMPONENTE_PK")
                    .addScalar("CO_ID_TIPO_COMPONENTE")
                    .addScalar("c1")
                    .addScalar("c2");
            
            query.setString("p_correo", strCorreoUsuario);
            
            if(Utilitarios.noEsNuloOVacio(strMetodologia)){
                query.setInteger("p_metodologia", Integer.parseInt(strMetodologia));
            }
            if(Utilitarios.noEsNuloOVacio(strEstadoProyecto)){
                query.setInteger("p_estado", Integer.parseInt(strEstadoProyecto));
            }
            
            lstItems = query.list();
                        
        } catch (Exception e){
            log.error(e);
        } finally { 
            sesion.close(); 
        }  
        return lstItems;
        
    }
    
    
    public List<Cuestionario> obtenListaCuestionarioXEstado(Integer intIdProyecto, Integer intEstadoCuestionario) throws HibernateException{ 
        
        List<Cuestionario> listaCuestionario = null;  

        try { 
        
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("from Cuestionario c where c.proyecto.poIdProyectoPk = ? and c.cuIdEstado = ? order by c.cuTxDescripcion asc ");
            query.setInteger(0, intIdProyecto);
            query.setInteger(1, intEstadoCuestionario);
            
            listaCuestionario = query.list(); 
        
        } finally { 
            sesion.close(); 
        }  

        return listaCuestionario; 
    }  
    
    public List<Cuestionario> obtenListaCuestionario(Integer intIdProyecto) throws HibernateException{ 
        
        List<Cuestionario> listaCuestionario = null;  

        try { 
        
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("from Cuestionario c where c.proyecto.poIdProyectoPk = ? ");
            
            query.setInteger(0, intIdProyecto);  
            
            listaCuestionario = query.list(); 
        
        } finally { 
            sesion.close(); 
        }  

        return listaCuestionario; 
    }  
    
    public List<CuestionarioEvaluado> obtenListaRelacionCuestionarioEvaluado(Integer intIdProyecto) throws HibernateException{ 
        
        List<CuestionarioEvaluado> listaCuestionario = null;  

        try { 
        
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("select ce from CuestionarioEvaluado ce where ce.proyecto.poIdProyectoPk = ? ");
            
            query.setInteger(0, intIdProyecto);  
            
            listaCuestionario = query.list(); 
        
        } finally { 
            sesion.close(); 
        }  

        return listaCuestionario; 
    }  

    public List obtenerSeleccionAnterior(Integer intIdProyecto) {
        
        List lista = null;  

        try{
            
            iniciaOperacion(); 
            Query query = sesion.createQuery(" select c.id.paIdParticipanteFk, c.id.cuIdCuestionarioFk, c.ceIdEstado " +
                                             "   from CuestionarioEvaluado c " +
                                             " where c.proyecto.poIdProyectoPk = ? "); 
            
            query.setInteger(0, intIdProyecto);
            
            lista = query.list();
            
        }catch (Exception e){
            log.error(e);
        }finally { 
            sesion.close(); 
        }  

        return lista;
    
    }

    public boolean guardaSeleccion(List<EvaluadoCuestionario> lstEvaluados, Integer intIdProyecto) {
        
        boolean flag = true;
        try{ 
            
            iniciaOperacion(); 
            
            /* Borramos datos anteriores */ 
            /* SOLO BORRA LAS SELECCIONES DE EVALUADOS QUE NO ESTAN EN EJECUCION */
            /*
            Query query = sesion.createQuery("delete from CuestionarioEvaluado c where c.id.poIdProyectoFk = ? and c.ceIdEstado != ? ");
            query.setInteger(0, intIdProyecto);
            query.setInteger(1, Constantes.INT_ET_ESTADO_SELECCION_EN_EJECUCION);
            query.executeUpdate();
            */
            
            /* Insertamos los datos nuevos */
            CuestionarioEvaluadoId objCuestionarioEvaluadoId;
            CuestionarioEvaluado objCuestionarioEvaluado;
                    
            for (EvaluadoCuestionario objEvaluadoCuestionario : lstEvaluados){
                
                if(Utilitarios.noEsNuloOVacio(objEvaluadoCuestionario.getIntIdCuestionario())&&
                   !objEvaluadoCuestionario.getIntIdEstadoSel().equals(Constantes.INT_ET_ESTADO_SELECCION_EN_EJECUCION)){
              
                    objCuestionarioEvaluadoId = new CuestionarioEvaluadoId();
                    objCuestionarioEvaluadoId.setCuIdCuestionarioFk(objEvaluadoCuestionario.getIntIdCuestionario());
                    objCuestionarioEvaluadoId.setPaIdParticipanteFk(objEvaluadoCuestionario.getIntIdEvaluado());
                    objCuestionarioEvaluadoId.setPoIdProyectoFk(intIdProyecto);
                    
                    objCuestionarioEvaluado = new CuestionarioEvaluado();
                    objCuestionarioEvaluado.setId(objCuestionarioEvaluadoId);
                    objCuestionarioEvaluado.setCeIdEstado(Constantes.INT_ET_ESTADO_SELECCION_REGISTRADO);
                    objCuestionarioEvaluado.setCuestionario((Cuestionario) sesion.get(Cuestionario.class, (int)objEvaluadoCuestionario.getIntIdCuestionario()));
                    objCuestionarioEvaluado.setParticipante((Participante) sesion.get(Participante.class, (int)objEvaluadoCuestionario.getIntIdEvaluado()));
                    objCuestionarioEvaluado.setProyecto((Proyecto) sesion.get(Proyecto.class, (int)intIdProyecto));
                    
                    sesion.save(objCuestionarioEvaluado);
                }
            }

            tx.commit(); 
            
        } catch (HibernateException he){ 
            log.error(he);
            manejaExcepcion(he); 
            flag = false;
        } finally { 
            sesion.close(); 
        }  

        return flag; 

    }
 
    
    
    public void actualizaEstadoCuestionarioXEvaluado(Evaluado objEvaluado, Integer intIdProyecto, Integer intIdEstado) throws HibernateException { 
        try { 
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("update CuestionarioEvaluado c set c.ceIdEstado = ? where c.participante.paIdParticipantePk = ? and c.proyecto.poIdProyectoPk = ? ");
            query.setInteger(0, intIdEstado);
            query.setInteger(1, objEvaluado.getPaIdParticipantePk());
            query.setInteger(2, intIdProyecto);
            query.executeUpdate();
            
            tx.commit(); 
        } catch (HibernateException he) { 
            manejaExcepcion(he); 
            throw he; 
        } finally 
        { 
            sesion.close(); 
        } 
    }  
    
    public boolean actualizaEstadoCuestionarioXEvaluado(Evaluado objEvaluado, Integer intIdProyecto, Integer intIdEstado, Session sesion) throws HibernateException { 
        
        boolean correcto = true;
        try { 
            
            Query query = sesion.createQuery("update CuestionarioEvaluado c set c.ceIdEstado = ? where c.participante.paIdParticipantePk = ? and c.proyecto.poIdProyectoPk = ? ");
            query.setInteger(0, intIdEstado);
            query.setInteger(1, objEvaluado.getPaIdParticipantePk());
            query.setInteger(2, intIdProyecto);
            query.executeUpdate();
            
        } catch (HibernateException he) { 
            log.error(he);
            correcto = false;
        } 
        
        return correcto;
    }  
    
    public Cuestionario obtenCuestionarioXEvaluado(Integer intIdEvaluado) throws HibernateException{ 
        
        Cuestionario objCuestionario;

        try { 
        
            iniciaOperacion(); 
            
            Query query = sesion.createQuery("select c from Cuestionario c, CuestionarioEvaluado ce " +
                                             " where c.proyecto.poIdProyectoPk = ? " +
                                             "   and ce.id.paIdParticipanteFk = ? " +
                                             "   and c.cuIdCuestionarioPk = ce.id.cuIdCuestionarioFk "); 
            
            query.setInteger(0, Utilitarios.obtenerProyecto().getIntIdProyecto());  
            query.setInteger(1, intIdEvaluado);  
            
            objCuestionario = (Cuestionario) query.uniqueResult(); 
        
        } finally { 
            sesion.close(); 
        }  

        return objCuestionario;
    }  

    public boolean guardaImportacionCuestionario(List<CuestionarioImportado> lstCuestionariosImportados) {
        
        boolean correcto = false;
        
        try{
            
            iniciaOperacion(); 
            
            Proyecto objProyecto = new Proyecto();
            objProyecto.setPoIdProyectoPk(Utilitarios.obtenerProyecto().getIntIdProyecto());

            for(CuestionarioImportado objCuestionarioImportado : lstCuestionariosImportados){

                Cuestionario objCuestionario = new Cuestionario();

                objCuestionario.setProyecto(objProyecto);
                objCuestionario.setCuFeRegistro(new Date());
                objCuestionario.setCuIdEstado(Constantes.INT_ET_ESTADO_CUESTIONARIO_CONFIRMADO);
                objCuestionario.setCuTxDescripcion(objCuestionarioImportado.getStrDescCuestionario());

                objCuestionario.setCuIdCuestionarioPk((Integer) sesion.save(objCuestionario));

                List<Categorias> lstCategorias = objCuestionarioImportado.getLstCategorias();

                for(Categorias objCategorias : lstCategorias){
                    Componente objComponenteCat = new Componente();
                    objComponenteCat.setCoIdTipoComponente(Constantes.INT_ET_TIPO_COMPONENTE_CATEGORIA);
                    objComponenteCat.setCoTxDescripcion(objCategorias.getStrCategoria());
                    objComponenteCat.setCuestionario(objCuestionario);
                    objComponenteCat.setCoIdComponentePk((Integer)sesion.save(objComponenteCat));
                    
                    for(String strPreguntaC : objCategorias.getLstPreguntasCerradas()){
                        Componente objComponentePreC = new Componente();
                        objComponentePreC.setCoIdTipoComponente(Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_CERRADA);
                        objComponentePreC.setCoTxDescripcion(strPreguntaC);
                        objComponentePreC.setCuestionario(objCuestionario);
                        objComponentePreC.setComponente(objComponenteCat);
                        objComponentePreC.setCoIdComponentePk((Integer)sesion.save(objComponentePreC));                    
                    }
                    
                }

                List<String> lstComentarios = objCuestionarioImportado.getLstComentarios();
                
                for(String strComentario : lstComentarios){
                    Componente objComponenteCom = new Componente();
                    objComponenteCom.setCoIdTipoComponente(Constantes.INT_ET_TIPO_COMPONENTE_COMENTARIO);
                    objComponenteCom.setCoTxDescripcion(strComentario);
                    objComponenteCom.setCuestionario(objCuestionario);
                    objComponenteCom.setCoIdComponentePk((Integer)sesion.save(objComponenteCom));
                }
                                
                List<String> lstPreguntasAbiertas = objCuestionarioImportado.getLstPreguntasAbiertas();
                
                for(String strComentario : lstPreguntasAbiertas){
                    Componente objComponentePre = new Componente();
                    objComponentePre.setCoIdTipoComponente(Constantes.INT_ET_TIPO_COMPONENTE_PREGUNTA_ABIERTA);
                    objComponentePre.setCoTxDescripcion(strComentario);
                    objComponentePre.setCuestionario(objCuestionario);
                    objComponentePre.setCoIdComponentePk((Integer)sesion.save(objComponentePre));
                }
                
            }
            
            tx.commit(); 
            
            correcto = true;
            
        }catch(Exception ex){
            log.error(ex);
            tx.rollback();
        }

        return correcto;
      
    }
   
}