package dashboard;

import connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/** Carrega todos os KPIs do painel usando uma única conexão. */
public class DashboardService {

    public DashboardResumo carregarResumo() {
        DashboardResumo r = new DashboardResumo();
        LocalDate inicioAno = LocalDate.now().withDayOfYear(1);
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(30);

        try (Connection c = ConnectionFactory.getConnection()) {
            r.setTotalEquipamentos(inteiro(c, "SELECT COUNT(*) FROM equipamento"));
            r.setEquipamentosAtivos(inteiro(c, "SELECT COUNT(*) FROM equipamento WHERE ativo=1"));
            r.setEquipamentosInativos(inteiro(c, "SELECT COUNT(*) FROM equipamento WHERE ativo=0"));
            r.setTotalCombustiveis(inteiro(c, "SELECT COUNT(*) FROM combustivel"));
            r.setCombustiveisBaixoEstoque(inteiro(c,
                    "SELECT COUNT(*) FROM combustivel WHERE estoqueMinimo > 0 AND litros <= estoqueMinimo"));
            r.setOcorrenciasAbertas(inteiro(c,
                    "SELECT COUNT(*) FROM ocorrencia WHERE lower(coalesce(status,'')) IN ('aberta','aberto')"));
            r.setManutencoes(inteiro(c, "SELECT COUNT(*) FROM manutencao"));
            r.setManutencoesNoLimite(inteiro(c,
                    "SELECT COUNT(*) FROM manutencao WHERE proximaRevisao > 0 AND revisaoAtual >= proximaRevisao"));
            r.setOrdensServicoAbertas(inteiro(c,
                    "SELECT COUNT(*) FROM ordem_servico WHERE status IN ('ABERTA','EM_ANDAMENTO')"));
            r.setTotalEmpenhos(decimalPeriodo(c, "SELECT COALESCE(SUM(valor),0) FROM empenho WHERE data BETWEEN ? AND ?", inicioAno, hoje));
            r.setSaldoEmpenhos(decimalPeriodo(c, "SELECT COALESCE(SUM(saldo),0) FROM empenho WHERE data BETWEEN ? AND ?", inicioAno, hoje));
            r.setTotalLicitacoes(inteiroPeriodo(c, "SELECT COUNT(*) FROM licitacao WHERE data BETWEEN ? AND ?", inicioAno, hoje));
            r.setLicitacoesVencendo(inteiroPeriodo(c,
                    "SELECT COUNT(*) FROM licitacao WHERE vencimento BETWEEN ? AND ?", hoje, limite));
            r.setLitrosAbastecidosAno(decimalPeriodo(c,
                    "SELECT COALESCE(SUM(litros),0) FROM abastecimento WHERE data BETWEEN ? AND ?", inicioAno, hoje));
            return r;
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível carregar os indicadores do dashboard.", e);
        }
    }

    private int inteiro(Connection c, String sql) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int inteiroPeriodo(Connection c, String sql, LocalDate inicio, LocalDate fim) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, inicio.toString());
            ps.setString(2, fim.toString());
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    private double decimalPeriodo(Connection c, String sql, LocalDate inicio, LocalDate fim) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, inicio.toString());
            ps.setString(2, fim.toString());
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getDouble(1) : 0d; }
        }
    }
}
