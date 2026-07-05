package view.licitacao;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LicitacaoDialog extends JDialog {

    private JTextField txtNumero;
    private JTextField txtObjeto;
    private JTextField txtModalidade;
    private JTextField txtEmpresa;
    private JTextField txtValor;
    private JTextField txtData;

    private JComboBox<String> cbEmpenho;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public LicitacaoDialog() {

        setTitle("Cadastro de Licitação");
        setSize(500, 380);
        setLocationRelativeTo(null);
        setModal(true);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

        panel.add(new JLabel("Número:"));
        txtNumero = new JTextField();
        panel.add(txtNumero);

        panel.add(new JLabel("Objeto:"));
        txtObjeto = new JTextField();
        panel.add(txtObjeto);

        panel.add(new JLabel("Modalidade:"));
        txtModalidade = new JTextField();
        panel.add(txtModalidade);

        panel.add(new JLabel("Empresa Vencedora:"));
        txtEmpresa = new JTextField();
        panel.add(txtEmpresa);

        panel.add(new JLabel("Valor:"));
        txtValor = new JTextField();
        panel.add(txtValor);

        panel.add(new JLabel("Data:"));
        txtData = new JTextField();
        panel.add(txtData);

        panel.add(new JLabel("Empenho:"));
        cbEmpenho = new JComboBox<>();
        panel.add(cbEmpenho);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);

    }

    public JTextField getTxtNumero() {
        return txtNumero;
    }

    public JTextField getTxtObjeto() {
        return txtObjeto;
    }

    public JTextField getTxtModalidade() {
        return txtModalidade;
    }

    public JTextField getTxtEmpresa() {
        return txtEmpresa;
    }

    public JTextField getTxtValor() {
        return txtValor;
    }

    public JTextField getTxtData() {
        return txtData;
    }

    public JComboBox<String> getCbEmpenho() {
        return cbEmpenho;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

}