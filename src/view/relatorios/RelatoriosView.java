package view.relatorios;

import com.toedter.calendar.JDateChooser;
import report.*;
import report.documents.OrdemServico;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RelatoriosView extends JPanel {

    private JDateChooser dataInicio;
    private JDateChooser dataFim;

    public RelatoriosView() {
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        add(criarTopo(), BorderLayout.NORTH);
        add(criarCentro(), BorderLayout.CENTER);
    }

    private JPanel criarTopo() {
        JPanel topo = new JPanel(new BorderLayout(12, 12));
        topo.setOpaque(false);

        JLabel titulo = new JLabel("Central de Relatórios");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(33, 37, 41));

        JPanel periodo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        periodo.setOpaque(false);

        dataInicio = new JDateChooser();
        dataInicio.setDate(new Date());
        dataInicio.setPreferredSize(new Dimension(135, 30));

        dataFim = new JDateChooser();
        dataFim.setDate(new Date());
        dataFim.setPreferredSize(new Dimension(135, 30));

        periodo.add(new JLabel("Período:"));
        periodo.add(dataInicio);
        periodo.add(new JLabel("até"));
        periodo.add(dataFim);

        topo.add(titulo, BorderLayout.WEST);
        topo.add(periodo, BorderLayout.EAST);
        return topo;
    }

    private JScrollPane criarCentro() {
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);

        conteudo.add(secao("Relatórios Operacionais",
                card("Equipamentos - Geral", "Relação completa dos equipamentos.", () -> abrir(new RelatorioEquipamentos().gerarGeral())),
                card("Equipamentos - Ativos", "Somente equipamentos ativos.", () -> abrir(new RelatorioEquipamentos().gerarAtivos())),
                card("Equipamentos - Inativos", "Somente equipamentos inativos.", () -> abrir(new RelatorioEquipamentos().gerarInativos())),
                card("Abastecimentos", "Consumo por período.", () -> abrir(new RelatorioAbastecimentos().gerarPorPeriodo(getInicio(), getFim()))),
                card("Manutenções", "Manutenções registradas no período.", () -> abrir(new RelatorioManutencoes().gerarPorPeriodo(getInicio(), getFim()))),
                card("Ocorrências", "Ocorrências registradas no período.", () -> abrir(new RelatorioOcorrencias().gerarPorPeriodo(getInicio(), getFim())))
        ));

        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(secao("Relatórios Administrativos",
                card("Empenhos", "Valores e saldos por período.", () -> abrir(new RelatorioEmpenhos().gerarPorPeriodo(getInicio(), getFim()))),
                card("Licitações", "Processos licitatórios por período.", () -> abrir(new RelatorioLicitacoes().gerarPorPeriodo(getInicio(), getFim())))
        ));

        conteudo.add(Box.createVerticalStrut(16));
        conteudo.add(secao("Documentos Oficiais",
                card("Parecer Técnico", "Documento oficial no padrão da secretaria.", () -> abrir(new ParecerTecnico().gerar(
                        "Assunto do parecer",
                        "Secretaria Municipal de Obras",
                        "Descreva aqui o conteúdo do parecer técnico. Este texto poderá ser substituído por dados vindos de uma tela própria futuramente."
                ))),
                card("Ordem de Serviço", "Modelo simples para solicitação de serviço.", () -> abrir(new OrdemServico().gerar(
                        "Equipamento não informado",
                        "Serviço a definir",
                        "Responsável",
                        "Observações a preencher."
                )))
        ));

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        return scroll;
    }

    private JPanel secao(String titulo, JPanel... cards) {
        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(23, 37, 84));
        wrapper.add(label, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false);
        for (JPanel card : cards) grid.add(card);

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel card(String titulo, String descricao, AcaoRelatorio acao) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 235)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lTitulo.setForeground(new Color(33, 37, 41));

        JTextArea lDesc = new JTextArea(descricao);
        lDesc.setWrapStyleWord(true);
        lDesc.setLineWrap(true);
        lDesc.setOpaque(false);
        lDesc.setEditable(false);
        lDesc.setFocusable(false);
        lDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lDesc.setForeground(new Color(100, 100, 100));

        JButton btn = new JButton("Gerar PDF");
        btn.setFocusPainted(false);
        btn.addActionListener(e -> executar(acao));

        p.add(lTitulo, BorderLayout.NORTH);
        p.add(lDesc, BorderLayout.CENTER);
        p.add(btn, BorderLayout.SOUTH);
        return p;
    }

    private void executar(AcaoRelatorio acao) {
        try {
            acao.executar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao gerar relatório", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void abrir(String caminho) {
        ReportManager.abrirPdf(caminho);
        JOptionPane.showMessageDialog(this, "Arquivo gerado com sucesso:\n" + caminho);
    }

    private LocalDate getInicio() {
        return converter(dataInicio.getDate());
    }

    private LocalDate getFim() {
        return converter(dataFim.getDate());
    }

    private LocalDate converter(Date data) {
        if (data == null) return LocalDate.now();
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private interface AcaoRelatorio {
        void executar() throws Exception;
    }
}
