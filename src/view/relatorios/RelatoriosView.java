package view.relatorios;

import com.toedter.calendar.JDateChooser;
import report.*;
import report.documents.OrdemServico;
import ui.components.Notifications;
import ui.theme.Theme;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class RelatoriosView extends JPanel {
    private JDateChooser dataInicio;
    private JDateChooser dataFim;
    private JLabel statusPeriodo;

    public RelatoriosView() { inicializar(); }

    private void inicializar() {
        setLayout(new BorderLayout(0, 18));
        StyleUtils.styleView(this);
        add(criarTopo(), BorderLayout.NORTH);
        add(criarCentro(), BorderLayout.CENTER);
    }

    private JPanel criarTopo() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 14));
        wrapper.setOpaque(false);
        wrapper.add(StyleUtils.criarCabecalhoTela(
                "Central de relatórios",
                "Gere documentos operacionais e administrativos com padrão oficial em PDF"
        ), BorderLayout.NORTH);

        JPanel filtro = new JPanel(new BorderLayout(14, 10));
        filtro.setBackground(Color.WHITE);
        filtro.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(14, 16, 14, 16)));

        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        campos.setOpaque(false);
        JLabel periodo = new JLabel("Período dos relatórios");
        periodo.setFont(Theme.FONT_BOLD);
        periodo.setForeground(Theme.TEXT);
        dataInicio = criarData();
        dataFim = criarData();
        campos.add(periodo);
        campos.add(new JLabel("De"));
        campos.add(dataInicio);
        campos.add(new JLabel("até"));
        campos.add(dataFim);

        statusPeriodo = new JLabel("Datas aplicadas aos relatórios por período");
        statusPeriodo.setForeground(Theme.MUTED);
        statusPeriodo.setFont(Theme.SMALL);
        filtro.add(campos, BorderLayout.WEST);
        filtro.add(statusPeriodo, BorderLayout.EAST);
        wrapper.add(filtro, BorderLayout.CENTER);
        return wrapper;
    }

    private JDateChooser criarData() {
        JDateChooser chooser = new JDateChooser();
        chooser.setDate(new Date());
        chooser.setDateFormatString("dd/MM/yyyy");
        chooser.setPreferredSize(new Dimension(145, 38));
        StyleUtils.prepareInput(chooser);
        return chooser;
    }

    private JScrollPane criarCentro() {
        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setOpaque(false);
        conteudo.add(secao("Operação", "Controle diário da frota e dos serviços",
                card("Equipamentos", "Relação completa da frota cadastrada.", "GERAL", () -> abrir(new RelatorioEquipamentos().gerarGeral())),
                card("Equipamentos ativos", "Somente máquinas e veículos em operação.", "ATIVOS", () -> abrir(new RelatorioEquipamentos().gerarAtivos())),
                card("Equipamentos inativos", "Itens desativados ou fora de operação.", "INATIVOS", () -> abrir(new RelatorioEquipamentos().gerarInativos())),
                card("Abastecimentos", "Consumo de combustível no período selecionado.", "CONSUMO", () -> abrir(new RelatorioAbastecimentos().gerarPorPeriodo(getInicio(), getFim()))),
                card("Manutenções", "Serviços e revisões registrados no período.", "SERVIÇOS", () -> abrir(new RelatorioManutencoes().gerarPorPeriodo(getInicio(), getFim()))),
                card("Ocorrências", "Histórico de ocorrências operacionais.", "HISTÓRICO", () -> abrir(new RelatorioOcorrencias().gerarPorPeriodo(getInicio(), getFim())))
        ));
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(secao("Administração", "Acompanhamento financeiro e processos públicos",
                card("Empenhos", "Valores, fornecedores e saldos por período.", "FINANCEIRO", () -> abrir(new RelatorioEmpenhos().gerarPorPeriodo(getInicio(), getFim()))),
                card("Licitações", "Processos licitatórios no período selecionado.", "PROCESSOS", () -> abrir(new RelatorioLicitacoes().gerarPorPeriodo(getInicio(), getFim())))
        ));
        conteudo.add(Box.createVerticalStrut(20));
        conteudo.add(secao("Documentos oficiais", "Modelos institucionais prontos para impressão",
                card("Parecer técnico", "Documento técnico padronizado da secretaria.", "OFICIAL", () -> abrir(new ParecerTecnico().gerar(
                        "Assunto do parecer", "Secretaria Municipal de Obras",
                        "Descreva aqui o conteúdo do parecer técnico."
                ))),
                card("Ordem de serviço", "Documento para solicitação e execução de serviço.", "ORDEM", () -> abrir(new OrdemServico().gerar(
                        "Equipamento não informado", "Serviço a definir", "Responsável", "Observações a preencher."
                )))
        ));
        conteudo.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        return scroll;
    }

    private JPanel secao(String titulo, String subtitulo, JPanel... cards) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(titulo); t.setFont(Theme.SECTION); t.setForeground(Theme.TEXT);
        JLabel s = new JLabel(subtitulo); s.setFont(Theme.FONT); s.setForeground(Theme.MUTED);
        textos.add(t); textos.add(Box.createVerticalStrut(3)); textos.add(s);
        wrapper.add(textos, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false);
        for (JPanel card : cards) grid.add(card);
        wrapper.add(grid, BorderLayout.CENTER);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, wrapper.getPreferredSize().height));
        return wrapper;
    }

    private JPanel card(String titulo, String descricao, String tag, AcaoRelatorio acao) {
        JPanel p = new JPanel(new BorderLayout(10, 12));
        p.setBackground(Color.WHITE);
        Border normal = new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(17, 18, 17, 18));
        Border hover = new CompoundBorder(new LineBorder(Theme.PRIMARY, 1, true), new EmptyBorder(17, 18, 17, 18));
        p.setBorder(normal);

        JLabel badge = new JLabel(tag);
        badge.setOpaque(true);
        badge.setBackground(Theme.PRIMARY_SOFT);
        badge.setForeground(Theme.PRIMARY_DARK);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setBorder(new EmptyBorder(5, 8, 5, 8));
        JPanel linhaTag = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linhaTag.setOpaque(false); linhaTag.add(badge);

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lTitulo.setForeground(Theme.TEXT);
        JTextArea lDesc = new JTextArea(descricao);
        lDesc.setWrapStyleWord(true); lDesc.setLineWrap(true); lDesc.setOpaque(false);
        lDesc.setEditable(false); lDesc.setFocusable(false); lDesc.setFont(Theme.FONT); lDesc.setForeground(Theme.MUTED);

        JButton btn = new JButton("Gerar PDF");
        StyleUtils.styleToolbarButton(btn);
        btn.addActionListener(e -> executar(acao));
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rodape.setOpaque(false); rodape.add(btn);

        JPanel centro = new JPanel(); centro.setOpaque(false); centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(lTitulo); centro.add(Box.createVerticalStrut(7)); centro.add(lDesc);
        p.add(linhaTag, BorderLayout.NORTH); p.add(centro, BorderLayout.CENTER); p.add(rodape, BorderLayout.SOUTH);
        p.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { p.setBorder(hover); }
            @Override public void mouseExited(MouseEvent e) { p.setBorder(normal); }
        });
        return p;
    }

    private void executar(AcaoRelatorio acao) {
        try { validarPeriodo(); acao.executar(); }
        catch (Exception ex) { Notifications.error(this, ex.getMessage() == null ? "Não foi possível gerar o PDF." : ex.getMessage()); ex.printStackTrace(); }
    }

    private void validarPeriodo() {
        if (dataInicio.getDate() == null || dataFim.getDate() == null) throw new IllegalArgumentException("Informe as duas datas do período.");
        if (getInicio().isAfter(getFim())) throw new IllegalArgumentException("A data inicial não pode ser posterior à data final.");
    }

    private void abrir(String caminho) {
        ReportManager.ResultadoAbertura resultado = ReportManager.abrirPdf(caminho);
        String local = ReportManager.validarArquivo(caminho).getAbsolutePath();

        switch (resultado) {
            case PDF_ABERTO:
                Notifications.success(this, "PDF gerado e aberto com sucesso.");
                break;
            case PASTA_ABERTA:
                Notifications.success(this, "PDF gerado. A pasta do relatório foi aberta: " + local);
                break;
            default:
                Notifications.success(this, "PDF gerado com sucesso em: " + local);
                break;
        }
    }

    private LocalDate getInicio() { return converter(dataInicio.getDate()); }
    private LocalDate getFim() { return converter(dataFim.getDate()); }
    private LocalDate converter(Date data) { return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); }
    private interface AcaoRelatorio { void executar() throws Exception; }
}
