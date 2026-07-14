package view;

import com.toedter.calendar.JDateChooser;
import ui.components.Notifications;
import ui.components.RoundedPanel;
import ui.components.SearchUtils;
import ui.theme.Theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;

public final class StyleUtils {
    public static final Color PRIMARY = Theme.PRIMARY;
    public static final Color PRIMARY_DARK = Theme.PRIMARY_DARK;
    public static final Color BACKGROUND = Theme.BACKGROUND;
    public static final Color CARD = Theme.CARD;
    public static final Color TEXT = Theme.TEXT;
    public static final Color BORDER = Theme.BORDER;
    public static final Color DANGER = Theme.DANGER;
    public static final Color DANGER_DARK = Theme.DANGER_DARK;
    public static final Color SUCCESS = Theme.SUCCESS;
    public static final Font FONT = Theme.FONT;
    public static final Font FONT_BOLD = Theme.FONT_BOLD;
    public static final Font TITLE_FONT = Theme.TITLE;

    private StyleUtils() {}

    public static void aplicarTema() {
        UIManager.put("Label.font", FONT);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("TextField.font", FONT);
        UIManager.put("PasswordField.font", FONT);
        UIManager.put("FormattedTextField.font", FONT);
        UIManager.put("TextArea.font", FONT);
        UIManager.put("ComboBox.font", FONT);
        UIManager.put("CheckBox.font", FONT);
        UIManager.put("Table.font", FONT);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("OptionPane.messageFont", FONT);
        UIManager.put("OptionPane.buttonFont", FONT_BOLD);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ScrollPane.background", BACKGROUND);
        UIManager.put("TabbedPane.font", FONT_BOLD);
        UIManager.put("ToolTip.font", Theme.SMALL);
        UIManager.put("ToolTip.background", Theme.TEXT);
        UIManager.put("ToolTip.foreground", Color.WHITE);
    }

    public static void styleView(JComponent component) {
        component.setBackground(BACKGROUND);
        component.setBorder(new EmptyBorder(26, 28, 26, 28));
    }

