package view.manutencao;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import view.StyleUtils;

public class ManutencaoDialog extends JDialog {
    private JComboBox<String> cbEquipamento;
    private JTextField txtDescricao;
    private JTextField txtRevisaoAtual;
    private JTextField txtProximaRevisao;
    private JDateChooser dataChooser;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public ManutencaoDialog() { inicializar(); }

    private void inicializar() {
        setTitle("Cadastro de Manutenção");
        StyleUtils.styleDialog(this, 540, 380);
        JPanel panel = StyleUtils.formPanel();
        cbEquipamento = new JComboBox<>();
        txtDescricao = new JTextField();
        txtRevisaoAtual = new JTextField();
        txtProximaRevisao = new JTextField();
        dataChooser = new JDateChooser();
        dataChooser.setDateFormatString("dd/MM/yyyy");
        StyleUtils.addField(panel, 0, "Equipamento:", cbEquipamento);
        StyleUtils.addField(panel, 1, "Descrição:", txtDescricao);
        StyleUtils.addField(panel, 2, "Revisão Atual:", txtRevisaoAtual);
        StyleUtils.addField(panel, 3, "Próxima Revisão:", txtProximaRevisao);
        StyleUtils.addField(panel, 4, "Data:", dataChooser);
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        add(StyleUtils.contentScroll(panel), java.awt.BorderLayout.CENTER);
        add(StyleUtils.dialogButtons(btnSalvar, btnCancelar), java.awt.BorderLayout.SOUTH);
    }

    public JComboBox<String> getCbEquipamento() { return cbEquipamento; }
    public JTextField getTxtDescricao() { return txtDescricao; }
    public JTextField getTxtRevisaoAtual() { return txtRevisaoAtual; }
    public JTextField getTxtProximaRevisao() { return txtProximaRevisao; }
    public JDateChooser getDataChooser() { return dataChooser; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
