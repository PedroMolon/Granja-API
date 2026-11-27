package com.pedromolon.granjaapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroMortalidadeRepository extends JpaRepository<RegistroMortalidade, Long> {
    List<RegistroMortalidade> findByLoteIdOrderByDataRegistroAsc(Long loteId);

    @Query("select coalesce(sum(m.quantidade), 0) from RegistroMortalidade m where m.lote.id = :loteId")
    long totalMortalidade(Long loteId);
}
