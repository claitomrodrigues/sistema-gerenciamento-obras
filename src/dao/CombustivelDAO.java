package dao;

import connection.ConnectionFactory;
import model.Combustivel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CombustivelDAO {

    public void salvar(Combustivel c) {
        String sql = "INSERT INTO combustivel (tipo, litros, estoqueMinimo) VALUES (?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getTipo());
            stmt.setDouble(2, c.getLitros());
            stmt.setDouble(3, c.getEstoqueMinimo());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Combustivel> listar() {
        List<Combustivel> lista = new ArrayList<>();
        String sql = "SELECT * FROM combustivel";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Combustivel c = new Combustivel();
                c.setId(rs.getInt("id"));
                c.setTipo(rs.getString("tipo"));
                c.setLitros(rs.getDouble("litros"));
                c.setEstoqueMinimo(rs.getDouble("estoqueMinimo"));
                lista.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Combustivel c) {
        String sql = "UPDATE combustivel SET tipo=?, litros=?, estoqueMinimo=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getTipo());
            stmt.setDouble(2, c.getLitros());
            stmt.setDouble(3, c.getEstoqueMinimo());
            stmt.setInt(4, c.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM combustivel WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}