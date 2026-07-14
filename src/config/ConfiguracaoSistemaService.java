package config;

import connection.ConnectionFactory;
import report.PrefeituraConfig;
import report.ReportConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Persiste as configurações institucionais na tabela configuracao_sistema.
 */
public final class ConfiguracaoSistemaService {

    private static final String[] CHAVES = {
            "sistema.nome", "prefeitura.estado", "prefeitura.nome", "prefeitura.secretaria",
            "prefeitura.cnpj", "prefeitura.endereco", "prefeitura.cidade", "prefeitura.telefone",
            "prefeitura.email", "prefeitura.logo", "prefeitura.responsavel", "prefeitura.cargo",
            "diretorio.relatorios", "diretorio.backup"
    };

    private ConfiguracaoSistemaService() {
    }

    public static void carregar() {
        Map<String, String> valores = listar();
        if (valores.isEmpty()) {
            return;
        }

        PrefeituraConfig p = ReportConfig.getPrefeitura();
        aplicar(valores, "sistema.nome", SistemaConfig::setNomeSistema);
        aplicar(valores, "prefeitura.estado", p::setEstado);
        aplicar(valores, "prefeitura.nome", p::setPrefeitura);
        aplicar(valores, "prefeitura.secretaria", p::setSecretaria);
        aplicar(valores, "prefeitura.cnpj", p::setCnpj);
        aplicar(valores, "prefeitura.endereco", p::setEndereco);
        aplicar(valores, "prefeitura.cidade", p::setCidade);
        aplicar(valores, "prefeitura.telefone", p::setTelefone);
        aplicar(valores, "prefeitura.email", p::setEmail);
        aplicar(valores, "prefeitura.logo", p::setCaminhoLogo);
        aplicar(valores, "prefeitura.responsavel", p::setNomeResponsavel);
        aplicar(valores, "prefeitura.cargo", p::setCargoResponsavel);
        aplicar(valores, "diretorio.relatorios", SistemaConfig::setPastaRelatorios);
        aplicar(valores, "diretorio.backup", SistemaConfig::setPastaBackup);
    }

    public static void salvar() {
        PrefeituraConfig p = ReportConfig.getPrefeitura();
        Map<String, String> valores = new LinkedHashMap<>();
        valores.put("sistema.nome", SistemaConfig.getNomeSistema());
        valores.put("prefeitura.estado", p.getEstado());
        valores.put("prefeitura.nome", p.getPrefeitura());
        valores.put("prefeitura.secretaria", p.getSecretaria());
        valores.put("prefeitura.cnpj", p.getCnpj());
        valores.put("prefeitura.endereco", p.getEndereco());
        valores.put("prefeitura.cidade", p.getCidade());
        valores.put("prefeitura.telefone", p.getTelefone());
        valores.put("prefeitura.email", p.getEmail());
        valores.put("prefeitura.logo", p.getCaminhoLogo());
        valores.put("prefeitura.responsavel", p.getNomeResponsavel());
        valores.put("prefeitura.cargo", p.getCargoResponsavel());
        valores.put("diretorio.relatorios", SistemaConfig.getPastaRelatorios());
        valores.put("diretorio.backup", SistemaConfig.getPastaBackup());

        String sql = "INSERT INTO configuracao_sistema(chave, valor) VALUES (?, ?) "
                + "ON CONFLICT(chave) DO UPDATE SET valor = excluded.valor";
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (Map.Entry<String, String> e : valores.entrySet()) {
                    ps.setString(1, e.getKey());
                    ps.setString(2, normalizar(e.getValue()));
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            } catch (Exception e) {
                try { conn.rollback(); } catch (SQLException ignored) { }
                throw e;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível salvar as configurações do sistema.", e);
        }
    }

    private static Map<String, String> listar() {
        Map<String, String> valores = new LinkedHashMap<>();
        String placeholders = String.join(",", java.util.Collections.nCopies(CHAVES.length, "?"));
        String sql = "SELECT chave, valor FROM configuracao_sistema WHERE chave IN (" + placeholders + ")";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < CHAVES.length; i++) {
                ps.setString(i + 1, CHAVES[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    valores.put(rs.getString("chave"), rs.getString("valor"));
                }
            }
            return valores;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível carregar as configurações do sistema.", e);
        }
    }

    private static void aplicar(Map<String, String> valores, String chave, java.util.function.Consumer<String> setter) {
        String valor = valores.get(chave);
        if (valor != null && !valor.isBlank()) {
            setter.accept(valor.trim());
        }
    }

    private static String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
