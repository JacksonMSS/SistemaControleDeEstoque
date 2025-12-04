package com.JR.SistemaEstoque.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JR.SistemaEstoque.Model.Produto;
import com.JR.SistemaEstoque.Repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class ProdutoService{
	
	@Autowired
    private ProdutoRepository produtorepository;

    
    public List<Produto> produtos() {
        return produtorepository.findAll();
    }

    public Optional<Produto> buscarPorID(Produto produto) {
        return produtorepository.findById(produto.getId());
    }

    public void delete(UUID id) {
        produtorepository.deleteById(id);
    }

    //Atualiza dados do profuto
    public Produto atualizarProduto(UUID ID, Produto produtoAtualizado) {
        return produtorepository.findById(ID)
                .map(produtoExistente -> {
                    produtoExistente.setNome(produtoAtualizado.getNome());
                    produtoExistente.setPreco(produtoAtualizado.getPreco());
                    produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
                    return produtorepository.save(produtoExistente);
                }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void deleteByNome(String nome) {
        Produto produto = produtorepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produtorepository.deleteById(produto.getId());
    }

    public List<Produto> buscarPorName(String nome) {
        return produtorepository.findByNomeContainingIgnoreCase(nome);
    }

    public void listarTodos() {
        produtorepository.findAll().forEach(p -> System.out.println(p.getNome()));
    }
    
    public BigDecimal calcularValorTotalEstoque() {
       
        List<Produto> todosProdutos = produtos(); 
  
        BigDecimal valorTotal = todosProdutos.stream()
            .map(p -> {
                if (p.getPreco() != null && p.getQuantidade() > 0) {
                  
                    return p.getPreco().multiply(new BigDecimal(p.getQuantidade()));
                }
                return BigDecimal.ZERO;
            })
           
            .reduce(BigDecimal.ZERO, BigDecimal::add); 

        return valorTotal;
    }


    @Transactional
	public void save(Produto produto) {
		produtorepository.save(produto);
	}

	public Iterable<Produto> findAll() {
		return produtorepository.findAll();
	}

	public Produto findById(UUID id) {
		 return produtorepository.findById(id)
			        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
			}

	public void deleteById(UUID id) {
		try {
			produtorepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao apagar produto com ID: " + id, e);
        }
		
	}
	
	}
	
	