package view.licitacao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LicitacaoView extends JPanel {

    private JTable tabela;
    private DefaultTableModel tableModel;

    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnAtualizar;

    public LicitacaoView() {

        setLayout(new BorderLayout());

        JPanel painelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnNovo = new JButton("Novo");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnAtualizar = new JButton("Atualizar");

        painelSuperior.add(btnNovo);
        painelSuperior.add(btnEditar);
        painelSuperior.add(btnExcluir);
        painelSuperior.add(btnAtualizar);

        add(painelSuperior, BorderLayout.NORTH);

        String[] colunas = {
                "ID",
                "Número",
                "Objeto",
                "Modalidade",
                "Empresa",
                "Valor",
                "Data",
                "Empenho"
        };

        tableModel = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        tabela = new JTable(tableModel);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

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