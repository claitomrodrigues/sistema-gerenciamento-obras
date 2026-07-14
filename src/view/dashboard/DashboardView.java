package view.dashboard;

import connection.ConnectionFactory;
import dashboard.DashboardResumo;
import dashboard.DashboardService;
import ui.components.Notifications;
import ui.components.RoundedPanel;
import ui.theme.Theme;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class DashboardView extends JPanel {

    private final DashboardService service = new DashboardService();
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private final JPanel cardsPanel = new JPanel(new GridLayout(0, 3, 16, 16));
    private final JPanel graficosPanel = new JPanel(new GridLayout(1, 2, 16, 16));
    private final JTextArea alertasArea = new JTextArea(7, 40);
    private final JLabel subtitulo = new JLabel("Resumo operacional da Secretaria Municipal de Obras");

    public DashboardView() {
        inicializar();
        carregarDados();
    }

    private void inicializar() {
        setLayout(new BorderLayout(0, 16));
        StyleUtils.styleView(this);

        JPanel topo = new JPanel(new BorderLayout(16, 0));
        topo.setOpaque(false);
        topo.add(StyleUtils.criarCabecalhoTela("Dashboard executivo", "Indicadores, custos e alertas operacionais em um único painel"), BorderLayout.WEST);
        JButton atualizar = new JButton("Atualizar dados");
        StyleUtils.styleSecondaryButton(atualizar);
        atualizar.addActionListener(e -> carregarDados());
        JPanel acao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        acao.setOpaque(false);
        acao.add(atualizar);
        topo.add(acao, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        cardsPanel.setOpaque(false);
        graficosPanel.setOpaque(false);
        graficosPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 285));

        alertasArea.setEditable(false);
        alertasArea.setFocusable(false);
        alertasArea.setLineWrap(true);
        alertasArea.setWrapStyleWord(true);
        alertasArea.setFont(Theme.FONT);
        alertasArea.setForeground(Theme.TEXT);
        alertasArea.setBackground(Color.WHITE);
        alertasArea.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.add(subtituloAtualizacao());
        corpo.add(Box.createVerticalStrut(12));
        corpo.add(cardsPanel);
        corpo.add(Box.createVerticalStrut(18));
        corpo.add(graficosPanel);
        corpo.add(Box.createVerticalStrut(18));
        corpo.add(painelAlertas());

        JScrollPane scroll = new JScrollPane(corpo);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel subtituloAtualizacao() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        subtitulo.setFont(Theme.SMALL);
        subtitulo.setForeground(Theme.MUTED);
        p.add(subtitulo, BorderLayout.WEST);
        return p;
    }

    private JPanel painelAlertas() {
        JPanel p = new RoundedPanel(18, Color.WHITE);
        p.setLayout(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(18, 20, 18, 20)
        ));
        JLabel titulo = new JLabel("Alertas operacionais");
        titulo.setFont(Theme.SECTION);
        titulo.setForeground(Theme.TEXT);
        JLabel descricao = new JLabel("Itens que merecem atenção da equipe");
        descricao.setFont(Theme.SMALL);
        descricao.setForeground(Theme.MUTED);
        JPanel cabecalho = new JPanel();
        cabecalho.setOpaque(false);
        cabecalho.setLayout(new BoxLayout(cabecalho, BoxLayout.Y_AXIS));
        cabecalho.add(titulo);
        cabecalho.add(Box.createVerticalStrut(3));
        cabecalho.add(descricao);
        p.add(cabecalho, BorderLayout.NORTH);
        p.add(alertasArea, BorderLayout.CENTER);
        return p;
    }

    private void carregarDados() {
        try {
            DashboardResumo r = service.carregarResumo();
            cardsPanel.removeAll();
            graficosPanel.removeAll();

            cardsPanel.add(card("Equipamentos", String.valueOf(r.getTotalEquipamentos()),
                    "Ativos: " + r.getEquipamentosAtivos() + "  •  Inativos: " + r.getEquipamentosInativos(), Theme.PRIMARY));
            cardsPanel.add(card("Combustíveis", String.valueOf(r.getTotalCombustiveis()),
                    "Baixo estoque: " + r.getCombustiveisBaixoEstoque(), Theme.WARNING));
            cardsPanel.add(card("Ocorrências abertas", String.valueOf(r.getOcorrenciasAbertas()),
                    "Pendências operacionais", Theme.DANGER));
            cardsPanel.add(card("Manutenções", String.valueOf(r.getManutencoes()),
                    "No limite: " + r.getManutencoesNoLimite(), new Color(79, 70, 229)));
            cardsPanel.add(card("Ordens de serviço", String.valueOf(r.getOrdensServicoAbertas()),
                    "Abertas ou em andamento", new Color(180, 83, 9)));
            cardsPanel.add(card("Abastecido no ano", String.format(new Locale("pt", "BR"), "%.2f L", r.getLitrosAbastecidosAno()),
                    "Consumo acumulado", new Color(14, 116, 144)));
            cardsPanel.add(card("Empenhos no ano", moeda.format(r.getTotalEmpenhos()),
                    "Saldo: " + moeda.format(r.getSaldoEmpenhos()), Theme.SUCCESS));
            cardsPanel.add(card("Licitações no ano", String.valueOf(r.getTotalLicitacoes()),
                    "Vencendo em 30 dias: " + r.getLicitacoesVencendo(), new Color(8, 145, 178)));

            graficosPanel.add(new BarChartPanel("Combustível por mês", "Litros abastecidos", consultarCombustivelPorMes()));
            graficosPanel.add(new BarChartPanel("Manutenções por equipamento", "Quantidade de registros", consultarGastosEquipamento()));
            alertasArea.setText(consultarAlertas());
            alertasArea.setCaretPosition(0);
            subtitulo.setText("Dados atualizados em " + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss")));
            revalidate();
            repaint();
        } catch (Exception e) {
            Notifications.error(this, "Erro ao carregar dashboard: " + e.getMessage());
        }
    }

    private JPanel card(String titulo, String valor, String detalhe, Color destaque) {
        JPanel p = new RoundedPanel(18, Color.WHITE);
        p.setLayout(new BorderLayout(12, 10));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JPanel faixa = new JPanel();
        faixa.setBackground(destaque);
        faixa.setPreferredSize(new Dimension(5, 0));

        JLabel lTitulo = new JLabel(titulo.toUpperCase(Locale.ROOT));
        lTitulo.setFont(Theme.SMALL_BOLD);
        lTitulo.setForeground(Theme.MUTED);
        JLabel lValor = new JLabel(valor);
        lValor.setFont(Theme.KPI);
        lValor.setForeground(Theme.TEXT);
        JLabel lDetalhe = new JLabel(detalhe);
        lDetalhe.setFont(Theme.SMALL);
        lDetalhe.setForeground(Theme.MUTED);

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.add(lTitulo);
        conteudo.add(Box.createVerticalStrut(9));
        conteudo.add(lValor);
        conteudo.add(Box.createVerticalStrut(7));
        conteudo.add(lDetalhe);

        p.add(faixa, BorderLayout.WEST);
        p.add(conteudo, BorderLayout.CENTER);
        return p;
    }

    private Map<String, Double> consultarCombustivelPorMes() throws SQLException {
        Map<String, Double> dados = new LinkedHashMap<>();
        String sql = "SELECT substr(data,1,7) mes, sum(litros) total FROM abastecimento " +
                "GROUP BY substr(data,1,7) ORDER BY mes DESC LIMIT 6";
        try (Connection c = ConnectionFactory.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) dados.put(rs.getString(1), rs.getDouble(2));
        }
        return inverter(dados);
    }

    private Map<String, Double> consultarGastosEquipamento() throws SQLException {
        Map<String, Double> dados = new LinkedHashMap<>();
        String sql = "SELECT e.nome, count(m.id) total FROM manutencao m " +
                "JOIN equipamento e ON e.id=m.equipamento_id GROUP BY e.nome ORDER BY total DESC LIMIT 6";
        try (Connection c = ConnectionFactory.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) dados.put(rs.getString(1), rs.getDouble(2));
        }
        return dados;
    }

    private String consultarAlertas() throws SQLException {
        StringBuilder sb = new StringBuilder();
        try (Connection c = ConnectionFactory.getConnection()) {
            try (Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery("SELECT tipo, litros, estoqueMinimo FROM combustivel " +
                         "WHERE litros <= estoqueMinimo AND estoqueMinimo > 0")) {
                while (rs.next()) sb.append("Estoque baixo: ").append(rs.getString(1))
                        .append(" — ").append(rs.getDouble(2)).append(" L\n");
            }
            String limite = LocalDate.now().plusDays(30).toString();
            try (PreparedStatement ps = c.prepareStatement("SELECT numero, objeto, vencimento FROM licitacao " +
                    "WHERE vencimento IS NOT NULL AND vencimento <= ? ORDER BY vencimento LIMIT 5")) {
                ps.setString(1, limite);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) sb.append("Licitação próxima do vencimento: ")
                            .append(rs.getString(1)).append(" — ").append(rs.getString(3)).append("\n");
                }
            }
            try (Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery("SELECT e.nome FROM manutencao m JOIN equipamento e " +
                         "ON e.id=m.equipamento_id WHERE m.proximaRevisao > 0 AND m.revisaoAtual >= m.proximaRevisao " +
                         "ORDER BY m.data DESC LIMIT 5")) {
                while (rs.next()) sb.append("Manutenção no limite: ").append(rs.getString(1)).append("\n");
            }
        }
        if (sb.length() == 0) sb.append("Nenhum alerta crítico no momento.");
        return sb.toString();
    }

    private Map<String, Double> inverter(Map<String, Double> origem) {
        List<Map.Entry<String, Double>> lista = new ArrayList<>(origem.entrySet());
        Collections.reverse(lista);
        Map<String, Double> res = new LinkedHashMap<>();
        for (Map.Entry<String, Double> e : lista) res.put(e.getKey(), e.getValue());
        return res;
    }

    private static class BarChartPanel extends RoundedPanel {
        private final String titulo;
        private final String descricao;
        private final Map<String, Double> dados;

        BarChartPanel(String titulo, String descricao, Map<String, Double> dados) {
            super(18, Color.WHITE);
            this.titulo = titulo;
            this.descricao = descricao;
            this.dados = dados;
            setPreferredSize(new Dimension(420, 285));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.BORDER),
                    new EmptyBorder(16, 18, 16, 18)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(Theme.SECTION);
            g2.setColor(Theme.TEXT);
            g2.drawString(titulo, 20, 30);
            g2.setFont(Theme.SMALL);
            g2.setColor(Theme.MUTED);
            g2.drawString(descricao, 20, 50);

            if (dados == null || dados.isEmpty()) {
                g2.drawString("Sem dados para exibir.", 20, 88);
                g2.dispose();
                return;
            }

            double max = dados.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);
            int baseY = getHeight() - 42;
            int areaTop = 76;
            int maxH = Math.max(50, baseY - areaTop);
            int gap = 12;
            int totalWidth = Math.max(1, getWidth() - 48);
            int w = Math.max(24, (totalWidth - gap * (dados.size() - 1)) / dados.size());
            int x = 22;
            int i = 0;

            for (Map.Entry<String, Double> e : dados.entrySet()) {
                int h = (int) ((e.getValue() / max) * maxH);
                int bx = x + i * (w + gap);
                g2.setColor(Theme.PRIMARY_SOFT);
                g2.fillRoundRect(bx, areaTop, w, maxH, 10, 10);
                g2.setColor(Theme.PRIMARY);
                g2.fillRoundRect(bx, baseY - h, w, h, 10, 10);
                g2.setColor(Theme.MUTED);
                g2.setFont(Theme.SMALL);
                String lab = e.getKey() == null ? "-" : e.getKey();
                if (lab.length() > 11) lab = lab.substring(0, 11);
                g2.drawString(lab, bx, baseY + 17);
                i++;
            }
            g2.dispose();
        }
    }
}
