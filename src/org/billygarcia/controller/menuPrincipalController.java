 package org.billygarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.billygarcia.sistema.Principal;

 
    public class menuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    @Override
    
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedico(){
      escenarioPrincipal.ventanaMedico();
    }
    public void ventanaProgramador(){
    escenarioPrincipal.ventanaProgramadores();
    }
    public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
    public void ventanaespecialidades(){
        escenarioPrincipal.ventanaespecialidad();
    }
    public void ventanaContactoUrgencia(){
        escenarioPrincipal.ventanaContactoUrgencia();
    }
  public void ventanaAreas(){
      escenarioPrincipal.ventanaAreas();
  }
  public void ventanaControlCitas(){
      escenarioPrincipal.ventanaControlCita();
  }
  public void VentanaReceta(){
      escenarioPrincipal.ventanaRecetas();
  }
}
