package org.billygarcia.bean;


public class especialidades {
    private int codigoEspecialidad;
    private String nombreEspecialidad;

    public especialidades() {
    }

    public especialidades(int codigoEspecialidad, String nombreEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }

   
    @Override
    public String toString(){
        return ""+codigoEspecialidad;
    }

}
