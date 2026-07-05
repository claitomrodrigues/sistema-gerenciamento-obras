package view.ocorrencia;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OcorrenciaDialog extends JDialog {

    private JComboBox<String> cbEquipamento;
    private JTextField txtDescricao;
    private JTextField txtData;
    private JComboBox<String> cbStatus;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public OcorrenciaDialog() {

        setTitle("Cadastro de Ocorrência");
        setSize(500, 300);
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Equipamento:"));
        cbEquipamento = new JComboBox<>();
        panel.add(cbEquipamento);

        panel.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField();
        panel.add(txtDescricao);

        panel.add(new JLabel("Data:"));
        txtData = new JTextField();
        panel.add(txtData);

        panel.add(new JLabel("Status:"));
        cbStatus = new JComboBox<>();
        cbStatus.addItem("Aberta");
        cbStatus.addItem("Em andamento");
        cbStatus.addItem("Resolvida");
        panel.add(cbStatus);

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

    public JTextField getTxtData() {
        return txtData;
    }

    public JComboBox<String> getCbStatus() {
        return cbStatus;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

}