package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

/**
 * Recursos comuns utilizados pelos DAOs.
 */
public abstract class DAOBase {

    protected void preencherParametros(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            setParametro(stmt, i + 1, params[i]);
        }
    }

    protected void setParametro(PreparedStatement stmt, int index, Object param) throws SQLException {
        if (param == null) {
            stmt.setNull(index, Types.NULL);
        } else if (param instanceof LocalDate data) {
            setLocalDate(stmt, index, data);
        } else if (param instanceof Integer valor) {
            stmt.setInt(index, valor);
        } else if (param instanceof Long valor) {
            stmt.setLong(index, valor);
        } else if (param instanceof Double valor) {
            stmt.setDouble(index, valor);
        } else if (param instanceof Float valor) {
            stmt.setFloat(index, valor);
        } else if (param instanceof BigDecimal valor) {
            stmt.setBigDecimal(index, valor);
        } else if (param instanceof Boolean valor) {
            stmt.setInt(index, valor ? 1 : 0);
        } else {
            stmt.setObject(index, param);
        }
    }

    protected void setLocalDate(PreparedStatement stmt, int index, LocalDate data) throws SQLException {
        if (data == null) {
            stmt.setNull(index, Types.VARCHAR);
        } else {
            stmt.setString(index, data.toString());
        }
    }

    protected LocalDate getLocalDate(ResultSet rs, String coluna) throws SQLException {
        String valor = rs.getString(coluna);
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(valor.trim());
        } catch (RuntimeException e) {
            throw new SQLException("Data inválida na coluna '" + coluna + "': " + valor, e);
        }
    }

    protected void confirmarAlteracao(int linhasAfetadas, String mensagem) {
        if (linhasAfetadas == 0) {
            throw new DAOException(mensagem);
        }
    }

    protected void rollback(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // A exceção original é mais relevante para o chamador.
        }
    }

    protected DAOException erro(String mensagem, Exception causa) {
        if (causa instanceof DAOException daoException) {
            return daoException;
        }
        return new DAOException(mensagem, causa);
    }
}
