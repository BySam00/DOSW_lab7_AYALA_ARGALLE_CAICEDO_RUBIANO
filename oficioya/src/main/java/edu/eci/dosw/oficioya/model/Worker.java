package edu.eci.dosw.oficioya.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Worker {
    private int id;
    private User user; // Composición: Worker contiene la referencia a User
    private int rate;
    private ArrayList<Job> secondaryJobs = new ArrayList<>();
    private int salary;
    private int finishedJobs;
    private ArrayList<URL> finishJobsPictures = new ArrayList<>();
    private boolean state;

    private Job PrincipalJob;
    private List<Request> workRequests = new ArrayList<>();
    private WorkZone workZone;
    private List<Disponibility> disponibilities = new ArrayList<>();

    // Constructor por defecto
    public Worker() {
        this.state = true; // Por defecto activo
    }

    // Constructor por composición directa con User
    public Worker(int id, User user, int rate, Job principalJob, WorkZone workZone) {
        this.id = id;
        this.user = user;
        if (user != null) {
            user.setWorker(this);
        }
        this.rate = rate;
        this.PrincipalJob = principalJob;
        this.workZone = workZone;
        this.state = true; // Por defecto activo
    }

    // Sobrecarga con Long para compatibilidad
    public Worker(Long id, User user, int rate, Job principalJob, WorkZone workZone) {
        this(id != null ? id.intValue() : 0, user, rate, principalJob, workZone);
    }

    // Constructor básico para datos iniciales de simulación
    public Worker(int id, String nombre, String correo) {
        this.id = id;
        this.user = new User(id, nombre, correo, 0, null, null);
        this.user.setWorker(this);
        this.state = true;
    }

    public Worker(Long id, String nombre, String correo) {
        this(id != null ? id.intValue() : 0, nombre, correo);
    }

    // Constructor completo con datos de usuario y trabajador
    public Worker(int id, String nombre, String correo, int telefono, String passwd, URL foto,
            int rate, Job principalJob, WorkZone workZone) {
        this.id = id;
        this.user = new User(id, nombre, correo, telefono, passwd, foto);
        this.user.setWorker(this);
        this.rate = rate;
        this.PrincipalJob = principalJob;
        this.workZone = workZone;
        this.state = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && user.getWorker() != this) {
            user.setWorker(this);
        }
    }

    // Métodos delegados para acceder/modificar atributos de User a través de la
    // composición
    public String getNombre() {
        return user != null ? user.getNombre() : null;
    }

    public void setNombre(String nombre) {
        if (user != null) {
            user.setNombre(nombre);
        }
    }

    public String getCorreo() {
        return user != null ? user.getCorreo() : null;
    }

    public void setCorreo(String correo) {
        if (user != null) {
            user.setCorreo(correo);
        }
    }

    public URL getFoto() {
        return user != null ? user.getFoto() : null;
    }

    public void setFoto(URL foto) {
        if (user != null) {
            user.setFoto(foto);
        }
    }

    public int getTelefono() {
        return user != null ? user.getTelefono() : 0;
    }

    public void setTelefono(int telefono) {
        if (user != null) {
            user.setTelefono(telefono);
        }
    }

    public String getPasswd() {
        return user != null ? user.getPasswd() : null;
    }

    public void setPasswd(String passwd) {
        if (user != null) {
            user.setPasswd(passwd);
        }
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public ArrayList<Job> getSecondaryJobs() {
        return secondaryJobs;
    }

    public void setSecondaryJobs(ArrayList<Job> secondaryJobs) {
        this.secondaryJobs = secondaryJobs;
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

    public ArrayList<URL> getFinishJobsPictures() {
        return finishJobsPictures;
    }

    public void setFinishJobsPictures(ArrayList<URL> finishJobsPictures) {
        this.finishJobsPictures = finishJobsPictures;
    }

    public boolean isState() {
        return state;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Job getPrincipalJob() {
        return PrincipalJob;
    }

    public void setPrincipalJob(Job principalJob) {
        this.PrincipalJob = principalJob;
    }

    public List<Request> getWorkRequests() {
        return workRequests;
    }

    public void setWorkRequests(List<Request> workRequests) {
        this.workRequests = workRequests;
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