    public static JPanel toolbar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 8));
        panel.setBackground(CARD);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(4, 6, 4, 6)
        ));
        return panel;
    }

    public static void styleToolbarButton(JButton btn) {
        styleButton(btn, PRIMARY, Theme.PRIMARY_HOVER, Color.WHITE, false);
    }

    public static void styleSecondaryButton(JButton btn) {
        styleButton(btn, Color.WHITE, Theme.PRIMARY_SOFT, PRIMARY, true);
    }

    public static void styleDangerButton(JButton btn) {
        styleButton(btn, DANGER, DANGER_DARK, Color.WHITE, false);
    }

    private static void styleButton(JButton btn, Color normal, Color hover, Color foreground, boolean outlined) {
        btn.setFont(FONT_BOLD);
        btn.setBackground(normal);
        btn.setForeground(foreground);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setBorder(new CompoundBorder(
                outlined ? new LineBorder(BORDER, 1, true) : new EmptyBorder(1, 1, 1, 1),
                new EmptyBorder(10, 16, 10, 16)
        ));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { if (btn.isEnabled()) btn.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
    }

    public static JScrollPane tableScroll(JTable tabela) {
        estilizarTabela(tabela);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(new LineBorder(BORDER, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        scroll.getHorizontalScrollBar().setUnitIncrement(18);
        return scroll;
    }

    public static void styleDialog(JDialog dialog, int width, int height) {
        aplicarTema();
        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
        Insets limites = Toolkit.getDefaultToolkit().getScreenInsets(dialog.getGraphicsConfiguration());
        int larguraMaxima = Math.max(520, tela.width - limites.left - limites.right - 80);
        int alturaMaxima = Math.max(420, tela.height - limites.top - limites.bottom - 80);
        int largura = Math.min(width, larguraMaxima);
        int altura = Math.min(height, alturaMaxima);

        dialog.setSize(largura, altura);
        dialog.setMinimumSize(new Dimension(Math.min(largura, 440), Math.min(altura, 320)));
        dialog.setLayout(new BorderLayout(14, 14));
        dialog.setLocationRelativeTo(dialog.getOwner());
        dialog.setModal(true);
        dialog.setResizable(true);
        dialog.getContentPane().setBackground(BACKGROUND);
        ((JComponent) dialog.getContentPane()).setBorder(new EmptyBorder(18, 20, 18, 20));
    }

    /**
     * Cria uma área rolável para formulários e conteúdos extensos.
     * A largura acompanha a janela, enquanto a altura pode crescer e ser rolada.
     */
    public static JScrollPane contentScroll(Component content) {
        ScrollablePanel wrapper = new ScrollablePanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(content, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(22);
        scroll.getHorizontalScrollBar().setUnitIncrement(22);
        return scroll;
    }

    /**
     * Rolagem para telas principais. Mantém a tela ocupando toda a largura disponível.
     */
    public static JScrollPane screenScroll(Component content) {
        ScrollablePanel wrapper = new ScrollablePanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND);
        wrapper.add(content, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(wrapper,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
        scroll.setBackground(BACKGROUND);
        scroll.getViewport().setBackground(BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.getHorizontalScrollBar().setUnitIncrement(24);
        return scroll;
    }

    private static final class ScrollablePanel extends JPanel implements Scrollable {
        private ScrollablePanel(LayoutManager layout) { super(layout); }

        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 24; }
        @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL
                    ? Math.max(80, visibleRect.height - 60)
                    : Math.max(80, visibleRect.width - 60);
        }
        @Override public boolean getScrollableTracksViewportWidth() {
            Container parent = getParent();
            return parent instanceof JViewport && getPreferredSize().width <= parent.getWidth();
        }
        @Override public boolean getScrollableTracksViewportHeight() {
            Container parent = getParent();
            return parent instanceof JViewport && getPreferredSize().height <= parent.getHeight();
        }
    }

    public static JPanel formPanel() {
        JPanel panel = new RoundedPanel(18, CARD);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(20, 22, 20, 22)
        ));
        return panel;
    }

    public static void addField(JPanel panel, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT);
        GridBagConstraints lc = new GridBagConstraints();
        lc.gridx = 0; lc.gridy = row; lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(8, 6, 8, 20);
        panel.add(label, lc);
        prepareInput(field);
        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx = 1; fc.gridy = row; fc.weightx = 1.0;
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.insets = new Insets(8, 6, 8, 6);
        panel.add(field, fc);
    }

    public static JPanel dialogButtons(JButton btnSalvar, JButton btnCancelar) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        panel.setOpaque(false);
        styleToolbarButton(btnSalvar);
        styleSecondaryButton(btnCancelar);
        panel.add(btnCancelar);
        panel.add(btnSalvar);
        return panel;
    }

    public static LocalDate getSqlDate(JDateChooser chooser, String fieldName) {
        if (chooser == null || chooser.getDate() == null) throw new IllegalArgumentException(fieldName + " é obrigatório.");
        return chooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static java.util.Date toUtilDate(LocalDate date) {
        return date == null ? null : java.util.Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static void prepareInput(JComponent component) {
        component.setFont(FONT);
        component.setForeground(TEXT);
        component.setPreferredSize(new Dimension(Math.max(component.getPreferredSize().width, 180), 40));
        component.setMinimumSize(new Dimension(120, 40));

        if (component instanceof JTextField || component instanceof JPasswordField || component instanceof JFormattedTextField) {
            component.setBackground(Color.WHITE);
            Border normal = new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 11, 8, 11));
            Border focus = new CompoundBorder(new LineBorder(PRIMARY, 1, true), new EmptyBorder(8, 11, 8, 11));
            component.setBorder(normal);
            component.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { component.setBorder(focus); }
                @Override public void focusLost(FocusEvent e) { component.setBorder(normal); }
            });
        } else if (component instanceof JComboBox) {
            component.setBackground(Color.WHITE);
            component.setBorder(new LineBorder(BORDER, 1, true));
        } else if (component instanceof JCheckBox) {
            component.setOpaque(false);
        } else if (component instanceof JDateChooser chooser) {
            chooser.setBackground(Color.WHITE);
            if (chooser.getDateEditor() != null && chooser.getDateEditor().getUiComponent() instanceof JComponent c) {
                c.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(7, 9, 7, 9)));
                c.setFont(FONT);
            }
        }
    }

    public static JLabel criarTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT);
        label.setBorder(new EmptyBorder(8, 0, 18, 0));
        return label;
    }

    public static JButton criarBotao(String texto) { JButton b = new JButton(texto); styleToolbarButton(b); return b; }
    public static JButton criarBotaoPerigo(String texto) { JButton b = new JButton(texto); styleDangerButton(b); return b; }
    public static JTextField criarCampoTexto() { JTextField c = new JTextField(); prepareInput(c); return c; }
    public static JComboBox<?> estilizarComboBox(JComboBox<?> combo) { prepareInput(combo); return combo; }
    public static JPanel criarPainelCard() { return formPanel(); }

    public static void estilizarTabela(JTable tabela) {
        tabela.setFont(FONT);
        tabela.setRowHeight(44);
        tabela.setForeground(TEXT);
        tabela.setBackground(Color.WHITE);
        tabela.setGridColor(new Color(241, 245, 249));
        tabela.setSelectionBackground(Theme.PRIMARY_SOFT);
        tabela.setSelectionForeground(PRIMARY_DARK);
        tabela.setShowVerticalLines(false);
        tabela.setShowHorizontalLines(true);
        tabela.setIntercellSpacing(new Dimension(0, 1));
        tabela.setFillsViewportHeight(true);
        if (tabela.getRowSorter() == null) tabela.setAutoCreateRowSorter(true);
        tabela.setBorder(null);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        JTableHeader header = tabela.getTableHeader();
        header.setFont(Theme.SMALL_BOLD);
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(Theme.MUTED);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 43));
        header.setReorderingAllowed(false);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        centro.setBorder(new EmptyBorder(0, 8, 0, 8));
        for (int i = 0; i < tabela.getColumnCount(); i++) tabela.getColumnModel().getColumn(i).setCellRenderer(centro);
    }

    public static JScrollPane criarScrollTabela(JTable tabela) { return tableScroll(tabela); }

    public static JTextField adicionarPesquisa(JPanel toolbar, JTable tabela) {
        JLabel label = new JLabel("Pesquisar");
        label.setFont(Theme.SMALL_BOLD); label.setForeground(Theme.MUTED);
        JTextField pesquisa = SearchUtils.criarPesquisa(tabela);
        prepareInput(pesquisa);
        pesquisa.setPreferredSize(new Dimension(230, 40));
        toolbar.add(Box.createHorizontalStrut(8)); toolbar.add(label); toolbar.add(pesquisa);
        return pesquisa;
    }

    public static JPanel criarCabecalhoTela(String titulo, String subtitulo) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        painel.setBorder(new EmptyBorder(0, 0, 16, 0));
        JLabel t = new JLabel(titulo); t.setFont(TITLE_FONT); t.setForeground(TEXT);
        JLabel s = new JLabel(subtitulo); s.setFont(FONT); s.setForeground(Theme.MUTED);
        JPanel textos = new JPanel(); textos.setOpaque(false); textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(t); textos.add(Box.createVerticalStrut(5)); textos.add(s);
        painel.add(textos, BorderLayout.WEST);
        return painel;
    }

    public static JPanel criarTopoModulo(String titulo, String subtitulo, JPanel acoes) {
        JPanel topo = new JPanel(new BorderLayout(16, 10));
        topo.setOpaque(false);
        topo.add(criarCabecalhoTela(titulo, subtitulo), BorderLayout.NORTH);
        if (acoes != null) topo.add(acoes, BorderLayout.CENTER);
        topo.setBorder(new EmptyBorder(0, 0, 14, 0));
        return topo;
    }

    public static JPanel card(String titulo, String descricao) {
        JPanel p = new RoundedPanel(18, CARD);
        p.setLayout(new BorderLayout(8, 8));
        p.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(18, 18, 18, 18)));
        JLabel t = new JLabel(titulo); t.setFont(Theme.SECTION); t.setForeground(TEXT);
        JLabel d = new JLabel(descricao); d.setFont(FONT); d.setForeground(Theme.MUTED);
        p.add(t, BorderLayout.NORTH); p.add(d, BorderLayout.CENTER);
        return p;
    }

    public static void mensagemSucesso(Component parent, String msg) { Notifications.success(parent, msg); }
    public static void mensagemErro(Component parent, String msg) { Notifications.error(parent, msg); }
    public static boolean confirmar(Component parent, String msg) { return util.FormUtils.confirmar(parent, msg); }
}
