package edu.eci.dosw.oficioya.dto;

public class LoginRequestDTO {
    private String correo;
    private String passwd;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String correo, String passwd) {
        this.correo = correo;
        this.passwd = passwd;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
