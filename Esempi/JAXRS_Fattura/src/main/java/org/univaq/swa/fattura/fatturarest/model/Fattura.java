package org.univaq.swa.fattura.fatturarest.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Giuseppe Della Penna
 */
public class Fattura {

    private int numero;
    private Calendar data;
    private Anagrafica intestatario;
    private List<Prodotto> elementi;
    private double totaleIVAEsclusa;
    private double totaleIVA;
    private double totaleIVAInclusa;

    public Fattura() {
        intestatario = new Anagrafica();
        numero = 0;
        data = Calendar.getInstance();
        elementi = new ArrayList<>();
        totaleIVAEsclusa = 0;
        totaleIVA = 0;
        totaleIVAInclusa = 0;
    }

    /**
     * @return the intestatario
     */
    public Anagrafica getIntestatario() {
        return intestatario;
    }

    /**
     * @param intestatario the intestatario to set
     */
    public void setIntestatario(Anagrafica intestatario) {
        this.intestatario = intestatario;
    }

    /**
     * @return the numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /**
     * @return the data
     */
    public Calendar getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Calendar data) {
        this.data = data;
    }

    /**
     * @return the elementi
     */
    public List<Prodotto> getElementi() {
        return elementi;
    }

    /**
     * @param elementi the elementi to set
     */
    public void setElementi(List<Prodotto> elementi) {
        this.elementi = elementi;
    }

    /**
     * @return the totaleIVAEsclusa
     */
    public double getTotaleIVAEsclusa() {
        return totaleIVAEsclusa;
    }

    /**
     * @param totaleIVAEsclusa the totaleIVAEsclusa to set
     */
    public void setTotaleIVAEsclusa(double totaleIVAEsclusa) {
        this.totaleIVAEsclusa = totaleIVAEsclusa;
    }

    /**
     * @return the totaleIVA
     */
    public double getTotaleIVA() {
        return totaleIVA;
    }

    /**
     * @param totaleIVA the totaleIVA to set
     */
    public void setTotaleIVA(double totaleIVA) {
        this.totaleIVA = totaleIVA;
    }

    /**
     * @return the totaleIVAInclusa
     */
    public double getTotaleIVAInclusa() {
        return totaleIVAInclusa;
    }

    /**
     * @param totaleIVAInclusa the totaleIVAInclusa to set
     */
    public void setTotaleIVAInclusa(double totaleIVAInclusa) {
        this.totaleIVAInclusa = totaleIVAInclusa;
    }

    //metodo di prova
    public static Fattura dummyFattura(int numero, int anno) {

        Fattura f = new Fattura();
        f.setNumero(numero);
        Calendar data = Calendar.getInstance();
        data.set(Calendar.YEAR, anno);
        f.setData(data);

        f.getIntestatario().setRagioneSociale("Pippo");
        f.getIntestatario().setPartitaIVA("123456789");

        f.getElementi().add(Prodotto.dummyProdotto());
        f.getElementi().add(Prodotto.dummyProdotto());
        return f;
    }

}
