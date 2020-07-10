
package org.billygarcia.bean;


public class especialidadMedicos {
    private int codigoEspecialidadMedicos;
    private int codigoMedico;
    private int codigoEspecialidad;
    private int codigoHorario;

    public especialidadMedicos() {
    }

    public especialidadMedicos(int codigoEspecialidadMedicos, int codigoMedico, int codigoEspecialidad, int codigoHorario) {
        this.codigoEspecialidadMedicos = codigoEspecialidadMedicos;
        this.codigoMedico = codigoMedico;
        this.codigoEspecialidad = codigoEspecialidad;
        this.codigoHorario = codigoHorario;
    }

    public int getCodigoEspecialidadMedicos() {
        return codigoEspecialidadMedicos;
    }

    public void setCodigoEspecialidadMedicos(int codigoEspecialidadMedicos) {
        this.codigoEspecialidadMedicos = codigoEspecialidadMedicos;
    }

    public int getCodigoMedico() {
        return codigoMedico;
    }

    public void setCodigoMedico(int codigoMedico) {
        this.codigoMedico = codigoMedico;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public int getCodigoHorario() {
        return codigoHorario;
    }

    public void setCodigoHorario(int codigoHorario) {
        this.codigoHorario = codigoHorario;
    }
    @Override
    public String toString(){
       return ""+codigoEspecialidadMedicos;
    }
}
