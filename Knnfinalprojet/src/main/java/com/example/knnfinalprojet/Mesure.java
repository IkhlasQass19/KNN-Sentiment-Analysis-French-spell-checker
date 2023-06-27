package com.example.knnfinalprojet;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import static java.lang.System.exit;
@JsonSerialize
public class Mesure {
    Double Exactitude,RappelG,F_mesureG,PrecisionG;
    Double [] Rappel,F_mesure,Precision;

    int[][] matrice=null;


    public void matrice(ArrayList<Instance> train, ArrayList<Instance> list, String[] classe, int knn,int type) throws IOException {
         int[][] Matrix = new int[classe.length + 1][classe.length + 1];
         knn k = new knn(knn);

         for (int i = 0; i < classe.length; i++) {
             ArrayList<Instance> elementsClass = new ArrayList<>();

             for (Instance objet : list) {
                 if (objet.classe.equals(classe[i])) {
                     elementsClass.add(objet);
                 }
             }

             for (Instance objet : elementsClass) {
                 String tst = k.PredictClasse(train, objet,type);
                 System.out.println(tst);
                 for (int j = 0; j < classe.length; j++) {
                     if (tst.equals(classe[j])) {
                         Matrix[i][j]++;
                         break;
                     }
                 }
             }
             elementsClass.clear();
         }

         for (int r = 0; r < Matrix.length - 1; r++) {
             for (int j = 0; j < Matrix[r].length - 1; j++) {
                 Matrix[r][Matrix[r].length - 1] += Matrix[r][j];
             }
         }

         for (int c = 0; c < Matrix[Matrix.length - 1].length; c++) {
             for (int l = 0; l < Matrix.length - 1; l++) {
                 Matrix[Matrix.length - 1][c] += Matrix[l][c];
             }
         }

         this.matrice = Matrix;
     }
    public double sum(Double [] tab)
    {
        double sum=0;
        for(int i=0;i<tab.length;i++)
        {
            sum+=tab[i];
        }
        return sum;
    }
   public void Calcul(int [][] mat){
       if (mat==null)
       {
           System.out.println("matrice invalide");
           exit(0);
       }
       //calcul exactitude:
       int sum=0;
       for(int i=0;i<mat.length-1;i++)
       {
           for(int j=0;j<mat[i].length-1;j++)
               if(i==j)
                   sum+=mat[i][j];
       }
       this.Exactitude=(double)sum/(double)mat[mat.length-1][mat[mat.length-1].length-1];
       //calcul de Précision:

       Double [] pres=new Double[mat.length-1];
       for(int i=0;i<mat.length-1;i++)
       {
           for(int j=0;j<mat[i].length-1;j++)
               if(i==j)
                    pres[i]=(double)mat[i][j]/(double)mat[mat.length-1][j];
       }
       this.Precision=pres;
       //calcul de rappel
       Double [] rap=new Double[mat.length-1];
       for(int i=0;i<mat.length-1;i++)
       {
           for(int j=0;j<mat[i].length-1;j++)
               if(i==j)
                   rap[i]=(double)mat[i][j]/(double)mat[i][mat.length-1];
       }
       this.Rappel=rap;
       //calcul de f_mesure
       Double [] mes=new Double[mat.length-1];
       for(int i=0;i<mat.length-1;i++)
       {
           mes[i]= (double)(2*rap[i]*pres[i]) / (double)(rap[i]+pres[i]);
       }
       this.F_mesure=mes;
       // calcul de Rappel,F_mesure,Precision;
       this.PrecisionG=((double)1/(double)this.Precision.length)*sum(this.Precision);
       //System.out.println(this.Precision.length);
       this.RappelG=((double)1/(double)this.Rappel.length)*sum(this.Rappel);
       this.F_mesureG= (double)(2*PrecisionG*RappelG) / (double)(RappelG+PrecisionG);

   }

    public int[][] getMatrice() {
        return matrice;
    }

    public void setMatrice(int[][] matrice) {
        this.matrice = matrice;
    }

    public Double getExactitude() {
        return Exactitude;
    }

    public void setExactitude(Double exactitude) {
        Exactitude = exactitude;
    }

    public Double getRappelG() {
        return RappelG;
    }

    public void setRappelG(Double rappelG) {
        RappelG = rappelG;
    }

    public Double getF_mesureG() {
        return F_mesureG;
    }

    public void setF_mesureG(Double f_mesureG) {
        F_mesureG = f_mesureG;
    }

    public Double getPrecisionG() {
        return PrecisionG;
    }

    public void setPrecisionG(Double precisionG) {
        PrecisionG = precisionG;
    }
}
