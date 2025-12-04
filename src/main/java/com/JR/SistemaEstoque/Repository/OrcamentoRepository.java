package com.JR.SistemaEstoque.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JR.SistemaEstoque.Model.Orcamento;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, UUID> {

}
