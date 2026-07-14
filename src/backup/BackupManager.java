package backup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupManager {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private BackupManager() {}

    public static File gerarBackup(String caminhoBanco, String pastaBackup) {
        try {
            File banco = new File(caminhoBanco);
            if (!banco.exists()) {
                throw new IllegalArgumentException("Banco de dados não encontrado: " + caminhoBanco);
            }
            File pasta = new File(pastaBackup);
            if (!pasta.exists()) pasta.mkdirs();
            String nome = "backup_obras_" + LocalDateTime.now().format(FORMATTER) + ".db";
            File destino = new File(pasta, nome);
            Files.copy(banco.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destino;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar backup.", e);
        }
    }

    public static File gerarBackupAutomatico(String caminhoBanco, String pastaBackup) {
        File pasta = new File(pastaBackup, "automaticos");
        return gerarBackup(caminhoBanco, pasta.getPath());
    }

    public static void restaurarBackup(File arquivoBackup, String caminhoBanco) {
        try {
            if (arquivoBackup == null || !arquivoBackup.exists()) {
                throw new IllegalArgumentException("Arquivo de backup não encontrado.");
            }
            File banco = new File(caminhoBanco);
            File pasta = banco.getAbsoluteFile().getParentFile();
            if (pasta != null && !pasta.exists()) pasta.mkdirs();
            Files.copy(arquivoBackup.toPath(), banco.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao restaurar backup.", e);
        }
    }
}
