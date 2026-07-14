package view.empenho;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import view.StyleUtils;

public class EmpenhoDialog extends JDialog {
    private JTextField txtNumero;
    private JTextField txtDescricao;
    private JTextField txtCategoria;
    private JTextField txtValor;
    private JTextField txtSaldo;
    private JTextField txtFornecedor;
    private JDateChooser dataChooser;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public EmpenhoDialog() { inicializar(); }

    private void inicializar() {
        setTitle("Cadastro de Empenho");
        StyleUtils.styleDialog(this, 540, 450);
        JPanel panel = StyleUtils.formPanel();
        txtNumero = new JTextField();
        txtDescricao = new JTextField();
        txtCategoria = new JTextField();
        txtValor = new JTextField();
        txtSaldo = new JTextField();
        txtFornecedor = new JTextField();
        dataChooser = new JDateChooser();
        dataChooser.setDateFormatString("dd/MM/yyyy");
        StyleUtils.addField(panel, 0, "Número:", txtNumero);
        StyleUtils.addField(panel, 1, "Descrição:", txtDescricao);
        StyleUtils.addField(panel, 2, "Categoria:", txtCategoria);
        StyleUtils.addField(panel, 3, "Valor:", txtValor);
        StyleUtils.addField(panel, 4, "Saldo:", txtSaldo);
        StyleUtils.addField(panel, 5, "Fornecedor:", txtFornecedor);
        StyleUtils.addField(panel, 6, "Data:", dataChooser);
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        add(StyleUtils.contentScroll(panel), java.awt.BorderLayout.CENTER);
        add(StyleUtils.dialogButtons(btnSalvar, btnCancelar), java.awt.BorderLayout.SOUTH);
    }

    public JTextField getTxtNumero() { return txtNumero; }
    public JTextField getTxtDescricao() { return txtDescricao; }
    public JTextField getTxtCategoria() { return txtCategoria; }
    public JTextField getTxtValor() { return txtValor; }
    public JTextField getTxtSaldo() { return txtSaldo; }
    public JTextField getTxtFornecedor() { return txtFornecedor; }
    public JDateChooser getDataChooser() { return dataChooser; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
