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
import org.billygarcia.bean.TelefonosMedico;
import org.billygarcia.db.conexion;
import org.billygarcia.report.GenerarReporte;
import org.billygarcia.sistema.Principal;

public class TelefonoMedicoController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <TelefonosMedico> listadoTelefonoMedico;
    private Principal escenarioPrincipal;
    @FXML private ComboBox cmbCodigoTellMedico;
    @FXML private ComboBox cmbMedico;
    @FXML private TextField txtTellPersonal;
    @FXML private TextField txtTellTrabajo;
    @FXML private TableView tblTelefonoMedico;
    @FXML private TableColumn colCodTellMedico;
    @FXML private TableColumn colTellPersonal;
    @FXML private TableColumn colTellTrabajo;
    @FXML private TableColumn colCodMedico;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
     @Override
    public void initialize(URL location, ResourceBundle resources) {
     cargarDatos();
    cmbCodigoTellMedico.setItems(getTelefonosMedico());
     }
     public void cargarDatos(){
    tblTelefonoMedico.setItems(getTelefonosMedico());
    colCodTellMedico.setCellValueFactory(new PropertyValueFactory <TelefonosMedico, Integer>("codigoTelefonoMedico"));
    colTellPersonal.setCellValueFactory(new PropertyValueFactory <TelefonosMedico, String>("telefonoPersonal"));
    colTellTrabajo.setCellValueFactory(new PropertyValueFactory <TelefonosMedico, String>("telefonoTrabajo"));
    colCodMedico.setCellValueFactory(new PropertyValueFactory <TelefonosMedico, Integer>("codigoMedico"));
}
     public ObservableList <TelefonosMedico> getTelefonosMedico(){
        ArrayList<TelefonosMedico>Lista = new ArrayList<TelefonosMedico>();
        try{
            PreparedStatement procedimiento=conexion.getInstancia().getConexion().prepareCall("{call sp_ListaTelefonoMedico}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                 Lista.add(new TelefonosMedico(
                        resultado.getInt("codigoTelefonoMedico"),
                        resultado.getString("telefonoPersonal"),
                        resultado.getString("telefonoTrabajo"),
                        resultado.getInt("codigoMedico")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return listadoTelefonoMedico=FXCollections.observableList(Lista);
    }
     public void seleccionar(){
        cmbCodigoTellMedico.getSelectionModel().select(buscarMedicos( ((TelefonosMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico()));
        txtTellPersonal.setText(((TelefonosMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        txtTellTrabajo.setText(((TelefonosMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
        cmbMedico.getSelectionModel().select(buscarMedicos( ((TelefonosMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico()));
    }
         public TelefonosMedico buscarMedicos (int codigoTelefonoMedico){
        TelefonosMedico resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonosMedicos(?)}");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
        while (registro.next()){
            resultado = new TelefonosMedico (
            registro.getInt("codigoTelefonoMedico"),
            registro.getString("telefonoPersonal"),
            registro.getString("telefonoTrabajo"),
            registro.getInt("codigoMedico"));
                    
                    
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
        cmbCodigoTellMedico.setDisable(false);
        txtTellPersonal.setDisable(false);
        txtTellTrabajo.setDisable(false);
         cmbMedico.setDisable(false);
        
        cmbCodigoTellMedico.setDisable(false);
        txtTellPersonal.setEditable(true);
        txtTellTrabajo.setEditable(true);       
        cmbMedico.setEditable(true);
    }
        private void limpiar() {
       txtTellPersonal.setText("");
       txtTellTrabajo.setText("");
    
    }
          private void ingresar() {
       TelefonosMedico registro = new TelefonosMedico();
       registro.setTelefonoPersonal(txtTellPersonal.getText());
       registro.setTelefonoTrabajo(txtTellTrabajo.getText());

       try{
          PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonosMedico(?,?,?)}"); 
          procedimiento.setString(1, registro.getTelefonoPersonal());
          procedimiento.setString(2,registro.getTelefonoTrabajo());
          procedimiento.setInt(3, registro.getCodigoMedico());
          procedimiento.execute();
          listadoTelefonoMedico.add(registro);
       }catch(SQLException e){
       
         e.printStackTrace();

       }
       
        
    }
         private void desactivar() {    
         cmbMedico.setDisable(true);
        txtTellPersonal.setDisable(true);
        txtTellTrabajo.setDisable(true);
        cmbMedico.setDisable(true);

         cmbMedico.setEditable(false);
        txtTellPersonal.setEditable(false);
        txtTellTrabajo.setEditable(false);
        cmbMedico.setDisable(true);
         }
          public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblTelefonoMedico.getSelectionModel().getSelectedItem()!=null){
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
                PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarTelefonosMedico(?,?,?,?)}");
                TelefonosMedico registro = (TelefonosMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem();
                registro.setCodigoTelefonoMedico(Integer.parseInt(cmbCodigoTellMedico.getSelectionModel().getSelectedItem().toString()));
                registro.setTelefonoPersonal(txtTellPersonal.getText());
                registro.setTelefonoTrabajo(txtTellTrabajo.getText());
                registro.setCodigoMedico(Integer.parseInt(cmbMedico.getSelectionModel().getSelectedItem().toString()));
       

             
                 procedimiento.setInt(1,registro.getCodigoTelefonoMedico());
                 procedimiento.setString(2,registro.getTelefonoPersonal());
                 procedimiento.setString(3, registro.getTelefonoTrabajo());
                 procedimiento.setInt(4, registro.getCodigoMedico());
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
        if(tblTelefonoMedico.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar ?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoMedico(?)}");
                    procedimiento.setInt(1,((TelefonosMedico) tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                    procedimiento.execute();
                    listadoTelefonoMedico.remove(tblTelefonoMedico.getSelectionModel().getSelectedIndex());
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
            if (tblTelefonoMedico.getSelectionModel().getSelectedItem()!=null){
                int codigoTelefonoMedico =((TelefonosMedico )tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico();
                Map parametros = new HashMap();
                parametros.put("p_codigoTelefonoMedico",codigoTelefonoMedico);
                GenerarReporte.mostrarReporte("TelefonoMedicoReport.jasper", "Reporte TelefonoMedico,parametros",parametros);
            }
        }
                 public void imprimirReporteGeneral(){
            if (tblTelefonoMedico.getSelectionModel()!=null){
                Map parametros = new HashMap();
                GenerarReporte.mostrarReporte("MedicoGeneralreport.jasper","Reporte Medicos,parametros",parametros);
            }
        }
          

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
     public void Medicos(){
        escenarioPrincipal.ventanaMedico();
   } 
         
}
