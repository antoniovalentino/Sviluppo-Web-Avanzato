package com.ansima.aulery.model;

import java.util.HashSet;
import java.util.Set;


public class Aula {

    private String nome;
    private String note;
    private Edificio edificio;
    private int piano;
    private String zona;
    private int capienza;
    private int preseElettriche;
    private int preseDiRete;
    private Set<Attrezzature> attrezzature;
    private Area area;

    public Aula() {
        this.nome = nome;
        this.note = note;
        this.edificio = edificio;
        this.piano = piano;
        this.capienza = capienza;
        this.preseDiRete = preseDiRete;
        this.preseElettriche = preseElettriche;
        attrezzature = new HashSet<>();
        area = null;
    }
    
    public Aula(String nome, String note, Edificio edificio, int piano, int capienza, int preseDiRete, int preseElettriche, Set<Attrezzature> attrezzature, Area area) {
        this.nome = nome;
        this.note = note;
        this.edificio = edificio;
        this.piano = piano;
        this.capienza = capienza;
        this.preseDiRete = preseDiRete;
        this.preseElettriche = preseElettriche;
        this.attrezzature = attrezzature;
        this.area = area;
    }
    
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;   
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
       this.note = note;
    }

    public Edificio getEdificio() {
       return edificio;
    }

    public void setEdificio(Edificio edificio) {
       this.edificio = edificio;
    }

    public int getPiano() {
        return piano;   
    }

    public void setPiano(int piano) {
       this.piano = piano;  
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;   
    }

    public int getCapienza() {
       return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public int getPreseElettriche() {
        return preseElettriche;
    }

    public void setPreseElettriche(int preseElettriche) {
      this.preseElettriche = preseElettriche;
    }

    public int getPreseDiRete() {
       return preseDiRete;  
    }

    public void setPreseDiRete(int preseDiRete) {
        this.preseDiRete = preseDiRete;
    }

    public Set<Attrezzature> getAttrezzature() {
        return attrezzature;
    }

    public void setAttrezzature(Set<Attrezzature> attrezzature) {
       this.attrezzature = attrezzature;
    }

    public void addAttrezzatura(Attrezzature attrezzatura) {
        this.attrezzature.add(attrezzatura);
    }

    public void setArea(Area area) {
        this.area = area;
    }
    
    public Area getArea(){
        return area;
    }

}