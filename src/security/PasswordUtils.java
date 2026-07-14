package security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private PasswordUtils() {}

    public static String criarHash(String senha) {
        String salt = gerarSalt();
        String hash = sha256(salt + senha);
        return "sha256$" + salt + "$" + hash;
    }

    public static boolean verificar(String senhaDigitada, String valorArmazenado) {
        if (valorArmazenado == null) return false;
        if (valorArmazenado.startsWith("sha256$")) {
            String[] partes = valorArmazenado.split("\\$", 3);
            if (partes.length != 3) return false;
            String hash = sha256(partes[1] + senhaDigitada);
            return constantTimeEquals(hash, partes[2]);
        }
        // compatibilidade com usuários antigos em texto puro, só para permitir migração automática
        return valorArmazenado.equals(senhaDigitada);
    }

    public static boolean precisaMigrar(String valorArmazenado) {
        return valorArmazenado != null && !valorArmazenado.startsWith("sha256$");
    }

    private static String gerarSalt() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao criptografar senha.", e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        int diff = a.length() ^ b.length();
        int max = Math.min(a.length(), b.length());
        for (int i = 0; i < max; i++) diff |= a.charAt(i) ^ b.charAt(i);
        return diff == 0;
    }
}
