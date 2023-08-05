package com.ansima.aulery.model;

public class Amministratore{
    
    private String username;
    private String password;
    
    public Amministratore() {
        this.username = username;
        this.password = password;
    }
    
    public Amministratore(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}