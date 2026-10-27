package com.JR.SistemaEstoque.Model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "TB_Produtos", uniqueConstraints = {
	    @UniqueConstraint(columnNames = "nome")
	})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto implements Serializable {
	@Serial
	  private static final long serialVersionUID= 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(unique = true, nullable = false)
	private String nome;


	@Column
	private int quantidade;


	@Column
	private BigDecimal preco;

	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}




	public void setNome(String nome) {
		this.nome = nome;
	}


	public int getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}


	public BigDecimal getPreco() {
		return preco;
	}


	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

}


	



