package com.example.knnfinalprojet;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.knnfinalprojet.Controller.*;
import static com.example.knnfinalprojet.FichierVersListeInstances.classe;
import static com.example.knnfinalprojet.FichierVersListeInstances.dataset;
import static java.lang.Double.NaN;

@Service
public class AllServ {


    static String str1;
    static String str2;
    private static  KnnBDRepos knnbdrepos;
    @Autowired
    public AllServ(KnnBDRepos knnbdrepos ){

        this.knnbdrepos=knnbdrepos;
    }
    public static List<String> getFileContentAsList(String resourceFilePath) throws IOException {
        // stopwords
        File file = ResourceUtils.getFile(resourceFilePath);
        List<String> lines = Files.readAllLines(file.toPath());
        lines = lines.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());
        return lines;
    }

    public static List<String> removeStopwords(String input) throws IOException {
        List<String> inputList = new ArrayList<>(Arrays.asList(input.toLowerCase().split("[,\\s;\\.]+")));
        inputList.removeAll(getFileContentAsList("stopwords.txt"));
        return inputList;
    }


    public static List<String> lemma(String text,int choix) throws IOException {
        List<String> input =new ArrayList<>();
        System.out.println(text);
        if(choix ==2){//c-ad : une phrase
        input = removeStopwords(text);}
        else //c-a-d : mot
        input.add(text) ;
        List<String> output = new ArrayList<>();
        // Set up pipeline properties
        Properties props = new Properties();
        // Set the list of annotators to run
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        // Build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        for (String item : input) {
            // Create a document object
            CoreDocument document = new CoreDocument(item);
            // Annotate the document
            pipeline.annotate(document);
            // Display tokens
            for (CoreLabel tok : document.tokens()) {
                output.add(tok.lemma());
            }
        }
        return output;
    }

    public static int findMin(int a, int b, int c) {
        int min = Math.min(a, b);
        return Math.min(min, c);
    }

    public static int LevenshteinDistance(String s1, String s2)
    {

        str1 = s1.toLowerCase();
        str2 = s2.toLowerCase();
        int[][] d = new int[str1.length() + 1][str2.length() + 1];
        int min;
        if(str1.isEmpty()){
            return str2.length();
        }
        else if(str2.isEmpty()){
            return str1.length();
        }

        for (int i = 0; i < str1.length(); i++)
        {
            for (int j = 0; j < str2.length(); j++) {
                if (i == 0) {
                    d[i][j] = j;
                }
                else if (j == 0) {
                    d[i][j] = i;
                }
                else {
                    min= findMin(d[i - 1][j - 1],d[i - 1][j],d[i][j - 1]);
                    if( str1.charAt(i) == str2.charAt(j)){
                        d[i][j]=min;
                    }
                    else{
                        d[i][j]=min+1;
                    }
                }
            }
        }

        return d[str1.length()-1][str2.length()-1];
    }
    //BOW Similarity
    public static double similarity(List<String> text1, List<String> text2){
        int i, ps = 0;
        double norm1 = 0.0, norm2 = 0.0, sim;
        List<String> recap = new ArrayList<>();
        for(i=0; i<text1.size(); i++){
            if(!recap.contains(text1.get(i))){
                recap.add(text1.get(i));
            }
        }
        for(i=0; i<text2.size(); i++){
            if(!recap.contains(text2.get(i))){
                recap.add(text2.get(i));
            }
        }

        int[] vtext1 = new int[recap.size()];
        int[] vtext2 = new int[recap.size()];
        i = 0;
        for (String s : recap) {
            int count1 = 0;
            int count2 = 0;
            for (String s1 : text1){
                if (s.equals(s1)) {
                    count1++;
                }
            }
            for (String s2 : text2){
                if (s.equals(s2)) {
                    count2++;
                }
            }
            vtext1[i]=count1;
            vtext2[i]=count2;
            i++;
        }
        for(i=0; i<recap.size(); i++){
            ps = ps + (vtext1[i] * vtext2[i]);
            norm1 = norm1 + Math.pow(vtext1[i], 2);
            norm2 = norm2 + Math.pow(vtext2[i], 2);
        }
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);
        sim = ps / (norm1 * norm2);

        return  sim;

    }
    public static ArrayList<Statistique> getStatistique() throws IOException {
        FichierVersListeInstances a=new FichierVersListeInstances();
        String nom=UPLOAD_DIR+fileName;
        ArrayList<Statistique> listofstatistique=new ArrayList<Statistique>();
        ArrayList<Instance> dataset=a.File2list(nom,choix,1);
        ArrayList<String> exemple= a.getAttribute( nom,  choix);
        ArrayList<String> classe=a.classe;
        System.out.println("la taile est:"+exemple.size());
        for(String clas:classe) {
            System.out.println("la classe est :"+clas);
            int nbr=0;
            for (Instance ins : dataset) {
                if (ins.classe.equals(clas))
                    nbr++;
            }
            System.out.println(nbr);
            double pourcentage=((double)(nbr*100)/(double) dataset.size());
            System.out.println(pourcentage);
            Statistique sta=new Statistique(clas,pourcentage);
            listofstatistique.add(sta);

        }
      return listofstatistique;
   }
