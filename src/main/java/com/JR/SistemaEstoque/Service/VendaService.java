package com.JR.SistemaEstoque.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.JR.SistemaEstoque.Model.ItemOrcamento;
import com.JR.SistemaEstoque.Model.Produto;
import com.JR.SistemaEstoque.Model.Vendas;
import com.JR.SistemaEstoque.Repository.VendasRepository;

import jakarta.transaction.Transactional;

@Service
public class VendaService {
	
	@Autowired 
	private VendasRepository vendasRepository;
	
	@Autowired 
	private ProdutoService produtoService; 
		
	
	// Salvar venda
    public Vendas salvar(Vendas venda) {
        return vendasRepository.save(venda);
    }

    // Buscar todas
    @Transactional
    public List<Vendas> listarTodas() {
    	return vendasRepository.findAllByOrderByDataVendaDesc();
    }

    // Buscar por ID
    public Optional<Vendas> buscarPorId(UUID id) {
        return vendasRepository.findById(id);
    }
    
    @Transactional 
    public String getResumoProdutosDaVenda(UUID vendaId) {
            
        Optional<Vendas> vendaOptional = vendasRepository.findById(vendaId);

        if (vendaOptional.isEmpty()) {
            return "Venda não encontrada.";
        }

        Vendas venda = vendaOptional.get();
        
        
        List<ItemOrcamento> itens = venda.getItens();
        
        if (itens == null || itens.isEmpty()) {
            return "Nenhum produto associado.";
        }
        
        
        return itens.stream()
            .map(item -> String.format("%dx %s", 
                                        item.getQuantidade(), 
                                        item.getProduto().getNome())) 
            .collect(Collectors.joining("<br>"));
    }

    @Transactional
    public List<Vendas> buscarVendasPorData(String dataString) {
        String busca = dataString.trim();
      
        if (busca.isEmpty()) {
            return vendasRepository.findAll();
        }

        try {
        	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            
            LocalDate dataBusca = LocalDate.parse(busca, formatter);
            LocalDateTime inicioDoDia = dataBusca.atStartOfDay();
            LocalDateTime fimDoDia = dataBusca.atTime(23, 59, 59);

            return vendasRepository.findByDataVendaBetween(inicioDoDia, fimDoDia);

        } catch (DateTimeParseException e) {
            System.err.println("Formato inválido. Use DD-MM-AAAA. " + e.getMessage());
            return List.of(); 
        }
    }
    
    public void apagarVenda(UUID id) {
        vendasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Venda não encontrada com o ID: " + id));
        vendasRepository.deleteById(id);
    }

   
    @Transactional 
    public void cancelarVenda(UUID id) {
        Optional<Vendas> vendaOpt = vendasRepository.findById(id);
        
        if (vendaOpt.isPresent()) {
            Vendas venda = vendaOpt.get();
            
            if (venda.isCancelada()) {
                throw new RuntimeException("A Venda já foi cancelada previamente.");
            }
            
            
            for (ItemOrcamento item : venda.getItens()) {
                
                Produto produtoEmEstoque = produtoService.findById(item.getProduto().getId()); 
                
                
                int novaQuantidade = produtoEmEstoque.getQuantidade() + item.getQuantidade();
                produtoEmEstoque.setQuantidade(novaQuantidade);
                
                
                produtoService.save(produtoEmEstoque);
            }
            
            
            venda.setCancelada(true);
            vendasRepository.save(venda);
            
        } else {
            throw new RuntimeException("Venda não encontrada com o ID: " + id);
        }
    }

    
    public void excluir(UUID id) {
        vendasRepository.deleteById(id);
    }
    
    
}


