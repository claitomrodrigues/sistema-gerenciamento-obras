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
        String inserir = "INSERT INTO abastecimento (equipamento_id, combustivel_id, litros, data) VALUES (?,?,?,?)";
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                baixarEstoque(conn, a.getCombustivel().getId(), a.getLitros());
                try (PreparedStatement stmt = conn.prepareStatement(inserir)) {
                    preencherStatement(stmt, a, false);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (Exception e) {
                rollback(conn);
                throw e;
            }
        } catch (Exception e) {
            throw erro("Não foi possível registrar o abastecimento. Verifique o estoque disponível.", e);
        }
    }

    public List<Abastecimento> listar() { return consultarLista(baseSelect() + " ORDER BY a.id DESC"); }

    public List<Abastecimento> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return consultarLista(baseSelect() + " WHERE date(a.data) BETWEEN date(?) AND date(?) ORDER BY a.data DESC, a.id DESC", inicio, fim);
    }

    public List<Abastecimento> listarPorEquipamento(int equipamentoId) {
        return consultarLista(baseSelect() + " WHERE a.equipamento_id = ? ORDER BY a.data DESC, a.id DESC", equipamentoId);
    }

    public List<Abastecimento> listarPorCombustivel(int combustivelId) {
        return consultarLista(baseSelect() + " WHERE a.combustivel_id = ? ORDER BY a.data DESC, a.id DESC", combustivelId);
    }

    public Abastecimento buscarPorId(int id) {
        List<Abastecimento> lista = consultarLista(baseSelect() + " WHERE a.id = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public double totalLitrosPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT COALESCE(SUM(litros), 0) AS total FROM abastecimento WHERE date(data) BETWEEN date(?) AND date(?)";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            setLocalDate(stmt, 1, inicio); setLocalDate(stmt, 2, fim);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next() ? rs.getDouble("total") : 0; }
        } catch (Exception e) { throw erro("Erro ao totalizar abastecimentos.", e); }
    }

    public void atualizar(Abastecimento novo) {
        String atualizar = "UPDATE abastecimento SET equipamento_id=?, combustivel_id=?, litros=?, data=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Abastecimento antigo = buscarPorId(conn, novo.getId());
                if (antigo == null) throw new IllegalArgumentException("Abastecimento não encontrado.");
                devolverEstoque(conn, antigo.getCombustivel().getId(), antigo.getLitros());
                baixarEstoque(conn, novo.getCombustivel().getId(), novo.getLitros());
                try (PreparedStatement stmt = conn.prepareStatement(atualizar)) {
                    preencherStatement(stmt, novo, true);
                    if (stmt.executeUpdate() == 0) throw new IllegalArgumentException("Abastecimento não encontrado.");
                }
                conn.commit();
            } catch (Exception e) { rollback(conn); throw e; }
        } catch (Exception e) { throw erro("Não foi possível atualizar o abastecimento. Verifique o estoque disponível.", e); }
    }

    public void deletar(int id) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Abastecimento antigo = buscarPorId(conn, id);
                if (antigo == null) throw new IllegalArgumentException("Abastecimento não encontrado.");
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM abastecimento WHERE id=?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                devolverEstoque(conn, antigo.getCombustivel().getId(), antigo.getLitros());
                conn.commit();
            } catch (Exception e) { rollback(conn); throw e; }
        } catch (Exception e) { throw erro("Não foi possível excluir o abastecimento.", e); }
    }

    private void baixarEstoque(Connection conn, int combustivelId, double litros) throws SQLException {
        String sql = "UPDATE combustivel SET litros = litros - ? WHERE id = ? AND litros >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, litros); stmt.setInt(2, combustivelId); stmt.setDouble(3, litros);
            if (stmt.executeUpdate() == 0) throw new IllegalArgumentException("Estoque de combustível insuficiente.");
        }
    }

    private void devolverEstoque(Connection conn, int combustivelId, double litros) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE combustivel SET litros = litros + ? WHERE id = ?")) {
            stmt.setDouble(1, litros); stmt.setInt(2, combustivelId);
            if (stmt.executeUpdate() == 0) throw new IllegalArgumentException("Combustível não encontrado.");
        }
    }

    private Abastecimento buscarPorId(Connection conn, int id) throws SQLException {
        String sql = baseSelect() + " WHERE a.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }

    private String baseSelect() {
        return """
            SELECT a.id, a.equipamento_id, a.combustivel_id, a.litros, a.data,
                   e.nome AS equipamento_nome, c.tipo AS combustivel_tipo
            FROM abastecimento a
            LEFT JOIN equipamento e ON e.id = a.equipamento_id
            LEFT JOIN combustivel c ON c.id = a.combustivel_id
        """;
    }

    private List<Abastecimento> consultarLista(String sql, Object... params) {
        List<Abastecimento> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            preencherParametros(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) { while (rs.next()) lista.add(mapear(rs)); }
        } catch (Exception e) { throw erro("Erro ao consultar abastecimentos.", e); }
        return lista;
    }

    private Abastecimento mapear(ResultSet rs) throws SQLException {
        Abastecimento a = new Abastecimento();
        a.setId(rs.getInt("id")); a.setLitros(rs.getDouble("litros")); a.setData(getLocalDate(rs, "data"));
        Equipamento equipamento = new Equipamento();
        equipamento.setId(rs.getInt("equipamento_id")); equipamento.setNome(rs.getString("equipamento_nome"));
        a.setEquipamento(equipamento);
        Combustivel combustivel = new Combustivel();
        combustivel.setId(rs.getInt("combustivel_id")); combustivel.setTipo(rs.getString("combustivel_tipo"));
        a.setCombustivel(combustivel);
        return a;
    }

    private void preencherStatement(PreparedStatement stmt, Abastecimento a, boolean incluirId) throws SQLException {
        stmt.setInt(1, a.getEquipamento().getId()); stmt.setInt(2, a.getCombustivel().getId());
        stmt.setDouble(3, a.getLitros()); setLocalDate(stmt, 4, a.getData());
        if (incluirId) stmt.setInt(5, a.getId());
    }

}
