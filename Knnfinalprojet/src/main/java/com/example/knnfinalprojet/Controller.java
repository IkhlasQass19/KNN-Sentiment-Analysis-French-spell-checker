package com.example.knnfinalprojet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.knnfinalprojet.FichierVersListeInstances.*;

@CrossOrigin("*")
@RestController
@RequestMapping("")
public class Controller {

    static final String UPLOAD_DIR = "src/rep/";
    static String fileName;
    static int choix;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // Vérifier si le fichier est vide
        if (file.isEmpty()) {
            return new ResponseEntity<>("Aucun fichier sélectionné", HttpStatus.BAD_REQUEST);
        }

        try {
            // Générer un nom de fichier unique
            String fileName = file.getOriginalFilename();
            String uniqueFileName = fileName;
            this.fileName = fileName;
            // Chemin complet du fichier de destination
            Path destinationPath = Path.of(UPLOAD_DIR, uniqueFileName);

            // Copier le fichier vers le répertoire de destination
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            // System.out.println(fileName);
            if (fileName.contains(".csv"))
                choix = 2;
            else
                choix = 1;
            // Vous pouvez également exécuter d'autres opérations avec le fichier ici
            // FichierVersListeInstances a=new FichierVersListeInstances();
            // a.File2list("C:\\Users\\hp\\IdeaProjects\\test1323\\src\\rep\\"+this.fileName,choix);
            return new ResponseEntity<>("Fichier uploadé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erreur lors du stockage du fichier", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getliststat")

    public ArrayList<Statistique> getstat() throws IOException {
        return AllServ.getStatistique();
    }

    @GetMapping("/getnbr")
    public int[] getnbr() throws IOException {
        int[] data = new int[3];
        data[0] = dataset.size();
        data[1] = classe.size();
        data[2] = attribute.size();

        return data;
    }

    @GetMapping("/getAttrebutandclasse")
    public Map<ArrayList<String>, ArrayList<String>> getAll() {
        Map<ArrayList<String>, ArrayList<String>> dataMap = new HashMap<>();
        dataMap.put(attribute, classe);
        return dataMap;
    }

    @GetMapping("/getresult")
    public ArrayList<Mesure> getResult(@RequestParam int k, @RequestParam int choix, @RequestParam int nbr, @RequestParam int type) throws IOException {
        return AllServ.getMesures(choix, k, nbr,type);
    }
    @GetMapping("/getHistorique")
    public List<KnnBD> getHistorique(@RequestParam String chemin) throws IOException {
        return AllServ.getKnnCheminDS(chemin);
    }
    @GetMapping("/getFeeling")
    public String getFeeling(@RequestParam String phrase) throws IOException {
        return AllServ.getFeeling(phrase);
    }
    @GetMapping("/getOrthographe")
    public List<Instance> getCorrection(@RequestParam String phrase) throws IOException {
        Orthographe a=new Orthographe();
        a.generer();
        List <Instance> list=a.Corriger(phrase);
        return  list;
    }
}




