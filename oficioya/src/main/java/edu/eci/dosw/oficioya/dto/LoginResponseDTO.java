package edu.eci.dosw.oficioya.dto;

public class LoginResponseDTO {
    private boolean authenticated;
    private String message;
    private Integer userId;
    private String nombre;
    private String correo;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(boolean authenticated, String message, Integer userId, String nombre, String correo) {
        this.authenticated = authenticated;
        this.message = message;
        this.userId = userId;
        this.nombre = nombre;
        this.correo = correo;
    }

    public static LoginResponseDTO success(int userId, String nombre, String correo) {
        return new LoginResponseDTO(true, "Autenticación exitosa", userId, nombre, correo);
    }

    public static LoginResponseDTO failure(String message) {
        return new LoginResponseDTO(false, message, null, null, null);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
