package view.equipamento;

import javax.swing.*;
import view.StyleUtils;

public class EquipamentoDialog extends JDialog {
    private JTextField txtNome;
    private JTextField txtTipo;
    private JTextField txtPlaca;
    private JTextField txtKmAtual;
    private JTextField txtHorasUso;
    private JCheckBox chkAtivo;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public EquipamentoDialog() { inicializar(); }

    private void inicializar() {
        setTitle("Cadastro de Equipamento");
        StyleUtils.styleDialog(this, 500, 410);
        JPanel panel = StyleUtils.formPanel();
        txtNome = new JTextField();
        txtTipo = new JTextField();
        txtPlaca = new JTextField();
        txtKmAtual = new JTextField();
        txtHorasUso = new JTextField();
        chkAtivo = new JCheckBox("Equipamento ativo");
        chkAtivo.setOpaque(false);
        chkAtivo.setSelected(true);
        StyleUtils.addField(panel, 0, "Nome:", txtNome);
        StyleUtils.addField(panel, 1, "Tipo:", txtTipo);
        StyleUtils.addField(panel, 2, "Placa:", txtPlaca);
        StyleUtils.addField(panel, 3, "KM Atual:", txtKmAtual);
        StyleUtils.addField(panel, 4, "Horas de Uso:", txtHorasUso);
        StyleUtils.addField(panel, 5, "Status:", chkAtivo);
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        add(panel, java.awt.BorderLayout.CENTER);
        add(StyleUtils.dialogButtons(btnSalvar, btnCancelar), java.awt.BorderLayout.SOUTH);
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
