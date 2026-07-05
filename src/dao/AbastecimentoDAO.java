package dao;

import connection.ConnectionFactory;
import model.Abastecimento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbastecimentoDAO {

    public void salvar(Abastecimento a) {
        String sql = "INSERT INTO abastecimento (equipamento_id, combustivel_id, litros, data) VALUES (?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, a.getEquipamento().getId());
            stmt.setInt(2, a.getCombustivel().getId());
            stmt.setDouble(3, a.getLitros());
            stmt.setString(4, a.getData().toString());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Abastecimento> listar() {
        List<Abastecimento> lista = new ArrayList<>();

        String sql = "SELECT * FROM abastecimento";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Abastecimento a = new Abastecimento();

                a.setId(rs.getInt("id"));

                // simples (sem JOIN ainda)
                lista.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM abastecimento WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizar(Abastecimento a) {
        String sql = """
            UPDATE abastecimento 
            SET equipamento_id=?, combustivel_id=?, litros=?, data=? 
            WHERE id=?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, a.getEquipamento().getId());
            stmt.setInt(2, a.getCombustivel().getId());
            stmt.setDouble(3, a.getLitros());
            stmt.setString(4, a.getData().toString());
            stmt.setInt(5, a.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}