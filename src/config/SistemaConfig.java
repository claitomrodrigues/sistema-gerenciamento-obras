package config;

import report.ReportConfig;

import java.io.File;

public class SistemaConfig {

    private static String nomeSistema = "Sistema de Gestão de Obras e Frota";
    private static String caminhoBanco = "sistema.db";
    private static String pastaBackup = "backup";

    private SistemaConfig() {}

    public static String getNomeSistema() {
        return nomeSistema;
    }

    public static void setNomeSistema(String nomeSistema) {
        SistemaConfig.nomeSistema = nomeSistema;
    }

    public static String getCaminhoBanco() {
        return caminhoBanco;
    }

    public static void setCaminhoBanco(String caminhoBanco) {
        SistemaConfig.caminhoBanco = caminhoBanco;
    }

    public static String getPastaBackup() {
        File pasta = new File(pastaBackup);
        if (!pasta.exists()) pasta.mkdirs();
        return pasta.getAbsolutePath();
    }

    public static void setPastaBackup(String pastaBackup) {
        SistemaConfig.pastaBackup = pastaBackup;
    }

    public static String getPastaRelatorios() {
        return ReportConfig.getPastaRelatorios();
    }

    public static void setPastaRelatorios(String pasta) {
        ReportConfig.setPastaRelatorios(pasta);
    }
}
