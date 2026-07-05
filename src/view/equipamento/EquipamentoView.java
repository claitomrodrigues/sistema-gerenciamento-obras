package view.equipamento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class EquipamentoView extends JPanel {

    private JTable tabela;
    private DefaultTableModel tableModel;

    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnAtualizar;

    public EquipamentoView() {
        inicializar();
    }

    private void inicializar() {
        setLayout(new BorderLayout());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnNovo = new JButton("Novo");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnAtualizar = new JButton("Atualizar");

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnAtualizar);

        add(painelBotoes, BorderLayout.NORTH);

        String[] colunas = {
            "ID",
            "Nome",
            "Tipo",
            "Placa",
            "KM Atual",
            "Horas de Uso",
            "Ativo"
        };

        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(scrollPane, BorderLayout.CENTER);
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
}