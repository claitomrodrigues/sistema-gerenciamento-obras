package view;

import backup.BackupManager;
import config.SistemaConfig;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;

import view.abastecimento.AbastecimentoView;
import view.combustivel.CombustivelView;
import view.configuracoes.ConfiguracoesView;
import view.dashboard.DashboardView;
import view.empenho.EmpenhoView;
import view.equipamento.EquipamentoView;
import view.licitacao.LicitacaoView;
import view.manutencao.ManutencaoView;
import view.ocorrencia.OcorrenciaView;
import view.relatorios.RelatoriosView;

public class MainFrame extends JFrame {

    private static final Color AZUL_MENU = new Color(23, 37, 84);
    private static final Color AZUL_BOTAO = new Color(30, 58, 95);
    private static final Color AZUL_HOVER = new Color(42, 82, 135);
    private static final Color FUNDO = new Color(245, 247, 250);
    private static final Color TEXTO_ESCURO = new Color(33, 37, 41);
    private static final Color TEXTO_CINZA = new Color(100, 100, 100);

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle(SistemaConfig.getNomeSistema());
        setSize(1280, 720);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);
    }

    private void inicializarComponentes() {
        JPanel menuPanel = criarMenuLateral();
        add(menuPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(FUNDO);

        contentPanel.add(criarTelaInicial(), "HOME");
        contentPanel.add(new DashboardView(), "DASHBOARD");
        contentPanel.add(new EquipamentoView(), "EQUIPAMENTOS");
        contentPanel.add(new CombustivelView(), "COMBUSTIVEL");
        contentPanel.add(new AbastecimentoView(), "ABASTECIMENTO");
        contentPanel.add(new ManutencaoView(), "MANUTENCAO");
        contentPanel.add(new OcorrenciaView(), "OCORRENCIA");
        contentPanel.add(new LicitacaoView(), "LICITACAO");
        contentPanel.add(new EmpenhoView(), "EMPENHO");
        contentPanel.add(new RelatoriosView(), "RELATORIOS");
        contentPanel.add(new ConfiguracoesView(), "CONFIGURACOES");

        add(contentPanel, BorderLayout.CENTER);
        mostrarTela("HOME");
    }

    private JPanel criarMenuLateral() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(250, 0));
        menuPanel.setBackground(AZUL_MENU);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));

        JLabel logo = criarLogoMenu();
        menuPanel.add(logo);
        menuPanel.add(Box.createVerticalStrut(14));

        JLabel titulo = new JLabel("OBRAS E FROTA");
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuPanel.add(titulo);

        JLabel subtitulo = new JLabel("São Vicente do Sul - RS");
        subtitulo.setForeground(new Color(200, 210, 235));
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuPanel.add(Box.createVerticalStrut(4));
        menuPanel.add(subtitulo);
        menuPanel.add(Box.createVerticalStrut(24));

        JButton btnHome = criarBotao("Início");
        JButton btnDashboard = criarBotao("Dashboard");
        JButton btnEquipamentos = criarBotao("Equipamentos");
        JButton btnCombustivel = criarBotao("Combustíveis");
        JButton btnAbastecimento = criarBotao("Abastecimentos");
        JButton btnManutencao = criarBotao("Manutenções");
        JButton btnOcorrencia = criarBotao("Ocorrências");
        JButton btnLicitacao = criarBotao("Licitações");
        JButton btnEmpenho = criarBotao("Empenhos");
        JButton btnRelatorios = criarBotao("Relatórios");
        JButton btnConfiguracoes = criarBotao("Configurações");
        JButton btnBackup = criarBotao("Backup");
        JButton btnSair = criarBotao("Sair");

        adicionarBotao(menuPanel, btnHome);
        adicionarBotao(menuPanel, btnDashboard);
        adicionarSeparador(menuPanel);
        adicionarBotao(menuPanel, btnEquipamentos);
        adicionarBotao(menuPanel, btnCombustivel);
        adicionarBotao(menuPanel, btnAbastecimento);
        adicionarBotao(menuPanel, btnManutencao);
        adicionarBotao(menuPanel, btnOcorrencia);
        adicionarSeparador(menuPanel);
        adicionarBotao(menuPanel, btnLicitacao);
        adicionarBotao(menuPanel, btnEmpenho);
        adicionarSeparador(menuPanel);
        adicionarBotao(menuPanel, btnRelatorios);
        adicionarBotao(menuPanel, btnConfiguracoes);
        adicionarBotao(menuPanel, btnBackup);
        menuPanel.add(Box.createVerticalGlue());
        adicionarBotao(menuPanel, btnSair);

        btnHome.addActionListener(e -> mostrarTela("HOME"));
        btnDashboard.addActionListener(e -> mostrarTela("DASHBOARD"));
        btnEquipamentos.addActionListener(e -> mostrarTela("EQUIPAMENTOS"));
        btnCombustivel.addActionListener(e -> mostrarTela("COMBUSTIVEL"));
        btnAbastecimento.addActionListener(e -> mostrarTela("ABASTECIMENTO"));
        btnManutencao.addActionListener(e -> mostrarTela("MANUTENCAO"));
        btnOcorrencia.addActionListener(e -> mostrarTela("OCORRENCIA"));
        btnLicitacao.addActionListener(e -> mostrarTela("LICITACAO"));
        btnEmpenho.addActionListener(e -> mostrarTela("EMPENHO"));
        btnRelatorios.addActionListener(e -> mostrarTela("RELATORIOS"));
        btnConfiguracoes.addActionListener(e -> mostrarTela("CONFIGURACOES"));
        btnBackup.addActionListener(e -> gerarBackup());
        btnSair.addActionListener(e -> dispose());

        return menuPanel;
    }

    private JLabel criarLogoMenu() {
        JLabel logo = new JLabel();
        logo.setAlignmentX(CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/saovicente.png"));
            Image img = icon.getImage().getScaledInstance(86, 86, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/img/saovicente.png"));
                Image img = icon.getImage().getScaledInstance(86, 86, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                logo.setText("LOGO");
                logo.setForeground(Color.WHITE);
                logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
            }
        }
        return logo;
    }

    private void mostrarTela(String nomeTela) {
        cardLayout.show(contentPanel, nomeTela);
    }

    private void gerarBackup() {
        try {
            java.io.File arquivo = BackupManager.gerarBackup(
                    SistemaConfig.getCaminhoBanco(),
                    SistemaConfig.getPastaBackup()
            );
            JOptionPane.showMessageDialog(this, "Backup gerado com sucesso:\n" + arquivo.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao gerar backup", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarBotao(JPanel menuPanel, JButton botao) {
        menuPanel.add(botao);
        menuPanel.add(Box.createVerticalStrut(8));
    }

    private void adicionarSeparador(JPanel menuPanel) {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(205, 1));
        sep.setForeground(new Color(70, 90, 140));
        sep.setBackground(new Color(70, 90, 140));
        menuPanel.add(Box.createVerticalStrut(6));
        menuPanel.add(sep);
        menuPanel.add(Box.createVerticalStrut(12));
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setMaximumSize(new Dimension(205, 40));
        botao.setPreferredSize(new Dimension(205, 40));
        botao.setAlignmentX(CENTER_ALIGNMENT);
        botao.setBackground(AZUL_BOTAO);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                botao.setBackground(AZUL_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                botao.setBackground(AZUL_BOTAO);
            }
        });

        return botao;
    }

    private JPanel criarTelaInicial() {
        JPanel painel = new JPanel(new BorderLayout()) {
            private Image imagem;
            {
                try {
                    imagem = new ImageIcon(getClass().getResource("/img/saovicente.png")).getImage();
                } catch (Exception e) {
                    try {
                        imagem = new ImageIcon(getClass().getResource("/img/saovicente.png")).getImage();
                    } catch (Exception ex) {
                        imagem = null;
                    }
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagem == null) return;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.07f));

                int tamanho = Math.min(720, Math.min(getWidth(), getHeight()) - 80);
                if (tamanho < 300) tamanho = 300;
                int x = (getWidth() - tamanho) / 2;
                int y = (getHeight() - tamanho) / 2;

                g2.drawImage(imagem, x, y, tamanho, tamanho, this);
                g2.dispose();
            }
        };
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(34, 42, 28, 42));

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        JLabel prefeitura = labelCentro("PREFEITURA MUNICIPAL DE SÃO VICENTE DO SUL", 18, Font.BOLD, new Color(23, 37, 84));
        JLabel secretaria = labelCentro("SECRETARIA MUNICIPAL DE OBRAS", 16, Font.BOLD, new Color(43, 62, 110));
        JLabel sistema = labelCentro("Sistema de Gestão de Obras e Frota", 36, Font.BOLD, TEXTO_ESCURO);
        JLabel frase = labelCentro("Planejamento e organização para uma gestão pública eficiente.", 15, Font.PLAIN, TEXTO_CINZA);

        JLabel saudacao = labelCentro("", 24, Font.BOLD, new Color(23, 37, 84));
        JLabel data = labelCentro("", 17, Font.PLAIN, TEXTO_CINZA);
        JLabel hora = labelCentro("", 42, Font.BOLD, new Color(30, 58, 95));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        statusPanel.setOpaque(false);
        statusPanel.add(statusBadge("Município", "São Vicente do Sul - RS"));
        statusPanel.add(statusBadge("Versão", "1.0.0"));

        conteudo.add(Box.createVerticalGlue());
        conteudo.add(prefeitura);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(secretaria);
        conteudo.add(Box.createVerticalStrut(42));
        conteudo.add(sistema);
        conteudo.add(Box.createVerticalStrut(12));
        conteudo.add(frase);
        conteudo.add(Box.createVerticalStrut(42));
        conteudo.add(saudacao);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(data);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(hora);
        conteudo.add(Box.createVerticalStrut(36));
        conteudo.add(statusPanel);
        conteudo.add(Box.createVerticalGlue());

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);

        JLabel esquerda = new JLabel("Sistema de Gestão da Secretaria Municipal de Obras");
        esquerda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        esquerda.setForeground(new Color(120, 120, 120));

        JLabel direita = new JLabel("Desenvolvido por Claitom Rodrigues");
        direita.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        direita.setForeground(new Color(120, 120, 120));
        direita.setHorizontalAlignment(SwingConstants.RIGHT);

        rodape.add(esquerda, BorderLayout.WEST);
        rodape.add(direita, BorderLayout.EAST);

        painel.add(conteudo, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);

        Timer timer = new Timer(1000, e -> atualizarDataHora(saudacao, data, hora));
        timer.start();
        atualizarDataHora(saudacao, data, hora);

        return painel;
    }

    private JLabel labelCentro(String texto, int tamanho, int estilo, Color cor) {
        JLabel label = new JLabel(texto);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", estilo, tamanho));
        label.setForeground(cor);
        return label;
    }

    private JPanel statusBadge(String titulo, String valor) {
        JPanel badge = new JPanel();
        badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
        badge.setBackground(new Color(248, 250, 252));
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lTitulo.setForeground(new Color(80, 90, 110));

        JLabel lValor = new JLabel(valor);
        lValor.setAlignmentX(CENTER_ALIGNMENT);
        lValor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lValor.setForeground(new Color(110, 110, 110));

        badge.add(lTitulo);
        badge.add(Box.createVerticalStrut(4));
        badge.add(lValor);
        return badge;
    }

    private void atualizarDataHora(JLabel saudacao, JLabel data, JLabel hora) {
        Locale brasil = new Locale("pt", "BR");
        LocalTime agora = LocalTime.now();
        LocalDate hoje = LocalDate.now();

        int h = agora.getHour();
        String textoSaudacao;
        if (h < 12) {
            textoSaudacao = "Bom dia, seja bem-vindo!";
        } else if (h < 18) {
            textoSaudacao = "Boa tarde, seja bem-vindo!";
        } else {
            textoSaudacao = "Boa noite, seja bem-vindo!";
        }

        saudacao.setText(textoSaudacao);
        data.setText(hoje.format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", brasil)));
        hora.setText(agora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
