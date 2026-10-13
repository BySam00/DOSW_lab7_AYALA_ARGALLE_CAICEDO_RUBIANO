package edu.eci.dosw.oficioya.dto;

import edu.eci.dosw.oficioya.model.Disponibility;
import edu.eci.dosw.oficioya.model.Job;
import edu.eci.dosw.oficioya.model.WorkZone;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WorkerUpdateDTO {
    // Modificaciones que aplican a Worker:
    private Integer rate;
    private Integer salary;
    private Job principalJob;
    private ArrayList<Job> secondaryJobs;
    private WorkZone workZone;
    private List<Disponibility> disponibilities;

    // Modificaciones que aplican a User:
    private String nombre;
    private URL foto;

    public WorkerUpdateDTO() {
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
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
}
