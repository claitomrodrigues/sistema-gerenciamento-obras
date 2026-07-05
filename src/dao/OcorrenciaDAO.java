package dao;

import connection.ConnectionFactory;
import model.Ocorrencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaDAO {

    public void salvar(Ocorrencia o) {
        String sql = "INSERT INTO ocorrencia (equipamento_id, descricao, data, status) VALUES (?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, o.getEquipamento().getId());
            stmt.setString(2, o.getDescricao());
            stmt.setString(3, o.getData().toString());
            stmt.setString(4, o.getStatus());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Ocorrencia> listar() {
        List<Ocorrencia> lista = new ArrayList<>();

        String sql = "SELECT * FROM ocorrencia";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ocorrencia o = new Ocorrencia();
                o.setId(rs.getInt("id"));
                lista.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM ocorrencia WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizar(Ocorrencia o) {
        String sql = """
            UPDATE ocorrencia
            SET equipamento_id=?, descricao=?, data=?, status=?
            WHERE id=?
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, o.getEquipamento().getId());
            stmt.setString(2, o.getDescricao());
            stmt.setString(3, o.getData().toString());
            stmt.setString(4, o.getStatus());
            stmt.setInt(5, o.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}