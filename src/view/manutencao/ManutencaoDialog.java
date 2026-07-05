package view.manutencao;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ManutencaoDialog extends JDialog {

    private JComboBox<String> cbEquipamento;

    private JTextField txtDescricao;
    private JTextField txtRevisaoAtual;
    private JTextField txtProximaRevisao;
    private JTextField txtData;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public ManutencaoDialog() {

        setTitle("Cadastro de Manutenção");
        setSize(500, 350);
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Equipamento:"));
        cbEquipamento = new JComboBox<>();
        panel.add(cbEquipamento);

        panel.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField();
        panel.add(txtDescricao);

        panel.add(new JLabel("Revisão Atual:"));
        txtRevisaoAtual = new JTextField();
        panel.add(txtRevisaoAtual);

        panel.add(new JLabel("Próxima Revisão:"));
        txtProximaRevisao = new JTextField();
        panel.add(txtProximaRevisao);

        panel.add(new JLabel("Data:"));
        txtData = new JTextField();
        panel.add(txtData);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);

    }

    public JComboBox<String> getCbEquipamento() {
        return cbEquipamento;
    }

    public JTextField getTxtDescricao() {
        return txtDescricao;
    }

    public JTextField getTxtRevisaoAtual() {
        return txtRevisaoAtual;
    }

    public JTextField getTxtProximaRevisao() {
        return txtProximaRevisao;
    }

    public JTextField getTxtData() {
        return txtData;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

}