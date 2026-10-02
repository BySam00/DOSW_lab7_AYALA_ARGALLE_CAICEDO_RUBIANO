package edu.eci.dosw.oficioya.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String nombre;
    private URL foto;
    protected String correo;
    protected int telefono;
    private String passwd;

    private List<Notification> notifications = new ArrayList<>();
    private Worker worker; // Composición: un usuario puede tener un perfil de trabajador asociado

    public User() {
    }

    public User(int id, String nombre, String correo, int telefono, String passwd, URL foto) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.passwd = passwd;
        this.foto = foto;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
