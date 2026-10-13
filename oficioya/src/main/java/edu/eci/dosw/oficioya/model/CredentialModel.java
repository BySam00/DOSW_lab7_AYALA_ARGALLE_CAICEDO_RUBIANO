package edu.eci.dosw.oficioya.model;

import java.time.LocalDateTime;

public class CredentialModel {

    private Long id;
    private String passwordHash;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private UserModel user;

    public CredentialModel() {
    }

    public CredentialModel(Long id, String passwordHash, LocalDateTime expirationDate, UserModel user) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
