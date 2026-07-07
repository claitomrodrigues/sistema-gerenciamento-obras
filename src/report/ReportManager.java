package report;

import java.awt.Desktop;
import java.io.File;

public class ReportManager {

    private ReportManager() {}

    public static void abrirPdf(String caminho) {
        try {
            File arquivo = new File(caminho);
            if (arquivo.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(arquivo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao abrir PDF: " + caminho, e);
        }
    }
}
