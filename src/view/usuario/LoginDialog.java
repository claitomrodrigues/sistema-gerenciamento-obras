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
    private final JButton btnEntrar = new JButton("Entrar no sistema");
    private final JButton btnSair = new JButton("Fechar");
    private char caractereSenha;

    public LoginDialog() {
        setTitle("Acesso ao Sistema de Obras");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setUndecorated(false);
        montar();
        pack();
        setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> txtLogin.requestFocusInWindow());
    }

    private void montar() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BACKGROUND);
        root.setPreferredSize(new Dimension(920, 550));
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
        painel.setPreferredSize(new Dimension(390, 550));
        painel.setLayout(new BorderLayout());
        painel.setBorder(new EmptyBorder(42, 40, 34, 40));

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));

        JLabel logo = criarLogo();
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel produto = new JLabel("Obras & Frota");
        produto.setAlignmentX(Component.LEFT_ALIGNMENT);
        produto.setForeground(Color.WHITE);
        produto.setFont(new Font("Segoe UI", Font.BOLD, 32));

        JLabel categoria = new JLabel("GESTÃO MUNICIPAL");
        categoria.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoria.setForeground(new Color(191, 219, 254));
        categoria.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JTextArea descricao = new JTextArea(
                "Controle operacional da Secretaria de Obras em um ambiente seguro, organizado e fácil de usar."
        );
        descricao.setAlignmentX(Component.LEFT_ALIGNMENT);
        descricao.setOpaque(false);
        descricao.setEditable(false);
        descricao.setFocusable(false);
        descricao.setLineWrap(true);
        descricao.setWrapStyleWord(true);
        descricao.setForeground(new Color(226, 232, 240));
        descricao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descricao.setMaximumSize(new Dimension(300, 86));

        topo.add(logo);
        topo.add(Box.createVerticalStrut(24));
        topo.add(categoria);
        topo.add(Box.createVerticalStrut(7));
        topo.add(produto);
        topo.add(Box.createVerticalStrut(22));
        topo.add(descricao);

        JPanel recursos = new JPanel();
        recursos.setOpaque(false);
        recursos.setLayout(new BoxLayout(recursos, BoxLayout.Y_AXIS));
        recursos.add(criarRecurso("Controle centralizado", "Frota, combustível, manutenção e serviços."));
        recursos.add(Box.createVerticalStrut(18));
        recursos.add(criarRecurso("Informação gerencial", "Indicadores, alertas e relatórios em PDF."));
        recursos.add(Box.createVerticalStrut(18));
        recursos.add(criarRecurso("Segurança operacional", "Usuários, permissões, auditoria e backup."));

        JLabel rodape = new JLabel("Secretaria Municipal de Obras");
        rodape.setForeground(new Color(148, 163, 184));
        rodape.setFont(Theme.SMALL);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(recursos, BorderLayout.CENTER);
        painel.add(rodape, BorderLayout.SOUTH);
        return painel;
    }

    private JLabel criarLogo() {
        URL recurso = LoginDialog.class.getResource("/img/logo.png");
        if (recurso != null) {
            ImageIcon original = new ImageIcon(recurso);
            Image imagem = original.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(imagem));
            label.setPreferredSize(new Dimension(70, 70));
            return label;
        }

        JLabel fallback = new JLabel("OS", SwingConstants.CENTER);
        fallback.setOpaque(true);
        fallback.setBackground(new Color(255, 255, 255, 28));
        fallback.setForeground(Color.WHITE);
        fallback.setFont(new Font("Segoe UI", Font.BOLD, 24));
        fallback.setPreferredSize(new Dimension(64, 64));
        fallback.setMaximumSize(new Dimension(64, 64));
        return fallback;
    }

    private JComponent criarRecurso(String titulo, String texto) {
        JPanel item = new JPanel(new BorderLayout(14, 0));
        item.setOpaque(false);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(310, 58));

        JLabel indicador = new JLabel("OK", SwingConstants.CENTER);
        indicador.setOpaque(true);
        indicador.setBackground(new Color(255, 255, 255, 24));
        indicador.setForeground(new Color(191, 219, 254));
        indicador.setFont(new Font("Segoe UI", Font.BOLD, 16));
        indicador.setPreferredSize(new Dimension(36, 36));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblTexto = new JLabel(texto);
        lblTexto.setForeground(new Color(203, 213, 225));
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(3));
        textos.add(lblTexto);

        item.add(indicador, BorderLayout.WEST);
        item.add(textos, BorderLayout.CENTER);
        return item;
    }

    private JComponent criarAreaLogin() {
        JPanel area = new JPanel(new GridBagLayout());
        area.setBackground(Theme.BACKGROUND);
        area.setBorder(new EmptyBorder(34, 48, 34, 48));

        RoundedCard card = new RoundedCard();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(420, 460));
        card.setBorder(new EmptyBorder(40, 42, 30, 42));

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel selo = new JLabel("ACESSO SEGURO");
        selo.setForeground(Theme.PRIMARY);
        selo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        selo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Bem-vindo de volta");
        titulo.setForeground(Theme.TEXT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Informe suas credenciais para continuar.");
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
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnEntrar.setPreferredSize(new Dimension(320, 46));
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyleUtils.styleSecondaryButton(btnSair);
        btnSair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnSair.setPreferredSize(new Dimension(320, 44));
        btnSair.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(selo);
        form.add(Box.createVerticalStrut(8));
        form.add(titulo);
        form.add(Box.createVerticalStrut(7));
        form.add(subtitulo);
        form.add(Box.createVerticalStrut(30));
        form.add(loginLabel);
        form.add(Box.createVerticalStrut(7));
        form.add(txtLogin);
        form.add(Box.createVerticalStrut(17));
        form.add(senhaLabel);
        form.add(Box.createVerticalStrut(7));
        form.add(txtSenha);
        form.add(Box.createVerticalStrut(9));
        form.add(chkMostrarSenha);
        form.add(Box.createVerticalStrut(24));
        form.add(btnEntrar);
        form.add(Box.createVerticalStrut(10));
        form.add(btnSair);

        JLabel suporte = new JLabel("Sistema de uso interno - Acesso restrito");
        suporte.setForeground(new Color(148, 163, 184));
        suporte.setFont(Theme.SMALL);
        suporte.setHorizontalAlignment(SwingConstants.CENTER);
        suporte.setBorder(new EmptyBorder(18, 0, 0, 0));

        card.add(form, BorderLayout.CENTER);
        card.add(suporte, BorderLayout.SOUTH);
        area.add(card);
        return area;
    }

    private void configurarCampo(JTextField campo, String nomeAcessivel) {
        StyleUtils.prepareInput(campo);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        campo.setPreferredSize(new Dimension(320, 44));
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
            if (login.isBlank()) txtLogin.requestFocusInWindow();
            else txtSenha.requestFocusInWindow();
            return;
        }

        btnEntrar.setEnabled(false);
        btnEntrar.setText("Validando acesso...");
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
            btnEntrar.setText("Entrar no sistema");
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
            g2.fillOval(getWidth() - 180, -80, 260, 260);
            g2.fillOval(-120, getHeight() - 130, 250, 250);
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
