package com.ansima.aulery.model;

import java.time.LocalDate;

public class EventoRicorrente {

    private Ricorrenza ricorrenza;
    private LocalDate dataFine;

    public EventoRicorrente() {
        this.ricorrenza = null;
        this.dataFine = dataFine;
    }
    
    public EventoRicorrente(Ricorrenza ricorrenza, LocalDate dataFine) {
        this.ricorrenza = ricorrenza;
        this.dataFine = dataFine;
    }
    
    public Ricorrenza getRicorrenza() {
        return ricorrenza;
    }

    public void setRicorrenza(Ricorrenza ricorrenza) {
        this.ricorrenza = ricorrenza;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }
    
}