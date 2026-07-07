package view.abastecimento;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import view.StyleUtils;

public class AbastecimentoDialog extends JDialog {
    private JComboBox<String> comboEquipamento;
    private JComboBox<String> comboCombustivel;
    private JTextField txtLitros;
    private JDateChooser dataChooser;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public AbastecimentoDialog() { inicializar(); }

    private void inicializar() {
        setTitle("Cadastro de Abastecimento");
        StyleUtils.styleDialog(this, 480, 310);
        JPanel panel = StyleUtils.formPanel();
        comboEquipamento = new JComboBox<>();
        comboCombustivel = new JComboBox<>();
        txtLitros = new JTextField();
        dataChooser = new JDateChooser();
        dataChooser.setDateFormatString("dd/MM/yyyy");
        StyleUtils.addField(panel, 0, "Equipamento:", comboEquipamento);
        StyleUtils.addField(panel, 1, "Combustível:", comboCombustivel);
        StyleUtils.addField(panel, 2, "Litros:", txtLitros);
        StyleUtils.addField(panel, 3, "Data:", dataChooser);
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        add(panel, java.awt.BorderLayout.CENTER);
        add(StyleUtils.dialogButtons(btnSalvar, btnCancelar), java.awt.BorderLayout.SOUTH);
    }

    public JComboBox<String> getComboEquipamento() { return comboEquipamento; }
    public JComboBox<String> getComboCombustivel() { return comboCombustivel; }
    public JTextField getTxtLitros() { return txtLitros; }
    public JDateChooser getDataChooser() { return dataChooser; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
