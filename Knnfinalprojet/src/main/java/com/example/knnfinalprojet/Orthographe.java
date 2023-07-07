package com.example.knnfinalprojet;

import edu.stanford.nlp.ling.tokensregex.PhraseTable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Orthographe {
    List<String> mots = new ArrayList<>();

    public static int calculateDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        // Si l'un des mots est vide, la distance est la longueur de l'autre mot
        if (m == 0) {
            return n;
        }
        if (n == 0) {
            return m;
        }

        // Initialisation de la matrice de distances
        int[][] distances = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            distances[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            distances[0][j] = j;
        }

        // Calcul des distances
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (word1.charAt(i - 1) == word2.charAt(j - 1)) ? 0 : 1;

                distances[i][j] = Math.min(Math.min(distances[i - 1][j] + 1, distances[i][j - 1] + 1),
                        distances[i - 1][j - 1] + cost);
            }
        }

        // Retourne la distance entre les deux mots
        return distances[m][n];
    }


        public void generer(){
        try(
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Ikhlas\\Desktop\\KNN-Sentiment-Analysis\\Knnfinalprojet\\src\\rep\\liste.txt")))

                 {
        String ligne;
        while ((ligne = br.readLine()) != null) {
            mots.add(ligne.toLowerCase()); // Convertir le mot en minuscules
        }
    }
        catch(
    IOException e)

    {
        e.printStackTrace();
    }

}
        public List<Instance> Corriger(String phrase){
        String[] motsPhrase = phrase.toLowerCase().split("\\s+"); // Convertir la phrase en minuscules
         List<Instance> correction = new ArrayList<>();
        List<String> motsInconnus = new ArrayList<>();
        for (String mot : motsPhrase) {
            System.out.println(mot);
            if (!this.mots.contains(mot)) {
                motsInconnus.add(mot);
                System.out.println(mot);
            }
        }
        System.out.println("Les mots mal écrits : " + motsInconnus.size());
        System.out.println("Mots inconnus : " + motsInconnus);

        int[][] dist = new int[mots.size()][2];

        for (String mot : motsInconnus) {
            int i = 0;
            for (String mott : mots) {
                dist[i][0] = calculateDistance(mott, mot);
                dist[i][1] = i;
                i++;
            }
            Arrays.sort(dist, Comparator.comparingInt(a -> a[0]));

            // Extraction des cinq premiers petits éléments
            int[][] cinqPremiers = Arrays.copyOfRange(dist, 0, 5);

            String[] correctionMots = new String[5];
            for (int j = 0; j < 5; j++) {
                correctionMots[j] = mots.get(cinqPremiers[j][1]);
                System.out.println(cinqPremiers[j][1]);
                System.out.println(cinqPremiers[j][0]);
                System.out.println(mots.get(cinqPremiers[j][1]));
            }
            Instance instance = new Instance(correctionMots, mot, 5);
            correction.add(instance);
        }
        for (Instance instance : correction) {
            System.out.println("Pour le mot : " + instance.classe);
            for (String mot : instance.att) {
                System.out.print(mot + "\t");
            }
            System.out.println();
        }
        System.out.println(correction.size());
        return correction;
    }
}
