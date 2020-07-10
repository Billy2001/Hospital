 
package org.billygarcia.db;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.Driver;



public class conexion {
    private  Connection conexion ;
    private static conexion instancia;
    
    private String driver;
    private String url;
    private String usuario;
    private String password;
    private String dbname;
    
    public conexion () {
    String dbname = "DBHospitalInfectologia2015129";
    String url = "jdbc:mysql://localhost:3306/"+dbname+"?useSSL=false&zeroDateTimeBehavior=convertToNull";
    String driver = "com.mysql.jdbc.Driver";
    String usuario = "root";
    String password = "admin";
    
    try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();  
        
        
       conexion= DriverManager.getConnection(url, usuario, password);        
       
    }catch(ClassNotFoundException e){ 
            e.printStackTrace();
           e.getMessage();
    }catch(InstantiationException e){ 
            e.printStackTrace();
            e.getMessage();      
    }catch(IllegalAccessException e){ 
            e.printStackTrace();
            e.getMessage();
    }catch(SQLException e){ 
            e.printStackTrace();
            e.getMessage();
        } 
    }
    
    
     public static conexion getInstancia(){ 
        if(instancia == null){ 
            instancia = new conexion();
        } 
        return instancia;
    } 

    public Connection getConexion() {
        return (Connection) conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = (Connection) conexion;
    }
    
    
}
