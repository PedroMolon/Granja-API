package com.pedromolon.granjaapi.repository;

import com.pedromolon.granjaapi.domain.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRepository extends JpaRepository<Standard, Long> {
    List<Standard> findByEmpresaId(Long empresaId);
    boolean existsByEmpresaIdAndNomeIgnoreCase(Long empresaId, String nome);
}
