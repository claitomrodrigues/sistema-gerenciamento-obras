package view.ordemservico;

import controller.OrdemServicoController;
import model.OrdemServico;
import security.PermissionManager;
import util.FormUtils;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrdemServicoView extends JPanel {
    private final OrdemServicoController controller=new OrdemServicoController();
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID","Número","Equipamento","Descrição","Local","Responsável","Prioridade","Status","Abertura","Conclusão"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabela=new JTable(model);
    public OrdemServicoView(){ setLayout(new BorderLayout()); StyleUtils.styleView(this); JPanel bar=StyleUtils.toolbar(); JButton novo=new JButton("Nova"); JButton editar=new JButton("Editar"); JButton concluir=new JButton("Concluir"); JButton excluir=new JButton("Excluir"); JButton atualizar=new JButton("Atualizar"); StyleUtils.styleToolbarButton(novo);StyleUtils.styleToolbarButton(editar);StyleUtils.styleSecondaryButton(concluir);StyleUtils.styleDangerButton(excluir);StyleUtils.styleSecondaryButton(atualizar); for(JButton b:new JButton[]{novo,editar,concluir,excluir,atualizar})bar.add(b); PermissionManager.aplicarCrud(this,"ordem_servico",novo,editar,excluir); add(StyleUtils.criarTopoModulo("Ordens de Serviço","Planeje e acompanhe os serviços da Secretaria de Obras",bar),BorderLayout.NORTH); StyleUtils.adicionarPesquisa(bar,tabela); add(StyleUtils.tableScroll(tabela),BorderLayout.CENTER); novo.addActionListener(e->editar(null)); editar.addActionListener(e->editarSelecionada()); concluir.addActionListener(e->concluir()); excluir.addActionListener(e->excluir()); atualizar.addActionListener(e->carregar()); tabela.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2)editarSelecionada();}}); carregar(); }
    private void carregar(){model.setRowCount(0);for(OrdemServico o:controller.listar())model.addRow(new Object[]{o.getId(),o.getNumero(),o.getEquipamento()==null?"-":o.getEquipamento().getNome(),o.getDescricao(),o.getLocalServico(),o.getResponsavel(),o.getPrioridade(),o.getStatus(),o.getDataAberturaFormatada(),o.getDataConclusaoFormatada()});}
    private OrdemServico selecionada(){int id=FormUtils.selectedTableId(tabela);return id<0?null:controller.buscarPorId(id);}
    private void editarSelecionada(){OrdemServico o=selecionada();if(o==null){FormUtils.info("Selecione uma ordem de serviço.");return;}editar(o);}
    private void editar(OrdemServico o){try{OrdemServicoDialog d=new OrdemServicoDialog(SwingUtilities.getWindowAncestor(this),o);d.setVisible(true);if(!d.isConfirmado())return;OrdemServico alvo=o==null?new OrdemServico():o;d.aplicar(alvo);if(o==null)controller.salvar(alvo);else controller.atualizar(alvo);carregar();}catch(Exception ex){FormUtils.erro(ex);}}
    private void concluir(){try{OrdemServico o=selecionada();if(o==null){FormUtils.info("Selecione uma ordem de serviço.");return;}controller.concluir(o);carregar();}catch(Exception ex){FormUtils.erro(ex);}}
    private void excluir(){try{int id=FormUtils.selectedTableId(tabela);if(id<0){FormUtils.info("Selecione uma ordem de serviço.");return;}if(FormUtils.confirmar(this,"Deseja excluir a ordem de serviço selecionada?")){controller.deletar(id);carregar();}}catch(Exception ex){FormUtils.erro(ex);}}
}
