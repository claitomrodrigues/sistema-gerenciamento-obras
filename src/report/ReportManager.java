package report;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Centraliza a validação e a abertura dos relatórios gerados.
 * A ausência de um leitor de PDF não é tratada como falha de geração.
 */
public final class ReportManager {

    public enum ResultadoAbertura {
        PDF_ABERTO,
        PASTA_ABERTA,
        SOMENTE_GERADO
    }

    private ReportManager() {
    }

    public static ResultadoAbertura abrirPdf(String caminho) {
        File arquivo = validarArquivo(caminho);

        if (tentarAbrir(arquivo)) {
            return ResultadoAbertura.PDF_ABERTO;
        }

        if (abrirPastaDoArquivo(arquivo)) {
            return ResultadoAbertura.PASTA_ABERTA;
        }

        return ResultadoAbertura.SOMENTE_GERADO;
    }

    public static boolean abrirPastaDoArquivo(File arquivo) {
        if (arquivo == null) {
            return false;
        }

        File pasta = arquivo.getAbsoluteFile().getParentFile();
        if (pasta == null || !pasta.isDirectory()) {
            return false;
        }

        // No Windows, o Explorer consegue destacar o arquivo gerado.
        if (isWindows()) {
            try {
                new ProcessBuilder("explorer.exe", "/select,", arquivo.getAbsolutePath()).start();
                return true;
            } catch (IOException ignored) {
                // Tenta a alternativa portável abaixo.
            }
        }

        return tentarAbrir(pasta);
    }

    public static File validarArquivo(String caminho) {
        if (caminho == null || caminho.isBlank()) {
            throw new IllegalArgumentException("O caminho do relatório não foi informado.");
        }

        File arquivo = new File(caminho).getAbsoluteFile();
        if (!arquivo.isFile()) {
            throw new IllegalStateException("O relatório não foi encontrado em: " + arquivo.getAbsolutePath());
        }
        if (arquivo.length() == 0L) {
            throw new IllegalStateException("O relatório foi criado, mas está vazio: " + arquivo.getAbsolutePath());
        }
        return arquivo;
    }

    private static boolean tentarAbrir(File arquivo) {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            return false;
        }

        try {
            desktop.open(arquivo);
            return true;
        } catch (IOException | SecurityException ignored) {
            return false;
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }
}
