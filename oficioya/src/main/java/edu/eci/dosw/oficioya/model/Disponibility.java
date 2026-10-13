package edu.eci.dosw.oficioya.model;

import java.time.LocalDateTime;
import java.util.Timer;

public class Disponibility {
    private boolean active;
    private LocalDateTime date;
    private Timer startHour;
    private Timer finishHour;

    public Disponibility() {
    }

    public Disponibility(boolean active, LocalDateTime date, Timer startHour, Timer finishHour) {
        this.active = active;
        this.date = date;
        this.startHour = startHour;
        this.finishHour = finishHour;
    }

    public Disponibility(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Timer getStartHour() {
        return startHour;
    }

    public void setStartHour(Timer startHour) {
        this.startHour = startHour;
    }

    public Timer getFinishHour() {
        return finishHour;
    }

    public void setFinishHour(Timer finishHour) {
        this.finishHour = finishHour;
    }
}
