package view.equipamento;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EquipamentoDialog extends JDialog {

    private JTextField txtNome;
    private JTextField txtTipo;
    private JTextField txtPlaca;
    private JTextField txtKmAtual;
    private JTextField txtHorasUso;
    private JCheckBox chkAtivo;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public EquipamentoDialog() {
        inicializar();
    }

    private void inicializar() {
        setTitle("Cadastro de Equipamento");
        setSize(450, 320);
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        panel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        panel.add(txtNome);

        panel.add(new JLabel("Tipo:"));
        txtTipo = new JTextField();
        panel.add(txtTipo);

        panel.add(new JLabel("Placa:"));
        txtPlaca = new JTextField();
        panel.add(txtPlaca);

        panel.add(new JLabel("KM Atual:"));
        txtKmAtual = new JTextField();
        panel.add(txtKmAtual);

        panel.add(new JLabel("Horas de Uso:"));
        txtHorasUso = new JTextField();
        panel.add(txtHorasUso);

        panel.add(new JLabel("Ativo:"));
        chkAtivo = new JCheckBox();
        panel.add(chkAtivo);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        add(panel);
    }

    public JTextField getTxtNome() { return txtNome; }
    public JTextField getTxtTipo() { return txtTipo; }
    public JTextField getTxtPlaca() { return txtPlaca; }
    public JTextField getTxtKmAtual() { return txtKmAtual; }
    public JTextField getTxtHorasUso() { return txtHorasUso; }
    public JCheckBox getChkAtivo() { return chkAtivo; }

    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}