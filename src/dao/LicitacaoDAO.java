package dao;

import connection.ConnectionFactory;
import model.Empenho;
import model.Licitacao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LicitacaoDAO extends DAOBase {

    public void salvar(Licitacao l) {
        String sql = "INSERT INTO licitacao (numero, objeto, modalidade, empresa, valor, data, empenho_id) VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, l, false);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Licitacao.", e);
        }
    }

    public List<Licitacao> listar() {
        String sql = baseSelect() + " ORDER BY l.id DESC";
        return consultarLista(sql);
    }

    public List<Licitacao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = baseSelect() + " WHERE date(l.data) BETWEEN date(?) AND date(?) ORDER BY l.data DESC, l.id DESC";
        return consultarLista(sql, inicio, fim);
    }

    public List<Licitacao> listarPorModalidade(String modalidade) {
        String sql = baseSelect() + " WHERE l.modalidade LIKE ? ORDER BY l.data DESC, l.id DESC";
        return consultarLista(sql, "%" + modalidade + "%");
    }

    public List<Licitacao> listarPorEmpresa(String empresa) {
        String sql = baseSelect() + " WHERE l.empresa LIKE ? ORDER BY l.data DESC, l.id DESC";
        return consultarLista(sql, "%" + empresa + "%");
    }

    public Licitacao buscarPorId(int id) {
        String sql = baseSelect() + " WHERE l.id = ?";
        List<Licitacao> lista = consultarLista(sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public Licitacao buscarPorNumero(String numero) {
        String sql = baseSelect() + " WHERE l.numero = ? LIMIT 1";
        List<Licitacao> lista = consultarLista(sql, numero);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public double totalValorPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM licitacao WHERE date(data) BETWEEN date(?) AND date(?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setLocalDate(stmt, 1, inicio);
            setLocalDate(stmt, 2, fim);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Licitacao.", e);
        }

        return 0;
    }

    public void atualizar(Licitacao l) {
        String sql = "UPDATE licitacao SET numero=?, objeto=?, modalidade=?, empresa=?, valor=?, data=?, empenho_id=? WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, l, true);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Licitacao.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM licitacao WHERE id=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Licitacao.", e);
        }
    }

    private String baseSelect() {
        return """
            SELECT
                l.id,
                l.numero,
                l.objeto,
                l.modalidade,
                l.empresa,
                l.valor,
                l.data,
                l.empenho_id,
                e.numero AS empenho_numero
            FROM licitacao l
            LEFT JOIN empenho e ON e.id = l.empenho_id
        """;
    }

    private List<Licitacao> consultarLista(String sql, Object... params) {
        List<Licitacao> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherParametros(stmt, params);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (Exception e) {
            throw erro("Erro na operação do DAO de Licitacao.", e);
        }

        return lista;
    }

    private Licitacao mapear(ResultSet rs) throws SQLException {
        Licitacao l = new Licitacao();
        l.setId(rs.getInt("id"));
        l.setNumero(rs.getString("numero"));
        l.setObjeto(rs.getString("objeto"));
        l.setModalidade(rs.getString("modalidade"));
        l.setEmpresa(rs.getString("empresa"));
        l.setValor(rs.getDouble("valor"));
        l.setData(getLocalDate(rs, "data"));

        int empenhoId = rs.getInt("empenho_id");
        if (!rs.wasNull()) {
            Empenho empenho = new Empenho();
            empenho.setId(empenhoId);
            empenho.setNumero(rs.getString("empenho_numero"));
            l.setEmpenho(empenho);
        }

        return l;
    }

    private void preencherStatement(PreparedStatement stmt, Licitacao l, boolean incluirId) throws SQLException {
        stmt.setString(1, l.getNumero());
        stmt.setString(2, l.getObjeto());
        stmt.setString(3, l.getModalidade());
        stmt.setString(4, l.getEmpresa());
        stmt.setDouble(5, l.getValor());
        setLocalDate(stmt, 6, l.getData());

        if (l.getEmpenho() != null && l.getEmpenho().getId() > 0) {
            stmt.setInt(7, l.getEmpenho().getId());
        } else {
            stmt.setNull(7, Types.INTEGER);
        }

        if (incluirId) {
            stmt.setInt(8, l.getId());
        }
    }

}
