
package com.JR.SistemaEstoque.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.JR.SistemaEstoque.Model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    Optional<Produto> findByNome(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    @Query(value = """
        SELECT *
        FROM tb_produtos
        ORDER BY
            split_part(nome, ' ', 1),
            CAST(
                NULLIF(
                    regexp_replace(nome, '^.*?([0-9]+).*$', '\\1'),
                    nome
                ) AS INTEGER
            ) NULLS LAST,
            nome ASC
        """, nativeQuery = true)
    List<Produto> listarOrdenadosPorNomeENumero();
}
