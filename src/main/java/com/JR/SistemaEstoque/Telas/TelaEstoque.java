package com.JR.SistemaEstoque.Telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.springframework.stereotype.Component;

import com.JR.SistemaEstoque.Model.Produto;
import com.JR.SistemaEstoque.Model.Vendas;
import com.JR.SistemaEstoque.Service.ProdutoService;
import com.JR.SistemaEstoque.Service.VendaService;

@Component
public class TelaEstoque  extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldBuscar;

    private DefaultListModel<Produto> modeloLista;
    private JList<Produto> listprodutos;
    
   
    private DefaultListModel<Vendas> modeloVendas; 
    private JList<Vendas> listVendas; 


    private final ProdutoService produtoService;
    private final VendaService vendaService;
    private JLabel lblTotalEstoque;
    private JTextField textBuscarVendas;

   
    public TelaEstoque(ProdutoService produtoService, VendaService vendaService) {
        this.produtoService = produtoService;
        this.vendaService = vendaService; 
        initialize();
        carregarProdutos();
        carregarVendas(); 
    }

    
    public void refreshData() {
        carregarProdutos();
        carregarVendas();
        atualizarValorTotalEstoque();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            refreshData(); 
        }
        super.setVisible(b);
    }
    
    private void initialize() {
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 255, 128));
        panel.setBounds(10, 10, 766, 543);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Produtos em estoque ");
        lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 20));
        lblNewLabel.setBounds(53, 10, 260, 32);
        panel.add(lblNewLabel);

        // Inicializando modelo e JList de PRODUTOS
        modeloLista = new DefaultListModel<>();
        listprodutos = new JList<>(modeloLista);
        listprodutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listprodutos.setCellRenderer((listVendas, value, index, isSelected, cellHasFocus) -> {
            String nome = value != null && value.getNome() != null ? value.getNome() : "(sem nome)";
            String qtd = "Unidades:" + (value != null ? value.getQuantidade() : 0);
            String preco = " R$ " + (value != null && value.getPreco() != null ? value.getPreco().doubleValue() : 0.0);
            JLabel label = new JLabel(nome +  " | " +  qtd  +  "| " + preco);
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(listVendas.getSelectionBackground());
                label.setForeground(listVendas.getSelectionForeground());
            }
            return label;
        });

        JScrollPane scrollPane = new JScrollPane(listprodutos);
        scrollPane.setBounds(10, 117, 348, 416);
        panel.add(scrollPane);

        textFieldBuscar = new JTextField();
        textFieldBuscar.setBounds(10, 88, 348, 19);
        panel.add(textFieldBuscar);
        textFieldBuscar.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Buscar");
        lblNewLabel_1.setFont(new Font("Arial Black", Font.PLAIN, 11));
        lblNewLabel_1.setBounds(10, 52, 45, 26);
        panel.add(lblNewLabel_1);
        
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setFont(new Font("Arial Black", Font.PLAIN, 10));
        btnAtualizar.setBounds(273, 52, 85, 21);
        panel.add(btnAtualizar);
        btnAtualizar.addActionListener(e -> {
		    Produto selecionado = listprodutos.getSelectedValue();
            // ... (lógica de atualização)
		    if (selecionado != null) {
		        String novoNome = JOptionPane.showInputDialog(this, "Novo nome:", selecionado.getNome());
		        String novaQtdStr = JOptionPane.showInputDialog(this, "Nova quantidade:", selecionado.getQuantidade());
		        String novoPrecoStr = JOptionPane.showInputDialog(this, "Novo preço:", selecionado.getPreco());

		        try {
		            int novaQtd = Integer.parseInt(novaQtdStr);
		            BigDecimal novoPreco = new BigDecimal(novoPrecoStr);

		            selecionado.setNome(novoNome);
		            selecionado.setQuantidade(novaQtd);
		            selecionado.setPreco(novoPreco);

		            produtoService.save(selecionado);

		            listprodutos.repaint();

		            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
		        } catch (NumberFormatException ex1) {
		            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser numéricos.");
		        } catch (Exception ex2) {
		            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex2.getMessage());
		        }
		    } else {
		        JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar!");
		    }
		});
 
        JButton btnApagar = new JButton("Apagar");
        btnApagar.setFont(new Font("Arial Black", Font.PLAIN, 10));
        btnApagar.setBounds(178, 52, 85, 21);
        panel.add(btnApagar);
        
        // JList de VENDAS (CONFIGURAÇÃO)
        modeloVendas = new DefaultListModel<>();
        listVendas = new JList<>(modeloVendas);
        listVendas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Renderizador para Vendas
        listVendas.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            String status = value.isCancelada() ? "CANCELADA" : "OK";
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            String dataFormatada = value.getDataVenda().toLocalDate().format(formatter);            
          
            String resumoProdutos;

            try {                
                resumoProdutos = vendaService.getResumoProdutosDaVenda(value.getId());
            } catch (Exception e) {
                 resumoProdutos = "ERRO: Não foi possível carregar os produtos."; 
            }           
            
            String texto = String.format("<html>"
                    + "Data: %s | Status: %s"
                    + "<br>------------------------" 
                    + "<br>Produtos Vendidos: <br>%s"
                    + "<br>------------------------" 
                    + "<br>Total: R$ %.2f" 
                    + "</html>",                   
                    dataFormatada,
                    status,
                    resumoProdutos,
                    value.getValorTotal()
                    );
            
            JLabel label = new JLabel(texto);
            
            label.setBorder(new EmptyBorder(5, 5, 10, 5)); 
            
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(Color.WHITE);
            } else if (value.isCancelada()) {
                 label.setForeground(Color.RED);
            } else {
                 label.setForeground(Color.BLACK);
            }
            
            label.setOpaque(true);
            
            return label;
        });
        
        
        JLabel lblNewLabel_2 = new JLabel("Vendas");
        lblNewLabel_2.setFont(new Font("Arial Black", Font.PLAIN, 20));
        lblNewLabel_2.setBounds(379, 45, 96, 27);
        panel.add(lblNewLabel_2);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
        	Vendas selecionada = listVendas.getSelectedValue();
        	
        	if(selecionada != null) {
        		if(selecionada.isCancelada()) {
        			JOptionPane.showMessageDialog(this, "Esta venda já está cancelada!", "Aviso", JOptionPane.WARNING_MESSAGE);
        			return;
        		}
                
                // Mensagem de confirmação correta
        		int confirmacao = JOptionPane.showConfirmDialog(
        			    this,
        			    "Tem certeza que deseja CANCELAR a Venda ", 
        			    "Devolver ao estoque",
        			    JOptionPane.YES_NO_OPTION,
        			    JOptionPane.QUESTION_MESSAGE
        			); 
        
                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        // Chama o serviço para cancelar e reverter o estoque
                        vendaService.cancelarVenda(selecionada.getId()); 

                        // Atualiza as listas na tela (Estoque e Vendas)
                        refreshData(); 

                        JOptionPane.showMessageDialog(this, "Venda cancelada e estoque revertido com sucesso!");

                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao cancelar venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
        	} else {
                 JOptionPane.showMessageDialog(this, "Selecione uma venda para cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });        btnCancelar.setBounds(671, 87, 85, 21);
        panel.add(btnCancelar);
        
        JScrollPane scrollPane_1 = new JScrollPane(listVendas); // USANDO listVendas CORRETA
        scrollPane_1.setBounds(379, 117, 377, 416);
        panel.add(scrollPane_1);
        
        
        lblTotalEstoque = new JLabel("Total em Estoque");
        lblTotalEstoque.setFont(new Font("Arial Black", Font.PLAIN, 20));
        lblTotalEstoque.setBounds(540, 10, 197, 32);
        panel.add(lblTotalEstoque);

        lblTotalEstoque = new JLabel("R$ 0,00");
        lblTotalEstoque.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblTotalEstoque.setForeground(new Color(0, 100, 0)); // Verde escuro
        lblTotalEstoque.setBounds(546, 33, 210, 25);
        panel.add(lblTotalEstoque);
        
        JButton btnApagarVendas = new JButton("Apagar");
        btnApagarVendas.addActionListener(e -> { 
            Vendas selecionada = listVendas.getSelectedValue();
        	
            if(selecionada != null) {
                // Confirmação para APAGAR PERMANENTEMENTE
                int confirmacao = JOptionPane.showConfirmDialog(
        			    this,
        			    "Tem certeza que deseja APAGAR PERMANENTEMENTE a Venda " + 
        			    "? Esta ação é irreversível.", 
        			    "Confirmar Exclusão de Venda",
        			    JOptionPane.YES_NO_OPTION,
        			    JOptionPane.WARNING_MESSAGE // Alerta visual
        			); 
        
                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        // Chama o serviço para apagar a venda
                        vendaService.apagarVenda(selecionada.getId()); 

                        // Atualiza as listas na tela (Estoque e Vendas)
                        refreshData(); 

                        JOptionPane.showMessageDialog(this, "Venda apagada permanentemente com sucesso!");

                    } catch (RuntimeException ex) {
                 
                        JOptionPane.showMessageDialog(this, "Erro ao apagar venda: " + ex.getMessage() + 
                                                      "\nVerifique se há referências não apagadas (itens de venda).", 
                                                      "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                    }
                }
        	} else {
                 JOptionPane.showMessageDialog(this, "Selecione uma venda para apagar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnApagarVendas.setBounds(576, 87, 85, 21);
        panel.add(btnApagarVendas);
        
        textBuscarVendas = new JTextField();
        textBuscarVendas.setBounds(379, 88, 96, 19);
        panel.add(textBuscarVendas);
        textBuscarVendas.setColumns(10);
        
        JButton btnBucarVendas = new JButton("Buscar");
        btnBucarVendas.setBounds(377, 56, 85, 21);
        panel.add(btnBucarVendas);
        
        btnBucarVendas.addActionListener(e -> {
        	filtrarVendas();
        });
        
        btnBucarVendas.setBounds(485, 87, 85, 21);
        panel.add(btnBucarVendas);  
      
     //  botão APAGAR Produtos 
        btnApagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Produto selecionado = listprodutos.getSelectedValue();

                if (selecionado != null) {
                    int confirmacao = JOptionPane.showConfirmDialog(
                        TelaEstoque.this, 
                        "Tem certeza que deseja apagar o produto: " + selecionado.getNome() + "?",
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirmacao == JOptionPane.YES_OPTION) {
                        try {
                            produtoService.delete(selecionado.getId());
                            modeloLista.removeElement(selecionado);
                            JOptionPane.showMessageDialog(TelaEstoque.this, "Produto apagado com sucesso!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(TelaEstoque.this, "Erro ao apagar produto: ");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(TelaEstoque.this, "Selecione um produto para apagar!");
                }
            }
        });

       
        // Listener para busca dinâmica
        textFieldBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
    }

    private void carregarProdutos() {
        try {
            modeloLista.clear();
            if (produtoService == null) {
                JOptionPane.showMessageDialog(this, "ProdutoService está nulo. Passe o serviço ao criar TelaEstoque.");
                return;
            }
            
            List<Produto> todos = produtoService.produtos(); 
  
            List<Produto> ordenados = todos.stream()
                .sorted(Comparator.comparing(Produto::getNome, 
                                            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER))) 
                .collect(Collectors.toList());
             for (Produto p : ordenados) { 
                modeloLista.addElement(p);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + ex.getMessage());
        }
    }
    
    // MÉTODO PARA CARREGAR VENDAS
    private void carregarVendas() {
        try {
            if (vendaService == null) {
                return; 
            }
            modeloVendas.clear();
            List<Vendas> todasVendas = vendaService.listarTodas(); 
            for (Vendas v : todasVendas) {
                modeloVendas.addElement(v);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar vendas do banco de dados: " + ex.getMessage(), "Erro de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("deprecation")
	private void atualizarValorTotalEstoque() {
    try {
    	if(produtoService != null) {
    		BigDecimal total =  produtoService.calcularValorTotalEstoque();
    		
    		NumberFormat nf= NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
			String valorFormatado = nf.format(total);
    		
    		lblTotalEstoque.setText(valorFormatado);
    	}
    }catch (Exception ex) {
    	lblTotalEstoque.setText("R$ ERRO");
        System.err.println("Erro ao calcular valor total: " + ex.getMessage());
   }
}
    
    private void filtrarVendas() {
        String dataBusca = textBuscarVendas.getText().trim();

        if (vendaService == null) {
            modeloVendas.clear();
            return;
        }

        try {
            // Assume que este método foi implementado no VendaService
            List<Vendas> encontradas = vendaService.buscarVendasPorData(dataBusca);

            modeloVendas.clear();
            for (Vendas v : encontradas) {
                modeloVendas.addElement(v);
            }
        } catch (Exception ex) {
            // A lógica do VendaService deve capturar o erro de formatação de data,
            // mas é bom ter uma segurança aqui.
            JOptionPane.showMessageDialog(this, "Erro no filtro de vendas: " + ex.getMessage());
        }
    }


    // Filtra a lista pelo texto digitado
    private void filtrar() {
        String texto = textFieldBuscar.getText().trim().toLowerCase();

        if (produtoService == null) {
            modeloLista.clear();
            return;
        }

        try {
            List<Produto> encontrados = produtoService.produtos()
                .stream()
                .filter(p -> p.getNome() != null && p.getNome().toLowerCase().startsWith(texto))
                .collect(Collectors.toList());

            modeloLista.clear();
            for (Produto p : encontrados) modeloLista.addElement(p);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro no filtro: " + ex.getMessage());
        }
    }
}