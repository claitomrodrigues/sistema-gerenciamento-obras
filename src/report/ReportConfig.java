package report;

import java.io.File;

public class ReportConfig {

    private static final PrefeituraConfig PREFEITURA = new PrefeituraConfig();
    private static String pastaRelatorios = "relatorios";

    private ReportConfig() {}

    public static PrefeituraConfig getPrefeitura() {
        return PREFEITURA;
    }

    public static String getPastaRelatorios() {
        File pasta = new File(pastaRelatorios);
        if (!pasta.exists()) pasta.mkdirs();
        return pasta.getAbsolutePath();
    }

    public static void setPastaRelatorios(String pastaRelatorios) {
        ReportConfig.pastaRelatorios = pastaRelatorios;
    }
}
