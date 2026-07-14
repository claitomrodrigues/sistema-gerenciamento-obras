package security;

import connection.ConnectionFactory;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Locale;

public final class PermissionManager {
    private PermissionManager() {}

    public static boolean isAdmin() { return perfil().equals("administrador"); }
    public static boolean isSecretario() { return perfil().equals("secretario") || isAdmin(); }
    public static boolean isOperador() { return perfil().equals("operador") || isSecretario(); }
    public static boolean isVisualizador() { return perfil().equals("visualizador"); }

    public static boolean podeCadastrar() { return isOperador(); }
    public static boolean podeEditar() { return isOperador(); }
    public static boolean podeExcluir() { return isSecretario(); }
    public static boolean podeUsuarios() { return podeModuloAcao("usuarios", "visualizar") && isAdmin(); }
    public static boolean podeConfiguracoes() { return podeModuloAcao("configuracoes", "visualizar") && (isAdmin() || isSecretario()); }
    public static boolean podeBackup() { return podeModuloAcao("backup", "visualizar") && (isAdmin() || isSecretario()); }
    public static boolean podeAuditoria() { return podeModuloAcao("auditoria", "visualizar") && (isAdmin() || isSecretario()); }

    public static boolean podeModuloAcao(String modulo, String acao) {
        if (isAdmin()) return true;
        String coluna = colunaAcao(acao);
        if (coluna == null) return false;
        String sql = "SELECT " + coluna + " FROM permissao_modulo WHERE perfil=? AND modulo=?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, perfil()); ps.setString(2, normalizar(modulo));
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getInt(1) == 1; }
        } catch (Exception ignored) { }
        return fallback(acao);
    }

    private static String colunaAcao(String acao) {
        return switch (normalizar(acao)) {
            case "visualizar" -> "pode_visualizar";
            case "cadastrar", "criar", "novo" -> "pode_cadastrar";
            case "editar", "alterar" -> "pode_editar";
            case "excluir", "deletar" -> "pode_excluir";
            case "exportar" -> "pode_exportar";
            case "importar" -> "pode_importar";
            default -> null;
        };
    }
    private static boolean fallback(String acao) { return switch (normalizar(acao)) { case "visualizar", "exportar" -> !isVisualizador() || isVisualizador(); case "cadastrar", "criar", "novo", "editar", "alterar", "importar" -> podeCadastrar(); case "excluir", "deletar" -> podeExcluir(); default -> false; }; }

    public static void aplicarCrud(Component parent, JButton novo, JButton editar, JButton excluir) {
        aplicar(novo, podeCadastrar(), "Seu perfil não permite cadastrar registros.");
        aplicar(editar, podeEditar(), "Seu perfil não permite editar registros.");
        aplicar(excluir, podeExcluir(), "Seu perfil não permite excluir registros.");
    }
    public static void aplicarCrud(Component parent, String modulo, JButton novo, JButton editar, JButton excluir) {
        aplicar(novo, podeModuloAcao(modulo, "cadastrar"), "Seu perfil não permite cadastrar neste módulo.");
        aplicar(editar, podeModuloAcao(modulo, "editar"), "Seu perfil não permite editar neste módulo.");
        aplicar(excluir, podeModuloAcao(modulo, "excluir"), "Seu perfil não permite excluir neste módulo.");
    }

    public static void aplicar(JButton botao, boolean permitido, String dica) { if (botao == null) return; botao.setEnabled(permitido); if (!permitido) botao.setToolTipText(dica); }
    public static boolean exigir(Component parent, boolean permitido, String mensagem) { if (permitido) return true; ui.components.Notifications.warning(parent, mensagem); return false; }
    private static String perfil() { String p = SessaoUsuario.getPerfil(); return p == null ? "visualizador" : normalizar(p); }
    private static String normalizar(String s) { return s == null ? "" : s.trim().toLowerCase(Locale.ROOT).replace('ç','c').replace('õ','o').replace('á','a').replace('é','e').replace('í','i').replace('ó','o').replace('ú','u'); }
}
