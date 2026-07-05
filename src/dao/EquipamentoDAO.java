package dao;

import connection.ConnectionFactory;
import model.Equipamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoDAO {

    // CREATE
    public void salvar(Equipamento e) {
        String sql = "INSERT INTO equipamento (nome, tipo, placa, kmAtual, horasUso, ativo) VALUES (?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNome());
            stmt.setString(2, e.getTipo());
            stmt.setString(3, e.getPlaca());
            stmt.setDouble(4, e.getKmAtual());
            stmt.setDouble(5, e.getHorasUso());
            stmt.setInt(6, e.isAtivo() ? 1 : 0);

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // READ (LISTAR)
    public List<Equipamento> listar() {
        List<Equipamento> lista = new ArrayList<>();

        String sql = "SELECT * FROM equipamento";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Equipamento e = new Equipamento();

                e.setId(rs.getInt("id"));
                e.setNome(rs.getString("nome"));
                e.setTipo(rs.getString("tipo"));
                e.setPlaca(rs.getString("placa"));
                e.setKmAtual(rs.getDouble("kmAtual"));
                e.setHorasUso(rs.getDouble("horasUso"));
                e.setAtivo(rs.getInt("ativo") == 1);

                lista.add(e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    // UPDATE
    public void atualizar(Equipamento e) {
        String sql = "UPDATE equipamento SET nome=?, tipo=?, placa=?, kmAtual=?, horasUso=?, ativo=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNome());
            stmt.setString(2, e.getTipo());
            stmt.setString(3, e.getPlaca());
            stmt.setDouble(4, e.getKmAtual());
            stmt.setDouble(5, e.getHorasUso());
            stmt.setInt(6, e.isAtivo() ? 1 : 0);
            stmt.setInt(7, e.getId());

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // DELETE
    public void deletar(int id) {
        String sql = "DELETE FROM equipamento WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}