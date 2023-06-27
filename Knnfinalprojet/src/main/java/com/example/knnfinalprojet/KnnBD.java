package com.example.knnfinalprojet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "KnnDB")
@Getter
@Setter
public class KnnBD {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "knndb_seq_generator")
    @SequenceGenerator(name = "knndb_seq_generator", sequenceName = "knndb_seq", allocationSize = 1)
    @Column(name = "id")
    private int Id;

    @Column(name = "k")
    private Integer k;

    @Column(name = "exactitude")
    private double exactitude;

    @Column(name = "precizion")
    private double precizion;

    @Column(name = "rappel")
    private double rappel;

    @Column(name = "f_mesure")
    private double f_mesure;

    @Column(name = "cheminDS")
    private String cheminDS;


    public KnnBD() {

    }

    public KnnBD(int id, int k, double exactitude, double precizion, double rappel, double f_mesure, String cheminDS) {
        Id = id;
        this.k = k;
        this.exactitude = exactitude;
        this.precizion = precizion;
        this.rappel = rappel;
        this.f_mesure = f_mesure;
        this.cheminDS = cheminDS;
    }

}
