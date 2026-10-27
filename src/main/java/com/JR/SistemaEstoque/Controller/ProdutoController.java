package com.JR.SistemaEstoque.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.JR.SistemaEstoque.Model.Produto;
import com.JR.SistemaEstoque.Service.ProdutoService;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {
	@Autowired
    private ProdutoService service;

    // 🔍 LISTAR TODOS
    @GetMapping
    public List<Produto> listar() {
        return service.produtos(); // seu método atual
    }

    // 🔍 BUSCAR POR ID
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable UUID id) {
        return service.findById(id); // seu método atual
    }

    // 💾 SALVAR
    @PostMapping
    public void salvar(@RequestBody Produto produto) {
        service.save(produto); // seu método atual
    }

    // ✏️ ATUALIZAR
    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable UUID id, @RequestBody Produto produto) {
        return service.atualizarProduto(id, produto); // seu método atual
    }

    // ❌ DELETAR POR ID
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        service.deleteById(id); // seu método atual
    }

    // 🔎 BUSCAR POR NOME
    @GetMapping("/buscar")
    public List<Produto> buscarPorNome(@RequestParam String nome) {
        return service.buscarPorName(nome); // seu método atual
    }

    // 💰 VALOR TOTAL DO ESTOQUE
    @GetMapping("/total")
    public String valorTotal() {
        return service.calcularValorTotalEstoque().toString();
    }
}
	


