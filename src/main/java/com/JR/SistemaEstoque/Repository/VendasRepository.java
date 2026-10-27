package com.JR.SistemaEstoque.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.JR.SistemaEstoque.Model.Vendas;

@Repository
public interface VendasRepository extends JpaRepository<Vendas,UUID> {

	List<Vendas> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);

	List<Vendas> findByDataVenda(LocalDateTime startOfDay);
	
	List<Vendas> findAllByOrderByDataVendaDesc();
}
