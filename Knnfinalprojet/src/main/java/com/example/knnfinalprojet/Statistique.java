package com.example.knnfinalprojet;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class Statistique {
    String classe;
    double poursentagetotal;

    public Statistique(String classe, double poursentagetotal) {
        this.classe = classe;
        this.poursentagetotal = poursentagetotal;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public double getPoursentagetotal() {
        return poursentagetotal;
    }

    public void setPoursentagetotal(double poursentagetotal) {
        this.poursentagetotal = poursentagetotal;
    }
}
