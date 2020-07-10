package org.billygarcia.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.billygarcia.bean.Areas;
import org.billygarcia.db.conexion;
import org.billygarcia.sistema.Principal;

public class AreasController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <Areas> listadoAreas;
    private Principal escenarioPrincipal;
    @FXML private ComboBox  cmbCodigoArea;
    @FXML private TextField TxtNombreArea;
    @FXML private TableView tblAreas;
    @FXML private TableColumn ColCodAreas;
    @FXML private TableColumn colNombreAreas;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
      @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoArea.setItems(getAreas());
    }
     public void cargarDatos(){
    tblAreas.setItems(getAreas());
    ColCodAreas.setCellValueFactory(new PropertyValueFactory <Areas, Integer>("codigoArea"));
    colNombreAreas.setCellValueFactory(new PropertyValueFactory <Areas, String>("nombreArea"));
    }
         public ObservableList <Areas> getAreas(){
        ArrayList<Areas> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ListaAreas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Areas(
                resultado.getInt("codigoArea"),
                resultado.getString("nombreArea")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
      return listadoAreas = FXCollections.observableList(Lista);
    }
         public void seleccionar(){
        cmbCodigoArea.getSelectionModel().select(buscarAreas( ((Areas) tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea()));
        TxtNombreArea.setText(((Areas)tblAreas.getSelectionModel().getSelectedItem()).getNombreArea());
         }
     public Areas buscarAreas (int codigoArea){
        Areas resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarAreas(?)}");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
        while (registro.next()){
            resultado = new Areas (
            registro.getInt("codigoArea"),
            registro.getString("nombreArea"));
                    
                    
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
        cmbCodigoArea.setDisable(false);
        TxtNombreArea.setDisable(false);
   
        cmbCodigoArea.setEditable(true);
        TxtNombreArea.setEditable(true);
    }
           private void limpiar() {
       TxtNombreArea.setText("");
    }
               private void ingresar() {
       Areas registro = new Areas();
       registro.setNombreArea(TxtNombreArea.getText());
try{
          PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargos(?)}"); 
          procedimiento.setString(1,registro.getNombreArea());
     
          procedimiento.execute();
          listadoAreas.add(registro);
       }catch(SQLException e){
       
         e.printStackTrace();

       }
    }
          private void desactivar() {
        cmbCodigoArea.setDisable(true);
        TxtNombreArea.setDisable(true);
       
        cmbCodigoArea.setEditable(false);
        TxtNombreArea.setEditable(false);
    }
           public void actualizar (){
            try{
                PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarAreas(?,?)}");
                Areas registro = (Areas) tblAreas.getSelectionModel().getSelectedItem();
                registro.setCodigoArea(Integer.parseInt(cmbCodigoArea.getSelectionModel().getSelectedItem().toString()));
                registro.setNombreArea(TxtNombreArea.getText());
           
             
                 procedimiento.setInt(1,registro.getCodigoArea());
                 procedimiento.setString(3, registro.getNombreArea());
           
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
        if(tblAreas.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar ?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarAreas(?)}");
                    procedimiento.setInt(1,((Areas) tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea());
                    procedimiento.execute();
                    listadoAreas.remove(tblAreas.getSelectionModel().getSelectedIndex());
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
