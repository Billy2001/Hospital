 package org.billygarcia.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import org.billygarcia.bean.Medico;
import org.billygarcia.db.conexion;
import org.billygarcia.sistema.Principal;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.billygarcia.report.GenerarReporte;

 
 
public class MedicosController implements Initializable {
    
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <Medico> listadoMedico;
    private Principal escenarioPrincipal;
    private DatePicker dtpEntrada;
    private DatePicker dtpSalida;
    @FXML private ComboBox cmbMedico;
    @FXML private TextField txtLicencia;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtSexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private GridPane grpHorarioEntrada; 
    @FXML private GridPane grpHorarioSalida;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodigoMedico;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colHorarioEntrada;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colSexo;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    cargarDatos();
    cmbMedico.setItems(getMedicos());
    
     dtpEntrada = new DatePicker(Locale.ENGLISH);
       dtpEntrada.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
       dtpEntrada.getCalendarView().todayButtonTextProperty().set("Today");
       grpHorarioEntrada.add(dtpEntrada,0,0);
   
       dtpSalida = new DatePicker(Locale.ENGLISH);
       dtpSalida.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
       dtpSalida.getCalendarView().todayButtonTextProperty().set("Today");
       grpHorarioSalida.add(dtpSalida,0,0);
    
 
    }
    
    public void cargarDatos(){
    tblMedicos.setItems(getMedicos());
    colCodigoMedico.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("codigoMedico"));
    colLicenciaMedica.setCellValueFactory(new PropertyValueFactory <Medico, Integer>("licenciaMedica"));
    colNombre.setCellValueFactory(new PropertyValueFactory <Medico, String>("nombre"));
    colApellidos.setCellValueFactory(new PropertyValueFactory <Medico, String>("apellidos"));
    colHorarioEntrada.setCellValueFactory(new PropertyValueFactory <Medico, Date>("horaEntrada"));
    colHorarioSalida.setCellValueFactory(new PropertyValueFactory <Medico, Date>("haraSalida"));
    colSexo.setCellValueFactory(new PropertyValueFactory <Medico, String>("sexo"));
}
    public ObservableList <Medico> getMedicos(){
        ArrayList<Medico> Lista = new ArrayList<>();
        try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_Lista_Medicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                Lista.add(new Medico(
                resultado.getInt("codigoMedico"),
                resultado.getInt("licenciaMedica"),
                resultado.getString("nombre"),
                resultado.getString("apellidos"),
                resultado.getDate("horaEntrada"),
                resultado.getDate("haraSalida"),
                resultado.getString("sexo")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
      return listadoMedico = FXCollections.observableList(Lista);
    }
        
    public void seleccionar(){
        cmbMedico.getSelectionModel().select(buscarMedicos( ((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        txtLicencia.setText(""+((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica());
        txtNombre.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombre());
        txtApellidos.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
        dtpEntrada.selectedDateProperty().set(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        dtpSalida.selectedDateProperty().set(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHaraSalida());
        txtSexo.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
    }
    public Medico buscarMedicos (int codigoMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicos(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
        while (registro.next()){
            resultado = new Medico (registro.getInt("codigoMedico"),
            registro.getInt("licenciaMedica"),
            registro.getString("nombre"),
            registro.getString("apellidos"),
            registro.getDate("horaEntrada"),
            registro.getDate("haraSalida"),
            registro.getString("sexo"));
                    
                    
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
        cmbMedico.setDisable(false);
        txtNombre.setDisable(false);
        txtApellidos.setDisable(false);
        txtLicencia.setDisable(false);
        dtpEntrada.setDisable(false);
        dtpSalida.setDisable(false);
        txtSexo.setDisable(false);
        
        cmbMedico.setEditable(true);
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        txtLicencia.setEditable(true);
        txtSexo.setEditable(true);
        
        
    }

    private void limpiar() {
       txtLicencia.setText("");
       txtNombre.setText("");
       txtApellidos.setText("");
       txtSexo.setText("");
    }
    
    private void ingresar() {
       Medico registro = new Medico();
       registro.setLicenciaMedica(Integer.parseInt(txtLicencia.getText()));
       registro.setNombre(txtNombre.getText());
       registro.setApellidos(txtApellidos.getText());
       registro.setHoraEntrada(new java.sql.Date(dtpEntrada.getSelectedDate().getTime()));
       registro.setHaraSalida(new java.sql.Date(dtpSalida.getSelectedDate().getTime()));
       registro.setSexo(txtSexo.getText());
       
       try{
          PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicos (?,?,?,?,?,?)}"); 
          procedimiento.setInt(1,registro.getLicenciaMedica());
          procedimiento.setString(2,registro.getNombre());
          procedimiento.setString(3,registro.getApellidos());
          procedimiento.setDate(4, new java.sql.Date(registro.getHoraEntrada().getTime()));
          procedimiento.setDate(5,new java.sql.Date (registro.getHaraSalida().getTime()));
          procedimiento.setString(6, registro.getSexo());
          procedimiento.execute();
          listadoMedico.add(registro);
       }catch(SQLException e){
       
         e.printStackTrace();

       }
    }

    private void desactivar() {
        cmbMedico.setDisable(true);
        txtNombre.setDisable(true);
        txtApellidos.setDisable(true);
        txtLicencia.setDisable(true);
        dtpEntrada.setDisable(true);
        dtpSalida.setDisable(true);
        txtSexo.setDisable(true);
        
        cmbMedico.setEditable(false);
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtLicencia.setEditable(false);
        dtpEntrada.setDisable(false);
        dtpSalida.setDisable(false);
        txtSexo.setEditable(false);
    }
    public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblMedicos.getSelectionModel().getSelectedItem()!=null){
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
                PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarMedicos(?,?,?,?,?,?,?)}");
                Medico registro = (Medico) tblMedicos.getSelectionModel().getSelectedItem();
                registro.setCodigoMedico(Integer.parseInt(cmbMedico.getSelectionModel().getSelectedItem().toString()));
                registro.setLicenciaMedica(Integer.parseInt(txtLicencia.getText()));
                registro.setNombre(txtNombre.getText());
                registro.setApellidos(txtApellidos.getText());
                registro.setHoraEntrada(dtpEntrada.getSelectedDate());
                registro.setHaraSalida(dtpSalida.getSelectedDate());
                registro.setSexo(txtSexo.getText());
             
                 procedimiento.setInt(1,registro.getCodigoMedico());
                 procedimiento.setInt(2,registro.getLicenciaMedica());
                 procedimiento.setString(3, registro.getNombre());
                 procedimiento.setString(4, registro.getApellidos());
                 procedimiento.setDate(5,new java.sql.Date(registro.getHoraEntrada().getTime()));
                 procedimiento.setDate(6,new java.sql.Date(registro.getHaraSalida().getTime()));
                 procedimiento.setString(7, registro.getSexo());
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
        if(tblMedicos.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar ?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedicos(?)}");
                    procedimiento.setInt(1,((Medico) tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                    procedimiento.execute();
                    listadoMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex());
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
            if (tblMedicos.getSelectionModel().getSelectedItem()!=null){
                int codigoMedico =((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico();
                Map parametros = new HashMap();
                parametros.put("p_codigoMedico",codigoMedico);
                GenerarReporte.mostrarReporte("MedicoReport.jasper", "Reporte Medicos,parametros",parametros);
            }
        }
       public void imprimirReporteGeneral(){
            if (tblMedicos.getSelectionModel()!=null){
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
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
   } 
    public void ventanaTelefonoMedico(){
        escenarioPrincipal.ventanaTelefonoMedico();
    }
      
}