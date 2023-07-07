package com.example.knnfinalprojet;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Collections;

@JsonSerialize
public class Instance {
    int n;
    String [] att=new String[n];
    String classe;
    public Instance() {

    }


    public Instance(String[] att, String classe,int nbratt) {

        this.n=nbratt;
        this.att = att;
        this.classe = classe;
    }


    public static ArrayList<Instance>[] Partitionnement(ArrayList<Instance> list, int pourcentagetest) {
        int size = (list.size() * pourcentagetest) / 100;
        System.out.println("la taille de ensemble de test"+size);
        ArrayList<Instance> test = new ArrayList<>();
        Collections.shuffle(list);

        for (int i = 0; i < size; i++) {
          test.add(list.get(i));
        }

        ArrayList<Instance> train = new ArrayList<>(list.subList(size, list.size()));
        ArrayList<Instance>[] lists=new ArrayList[2];
        lists[0]=train;
        lists[1]=test;
        return lists;
    }
    //CrossValidation
    /*
    ArrayList<Instance>[] Separation(ArrayList<Instance> list,int k){
        int size=list.size()/k;
        Collections.shuffle(list);
        ArrayList<Instance>[] lists=new ArrayList[k];
        for(int i=0;i<k;i++)
        {
            ArrayList<Instance> element=new ArrayList<Instance>();
            for(int j=0;j<size;j++) {
                element.add(list.get(0));
                list.remove(0);
            }
            lists[i]=element;

        }
        return lists;

    }
    ArrayList<Instance>[] CrossValidation(ArrayList<Instance>[] list, int indice){

        ArrayList<Instance>[] lists=new ArrayList[2];
        ArrayList<Instance> train=new ArrayList<Instance>();
        ArrayList<Instance> test=new ArrayList<Instance>();
        test=list[indice];
        for(int i=0;i<list.length;i++)
            if(i!=indice)
                for (Instance element : list[i]) {
                    train.add(element);
                }
        lists[0]=train;
        lists[1]=test;
        return lists;
    }*/
    public  ArrayList<Instance>[] CrossValidation(ArrayList<Instance> list, int numFolds, int foldIndex) {

        int foldSize = list.size() / numFolds;

        // Vérifier si l'indice de pli est valide
        if (foldIndex < 0 || foldIndex >= numFolds) {
            throw new IllegalArgumentException("Invalid fold index");
        }

        // Déterminer les indices de début et de fin du pli
        int startIndex = foldIndex * foldSize;
        int endIndex = startIndex + foldSize;
        if (foldIndex == numFolds - 1) {
            endIndex = list.size();
        }

        // Extraire le sous-ensemble de test
        ArrayList<Instance> testFold = new ArrayList<>(list.subList(startIndex, endIndex));

        // Extraire le sous-ensemble d'entraînement
        ArrayList<Instance> trainFold = new ArrayList<>(list);
        trainFold.removeAll(testFold);

        // Créer la liste des plis

        ArrayList<Instance>[] lists=new ArrayList[2];
        lists[0]=trainFold;
        lists[1]=testFold;

        return lists;
    }

    public String[] getAtt() {
        return att;
    }


    public String getClasse() {
        return classe;
    }

}
