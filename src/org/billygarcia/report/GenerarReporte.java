package org.billygarcia.report;
import java.io.InputStream;
import java.util.Map;
import static jdk.nashorn.internal.objects.NativeArray.map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.billygarcia.db.conexion;

   public class GenerarReporte {
       
    public static void mostrarReporte(String nombreReporte, String titulo, Map parametros){
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
        try{
        JasperReport reporteMaestro =(JasperReport) JRLoader.loadObject(reporte);
        JasperPrint reporteIngreso = JasperFillManager.fillReport(reporteMaestro, parametros, conexion.getInstancia().getConexion());
        JasperViewer visor = new JasperViewer(reporteIngreso,false);
        visor.setTitle(titulo);
        visor.setVisible(true);
                
        }catch(Exception e) {
            e.printStackTrace();
        }
}

    public static void mostrarReporte(String reportePruebajasper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   }

