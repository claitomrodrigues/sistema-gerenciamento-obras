package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.abastecimento.AbastecimentoView;
import view.combustivel.CombustivelView;
import view.empenho.EmpenhoView;
import view.equipamento.EquipamentoView;
import view.licitacao.LicitacaoView;
import view.manutencao.ManutencaoView;
import view.ocorrencia.OcorrenciaView;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {

        setTitle("Sistema de Gestão de Frota");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

    }

    private void inicializarComponentes() {

        // ================= MENU =================

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(230, 0));
        menuPanel.setBackground(new Color(32, 50, 81));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.add(Box.createVerticalStrut(20));

        JLabel logo = new JLabel();
        logo.setAlignmentX(CENTER_ALIGNMENT);

        ImageIcon icon = new ImageIcon(getClass().getResource("/img/saovicente.png"));
        Image img = icon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(img));

        menuPanel.add(logo);

        menuPanel.add(Box.createVerticalStrut(15));

        JLabel titulo = new JLabel("GESTÃO DE FROTA");
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        menuPanel.add(titulo);

        menuPanel.add(Box.createVerticalStrut(30));

        JButton btnHome = criarBotao("Início");
        JButton btnEquipamentos = criarBotao("Equipamentos");
        JButton btnCombustivel = criarBotao("Combustíveis");
        JButton btnAbastecimento = criarBotao("Abastecimentos");
        JButton btnManutencao = criarBotao("Manutenções");
        JButton btnOcorrencia = criarBotao("Ocorrências");
        JButton btnLicitacao = criarBotao("Licitações");
        JButton btnEmpenho = criarBotao("Empenhos");

        menuPanel.add(btnHome);
        menuPanel.add(Box.createVerticalStrut(10));

        menuPanel.add(btnEquipamentos);
        menuPanel.add(Box.createVerticalStrut(10));

        menuPanel.add(btnCombustivel);
        menuPanel.add(Box.createVerticalStrut(10));

        menuPanel.add(btnAbastecimento);
        menuPanel.add(Box.createVerticalStrut(10));

        menuPanel.add(btnManutencao);
        menuPanel.add(Box.createVerticalStrut(10));

        menuPanel.add(btnOcorrencia);
        menuPanel.add(Box.createVerticalStrut(10));

        menuPanel.add(btnLicitacao);
        menuPanel.add(Box.createVerticalStrut(10));

        menuPanel.add(btnEmpenho);

        add(menuPanel, BorderLayout.WEST);

        // ================= CARDLAYOUT =================

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel homePanel = criarTelaInicial();

        EquipamentoView equipamentoView = new EquipamentoView();
        CombustivelView combustivelView = new CombustivelView();
        AbastecimentoView abastecimentoView = new AbastecimentoView();
        ManutencaoView manutencaoView = new ManutencaoView();
        OcorrenciaView ocorrenciaView = new OcorrenciaView();
        LicitacaoView licitacaoView = new LicitacaoView();
        EmpenhoView empenhoView = new EmpenhoView();

        contentPanel.add(homePanel, "HOME");
        contentPanel.add(equipamentoView, "EQUIPAMENTOS");
        contentPanel.add(combustivelView, "COMBUSTIVEL");
        contentPanel.add(abastecimentoView, "ABASTECIMENTO");
        contentPanel.add(manutencaoView, "MANUTENCAO");
        contentPanel.add(ocorrenciaView, "OCORRENCIA");
        contentPanel.add(licitacaoView, "LICITACAO");
        contentPanel.add(empenhoView, "EMPENHO");

        add(contentPanel, BorderLayout.CENTER);

        // ================= EVENTOS =================

        btnHome.addActionListener(e ->
                cardLayout.show(contentPanel, "HOME"));

        btnEquipamentos.addActionListener(e ->
                cardLayout.show(contentPanel, "EQUIPAMENTOS"));

        btnCombustivel.addActionListener(e ->
                cardLayout.show(contentPanel, "COMBUSTIVEL"));

        btnAbastecimento.addActionListener(e ->
                cardLayout.show(contentPanel, "ABASTECIMENTO"));

        btnManutencao.addActionListener(e ->
                cardLayout.show(contentPanel, "MANUTENCAO"));

        btnOcorrencia.addActionListener(e ->
                cardLayout.show(contentPanel, "OCORRENCIA"));

        btnLicitacao.addActionListener(e ->
                cardLayout.show(contentPanel, "LICITACAO"));

        btnEmpenho.addActionListener(e ->
                cardLayout.show(contentPanel, "EMPENHO"));

    }

    private JButton criarBotao(String texto) {

        JButton botao = new JButton(texto);

        botao.setMaximumSize(new Dimension(190, 45));
        botao.setPreferredSize(new Dimension(190,45));
        botao.setAlignmentX(CENTER_ALIGNMENT);

        botao.setBackground(new Color(52, 73, 94));
        botao.setForeground(Color.WHITE);

        botao.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        botao.setFocusPainted(false);

        botao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return botao;
    }

    private JPanel criarTelaInicial() {

        JPanel painel = new JPanel(new BorderLayout()) {

            private final Image imagem =
                    new ImageIcon(getClass().getResource("/img/saovicente.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER, 0.12f));

                int largura = 500;
                int altura = 500;

                int x = (getWidth() - largura) / 2;
                int y = (getHeight() - altura) / 2;

                g2.drawImage(imagem, x, y, largura, altura, this);

            }

        };

        painel.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Sistema de Gestão de Frota");

        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));

        titulo.setForeground(new Color(40,40,40));

        painel.add(titulo, BorderLayout.CENTER);

        return painel;

    }

}