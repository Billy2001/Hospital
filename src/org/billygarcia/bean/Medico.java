 package org.billygarcia.bean;

import java.util.Date;

 
 
public class Medico {
    private int codigoMedico;
    private int licenciaMedica;
    private String nombre;
    private String apellidos;
    private Date horaEntrada;
    private Date haraSalida;
    private String sexo;

    public Medico() {
    }

    public Medico(int codigoMedico, int licenciaMedica, String nombre, String apellidos, Date horaEntrada, Date haraSalida, String sexo) {
        this.codigoMedico = codigoMedico;
        this.licenciaMedica = licenciaMedica;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.horaEntrada = horaEntrada;
        this.haraSalida = haraSalida;
        this.sexo = sexo;
    }

    public int getCodigoMedico() {
        return codigoMedico;
    }

    public void setCodigoMedico(int codigoMedico) {
        this.codigoMedico = codigoMedico;
    }

    public int getLicenciaMedica() {
        return licenciaMedica;
    }

    public void setLicenciaMedica(int licenciaMedica) {
        this.licenciaMedica = licenciaMedica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Date horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Date getHaraSalida() {
        return haraSalida;
    }

    public void setHaraSalida(Date haraSalida) {
        this.haraSalida = haraSalida;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    @Override
    public String toString(){
    return "" + codigoMedico+":"+nombre;
    }
 
}