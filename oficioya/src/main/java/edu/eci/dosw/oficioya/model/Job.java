package edu.eci.dosw.oficioya.model;

public class Job {
    private String description;
    private boolean materialsRequired;

    public Job() {
    }

    public Job(String description, boolean materialsRequired) {
        this.description = description;
        this.materialsRequired = materialsRequired;
    }

    public Job(String description) {
        this.description = description;
        this.materialsRequired = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMaterialsRequired() {
        return materialsRequired;
    }

    public void setMaterialsRequired(boolean materialsRequired) {
        this.materialsRequired = materialsRequired;
    }
}
