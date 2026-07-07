package view.licitacao;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import view.StyleUtils;

public class LicitacaoDialog extends JDialog {
    private JTextField txtNumero;
    private JTextField txtObjeto;
    private JTextField txtModalidade;
    private JTextField txtEmpresa;
    private JTextField txtValor;
    private JDateChooser dataChooser;
    private JComboBox<String> cbEmpenho;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public LicitacaoDialog() { inicializar(); }

    private void inicializar() {
        setTitle("Cadastro de Licitação");
        StyleUtils.styleDialog(this, 560, 460);
        JPanel panel = StyleUtils.formPanel();
        txtNumero = new JTextField();
        txtObjeto = new JTextField();
        txtModalidade = new JTextField();
        txtEmpresa = new JTextField();
        txtValor = new JTextField();
        dataChooser = new JDateChooser();
        dataChooser.setDateFormatString("dd/MM/yyyy");
        cbEmpenho = new JComboBox<>();
        StyleUtils.addField(panel, 0, "Número:", txtNumero);
        StyleUtils.addField(panel, 1, "Objeto:", txtObjeto);
        StyleUtils.addField(panel, 2, "Modalidade:", txtModalidade);
        StyleUtils.addField(panel, 3, "Empresa Vencedora:", txtEmpresa);
        StyleUtils.addField(panel, 4, "Valor:", txtValor);
        StyleUtils.addField(panel, 5, "Data:", dataChooser);
        StyleUtils.addField(panel, 6, "Empenho:", cbEmpenho);
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        add(panel, java.awt.BorderLayout.CENTER);
        add(StyleUtils.dialogButtons(btnSalvar, btnCancelar), java.awt.BorderLayout.SOUTH);
    }

    public JTextField getTxtNumero() { return txtNumero; }
    public JTextField getTxtObjeto() { return txtObjeto; }
    public JTextField getTxtModalidade() { return txtModalidade; }
    public JTextField getTxtEmpresa() { return txtEmpresa; }
    public JTextField getTxtValor() { return txtValor; }
    public JDateChooser getDataChooser() { return dataChooser; }
    public JComboBox<String> getCbEmpenho() { return cbEmpenho; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
