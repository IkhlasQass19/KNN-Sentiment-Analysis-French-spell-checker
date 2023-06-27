package com.example.knnfinalprojet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;

import static com.example.knnfinalprojet.Controller.*;

@Service
public class FichierVersListeInstances {

    public static ArrayList<String> attribute = new ArrayList<>();
    public static ArrayList<String> classe = new ArrayList<>();
    public static ArrayList<Instance> dataset = new ArrayList<>();

    ArrayList<String> getAttribute(String chemin, int choix) throws IOException {
        ArrayList<String> attributs = new ArrayList<>();
        if (choix == 1) {
            try (BufferedReader lecteur = new BufferedReader(new FileReader(chemin))) {
                String ligne;
                boolean attribute = false;
                System.out.println("hi");
                while ((ligne = lecteur.readLine()) != null) {
                    if (ligne.contains("@attribute")) {
                        attribute = true;
                    }
                    if (attribute) {
                        String[] attribut = ligne.trim().split("\\s+");
                        if (attribut.length >= 2) {
                            attributs.add(attribut[1]);
                            System.out.println(attribut[1]);
                        }
                    }

                }
            }
        }
    else {
            try (Reader reader = new FileReader(chemin);

                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
                for (CSVRecord csvRecord : csvParser) {
                    for (int i = 0; i < csvRecord.size(); i++) {
                        if ((csvRecord.get(0) == "") && (i == 0))
                            System.out.println("hi");
                        else
                            attributs.add(csvRecord.get(i));
                        System.out.println("attribute "+csvRecord.get(i));
                    }
                    break;
                }
            }
        }
        this.attribute = attributs;
        return this.attribute;
    }

    ArrayList<Instance> File2list(String chemin, int choix,int forwhat) throws IOException {

        ArrayList<Instance> ins = new ArrayList<Instance>();

        ArrayList<String> clas = new ArrayList<>();
        if (choix == 1) {//c-a-d un fichier de type arff
            try {
                FileInputStream fichier = new FileInputStream(chemin);
                BufferedReader lecteur = new BufferedReader(new FileReader(chemin));
                String ligne;

                while ((ligne = lecteur.readLine()) != null) {
                    if(!ligne.startsWith("%") && !ligne.startsWith("@") && !ligne.trim().isEmpty()) {
                        String[] valeurs = ligne.split(",");
                        String[] attributs = new String[valeurs.length - 1];
                        for (int i = 0; i < attributs.length; i++) {
                            attributs[i] =valeurs[i];
                        }
                        String valeurClasse = valeurs[valeurs.length - 1];
                        if (!clas.contains(valeurClasse)) {
                            clas.add(valeurClasse);
                        }
                        Instance instance = new Instance(attributs, valeurClasse,attributs.length);
                        ins.add(instance);
                    }
                }

                lecteur.close();
            } catch (IOException e) {

                System.out.println("Erreur : " + e.getMessage());
            }
        } else {// fichier de type csv
            try (Reader reader = new FileReader(chemin);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
                int start = 0;//pour iliminer entete
                boolean insert = false;
                for (CSVRecord csvRecord : csvParser) {

                    if (start == 0) {
                        if ((csvRecord.get(0) == "")) {
                            insert = true;
                        }
                        start = 1;
                        continue;
                    }
                    //pour ilimini colonne 1:
                    String[] att = new String[csvRecord.size()-1];//taille de tableau des attribut je fait moin i pour elemine colone d indice
                    // Accédez aux valeurs de chaque ligne en utilisant csvRecord.get(index)
                    for (int i = 0; i < csvRecord.size()-1; i++) {
                        if (insert == true)
                            att[i] = csvRecord.get(i + 1);
                        else
                            att[i] = csvRecord.get(i);
                    }
                    if (!clas.contains(csvRecord.get(csvRecord.size() - 1))) {
                        clas.add(csvRecord.get(csvRecord.size() - 1));
                    }
                    Instance ob = new Instance(att, csvRecord.get(csvRecord.size() - 1), att.length);
                    ins.add(ob);
                }
            }
        }
        if(forwhat==1){//c-a-d pour le classifaction n'est pas anlyse des sentiment:
        this.classe =  clas;
        this.dataset = ins;}
        return ins;
    }
}
