package main;

import connection.DatabaseInitializer;
import config.ConfiguracaoSistemaService;
import view.MainFrame;
import view.StyleUtils;
import view.usuario.LoginDialog;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        try {
            DatabaseInitializer.inicializar();
            ConfiguracaoSistemaService.carregar();
            StyleUtils.aplicarTema();
        } catch (Exception e) {
            exibirErroInicializacao(e);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            LoginDialog login = new LoginDialog();
            login.setVisible(true);
            if (!login.isAutenticado()) {
                return;
            }

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    private static void exibirErroInicializacao(Exception e) {
        String detalhe = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        JOptionPane.showMessageDialog(
                null,
                "Não foi possível iniciar o sistema.\n\n" + detalhe,
                "Erro de inicialização",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
