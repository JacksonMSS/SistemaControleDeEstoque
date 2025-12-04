package com.JR.SistemaEstoque.Model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_Orcamento")
public class Orcamento implements Serializable {
	
	@Serial
	private static final long serialVersionUID= 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	 @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	    @JoinColumn(name = "orcamento_id")
	    private List<ItemOrcamento> itens = new ArrayList<>();
	 
	 
	public List<ItemOrcamento> getItens() {
		return itens;
	}

	 public void setItens(List<ItemOrcamento> itens) {
		 this.itens = itens;
	 }

	public void addProduto(Produto produto, int quantidade) {
		itens.add(new ItemOrcamento(produto, quantidade));	
	} 
	
	public  void ImprimirOrcamento() {
		for(ItemOrcamento item : itens) {
			System.out.printf(
			item.getProduto().getNome(),
			item.getQuantidade(),
			item.getValortotal()
		);
		}
	}
		
     public BigDecimal calcularvalor(){
    	 BigDecimal total = BigDecimal.ZERO;
    	 for(ItemOrcamento item : itens) {
    		 total = total.add(item.getValortotal());
    	 }
     return total;
	}
	
}
