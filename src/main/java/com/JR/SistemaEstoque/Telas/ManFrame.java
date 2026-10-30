package com.JR.SistemaEstoque.Telas;

import java.awt.Color;

import java.awt.Desktop;

import java.awt.Font;

import java.math.BigDecimal;

import java.net.URI;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.util.List;

import javax.swing.DefaultListModel;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTextField;

import javax.swing.JTextArea;

import javax.swing.ListSelectionModel;

import javax.swing.event.DocumentEvent;

import javax.swing.event.DocumentListener;

import org.springframework.stereotype.Component;

import com.JR.SistemaEstoque.Model.ItemOrcamento;

import com.JR.SistemaEstoque.Model.Produto;

import com.JR.SistemaEstoque.Model.Vendas;

import com.JR.SistemaEstoque.Service.ProdutoService;

import com.JR.SistemaEstoque.Service.VendaService;

import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;

import java.awt.Toolkit;

import java.awt.BorderLayout;

@Component

public class ManFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final VendaService vendaService;

    private final ProdutoService produtoService;

    private JTextField Campobuscar;

    private JTextField nome;

    private JTextField quantidade;

    private JTextField preço;

    private DefaultListModel<Produto> meuModelo;  // modelo da lista

    private JList<Produto> jListProdutos;

    private DefaultListModel<ItemOrcamento> modeloOrcamento;

    private JList<ItemOrcamento> jListOrcamento;

      @SuppressWarnings("unused")

    private JLabel lblTotal;

      public ManFrame(ProdutoService produtoService, VendaService vendaService) {

      	setForeground(new Color(64, 171, 52));

    		this.produtoService = produtoService;

    		this.vendaService = vendaService;

    		meuModelo = new DefaultListModel<>();

    		setBackground(new Color(255, 255, 255));

    		getContentPane().setBackground(new Color(0, 0, 0));

            setSize(800, 600);

            setResizable(false); // botão de maximizar desabilitado

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            setLocationRelativeTo(null); //centralização

            getContentPane().setLayout(null);

            JPanel painelBuscar = new JPanel();

            painelBuscar.setBackground(new Color(0, 128, 64));

            painelBuscar.setBounds(13, 57, 369, 208);

            getContentPane().add(painelBuscar);

            painelBuscar.setLayout(null);

            JLabel lblNewLabel = new JLabel("Buscar");

            lblNewLabel.setFont(new Font("Arial Black", Font.PLAIN, 10));

            lblNewLabel.setBounds(10, 0, 58, 26);

            painelBuscar.add(lblNewLabel);

            Campobuscar = new JTextField();

            Campobuscar.setFont(new Font("Arial Black", Font.PLAIN, 10));

            Campobuscar.setBounds(10, 23, 336, 19);

            painelBuscar.add(Campobuscar);

            Campobuscar.setColumns(10);

         // Listener para busca dinâmica

            Campobuscar.getDocument().addDocumentListener(new DocumentListener() {

                private void updateSuggestions() {

                    String texto = Campobuscar.getText().trim().toLowerCase();

                    meuModelo.clear();

                    if (!texto.isEmpty()) {

                        List<Produto> encontrados = produtoService.buscarPorName(texto);

                        for (Produto p : encontrados) {

                            meuModelo.addElement(p);

                        }

                    }

                }

                @Override

                public void insertUpdate(DocumentEvent e) { updateSuggestions(); }

                @Override

                public void removeUpdate(DocumentEvent e) { updateSuggestions(); }

                @Override

                public void changedUpdate(DocumentEvent e) { updateSuggestions(); }

            });

            JButton btnADDItem = new JButton("Adicionar");

            btnADDItem.setFont(new Font("Arial Black", Font.PLAIN, 10));

            btnADDItem.addActionListener(e -> {

                Produto selecionado = jListProdutos.getSelectedValue();

                if (selecionado != null) {

                    String qtdStr = JOptionPane.showInputDialog(this, "Quantidade do produto:");

                    try {

                        int qtd = Integer.parseInt(qtdStr);

                        if (qtd <= 0) {

                            JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que 0!");

                            return;

                        }

                        ItemOrcamento item = new ItemOrcamento(selecionado, qtd);

                        modeloOrcamento.addElement(item);

                        atualizarTotal();

                    } catch (NumberFormatException ex) {

                        JOptionPane.showMessageDialog(this, "Digite um número válido para a quantidade.");

                    }

                } else {

                    JOptionPane.showMessageDialog(this, "Selecione um produto para adicionar ao orçamento!");

                }

            });

            btnADDItem.setBounds(248, 177, 98, 21);

            painelBuscar.add(btnADDItem);

            		JScrollPane scrollListaProdutos = new JScrollPane();

            		scrollListaProdutos.setBounds(10, 46, 336, 121);

            		painelBuscar.add(scrollListaProdutos);

            		jListProdutos = new JList<>(meuModelo);

            		jListProdutos.setFont(new Font("Arial Black", Font.PLAIN, 10));

            		scrollListaProdutos.setViewportView(jListProdutos);

            		jListProdutos.setVisibleRowCount(5); // mostra 5 itens

            		jListProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            				jListProdutos.setCellRenderer((listOrçamento, value, index, isSelected, cellHasFocus) -> {

            					 String texto = String.format("%s | Quantidade: %d | Preço: R$ %.2f",

            					            value.getNome(),

            					            value.getQuantidade(),

            					            value.getPreco());

            				    JLabel label = new JLabel(texto);

            				    if (isSelected) {

            				        label.setBackground(Color.LIGHT_GRAY);

            				        label.setOpaque(true);

            				    }

            				    return label;

            				});

            //orçamento

            JPanel panel_1 = new JPanel();

            panel_1.setBackground(new Color(0, 128, 64));

            panel_1.setBounds(392, 57, 384, 208);

            getContentPane().add(panel_1);

            panel_1.setLayout(null);

            JLabel lblNewLabel_1 = new JLabel("Orçamento");

            lblNewLabel_1.setFont(new Font("Arial Black", Font.PLAIN, 10));

            lblNewLabel_1.setBounds(10, 10, 69, 13);

            panel_1.add(lblNewLabel_1);

            modeloOrcamento = new DefaultListModel<>();

            jListOrcamento = new JList<>(modeloOrcamento);

            jListOrcamento.setFont(new Font("Arial Black", Font.PLAIN, 10));

            JScrollPane scrollPane = new JScrollPane(jListOrcamento);

            scrollPane.setBounds(10, 33, 364, 134);

            panel_1.add(scrollPane);

            // Render do orçamento igual o de produtos

            jListOrcamento.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {

                // value é do tipo ItemOrcamento

                String texto = String.format("%s | Qtd: %d | Unit: R$ %.2f | Total: R$ %.2f",

                        value.getProduto().getNome(),       // pega o nome do produto

                        value.getQuantidade(),              // quantidade escolhida

                        value.getPrecoUnitario(),           // preço unitário

                        value.getValortotal());             // total = qtd * unit

                JLabel label = new JLabel(texto);

                if (isSelected) {

                    label.setOpaque(true);

                    label.setBackground(list.getSelectionBackground());

                    label.setForeground(list.getSelectionForeground());

                }

                return label;

            });

            JButton btnVenda = new JButton("Venda");

            btnVenda.setFont(new Font("Arial Black", Font.PLAIN, 10));

            btnVenda.setBounds(289, 177, 85, 21);

            panel_1.add(btnVenda);

            btnVenda.addActionListener(e -> {

                if (modeloOrcamento.isEmpty()) {

                    JOptionPane.showMessageDialog(this, "O orçamento está vazio!");

                    return;

                }

                int confirmacao = JOptionPane.showConfirmDialog(

                    this,

                    "Confirmar a venda dos " + modeloOrcamento.size() + " itens?",

                    "Confirmação de Venda",

                    JOptionPane.YES_NO_OPTION

                );

                if (confirmacao == JOptionPane.NO_OPTION) {

                    return;

                }

                try {

                    List<Produto> produtosParaAtualizar = new java.util.ArrayList<>();

                    BigDecimal totalVenda = BigDecimal.ZERO;

                    List<ItemOrcamento> itensVendidos = new java.util.ArrayList<>();

                    for (int i = 0; i < modeloOrcamento.size(); i++) {

                        ItemOrcamento item = modeloOrcamento.getElementAt(i);

                        Produto produtoOriginal = produtoService.findById(item.getProduto().getId());

                        if (produtoOriginal.getQuantidade() < item.getQuantidade()) {

                            JOptionPane.showMessageDialog(this, "Estoque insuficiente para: " + item.getProduto().getNome(),

                                                          "Erro de Estoque", JOptionPane.ERROR_MESSAGE);

                            return;

                        }

                        produtoOriginal.setQuantidade(produtoOriginal.getQuantidade() - item.getQuantidade());

                        produtosParaAtualizar.add(produtoOriginal);

                        totalVenda = totalVenda.add(item.getValortotal());

                        itensVendidos.add(item);

                    }

                    Vendas novaVenda = new Vendas();

                    novaVenda.setItens(itensVendidos);

                    novaVenda.setValorTotal(totalVenda);

                    vendaService.salvar(novaVenda);

                    for(Produto p : produtosParaAtualizar) {

                        produtoService.save(p);

                    }

                    modeloOrcamento.clear();

                    atualizarTotal();

                    int imprimir = JOptionPane.showConfirmDialog(

                        this,

                        "Venda concluída com sucesso! Deseja imprimir o cupom?",

                        "Opção de Impressão",

                        JOptionPane.YES_NO_OPTION

                    );

                    if (imprimir == JOptionPane.YES_OPTION) {

                        StringBuilder sb = new StringBuilder();

                        sb.append("* HS da Contrução *\n");

                        sb.append("CUPOM FISCAL - VENDA\n");
                        sb.append("CNPJ: 17.380.515.0001-67\n");
                        sb.append("Contato: Naldo (75) 99809-7951\n");
                        sb.append("Contato: juliana (75) 99920-1809\n");

                        sb.append("-----------------------------\n");

                        for (ItemOrcamento item : itensVendidos) {

                            sb.append(String.format("%-12s %2dx R$ %.2f\n  = R$ %.2f\n",

                                item.getProduto().getNome(),

                                item.getQuantidade(),

                                item.getPrecoUnitario(),

                                item.getValortotal()));

                        }

                        sb.append("-----------------------------\n");

                        sb.append(String.format("TOTAL DA VENDA: R$ %.2f\n", totalVenda));

                        sb.append("-----------------------------\n");

                        sb.append("Obrigado pela preferência!\n\n\n");

                        imprimirTexto(sb.toString());

                    }

                } catch (RuntimeException ex) {

                    JOptionPane.showMessageDialog(this, "Erro fatal ao processar a venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(this, "Erro desconhecido ao processar a venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);

                }

            });

            //Compartilha pelo whatsAPP

            JButton btnNewButtonOrcamento = new JButton("Orçamento");

            btnNewButtonOrcamento.setFont(new Font("Arial Black", Font.PLAIN, 10));

            btnNewButtonOrcamento.setBounds(135, 177, 106, 21);

            panel_1.add(btnNewButtonOrcamento);

            JButton btnOrcamento = new JButton("Orçamento");

            btnOrcamento.setFont(new Font("Arial Black", Font.PLAIN, 10));

            btnOrcamento.setBounds(135, 177, 106, 21);

            panel_1.add(btnOrcamento);

            btnNewButtonOrcamento.addActionListener(e -> {

                if (modeloOrcamento.isEmpty()) {

                    JOptionPane.showMessageDialog(this, "O orçamento está vazio!");

                    return;

                }

                // Opções

                String[] opcoes = {"Imprimir", "Compartilhar"};

                int escolha = JOptionPane.showOptionDialog(

                    this,

                    "Escolha a ação para o orçamento:",

                    "Orçamento",

                    JOptionPane.DEFAULT_OPTION,

                    JOptionPane.QUESTION_MESSAGE,

                    null,

                    opcoes,

                    opcoes[0]

                );

                // texto do orçamento

                StringBuilder sb = new StringBuilder();

                sb.append("* HS da Contrução *\n");

                sb.append("CNPJ: 17.380.515.0001-67\n");

                sb.append("Contato: Naldo (75) 99809-7951\n");

                sb.append("Contato: juliana (75) 99920-1809\n");

                sb.append("Orçamento Cliente\n");

                sb.append("-----------------------------\n");

                BigDecimal total = BigDecimal.ZERO;

                for (int i = 0; i < modeloOrcamento.size(); i++) {

                    ItemOrcamento item = modeloOrcamento.getElementAt(i);

                    BigDecimal valorItem = item.getValortotal();

                    if (valorItem == null) valorItem = BigDecimal.ZERO;

                    total = total.add(valorItem);

                    sb.append(String.format("%-12s %2dx R$ %.2f\n  = R$ %.2f\n",

                    item.getProduto().getNome(),

                    item.getQuantidade(),

                    item.getPrecoUnitario(),

                            valorItem));

                }

                sb.append("-----------------------------\n");

                sb.append(String.format("TOTAL: R$ %.2f\n", total));

                sb.append("-----------------------------\n");

                sb.append("Obrigado pela preferência!\n\n\n");

                try {

                    if (escolha == 0) {

                        // Imprimir

                        imprimirTexto(sb.toString());

                    } else if (escolha == 1) {

                        // Compartilhar via WhatsApp

                        String textoEncoded = URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8.toString());

                        String whatsappDesktopUrl = "whatsapp://send?text=" + textoEncoded;

                        try {

                            Desktop.getDesktop().browse(new URI(whatsappDesktopUrl));

                        } catch (Exception ex) {

                            String whatsappWebUrl = "https://web.whatsapp.com/send?text=" + textoEncoded;

                            Desktop.getDesktop().browse(new URI(whatsappWebUrl));

                        }

                    }

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(this, "Erro ao processar orçamento: " + ex.getMessage());

                }

            });

            @SuppressWarnings("rawtypes")

    		JList listOrcamento = new JList();

            listOrcamento.setBounds(10, 33, 364, 134);

            panel_1.add(listOrcamento);

            JButton btnRemover = new JButton("Remover");

            btnRemover.setFont(new Font("Arial Black", Font.PLAIN, 10));

            btnRemover.setBounds(10, 177, 85, 21);

            panel_1.add(btnRemover);

            btnRemover.addActionListener(e -> {

                ItemOrcamento selecionado = jListOrcamento.getSelectedValue();

                if (selecionado != null) {

                    modeloOrcamento.removeElement(selecionado);

                    atualizarTotal(); // atualiza o valor total depois de remover

                } else {

                    JOptionPane.showMessageDialog(this, "Selecione um item para remover do orçamento.");

                }

            });

            JScrollPane scrollPane1 = new JScrollPane();

            scrollPane1.setBounds(10, 33, 364, 134);

            panel_1.add(scrollPane1);

            JPanel panelCadastro = new JPanel();

            panelCadastro.setBackground(new Color(0, 128, 64));

            panelCadastro.setBounds(13, 275, 763, 278);

            getContentPane().add(panelCadastro);

            panelCadastro.setLayout(null);

            //Painel de Cadastro

            JLabel lblNewLabel_2 = new JLabel("Cadastrar produto");

            lblNewLabel_2.setFont(new Font("Arial Black", Font.PLAIN, 20));

            lblNewLabel_2.setBounds(271, 24, 234, 33);

            panelCadastro.add(lblNewLabel_2);

            JLabel lblNewLabel_3 = new JLabel("Nome");

            lblNewLabel_3.setFont(new Font("Arial Black", Font.PLAIN, 10));

            lblNewLabel_3.setBounds(117, 75, 73, 19);

            panelCadastro.add(lblNewLabel_3);

            JLabel lblNewLabel_4 = new JLabel("Quantidade");

            lblNewLabel_4.setFont(new Font("Arial Black", Font.PLAIN, 10));

            lblNewLabel_4.setBounds(115, 130, 85, 13);

            panelCadastro.add(lblNewLabel_4);

            JLabel lblNewLabel_5 = new JLabel("Preço");

            lblNewLabel_5.setFont(new Font("Arial Black", Font.PLAIN, 10));

            lblNewLabel_5.setBounds(117, 181, 45, 13);

            panelCadastro.add(lblNewLabel_5);

            nome = new JTextField();

            nome.setFont(new Font("Arial Black", Font.PLAIN, 10));

            nome.setBounds(117, 101, 482, 19);

            panelCadastro.add(nome);

            nome.setColumns(10);

            quantidade = new JTextField();

            quantidade.setFont(new Font("Arial Black", Font.PLAIN, 10));

            quantidade.setBounds(117, 152, 482, 19);

            panelCadastro.add(quantidade);

            quantidade.setColumns(10);

            preço = new JTextField();

            preço.setFont(new Font("Arial Black", Font.PLAIN, 10));

            preço.setBounds(117, 198, 482, 19);

            panelCadastro.add(preço);

            preço.setColumns(10);

            JButton btnsave = new JButton("save");

            btnsave.setFont(new Font("Arial Black", Font.PLAIN, 10));

            btnsave.setBounds(514, 230, 85, 21);

            panelCadastro.add(btnsave);

            JButton BtnDados = new JButton("Geral");

            BtnDados.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    // Instancia a tela de estoque

                    TelaEstoque estoque = new TelaEstoque(produtoService, vendaService);

                    // Torna a tela visível

                    estoque.setVisible(true);

                    // Opcional: fecha a tela atual

                    // ((JFrame) SwingUtilities.getWindowAncestor(BtnEstoque)).dispose();

                }

            });

            BtnDados.setBounds(673, 247, 80, 21);

            panelCadastro.add(BtnDados);

            btnsave.addActionListener(e -> salvarProduto());

            JPanel panel_4 = new JPanel();

            panel_4.setToolTipText("HS da construção");

            panel_4.setBackground(new Color(0, 128, 64));

            panel_4.setBounds(13, 10, 763, 37);

            getContentPane().add(panel_4);

            panel_4.setLayout(new BorderLayout(0, 0));

            JLabel lblNewLabel_6 = new JLabel("                                           HS da Contrução");

            lblNewLabel_6.setFont(new Font("Arial Black", Font.PLAIN, 20));

            panel_4.add(lblNewLabel_6, BorderLayout.CENTER);

        }

    	 private void atualizarTotal() {

    	}

    	private void salvarProduto() {

    	        try {

    	            String nomeProduto = nome.getText().trim();

    	            int quantidadeProduto = Integer.parseInt(quantidade.getText().trim());

    	            BigDecimal valor = new BigDecimal(preço.getText().trim());

    	            Produto produto = new Produto();

    	            produto.setNome(nomeProduto);

    	            produto.setPreco(valor);

    	            produto.setQuantidade(quantidadeProduto);

    	            produtoService.save(produto);

    	            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!");

    	            nome.setText("");

    	            quantidade.setText("");

    	            preço.setText("");

    	        } catch (NumberFormatException ex) {

    	            JOptionPane.showMessageDialog(this, "Quantidade e preço devem ser numéricos.");

    	        } catch (Exception ex) {

    	            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + ex.getMessage());

    	        }

    	    }

    	public DefaultListModel<Produto> getListModel() {

    		return meuModelo;

    	}

    	public void setListModel(DefaultListModel<Produto> listModel) {

    		this.meuModelo = listModel;

    	}

    /**
     * Imprime texto usando a impressora selecionada na janela de impressão do Windows.
     * Não cria PDF nem depende da porta LPT1.
     */
    private void imprimirTexto(String texto) {
        JTextArea areaImpressao = new JTextArea(texto);
        areaImpressao.setFont(new Font("Monospaced", Font.PLAIN, 10));
        areaImpressao.setEditable(false);
        areaImpressao.setLineWrap(false);

        try {
            boolean impresso = areaImpressao.print(
                null,   // sem cabeçalho
                null,   // sem rodapé
                true,   // exibe a janela de impressão
                null,   // impressora padrão; usuário pode escolher outra
                null,   // opções padrão
                true    // impressão interativa
            );

            if (impresso) {
                JOptionPane.showMessageDialog(this, "Impressão enviada para a impressora.");
            }
        } catch (java.awt.print.PrinterException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao imprimir: " + ex.getMessage(),
                "Erro de impressão",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
