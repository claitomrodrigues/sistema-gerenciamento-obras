package security;

import auditoria.AuditoriaService;
import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UsuarioService {
    private UsuarioService() {
    }

    public static boolean autenticar(String login, String senha) {
        String loginNormalizado = normalizarObrigatorio(login, "Informe o login.");
        String senhaNormalizada = normalizarObrigatorio(senha, "Informe a senha.");
        String sql = "SELECT id, nome, login, senha, perfil FROM usuario WHERE login=? AND ativo=1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loginNormalizado);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || !PasswordUtils.verificar(senhaNormalizada, rs.getString("senha"))) {
                    return false;
                }

                if (PasswordUtils.precisaMigrar(rs.getString("senha"))) {
                    migrarSenha(conn, rs.getInt("id"), senhaNormalizada);
                }

                SessaoUsuario.iniciar(
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("perfil"));
                AuditoriaService.registrar("LOGIN", "Usuários", loginNormalizado);
                return true;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível autenticar o usuário.", e);
        }
    }

    private static void migrarSenha(Connection conn, int id, String senha) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE usuario SET senha=? WHERE id=?")) {
            ps.setString(1, PasswordUtils.criarHash(senha));
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Mantido por compatibilidade com UsuariosView. O chamador deve fechar o ResultSet,
     * o Statement associado e a Connection recebida.
     */
    public static ResultSet listar(Connection conn) throws SQLException {
        if (conn == null || conn.isClosed()) {
            throw new IllegalArgumentException("Conexão inválida para listar usuários.");
        }
        return conn.createStatement().executeQuery(
                "SELECT id,nome,login,perfil,ativo FROM usuario ORDER BY nome");
    }

    public static void salvar(String nome, String login, String senha, String perfil, boolean ativo)
            throws SQLException {
        validarUsuario(nome, login, perfil);
        validarSenha(senha);

        String sql = "INSERT INTO usuario(nome,login,senha,perfil,ativo) VALUES(?,?,?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome.trim());
            ps.setString(2, login.trim());
            ps.setString(3, PasswordUtils.criarHash(senha));
            ps.setString(4, perfil.trim().toLowerCase());
            ps.setInt(5, ativo ? 1 : 0);
            ps.executeUpdate();
        }
    }

    public static void atualizar(int id, String nome, String login, String perfil, boolean ativo)
            throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Usuário inválido para atualização.");
        }
        validarUsuario(nome, login, perfil);

        String sql = "UPDATE usuario SET nome=?, login=?, perfil=?, ativo=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome.trim());
            ps.setString(2, login.trim());
            ps.setString(3, perfil.trim().toLowerCase());
            ps.setInt(4, ativo ? 1 : 0);
            ps.setInt(5, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Usuário não encontrado.");
            }
        }
    }

    public static void trocarSenha(int id, String novaSenha) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Usuário inválido para troca de senha.");
        }
        validarSenha(novaSenha);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE usuario SET senha=? WHERE id=?")) {
            ps.setString(1, PasswordUtils.criarHash(novaSenha));
            ps.setInt(2, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Usuário não encontrado.");
            }
        }
    }

    private static void validarUsuario(String nome, String login, String perfil) {
        normalizarObrigatorio(nome, "Informe o nome do usuário.");
        normalizarObrigatorio(login, "Informe o login do usuário.");
        normalizarObrigatorio(perfil, "Informe o perfil do usuário.");
    }

    private static void validarSenha(String senha) {
        if (senha == null || senha.length() < 4) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 4 caracteres.");
        }
    }

    private static String normalizarObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }
}
