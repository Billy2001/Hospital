 
package org.billygarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.billygarcia.sistema.Principal;

 
public class ProgramadorController implements Initializable {
  
    private Principal escenarioPrincipalProgramador;
 
    public Principal getEscenarioPrincipalProgramador() {
        return escenarioPrincipalProgramador;
    }

    public void setEscenarioPrincipalProgramador(Principal escenarioPrincipalProgramador) {
        this.escenarioPrincipalProgramador = escenarioPrincipalProgramador;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void MenuPrincipalProgramador(){
    escenarioPrincipalProgramador.menuPrincipal();
    }
}









