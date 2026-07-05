package dao;

import connection.ConnectionFactory;
import model.Licitacao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LicitacaoDAO {

    public void salvar(Licitacao l) {
        String sql = "INSERT INTO licitacao (descricao, vencimento, observacoes) VALUES (?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, l.getDescricao());
            stmt.setString(2, l.getVencimento().toString());
            stmt.setString(3, l.getObservacoes());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Licitacao> listar() {
        List<Licitacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM licitacao";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Licitacao l = new Licitacao();
                l.setId(rs.getInt("id"));
                l.setDescricao(rs.getString("descricao"));
                l.setVencimento(LocalDate.parse(rs.getString("vencimento")));
                l.setObservacoes(rs.getString("observacoes"));
                lista.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void atualizar(Licitacao l) {
        String sql = "UPDATE licitacao SET descricao=?, vencimento=?, observacoes=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, l.getDescricao());
            stmt.setString(2, l.getVencimento().toString());
            stmt.setString(3, l.getObservacoes());
            stmt.setInt(4, l.getId());

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM licitacao WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}