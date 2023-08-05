package com.ansima.aulery.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento{

    private String nome;
    private String descrizione;
    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private TipologiaEvento tipologiaEvento;
    private Persona persona;
    private Aula aula;
    private EventoRicorrente eventoRicorrente;
    private Corso corso;

    public Evento() {
        this.nome = nome;
        this.descrizione = descrizione;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.tipologiaEvento = null;
        this.persona = null;
        this.aula = null;
        this.eventoRicorrente = null;
        this.corso = null;
    }
    
    public Evento(String nome, String descrizione, LocalDate data, LocalTime oraInizio, LocalTime oraFine, TipologiaEvento tipologiaEvento, Persona persona, Aula aula, EventoRicorrente eventoRicorrente, Corso corso) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.tipologiaEvento = tipologiaEvento;
        this.persona = persona;
        this.aula = aula;
        this.eventoRicorrente = eventoRicorrente;
        this.corso = corso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
       return descrizione;
    }

    public void setDescrizione(String descrizione) {
       this.descrizione = descrizione;
    }

    public LocalDate getData() {
       return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
       return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public TipologiaEvento getTipologiaEvento() {
        return tipologiaEvento;
    }

    public void setTipologiaEvento(TipologiaEvento tipologiaEvento) {
        this.tipologiaEvento = tipologiaEvento;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
       this.persona = persona;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public EventoRicorrente getEventoRicorrente() {
        return eventoRicorrente;
    }

    public void setEventoRicorrente(EventoRicorrente eventoRicorrente) {
        this.eventoRicorrente = eventoRicorrente;
    }

    public Corso getCorso() {
       return corso;
    }

    public void setCorso(Corso corso) {
        this.corso = corso; 
    }
}