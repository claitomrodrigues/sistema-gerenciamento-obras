package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.Dialog;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import ui.components.Notifications;
import ui.theme.Theme;

public class FormUtils {
    public static double parseDouble(String valor, String campo) {
        try {
            String normalizado = valor == null ? "" : valor.trim().replace(",", ".");
            if (normalizado.isEmpty()) return 0.0;
            return Double.parseDouble(normalizado);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Campo inválido: " + campo + ". Use apenas números.");
        }
    }

    public static LocalDate parseDate(String valor, String campo) {
        try {
            String normalizado = valor == null ? "" : valor.trim();
            if (normalizado.isEmpty()) return LocalDate.now();
            return LocalDate.parse(normalizado);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Campo inválido: " + campo + ". Use o formato AAAA-MM-DD. Exemplo: 2026-07-07");
        }
    }

    public static int selectedId(JComboBox<String> combo, String campo) {
        Object item = combo.getSelectedItem();
        if (item == null || item.toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Selecione um item em: " + campo);
        }
        String texto = item.toString();
        int pos = texto.indexOf(" - ");
        try {
            return Integer.parseInt(pos >= 0 ? texto.substring(0, pos).trim() : texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Seleção inválida em: " + campo);
        }
    }

    public static int selectedTableId(JTable tabela) {
        int row = tabela.getSelectedRow();
        if (row < 0) return -1;
        int modelRow = tabela.convertRowIndexToModel(row);
        return Integer.parseInt(tabela.getModel().getValueAt(modelRow, 0).toString());
    }

    public static void info(String msg) { Notifications.info(null, msg); }
    public static void erro(Exception e) { Notifications.error(null, e.getMessage() == null ? "Erro inesperado." : e.getMessage()); }

    public static boolean confirmar(String msg) { return confirmar(null, msg); }

    public static boolean confirmar(Component parent, String msg) {
        final boolean[] resposta = {false};
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Confirmação", Dialog.ModalityType.APPLICATION_MODAL);
        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBackground(Theme.CARD);
        raiz.setBorder(new EmptyBorder(18, 20, 16, 20));
        JLabel texto = new JLabel("<html><b>Confirmação</b><br>" + msg + "</html>");
        texto.setFont(Theme.FONT);
        texto.setForeground(Theme.TEXT);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setOpaque(false);
        JButton nao = new JButton("Cancelar");
        JButton sim = new JButton("Confirmar");
        sim.setBackground(Theme.DANGER);
        sim.setForeground(java.awt.Color.WHITE);
        nao.addActionListener(e -> dialog.dispose());
        sim.addActionListener(e -> { resposta[0] = true; dialog.dispose(); });
        botoes.add(nao); botoes.add(sim);
        raiz.add(texto, BorderLayout.CENTER);
        raiz.add(botoes, BorderLayout.SOUTH);
        dialog.setContentPane(raiz);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return resposta[0];
    }
}
