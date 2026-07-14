package util;

import auditoria.AuditoriaService;
import ui.components.Notifications;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public final class TableExportUtils {
    private TableExportUtils() {}

    public static void exportarCSV(Component parent, JTable tabela, String nomeBase) {
        if (tabela == null || tabela.getModel() == null || tabela.getModel().getRowCount() == 0) {
            Notifications.warning(parent, "Não há dados para exportar.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Exportar CSV");
        chooser.setSelectedFile(new File(nomeBase + ".csv"));
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivo CSV", "csv"));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File arquivo = chooser.getSelectedFile();
        if (!arquivo.getName().toLowerCase().endsWith(".csv")) {
            arquivo = new File(arquivo.getParentFile(), arquivo.getName() + ".csv");
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(arquivo, StandardCharsets.UTF_8))) {
            TableModel model = tabela.getModel();
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) out.print(';');
                out.print(escapar(model.getColumnName(c)));
            }
            out.println();
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) {
                    if (c > 0) out.print(';');
                    out.print(escapar(model.getValueAt(r, c)));
                }
                out.println();
            }
            AuditoriaService.registrar("EXPORTAR_CSV", "Tabela", arquivo.getAbsolutePath());
            Notifications.success(parent, "Arquivo exportado: " + arquivo.getName());
        } catch (Exception ex) {
            Notifications.error(parent, "Erro ao exportar: " + ex.getMessage());
        }
    }

    private static String escapar(Object valor) {
        String s = valor == null ? "" : valor.toString();
        s = s.replace("\"", "\"\"");
        return "\"" + s + "\"";
    }
}
