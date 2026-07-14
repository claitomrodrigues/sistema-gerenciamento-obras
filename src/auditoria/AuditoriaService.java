package auditoria;

import connection.ConnectionFactory;
import security.SessaoUsuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

/**
 * Registra ações relevantes sem bloquear a operação principal caso o log falhe.
 */
public final class AuditoriaService {
    private AuditoriaService() {
    }

    public static void registrar(String acao, String modulo, String detalhes) {
        String sql = "INSERT INTO auditoria_log(data_hora, usuario, acao, modulo, detalhes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, LocalDateTime.now().toString());
            ps.setString(2, SessaoUsuario.getLogin());
            ps.setString(3, textoSeguro(acao, "AÇÃO"));
            ps.setString(4, textoSeguro(modulo, "Sistema"));
            ps.setString(5, detalhes == null ? "" : detalhes.trim());
            ps.executeUpdate();
        } catch (Exception ignored) {
            // Auditoria é complementar e nunca deve impedir o uso do sistema.
        }
    }

    private static String textoSeguro(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor.trim();
    }
}
