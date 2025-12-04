package com.JR.SistemaEstoque.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JR.SistemaEstoque.Model.ItemOrcamento;
import com.JR.SistemaEstoque.Model.Orcamento;
import com.JR.SistemaEstoque.Model.Produto;
import com.JR.SistemaEstoque.Repository.OrcamentoRepository;

@Service
public class OrcamentoService {
		
	@Autowired
	private  OrcamentoRepository orcamentoRepository;
	
	public List<Orcamento> orcamentos (){
		return orcamentoRepository.findAll();
	}
	// Adiciona um produto ao orçamento
    public void adicionarProduto(Orcamento orcamento, Produto produto, int quantidade) {
        Optional<ItemOrcamento> itemExistente = orcamento.getItens()
                .stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            // já existe no orçamento → só soma a quantidade
            ItemOrcamento item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
        } else {
            // novo produto no orçamento
            orcamento.getItens().add(new ItemOrcamento(produto, quantidade));
        }
    }

    // Remove um produto do orçamento
    public void removerProduto(Orcamento orcamento, UUID produtoId) {
        orcamento.getItens().removeIf(item -> item.getProduto().getId().equals(produtoId));
    }

    // Atualiza a quantidade de um produto
    public void alterarQuantidade(Orcamento orcamento, UUID produtoId, int novaQuantidade) {
        for (ItemOrcamento item : orcamento.getItens()) {
            if (item.getProduto().getId().equals(produtoId)) {
                item.setQuantidade(novaQuantidade);
                return;
            }
        }
    }

    // Calcula o valor total do orçamento
    public BigDecimal calcularTotal(Orcamento orcamento) {
        return orcamento.getItens()
                .stream()
                .map(ItemOrcamento::getValortotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
