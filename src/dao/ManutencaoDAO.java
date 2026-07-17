package dao;

import connection.ConnectionFactory;
import model.Equipamento;
import model.Manutencao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoDAO extends DAOBase {

    public void salvar(Manutencao m) {
        String sql = "INSERT INTO manutencao "
                + "(equipamento_id, descricao, revisaoAtual, proximaRevisao, valor, data) "
                + "VALUES (?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, m, false);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Manutencao.", e);
        }
    }

    public List<Manutencao> listar() {
        String sql = baseSelect() + " ORDER BY m.id DESC";
        return consultarLista(sql);
    }

    public List<Manutencao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = baseSelect()
                + " WHERE date(m.data) BETWEEN date(?) AND date(?) ORDER BY m.data DESC, m.id DESC";
        return consultarLista(sql, inicio, fim);
    }

    public List<Manutencao> listarPorEquipamento(int equipamentoId) {
        String sql = baseSelect() + " WHERE m.equipamento_id = ? ORDER BY m.data DESC, m.id DESC";
        return consultarLista(sql, equipamentoId);
    }

    public List<Manutencao> listarProximasRevisoes(double limite) {
        String sql = baseSelect() + " WHERE m.proximaRevisao <= ? ORDER BY m.proximaRevisao ASC";
        return consultarLista(sql, limite);
    }

    public Manutencao buscarPorId(int id) {
        String sql = baseSelect() + " WHERE m.id = ?";
        List<Manutencao> lista = consultarLista(sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void atualizar(Manutencao m) {
        String sql = "UPDATE manutencao SET equipamento_id=?, descricao=?, revisaoAtual=?, "
                + "proximaRevisao=?, valor=?, data=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, m, true);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Manutencao.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM manutencao WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Manutencao.", e);
        }
    }

    private String baseSelect() {
        return """
            SELECT
                m.id,
                m.equipamento_id,
                m.descricao,
                m.revisaoAtual,
                m.proximaRevisao,
                m.valor,
                m.data,
                e.nome AS equipamento_nome
            FROM manutencao m
            LEFT JOIN equipamento e ON e.id = m.equipamento_id
        """;
    }

    private List<Manutencao> consultarLista(String sql, Object... params) {
        List<Manutencao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametros(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Manutencao.", e);
        }

        return lista;
    }

    private Manutencao mapear(ResultSet rs) throws SQLException {
        Manutencao m = new Manutencao();
        m.setId(rs.getInt("id"));
        m.setDescricao(rs.getString("descricao"));
        m.setRevisaoAtual(rs.getDouble("revisaoAtual"));
        m.setProximaRevisao(rs.getDouble("proximaRevisao"));
        m.setValor(rs.getDouble("valor"));
        m.setData(getLocalDate(rs, "data"));

        Equipamento equipamento = new Equipamento();
        equipamento.setId(rs.getInt("equipamento_id"));
        equipamento.setNome(rs.getString("equipamento_nome"));
        m.setEquipamento(equipamento);

        return m;
    }

    private void preencherStatement(PreparedStatement stmt, Manutencao m, boolean incluirId) throws SQLException {
        stmt.setInt(1, m.getEquipamento().getId());
        stmt.setString(2, m.getDescricao());
        stmt.setDouble(3, m.getRevisaoAtual());
        stmt.setDouble(4, m.getProximaRevisao());
        stmt.setDouble(5, m.getValor());
        setLocalDate(stmt, 6, m.getData());

        if (incluirId) {
            stmt.setInt(7, m.getId());
        }
    }
}
