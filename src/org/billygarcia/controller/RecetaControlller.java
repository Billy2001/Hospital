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
import org.billygarcia.bean.ControlCitas;
import org.billygarcia.bean.Recetas;
import org.billygarcia.db.conexion;
import org.billygarcia.report.GenerarReporte;
import org.billygarcia.sistema.Principal;


public class RecetaControlller implements Initializable {
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <Recetas> listadoRecetas;
    private ObservableList <ControlCitas> listadoCrontolCitas;

    private Principal escenarioPrincipal;
    @FXML private ComboBox cmbCodReceta;
    @FXML private ComboBox cmbCodCOntrolCita;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodReceta;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colCodControlCita;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodReceta.setItems(getRecetas()); 
        cmbCodCOntrolCita.setItems(getControlCitas());
    }
        public ObservableList <Recetas> getRecetas(){
        ArrayList<Recetas> Lista = new ArrayList<>();
                try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ListarRecetas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Recetas(
                resultado.getInt("codigoReceta"),
                resultado.getString("descripcionReceta"),            
                resultado.getInt("codigoControlCita")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
            return listadoRecetas = FXCollections.observableList(Lista);        
        }
        public ObservableList <ControlCitas> getControlCitas(){
        ArrayList<ControlCitas> Lista = new ArrayList<>();
                try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ListaControlCitas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new ControlCitas(
                resultado.getInt("codigoControlCita"),
                resultado.getDate("fecha"),
                resultado.getString("horaInicio"),
                resultado.getString("horaFin"),
                resultado.getInt("codigoMedico"),
                resultado.getInt("codigoPaciente")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
                
      return listadoCrontolCitas = FXCollections.observableList(Lista);
    
     }
     public void cargarDatos(){
    tblRecetas.setItems(getRecetas());
    colCodReceta.setCellValueFactory(new PropertyValueFactory <Recetas, Integer>("codigoReceta"));
    colDescripcion.setCellValueFactory(new PropertyValueFactory <Recetas,String>("descripcionReceta"));
    colCodControlCita.setCellValueFactory(new PropertyValueFactory<Recetas,Integer>("codigoControlCita"));
     }
      public void seleccionar(){
        cmbCodReceta.getSelectionModel().select(buscarRecetas(((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
        txtDescripcion.setText(((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getDescripcionReceta());
        cmbCodCOntrolCita.getSelectionModel().select(buscarRecetas(((Recetas) tblRecetas.getSelectionModel().getSelectedItem()).getCodigoControlCita()));
    }
      public Recetas buscarRecetas (int codigoReceta){
        Recetas resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarRecetas(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
        while (registro.next()){
            resultado = new Recetas (
            registro.getInt("codigoReceta"),
            registro.getString("descripcionReceta"),
            registro.getInt("codigoControlCita"));
   
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
    private void activar(){
        cmbCodReceta.setDisable(false);
        txtDescripcion.setDisable(false);
        cmbCodCOntrolCita.setDisable(false);        
        
        cmbCodReceta.setEditable(true);
        txtDescripcion.setEditable(true);
        cmbCodCOntrolCita.setEditable(true);        
        
    }
    private void limpiar(){
        txtDescripcion.setText("");
        
    }
         private void ingresar(){
        Recetas registro= new Recetas();
        registro.setDescripcionReceta(txtDescripcion.getText());
        registro.setCodigoControlCita(Integer.parseInt(cmbCodCOntrolCita.getSelectionModel().getSelectedItem().toString()));

     
        try{
            PreparedStatement procedimiento=conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarRecetas(?,?)}");
             procedimiento.setString(1,registro.getDescripcionReceta());
             procedimiento.setInt(2, registro.getCodigoControlCita());
            procedimiento.execute();
            listadoRecetas.add(registro);
         }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void desactivar(){
        cmbCodReceta.setDisable(true);
        txtDescripcion.setDisable(true);
        cmbCodCOntrolCita.setDisable(true);
        
        cmbCodReceta.setEditable(false);
        txtDescripcion.setEditable(false);
        cmbCodCOntrolCita.setDisable(true);
    }
     public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblRecetas.getSelectionModel().getSelectedItem()!=null){
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
                PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarRecetas(?,?,?)}");
                Recetas registro = (Recetas) tblRecetas.getSelectionModel().getSelectedItem();
                registro.setCodigoReceta(Integer.parseInt(cmbCodReceta.getSelectionModel().getSelectedItem().toString()));
                registro.setDescripcionReceta(txtDescripcion.getText());
                registro.setCodigoControlCita(Integer.parseInt(cmbCodCOntrolCita.getSelectionModel().getSelectedItem().toString()));
              
                 procedimiento.setInt(1,registro.getCodigoReceta());
                 procedimiento.setString(2, registro.getDescripcionReceta());
                 procedimiento.setInt(3,registro.getCodigoControlCita());

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
            if(tblRecetas.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null," ¿Está Seguro que desea Eliminar este registro?","Eliminar Control Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
               try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContrlCitas(?)}");
                    procedimiento.setInt(1,((Recetas) tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                    procedimiento.execute();
                    listadoRecetas.remove(tblRecetas.getSelectionModel().getSelectedIndex());
                    limpiar();
                    cargarDatos();
                }catch(SQLException e){
                 e.printStackTrace();
                }
                }
                
                
            }else{
                        JOptionPane.showConfirmDialog(null, "''seleccionar un registro a eliminar''");
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
            if (tblRecetas.getSelectionModel().getSelectedItem()!=null){
                int codigoReceta =((Recetas)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoControlCita();
                Map parametros = new HashMap();
                parametros.put("p_codigoReceta",codigoReceta);
                GenerarReporte.mostrarReporte("RecetaReporte.jasper", "Reporte Receta,parametros",parametros);
            }
        }
       public void imprimirReporteGeneral(){
            if (tblRecetas.getSelectionModel()!=null){
                Map parametros = new HashMap();
                GenerarReporte.mostrarReporte("RecetasGeneralreport.jasper","Reporte Receta,parametros",parametros);
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

