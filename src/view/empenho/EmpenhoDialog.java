package view.empenho;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EmpenhoDialog extends JDialog {

    private JTextField txtNumero;
    private JTextField txtDescricao;
    private JTextField txtCategoria;
    private JTextField txtValor;
    private JTextField txtSaldo;
    private JTextField txtFornecedor;
    private JTextField txtData;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public EmpenhoDialog() {
        inicializar();
    }

    private void inicializar() {
        setTitle("Cadastro de Empenho");
        setSize(500, 350);
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        panel.add(new JLabel("Número:"));
        txtNumero = new JTextField();
        panel.add(txtNumero);

        panel.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField();
        panel.add(txtDescricao);

        panel.add(new JLabel("Categoria:"));
        txtCategoria = new JTextField();
        panel.add(txtCategoria);

        panel.add(new JLabel("Valor:"));
        txtValor = new JTextField();
        panel.add(txtValor);

        panel.add(new JLabel("Saldo:"));
        txtSaldo = new JTextField();
        panel.add(txtSaldo);

        panel.add(new JLabel("Fornecedor:"));
        txtFornecedor = new JTextField();
        panel.add(txtFornecedor);

        panel.add(new JLabel("Data:"));
        txtData = new JTextField();
        panel.add(txtData);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);
    }

    public JTextField getTxtNumero() { return txtNumero; }
    public JTextField getTxtDescricao() { return txtDescricao; }
    public JTextField getTxtCategoria() { return txtCategoria; }
    public JTextField getTxtValor() { return txtValor; }
    public JTextField getTxtSaldo() { return txtSaldo; }
    public JTextField getTxtFornecedor() { return txtFornecedor; }
    public JTextField getTxtData() { return txtData; }

    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}