package view.ocorrencia;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import view.StyleUtils;

public class OcorrenciaDialog extends JDialog {
    private JComboBox<String> cbEquipamento;
    private JTextField txtDescricao;
    private JDateChooser dataChooser;
    private JComboBox<String> cbStatus;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public OcorrenciaDialog() { inicializar(); }

    private void inicializar() {
        setTitle("Cadastro de Ocorrência");
        StyleUtils.styleDialog(this, 540, 340);
        JPanel panel = StyleUtils.formPanel();
        cbEquipamento = new JComboBox<>();
        txtDescricao = new JTextField();
        dataChooser = new JDateChooser();
        dataChooser.setDateFormatString("dd/MM/yyyy");
        cbStatus = new JComboBox<>();
        cbStatus.addItem("Aberta");
        cbStatus.addItem("Em andamento");
        cbStatus.addItem("Resolvida");
        StyleUtils.addField(panel, 0, "Equipamento:", cbEquipamento);
        StyleUtils.addField(panel, 1, "Descrição:", txtDescricao);
        StyleUtils.addField(panel, 2, "Data:", dataChooser);
        StyleUtils.addField(panel, 3, "Status:", cbStatus);
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        add(panel, java.awt.BorderLayout.CENTER);
        add(StyleUtils.dialogButtons(btnSalvar, btnCancelar), java.awt.BorderLayout.SOUTH);
    }

    public JComboBox<String> getCbEquipamento() { return cbEquipamento; }
    public JTextField getTxtDescricao() { return txtDescricao; }
    public JDateChooser getDataChooser() { return dataChooser; }
    public JComboBox<String> getCbStatus() { return cbStatus; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
