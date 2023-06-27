package com.example.knnfinalprojet;

import java.io.IOException;
import java.util.*;

public class knn {
    int k;
class classe{
    private String nom;
    private int nbr;

    public classe(String nom, int nbr) {
        this.nom = nom;
        this.nbr = nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbr() {
        return nbr;
    }

    public String getNom() {
        return nom;
    }
}
    public knn(int k) {
        this.k = k;
    }
    double [] dif (Instance a,Instance b) throws IOException {
        double [] dif= new double[a.att.length];
        for(int i=0;i<a.att.length;i++)
        {
            String att1= AllServ.lemma(a.att[i],1).get(0);
            System.out.println(AllServ.lemma(b.att[i],1).size());
            String att2=AllServ.lemma(b.att[i],1).get(0);
            dif[i]=AllServ.LevenshteinDistance(att1,att2);
        }
        return dif;
    }
    double distance (Instance a,Instance b) throws IOException {

        double [] dif= new double[a.att.length];
        dif=dif (a,b);
        double distance=0;
        for(int i=0;i<dif.length;i++)
            distance=distance+dif[i];
        return distance;
    }
    double [][] kproche (ArrayList<Instance> list ,Instance a,int type) throws IOException {
        double [][] kproche=new double[this.k][2];
        if(type==2){//c-a-d single mot
        for(int i=0;i<k;i++)
        {
            kproche[i][0]=distance (list.get(i),a);
            kproche[i][1]=i;
        }
        for(int i=k;i< list.size();i++)
        {
            double distance=distance (list.get(i),a);
            if(distance<Arrays.stream(kproche).mapToDouble(row -> row[0]).max().getAsDouble())
            {
                int maxIndex = 0;
                double  max=0;
                for (int j = 0; j < kproche.length; j++) {
                    if (kproche[j][0] > max) {
                        max = kproche[j][0];
                        maxIndex = j;
                    }
                }

                kproche[maxIndex][0]=distance;
                kproche[maxIndex][1]=i;
            }
        }}
        else
        {
            for(int i=0;i<k;i++)
            {
                List <String> text1=AllServ.lemma(list.get(i).att[0],1);
                List <String> text2=AllServ.lemma(a.att[0],1);
                kproche[i][0]=AllServ.similarity(text1,text2);
                System.out.println("la similarite est : "+ kproche[i][0]);
                kproche[i][1]=i;
            }
            for(int i=k;i< list.size();i++)
            {
                List <String> text1=AllServ.lemma(list.get(i).att[0],1);
                List <String> text2=AllServ.lemma(a.att[0],1);
                double distance=AllServ.similarity(text1,text2);
                if(distance>Arrays.stream(kproche).mapToDouble(row -> row[0]).min().getAsDouble())
                {
                    int minIndex = 0;
                    double  min=kproche[0][0];
                    for (int j = 0; j < kproche.length; j++) {
                        if (kproche[j][0] < min) {
                            min = kproche[j][0];
                            minIndex = j;
                        }
                    }
                    kproche[minIndex][0]=distance;
                    kproche[minIndex][1]=i;
                }
            }}
        return kproche;
    }
    Comparator<classe> compareByNbr = new Comparator<classe>() {
        @Override
        public int compare(classe a, classe b) {
            return Integer.compare(a.getNbr(),b.getNbr());
        }
    };
    String PredictClasse(ArrayList<Instance> list ,Instance a,int type) throws IOException {
        ArrayList<classe> classfication = new ArrayList<classe>();

        double[][] kproche=kproche ( list , a,type);
        classe d=new classe(list.get((int) kproche[0][1]).classe,0);
        System.out.println(d.getNom());
        classfication.add(d);
        for(int i=1;i<k;i++)
        {
            boolean exist=false;
            for (classe  clas: classfication) {
                if (clas.getNom().equals(list.get((int) kproche[i][1]).classe)) {
                    clas.setNbr(clas.getNbr() + 1);
                    exist=true;
                }
            }
            if(exist==false)
            {
                classe c=new classe(list.get((int) kproche[i][1]).classe,0);
                classfication.add(c);
            }
        }
        classe maxObject = Collections.max(classfication,compareByNbr);
        //int maxNbr = maxObject.getNbr();
        String nomClasse= maxObject.getNom();
        System.out.println(nomClasse);
        return nomClasse;
    }

}
