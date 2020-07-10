 
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
import org.billygarcia.bean.Medico;
import org.billygarcia.bean.contactoUrgencia;
import org.billygarcia.db.conexion;
import org.billygarcia.report.GenerarReporte;
import org.billygarcia.sistema.Principal;

 
public class contactoUrgenciaController implements Initializable {


    private enum operaciones{Nuevo,Guardar,Editar,Actualizar,Eliminar,Cancelar,Ninguno}
    private  operaciones tipoDeOperaciones = operaciones.Ninguno;
    private ObservableList<contactoUrgencia>listadocontactoUrgencia;
    private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbUrgencia;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumeroContacto;
    @FXML private TableView tblContactoUrgencia;
    @FXML private TableColumn colCodigoContactoUrgencia;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNumeroContacto;
    @FXML private TableColumn colCodigoPaciente;
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
        @Override
    public void initialize(URL location, ResourceBundle resources) {
          cargarDatos();
           cmbUrgencia.setItems(getContactoUrgencia());
     }
    public void cargarDatos(){
        tblContactoUrgencia.setItems(getContactoUrgencia());
        colCodigoContactoUrgencia.setCellValueFactory(new PropertyValueFactory<contactoUrgencia,Integer>("codigoContactoUrgencia"));
        colNombre.setCellValueFactory(new PropertyValueFactory<contactoUrgencia,String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<contactoUrgencia,String>("apellidos"));
        colNumeroContacto.setCellValueFactory(new PropertyValueFactory<contactoUrgencia,String>("numeroContacto"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<contactoUrgencia,Integer>("codigoPaciente"));
    }
    public ObservableList<contactoUrgencia>getContactoUrgencia(){
            ArrayList<contactoUrgencia>Lista=new ArrayList<contactoUrgencia>();
       try{
        PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_ListaContactoUrgencia}");
         ResultSet resultado=procedimiento.executeQuery();
        while(resultado.next()){
            Lista.add(new contactoUrgencia(
                        resultado.getInt("codigoContactoUrgencia"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                        resultado.getString("numeroContacto"),
                        resultado.getInt("codigoPaciente")));
        }
       
       }catch(SQLException e){
           e.printStackTrace();
       }
        return listadocontactoUrgencia=FXCollections.observableArrayList(Lista);
        
    }
    public void seleccionar() throws SQLException{
        cmbUrgencia.getSelectionModel().select(buscarContactoUrgencia(((contactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia()));
        txtNombre.setText(((contactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((contactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
        txtNumeroContacto.setText(((contactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto());
        cmbCodigoPaciente.getSelectionModel().select(buscarContactoUrgencia(((contactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
    }
   
    public contactoUrgencia buscarContactoUrgencia (int codigoContactoUrgencia) throws SQLException{
        contactoUrgencia resultado = null;
        try{
            PreparedStatement procedimiento =conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarContactoUrgencia(?)}");
            procedimiento.setInt(1, codigoContactoUrgencia);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado=new contactoUrgencia(
                        registro.getInt("codigoContactoUrgencia"),
                        registro.getString("nombres"),
                        registro.getString("apellidos"),
                        registro.getString("numeroContacto"),
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
        cmbUrgencia.setDisable(false);
        txtNombre.setDisable(false);
        txtApellidos.setDisable(false);
        txtNumeroContacto.setDisable(false);
        cmbCodigoPaciente.setDisable(false);
        
        cmbUrgencia.setEditable(true);
        txtNombre.setEditable(true);
        txtApellidos.setEditable(true);
        txtNumeroContacto.setEditable(true);
        cmbCodigoPaciente.setEditable(true);
        }
        private void limpiar() {
        txtNombre.setText("");
       txtApellidos.setText("");
       txtNumeroContacto.setText("");
    }
    private void ingresar() {
       contactoUrgencia registro = new contactoUrgencia();
        registro.setNombres(txtNombre.getText());
       registro.setApellidos(txtApellidos.getText());
        registro.setNumeroContacto(txtNumeroContacto.getText());
        registro.setCodigoPaciente(Integer.parseInt(cmbCodigoPaciente.getSelectionModel().getSelectedItem().toString()));

       
       try{
          PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{ call sp_AgregarContactourgencia(?,?,?,?)}"); 
          procedimiento.setString(1,registro.getNombres());
          procedimiento.setString(2,registro.getApellidos());
          procedimiento.setString(3,registro.getNumeroContacto());
          procedimiento.setInt(4, registro.getCodigoPaciente());
          
           procedimiento.execute();
          listadocontactoUrgencia.add(registro);
       }catch(SQLException e){
       
         e.printStackTrace();

       }
    }
    private void desactivar() {
        cmbUrgencia.setDisable(true);
        txtNombre.setDisable(true);
        txtApellidos.setDisable(true);
        txtNumeroContacto.setDisable(true);
        cmbCodigoPaciente.setDisable(true);
        
        cmbUrgencia.setEditable(false);
        txtNombre.setEditable(false);
        txtApellidos.setEditable(false);
        txtNumeroContacto.setEditable(false);
        cmbCodigoPaciente.setEditable(false);
    }
     public void edit(){
        switch (tipoDeOperaciones){
            case Ninguno:
                if (tblContactoUrgencia.getSelectionModel().getSelectedItem()!=null){
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
                PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_ModificarContactoUrgencia(?,?,?,?,?)}");
                contactoUrgencia registro = (contactoUrgencia) tblContactoUrgencia.getSelectionModel().getSelectedItem();
                registro.setCodigoContactoUrgencia(Integer.parseInt(cmbUrgencia.getSelectionModel().getSelectedItem().toString()));
                registro.setCodigoPaciente(Integer.parseInt(cmbCodigoPaciente.getSelectionModel().getSelectedItem().toString()));
                registro.setApellidos(txtApellidos.getText());
                registro.setNombres(txtNombre.getText());
                registro.setNumeroContacto(txtNumeroContacto.getText());
             
                 procedimiento.setInt(1,registro.getCodigoContactoUrgencia());
                 procedimiento.setInt(2,registro.getCodigoPaciente());
                 procedimiento.setString(3, registro.getApellidos());
                 procedimiento.setString(4, registro.getNombres());
                  procedimiento.setString(5, registro.getNumeroContacto());
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
        if(tblContactoUrgencia.getSelectionModel().getSelectedItem() !=null){
            int repuesta =JOptionPane.showConfirmDialog(null,"Seguro que desea Eliminar ?","Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(repuesta==JOptionPane.YES_OPTION){
                try{
                    PreparedStatement procedimiento = conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                    procedimiento.setInt(1,((contactoUrgencia) tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                    procedimiento.execute();
                    listadocontactoUrgencia.remove(tblContactoUrgencia.getSelectionModel().getSelectedIndex());
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
            if (tblContactoUrgencia.getSelectionModel().getSelectedItem()!=null){
                int codigoContactoUrgencia =((contactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia();
                Map parametros = new HashMap();
                parametros.put("p_codigoContactoUrgencia",codigoContactoUrgencia);
                GenerarReporte.mostrarReporte("contactoUrgenciareport.jasper", "Reporte contactoUrgencia,parametros",parametros);
            }
        }
            public void imprimirReporteGeneral(){
            if (tblContactoUrgencia.getSelectionModel()!=null){
                Map parametros = new HashMap();
                GenerarReporte.mostrarReporte("contactoUrgenciaGeneralreport.jasper","Reporte contactoUrgencia,parametros",parametros);
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
    

