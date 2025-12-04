package com.JR.SistemaEstoque.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JR.SistemaEstoque.Model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

	List<Produto> findByNomeContainingIgnoreCase(String nome);
	Optional<Produto> findByNome(String nome);
	

}
