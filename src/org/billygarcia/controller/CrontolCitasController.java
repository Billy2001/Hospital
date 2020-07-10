package org.billygarcia.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.billygarcia.bean.ControlCitas;
import org.billygarcia.bean.Medico;
import org.billygarcia.bean.Paciente;
import org.billygarcia.db.conexion;
import org.billygarcia.report.GenerarReporte;
import org.billygarcia.sistema.Principal;


public class CrontolCitasController implements Initializable{
    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList <ControlCitas> listadoCrontolCitas;
    private ObservableList<Medico>listaM;
    private ObservableList<Paciente>listaP;
    
    private Principal escenarioPrincipal;
    @FXML private ComboBox cmbCodControlCita;
    @FXML private ComboBox cmbCodMedico;
    @FXML private ComboBox cmbCodPaciente;
    @FXML private GridPane grpFecha;
      private DatePicker dtpFecha;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private TableView tblControlCitas;
    @FXML private TableColumn colCodControlCita;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHoraInicio;
    @FXML private TableColumn colHoraFin;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colCodPaciente;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
        @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodControlCita.setItems(getControlCitas());
        cmbCodMedico.setItems(getMedicos());
        cmbCodPaciente.setItems(getPaciente());
        
       dtpFecha = new DatePicker(Locale.ENGLISH);
       dtpFecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
       dtpFecha.getCalendarView().todayButtonTextProperty().set("Today");
       grpFecha.add(dtpFecha,0,0);
    }   
    public void cargarDatos(){
    tblControlCitas.setItems(getControlCitas());
    colCodControlCita.setCellValueFactory(new PropertyValueFactory <ControlCitas, Integer>("codigoControlCita"));
    colFecha.setCellValueFactory(new PropertyValueFactory<ControlCitas,Date>("fecha"));
    colHoraInicio.setCellValueFactory(new PropertyValueFactory <ControlCitas,String>("horaInicio"));
    colHoraFin.setCellValueFactory(new PropertyValueFactory <ControlCitas,String>("horaFin"));
    colCodMedico.setCellValueFactory(new PropertyValueFactory<ControlCitas,Integer>("codigoMedico"));
    colCodPaciente.setCellValueFactory(new PropertyValueFactory<ControlCitas,Integer>("codigoPaciente"));
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
      return listaM = FXCollections.observableList(Lista);
    }
     
       public ObservableList <Paciente> getPaciente(){
        ArrayList<Paciente>Lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento=conexion.getInstancia().getConexion().prepareCall("{call sp_ListaPacientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                 Lista.add(new Paciente(
                        resultado.getInt("codigoPaciente"),
                        resultado.getString("DPI"),
                        resultado.getString("apellidos"),
                        resultado.getString("nombres"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getInt("edad"),
                        resultado.getString("direccion"),
                        resultado.getString("ocupacion"),
                        resultado.getString("sexo")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        
        return listaP=FXCollections.observableList(Lista);
        
    }
     
        public void seleccionar(){
        cmbCodControlCita.getSelectionModel().select(buscarControlCitas(((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita()));
        dtpFecha.selectedDateProperty().set(((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getFecha());
        txtHoraInicio.setText(((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getHoraInicio());
        txtHoraFin.setText(((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getHoraFin());
        cmbCodMedico.getSelectionModel().select(buscarControlCitas(((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbCodPaciente.getSelectionModel().select(buscarControlCitas(((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
    }
      public ControlCitas buscarControlCitas (int codigoControlCita){
        ControlCitas resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarControlCitas(?)}");
            procedimiento.setInt(1, codigoControlCita);
            ResultSet registro = procedimiento.executeQuery();
        while (registro.next()){
            resultado = new ControlCitas (
            registro.getInt("codigoControlCita"),
            registro.getDate("fecha"),
            registro.getString("horaInicio"),
            registro.getString("horaFin"),
            registro.getInt("codigoMedico"),
            registro.getInt("codigoPaciente"));
   
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
        cmbCodControlCita.setDisable(false);
        txtHoraInicio.setDisable(false);
        txtHoraFin.setDisable(false);
        dtpFecha.setDisable(false);
        cmbCodMedico.setDisable(false);
        cmbCodPaciente.setDisable(false);
        
        cmbCodControlCita.setEditable(true);
        txtHoraInicio.setEditable(true);
        txtHoraFin.setEditable(true);
        cmbCodMedico.setEditable(true);
        cmbCodPaciente.setEditable(true);
        
    }

    private void limpiar() {
       txtHoraInicio.setText("");
       txtHoraFin.setText("");
    }
    
    private void ingresar() {
       ControlCitas registro = new ControlCitas();
       registro.setFecha(new java.sql.Date(dtpFecha.getSelectedDate().getTime()));
       registro.setHoraInicio(txtHoraInicio.getText());
       registro.setHoraFin(txtHoraFin.getText());
       registro.setCodigoMedico(Integer.parseInt(cmbCodMedico.getSelectionModel().getSelectedItem().toString()));
      registro.setCodigoPaciente(Integer.parseInt(cmbCodPaciente.getSelectionModel().getSelectedItem().toString()));

      
       
       try{
          PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarControlCitas (?,?,?,?,?)}"); 
          procedimiento.setDate(1, new java.sql.Date(registro.getFecha().getTime()));
          procedimiento.setString(2,registro.getHoraInicio());
          procedimiento.setString(3,registro.getHoraFin());
          procedimiento.setInt(4,registro.getCodigoMedico());
          procedimiento.setInt(5,registro.getCodigoPaciente());
          procedimiento.execute();
          listadoCrontolCitas.add(registro);
       }catch(SQLException e){
       
         e.printStackTrace();

       }
    }

    private void desactivar() {
      cmbCodControlCita.setDisable(true);
        txtHoraInicio.setDisable(true);
        txtHoraFin.setDisable(true);
        dtpFecha.setDisable(true);
        cmbCodMedico.setDisable(true);
        cmbCodPaciente.setDisable(true);
        
        cmbCodControlCita.setEditable(false);
        txtHoraInicio.setEditable(false);
        txtHoraFin.setEditable(false);
        cmbCodMedico.setEditable(false);
        cmbCodPaciente.setEditable(false);
    }
      public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblControlCitas.getSelectionModel().getSelectedItem()!=null){
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
                PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarControlCitas(?,?,?,?,?,?)}");
                ControlCitas registro = (ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem();
                registro.setCodigoControlCita(Integer.parseInt(cmbCodControlCita.getSelectionModel().getSelectedItem().toString()));
                registro.setFecha(dtpFecha.getSelectedDate());
                registro.setHoraInicio(txtHoraInicio.getText());
                registro.setHoraFin(txtHoraFin.getText());                
                registro.setCodigoMedico(Integer.parseInt(cmbCodMedico.getSelectionModel().getSelectedItem().toString()));
                registro.setCodigoPaciente(Integer.parseInt(cmbCodPaciente.getSelectionModel().getSelectedItem().toString()));
            
                 procedimiento.setInt(1,registro.getCodigoControlCita());
                 procedimiento.setDate(2,new java.sql.Date(registro.getFecha().getTime()));
                 procedimiento.setString(3, registro.getHoraInicio());
                 procedimiento.setString(4, registro.getHoraFin());                
                 procedimiento.setInt(5,registro.getCodigoMedico());
                 procedimiento.setInt(6,registro.getCodigoPaciente());

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
            if(tblControlCitas.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null," ¿Está Seguro que desea Eliminar este registro?","Eliminar Control Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
               try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContrlCitas(?)}");
                    procedimiento.setInt(1,((ControlCitas) tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita());
                    procedimiento.execute();
                    listadoCrontolCitas.remove(tblControlCitas.getSelectionModel().getSelectedIndex());
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
            if (tblControlCitas.getSelectionModel().getSelectedItem()!=null){
                int codigoControlCita =((ControlCitas)tblControlCitas.getSelectionModel().getSelectedItem()).getCodigoControlCita();
                Map parametros = new HashMap();
                parametros.put("p_codigoControlCita",codigoControlCita);
                GenerarReporte.mostrarReporte("ControlCitaReporte.jasper", "Reporte ControlCitas,parametros",parametros);
            }
        }
       public void imprimirReporteGeneral(){
            if (tblControlCitas.getSelectionModel()!=null){
                Map parametros = new HashMap();
                GenerarReporte.mostrarReporte("ControlCitasGeneralreport.jasper","Reporte ControlCitas,parametros",parametros);
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