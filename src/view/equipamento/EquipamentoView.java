package view.equipamento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.EquipamentoController;
import model.Equipamento;
import util.FormUtils; import view.StyleUtils;

public class EquipamentoView extends JPanel {
    private JTable tabela; private DefaultTableModel tableModel;
    private JButton btnNovo, btnEditar, btnExcluir, btnAtualizar;
    private EquipamentoController controller = new EquipamentoController();
    public EquipamentoView() { inicializar(); configurarEventos(); carregarTabela(); }
    private void inicializar() {
        setLayout(new BorderLayout()); StyleUtils.styleView(this); JPanel painelBotoes = StyleUtils.toolbar();
        btnNovo = new JButton("Novo"); btnEditar = new JButton("Editar"); btnExcluir = new JButton("Excluir"); btnAtualizar = new JButton("Atualizar"); StyleUtils.styleToolbarButton(btnNovo); StyleUtils.styleToolbarButton(btnEditar); StyleUtils.styleDangerButton(btnExcluir); StyleUtils.styleToolbarButton(btnAtualizar);
        painelBotoes.add(btnNovo); painelBotoes.add(btnEditar); painelBotoes.add(btnExcluir); painelBotoes.add(btnAtualizar); add(painelBotoes, BorderLayout.NORTH);
        tableModel = new DefaultTableModel(new String[]{"ID","Nome","Tipo","Placa","KM Atual","Horas de Uso","Ativo"},0){ public boolean isCellEditable(int r,int c){return false;}};
        tabela = new JTable(tableModel); add(StyleUtils.tableScroll(tabela), BorderLayout.CENTER);
    }
    private void configurarEventos(){ btnNovo.addActionListener(e->abrirDialog(null)); btnEditar.addActionListener(e->editar()); btnExcluir.addActionListener(e->excluir()); btnAtualizar.addActionListener(e->carregarTabela()); }
    private void carregarTabela(){ tableModel.setRowCount(0); for(Equipamento e: controller.listar()) tableModel.addRow(new Object[]{e.getId(),e.getNome(),e.getTipo(),e.getPlaca(),e.getKmAtual(),e.getHorasUso(),e.isAtivo()?"Sim":"Não"}); }
    private void editar(){ int row=tabela.getSelectedRow(); if(row<0){FormUtils.info("Selecione um registro para editar.");return;} int m=tabela.convertRowIndexToModel(row); Equipamento e=new Equipamento(); e.setId((int)tableModel.getValueAt(m,0)); e.setNome(String.valueOf(tableModel.getValueAt(m,1))); e.setTipo(String.valueOf(tableModel.getValueAt(m,2))); e.setPlaca(String.valueOf(tableModel.getValueAt(m,3))); e.setKmAtual(Double.parseDouble(tableModel.getValueAt(m,4).toString())); e.setHorasUso(Double.parseDouble(tableModel.getValueAt(m,5).toString())); e.setAtivo("Sim".equals(tableModel.getValueAt(m,6))); abrirDialog(e); }
    private void excluir(){ int id=FormUtils.selectedTableId(tabela); if(id<0){FormUtils.info("Selecione um registro para excluir.");return;} if(FormUtils.confirmar("Deseja excluir este equipamento?")){ controller.deletar(id); carregarTabela(); }}
    private void abrirDialog(Equipamento existente){ EquipamentoDialog d=new EquipamentoDialog(); if(existente!=null){ d.getTxtNome().setText(existente.getNome()); d.getTxtTipo().setText(existente.getTipo()); d.getTxtPlaca().setText(existente.getPlaca()); d.getTxtKmAtual().setText(String.valueOf(existente.getKmAtual())); d.getTxtHorasUso().setText(String.valueOf(existente.getHorasUso())); d.getChkAtivo().setSelected(existente.isAtivo()); }
        d.getBtnCancelar().addActionListener(e->d.dispose()); d.getBtnSalvar().addActionListener(e->{ try{ Equipamento obj=existente==null?new Equipamento():existente; obj.setNome(d.getTxtNome().getText().trim()); obj.setTipo(d.getTxtTipo().getText().trim()); obj.setPlaca(d.getTxtPlaca().getText().trim()); obj.setKmAtual(FormUtils.parseDouble(d.getTxtKmAtual().getText(),"KM Atual")); obj.setHorasUso(FormUtils.parseDouble(d.getTxtHorasUso().getText(),"Horas de Uso")); obj.setAtivo(d.getChkAtivo().isSelected()); if(obj.getNome().isBlank()||obj.getTipo().isBlank()) throw new IllegalArgumentException("Nome e tipo são obrigatórios."); if(existente==null) controller.salvar(obj); else controller.atualizar(obj); d.dispose(); carregarTabela(); }catch(Exception ex){FormUtils.erro(ex);} }); d.setVisible(true); }
    public JTable getTabela(){return tabela;} public DefaultTableModel getTableModel(){return tableModel;} public JButton getBtnNovo(){return btnNovo;} public JButton getBtnEditar(){return btnEditar;} public JButton getBtnExcluir(){return btnExcluir;} public JButton getBtnAtualizar(){return btnAtualizar;}
}
