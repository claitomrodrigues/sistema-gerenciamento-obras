package dao;

import connection.ConnectionFactory;
import model.Combustivel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CombustivelDAO extends DAOBase {

    public void salvar(Combustivel c) {
        String sql = "INSERT INTO combustivel (tipo, litros, estoqueMinimo) VALUES (?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, c, false);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Combustivel.", e);
        }
    }

    public List<Combustivel> listar() {
        String sql = "SELECT id, tipo, litros, estoqueMinimo FROM combustivel ORDER BY tipo";
        return consultarLista(sql);
    }

    public List<Combustivel> listarComEstoqueBaixo() {
        String sql = """
            SELECT id, tipo, litros, estoqueMinimo
            FROM combustivel
            WHERE litros <= estoqueMinimo
            ORDER BY tipo
        """;
        return consultarLista(sql);
    }

    public Combustivel buscarPorId(int id) {
        String sql = "SELECT id, tipo, litros, estoqueMinimo FROM combustivel WHERE id = ?";
        List<Combustivel> lista = consultarLista(sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public Combustivel buscarPorTipo(String tipo) {
        String sql = "SELECT id, tipo, litros, estoqueMinimo FROM combustivel WHERE tipo LIKE ? ORDER BY tipo LIMIT 1";
        List<Combustivel> lista = consultarLista(sql, "%" + tipo + "%");
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void atualizar(Combustivel c) {
        String sql = "UPDATE combustivel SET tipo=?, litros=?, estoqueMinimo=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, c, true);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Combustivel.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM combustivel WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Combustivel.", e);
        }
    }

    private List<Combustivel> consultarLista(String sql, Object... params) {
        List<Combustivel> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametros(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Combustivel.", e);
        }

        return lista;
    }

    private Combustivel mapear(ResultSet rs) throws SQLException {
        Combustivel c = new Combustivel();
        c.setId(rs.getInt("id"));
        c.setTipo(rs.getString("tipo"));
        c.setLitros(rs.getDouble("litros"));
        c.setEstoqueMinimo(rs.getDouble("estoqueMinimo"));
        return c;
    }

    private void preencherStatement(PreparedStatement stmt, Combustivel c, boolean incluirId) throws SQLException {
        stmt.setString(1, c.getTipo());
        stmt.setDouble(2, c.getLitros());
        stmt.setDouble(3, c.getEstoqueMinimo());

        if (incluirId) {
            stmt.setInt(4, c.getId());
        }
    }

}
