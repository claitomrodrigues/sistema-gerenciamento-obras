package view.dashboard;

import dashboard.DashboardResumo;
import dashboard.DashboardService;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardView extends JPanel {

    private final DashboardService service = new DashboardService();
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private JPanel cardsPanel;
    private JLabel subtitulo;

    public DashboardView() {
        inicializar();
        carregarDados();
    }

    private void inicializar() {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        setBackground(new Color(245, 247, 250));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Dashboard Executivo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(33, 37, 41));

        subtitulo = new JLabel("Resumo operacional da Secretaria Municipal de Obras");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(105, 105, 105));

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);

        JButton atualizar = new JButton("Atualizar dados");
        atualizar.setFocusPainted(false);
        atualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        atualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        atualizar.setBackground(new Color(30, 58, 95));
        atualizar.setForeground(Color.WHITE);
        atualizar.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        atualizar.addActionListener(e -> carregarDados());

        topo.add(textos, BorderLayout.WEST);
        topo.add(atualizar, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 3, 18, 18));
        cardsPanel.setOpaque(false);
        add(cardsPanel, BorderLayout.CENTER);
    }

    private void carregarDados() {
        try {
            DashboardResumo r = service.carregarResumo();
            cardsPanel.removeAll();

            cardsPanel.add(card("Equipamentos", String.valueOf(r.getTotalEquipamentos()), "Ativos: " + r.getEquipamentosAtivos() + " | Inativos: " + r.getEquipamentosInativos(), "🚜"));
            cardsPanel.add(card("Combustíveis", String.valueOf(r.getTotalCombustiveis()), "Baixo estoque: " + r.getCombustiveisBaixoEstoque(), "⛽"));
            cardsPanel.add(card("Ocorrências abertas", String.valueOf(r.getOcorrenciasAbertas()), "Pendências operacionais", "⚠"));
            cardsPanel.add(card("Manutenções", String.valueOf(r.getManutencoes()), "Registros cadastrados", "🔧"));
            cardsPanel.add(card("Empenhos no ano", moeda.format(r.getTotalEmpenhos()), "Saldo: " + moeda.format(r.getSaldoEmpenhos()), "💰"));
            cardsPanel.add(card("Licitações no ano", String.valueOf(r.getTotalLicitacoes()), "Processos registrados", "📑"));

            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dashboard: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel card(String titulo, String valor, String detalhe, String icone) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 235)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lTitulo.setForeground(new Color(85, 85, 85));

        JLabel lIcone = new JLabel(icone);
        lIcone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lIcone.setHorizontalAlignment(SwingConstants.RIGHT);

        topo.add(lTitulo, BorderLayout.WEST);
        topo.add(lIcone, BorderLayout.EAST);

        JLabel lValor = new JLabel(valor);
        lValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lValor.setForeground(new Color(23, 37, 84));

        JLabel lDetalhe = new JLabel(detalhe);
        lDetalhe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lDetalhe.setForeground(new Color(110, 110, 110));

        p.add(topo, BorderLayout.NORTH);
        p.add(lValor, BorderLayout.CENTER);
        p.add(lDetalhe, BorderLayout.SOUTH);
        return p;
    }
}
