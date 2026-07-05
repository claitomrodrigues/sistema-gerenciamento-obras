package view.combustivel;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CombustivelDialog extends JDialog {

    private JComboBox<String> comboTipo;
    private JTextField txtLitros;
    private JTextField txtEstoqueMinimo;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public CombustivelDialog() {
        inicializar();
    }

    private void inicializar() {
        setTitle("Cadastro de Combustível");
        setSize(400, 250);
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>();
        comboTipo.addItem("Diesel");
        comboTipo.addItem("Gasolina");
        comboTipo.addItem("Etanol");
        panel.add(comboTipo);

        panel.add(new JLabel("Litros em estoque:"));
        txtLitros = new JTextField();
        panel.add(txtLitros);

        panel.add(new JLabel("Estoque mínimo:"));
        txtEstoqueMinimo = new JTextField();
        panel.add(txtEstoqueMinimo);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);
    }

    public JComboBox<String> getComboTipo() {
        return comboTipo;
    }

    public JTextField getTxtLitros() {
        return txtLitros;
    }

    public JTextField getTxtEstoqueMinimo() {
        return txtEstoqueMinimo;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }
}