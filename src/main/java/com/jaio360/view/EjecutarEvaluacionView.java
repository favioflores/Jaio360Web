package com.jaio360.view;

import com.jaio360.dao.CuestionarioDAO;
import com.jaio360.dao.EjecutarEvaluacionDAO;
import com.jaio360.dao.MensajeDAO;
import com.jaio360.dao.ParticipanteDAO;
import com.jaio360.dao.ProyectoDAO;
import com.jaio360.dao.RelacionParticipanteDAO;
import com.jaio360.dao.ResultadoDAO; 
import com.jaio360.domain.ProyectoInfo;
import com.jaio360.domain.UsuarioInfo;
import com.jaio360.orm.Componente;
import com.jaio360.orm.Cuestionario;
import com.jaio360.orm.DetalleMetrica;
import com.jaio360.orm.Mensaje;
import com.jaio360.orm.Participante;
import com.jaio360.orm.Proyecto;
import com.jaio360.orm.RelacionParticipante;
import com.jaio360.orm.RelacionParticipanteId;
import com.jaio360.orm.Resultado;
import com.jaio360.utils.Constantes;
import com.jaio360.utils.Utilitarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.outputpanel.OutputPanel;
import org.primefaces.component.selectoneradio.SelectOneRadio;

/**
 *
 * @author user
 */
@ManagedBean(name = "ejecutarEvaluacionView")
@ViewScoped
public class EjecutarEvaluacionView implements Serializable{
    
    private static Log log = LogFactory.getLog(EjecutarEvaluacionView.class);
    
    private String strDescCuestionario;
    private String strDescEvaluado;
    private String strInstrucciones;
    private String strAgradecimiento;
    private List<Componente> lstComponenteCerrada;
    private List<Componente> lstComponenteAbierta;
    private List<Componente> lstCompComentario;
    private List<DetalleMetrica> lstDetalleMetrica;
    private RelacionParticipanteId relacionParticipanteId;
    
    private OutputPanel objOutputPanelCerrada;
    private OutputPanel objOutputPanelAbierta;
    
    private boolean blTerminado;
    
    public static int TIPO_COMPONENTE_CATEGORIA = 45;
    public static int TIPO_COMPONENTE_CERRADA = 46;
    public static int TIPO_COMPONENTE_ABIERTA = 47;
    public static int TIPO_COMPONENTE_COMENTARIO = 48;
    
    public static int TIPO_METODOLOGIA_ESCALA = 30;
    public static int TIPO_METODOLOGIA_ELECCION_FORZADA = 31; 

