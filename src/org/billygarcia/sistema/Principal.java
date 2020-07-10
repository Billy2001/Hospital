package org.billygarcia.sistema;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.billygarcia.controller.AreasController;
import org.billygarcia.controller.CrontolCitasController;
import org.billygarcia.controller.MedicosController;
import org.billygarcia.controller.PacientesController;
import org.billygarcia.controller.ProgramadorController;
import org.billygarcia.controller.RecetaControlller;
import org.billygarcia.controller.TelefonoMedicoController;
import org.billygarcia.controller.contactoUrgenciaController;
import org.billygarcia.controller.especialidadesController;
import org.billygarcia.controller.menuPrincipalController;
import org.billygarcia.db.conexion;


public class Principal extends Application {
    private final String PAQUETE_VISTA ="/org/billygarcia/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
   
    @Override
    public void start(Stage escenarioPrincipal) throws IOException      {
        this.escenarioPrincipal= escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectologia");
        menuPrincipal();
        escenarioPrincipal.show();
     try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicos(?)}");
                procedimiento.setInt(1,2);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            System.out.println("codigoMedico"+":"+registro.getInt("codigoMedico"));
            System.out.println("licenciaMedica"+":"+registro.getInt("licenciaMedica"));
        }
          }catch (Exception e){
            e.printStackTrace();
        }
       
    }
 
    public static void main(String[] args) {
        launch(args);
    }
    public void menuPrincipal(){
        try{

            menuPrincipalController menuPrincipal = (menuPrincipalController) cambiarEscena("MenuPrincipalView.fxml",432,236);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
             e.printStackTrace();
        }
    }
    
    public void ventanaMedico() {
        try{
                    MedicosController ventanaMedico = (MedicosController)cambiarEscena("MedicoView.fxml",686,499);
                    ventanaMedico.setEscenarioPrincipal(this);
                                }catch(Exception e){
                                e.printStackTrace();
        }
        
   }
        
      public void ventanaProgramadores(){
          try{
                    ProgramadorController ventanaProgramador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml",710,443);
                                 ventanaProgramador.setEscenarioPrincipalProgramador(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }
        public void ventanaPaciente(){
          try{
                    PacientesController ventanaPaciente = (PacientesController) cambiarEscena("PacienteView.fxml",684,454);
                                 ventanaPaciente.setEscenarioPrincipal(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }        
         public void ventanaTelefonoMedico(){
          try{
                    TelefonoMedicoController ventanaTelefonoMedico = (TelefonoMedicoController) cambiarEscena("TellMedicosView.fxml",600,400);
                                 ventanaTelefonoMedico.setEscenarioPrincipal(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }
          public void ventanaespecialidad(){
          try{
                    especialidadesController ventanaespecialidad = (especialidadesController) cambiarEscena("EspecialidadesView.fxml",600,400);
                                 ventanaespecialidad.setEscenarioPrincipal(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }
          public void ventanaContactoUrgencia(){
          try{
                    contactoUrgenciaController ventanaContactoUrgencia = (contactoUrgenciaController) cambiarEscena("ContactoUrgenciaView.fxml",633,421);
                                 ventanaContactoUrgencia.setEscenarioPrincipal(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }
          public void ventanaAreas(){
          try{
                    AreasController ventanaAreas = (AreasController) cambiarEscena("AreasView.fxml",600,400);
                                 ventanaAreas.setEscenarioPrincipal(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }
          public void ventanaControlCita(){
          try{
                    CrontolCitasController ventanaControlCita = (CrontolCitasController) cambiarEscena("ControlCitas.fxml",611,400);
                                 ventanaControlCita.setEscenarioPrincipal(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }
          public void ventanaRecetas(){
          try{
                    RecetaControlller ventanaRecetas = (RecetaControlller) cambiarEscena("Recetas.fxml",613,411);
                                 ventanaRecetas.setEscenarioPrincipal(this);
                                  }catch(Exception e){
                                  e.printStackTrace();
      }
          
      }
          

      
      
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado= null;
        FXMLLoader cargadorFXML= new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena= new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado= (Initializable)cargadorFXML.getController();
        return resultado;
    }
      
}
