package com.ansima.aulery.model;

public class Persona {

    private String nome;
    private String cognome;
    private String email;
    
    public Persona() {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
    
    public Persona(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
    
    public String getNome() {
       return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;   
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
      this.cognome = cognome;   
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
       this.email = email;
    }
    
}
