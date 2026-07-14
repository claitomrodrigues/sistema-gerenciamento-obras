package dao;

import connection.ConnectionFactory;
import model.Equipamento;
import model.OrdemServico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO extends DAOBase {
    private static final String SELECT_BASE = """
        SELECT os.id, os.numero, os.equipamento_id, e.nome equipamento_nome, e.placa equipamento_placa,
               os.descricao, os.local_servico, os.responsavel, os.prioridade,
               os.status, os.data_abertura, os.data_conclusao, os.observacoes
          FROM ordem_servico os
          LEFT JOIN equipamento e ON e.id = os.equipamento_id
        """;

    public void salvar(OrdemServico ordem) {
        executarAtualizacao("""
            INSERT INTO ordem_servico
            (numero, equipamento_id, descricao, local_servico, responsavel, prioridade, status,
             data_abertura, data_conclusao, observacoes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, ordem, false);
    }

    public void atualizar(OrdemServico ordem) {
        executarAtualizacao("""
            UPDATE ordem_servico SET numero=?, equipamento_id=?, descricao=?, local_servico=?, responsavel=?,
                   prioridade=?, status=?, data_abertura=?, data_conclusao=?, observacoes=? WHERE id=?
            """, ordem, true);
    }

    public void deletar(int id) {
        try (Connection conn=ConnectionFactory.getConnection(); PreparedStatement ps=conn.prepareStatement("DELETE FROM ordem_servico WHERE id=?")) {
            ps.setInt(1,id); if(ps.executeUpdate()==0) throw new IllegalArgumentException("Ordem de serviço não encontrada.");
        } catch(Exception e){ throw erro("Não foi possível excluir a ordem de serviço.",e); }
    }
    public List<OrdemServico> listar(){ return consultar(SELECT_BASE+" ORDER BY os.data_abertura DESC, os.id DESC"); }
    public List<OrdemServico> listarAbertas(){ return consultar(SELECT_BASE+" WHERE UPPER(os.status) NOT IN ('CONCLUIDA','CANCELADA') ORDER BY os.data_abertura, os.id"); }
    public List<OrdemServico> listarPorEquipamento(int equipamentoId){ return consultar(SELECT_BASE+" WHERE os.equipamento_id=? ORDER BY os.data_abertura DESC", equipamentoId); }
    public OrdemServico buscarPorId(int id){ var l=consultar(SELECT_BASE+" WHERE os.id=?",id); return l.isEmpty()?null:l.get(0); }

    private void executarAtualizacao(String sql, OrdemServico o, boolean id){
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            int i=1; ps.setString(i++,o.getNumero());
            if(o.getEquipamento()==null||o.getEquipamento().getId()<=0) ps.setNull(i++,Types.INTEGER); else ps.setInt(i++,o.getEquipamento().getId());
            ps.setString(i++,o.getDescricao()); ps.setString(i++,o.getLocalServico()); ps.setString(i++,o.getResponsavel());
            ps.setString(i++,o.getPrioridade()); ps.setString(i++,o.getStatus()); setLocalDate(ps,i++,o.getDataAbertura());
            setLocalDate(ps,i++,o.getDataConclusao()); ps.setString(i++,o.getObservacoes()); if(id) ps.setInt(i,o.getId());
            if(ps.executeUpdate()==0) throw new IllegalArgumentException("Ordem de serviço não encontrada.");
        } catch(Exception e){ throw erro("Não foi possível salvar a ordem de serviço.",e); }
    }
    private List<OrdemServico> consultar(String sql,Object... params){
        List<OrdemServico> out=new ArrayList<>();
        try(Connection c=ConnectionFactory.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            preencherParametros(ps,params); try(ResultSet rs=ps.executeQuery()){ while(rs.next()) out.add(mapear(rs)); }
        }catch(Exception e){ throw erro("Não foi possível consultar as ordens de serviço.",e); } return out;
    }
    private OrdemServico mapear(ResultSet rs)throws SQLException{
        OrdemServico o=new OrdemServico(); o.setId(rs.getInt("id")); o.setNumero(rs.getString("numero"));
        int eid=rs.getInt("equipamento_id"); if(!rs.wasNull()){ Equipamento e=new Equipamento(); e.setId(eid); e.setNome(rs.getString("equipamento_nome")); e.setPlaca(rs.getString("equipamento_placa")); o.setEquipamento(e); }
        o.setDescricao(rs.getString("descricao")); o.setLocalServico(rs.getString("local_servico")); o.setResponsavel(rs.getString("responsavel"));
        o.setPrioridade(rs.getString("prioridade")); o.setStatus(rs.getString("status")); o.setDataAbertura(getLocalDate(rs,"data_abertura"));
        o.setDataConclusao(getLocalDate(rs,"data_conclusao")); o.setObservacoes(rs.getString("observacoes")); return o;
    }
}
