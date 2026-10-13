package edu.eci.dosw.oficioya.dto;

import edu.eci.dosw.oficioya.model.Disponibility;
import edu.eci.dosw.oficioya.model.Job;
import edu.eci.dosw.oficioya.model.WorkZone;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WorkerRegistrationDTO {
    // Campos obligatorios requeridos por el laboratorio:
    // Nombre, correo, teléfono, oficio principal y contraseña
    private String nombre;
    private String correo;
    private Integer telefono;
    private Job principalJob;
    private String passwd;

    // Campos complementarios
    private URL foto;
    private int rate;
    private int salary;
    private WorkZone workZone;
    private ArrayList<Job> secondaryJobs = new ArrayList<>();
    private List<Disponibility> disponibilities = new ArrayList<>();

    public WorkerRegistrationDTO() {
    }

    public WorkerRegistrationDTO(String nombre, String correo, Integer telefono, Job principalJob, String passwd) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.principalJob = principalJob;
        this.passwd = passwd;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public Job getPrincipalJob() {
        return principalJob;
    }

    public void setPrincipalJob(Job principalJob) {
        this.principalJob = principalJob;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public URL getFoto() {
        return foto;
    }

    public void setFoto(URL foto) {
        this.foto = foto;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public WorkZone getWorkZone() {
        return workZone;
    }

    public void setWorkZone(WorkZone workZone) {
        this.workZone = workZone;
    }

    public ArrayList<Job> getSecondaryJobs() {
        return secondaryJobs;
    }

    public void setSecondaryJobs(ArrayList<Job> secondaryJobs) {
        this.secondaryJobs = secondaryJobs;
    }

    public List<Disponibility> getDisponibilities() {
        return disponibilities;
    }

    public void setDisponibilities(List<Disponibility> disponibilities) {
        this.disponibilities = disponibilities;
    }
}
