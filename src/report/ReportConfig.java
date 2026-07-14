package report;

import java.io.File;

public final class ReportConfig {

    private static final PrefeituraConfig PREFEITURA = new PrefeituraConfig();
    private static String pastaRelatorios = System.getProperty("obras.relatorios.dir", "relatorios");

    private ReportConfig() {
    }

    public static PrefeituraConfig getPrefeitura() {
        return PREFEITURA;
    }

    public static String getPastaRelatorios() {
        File pasta = new File(pastaRelatorios).getAbsoluteFile();
        if (pasta.exists() && !pasta.isDirectory()) {
            throw new IllegalStateException("O caminho de relatórios não é uma pasta: " + pasta);
        }
        if (!pasta.exists() && !pasta.mkdirs()) {
            throw new IllegalStateException("Não foi possível criar a pasta de relatórios: " + pasta);
        }
        if (!pasta.canWrite()) {
            throw new IllegalStateException("Sem permissão para gravar relatórios em: " + pasta);
        }
        return pasta.getAbsolutePath();
    }

    public static void setPastaRelatorios(String novaPasta) {
        if (novaPasta == null || novaPasta.isBlank()) {
            throw new IllegalArgumentException("A pasta de relatórios não pode ficar vazia.");
        }
        pastaRelatorios = novaPasta.trim();
    }
}
