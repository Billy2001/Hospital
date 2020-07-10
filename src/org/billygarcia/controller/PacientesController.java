 
package org.billygarcia.controller;

 
import java.net.URL;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import org.billygarcia.bean.Paciente;
import org.billygarcia.db.conexion;
import org.billygarcia.sistema.Principal;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.billygarcia.report.GenerarReporte;

 
public class PacientesController implements Initializable{

    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private operaciones tipoDeOperaciones=operaciones.Ninguno;
    private ObservableList <Paciente> listaPaciente;
    private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbPaciente;
    @FXML private TextField txtDPI;
    @FXML private TextField txtApellido;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtOcupacion;
    @FXML private TextField txtSexo;
    @FXML private GridPane grpFechaNacimiento;
     private DatePicker dtpFechaNacimiento;
    @FXML private TableView tblPaciente;
    @FXML private TableColumn colCodPaciente;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colSexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbPaciente.setItems(getPaciente());
        dtpFechaNacimiento=new DatePicker(Locale.ENGLISH);
        dtpFechaNacimiento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpFechaNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
        grpFechaNacimiento.add(dtpFechaNacimiento, 0, 0);
        
    }
    public void cargarDatos(){
        tblPaciente.setItems(getPaciente());
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<Paciente,Integer>("codigoPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente,String>("DPI"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Paciente,String>("apellidos"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Paciente,String>("nombres"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente,Date>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente,Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente,String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente,String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente,String>("sexo"));
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
        
        
        return listaPaciente=FXCollections.observableList(Lista);
        
    }
    public void seleccionar(){
        cmbPaciente.getSelectionModel().select(buscarPaciente( ((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        txtDPI.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getDPI());
        txtApellido.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getApellidos());
        txtNombre.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getNombres());
        dtpFechaNacimiento.selectedDateProperty().set(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtEdad.setText(""+((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getEdad());
        txtDireccion.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getDireccion());
        txtOcupacion.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getOcupacion());
        txtSexo.setText(((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getSexo());
   }
    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado=null;
        try{
            PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPacientes(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro= procedimiento.executeQuery();
            while(registro.next()){
                resultado =new Paciente(
                        registro.getInt("codigoPaciente"),
                        registro.getString("DPI"),
                        registro.getString("apellidos"),
                        registro.getString("nombres"),
                        registro.getDate("fechaNacimiento"),
                        registro.getInt("edad"),
                        registro.getString("direccion"),
                        registro.getString("ocupacion"),
                        registro.getString("sexo")
                );
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
        cmbPaciente.setDisable(false);
        txtDPI.setDisable(false);
        txtApellido.setDisable(false);
        txtNombre.setDisable(false);
        dtpFechaNacimiento.setDisable(false);
        txtEdad.setDisable(false);
        txtDireccion.setDisable(false);
        txtOcupacion.setDisable(false);
        txtSexo.setDisable(false);
        
        cmbPaciente.setEditable(true);
        txtDPI.setEditable(true);
        txtApellido.setEditable(true);
        txtNombre.setEditable(true);
        txtEdad.setEditable(true);
        txtDireccion.setEditable(true);
        txtOcupacion.setEditable(true);
        txtSexo.setEditable(true);
    }
    private void limpiar(){
        txtDPI.setText("");
        txtApellido.setText("");
        txtNombre.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        txtSexo.setText("");
    }
    private void ingresar(){
        Paciente registro= new Paciente();
        registro.setDPI(txtDPI.getText());
        registro.setApellidos(txtApellido.getText());
        registro.setNombres(txtNombre.getText());
        registro.setEdad(Integer.parseInt(txtEdad.getText()));
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        try{
            PreparedStatement procedimiento=conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPacientes(?,?,?,?,?,?,?,?)}");
             procedimiento.setString(1,registro.getDPI());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setDate(4, new java.sql.Date(registro.getEdad()));
            procedimiento.setInt(5, registro.getEdad());
            procedimiento.setString(6, registro.getDireccion());
            procedimiento.setString(7, registro.getOcupacion());
            procedimiento.setString(8, registro.getSexo());
            procedimiento.execute();
            listaPaciente.add(registro);
         }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void desactivar(){
         cmbPaciente.setDisable(true);
        txtDPI.setDisable(true);
        txtApellido.setDisable(true);
        txtNombre.setDisable(true);
        dtpFechaNacimiento.setDisable(true);
        txtEdad.setDisable(true);
        txtDireccion.setDisable(true);
        txtOcupacion.setDisable(true);
        txtSexo.setDisable(true);
        
        cmbPaciente.setEditable(false);
        txtDPI.setEditable(false);
        txtApellido.setEditable(false);
        txtNombre.setEditable(false);
        txtEdad.setEditable(false);
        txtDireccion.setEditable(false);
        txtOcupacion.setEditable(false);
        txtSexo.setEditable(false);
    }
        public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblPaciente.getSelectionModel().getSelectedItem()!=null){
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
        public void actualizar() {
            try{
                PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarPacientes(?,?,?,?,?,?,?,?,?)}");
                Paciente registro=(Paciente)tblPaciente.getSelectionModel().getSelectedItem();
                registro.setCodigoPaciente(Integer.parseInt(cmbPaciente.getSelectionModel().getSelectedItem().toString()));
                registro.setDPI(txtDPI.getText());
                registro.setApellidos(txtApellido.getText());
                registro.setNombres(txtNombre.getText());
                registro.setFechaNacimiento(dtpFechaNacimiento.getSelectedDate());
                registro.setEdad(Integer.parseInt(txtEdad.getText()));
                registro.setDireccion(txtDireccion.getText());
                registro.setOcupacion(txtOcupacion.getText());
                registro.setSexo(txtSexo.getText());
                
                procedimiento.setInt(1, registro.getCodigoPaciente());
                procedimiento.setString(2, registro.getDPI());
                procedimiento.setString(3, registro.getApellidos());
                procedimiento.setString(4, registro.getNombres());
                procedimiento.setDate(4, new java.sql.Date(registro.getFechaNacimiento().getTime()));
                procedimiento.setInt(6, registro.getEdad());
                procedimiento.setString(7, registro.getDireccion());
                procedimiento.setString(8, registro.getOcupacion());
                procedimiento.setString(9, registro.getSexo());
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
        if(tblPaciente.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar ?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPacientes(?)}");
                    procedimiento.setInt(1,((Paciente) tblPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
                    procedimiento.execute();
                    listaPaciente.remove(tblPaciente.getSelectionModel().getSelectedIndex());
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
            if (tblPaciente.getSelectionModel().getSelectedItem()!=null){
                int codigoPaciente =((Paciente)tblPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente();
                Map parametros = new HashMap();
                parametros.put("p_codigoPaciente",codigoPaciente);
                GenerarReporte.mostrarReporte("Pacientereport.jasper", "Reporte Paciente ,parametros",parametros);
            }
        }
            public void imprimirReporteGeneral(){
           if (tblPaciente.getSelectionModel()!=null){
                Map parametros = new HashMap();
                GenerarReporte.mostrarReporte("PacienteGeneralreport.jasper", "Reporte Paciente ,parametros",parametros);
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
