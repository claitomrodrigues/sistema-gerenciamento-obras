package security;

public final class SessaoUsuario {
    private static String nome = "Visitante";
    private static String login = "visitante";
    private static String perfil = "visualizador";

    private SessaoUsuario() {
    }

    public static void iniciar(String nomeUsuario, String loginUsuario, String perfilUsuario) {
        nome = normalizar(nomeUsuario, "Usuário");
        login = normalizar(loginUsuario, "usuario");
        perfil = normalizar(perfilUsuario, "visualizador").toLowerCase();
    }

    public static void encerrar() {
        nome = "Visitante";
        login = "visitante";
        perfil = "visualizador";
    }

    public static String getNome() {
        return nome;
    }

    public static String getLogin() {
        return login;
    }

    public static String getPerfil() {
        return perfil;
    }

    public static boolean isAdministrador() {
        return "administrador".equalsIgnoreCase(perfil);
    }

    private static String normalizar(String valor, String padrao) {
        return valor == null || valor.isBlank() ? padrao : valor.trim();
    }
}
