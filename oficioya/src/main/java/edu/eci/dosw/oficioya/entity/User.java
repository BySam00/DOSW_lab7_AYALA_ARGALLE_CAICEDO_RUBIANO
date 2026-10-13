package edu.eci.dosw.oficioya.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // "user" es palabra reservada en PostgreSQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String foto;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @Column(nullable = false, unique = true, length = 20)
    private String telefono;

    @Column(nullable = false)
    private Boolean state = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleType role = RoleType.USER;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    public User() {
    }

    public User(String nombre, String foto, String correo, String telefono) {
        this.nombre = nombre;
        this.foto = foto;
        this.correo = correo;
        this.telefono = telefono;
    }

    @PrePersist
    void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}