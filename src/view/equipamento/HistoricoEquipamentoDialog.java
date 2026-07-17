package view.equipamento;

import controller.AbastecimentoController;
import controller.ManutencaoController;
import controller.OcorrenciaController;
import controller.OrdemServicoController;
import model.Abastecimento;
import model.Equipamento;
import model.Manutencao;
import model.Ocorrencia;
import model.OrdemServico;
import view.StyleUtils;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Window;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoricoEquipamentoDialog extends JDialog {

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public HistoricoEquipamentoDialog(Window owner, Equipamento equipamento) {
        super(owner, "Histórico - " + equipamento.getNome(), ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Abastecimentos", abastecimentos(equipamento));
        tabs.add("Manutenções", manutencoes(equipamento));
        tabs.add("Ocorrências", ocorrencias(equipamento));
        tabs.add("Ordens de Serviço", ordens(equipamento));
        add(tabs, BorderLayout.CENTER);

        JLabel resumo = new JLabel("  KM atual: " + equipamento.getKmAtual()
                + "   |   Horas de uso: " + equipamento.getHorasUso()
                + "   |   Situação: " + equipamento.getStatusTexto());
        resumo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(resumo, BorderLayout.NORTH);

        setSize(960, 520);
        setLocationRelativeTo(owner);
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private JScrollPane tabela(String[] colunas, List<Object[]> linhas) {
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Object[] linha : linhas) {
            modelo.addRow(linha);
        }
        return StyleUtils.tableScroll(new JTable(modelo));
    }

    private JScrollPane abastecimentos(Equipamento equipamento) {
        List<Object[]> linhas = new ArrayList<>();
        double total = 0;
        for (Abastecimento abastecimento : new AbastecimentoController().listar()) {
            if (abastecimento.getEquipamento() != null
                    && abastecimento.getEquipamento().getId() == equipamento.getId()) {
                linhas.add(new Object[]{
                        abastecimento.getDataFormatada(),
                        abastecimento.getCombustivel() == null ? "-" : abastecimento.getCombustivel().getTipo(),
                        abastecimento.getLitros()
                });
                total += abastecimento.getLitros();
            }
        }
        linhas.add(new Object[]{"TOTAL", "", total});
        return tabela(new String[]{"Data", "Combustível", "Litros"}, linhas);
    }

    private JScrollPane manutencoes(Equipamento equipamento) {
        List<Object[]> linhas = new ArrayList<>();
        double total = 0;
        for (Manutencao manutencao : new ManutencaoController().listar()) {
            if (manutencao.getEquipamento() != null
                    && manutencao.getEquipamento().getId() == equipamento.getId()) {
                linhas.add(new Object[]{
                        manutencao.getDataFormatada(),
                        manutencao.getDescricao(),
                        manutencao.getRevisaoAtual(),
                        manutencao.getProximaRevisao(),
                        MOEDA.format(manutencao.getValor())
                });
                total += manutencao.getValor();
            }
        }
        linhas.add(new Object[]{"TOTAL", "", "", "", MOEDA.format(total)});
        return tabela(
                new String[]{"Data", "Descrição", "Revisão atual", "Próxima revisão", "Valor"},
                linhas
        );
    }

    private JScrollPane ocorrencias(Equipamento equipamento) {
        List<Object[]> linhas = new ArrayList<>();
        for (Ocorrencia ocorrencia : new OcorrenciaController().listar()) {
            if (ocorrencia.getEquipamento() != null
                    && ocorrencia.getEquipamento().getId() == equipamento.getId()) {
                linhas.add(new Object[]{
                        ocorrencia.getDataFormatada(),
                        ocorrencia.getDescricao(),
                        ocorrencia.getStatus()
                });
            }
        }
        return tabela(new String[]{"Data", "Descrição", "Status"}, linhas);
    }

    private JScrollPane ordens(Equipamento equipamento) {
        List<Object[]> linhas = new ArrayList<>();
        for (OrdemServico ordem : new OrdemServicoController().listarPorEquipamento(equipamento.getId())) {
            linhas.add(new Object[]{
                    ordem.getNumero(),
                    ordem.getDataAberturaFormatada(),
                    ordem.getDescricao(),
                    ordem.getPrioridade(),
                    ordem.getStatus()
            });
        }
        return tabela(
                new String[]{"Número", "Abertura", "Descrição", "Prioridade", "Status"},
                linhas
        );
    }
}
