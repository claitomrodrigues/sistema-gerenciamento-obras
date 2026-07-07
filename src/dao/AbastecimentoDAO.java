package dao;

import connection.ConnectionFactory;
import model.Abastecimento;
import model.Combustivel;
import model.Equipamento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbastecimentoDAO extends DAOBase {

    public void salvar(Abastecimento a) {
        String sql = "INSERT INTO abastecimento (equipamento_id, combustivel_id, litros, data) VALUES (?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, a, false);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Abastecimento.", e);
        }
    }

    public List<Abastecimento> listar() {
        String sql = baseSelect() + " ORDER BY a.id DESC";
        return consultarLista(sql);
    }

    public List<Abastecimento> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = baseSelect() + " WHERE date(a.data) BETWEEN date(?) AND date(?) ORDER BY a.data DESC, a.id DESC";
        return consultarLista(sql, inicio, fim);
    }

    public List<Abastecimento> listarPorEquipamento(int equipamentoId) {
        String sql = baseSelect() + " WHERE a.equipamento_id = ? ORDER BY a.data DESC, a.id DESC";
        return consultarLista(sql, equipamentoId);
    }

    public List<Abastecimento> listarPorCombustivel(int combustivelId) {
        String sql = baseSelect() + " WHERE a.combustivel_id = ? ORDER BY a.data DESC, a.id DESC";
        return consultarLista(sql, combustivelId);
    }

    public Abastecimento buscarPorId(int id) {
        String sql = baseSelect() + " WHERE a.id = ?";
        List<Abastecimento> lista = consultarLista(sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public double totalLitrosPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT COALESCE(SUM(litros), 0) AS total FROM abastecimento WHERE date(data) BETWEEN date(?) AND date(?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setLocalDate(stmt, 1, inicio);
            setLocalDate(stmt, 2, fim);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Abastecimento.", e);
        }

        return 0;
    }

    public void atualizar(Abastecimento a) {
        String sql = "UPDATE abastecimento SET equipamento_id=?, combustivel_id=?, litros=?, data=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, a, true);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Abastecimento.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM abastecimento WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Abastecimento.", e);
        }
    }

    private String baseSelect() {
        return """
            SELECT
                a.id,
                a.equipamento_id,
                a.combustivel_id,
                a.litros,
                a.data,
                e.nome AS equipamento_nome,
                c.tipo AS combustivel_tipo
            FROM abastecimento a
            LEFT JOIN equipamento e ON e.id = a.equipamento_id
            LEFT JOIN combustivel c ON c.id = a.combustivel_id
        """;
    }

    private List<Abastecimento> consultarLista(String sql, Object... params) {
        List<Abastecimento> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametros(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Abastecimento.", e);
        }

        return lista;
    }

    private Abastecimento mapear(ResultSet rs) throws SQLException {
        Abastecimento a = new Abastecimento();
        a.setId(rs.getInt("id"));
        a.setLitros(rs.getDouble("litros"));
        a.setData(getLocalDate(rs, "data"));

        Equipamento equipamento = new Equipamento();
        equipamento.setId(rs.getInt("equipamento_id"));
        equipamento.setNome(rs.getString("equipamento_nome"));
        a.setEquipamento(equipamento);

        Combustivel combustivel = new Combustivel();
        combustivel.setId(rs.getInt("combustivel_id"));
        combustivel.setTipo(rs.getString("combustivel_tipo"));
        a.setCombustivel(combustivel);

        return a;
    }

    private void preencherStatement(PreparedStatement stmt, Abastecimento a, boolean incluirId) throws SQLException {
        stmt.setInt(1, a.getEquipamento().getId());
        stmt.setInt(2, a.getCombustivel().getId());
        stmt.setDouble(3, a.getLitros());
        setLocalDate(stmt, 4, a.getData());

        if (incluirId) {
            stmt.setInt(5, a.getId());
        }
    }
}
