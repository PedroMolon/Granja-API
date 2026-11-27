package com.pedromolon.granjaapi.repository;

import com.pedromolon.granjaapi.domain.RegistroMortalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroMortalidadeRepository extends JpaRepository<RegistroMortalidade, Long> {
    List<RegistroMortalidade> findByLoteIdOrderByDataRegistroAsc(Long loteId);

    @Query("select coalesce(sum(m.quantidade), 0) from RegistroMortalidade m where m.lote.id = :loteId")
    long totalMortalidade(Long loteId);
}
