package view.combustivel;

import javax.swing.*;
import view.StyleUtils;

public class CombustivelDialog extends JDialog {
    private JComboBox<String> comboTipo;
    private JTextField txtLitros;
    private JTextField txtEstoqueMinimo;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public CombustivelDialog() { inicializar(); }

    private void inicializar() {
        setTitle("Cadastro de Combustível");
        StyleUtils.styleDialog(this, 460, 280);
        JPanel panel = StyleUtils.formPanel();
        comboTipo = new JComboBox<>();
        comboTipo.addItem("Diesel");
        comboTipo.addItem("Gasolina");
        comboTipo.addItem("Etanol");
        txtLitros = new JTextField();
        txtEstoqueMinimo = new JTextField();
        StyleUtils.addField(panel, 0, "Tipo:", comboTipo);
        StyleUtils.addField(panel, 1, "Litros em estoque:", txtLitros);
        StyleUtils.addField(panel, 2, "Estoque mínimo:", txtEstoqueMinimo);
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        add(panel, java.awt.BorderLayout.CENTER);
        add(StyleUtils.dialogButtons(btnSalvar, btnCancelar), java.awt.BorderLayout.SOUTH);
    }

    public JComboBox<String> getComboTipo() { return comboTipo; }
    public JTextField getTxtLitros() { return txtLitros; }
    public JTextField getTxtEstoqueMinimo() { return txtEstoqueMinimo; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
