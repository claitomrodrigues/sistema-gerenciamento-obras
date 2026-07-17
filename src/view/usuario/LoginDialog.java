package view.usuario;

import security.UsuarioService;
import ui.components.Notifications;
import ui.theme.Theme;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class LoginDialog extends JDialog {
    private boolean autenticado;

    private final JTextField txtLogin = new JTextField(22);
    private final JPasswordField txtSenha = new JPasswordField(22);
    private final JCheckBox chkMostrarSenha = new JCheckBox("Mostrar senha");
    private final JButton btnEntrar = new JButton("Entrar");
    private final JButton btnSair = new JButton("Fechar");
    private char caractereSenha;

    public LoginDialog() {
        setTitle("Acesso ao Sistema de Obras");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        montar();
        pack();
        setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> txtLogin.requestFocusInWindow());
    }

    private void montar() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BACKGROUND);
        root.setPreferredSize(new Dimension(920, 560));
        root.add(criarPainelInstitucional(), BorderLayout.WEST);
        root.add(criarAreaLogin(), BorderLayout.CENTER);
        setContentPane(root);

        btnEntrar.addActionListener(e -> autenticar());
        btnSair.addActionListener(e -> dispose());
        chkMostrarSenha.addActionListener(e -> alternarVisualizacaoSenha());

        caractereSenha = txtSenha.getEchoChar();
        getRootPane().setDefaultButton(btnEntrar);
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    private JComponent criarPainelInstitucional() {
        GradientPanel painel = new GradientPanel();
        painel.setPreferredSize(new Dimension(370, 560));
        painel.setLayout(new BorderLayout());
        painel.setBorder(new EmptyBorder(44, 42, 34, 42));

        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));

        JLabel logo = criarLogo();
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoria = new JLabel("GESTÃO MUNICIPAL");
        categoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoria.setForeground(new Color(191, 219, 254));
        categoria.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel produto = new JLabel("Obras & Frota");
        produto.setAlignmentX(Component.LEFT_ALIGNMENT);
        produto.setForeground(Color.WHITE);
        produto.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JSeparator separador = new JSeparator();
        separador.setAlignmentX(Component.LEFT_ALIGNMENT);
        separador.setForeground(new Color(255, 255, 255, 42));
        separador.setBackground(new Color(255, 255, 255, 42));
        separador.setMaximumSize(new Dimension(250, 1));

        JLabel secretaria = new JLabel("Secretaria Municipal de Obras");
        secretaria.setAlignmentX(Component.LEFT_ALIGNMENT);
        secretaria.setForeground(new Color(226, 232, 240));
        secretaria.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel local = new JLabel("São Vicente do Sul - RS");
        local.setAlignmentX(Component.LEFT_ALIGNMENT);
        local.setForeground(new Color(148, 163, 184));
        local.setFont(Theme.SMALL);

        conteudo.add(logo);
        conteudo.add(Box.createVerticalStrut(28));
        conteudo.add(categoria);
        conteudo.add(Box.createVerticalStrut(8));
        conteudo.add(produto);
        conteudo.add(Box.createVerticalStrut(25));
        conteudo.add(separador);
        conteudo.add(Box.createVerticalStrut(22));
        conteudo.add(secretaria);
        conteudo.add(Box.createVerticalStrut(6));
        conteudo.add(local);

        JLabel rodape = new JLabel("Acesso institucional");
        rodape.setForeground(new Color(148, 163, 184));
        rodape.setFont(Theme.SMALL);

        painel.add(conteudo, BorderLayout.NORTH);
        painel.add(rodape, BorderLayout.SOUTH);
        return painel;
    }

    private JLabel criarLogo() {
        URL recurso = LoginDialog.class.getResource("/img/logo.png");
        if (recurso != null) {
            ImageIcon original = new ImageIcon(recurso);
            Image imagem = original.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(imagem));
            label.setPreferredSize(new Dimension(78, 78));
            label.setMaximumSize(new Dimension(78, 78));
            return label;
        }

        JLabel fallback = new JLabel("OS", SwingConstants.CENTER);
        fallback.setOpaque(true);
        fallback.setBackground(new Color(255, 255, 255, 28));
        fallback.setForeground(Color.WHITE);
        fallback.setFont(new Font("Segoe UI", Font.BOLD, 24));
        fallback.setPreferredSize(new Dimension(72, 72));
        fallback.setMaximumSize(new Dimension(72, 72));
        return fallback;
    }

    private JComponent criarAreaLogin() {
        JPanel area = new JPanel(new GridBagLayout());
        area.setBackground(Theme.BACKGROUND);
        area.setBorder(new EmptyBorder(28, 48, 28, 48));

        RoundedCard card = new RoundedCard();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(420, 490));
        card.setBorder(new EmptyBorder(38, 42, 34, 42));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Acesse sua conta");
        titulo.setForeground(Theme.TEXT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Informe seu usuário e sua senha para continuar.");
        subtitulo.setForeground(Theme.MUTED);
        subtitulo.setFont(Theme.FONT);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        configurarCampo(txtLogin, "Usuário");
        configurarCampo(txtSenha, "Senha");

        JLabel loginLabel = criarLabelCampo("Usuário");
        JLabel senhaLabel = criarLabelCampo("Senha");

        chkMostrarSenha.setOpaque(false);
        chkMostrarSenha.setForeground(Theme.MUTED);
        chkMostrarSenha.setFont(Theme.SMALL);
        chkMostrarSenha.setFocusPainted(false);
        chkMostrarSenha.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyleUtils.styleToolbarButton(btnEntrar);
        btnEntrar.setMinimumSize(new Dimension(320, 48));
        btnEntrar.setPreferredSize(new Dimension(320, 48));
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyleUtils.styleSecondaryButton(btnSair);
        btnSair.setMinimumSize(new Dimension(320, 44));
        btnSair.setPreferredSize(new Dimension(320, 44));
        btnSair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnSair.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(titulo);
        form.add(Box.createVerticalStrut(8));
        form.add(subtitulo);
        form.add(Box.createVerticalStrut(30));
        form.add(loginLabel);
        form.add(Box.createVerticalStrut(7));
        form.add(txtLogin);
        form.add(Box.createVerticalStrut(17));
        form.add(senhaLabel);
        form.add(Box.createVerticalStrut(7));
        form.add(txtSenha);
        form.add(Box.createVerticalStrut(10));
        form.add(chkMostrarSenha);
        form.add(Box.createVerticalStrut(25));
        form.add(btnEntrar);
        form.add(Box.createVerticalStrut(11));
        form.add(btnSair);

        card.add(form, BorderLayout.CENTER);
        area.add(card);
        return area;
    }

    private void configurarCampo(JTextField campo, String nomeAcessivel) {
        StyleUtils.prepareInput(campo);
        campo.setMinimumSize(new Dimension(320, 44));
        campo.setPreferredSize(new Dimension(320, 44));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.getAccessibleContext().setAccessibleName(nomeAcessivel);
    }

    private JLabel criarLabelCampo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Theme.TEXT);
        label.setFont(Theme.FONT_BOLD);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void alternarVisualizacaoSenha() {
        txtSenha.setEchoChar(chkMostrarSenha.isSelected() ? (char) 0 : caractereSenha);
        txtSenha.requestFocusInWindow();
    }

    private void autenticar() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (login.isBlank() || senha.isBlank()) {
            Notifications.warning(this, "Informe o usuário e a senha.");
            if (login.isBlank()) {
                txtLogin.requestFocusInWindow();
            } else {
                txtSenha.requestFocusInWindow();
            }
            return;
        }

        btnEntrar.setEnabled(false);
        btnEntrar.setText("Validando...");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            if (UsuarioService.autenticar(login, senha)) {
                autenticado = true;
                dispose();
            } else {
                txtSenha.selectAll();
                txtSenha.requestFocusInWindow();
                Notifications.error(this, "Login ou senha inválidos.");
            }
        } finally {
            setCursor(Cursor.getDefaultCursor());
            btnEntrar.setEnabled(true);
            btnEntrar.setText("Entrar");
        }
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    private static final class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(15, 23, 42),
                    getWidth(), getHeight(), new Color(30, 64, 175)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 10));
            g2.fillOval(getWidth() - 170, -80, 250, 250);
            g2.fillOval(-125, getHeight() - 135, 255, 255);
            g2.dispose();
        }
    }

    private static final class RoundedCard extends JPanel {
        private RoundedCard() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape sombra = new RoundRectangle2D.Double(4, 7, getWidth() - 9, getHeight() - 11, 24, 24);
            g2.setColor(new Color(15, 23, 42, 18));
            g2.fill(sombra);

            Shape fundo = new RoundRectangle2D.Double(0, 0, getWidth() - 9, getHeight() - 11, 24, 24);
            g2.setColor(Color.WHITE);
            g2.fill(fundo);
            g2.setColor(Theme.BORDER);
            g2.draw(fundo);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
