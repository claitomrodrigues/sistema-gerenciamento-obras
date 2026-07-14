package ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.theme.Theme;

public final class Notifications {
    private Notifications() {}

    public static void success(Component parent, String message) { show(parent, "Sucesso", message, Theme.SUCCESS); }
    public static void warning(Component parent, String message) { show(parent, "Atenção", message, Theme.WARNING); }
    public static void error(Component parent, String message) { show(parent, "Erro", message, Theme.DANGER); }
    public static void info(Component parent, String message) { show(parent, "Informação", message, Theme.PRIMARY_2); }

    private static void show(Component parent, String title, String message, Color color) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        final JDialog dialog = new JDialog(owner);
        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(false);

        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel mark = new JLabel("●");
        mark.setForeground(color);
        mark.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel text = new JLabel("<html><b>" + title + "</b><br>" + message + "</html>");
        text.setFont(Theme.FONT);
        text.setForeground(Theme.TEXT);

        panel.add(mark, BorderLayout.WEST);
        panel.add(text, BorderLayout.CENTER);
        dialog.setContentPane(panel);
        dialog.pack();

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screen.width - dialog.getWidth() - 28;
        int y = screen.height - dialog.getHeight() - 70;
        dialog.setLocation(x, y);

        Timer timer = new Timer(2600, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }
}
