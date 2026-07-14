package view.home;

import connection.ConnectionFactory;
import dashboard.DashboardResumo;
import dashboard.DashboardService;
import ui.components.RoundedPanel;
import ui.theme.Theme;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Tela inicial executiva. Exibe uma visão rápida da operação sem substituir o
 * dashboard analítico completo.
 */
public class HomeView extends JPanel {

    private static final Locale BRASIL = new Locale("pt", "BR");
    private static final DateTimeFormatter DATA_EXTENSO =
            DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy", BRASIL);
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATA_TABELA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final DashboardService dashboardService = new DashboardService();
    private final JLabel dataHora = new JLabel();
    private final JPanel indicadores = new JPanel(new GridLayout(1, 4, 14, 14));
    private final DefaultTableModel modeloAbastecimentos = modeloNaoEditavel(
            "Data", "Equipamento", "Combustível", "Litros");
    private final DefaultTableModel modeloServicos = modeloNaoEditavel(
            "Número", "Serviço", "Responsável", "Status");
    private final JTextArea alertas = new JTextArea(6, 40);

    public HomeView() {
        inicializar();
        atualizarRelogio();
        carregarDados();

        Timer timerRelogio = new Timer(1000, e -> atualizarRelogio());
        timerRelogio.start();
    }

    private void inicializar() {
        setLayout(new BorderLayout(0, 18));
        StyleUtils.styleView(this);

        add(criarCabecalho(), BorderLayout.NORTH);

        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));

        indicadores.setOpaque(false);
        indicadores.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        corpo.add(indicadores);
        corpo.add(Box.createVerticalStrut(18));

        JPanel atividade = new JPanel(new GridLayout(1, 2, 16, 16));
        atividade.setOpaque(false);
        atividade.setMaximumSize(new Dimension(Integer.MAX_VALUE, 310));
        atividade.add(criarPainelTabela("Últimos abastecimentos",
                "Movimentações mais recentes de combustível", modeloAbastecimentos));
        atividade.add(criarPainelTabela("Ordens de serviço recentes",
                "Serviços abertos ou atualizados recentemente", modeloServicos));
        corpo.add(atividade);
        corpo.add(Box.createVerticalStrut(18));
        corpo.add(criarPainelAlertas());

        add(corpo, BorderLayout.CENTER);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new RoundedPanel(22, Color.WHITE);
        painel.setLayout(new BorderLayout(20, 0));
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(22, 24, 22, 24)
        ));

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Secretaria Municipal de Obras");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Theme.TEXT);

        dataHora.setFont(Theme.SMALL);
        dataHora.setForeground(Theme.MUTED);

        texto.add(titulo);
        texto.add(Box.createVerticalStrut(6));
        JLabel subtitulo = new JLabel("Painel executivo e resumo operacional");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitulo.setForeground(Theme.MUTED);
        texto.add(subtitulo);
        texto.add(Box.createVerticalStrut(4));
        texto.add(dataHora);

        JButton atualizar = new JButton("Atualizar painel");
        StyleUtils.styleSecondaryButton(atualizar);
        atualizar.addActionListener(e -> carregarDados());

        JPanel acao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 12));
        acao.setOpaque(false);
        acao.add(atualizar);

        painel.add(texto, BorderLayout.CENTER);
        painel.add(acao, BorderLayout.EAST);
        return painel;
    }

    private JPanel criarPainelTabela(String titulo, String descricao, DefaultTableModel modelo) {
        JPanel painel = new RoundedPanel(18, Color.WHITE);
        painel.setLayout(new BorderLayout(0, 12));
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(Theme.SECTION);
        lTitulo.setForeground(Theme.TEXT);
        JLabel lDescricao = new JLabel(descricao);
        lDescricao.setFont(Theme.SMALL);
        lDescricao.setForeground(Theme.MUTED);
        cabecalho.add(lTitulo);
        cabecalho.add(Box.createVerticalStrut(3));
        cabecalho.add(lDescricao);

        JTable tabela = new JTable(modelo);
        StyleUtils.estilizarTabela(tabela);
        tabela.setFillsViewportHeight(true);
        tabela.setRowSelectionAllowed(false);
        tabela.setFocusable(false);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        painel.add(cabecalho, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelAlertas() {
        JPanel painel = new RoundedPanel(18, Color.WHITE);
        painel.setLayout(new BorderLayout(0, 12));
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel titulo = new JLabel("Atenção da equipe");
        titulo.setFont(Theme.SECTION);
        titulo.setForeground(Theme.TEXT);

        alertas.setEditable(false);
        alertas.setFocusable(false);
        alertas.setLineWrap(true);
        alertas.setWrapStyleWord(true);
        alertas.setFont(Theme.FONT);
        alertas.setForeground(Theme.TEXT);
        alertas.setBackground(Color.WHITE);
        alertas.setBorder(null);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(alertas, BorderLayout.CENTER);
        return painel;
    }

    private void carregarDados() {
        try {
            DashboardResumo resumo = dashboardService.carregarResumo();
            double estoqueTotal = consultarEstoqueTotal();

            indicadores.removeAll();
            indicadores.add(criarIndicador("Equipamentos ativos",
                    String.valueOf(resumo.getEquipamentosAtivos()),
                    resumo.getEquipamentosInativos() + " inativos", Theme.PRIMARY));
            indicadores.add(criarIndicador("Ordens em andamento",
                    String.valueOf(resumo.getOrdensServicoAbertas()),
                    "Abertas ou em execução", new Color(180, 83, 9)));
            indicadores.add(criarIndicador("Manutenções no limite",
                    String.valueOf(resumo.getManutencoesNoLimite()),
                    "Requerem verificação", Theme.DANGER));
            indicadores.add(criarIndicador("Estoque disponível",
                    String.format(BRASIL, "%.2f L", estoqueTotal),
                    resumo.getCombustiveisBaixoEstoque() + " combustível(is) em nível baixo",
                    Theme.SUCCESS));

            carregarUltimosAbastecimentos();
            carregarOrdensRecentes();
            alertas.setText(consultarAlertas());
            alertas.setCaretPosition(0);

            revalidate();
            repaint();
        } catch (Exception e) {
            indicadores.removeAll();
            indicadores.add(criarIndicador("Painel indisponível", "-",
                    "Não foi possível carregar os indicadores", Theme.DANGER));
            alertas.setText("Não foi possível carregar os dados iniciais: " + mensagem(e));
            revalidate();
            repaint();
        }
    }

    private JPanel criarIndicador(String titulo, String valor, String detalhe, Color destaque) {
        JPanel painel = new RoundedPanel(18, Color.WHITE);
        painel.setLayout(new BorderLayout(12, 0));
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JPanel faixa = new JPanel();
        faixa.setBackground(destaque);
        faixa.setPreferredSize(new Dimension(5, 0));

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        JLabel lTitulo = new JLabel(titulo.toUpperCase(Locale.ROOT));
        lTitulo.setFont(Theme.SMALL_BOLD);
        lTitulo.setForeground(Theme.MUTED);
        JLabel lValor = new JLabel(valor);
        lValor.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lValor.setForeground(Theme.TEXT);
        JLabel lDetalhe = new JLabel(detalhe);
        lDetalhe.setFont(Theme.SMALL);
        lDetalhe.setForeground(Theme.MUTED);
        conteudo.add(lTitulo);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(lValor);
        conteudo.add(Box.createVerticalStrut(6));
        conteudo.add(lDetalhe);

        painel.add(faixa, BorderLayout.WEST);
        painel.add(conteudo, BorderLayout.CENTER);
        return painel;
    }

    private double consultarEstoqueTotal() throws SQLException {
        String sql = "SELECT COALESCE(SUM(litros), 0) FROM combustivel";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0d;
        }
    }

    private void carregarUltimosAbastecimentos() throws SQLException {
        modeloAbastecimentos.setRowCount(0);
        String sql = "SELECT a.data, e.nome, c.tipo, a.litros "
                + "FROM abastecimento a "
                + "JOIN equipamento e ON e.id=a.equipamento_id "
                + "JOIN combustivel c ON c.id=a.combustivel_id "
                + "ORDER BY a.data DESC, a.id DESC LIMIT 6";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modeloAbastecimentos.addRow(new Object[]{
                        formatarData(rs.getString("data")),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        String.format(BRASIL, "%.2f L", rs.getDouble("litros"))
                });
            }
        }
        if (modeloAbastecimentos.getRowCount() == 0) {
            modeloAbastecimentos.addRow(new Object[]{"-", "Nenhum abastecimento registrado", "-", "-"});
        }
    }

    private void carregarOrdensRecentes() throws SQLException {
        modeloServicos.setRowCount(0);
        String sql = "SELECT numero, descricao, responsavel, status FROM ordem_servico "
                + "ORDER BY data_abertura DESC, id DESC LIMIT 6";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modeloServicos.addRow(new Object[]{
                        rs.getString("numero"),
                        limitar(rs.getString("descricao"), 42),
                        valorOuTraco(rs.getString("responsavel")),
                        formatarStatus(rs.getString("status"))
                });
            }
        }
        if (modeloServicos.getRowCount() == 0) {
            modeloServicos.addRow(new Object[]{"-", "Nenhuma ordem de serviço registrada", "-", "-"});
        }
    }

    private String consultarAlertas() throws SQLException {
        StringBuilder texto = new StringBuilder();
        try (Connection c = ConnectionFactory.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT tipo, litros, estoqueMinimo FROM combustivel "
                            + "WHERE estoqueMinimo > 0 AND litros <= estoqueMinimo ORDER BY litros ASC LIMIT 4");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    texto.append("Estoque baixo de ").append(rs.getString("tipo"))
                            .append(": ").append(String.format(BRASIL, "%.2f L", rs.getDouble("litros")))
                            .append(" disponíveis.\n");
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT e.nome FROM manutencao m JOIN equipamento e ON e.id=m.equipamento_id "
                            + "WHERE m.proximaRevisao > 0 AND m.revisaoAtual >= m.proximaRevisao "
                            + "ORDER BY m.data DESC LIMIT 4");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    texto.append("Manutenção no limite para ").append(rs.getString("nome")).append(".\n");
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT numero, vencimento FROM licitacao WHERE vencimento BETWEEN ? AND ? "
                            + "ORDER BY vencimento LIMIT 4")) {
                ps.setString(1, LocalDate.now().toString());
                ps.setString(2, LocalDate.now().plusDays(30).toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        texto.append("Licitação ").append(valorOuTraco(rs.getString("numero")))
                                .append(" vence em ").append(formatarData(rs.getString("vencimento"))).append(".\n");
                    }
                }
            }
        }

        return texto.length() == 0
                ? "Nenhum alerta crítico no momento. A operação está dentro dos parâmetros cadastrados."
                : texto.toString().trim();
    }

    private void atualizarRelogio() {
        LocalTime agora = LocalTime.now();
        dataHora.setText(capitalizar(LocalDate.now().format(DATA_EXTENSO)) + " - " + agora.format(HORA));
    }

    private static DefaultTableModel modeloNaoEditavel(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private String formatarData(String valor) {
        if (valor == null || valor.isBlank()) return "-";
        try { return LocalDate.parse(valor).format(DATA_TABELA); }
        catch (Exception ignored) { return valor; }
    }

    private String formatarStatus(String status) {
        if (status == null || status.isBlank()) return "-";
        return status.replace('_', ' ').toLowerCase(BRASIL).substring(0, 1).toUpperCase(BRASIL)
                + status.replace('_', ' ').toLowerCase(BRASIL).substring(1);
    }

    private String limitar(String texto, int maximo) {
        if (texto == null || texto.isBlank()) return "-";
        return texto.length() <= maximo ? texto : texto.substring(0, maximo - 3) + "...";
    }

    private String valorOuTraco(String texto) {
        return texto == null || texto.isBlank() ? "-" : texto;
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase(BRASIL) + texto.substring(1);
    }

    private String mensagem(Throwable erro) {
        Throwable atual = erro;
        while (atual.getCause() != null) atual = atual.getCause();
        return atual.getMessage() == null ? erro.getClass().getSimpleName() : atual.getMessage();
    }
}
