package com.JR.SistemaEstoque.Model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "TB_ItensOrcamento")
public class ItemOrcamento implements Serializable{
	
	@Serial
	public static final long serialVersionUID= 1L;
	
	public ItemOrcamento() {
	}
		
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	
	@ManyToOne
	@JoinColumn(name = "venda_id") 
	private Vendas venda; 
	
	@ManyToOne(fetch = FetchType.EAGER)
	    private Produto produto;

	    private int quantidade;

	    private BigDecimal precoUnitario;

	    public ItemOrcamento(Produto produto, int quantidade) {
	        this.produto = produto;
	        this.quantidade = quantidade;
	        this.precoUnitario = produto.getPreco();
	    }

	    @Override
	    public String toString() {
	        return String.format("%s | Qtd: %d | Unit: R$ %.2f | Total: R$ %.2f",
	                produto.getNome(),
	                quantidade,
	                precoUnitario,
	                getValortotal());
	    }
	   
	    public String getNomeProduto() {
	        // Acessa o objeto Produto mapeado e chama o getNome() dele.
	        if (this.produto != null) {
	            return this.produto.getNome(); 
	        }
	        // Retorno em caso de erro.
	        return "(Produto sem nome)"; 
	    }

	    public BigDecimal getValortotal() {
	        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
	    }	    

	    public int getQuantidade() {
	        return quantidade;
	    }

	    public void setQuantidade(int quantidade) {
	        this.quantidade = quantidade;
	    }

	    public Produto getProduto() {
	        return produto;
	    }

	    public void setProduto(Produto produto) {
	        this.produto = produto;
	    }

	    public BigDecimal getPrecoUnitario() {
	        return precoUnitario;
	    }

	    public void setPrecoUnitario(BigDecimal precoUnitario) {
	        this.precoUnitario = precoUnitario;
	    }

	
	}