package view.configuracoes;

import config.SistemaConfig;
import report.PrefeituraConfig;
import report.ReportConfig;

import javax.swing.*;
import java.awt.*;

public class ConfiguracoesView extends JPanel {

    private JTextField txtEstado;
    private JTextField txtPrefeitura;
    private JTextField txtSecretaria;
    private JTextField txtCidade;
    private JTextField txtEndereco;
    private JTextField txtLogo;
    private JTextField txtPastaRelatorios;
    private JTextField txtPastaBackup;

    public ConfiguracoesView() {
        inicializar();
        carregar();
    }

    private void inicializar() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Configurações da Prefeitura");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 235)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        txtEstado = campo();
        txtPrefeitura = campo();
        txtSecretaria = campo();
        txtCidade = campo();
        txtEndereco = campo();
        txtLogo = campo();
        txtPastaRelatorios = campo();
        txtPastaBackup = campo();

        int y = 0;
        addLinha(form, y++, "Estado:", txtEstado);
        addLinha(form, y++, "Prefeitura:", txtPrefeitura);
        addLinha(form, y++, "Secretaria:", txtSecretaria);
        addLinha(form, y++, "Cidade:", txtCidade);
        addLinha(form, y++, "Endereço:", txtEndereco);
        addLinha(form, y++, "Caminho da logo:", txtLogo);
        addLinha(form, y++, "Pasta dos relatórios:", txtPastaRelatorios);
        addLinha(form, y++, "Pasta de backup:", txtPastaBackup);

        JButton salvar = new JButton("Salvar configurações");
        salvar.addActionListener(e -> salvar());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(12, 6, 6, 6);
        form.add(salvar, gbc);

        add(form, BorderLayout.CENTER);
    }

    private JTextField campo() {
        JTextField c = new JTextField();
        c.setPreferredSize(new Dimension(420, 32));
        return c;
    }

    private void addLinha(JPanel form, int y, String label, JTextField campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        form.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(campo, gbc);
    }

    private void carregar() {
        PrefeituraConfig p = ReportConfig.getPrefeitura();
        txtEstado.setText(p.getEstado());
        txtPrefeitura.setText(p.getPrefeitura());
        txtSecretaria.setText(p.getSecretaria());
        txtCidade.setText(p.getCidade());
        txtEndereco.setText(p.getEndereco());
        txtLogo.setText(p.getCaminhoLogo());
        txtPastaRelatorios.setText(SistemaConfig.getPastaRelatorios());
        txtPastaBackup.setText(SistemaConfig.getPastaBackup());
    }

    private void salvar() {
        PrefeituraConfig p = ReportConfig.getPrefeitura();
        p.setEstado(txtEstado.getText());
        p.setPrefeitura(txtPrefeitura.getText());
        p.setSecretaria(txtSecretaria.getText());
        p.setCidade(txtCidade.getText());
        p.setEndereco(txtEndereco.getText());
        p.setCaminhoLogo(txtLogo.getText());
        SistemaConfig.setPastaRelatorios(txtPastaRelatorios.getText());
        SistemaConfig.setPastaBackup(txtPastaBackup.getText());
        JOptionPane.showMessageDialog(this, "Configurações aplicadas nesta execução do sistema.");
    }
}
