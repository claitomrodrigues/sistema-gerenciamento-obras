package dao;

import connection.ConnectionFactory;
import model.Equipamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO extends DAOBase {

    public void salvar(Equipamento e) {
        String sql = "INSERT INTO equipamento (nome, tipo, placa, kmAtual, horasUso, ativo) VALUES (?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, e, false);
            stmt.executeUpdate();

        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Equipamento.", ex);
        }
    }

    public List<Equipamento> listar() {
        String sql = baseSelect() + " ORDER BY nome";
        return consultarLista(sql);
    }

    public List<Equipamento> listarAtivos() {
        String sql = baseSelect() + " WHERE ativo = 1 ORDER BY nome";
        return consultarLista(sql);
    }

    public List<Equipamento> listarInativos() {
        String sql = baseSelect() + " WHERE ativo = 0 ORDER BY nome";
        return consultarLista(sql);
    }

    public List<Equipamento> buscarPorNomeOuPlaca(String termo) {
        String sql = baseSelect() + " WHERE nome LIKE ? OR placa LIKE ? ORDER BY nome";
        String busca = "%" + termo + "%";
        return consultarLista(sql, busca, busca);
    }

    public Equipamento buscarPorId(int id) {
        String sql = baseSelect() + " WHERE id = ?";
        List<Equipamento> lista = consultarLista(sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void atualizar(Equipamento e) {
        String sql = "UPDATE equipamento SET nome=?, tipo=?, placa=?, kmAtual=?, horasUso=?, ativo=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, e, true);
            stmt.executeUpdate();

        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Equipamento.", ex);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM equipamento WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Equipamento.", ex);
        }
    }

    private String baseSelect() {
        return "SELECT id, nome, tipo, placa, kmAtual, horasUso, ativo FROM equipamento";
    }

    private List<Equipamento> consultarLista(String sql, Object... params) {
        List<Equipamento> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametros(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Equipamento.", ex);
        }

        return lista;
    }

    private Equipamento mapear(ResultSet rs) throws SQLException {
        Equipamento e = new Equipamento();
        e.setId(rs.getInt("id"));
        e.setNome(rs.getString("nome"));
        e.setTipo(rs.getString("tipo"));
        e.setPlaca(rs.getString("placa"));
        e.setKmAtual(rs.getDouble("kmAtual"));
        e.setHorasUso(rs.getDouble("horasUso"));
        e.setAtivo(rs.getInt("ativo") == 1);
        return e;
    }

    private void preencherStatement(PreparedStatement stmt, Equipamento e, boolean incluirId) throws SQLException {
        stmt.setString(1, e.getNome());
        stmt.setString(2, e.getTipo());
        stmt.setString(3, e.getPlaca());
        stmt.setDouble(4, e.getKmAtual());
        stmt.setDouble(5, e.getHorasUso());
        stmt.setInt(6, e.isAtivo() ? 1 : 0);

        if (incluirId) {
            stmt.setInt(7, e.getId());
        }
    }

}
