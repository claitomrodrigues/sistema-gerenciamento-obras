package dao;

import connection.ConnectionFactory;
import model.Equipamento;
import model.Ocorrencia;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaDAO extends DAOBase {

    public void salvar(Ocorrencia o) {
        String sql = "INSERT INTO ocorrencia (equipamento_id, descricao, data, status) VALUES (?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, o, false);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Ocorrencia.", e);
        }
    }

    public List<Ocorrencia> listar() {
        String sql = baseSelect() + " ORDER BY o.id DESC";
        return consultarLista(sql);
    }

    public List<Ocorrencia> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = baseSelect() + " WHERE date(o.data) BETWEEN date(?) AND date(?) ORDER BY o.data DESC, o.id DESC";
        return consultarLista(sql, inicio, fim);
    }

    public List<Ocorrencia> listarPorStatus(String status) {
        String sql = baseSelect() + " WHERE o.status LIKE ? ORDER BY o.data DESC, o.id DESC";
        return consultarLista(sql, "%" + status + "%");
    }

    public List<Ocorrencia> listarPorEquipamento(int equipamentoId) {
        String sql = baseSelect() + " WHERE o.equipamento_id = ? ORDER BY o.data DESC, o.id DESC";
        return consultarLista(sql, equipamentoId);
    }

    public Ocorrencia buscarPorId(int id) {
        String sql = baseSelect() + " WHERE o.id = ?";
        List<Ocorrencia> lista = consultarLista(sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void atualizar(Ocorrencia o) {
        String sql = "UPDATE ocorrencia SET equipamento_id=?, descricao=?, data=?, status=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, o, true);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Ocorrencia.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM ocorrencia WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Ocorrencia.", e);
        }
    }

    private String baseSelect() {
        return """
            SELECT
                o.id,
                o.equipamento_id,
                o.descricao,
                o.data,
                o.status,
                e.nome AS equipamento_nome
            FROM ocorrencia o
            LEFT JOIN equipamento e ON e.id = o.equipamento_id
        """;
    }

    private List<Ocorrencia> consultarLista(String sql, Object... params) {
        List<Ocorrencia> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametros(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Ocorrencia.", e);
        }

        return lista;
    }

    private Ocorrencia mapear(ResultSet rs) throws SQLException {
        Ocorrencia o = new Ocorrencia();
        o.setId(rs.getInt("id"));
        o.setDescricao(rs.getString("descricao"));
        o.setData(getLocalDate(rs, "data"));
        o.setStatus(rs.getString("status"));

        Equipamento equipamento = new Equipamento();
        equipamento.setId(rs.getInt("equipamento_id"));
        equipamento.setNome(rs.getString("equipamento_nome"));
        o.setEquipamento(equipamento);

        return o;
    }

    private void preencherStatement(PreparedStatement stmt, Ocorrencia o, boolean incluirId) throws SQLException {
        stmt.setInt(1, o.getEquipamento().getId());
        stmt.setString(2, o.getDescricao());
        setLocalDate(stmt, 3, o.getData());
        stmt.setString(4, o.getStatus());

        if (incluirId) {
            stmt.setInt(5, o.getId());
        }
    }
}
