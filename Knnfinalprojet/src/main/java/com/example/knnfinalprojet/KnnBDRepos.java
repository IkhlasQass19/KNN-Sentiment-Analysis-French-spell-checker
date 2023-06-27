package com.example.knnfinalprojet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface KnnBDRepos extends JpaRepository<KnnBD, Integer> {
    @Query("SELECT k FROM KnnBD k WHERE k.cheminDS = ?1")
    List<KnnBD> getKnnByCheminDS(String cheminDS) ;
}
