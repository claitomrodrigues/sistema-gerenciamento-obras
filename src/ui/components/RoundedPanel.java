package ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.theme.Theme;

public class RoundedPanel extends JPanel {
    private final int radius;
    private final Color backgroundColor;

    public RoundedPanel() {
        this(18, Theme.CARD);
    }

    public RoundedPanel(int radius, Color backgroundColor) {
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        setOpaque(false);
        setBorder(new EmptyBorder(16, 16, 16, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 18));
        g2.fillRoundRect(3, 5, getWidth() - 8, getHeight() - 8, radius, radius);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, radius, radius);
        g2.setColor(Theme.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 7, getHeight() - 7, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}
