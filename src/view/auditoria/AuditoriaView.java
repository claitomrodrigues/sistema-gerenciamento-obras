package view.auditoria;

import connection.ConnectionFactory;
import ui.components.Notifications;
import util.TableExportUtils;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AuditoriaView extends JPanel {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Data/Hora","Usuário","Ação","Módulo","Detalhes"},0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabela = new JTable(model);
    private final JTextField txtPesquisa = new JTextField(24);

    public AuditoriaView() {
        setLayout(new BorderLayout());
        StyleUtils.styleView(this);
        JPanel topo = StyleUtils.toolbar();
                topo.add(new JLabel("Pesquisar:"));
        topo.add(txtPesquisa);
        JButton atualizar = new JButton("Atualizar");
        JButton exportar = new JButton("Exportar CSV");
        StyleUtils.styleToolbarButton(atualizar); StyleUtils.styleToolbarButton(exportar);
        topo.add(atualizar); topo.add(exportar);
        add(StyleUtils.criarTopoModulo("Auditoria", "Histórico de ações realizadas no sistema", topo), BorderLayout.NORTH);
        add(StyleUtils.tableScroll(tabela), BorderLayout.CENTER);
        atualizar.addActionListener(e -> carregar());
        exportar.addActionListener(e -> TableExportUtils.exportarCSV(this, tabela, "auditoria"));
        txtPesquisa.addActionListener(e -> carregar());
        carregar();
    }

    private void carregar() {
        model.setRowCount(0);
        String filtro = txtPesquisa.getText().trim();
        String sql = "SELECT id,data_hora,usuario,acao,modulo,detalhes FROM auditoria_log "
                + (filtro.isEmpty() ? "" : "WHERE usuario LIKE ? OR acao LIKE ? OR modulo LIKE ? OR detalhes LIKE ? ")
                + "ORDER BY id DESC LIMIT 500";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!filtro.isEmpty()) for (int i=1;i<=4;i++) ps.setString(i, "%"+filtro+"%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) model.addRow(new Object[]{rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)});
            }
        } catch (Exception e) { Notifications.error(this, "Erro ao carregar auditoria: " + e.getMessage()); }
    }
}
