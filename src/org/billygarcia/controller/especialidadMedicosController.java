
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
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.billygarcia.bean.especialidadMedicos;
import org.billygarcia.db.conexion;
import org.billygarcia.report.GenerarReporte;
import org.billygarcia.sistema.Principal;

public class especialidadMedicosController implements Initializable {
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <especialidadMedicos> listadoespecialidadMedicos;
    private Principal escenarioPrincipal;
    @FXML private ComboBox cmbEspecialidadMedico;
    @FXML private ComboBox cmbCodMedico;
    @FXML private ComboBox cmbEspecialidad;
    @FXML private ComboBox cmbHorario;
    @FXML private TableView tblEspecialidMedicos;
    @FXML private TableColumn colEspecialidadMedicos;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colCodEspecialidad;
    @FXML private TableColumn colCodHorario;
    @FXML private Button btnReporte;
    @FXML private Button btnEliminar;
        @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
        public void cargarDatos(){
    tblEspecialidMedicos.setItems(getMedicos());
    colEspecialidadMedicos.setCellValueFactory(new PropertyValueFactory <especialidadMedicos, Integer>("codigoEspecialidadMedicos"));
    colCodMedico.setCellValueFactory(new PropertyValueFactory <especialidadMedicos, Integer>("codigoMedico"));
    colCodEspecialidad.setCellValueFactory(new PropertyValueFactory <especialidadMedicos, Integer>("codigoEspecialidad"));
    colCodHorario.setCellValueFactory(new PropertyValueFactory <especialidadMedicos, Integer>("codigoHorario"));
    }
            public ObservableList <especialidadMedicos> getMedicos(){
        ArrayList<especialidadMedicos> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ListaEspecialidadMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new especialidadMedicos(
                resultado.getInt("codigoEspecialidadMedicos"),
                resultado.getInt("codigoEspecialidadMedicos"),
                resultado.getInt("codigoEspecialidad"),
                resultado.getInt("codigoHorario")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
      return listadoespecialidadMedicos = FXCollections.observableList(Lista);
    }
         public void seleccionar(){
             cmbEspecialidadMedico.getSelectionModel().select(buscarEspecialidadMedicos(((especialidadMedicos)tblEspecialidMedicos.getSelectionModel().getSelectedItem()).getCodigoEspecialidadMedicos()));
             cmbCodMedico.getSelectionModel().select(buscarEspecialidadMedicos(((especialidadMedicos)tblEspecialidMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
             cmbEspecialidad.getSelectionModel().select(buscarEspecialidadMedicos(((especialidadMedicos)tblEspecialidMedicos.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
             cmbHorario.getSelectionModel().select(buscarEspecialidadMedicos(((especialidadMedicos)tblEspecialidMedicos.getSelectionModel().getSelectedItem()).getCodigoHorario()));
         }
          public especialidadMedicos buscarEspecialidadMedicos (int codigoEspecialidadMedicos){
        especialidadMedicos resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidadMedicos(?)}");
            procedimiento.setInt(1, codigoEspecialidadMedicos);
            ResultSet registro = procedimiento.executeQuery();
        while (registro.next()){
            resultado = new especialidadMedicos (
            registro.getInt("codigoMedico"),
            registro.getInt("licenciaMedica"),
          registro.getInt("codigoMedico"),
         registro.getInt("codigoMedico"));
                    
                    
        }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultado;
    }
           public void Eliminar (){
                if(tipoDeOperaciones == operaciones.Guardar)
                    cancelar();
                else
            tipoDeOperaciones= operaciones.Eliminar;
        if(tblEspecialidMedicos.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar ?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{ call sp_EliminarEspecialidadMedicos(?)}");
                    procedimiento.setInt(1,((especialidadMedicos) tblEspecialidMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                    procedimiento.execute();
                    listadoespecialidadMedicos.remove(tblEspecialidMedicos.getSelectionModel().getSelectedIndex()).getCodigoEspecialidadMedicos();
                   
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
        btnEliminar.setText("Eliminar");
        btnReporte.setDisable(false);
        tipoDeOperaciones = operaciones.Nuevo;
    
        }
                public void imprimirReporte(){
            if (tblEspecialidMedicos.getSelectionModel().getSelectedItem()!=null){
                int codigoEspecialidadMedicos =((especialidadMedicos)tblEspecialidMedicos.getSelectionModel().getSelectedItem()).getCodigoEspecialidadMedicos();
                Map parametros = new HashMap();
                parametros.put("p_codigoEspecialidadMedicos",codigoEspecialidadMedicos);
                GenerarReporte.mostrarReporte("MedicoReport.jasper", "Reporte Medicos,parametros",parametros);
            }
        }
       public void imprimirReporteGeneral(){
            if (tblEspecialidMedicos.getSelectionModel()!=null){
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
        
}
         
