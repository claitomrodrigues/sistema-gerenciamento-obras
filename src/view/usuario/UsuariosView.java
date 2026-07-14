package view.usuario;

import connection.ConnectionFactory;
import security.UsuarioService;
import ui.components.Notifications;
import util.TableExportUtils;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UsuariosView extends JPanel {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Nome","Login","Perfil","Ativo"},0) { public boolean isCellEditable(int r,int c){return false;} };
    private final JTable tabela = new JTable(model);

    public UsuariosView() {
        setLayout(new BorderLayout()); StyleUtils.styleView(this);
        JPanel topo = StyleUtils.toolbar();
        JButton novo = new JButton("Novo Usuário"); JButton editar = new JButton("Editar"); JButton senha = new JButton("Trocar Senha"); JButton atualizar = new JButton("Atualizar"); JButton exportar = new JButton("Exportar CSV");
        for (JButton b : new JButton[]{novo,editar,senha,atualizar,exportar}) StyleUtils.styleToolbarButton(b);
                topo.add(novo); topo.add(editar); topo.add(senha); topo.add(atualizar); topo.add(exportar);
        add(StyleUtils.criarTopoModulo("Usuários", "Cadastro, edição, perfis de acesso e troca de senha", topo), BorderLayout.NORTH); add(StyleUtils.tableScroll(tabela), BorderLayout.CENTER);
        novo.addActionListener(e -> dialogUsuario(null)); editar.addActionListener(e -> editarUsuario()); senha.addActionListener(e -> trocarSenha()); atualizar.addActionListener(e -> carregar()); exportar.addActionListener(e -> TableExportUtils.exportarCSV(this,tabela,"usuarios"));
        carregar();
    }
    private void carregar() { model.setRowCount(0); try (Connection conn = ConnectionFactory.getConnection(); ResultSet rs = UsuarioService.listar(conn)) { while (rs.next()) model.addRow(new Object[]{rs.getInt("id"),rs.getString("nome"),rs.getString("login"),rs.getString("perfil"),rs.getInt("ativo")==1?"Sim":"Não"}); } catch (Exception e) { Notifications.error(this, e.getMessage()); } }
    private void editarUsuario() { int r=tabela.getSelectedRow(); if(r<0){Notifications.warning(this,"Selecione um usuário.");return;} int m=tabela.convertRowIndexToModel(r); dialogUsuario(new Object[]{model.getValueAt(m,0),model.getValueAt(m,1),model.getValueAt(m,2),model.getValueAt(m,3),model.getValueAt(m,4)}); }
    private void dialogUsuario(Object[] dados) {
        boolean editando = dados != null; JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), editando?"Editar Usuário":"Novo Usuário", Dialog.ModalityType.APPLICATION_MODAL);
        JTextField nome = new JTextField(22); JTextField login = new JTextField(22); JPasswordField senha = new JPasswordField(22); JComboBox<String> perfil = new JComboBox<>(new String[]{"administrador","secretario","operador","visualizador"}); JCheckBox ativo = new JCheckBox("Ativo", true);
        if (editando) { nome.setText(String.valueOf(dados[1])); login.setText(String.valueOf(dados[2])); perfil.setSelectedItem(String.valueOf(dados[3])); ativo.setSelected("Sim".equals(String.valueOf(dados[4]))); }
        JPanel p = new JPanel(new GridLayout(0,1,6,4)); p.setBorder(BorderFactory.createEmptyBorder(16,18,10,18)); p.add(new JLabel("Nome")); p.add(nome); p.add(new JLabel("Login")); p.add(login); if(!editando){p.add(new JLabel("Senha")); p.add(senha);} p.add(new JLabel("Perfil")); p.add(perfil); p.add(ativo);
        JButton salvar = new JButton("Salvar"); JButton cancelar = new JButton("Cancelar"); JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT)); botoes.add(cancelar); botoes.add(salvar); cancelar.addActionListener(e -> dialog.dispose());
        salvar.addActionListener(e -> { try { if (nome.getText().isBlank() || login.getText().isBlank()) throw new IllegalArgumentException("Preencha nome e login."); if (editando) { UsuarioService.atualizar((int)dados[0], nome.getText().trim(), login.getText().trim(), perfil.getSelectedItem().toString(), ativo.isSelected()); auditoria.AuditoriaService.registrar("EDITAR_USUARIO", "Usuários", login.getText().trim()); Notifications.success(this, "Usuário atualizado."); } else { if (senha.getPassword().length == 0) throw new IllegalArgumentException("Informe a senha."); UsuarioService.salvar(nome.getText().trim(), login.getText().trim(), new String(senha.getPassword()), perfil.getSelectedItem().toString(), ativo.isSelected()); auditoria.AuditoriaService.registrar("CRIAR_USUARIO", "Usuários", login.getText().trim()); Notifications.success(this, "Usuário cadastrado."); } carregar(); dialog.dispose(); } catch (Exception ex) { Notifications.error(this, ex.getMessage()); } });
        dialog.setLayout(new BorderLayout()); dialog.add(p, BorderLayout.CENTER); dialog.add(botoes, BorderLayout.SOUTH); dialog.pack(); dialog.setLocationRelativeTo(this); dialog.setVisible(true);
    }
    private void trocarSenha() { int r=tabela.getSelectedRow(); if(r<0){Notifications.warning(this,"Selecione um usuário.");return;} int m=tabela.convertRowIndexToModel(r); int id=(int)model.getValueAt(m,0); String login=String.valueOf(model.getValueAt(m,2)); JPasswordField s1=new JPasswordField(18); JPasswordField s2=new JPasswordField(18); JPanel p=new JPanel(new GridLayout(0,1,6,4)); p.add(new JLabel("Nova senha para " + login)); p.add(s1); p.add(new JLabel("Confirmar senha")); p.add(s2); int op=JOptionPane.showConfirmDialog(this,p,"Trocar senha",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE); if(op!=JOptionPane.OK_OPTION)return; try{String a=new String(s1.getPassword()), b=new String(s2.getPassword()); if(!a.equals(b)) throw new IllegalArgumentException("As senhas não conferem."); UsuarioService.trocarSenha(id,a); auditoria.AuditoriaService.registrar("TROCAR_SENHA", "Usuários", login); Notifications.success(this,"Senha alterada.");}catch(Exception ex){Notifications.error(this,ex.getMessage());} }
}
