package view.equipamento;

import controller.*;
import model.*;
import view.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoricoEquipamentoDialog extends JDialog {
    public HistoricoEquipamentoDialog(Window owner, Equipamento equipamento){ super(owner,"Histórico - "+equipamento.getNome(),ModalityType.APPLICATION_MODAL); setLayout(new BorderLayout()); JTabbedPane tabs=new JTabbedPane(); tabs.add("Abastecimentos",abastecimentos(equipamento)); tabs.add("Manutenções",manutencoes(equipamento)); tabs.add("Ocorrências",ocorrencias(equipamento)); tabs.add("Ordens de Serviço",ordens(equipamento)); add(tabs,BorderLayout.CENTER); JLabel resumo=new JLabel("  KM atual: "+equipamento.getKmAtual()+"   |   Horas de uso: "+equipamento.getHorasUso()+"   |   Situação: "+equipamento.getStatusTexto()); resumo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); add(resumo,BorderLayout.NORTH); setSize(900,520);setLocationRelativeTo(owner);getRootPane().registerKeyboardAction(e->dispose(),KeyStroke.getKeyStroke("ESCAPE"),JComponent.WHEN_IN_FOCUSED_WINDOW); }
    private JScrollPane tabela(String[] cols, java.util.List<Object[]> rows){DefaultTableModel m=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};for(Object[]r:rows)m.addRow(r);JTable t=new JTable(m);return StyleUtils.tableScroll(t);}
    private JScrollPane abastecimentos(Equipamento e){java.util.List<Object[]>r=new java.util.ArrayList<>();double total=0;for(Abastecimento a:new AbastecimentoController().listar())if(a.getEquipamento()!=null&&a.getEquipamento().getId()==e.getId()){r.add(new Object[]{a.getDataFormatada(),a.getCombustivel()==null?"-":a.getCombustivel().getTipo(),a.getLitros()});total+=a.getLitros();}r.add(new Object[]{"TOTAL","",total});return tabela(new String[]{"Data","Combustível","Litros"},r);}
    private JScrollPane manutencoes(Equipamento e){java.util.List<Object[]>r=new java.util.ArrayList<>();for(Manutencao m:new ManutencaoController().listar())if(m.getEquipamento()!=null&&m.getEquipamento().getId()==e.getId())r.add(new Object[]{m.getDataFormatada(),m.getDescricao(),m.getRevisaoAtual(),m.getProximaRevisao()});return tabela(new String[]{"Data","Descrição","Revisão atual","Próxima revisão"},r);}
    private JScrollPane ocorrencias(Equipamento e){java.util.List<Object[]>r=new java.util.ArrayList<>();for(Ocorrencia o:new OcorrenciaController().listar())if(o.getEquipamento()!=null&&o.getEquipamento().getId()==e.getId())r.add(new Object[]{o.getDataFormatada(),o.getDescricao(),o.getStatus()});return tabela(new String[]{"Data","Descrição","Status"},r);}
    private JScrollPane ordens(Equipamento e){java.util.List<Object[]>r=new java.util.ArrayList<>();for(OrdemServico o:new OrdemServicoController().listarPorEquipamento(e.getId()))r.add(new Object[]{o.getNumero(),o.getDataAberturaFormatada(),o.getDescricao(),o.getPrioridade(),o.getStatus()});return tabela(new String[]{"Número","Abertura","Descrição","Prioridade","Status"},r);}
}
