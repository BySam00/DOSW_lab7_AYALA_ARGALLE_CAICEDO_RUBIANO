package edu.eci.dosw.oficioya.model;

public class WorkZone {
    private int id;
    private String ciudad;
    private String localidad;
    private String barrio;

    public WorkZone() {
    }

    public WorkZone(int id, String ciudad, String localidad, String barrio) {
        this.id = id;
        this.ciudad = ciudad;
        this.localidad = localidad;
        this.barrio = barrio;
    }

    public WorkZone(String barrio) {
        this.barrio = barrio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }
}
