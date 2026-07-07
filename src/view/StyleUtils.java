package view;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.toedter.calendar.JDateChooser;

public final class StyleUtils {

    public static final Color PRIMARY = new Color(33, 87, 164);
    public static final Color PRIMARY_DARK = new Color(22, 61, 120);
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(40, 40, 40);
    public static final Color BORDER = new Color(220, 225, 230);
    public static final Color DANGER = new Color(190, 45, 45);
    public static final Color DANGER_DARK = new Color(150, 35, 35);
    public static final Color SUCCESS = new Color(40, 150, 90);

    public static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);

    private StyleUtils() {}

    public static void aplicarTema() {
        UIManager.put("Label.font", FONT);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("TextField.font", FONT);
        UIManager.put("ComboBox.font", FONT);
        UIManager.put("Table.font", FONT);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("OptionPane.messageFont", FONT);
        UIManager.put("OptionPane.buttonFont", FONT_BOLD);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("ComboBox.background", Color.WHITE);
    }

    public static void styleView(JComponent component) {
        component.setBackground(BACKGROUND);
        component.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    public static JPanel toolbar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return panel;
    }

    public static void styleToolbarButton(JButton btn) {
        styleButton(btn, PRIMARY, PRIMARY_DARK);
    }

    public static void styleDangerButton(JButton btn) {
        styleButton(btn, DANGER, DANGER_DARK);
    }

    private static void styleButton(final JButton btn, final Color normal, final Color hover) {
        btn.setFont(FONT_BOLD);
        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(normal);
            }
        });
    }

    public static JScrollPane tableScroll(JTable tabela) {
        estilizarTabela(tabela);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(new LineBorder(BORDER, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public static void styleDialog(JDialog dialog, int width, int height) {
        aplicarTema();
        dialog.setSize(width, height);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.getContentPane().setBackground(BACKGROUND);
        ((JComponent) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    public static JPanel formPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    public static void addField(JPanel panel, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT);

        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(6, 6, 6, 12);
        panel.add(label, labelConstraints);

        prepareInput(field);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(6, 6, 6, 6);
        panel.add(field, fieldConstraints);
    }

    public static JPanel dialogButtons(JButton btnSalvar, JButton btnCancelar) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(BACKGROUND);
        styleToolbarButton(btnSalvar);
        styleDangerButton(btnCancelar);
        panel.add(btnSalvar);
        panel.add(btnCancelar);
        return panel;
    }

    public static LocalDate getSqlDate(JDateChooser chooser, String fieldName) {
        if (chooser == null || chooser.getDate() == null) {
            throw new IllegalArgumentException(fieldName + " é obrigatório.");
        }
        return chooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static java.util.Date toUtilDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return java.util.Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static void prepareInput(JComponent component) {
        component.setFont(FONT);
        component.setForeground(TEXT);

        if (component instanceof JTextField) {
            component.setBorder(new CompoundBorder(
                    new LineBorder(BORDER, 1, true),
                    new EmptyBorder(8, 10, 8, 10)
            ));
        } else if (component instanceof JComboBox) {
            component.setBackground(Color.WHITE);
            component.setBorder(new LineBorder(BORDER, 1, true));
        } else if (component instanceof JCheckBox) {
            component.setOpaque(false);
        } else if (component instanceof JDateChooser) {
            component.setBackground(Color.WHITE);
        }
    }

    public static JLabel criarTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT);
        label.setBorder(new EmptyBorder(10, 0, 20, 0));
        return label;
    }

    public static JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        styleToolbarButton(btn);
        return btn;
    }

    public static JButton criarBotaoPerigo(String texto) {
        JButton btn = new JButton(texto);
        styleDangerButton(btn);
        return btn;
    }

    public static JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        prepareInput(campo);
        return campo;
    }

    public static JComboBox<?> estilizarComboBox(JComboBox<?> combo) {
        prepareInput(combo);
        return combo;
    }

    public static JPanel criarPainelCard() {
        return formPanel();
    }

    public static void estilizarTabela(JTable tabela) {
        tabela.setFont(FONT);
        tabela.setRowHeight(32);
        tabela.setForeground(TEXT);
        tabela.setBackground(Color.WHITE);
        tabela.setGridColor(new Color(235, 235, 235));
        tabela.setSelectionBackground(new Color(210, 228, 250));
        tabela.setSelectionForeground(TEXT);
        tabela.setShowVerticalLines(false);
        tabela.setFillsViewportHeight(true);
        tabela.setAutoCreateRowSorter(true);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(PRIMARY);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centro);
        }
    }

    public static JScrollPane criarScrollTabela(JTable tabela) {
        return tableScroll(tabela);
    }

    public static void mensagemSucesso(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mensagemErro(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(Component parent, String msg) {
        int opcao = JOptionPane.showConfirmDialog(
                parent,
                msg,
                "Confirmação",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return opcao == JOptionPane.YES_OPTION;
    }
}
