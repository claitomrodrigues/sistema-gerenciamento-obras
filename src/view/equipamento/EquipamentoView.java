package view.equipamento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.EquipamentoController;
import model.Equipamento;
import util.FormUtils; import util.TableExportUtils; import util.ExcelImportUtils;
import security.PermissionManager; import view.StyleUtils;

public class EquipamentoView extends JPanel {
    private JTable tabela; private DefaultTableModel tableModel;
    private JButton btnNovo, btnEditar, btnExcluir, btnAtualizar, btnExportar, btnImportar, btnHistorico;
    private EquipamentoController controller = new EquipamentoController();
    public EquipamentoView() { inicializar(); configurarEventos(); carregarTabela(); }
    private void inicializar() {
        setLayout(new BorderLayout()); StyleUtils.styleView(this); JPanel painelBotoes = StyleUtils.toolbar();
        btnNovo = new JButton("Novo"); btnEditar = new JButton("Editar"); btnExcluir = new JButton("Excluir"); btnAtualizar = new JButton("Atualizar"); btnExportar = new JButton("Exportar CSV"); btnImportar = new JButton("Importar Excel/CSV"); btnHistorico = new JButton("Histórico"); StyleUtils.styleToolbarButton(btnNovo); StyleUtils.styleToolbarButton(btnEditar); StyleUtils.styleDangerButton(btnExcluir); StyleUtils.styleSecondaryButton(btnAtualizar); StyleUtils.styleSecondaryButton(btnExportar); StyleUtils.styleSecondaryButton(btnImportar); StyleUtils.styleSecondaryButton(btnHistorico);
        painelBotoes.add(btnNovo); painelBotoes.add(btnEditar); painelBotoes.add(btnExcluir); painelBotoes.add(btnAtualizar); painelBotoes.add(btnExportar); painelBotoes.add(btnImportar); painelBotoes.add(btnHistorico); PermissionManager.aplicarCrud(this, "equipamento", btnNovo, btnEditar, btnExcluir); PermissionManager.aplicar(btnImportar, PermissionManager.podeModuloAcao("equipamento", "importar"), "Seu perfil não permite importar neste módulo."); add(StyleUtils.criarTopoModulo("Equipamentos", "Cadastre e acompanhe a frota municipal", painelBotoes), BorderLayout.NORTH);
        tableModel = new DefaultTableModel(new String[]{"ID","Nome","Tipo","Placa","KM Atual","Horas de Uso","Ativo"},0){ public boolean isCellEditable(int r,int c){return false;}};
        tabela = new JTable(tableModel); StyleUtils.adicionarPesquisa(painelBotoes, tabela); add(StyleUtils.tableScroll(tabela), BorderLayout.CENTER);
    }
    private void configurarEventos(){ btnNovo.addActionListener(e->abrirDialog(null)); btnEditar.addActionListener(e->editar()); btnExcluir.addActionListener(e->excluir()); btnAtualizar.addActionListener(e->carregarTabela());btnExportar.addActionListener(e->TableExportUtils.exportarCSV(this,tabela,"equipamento")); btnImportar.addActionListener(e->ExcelImportUtils.importarEquipamentos(this, this::carregarTabela)); btnHistorico.addActionListener(e->abrirHistorico()); tabela.addMouseListener(new java.awt.event.MouseAdapter(){ public void mouseClicked(java.awt.event.MouseEvent e){ if(e.getClickCount()==2) abrirHistorico(); }}); }
    private void carregarTabela(){ tableModel.setRowCount(0); for(Equipamento e: controller.listar()) tableModel.addRow(new Object[]{e.getId(),e.getNome(),e.getTipo(),e.getPlaca(),e.getKmAtual(),e.getHorasUso(),e.isAtivo()?"Sim":"Não"}); }
    private void editar(){ int row=tabela.getSelectedRow(); if(row<0){FormUtils.info("Selecione um registro para editar.");return;} int m=tabela.convertRowIndexToModel(row); Equipamento e=new Equipamento(); e.setId((int)tableModel.getValueAt(m,0)); e.setNome(String.valueOf(tableModel.getValueAt(m,1))); e.setTipo(String.valueOf(tableModel.getValueAt(m,2))); e.setPlaca(String.valueOf(tableModel.getValueAt(m,3))); e.setKmAtual(Double.parseDouble(tableModel.getValueAt(m,4).toString())); e.setHorasUso(Double.parseDouble(tableModel.getValueAt(m,5).toString())); e.setAtivo("Sim".equals(tableModel.getValueAt(m,6))); abrirDialog(e); }
    private void excluir(){ int id=FormUtils.selectedTableId(tabela); if(id<0){FormUtils.info("Selecione um registro para excluir.");return;} if(FormUtils.confirmar("Deseja excluir este equipamento?")){ controller.deletar(id); carregarTabela(); }}
    private void abrirDialog(Equipamento existente){ EquipamentoDialog d=new EquipamentoDialog(); if(existente!=null){ d.getTxtNome().setText(existente.getNome()); d.getTxtTipo().setText(existente.getTipo()); d.getTxtPlaca().setText(existente.getPlaca()); d.getTxtKmAtual().setText(String.valueOf(existente.getKmAtual())); d.getTxtHorasUso().setText(String.valueOf(existente.getHorasUso())); d.getChkAtivo().setSelected(existente.isAtivo()); }
        d.getBtnCancelar().addActionListener(e->d.dispose()); d.getBtnSalvar().addActionListener(e->{ try{ Equipamento obj=existente==null?new Equipamento():existente; obj.setNome(d.getTxtNome().getText().trim()); obj.setTipo(d.getTxtTipo().getText().trim()); obj.setPlaca(d.getTxtPlaca().getText().trim()); obj.setKmAtual(FormUtils.parseDouble(d.getTxtKmAtual().getText(),"KM Atual")); obj.setHorasUso(FormUtils.parseDouble(d.getTxtHorasUso().getText(),"Horas de Uso")); obj.setAtivo(d.getChkAtivo().isSelected()); if(obj.getNome().isBlank()||obj.getTipo().isBlank()) throw new IllegalArgumentException("Nome e tipo são obrigatórios."); if(existente==null) controller.salvar(obj); else controller.atualizar(obj); d.dispose(); carregarTabela(); }catch(Exception ex){FormUtils.erro(ex);} }); d.setVisible(true); }

    private void abrirHistorico(){
        int id=FormUtils.selectedTableId(tabela); if(id<0){FormUtils.info("Selecione um equipamento para visualizar o histórico.");return;}
        Equipamento equipamento=controller.buscarPorId(id); if(equipamento==null){FormUtils.info("Equipamento não encontrado.");return;}
        new HistoricoEquipamentoDialog(SwingUtilities.getWindowAncestor(this), equipamento).setVisible(true);
    }
    public JTable getTabela(){return tabela;} public DefaultTableModel getTableModel(){return tableModel;} public JButton getBtnNovo(){return btnNovo;} public JButton getBtnEditar(){return btnEditar;} public JButton getBtnExcluir(){return btnExcluir;} public JButton getBtnAtualizar(){return btnAtualizar;} public JButton getBtnExportar(){return btnExportar;}
}
