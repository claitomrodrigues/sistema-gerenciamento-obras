package dao;

import connection.ConnectionFactory;
import model.Empenho;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpenhoDAO {

    public void salvar(Empenho e) {
        String sql = "INSERT INTO empenho (numero, descricao, categoria, valor, saldo, fornecedor, data) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNumero());
            stmt.setString(2, e.getDescricao());
            stmt.setString(3, e.getCategoria());
            stmt.setDouble(4, e.getValor());
            stmt.setDouble(5, e.getSaldo());
            stmt.setString(6, e.getFornecedor());
            stmt.setString(7, e.getData().toString());

            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Empenho> listar() {
        List<Empenho> lista = new ArrayList<>();
        String sql = "SELECT * FROM empenho";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Empenho e = new Empenho();
                e.setId(rs.getInt("id"));
                e.setNumero(rs.getString("numero"));
                e.setDescricao(rs.getString("descricao"));
                e.setCategoria(rs.getString("categoria"));
                e.setValor(rs.getDouble("valor"));
                e.setSaldo(rs.getDouble("saldo"));
                e.setFornecedor(rs.getString("fornecedor"));
                e.setData(LocalDate.parse(rs.getString("data")));

                lista.add(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Empenho e) {
        String sql = "UPDATE empenho SET numero=?, descricao=?, categoria=?, valor=?, saldo=?, fornecedor=?, data=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNumero());
            stmt.setString(2, e.getDescricao());
            stmt.setString(3, e.getCategoria());
            stmt.setDouble(4, e.getValor());
            stmt.setDouble(5, e.getSaldo());
            stmt.setString(6, e.getFornecedor());
            stmt.setString(7, e.getData().toString());
            stmt.setInt(8, e.getId());

            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM empenho WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}