package com.pedromolon.granjaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroPesagemRepository extends JpaRepository<RegistroPesagem, Long> {
    List<RegistroPesagem> findByLoteIdOrderByDataRegistroAsc(Long loteId);
    Optional<RegistroPesagem> findFirstByLoteIdOrderByDataRegistroDesc(Long loteId);
    boolean existsByLoteIdAndDataRegistro(Long loteId, LocalDate dataRegistro);
}
