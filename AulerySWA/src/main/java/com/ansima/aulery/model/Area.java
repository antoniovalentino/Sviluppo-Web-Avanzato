package com.ansima.aulery.model;

public class Area{
    
    private String nome;
    private String descrizione;
    
    public Area() {
        this.nome = nome;
        this.descrizione = descrizione;
    }
    
    public Area(String username, String password) {
        this.nome = username;
        this.descrizione = descrizione;
    }
    
    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}