package com.JR.SistemaEstoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.JR.SistemaEstoque.Telas.ManFrame;

@SpringBootApplication
public class SistemaEstoqueApplication {

	public static void main(String[] args) { 
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext context = SpringApplication.run(SistemaEstoqueApplication.class, args);
        java.awt.EventQueue.invokeLater(() -> {

            ManFrame frame = context.getBean(ManFrame.class);

            // Centraliza na tela
            frame.setLocationRelativeTo(null);

            // Exibe a tela
            frame.setVisible(true);
        });
    }
}
