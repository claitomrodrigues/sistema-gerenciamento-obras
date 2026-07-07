package main;

import javax.swing.SwingUtilities;

import connection.DatabaseInitializer;

//import com.formdev.flatlaf.FlatLightLaf;

import view.MainFrame;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.inicializar();

        try {
         //   FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });

    }

}