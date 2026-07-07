package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

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

    public static void info(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void erro(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(String msg) {
        return JOptionPane.showConfirmDialog(null, msg, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
