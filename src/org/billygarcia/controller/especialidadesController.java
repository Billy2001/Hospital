
package org.billygarcia.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.billygarcia.bean.especialidades;
import org.billygarcia.db.conexion;
import org.billygarcia.report.GenerarReporte;
import org.billygarcia.sistema.Principal;

public class especialidadesController implements Initializable {
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <especialidades> listadoEspecialidades;
    private Principal escenarioPrincipal;
    @FXML private ComboBox cmbCodEspecialidad;
    @FXML private TextField txtNombreEspecialidad;
    @FXML private TableView tblEspecialidad;
    @FXML private TableColumn colCodEspecialidad;
    @FXML private TableColumn colNombreEspecialidad;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
  @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       cmbCodEspecialidad.setItems(getEspecialidades());
    }
        public void cargarDatos(){
    tblEspecialidad.setItems(getEspecialidades());
    colCodEspecialidad.setCellValueFactory(new PropertyValueFactory <especialidades, Integer>("codigoEspecialidad"));
    colNombreEspecialidad.setCellValueFactory(new PropertyValueFactory <especialidades, String>("nombreEspecialidad"));
    
}
        public ObservableList <especialidades> getEspecialidades(){
        ArrayList<especialidades> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_especialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new especialidades(
                resultado.getInt("codigoEspecialidad"),
                resultado.getString("nombreEspecialidad")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
      return listadoEspecialidades = FXCollections.observableList(Lista);
    }
            public void seleccionar(){
        cmbCodEspecialidad.getSelectionModel().select(buscarespecialidades(((especialidades)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
        txtNombreEspecialidad.setText(((especialidades)tblEspecialidad.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
    }
         public especialidades buscarespecialidades (int codigoEspecialidad){
        especialidades resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidades(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
        while (registro.next()){
            resultado = new especialidades (
            registro.getInt("codigoEspecialidad"),
            registro.getString("nombreEspecialidad"));
                    
                    
        }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultado;
    }
             public void nuevo(){
        switch(tipoDeOperaciones){
            case  Ninguno:
                activar();
                limpiar();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.Guardar;
                break;
            case Guardar:
                ingresar();
                desactivar();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.Ninguno;
                cargarDatos();
                break;
        }
    
    }
             private void activar() {
        cmbCodEspecialidad.setDisable(false);
        txtNombreEspecialidad.setDisable(false);

        
        cmbCodEspecialidad.setEditable(true);
        txtNombreEspecialidad.setEditable(true);
         }
        private void limpiar() {
       txtNombreEspecialidad.setText("");
        }
         private void ingresar() {
       especialidades registro = new especialidades();
       registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
       try{
          PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidades(?)}"); 
          procedimiento.setString(1,registro.getNombreEspecialidad());
          procedimiento.execute();
          listadoEspecialidades.add(registro);
       }catch(SQLException e){
       
         e.printStackTrace();

       }
       }
             private void desactivar() {
        cmbCodEspecialidad.setDisable(true);
        txtNombreEspecialidad.setDisable(true);

        cmbCodEspecialidad.setEditable(false);
        txtNombreEspecialidad.setEditable(false);

    }
        public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblEspecialidad.getSelectionModel().getSelectedItem()!=null){
                 btnModificar.setText("Actualizar");
                 btnReporte.setText("Cancelar");
                 btnAgregar.setDisable(true);
                 btnEliminar.setDisable(true);
                 tipoDeOperaciones= operaciones.Actualizar;
                 activar();
                }
                break;
            case Actualizar:
                actualizar (); 
                btnModificar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.Ninguno;
                desactivar();
                cargarDatos();
                break;
        }
    }
          public void actualizar (){
            try{
                PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarEspecialidades(?,?)}");
                especialidades registro = (especialidades) tblEspecialidad.getSelectionModel().getSelectedItem();
                registro.setCodigoEspecialidad(Integer.parseInt(cmbCodEspecialidad.getSelectionModel().getSelectedItem().toString()));
                registro.setNombreEspecialidad(txtNombreEspecialidad.getText());
          
                 procedimiento.setInt(1,registro.getCodigoEspecialidad());
                 procedimiento.setString(2, registro.getNombreEspecialidad());
                 procedimiento.execute();
                 
             }catch(SQLException e){
                 e.printStackTrace();
             }
        }
                  public void Eliminar (){
                if(tipoDeOperaciones == operaciones.Guardar)
                    cancelar();
                else
            tipoDeOperaciones= operaciones.Eliminar;
        if(tblEspecialidad.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar ?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidades(?)}");
                    procedimiento.setInt(1,((especialidades) tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                    procedimiento.execute();
                    listadoEspecialidades.remove(tblEspecialidad.getSelectionModel().getSelectedIndex());
                    limpiar();
                    cargarDatos();
                }catch(SQLException e){
                 e.printStackTrace();
                }
                }
                
                
            }else{
                        JOptionPane.showConfirmDialog(null, "Debe seleccionar un registro a eliminar");
                        }
            }
         public void cancelar(){
        btnAgregar.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnReporte.setDisable(false);
        btnModificar.setDisable(false);
        tipoDeOperaciones = operaciones.Nuevo;
    
        }     
               public void imprimirReporte(){
            if (tblEspecialidad.getSelectionModel().getSelectedItem()!=null){
                int codigoEspecialidad =((especialidades)tblEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad();
                Map parametros = new HashMap();
                parametros.put("p_codigoEspecialidad",codigoEspecialidad);
                GenerarReporte.mostrarReporte("especialidadesreport.jasper", "Reporte especialidades,parametros",parametros);
            }
        }
            public void imprimirReporteGeneral(){
            if (tblEspecialidad.getSelectionModel()!=null){
                Map parametros = new HashMap();
                GenerarReporte.mostrarReporte("especialidadesGeneralreport.jasper","Reporte especialidades,parametros",parametros);
            }
        }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
        
}