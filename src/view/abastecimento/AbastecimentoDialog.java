package view.abastecimento;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AbastecimentoDialog extends JDialog {

    private JComboBox<String> comboEquipamento;
    private JComboBox<String> comboCombustivel;
    private JTextField txtLitros;
    private JTextField txtData;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public AbastecimentoDialog() {
        inicializar();
    }

    private void inicializar() {
        setTitle("Cadastro de Abastecimento");
        setSize(450, 300);
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Equipamento:"));
        comboEquipamento = new JComboBox<>();
        panel.add(comboEquipamento);

        panel.add(new JLabel("Combustível:"));
        comboCombustivel = new JComboBox<>();
        panel.add(comboCombustivel);

        panel.add(new JLabel("Litros:"));
        txtLitros = new JTextField();
        panel.add(txtLitros);

        panel.add(new JLabel("Data:"));
        txtData = new JTextField();
        panel.add(txtData);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);
    }

    public JComboBox<String> getComboEquipamento() {
        return comboEquipamento;
    }

    public JComboBox<String> getComboCombustivel() {
        return comboCombustivel;
    }

    public JTextField getTxtLitros() {
        return txtLitros;
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