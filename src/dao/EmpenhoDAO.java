package dao;

import connection.ConnectionFactory;
import model.Empenho;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpenhoDAO extends DAOBase {

    public void salvar(Empenho e) {
        String sql = "INSERT INTO empenho (numero, descricao, categoria, valor, saldo, fornecedor, data) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, e, false);
            stmt.executeUpdate();

        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Empenho.", ex);
        }
    }

    public List<Empenho> listar() {
        String sql = baseSelect() + " ORDER BY data DESC, id DESC";
        return consultarLista(sql);
    }

    public List<Empenho> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = baseSelect() + " WHERE date(data) BETWEEN date(?) AND date(?) ORDER BY data DESC, id DESC";
        return consultarLista(sql, inicio, fim);
    }

    public List<Empenho> listarPorCategoria(String categoria) {
        String sql = baseSelect() + " WHERE categoria LIKE ? ORDER BY data DESC, id DESC";
        return consultarLista(sql, "%" + categoria + "%");
    }

    public List<Empenho> listarComSaldo() {
        String sql = baseSelect() + " WHERE saldo > 0 ORDER BY saldo DESC";
        return consultarLista(sql);
    }

    public Empenho buscarPorId(int id) {
        String sql = baseSelect() + " WHERE id = ?";
        List<Empenho> lista = consultarLista(sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public Empenho buscarPorNumero(String numero) {
        String sql = baseSelect() + " WHERE numero = ? LIMIT 1";
        List<Empenho> lista = consultarLista(sql, numero);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public double totalValorPorPeriodo(LocalDate inicio, LocalDate fim) {
        return somarPorPeriodo("valor", inicio, fim);
    }

    public double totalSaldoPorPeriodo(LocalDate inicio, LocalDate fim) {
        return somarPorPeriodo("saldo", inicio, fim);
    }

    public void atualizar(Empenho e) {
        String sql = "UPDATE empenho SET numero=?, descricao=?, categoria=?, valor=?, saldo=?, fornecedor=?, data=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, e, true);
            stmt.executeUpdate();

        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Empenho.", ex);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM empenho WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Empenho.", ex);
        }
    }

    private String baseSelect() {
        return "SELECT id, numero, descricao, categoria, valor, saldo, fornecedor, data FROM empenho";
    }

    private List<Empenho> consultarLista(String sql, Object... params) {
        List<Empenho> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametros(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Empenho.", ex);
        }

        return lista;
    }

    private Empenho mapear(ResultSet rs) throws SQLException {
        Empenho e = new Empenho();
        e.setId(rs.getInt("id"));
        e.setNumero(rs.getString("numero"));
        e.setDescricao(rs.getString("descricao"));
        e.setCategoria(rs.getString("categoria"));
        e.setValor(rs.getDouble("valor"));
        e.setSaldo(rs.getDouble("saldo"));
        e.setFornecedor(rs.getString("fornecedor"));
        e.setData(getLocalDate(rs, "data"));
        return e;
    }

    private void preencherStatement(PreparedStatement stmt, Empenho e, boolean incluirId) throws SQLException {
        stmt.setString(1, e.getNumero());
        stmt.setString(2, e.getDescricao());
        stmt.setString(3, e.getCategoria());
        stmt.setDouble(4, e.getValor());
        stmt.setDouble(5, e.getSaldo());
        stmt.setString(6, e.getFornecedor());
        setLocalDate(stmt, 7, e.getData());

        if (incluirId) {
            stmt.setInt(8, e.getId());
        }
    }

    private double somarPorPeriodo(String coluna, LocalDate inicio, LocalDate fim) {
        String sql = "SELECT COALESCE(SUM(" + coluna + "), 0) AS total FROM empenho WHERE date(data) BETWEEN date(?) AND date(?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setLocalDate(stmt, 1, inicio);
            setLocalDate(stmt, 2, fim);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (Exception ex) {
            throw erro("Erro na operação do DAO de Empenho.", ex);
        }

        return 0;
    }

}
