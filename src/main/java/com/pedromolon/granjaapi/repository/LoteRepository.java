package com.pedromolon.granjaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {
    boolean existsByCodigo(String codigo);
    List<Lote> findByStatus(StatusLote status);

    @Query("select l from Lote l join fetch l.standard where l.status = :status")
    List<Lote> findByStatusComStandard(StatusLote status); // evita N+1 no relatório
}
