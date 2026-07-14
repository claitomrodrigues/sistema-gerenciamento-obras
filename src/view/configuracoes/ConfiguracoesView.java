package view.configuracoes;

import config.ConfiguracaoSistemaService;
import config.SistemaConfig;
import report.PrefeituraConfig;
import report.ReportConfig;
import ui.components.Notifications;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class ConfiguracoesView extends JPanel {

    private JTextField txtNomeSistema;
    private JTextField txtEstado;
    private JTextField txtPrefeitura;
    private JTextField txtSecretaria;
    private JTextField txtCnpj;
    private JTextField txtCidade;
    private JTextField txtEndereco;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtResponsavel;
    private JTextField txtCargo;
    private JTextField txtLogo;
    private JTextField txtPastaRelatorios;
    private JTextField txtPastaBackup;

    public ConfiguracoesView() {
        inicializar();
        carregar();
    }

    private void inicializar() {
        setLayout(new BorderLayout(0, 18));
        StyleUtils.styleView(this);
        add(StyleUtils.criarCabecalhoTela(
                "Configurações",
                "Dados institucionais, responsável, relatórios e rotinas de backup"), BorderLayout.NORTH);

        JPanel form = StyleUtils.formPanel();
        txtNomeSistema = campo();
        txtEstado = campo();
        txtPrefeitura = campo();
        txtSecretaria = campo();
        txtCnpj = campo();
        txtCidade = campo();
        txtEndereco = campo();
        txtTelefone = campo();
        txtEmail = campo();
        txtResponsavel = campo();
        txtCargo = campo();
        txtLogo = campo();
        txtPastaRelatorios = campo();
        txtPastaBackup = campo();

        int y = 0;
        StyleUtils.addField(form, y++, "Nome do sistema", txtNomeSistema);
        StyleUtils.addField(form, y++, "Estado", txtEstado);
        StyleUtils.addField(form, y++, "Prefeitura", txtPrefeitura);
        StyleUtils.addField(form, y++, "Secretaria", txtSecretaria);
        StyleUtils.addField(form, y++, "CNPJ", txtCnpj);
        StyleUtils.addField(form, y++, "Cidade", txtCidade);
        StyleUtils.addField(form, y++, "Endereço", txtEndereco);
        StyleUtils.addField(form, y++, "Telefone", txtTelefone);
        StyleUtils.addField(form, y++, "E-mail", txtEmail);
        StyleUtils.addField(form, y++, "Responsável", txtResponsavel);
        StyleUtils.addField(form, y++, "Cargo do responsável", txtCargo);
        StyleUtils.addField(form, y++, "Logomarca", campoComBotao(txtLogo, "Selecionar", () -> escolherLogo()));
        StyleUtils.addField(form, y++, "Pasta dos relatórios", campoComBotao(txtPastaRelatorios, "Selecionar", () -> escolherPasta(txtPastaRelatorios)));
        StyleUtils.addField(form, y++, "Pasta de backup", campoComBotao(txtPastaBackup, "Selecionar", () -> escolherPasta(txtPastaBackup)));

        JButton salvar = new JButton("Salvar configurações");
        StyleUtils.styleToolbarButton(salvar);
        salvar.addActionListener(e -> salvar());

        JButton recarregar = new JButton("Recarregar");
        StyleUtils.styleSecondaryButton(recarregar);
        recarregar.addActionListener(e -> carregar());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);
        botoes.add(recarregar);
        botoes.add(salvar);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(18, 6, 2, 6);
        form.add(botoes, gbc);

        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.add(form, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);
    }

    private JTextField campo() {
        JTextField c = new JTextField();
        c.setPreferredSize(new Dimension(480, 38));
        StyleUtils.prepareInput(c);
        return c;
    }

    private JPanel campoComBotao(JTextField campo, String texto, Runnable acao) {
        JPanel painel = new JPanel(new BorderLayout(8, 0));
        painel.setOpaque(false);
        painel.add(campo, BorderLayout.CENTER);
        JButton botao = new JButton(texto);
        StyleUtils.styleSecondaryButton(botao);
        botao.addActionListener(e -> acao.run());
        painel.add(botao, BorderLayout.EAST);
        return painel;
    }

    private void carregar() {
        PrefeituraConfig p = ReportConfig.getPrefeitura();
        txtNomeSistema.setText(SistemaConfig.getNomeSistema());
        txtEstado.setText(p.getEstado());
        txtPrefeitura.setText(p.getPrefeitura());
        txtSecretaria.setText(p.getSecretaria());
        txtCnpj.setText(p.getCnpj());
        txtCidade.setText(p.getCidade());
        txtEndereco.setText(p.getEndereco());
        txtTelefone.setText(p.getTelefone());
        txtEmail.setText(p.getEmail());
        txtResponsavel.setText(p.getNomeResponsavel());
        txtCargo.setText(p.getCargoResponsavel());
        txtLogo.setText(p.getCaminhoLogo());
        txtPastaRelatorios.setText(SistemaConfig.getPastaRelatorios());
        txtPastaBackup.setText(SistemaConfig.getPastaBackup());
    }

    private void salvar() {
        try {
            validarObrigatorios();
            PrefeituraConfig p = ReportConfig.getPrefeitura();
            SistemaConfig.setNomeSistema(txtNomeSistema.getText().trim());
            p.setEstado(txtEstado.getText());
            p.setPrefeitura(txtPrefeitura.getText());
            p.setSecretaria(txtSecretaria.getText());
            p.setCnpj(txtCnpj.getText());
            p.setCidade(txtCidade.getText());
            p.setEndereco(txtEndereco.getText());
            p.setTelefone(txtTelefone.getText());
            p.setEmail(txtEmail.getText());
            p.setNomeResponsavel(txtResponsavel.getText());
            p.setCargoResponsavel(txtCargo.getText());
            p.setCaminhoLogo(txtLogo.getText());
            SistemaConfig.setPastaRelatorios(txtPastaRelatorios.getText().trim());
            SistemaConfig.setPastaBackup(txtPastaBackup.getText().trim());

            validarDiretorios();
            ConfiguracaoSistemaService.salvar();
            Notifications.success(this, "Configurações salvas permanentemente.");
        } catch (Exception e) {
            Notifications.error(this, e.getMessage());
        }
    }

    private void validarObrigatorios() {
        if (txtNomeSistema.getText().isBlank()) throw new IllegalArgumentException("Informe o nome do sistema.");
        if (txtPrefeitura.getText().isBlank()) throw new IllegalArgumentException("Informe o nome da prefeitura.");
        if (txtSecretaria.getText().isBlank()) throw new IllegalArgumentException("Informe o nome da secretaria.");
        if (txtCidade.getText().isBlank()) throw new IllegalArgumentException("Informe a cidade.");
    }

    private void validarDiretorios() {
        SistemaConfig.getPastaRelatorios();
        SistemaConfig.getPastaBackup();
        String logo = txtLogo.getText().trim();
        if (!logo.isBlank() && !new File(logo).isFile()) {
            throw new IllegalArgumentException("A logomarca informada não foi encontrada.");
        }
    }

    private void escolherLogo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecionar logomarca");
        chooser.setFileFilter(new FileNameExtensionFilter("Imagens PNG, JPG e JPEG", "png", "jpg", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtLogo.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void escolherPasta(JTextField destino) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecionar pasta");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String atual = destino.getText().trim();
        if (!atual.isBlank()) chooser.setCurrentDirectory(new File(atual));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            destino.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
}