public static ArrayList<Mesure> getMesures(int choix, int k, int nbr, int type) throws IOException {

    Instance b = new Instance();
    ArrayList <Mesure> Mesures=new ArrayList<>();
    String[] classo=classe.toArray(new String[classe.size()]);
    KnnBD a=new KnnBD();
    if(choix==1)//c-a-d Cross Validation
    {
        int[][] sommeMatrice = new int[classo.length + 1][classo.length + 1];
        double taux=0,PrecisionG=0,RappelG=0,F_mesureG=0;
        for (int n=0;n<nbr;n++)
        {
            Mesure m = new Mesure();
            ArrayList<Instance>[] ChoixInstances = b.CrossValidation(dataset,nbr,n);
            m.matrice(ChoixInstances[0], ChoixInstances[1], classo, k,type);
            int[][] mat = m.matrice;
            m.Calcul(mat);
            taux+=m.Exactitude;
            RappelG+=m.RappelG;
            F_mesureG+=m.F_mesureG;
            PrecisionG+=m.PrecisionG;
            for (int i = 0; i < mat.length; i++) {
                for (int j = 0; j < mat[i].length; j++) {
                    sommeMatrice[i][j] = sommeMatrice[i][j]+mat[i][j];
                }
            }
            Mesures.add(m);
        }
        Mesure m=new Mesure();
        m.setF_mesureG((double)F_mesureG/nbr);
        m.setRappelG((double)RappelG/nbr);
        m.setPrecisionG((double)PrecisionG/nbr);
        m.setExactitude((double)taux/nbr);
        for (int i = 0; i < sommeMatrice.length; i++) {
            for (int j = 0; j < sommeMatrice[i].length; j++) {
                sommeMatrice[i][j] /= nbr;
            }
        }
        m.setMatrice(sommeMatrice);
        Mesures.add(m);
        if(m.getF_mesureG()==NaN){
            a.setF_mesure(0);
        }else{
            a.setF_mesure(m.getF_mesureG());
        }
        if(m.getExactitude()==NaN){
            a.setExactitude(0);
        }else{
            a.setExactitude(m.getExactitude());
        }
        if(m.getPrecisionG()==NaN){
            a.setPrecizion(0);
        }else{
            a.setPrecizion(m.getPrecisionG());
        }
        if(m.getRappelG()==NaN){
            a.setRappel(0);
        }else{
            a.setRappel(m.getRappelG());
        }
        a.setK(k);
        a.setCheminDS(fileName);
        knnbdrepos.save(a);

    }
    else//pourcentage split
    {
        Mesure m = new Mesure();
        ArrayList<Instance>[] ChoixInstances = b.Partitionnement(dataset,nbr);
        System.out.println("taile est "+dataset.size());
        m.matrice(ChoixInstances[0], ChoixInstances[1], classo, k,type);
        int[][] mat = m.matrice;
        m.Calcul(mat);
        Mesures.add(m);
        if(m.getF_mesureG()==NaN){
            a.setF_mesure(0);
        }else{
            a.setF_mesure(m.getF_mesureG());
        }
        if(m.getExactitude()==NaN){
            a.setExactitude(0);
        }else{
            a.setExactitude(m.getExactitude());
        }
        if(m.getPrecisionG()==NaN){
            a.setPrecizion(0);
        }else{
            a.setPrecizion(m.getPrecisionG());
        }
        if(m.getRappelG()==NaN){
            a.setRappel(0);
        }else{
            a.setRappel(m.getRappelG());
        }
        a.setK(k);
        a.setCheminDS(fileName);
        System.out.println(m.getF_mesureG());
        System.out.println(m.getExactitude());
        System.out.println(m.getPrecisionG());
        System.out.println(m.getRappelG());
        knnbdrepos.save(a);
    }
    return Mesures;
}
    public static List<KnnBD> getKnnCheminDS(String chemin)  {
        List<KnnBD> list = knnbdrepos.getKnnByCheminDS(chemin);
        return  list;
    }
    public static String getFeeling(String phrase) throws IOException {
        FichierVersListeInstances a=new FichierVersListeInstances();
        String nom="src/rep/yelp.csv";
        ArrayList<Instance> dataset=a.File2list(nom,choix,1);
        String []tab={phrase};
        Instance b = new Instance(tab,null,1);
        knn k = new knn(19);
        ArrayList<Instance>[] ChoixInstances = b.Partitionnement(dataset,20);
        System.out.println("taile est "+dataset.size());
        String tst = k.PredictClasse(ChoixInstances[0], b,1);
        return tst;
    }

}
