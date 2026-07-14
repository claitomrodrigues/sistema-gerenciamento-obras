package view;

import backup.BackupManager;
import config.SistemaConfig;
import auditoria.AuditoriaService;
import ui.components.Notifications;
import security.PermissionManager;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.io.File;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.*;

import view.abastecimento.AbastecimentoView;
import view.combustivel.CombustivelView;
import view.configuracoes.ConfiguracoesView;
import view.dashboard.DashboardView;
import view.home.HomeView;
import view.empenho.EmpenhoView;
import view.equipamento.EquipamentoView;
import view.licitacao.LicitacaoView;
import view.manutencao.ManutencaoView;
import view.ocorrencia.OcorrenciaView;
import view.ordemservico.OrdemServicoView;
import view.relatorios.RelatoriosView;
import view.auditoria.AuditoriaView;
import view.usuario.UsuariosView;
import view.sobre.SobreView;

public class MainFrame extends JFrame {

    private static final Color AZUL_MENU = new Color(15, 23, 42);
    private static final Color AZUL_BOTAO = new Color(30, 41, 59);
    private static final Color AZUL_HOVER = new Color(51, 65, 85);
    private static final Color FUNDO = new Color(248, 250, 252);
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
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { sairComBackupAutomatico(); }
        });
        setLayout(new BorderLayout());
        getContentPane().setBackground(FUNDO);
    }

    private void inicializarComponentes() {
        JPanel menuPanel = criarMenuLateral();
        add(menuPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(FUNDO);

        adicionarTela(new HomeView(), "HOME");
        adicionarTela(new DashboardView(), "DASHBOARD");
        adicionarTela(new EquipamentoView(), "EQUIPAMENTOS");
        adicionarTela(new CombustivelView(), "COMBUSTIVEL");
        adicionarTela(new AbastecimentoView(), "ABASTECIMENTO");
        adicionarTela(new ManutencaoView(), "MANUTENCAO");
        adicionarTela(new OcorrenciaView(), "OCORRENCIA");
        adicionarTela(new OrdemServicoView(), "ORDEM_SERVICO");
        adicionarTela(new LicitacaoView(), "LICITACAO");
        adicionarTela(new EmpenhoView(), "EMPENHO");
        adicionarTela(new RelatoriosView(), "RELATORIOS");
        adicionarTela(new ConfiguracoesView(), "CONFIGURACOES");
        adicionarTela(new AuditoriaView(), "AUDITORIA");
        adicionarTela(new UsuariosView(), "USUARIOS");
        adicionarTela(new SobreView(), "SOBRE");

        add(contentPanel, BorderLayout.CENTER);
        mostrarTela("HOME");
    }

    private void adicionarTela(JComponent tela, String nome) {
        JScrollPane scroll = StyleUtils.screenScroll(tela);
        scroll.getVerticalScrollBar().setValue(0);
        contentPanel.add(scroll, nome);
    }

    private JPanel criarMenuLateral() {
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(260, 0));
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
        JButton btnDashboard = criarBotao("Painel Executivo");
        JButton btnEquipamentos = criarBotao("Equipamentos");
        JButton btnCombustivel = criarBotao("Combustíveis");
        JButton btnAbastecimento = criarBotao("Abastecimentos");
        JButton btnManutencao = criarBotao("Manutenções");
        JButton btnOcorrencia = criarBotao("Ocorrências");
        JButton btnOrdemServico = criarBotao("Ordens de Serviço");
        JButton btnLicitacao = criarBotao("Licitações");
        JButton btnEmpenho = criarBotao("Empenhos");
        JButton btnRelatorios = criarBotao("Relatórios");
        JButton btnConfiguracoes = criarBotao("Configurações");
        JButton btnAuditoria = criarBotao("Auditoria");
        JButton btnUsuarios = criarBotao("Usuários");
        JButton btnBackup = criarBotao("Backup");
        JButton btnRestaurar = criarBotao("Restaurar Backup");
        JButton btnSobre = criarBotao("Sobre");
        JButton btnSair = criarBotao("Sair");

        adicionarBotao(menuPanel, btnHome);
        adicionarBotao(menuPanel, btnDashboard);
        adicionarSeparador(menuPanel);
        adicionarBotao(menuPanel, btnEquipamentos);
        adicionarBotao(menuPanel, btnCombustivel);
        adicionarBotao(menuPanel, btnAbastecimento);
        adicionarBotao(menuPanel, btnManutencao);
        adicionarBotao(menuPanel, btnOcorrencia);
        adicionarBotao(menuPanel, btnOrdemServico);
        adicionarSeparador(menuPanel);
        adicionarBotao(menuPanel, btnLicitacao);
        adicionarBotao(menuPanel, btnEmpenho);
        adicionarSeparador(menuPanel);
        adicionarBotao(menuPanel, btnRelatorios);
        adicionarBotao(menuPanel, btnConfiguracoes);
        adicionarBotao(menuPanel, btnAuditoria);
        adicionarBotao(menuPanel, btnUsuarios);
        adicionarBotao(menuPanel, btnBackup);
        adicionarBotao(menuPanel, btnRestaurar);
        adicionarBotao(menuPanel, btnSobre);
        menuPanel.add(Box.createVerticalGlue());
        adicionarBotao(menuPanel, btnSair);

        PermissionManager.aplicar(btnConfiguracoes, PermissionManager.podeConfiguracoes(), "Seu perfil não permite acessar configurações.");
        PermissionManager.aplicar(btnAuditoria, PermissionManager.podeAuditoria(), "Seu perfil não permite acessar auditoria.");
        PermissionManager.aplicar(btnUsuarios, PermissionManager.podeUsuarios(), "Seu perfil não permite gerenciar usuários.");
        PermissionManager.aplicar(btnBackup, PermissionManager.podeBackup(), "Seu perfil não permite gerar backup.");
        PermissionManager.aplicar(btnRestaurar, PermissionManager.podeBackup(), "Seu perfil não permite restaurar backup.");

        btnHome.addActionListener(e -> mostrarTela("HOME"));
        btnDashboard.addActionListener(e -> mostrarTela("DASHBOARD"));
        btnEquipamentos.addActionListener(e -> mostrarTela("EQUIPAMENTOS"));
        btnCombustivel.addActionListener(e -> mostrarTela("COMBUSTIVEL"));
        btnAbastecimento.addActionListener(e -> mostrarTela("ABASTECIMENTO"));
        btnManutencao.addActionListener(e -> mostrarTela("MANUTENCAO"));
        btnOcorrencia.addActionListener(e -> mostrarTela("OCORRENCIA"));
        btnOrdemServico.addActionListener(e -> mostrarTela("ORDEM_SERVICO"));
        btnLicitacao.addActionListener(e -> mostrarTela("LICITACAO"));
        btnEmpenho.addActionListener(e -> mostrarTela("EMPENHO"));
        btnRelatorios.addActionListener(e -> mostrarTela("RELATORIOS"));
        btnConfiguracoes.addActionListener(e -> { if (PermissionManager.exigir(this, PermissionManager.podeConfiguracoes(), "Acesso negado às configurações.")) mostrarTela("CONFIGURACOES"); });
        btnAuditoria.addActionListener(e -> { if (PermissionManager.exigir(this, PermissionManager.podeAuditoria(), "Acesso negado à auditoria.")) mostrarTela("AUDITORIA"); });
        btnUsuarios.addActionListener(e -> { if (PermissionManager.exigir(this, PermissionManager.podeUsuarios(), "Acesso negado ao gerenciamento de usuários.")) mostrarTela("USUARIOS"); });
        btnBackup.addActionListener(e -> { if (PermissionManager.exigir(this, PermissionManager.podeBackup(), "Acesso negado ao backup.")) gerarBackup(); });
        btnRestaurar.addActionListener(e -> { if (PermissionManager.exigir(this, PermissionManager.podeBackup(), "Acesso negado à restauração de backup.")) restaurarBackup(); });
        btnSobre.addActionListener(e -> mostrarTela("SOBRE"));
        btnSair.addActionListener(e -> sairComBackupAutomatico());

        JPanel containerMenu = new JPanel(new BorderLayout());
        containerMenu.setPreferredSize(new Dimension(260, 0));
        containerMenu.setBackground(AZUL_MENU);

        JScrollPane scrollMenu = new JScrollPane(menuPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMenu.setBorder(null);
        scrollMenu.setBackground(AZUL_MENU);
        scrollMenu.getViewport().setBackground(AZUL_MENU);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(20);
        scrollMenu.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        containerMenu.add(scrollMenu, BorderLayout.CENTER);
        return containerMenu;
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
            AuditoriaService.registrar("BACKUP", "Sistema", arquivo.getAbsolutePath());
            Notifications.success(this, "Backup gerado com sucesso: " + arquivo.getName());
        } catch (Exception ex) {
            Notifications.error(this, ex.getMessage());
        }
    }


    private void restaurarBackup() {
        if (!util.FormUtils.confirmar(this, "Restaurar um backup vai substituir o banco atual. Feche outras janelas do sistema antes de continuar. Deseja prosseguir?")) return;
        JFileChooser chooser = new JFileChooser(SistemaConfig.getPastaBackup());
        chooser.setDialogTitle("Selecionar backup para restaurar");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            File arquivo = chooser.getSelectedFile();
            BackupManager.restaurarBackup(arquivo, SistemaConfig.getCaminhoBanco());
            AuditoriaService.registrar("RESTAURAR_BACKUP", "Sistema", arquivo.getAbsolutePath());
            Notifications.success(this, "Backup restaurado. Reinicie o sistema para recarregar todos os dados.");
        } catch (Exception ex) {
            Notifications.error(this, ex.getMessage());
        }
    }

    private void sairComBackupAutomatico() {
        try {
            File arquivo = BackupManager.gerarBackupAutomatico(SistemaConfig.getCaminhoBanco(), SistemaConfig.getPastaBackup());
            AuditoriaService.registrar("BACKUP_AUTOMATICO", "Sistema", arquivo.getAbsolutePath());
        } catch (Exception ignored) {
            // Backup automático não deve travar o encerramento.
        }
        dispose();
        System.exit(0);
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

        JLabel prefeitura = labelCentro("PREFEITURA MUNICIPAL DE SÃO VICENTE DO SUL", 18, Font.BOLD, new Color(15, 23, 42));
        JLabel secretaria = labelCentro("SECRETARIA MUNICIPAL DE OBRAS", 16, Font.BOLD, new Color(43, 62, 110));
        JLabel sistema = labelCentro("Sistema de Gestão de Obras e Frota", 36, Font.BOLD, TEXTO_ESCURO);
        JLabel frase = labelCentro("Planejamento e organização para uma gestão pública eficiente.", 15, Font.PLAIN, TEXTO_CINZA);

        JLabel data = labelCentro("", 17, Font.PLAIN, TEXTO_CINZA);
        JLabel hora = labelCentro("", 42, Font.BOLD, new Color(30, 41, 59));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        statusPanel.setOpaque(false);
        statusPanel.add(statusBadge("Município", "São Vicente do Sul - RS"));
        statusPanel.add(statusBadge("Versão", "2.0"));

        conteudo.add(Box.createVerticalGlue());
        conteudo.add(prefeitura);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(secretaria);
        conteudo.add(Box.createVerticalStrut(42));
        conteudo.add(sistema);
        conteudo.add(Box.createVerticalStrut(12));
        conteudo.add(frase);
        conteudo.add(Box.createVerticalStrut(42));
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

        Timer timer = new Timer(1000, e -> atualizarDataHora(data, hora));
        timer.start();
        atualizarDataHora(data, hora);

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

    private void atualizarDataHora(JLabel data, JLabel hora) {
        Locale brasil = new Locale("pt", "BR");
        LocalTime agora = LocalTime.now();
        LocalDate hoje = LocalDate.now();
        data.setText(hoje.format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", brasil)));
        hora.setText(agora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
}
