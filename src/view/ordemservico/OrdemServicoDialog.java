package view.ordemservico;

import controller.EquipamentoController;
import model.Equipamento;
import model.OrdemServico;
import util.FormUtils;
import view.StyleUtils;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class OrdemServicoDialog extends JDialog {
    private final JTextField txtNumero = new JTextField();
    private final JComboBox<Equipamento> cbEquipamento = new JComboBox<>();
    private final JTextArea txtDescricao = new JTextArea(4, 30);
    private final JTextField txtLocal = new JTextField();
    private final JTextField txtResponsavel = new JTextField();
    private final JComboBox<String> cbPrioridade = new JComboBox<>(new String[]{"BAIXA","NORMAL","ALTA","URGENTE"});
    private final JComboBox<String> cbStatus = new JComboBox<>(new String[]{"ABERTA","EM_ANDAMENTO","CONCLUIDA","CANCELADA"});
    private final JTextField txtAbertura = new JTextField(LocalDate.now().toString());
    private final JTextField txtConclusao = new JTextField();
    private final JTextArea txtObservacoes = new JTextArea(3, 30);
    private boolean confirmado;

    public OrdemServicoDialog(Window owner, OrdemServico ordem) {
        super(owner, ordem == null ? "Nova ordem de serviço" : "Editar ordem de serviço", ModalityType.APPLICATION_MODAL);
        construir(); carregarEquipamentos();
        if (ordem != null) preencher(ordem);
        pack(); setMinimumSize(new Dimension(620, 620)); setLocationRelativeTo(owner);
    }

    private void construir() {
        JPanel form = new JPanel(new GridBagLayout());
        StyleUtils.styleView(form);
        GridBagConstraints c = new GridBagConstraints(); c.insets=new Insets(6,8,6,8); c.fill=GridBagConstraints.HORIZONTAL; c.weightx=1;
        int y=0;
        add(form,c,y++,"Número",txtNumero); add(form,c,y++,"Equipamento",cbEquipamento); add(form,c,y++,"Descrição",new JScrollPane(txtDescricao));
        add(form,c,y++,"Local do serviço",txtLocal); add(form,c,y++,"Responsável",txtResponsavel); add(form,c,y++,"Prioridade",cbPrioridade);
        add(form,c,y++,"Status",cbStatus); add(form,c,y++,"Data de abertura (AAAA-MM-DD)",txtAbertura); add(form,c,y++,"Data de conclusão (AAAA-MM-DD)",txtConclusao);
        add(form,c,y++,"Observações",new JScrollPane(txtObservacoes));
        JButton salvar=new JButton("Salvar"); JButton cancelar=new JButton("Cancelar"); StyleUtils.styleToolbarButton(salvar); StyleUtils.styleSecondaryButton(cancelar);
        JPanel botoes=new JPanel(new FlowLayout(FlowLayout.RIGHT)); botoes.add(cancelar); botoes.add(salvar);
        salvar.addActionListener(e->{ confirmado=true; dispose(); }); cancelar.addActionListener(e->dispose());
        getRootPane().setDefaultButton(salvar); getRootPane().registerKeyboardAction(e->dispose(),KeyStroke.getKeyStroke("ESCAPE"),JComponent.WHEN_IN_FOCUSED_WINDOW);
        setLayout(new BorderLayout()); add(new JScrollPane(form),BorderLayout.CENTER); add(botoes,BorderLayout.SOUTH);
    }
    private void add(JPanel p,GridBagConstraints c,int y,String label,Component comp){ c.gridy=y;c.gridx=0;c.weightx=0;p.add(new JLabel(label),c);c.gridx=1;c.weightx=1;p.add(comp,c); }
    private void carregarEquipamentos(){ cbEquipamento.addItem(null); for(Equipamento e:new EquipamentoController().listar()) cbEquipamento.addItem(e); }
    private void preencher(OrdemServico o){ txtNumero.setText(o.getNumero()); selecionar(o.getEquipamento()); txtDescricao.setText(o.getDescricao()); txtLocal.setText(o.getLocalServico()); txtResponsavel.setText(o.getResponsavel()); cbPrioridade.setSelectedItem(o.getPrioridade()); cbStatus.setSelectedItem(o.getStatus()); txtAbertura.setText(o.getDataAberturaFormatada().isEmpty()?"":o.getDataAbertura().toString()); txtConclusao.setText(o.getDataConclusao()==null?"":o.getDataConclusao().toString()); txtObservacoes.setText(o.getObservacoes()); }
    private void selecionar(Equipamento alvo){ if(alvo==null)return; for(int i=0;i<cbEquipamento.getItemCount();i++){ Equipamento e=cbEquipamento.getItemAt(i); if(e!=null&&e.getId()==alvo.getId()){cbEquipamento.setSelectedIndex(i);break;} } }
    public boolean isConfirmado(){return confirmado;}
    public void aplicar(OrdemServico o){ o.setNumero(txtNumero.getText().trim()); o.setEquipamento((Equipamento)cbEquipamento.getSelectedItem()); o.setDescricao(txtDescricao.getText().trim()); o.setLocalServico(txtLocal.getText().trim()); o.setResponsavel(txtResponsavel.getText().trim()); o.setPrioridade(String.valueOf(cbPrioridade.getSelectedItem())); o.setStatus(String.valueOf(cbStatus.getSelectedItem())); o.setDataAbertura(FormUtils.parseDate(txtAbertura.getText(),"Data de abertura")); o.setDataConclusao(txtConclusao.getText().isBlank()?null:FormUtils.parseDate(txtConclusao.getText(),"Data de conclusão")); o.setObservacoes(txtObservacoes.getText().trim()); }
}
