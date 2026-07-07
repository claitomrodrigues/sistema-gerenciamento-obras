package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

public abstract class DAOBase {

    protected void preencherParametros(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            int index = i + 1;

            if (param == null) {
                stmt.setObject(index, null);
            } else if (param instanceof LocalDate) {
                setLocalDate(stmt, index, (LocalDate) param);
            } else if (param instanceof Integer) {
                stmt.setInt(index, (Integer) param);
            } else if (param instanceof Double) {
                stmt.setDouble(index, (Double) param);
            } else if (param instanceof Boolean) {
                stmt.setInt(index, ((Boolean) param) ? 1 : 0);
            } else {
                stmt.setObject(index, param);
            }
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
        return valor == null || valor.isBlank() ? null : LocalDate.parse(valor);
    }

    protected RuntimeException erro(String mensagem, Exception e) {
        return new RuntimeException(mensagem, e);
    }
}
