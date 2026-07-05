package dao;

import connection.ConnectionFactory;
import model.Manutencao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ManutencaoDAO {

    public void salvar(Manutencao m) {
        String sql = "INSERT INTO manutencao (equipamento_id, descricao, revisaoAtual, proximaRevisao, data) VALUES (?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getEquipamento().getId());
            stmt.setString(2, m.getDescricao());
            stmt.setDouble(3, m.getRevisaoAtual());
            stmt.setDouble(4, m.getProximaRevisao());
            stmt.setString(5, m.getData().toString());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Manutencao> listar() {
        List<Manutencao> lista = new ArrayList<>();
        String sql = "SELECT * FROM manutencao";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Manutencao m = new Manutencao();
                m.setId(rs.getInt("id"));
                lista.add(m);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM manutencao WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizar(Manutencao m) {
        String sql = """
            UPDATE manutencao
            SET equipamento_id=?, descricao=?, revisaoAtual=?, proximaRevisao=?, data=?
            WHERE id=?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, m.getEquipamento().getId());
            stmt.setString(2, m.getDescricao());
            stmt.setDouble(3, m.getRevisaoAtual());
            stmt.setDouble(4, m.getProximaRevisao());
            stmt.setString(5, m.getData().toString());
            stmt.setInt(6, m.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}