package view.manutencao;

import controller.EquipamentoController;
import controller.ManutencaoController;
import model.Equipamento;
import model.Manutencao;
import security.PermissionManager;
import util.ExcelImportUtils;
import util.FormUtils;
import util.TableExportUtils;
import view.StyleUtils;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.text.NumberFormat;
import java.util.Locale;

public class ManutencaoView extends JPanel {

    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private JTable tabela;
    private DefaultTableModel tableModel;
    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnAtualizar;
    private JButton btnExportar;
    private JButton btnImportar;

    private final ManutencaoController controller = new ManutencaoController();
    private final EquipamentoController equipamentoController = new EquipamentoController();

    public ManutencaoView() {
        inicializar();
        configurarEventos();
        carregarTabela();
    }

    private void inicializar() {
        setLayout(new BorderLayout());
        StyleUtils.styleView(this);

        JPanel toolbar = StyleUtils.toolbar();
        btnNovo = new JButton("Novo");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnAtualizar = new JButton("Atualizar");
        btnExportar = new JButton("Exportar CSV");
        btnImportar = new JButton("Importar Excel/CSV");

        StyleUtils.styleToolbarButton(btnNovo);
        StyleUtils.styleToolbarButton(btnEditar);
        StyleUtils.styleDangerButton(btnExcluir);
        StyleUtils.styleSecondaryButton(btnAtualizar);
        StyleUtils.styleSecondaryButton(btnExportar);
        StyleUtils.styleSecondaryButton(btnImportar);

        toolbar.add(btnNovo);
        toolbar.add(btnEditar);
        toolbar.add(btnExcluir);
        toolbar.add(btnAtualizar);
        toolbar.add(btnExportar);
        toolbar.add(btnImportar);

        PermissionManager.aplicarCrud(this, "manutencao", btnNovo, btnEditar, btnExcluir);
        PermissionManager.aplicar(
                btnImportar,
                PermissionManager.podeModuloAcao("manutencao", "importar"),
                "Seu perfil não permite importar neste módulo."
        );

        add(StyleUtils.criarTopoModulo(
                "Manutenções",
                "Registre revisões, serviços e custos realizados na frota",
                toolbar
        ), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Equipamento", "Descrição", "Revisão Atual", "Próxima Revisão", "Valor", "Data"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        StyleUtils.adicionarPesquisa(toolbar, tabela);
        add(StyleUtils.tableScroll(tabela), BorderLayout.CENTER);
    }

    private void configurarEventos() {
        btnNovo.addActionListener(e -> abrirDialog(null));
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarTabela());
        btnExportar.addActionListener(e -> TableExportUtils.exportarCSV(this, tabela, "manutencao"));
        btnImportar.addActionListener(e -> ExcelImportUtils.importarManutencoes(this, this::carregarTabela));
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        for (Manutencao m : controller.listar()) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getEquipamento(),
                    m.getDescricao(),
                    m.getRevisaoAtual(),
                    m.getProximaRevisao(),
                    MOEDA.format(m.getValor()),
                    m.getData()
            });
        }
    }

    private void popularEquipamentos(ManutencaoDialog dialog) {
        dialog.getCbEquipamento().removeAllItems();
        for (Equipamento equipamento : equipamentoController.listar()) {
            dialog.getCbEquipamento().addItem(equipamento.toString());
        }
    }

    private void selecionarEquipamento(ManutencaoDialog dialog, Equipamento equipamento) {
        if (equipamento == null) {
            return;
        }
        String prefixo = equipamento.getId() + " - ";
        for (int i = 0; i < dialog.getCbEquipamento().getItemCount(); i++) {
            String item = dialog.getCbEquipamento().getItemAt(i);
            if (item != null && (item.equals(String.valueOf(equipamento.getId())) || item.startsWith(prefixo))) {
                dialog.getCbEquipamento().setSelectedIndex(i);
                return;
            }
        }
    }

    private void editar() {
        int id = FormUtils.selectedTableId(tabela);
        if (id < 0) {
            FormUtils.info("Selecione um registro para editar.");
            return;
        }

        Manutencao manutencao = controller.buscarPorId(id);
        if (manutencao == null) {
            FormUtils.info("A manutenção selecionada não foi encontrada.");
            carregarTabela();
            return;
        }
        abrirDialog(manutencao);
    }

    private void excluir() {
        int id = FormUtils.selectedTableId(tabela);
        if (id < 0) {
            FormUtils.info("Selecione um registro para excluir.");
            return;
        }
        if (FormUtils.confirmar("Deseja excluir esta manutenção?")) {
            controller.deletar(id);
            carregarTabela();
        }
    }

    private void abrirDialog(Manutencao existente) {
        ManutencaoDialog dialog = new ManutencaoDialog();
        popularEquipamentos(dialog);

        if (existente != null) {
            selecionarEquipamento(dialog, existente.getEquipamento());
            dialog.getTxtDescricao().setText(existente.getDescricao());
            dialog.getTxtRevisaoAtual().setText(String.valueOf(existente.getRevisaoAtual()));
            dialog.getTxtProximaRevisao().setText(String.valueOf(existente.getProximaRevisao()));
            dialog.getTxtValor().setText(String.format(new Locale("pt", "BR"), "%.2f", existente.getValor()));
            dialog.getDataChooser().setDate(StyleUtils.toUtilDate(existente.getData()));
        }

        dialog.getBtnCancelar().addActionListener(e -> dialog.dispose());
        dialog.getBtnSalvar().addActionListener(e -> {
            try {
                Manutencao manutencao = existente == null ? new Manutencao() : existente;
                Equipamento equipamento = new Equipamento();
                equipamento.setId(FormUtils.selectedId(dialog.getCbEquipamento(), "Equipamento"));

                manutencao.setEquipamento(equipamento);
                manutencao.setDescricao(dialog.getTxtDescricao().getText().trim());
                manutencao.setRevisaoAtual(FormUtils.parseDouble(
                        dialog.getTxtRevisaoAtual().getText(), "Revisão atual"));
                manutencao.setProximaRevisao(FormUtils.parseDouble(
                        dialog.getTxtProximaRevisao().getText(), "Próxima revisão"));
                manutencao.setValor(FormUtils.parseDouble(dialog.getTxtValor().getText(), "Valor"));
                manutencao.setData(StyleUtils.getSqlDate(dialog.getDataChooser(), "Data"));

                if (existente == null) {
                    controller.salvar(manutencao);
                } else {
                    controller.atualizar(manutencao);
                }

                dialog.dispose();
                carregarTabela();
            } catch (Exception ex) {
                FormUtils.erro(ex);
            }
        });

        dialog.setVisible(true);
    }

    public JTable getTabela() {
        return tabela;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnNovo() {
        return btnNovo;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JButton getBtnExcluir() {
        return btnExcluir;
    }

    public JButton getBtnAtualizar() {
        return btnAtualizar;
    }

    public JButton getBtnExportar() {
        return btnExportar;
    }

    public JButton getBtnImportar() {
        return btnImportar;
    }
}
