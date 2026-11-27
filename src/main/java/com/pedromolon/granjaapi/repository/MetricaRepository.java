package com.pedromolon.granjaapi.repository;

import com.pedromolon.granjaapi.domain.Metrica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetricaRepository extends JpaRepository<Metrica, Long> {
    List<Metrica> findByStandardIdOrderBySemanaIdadeAsc(Long standardId);
    Optional<Metrica> findByStandardIdAndSemanaIdade(Long standardId, Integer semanaIdade);
    boolean existsByStandardId(Long standardId);

    @Modifying
    @Query("delete from Metrica m where m.standard.id = :standardId")
    void deleteByStandardId(Long standardId); // para reimportar o CSV
}
