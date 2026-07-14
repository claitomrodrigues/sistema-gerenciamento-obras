package view.sobre;

import security.SessaoUsuario;
import ui.theme.Theme;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SobreView extends JPanel {
    public SobreView() {
        setLayout(new BorderLayout()); StyleUtils.styleView(this);
        JPanel card = new JPanel(); card.setBackground(Color.WHITE); card.setBorder(new EmptyBorder(34,42,34,42)); card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Sistema de Gestão de Obras e Frota"); titulo.setFont(new Font("Segoe UI", Font.BOLD, 30)); titulo.setForeground(Theme.PRIMARY);
        JLabel versao = new JLabel("Versão 2.0 - Reengenharia comercial"); versao.setFont(Theme.FONT); versao.setForeground(Theme.MUTED);
        JTextArea texto = new JTextArea("Desenvolvido para apoiar a Secretaria Municipal de Obras no controle de equipamentos, combustíveis, abastecimentos, manutenções, ocorrências, licitações e empenhos.\n\nDesenvolvedor: Claitom Rodrigues\nContato: claitomrodrigues / cleitinofc13@gmail.com\nMunicípio base: São Vicente do Sul - RS\nUsuário atual: " + SessaoUsuario.getNome() + " (" + SessaoUsuario.getPerfil() + ")");
        texto.setEditable(false); texto.setOpaque(false); texto.setLineWrap(true); texto.setWrapStyleWord(true); texto.setFont(new Font("Segoe UI", Font.PLAIN, 15)); texto.setForeground(Theme.TEXT);
        card.add(titulo); card.add(Box.createVerticalStrut(8)); card.add(versao); card.add(Box.createVerticalStrut(28)); card.add(texto);
        add(card, BorderLayout.CENTER);
    }
}