    private int number2;

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }
    
    public String getStrDescEvaluado() {
        return strDescEvaluado;
    }

    public void setStrDescEvaluado(String strDescEvaluado) {
        this.strDescEvaluado = strDescEvaluado;
    }

    public String getStrAgradecimiento() {
        return strAgradecimiento;
    }

    public void setStrAgradecimiento(String strAgradecimiento) {
        this.strAgradecimiento = strAgradecimiento;
    }

    public boolean isBlTerminado() {
        return blTerminado;
    }

    public void setBlTerminado(boolean blTerminado) {
        this.blTerminado = blTerminado;
    }

    public String getStrInstrucciones() {
        return strInstrucciones;
    }
    public void setStrInstrucciones(String strInstrucciones) {
        this.strInstrucciones = strInstrucciones;
    }

    public String getStrDescCuestionario() {
        return strDescCuestionario;
    }

    public void setStrDescCuestionario(String strDescCuestionario) {
        this.strDescCuestionario = strDescCuestionario;
    }

    public List<Componente> getLstComponenteCerrada() {
        return lstComponenteCerrada;
    }

    public void setLstComponenteCerrada(List<Componente> lstComponenteCerrada) {
        this.lstComponenteCerrada = lstComponenteCerrada;
    }

    public List<Componente> getLstComponenteAbierta() {
        return lstComponenteAbierta;
    }

    public void setLstComponenteAbierta(List<Componente> lstComponenteAbierta) {
        this.lstComponenteAbierta = lstComponenteAbierta;
    }

    public List<DetalleMetrica> getLstDetalleMetrica() {
        return lstDetalleMetrica;
    }

    public void setLstDetalleMetrica(List<DetalleMetrica> lstDetalleMetrica) {
        this.lstDetalleMetrica = lstDetalleMetrica;
    }

    public RelacionParticipanteId getRelacionParticipanteId() {
        return relacionParticipanteId;
    }

    public void setRelacionParticipanteId(RelacionParticipanteId relacionParticipanteId) {
        this.relacionParticipanteId = relacionParticipanteId;
    }
     
    public List<Componente> getLstCompComentario() {
        return lstCompComentario;
    }

    public void setLstCompComentario(List<Componente> lstCompComentario) {
        this.lstCompComentario = lstCompComentario;
    }

    public OutputPanel getObjOutputPanelCerrada() {
        return objOutputPanelCerrada;
    }

    public void setObjOutputPanelCerrada(OutputPanel objOutputPanelCerrada) {
        this.objOutputPanelCerrada = objOutputPanelCerrada;
    }

    public OutputPanel getObjOutputPanelAbierta() {
        return objOutputPanelAbierta;
    }

    public void setObjOutputPanelAbierta(OutputPanel objOutputPanelAbierta) {
        this.objOutputPanelAbierta = objOutputPanelAbierta;
    }
    
    public EjecutarEvaluacionView() {
        
        try{
        
            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();

            if(objProyectoInfo.isBoDefineArtificio()){
                if(objProyectoInfo.getStrCorreoEvaluado().equals(objProyectoInfo.getStrCorreoEvaluador())){
                    strDescEvaluado = "Usted está realizando la autoevaluación de " + objProyectoInfo.getStrNombreEvaluado();
                }else{
                    strDescEvaluado = "Usted está conectado como " + objProyectoInfo.getStrCorreoEvaluador() + " evaluando a : " + objProyectoInfo.getStrDescEvaluado();
                }
            }else{
                if(!objProyectoInfo.getStrDescEvaluado().equals("Autoevaluate")){
                    strDescEvaluado = "Usted está evaluando a : " + objProyectoInfo.getStrDescEvaluado();
                }else{
                    strDescEvaluado = "Evalúese usted mismo";
                }
            }

            EjecutarEvaluacionDAO eEvaluadoDAO = new EjecutarEvaluacionDAO();

            UsuarioSesion objUsuarioSesion = new UsuarioSesion();        
            UsuarioInfo objUsuarioInfo = objUsuarioSesion.obtenerUsuarioInfo();

            ProyectoDAO proyectoDAO =  new ProyectoDAO();

            proyectoDAO.obtenProyecto(objProyectoInfo.getIntIdProyecto());

            this.lstComponenteCerrada =  eEvaluadoDAO.obtenerComponenteTipo(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado(), TIPO_COMPONENTE_CERRADA);
            this.lstComponenteAbierta =  eEvaluadoDAO.obtenerComponenteTipo(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado(), TIPO_COMPONENTE_ABIERTA);
            this.lstDetalleMetrica = eEvaluadoDAO.obtenerDetalleMetrica(objProyectoInfo.getIntIdProyecto());
            this.lstCompComentario =  eEvaluadoDAO.obtenerComponenteTipo(objProyectoInfo.getIntIdProyecto(), objProyectoInfo.getStrCorreoEvaluado(), TIPO_COMPONENTE_COMENTARIO );

            RelacionParticipanteDAO relacionParticipanteDAO = new RelacionParticipanteDAO();            
            if(!objProyectoInfo.isBoDefineArtificio()){
                this.strDescCuestionario = objProyectoInfo.getStrDescCuestionario();
                this.relacionParticipanteId = relacionParticipanteDAO.obtenRelacionParticipanteId(objProyectoInfo, objUsuarioInfo.getStrEmail(), objProyectoInfo.getStrCorreoEvaluado());
            }else{
                CuestionarioDAO objCuestionarioDAO = new CuestionarioDAO();
                Cuestionario objCuestionario = objCuestionarioDAO.obtenCuestionarioXEvaluado(objProyectoInfo.getIntIdEvaluado());
                objProyectoInfo.setIntIdCuestionario(objCuestionario.getCuIdCuestionarioPk());
                this.strDescCuestionario = objCuestionario.getCuTxDescripcion();
                this.relacionParticipanteId = relacionParticipanteDAO.obtenRelacionParticipanteId(objProyectoInfo, objProyectoInfo.getStrCorreoEvaluador(), objProyectoInfo.getStrCorreoEvaluado());
            }
            MensajeDAO objMensajeDAO = new MensajeDAO();
            Mensaje objMensajeInstrucciones = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_INSTRUCCIONES);
            Mensaje objMensajeAgradecimiento = objMensajeDAO.obtenMensaje(objProyectoInfo.getIntIdProyecto(), Constantes.INT_ET_NOTIFICACION_AGRADECIMIENTO);

            byte[] bdataInstruccion = objMensajeInstrucciones.getMeTxCuerpo();
            byte[] bdataAgradecimiento = objMensajeAgradecimiento.getMeTxCuerpo();

            strInstrucciones = Utilitarios.decodeUTF8(bdataInstruccion);
            strAgradecimiento = Utilitarios.decodeUTF8(bdataAgradecimiento);

            /* GENERA FORMULARIO */
            blTerminado = false; 

            objOutputPanelCerrada = new OutputPanel();
            objOutputPanelCerrada.setStyle("text-align: center;");
            objOutputPanelCerrada.setId("PRUEBA1");

            objOutputPanelAbierta = new OutputPanel();
            objOutputPanelAbierta.setStyle("text-align: center;");
            objOutputPanelAbierta.setId("PRUEBA2");

            //Carousel objCarousel = new Carousel();

            //objCarousel.setNumVisible(1);
            //objCarousel.setEffect("easeInStrong");
            //objCarousel.setId("rnd");
            //objCarousel.setWidgetVar("rnd");

            //objCarousel.setCircular(true);
            //objCarousel.setEasing("easeOutBounce");

            //objScrollPanel = new ScrollPanel();

            int contador;

            //String idCategoria;
            String idPregunta;
            contador = 1;        

            HtmlPanelGrid objHtmlPanelGridPregunta;

            /* CREAR PREGUNTAS CERRADAS */
            for(Componente comp:this.lstComponenteCerrada){

                if(comp.getCoIdTipoComponente() == TIPO_COMPONENTE_CERRADA){

                    objHtmlPanelGridPregunta = new HtmlPanelGrid();
                    objHtmlPanelGridPregunta.setColumns(1);
                    objHtmlPanelGridPregunta.setId("hpg_" + contador);
                    objHtmlPanelGridPregunta.setCellpadding("5");
                    //objHtmlPanelGridPregunta.setStyle("min-width:500px; max-width:500px; text-align: left; padding:30px;");
                    objHtmlPanelGridPregunta.setStyle("width:100%; text-align: left; padding:30px;");

                    OutputLabel objOutputLabel = new OutputLabel();                
                    idPregunta = comp.getCoIdComponentePk().toString();
                    //objOutputLabel.setFor("pr" + idPregunta);
                    objOutputLabel.setId("ol_" + idPregunta);
                    objOutputLabel.setValue(contador + ". " + comp.getCoTxDescripcion());
                    //objOutputLabel.setStyle("min-width:450px; max-width:450px; font-size:15px;");
                    objOutputLabel.setStyle("font-size:15px;");

                    SelectOneRadio objSelectOneRadio = new SelectOneRadio();
                    objSelectOneRadio.setLabel("Opciones cargadas");
                    objSelectOneRadio.setId("pr" + idPregunta); 
                    objSelectOneRadio.setLayout("grid");
                    objSelectOneRadio.setColumns(1);

                    List items = new ArrayList();
                    DetalleMetrica objDetalleMetrica = new DetalleMetrica();
                    objDetalleMetrica.setDeIdDetalleEscalaPk(-1);
                    objDetalleMetrica.setDeTxValor("N/A");
                    items.add(new SelectItem(objDetalleMetrica.getDeIdDetalleEscalaPk(),objDetalleMetrica.getDeTxValor()));
                    for (DetalleMetrica obj:lstDetalleMetrica){
                        items.add(new SelectItem(obj.getDeIdDetalleEscalaPk(),obj.getDeTxValor()));
                    }

                    UISelectItems selectItems = new UISelectItems();
                    selectItems.setValue(items);

                    objSelectOneRadio.getChildren().add(selectItems);

                    objHtmlPanelGridPregunta.getChildren().add(objOutputLabel);
                    objHtmlPanelGridPregunta.getChildren().add(objSelectOneRadio);

                    for (Componente obj:this.lstCompComentario){

                        InputTextarea inputTextarea = new InputTextarea();
                        String id = "cmt"+ idPregunta + "_" + obj.getCoIdComponentePk().toString();
                        inputTextarea.setId(id); 
                        //inputTextarea.setStyle("min-width:450px; max-width:450px; max-height: 30px;");
                        inputTextarea.setStyle("width: 100%;");
                        inputTextarea.setPlaceholder(obj.getCoTxDescripcion());
                        inputTextarea.setMaxlength(300);
                        inputTextarea.setRows(3);
                        inputTextarea.setCols(60);

                        objHtmlPanelGridPregunta.getChildren().add(inputTextarea);
                    }

                    objOutputPanelCerrada.getChildren().add(objHtmlPanelGridPregunta);
                    contador++;

                }

            }

            //objOutputPanelCerrada.getChildren().add(objDataScroller);

            /* CREAR PREGUNTAS ABIERTAS */

            HtmlPanelGrid objHtmlPanelGridAbierta;

            String idAbierta;
            int contadorPA = 1;

            for(Componente comp:this.lstComponenteAbierta){

                if(comp.getCoIdTipoComponente() == TIPO_COMPONENTE_ABIERTA){

                    objHtmlPanelGridAbierta = new HtmlPanelGrid();
                    objHtmlPanelGridAbierta.setColumns(1);
                    objHtmlPanelGridAbierta.setCellpadding("5");
                    objHtmlPanelGridAbierta.setBorder(0);
                    objHtmlPanelGridAbierta.setId("hpda_"+contadorPA);
                    objHtmlPanelGridAbierta.setStyle("width:100%; text-align: left; padding-left: 30px; padding-right: 30px; padding-top: 5px; padding-bottom: 5px; ");

                    OutputLabel htmlInputText = new OutputLabel();                
                    idAbierta = comp.getCoIdComponentePk().toString();
                    htmlInputText.setId("pr" + idAbierta);

                    htmlInputText.setValue(contadorPA + ". " + comp.getCoTxDescripcion());

                    objHtmlPanelGridAbierta.getChildren().add(htmlInputText);

                    idAbierta = "c-a_" + idAbierta;
                    InputTextarea inputTextarea = new InputTextarea();
                    inputTextarea.setId(idAbierta);
                    inputTextarea.setMaxlength(300);
                    inputTextarea.setStyle("width: 100%;");
                    inputTextarea.setRows(3);
                    inputTextarea.setCols(60);
                    //inputTextarea.setStyle("min-width:450px; max-width:450px; max-height: 30px;");
                    inputTextarea.setPlaceholder("Ingresa tu respuesta");

                    objHtmlPanelGridAbierta.getChildren().add(inputTextarea);

                    contadorPA++;

                    objOutputPanelAbierta.getChildren().add(objHtmlPanelGridAbierta);
                }

            }
            
            FacesMessage message;
            if(objProyectoInfo.isBoDefineArtificio()){
                if(objProyectoInfo.getStrCorreoEvaluado().equals(objProyectoInfo.getStrCorreoEvaluador())){
                    strDescEvaluado = "Usted está realizando la autoevaluación de " + objProyectoInfo.getStrNombreEvaluado();
                }else{
                    strDescEvaluado = "Usted está conectado como " + objProyectoInfo.getStrCorreoEvaluador() + " evaluando a : " + objProyectoInfo.getStrDescEvaluado();
                }
            }else{
                if(!objProyectoInfo.getStrDescEvaluado().equals("Autoevaluate")){
                    strDescEvaluado = "Usted está evaluando a : " + objProyectoInfo.getStrDescEvaluado();
                }else{
                    strDescEvaluado = "Evalúese usted mismo";
                }
            }

            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Evaluación", "Recuerda guardar una vez completada tu evaluación");
            FacesContext.getCurrentInstance().addMessage(null, message);
        
        }catch(NumberFormatException | HibernateException e){
            log.error(e);
            if(Utilitarios.obtenerEvaluacion()==null){
                UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();
                try {
                    if(objUsuarioInfo.isBoEsAdministrador() || objUsuarioInfo.isBoEsUsuarioMaestro()){
                        FacesContext.getCurrentInstance().getExternalContext().redirect("principal.jsf");
                    }else{
                        FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
                    }
                } catch (IOException ex) {
                    log.error(ex);
                }
            }
        } 
        
    }
    
    public void guardarResultado(){

        try{        
            ProyectoInfo objProyectoInfo = Utilitarios.obtenerEvaluacion();
                    
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                
            Integer idProyecto = Utilitarios.obtenerEvaluacion().getIntIdProyecto();
            Integer IdEvaluado = Utilitarios.obtenerEvaluacion().getIntIdEvaluado();
            
            UsuarioInfo objUsuarioInfo = Utilitarios.obtenerUsuario();

            if(session.getAttribute("evalInfo")==null){
                if(objUsuarioInfo.isBoEsAdministrador() || objUsuarioInfo.isBoEsUsuarioMaestro()){
                    if(objProyectoInfo.isBoDefineArtificio()){
                        FacesContext.getCurrentInstance().getExternalContext().redirect("seguimientoProyecto.jsf");
                    }else{
                        FacesContext.getCurrentInstance().getExternalContext().redirect("principal.jsf");
                    }
                }else{
                    FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
                }
            }else{

                session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                session.removeAttribute("evalInfo");
            
                List<UIComponent> lstTabs = objOutputPanelCerrada.getChildren();

                for(UIComponent c : lstTabs){

                    HtmlPanelGrid ojHtmlPanelGrid = (HtmlPanelGrid) c;

                    List<UIComponent> lstComp = ojHtmlPanelGrid.getChildren(); 

                    for(UIComponent s:lstComp){
                        if(s instanceof SelectOneRadio){
                            SelectOneRadio componente = (SelectOneRadio)s;

                            if(Utilitarios.noEsNuloOVacio(componente.getValue()) && !componente.getValue().toString().equals("-1")){
                                String idDetalleMetrica = (String)(componente.getValue());
                                String idComponentePk = componente.getId().substring(2);
                                guardarResultadoEval(idComponentePk, idDetalleMetrica, null,idProyecto,IdEvaluado, null);
                            } 
                        }else if(s instanceof InputTextarea){

                            InputTextarea input = (InputTextarea)s; 

                            String ssdf = (String) input.getValue();
                            if(Utilitarios.noEsNuloOVacio(input.getValue())){
                                String id = input.getId();
                                String idComponentePk = id.substring(id.indexOf("_") + 1);
                                String idComponentePreguntaPk = id.substring(3 ,id.indexOf("_"));
                                guardarResultadoEval(idComponentePk, null, input.getValue().toString(),idProyecto,IdEvaluado, idComponentePreguntaPk);
                            }

                        }
                    }
                }

                /* Actualiza evaluacion a terminado */

                List<UIComponent> lstAbiertas = objOutputPanelAbierta.getChildren();

                for(UIComponent c : lstAbiertas){

                    HtmlPanelGrid objHtmlPanelGridAbierta = (HtmlPanelGrid) c;

                    for(UIComponent s: objHtmlPanelGridAbierta.getChildren()){

                        if(s instanceof InputTextarea){

                            InputTextarea input = (InputTextarea)s;

                            if(Utilitarios.noEsNuloOVacio(input.getValue())){
                                String id = input.getId();

                                String idComponentePk = id.substring(id.indexOf("_") + 1);
                                this.guardarResultadoEval(idComponentePk, null, input.getValue().toString(), idProyecto, IdEvaluado , null);
                            }

                        }
                    }
                }

                if(relacionParticipanteId!=null){

                    RelacionParticipanteDAO objRelacionParticipanteDAO = new RelacionParticipanteDAO();
                    RelacionParticipante objRelacionParticipante = objRelacionParticipanteDAO.obtenRelacionParticipante(relacionParticipanteId);
                    objRelacionParticipante.setRpIdEstado(Constantes.INT_ET_ESTADO_RELACION_EDO_EDOR_TERMINADO);
                    objRelacionParticipanteDAO.actualizaRelacionParticipante(objRelacionParticipante);

                }else{
                    ParticipanteDAO objParticipanteDAO = new ParticipanteDAO();
                    Participante objParticipante = objParticipanteDAO.obtenParticipante(IdEvaluado);  
                    objParticipante.setPaIdEstado(Constantes.INT_ET_ESTADO_EVALUADO_TERMINADO);
                    objParticipanteDAO.actualizaParticipante(objParticipante);
                }

                blTerminado = true;

                if(objUsuarioInfo.isBoEsAdministrador() || objUsuarioInfo.isBoEsUsuarioMaestro()){
                    if(objProyectoInfo.isBoDefineArtificio()){
                        FacesContext.getCurrentInstance().getExternalContext().redirect("seguimientoProyecto.jsf");
                    }else{
                        FacesContext.getCurrentInstance().getExternalContext().redirect("principal.jsf");
                    }
                }else{
                    FacesContext.getCurrentInstance().getExternalContext().redirect("bienvenida.jsf");
                }

            }
        } catch (IOException ex) {
            log.debug(ex);
        }
    }
   
    public void guardarResultadoEval(String idComponentePk, String idDetalleMetrica, String txtComentario, Integer idProyecto, Integer idEvaluado, String idComponentePreguntaPk){
        Resultado resultado = new Resultado();
        ResultadoDAO resultadoDAO = new ResultadoDAO();
        
        Componente componente = new Componente();
        componente.setCoIdComponentePk(Integer.parseInt(idComponentePk));
        
        
        DetalleMetrica detalleMetrica = new DetalleMetrica();
        if(Utilitarios.noEsNuloOVacio(idDetalleMetrica)){
            detalleMetrica.setDeIdDetalleEscalaPk(Integer.parseInt(idDetalleMetrica));
            resultado.setDetalleMetrica(detalleMetrica);
        }
        
        resultado.setComponente(componente);
        if(Utilitarios.noEsNuloOVacio(txtComentario)){
            resultado.setReTxComentario(txtComentario);
        }
        
        if(Utilitarios.noEsNuloOVacio(idComponentePreguntaPk)){
            resultado.setCoIdComponenteRefFk(Integer.parseInt(idComponentePreguntaPk));
        }
        
        if(relacionParticipanteId!=null){
            resultado.setPaIdParticipanteFk(relacionParticipanteId.getPaIdParticipanteFk());
            resultado.setReIdParticipanteFk(relacionParticipanteId.getReIdParticipanteFk());
            resultado.setReIdRelacionFk(relacionParticipanteId.getReIdRelacionFk());
        }else{
            resultado.setPaIdParticipanteFk(idEvaluado);
        }
        
        Proyecto objProyecto = new Proyecto();
        objProyecto.setPoIdProyectoPk(idProyecto);
        
        resultado.setProyecto(objProyecto);
        
        //RelacionParticipante relacionParticipante = new RelacionParticipante();
        //relacionParticipante.setId(this.relacionParticipanteId);
        
        //resultado.setRelacionParticipante(relacionParticipante);
        
        resultadoDAO.guardaResultado(resultado);
        
    }
        
}
