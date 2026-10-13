package edu.eci.dosw.oficioya.dto;

import edu.eci.dosw.oficioya.model.Disponibility;
import edu.eci.dosw.oficioya.model.Job;
import edu.eci.dosw.oficioya.model.WorkZone;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para lectura (Read) de trabajadores.
 * Combina los datos de User (nombre, foto) y Worker (tarifa, oficio, disponibilidad, zona),
 * sin exponer datos privados como correo, teléfono ni contraseña.
 */
public class WorkerResponseDTO {
    private int id;
    private String nombre;
    private URL foto;
    private int rate;
    private int salary;
    private int finishedJobs;
    private boolean state;
    private Job principalJob;
    private ArrayList<Job> secondaryJobs = new ArrayList<>();
    private WorkZone workZone;
    private List<Disponibility> disponibilities = new ArrayList<>();

    public WorkerResponseDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getFinishedJobs() {
        return finishedJobs;
    }

    public void setFinishedJobs(int finishedJobs) {
        this.finishedJobs = finishedJobs;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Job getPrincipalJob() {
        return principalJob;
    }

    public void setPrincipalJob(Job principalJob) {
        this.principalJob = principalJob;
    }

    public ArrayList<Job> getSecondaryJobs() {
        return secondaryJobs;
    }

    public void setSecondaryJobs(ArrayList<Job> secondaryJobs) {
        this.secondaryJobs = secondaryJobs;
    }

    public WorkZone getWorkZone() {
        return workZone;
    }

    public void setWorkZone(WorkZone workZone) {
        this.workZone = workZone;
    }

    public List<Disponibility> getDisponibilities() {
        return disponibilities;
    }

    public void setDisponibilities(List<Disponibility> disponibilities) {
        this.disponibilities = disponibilities;
    }
}
