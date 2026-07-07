package dashboard;

import controller.*;

import java.time.LocalDate;

public class DashboardService {

    private final EquipamentoController equipamentoController = new EquipamentoController();
    private final CombustivelController combustivelController = new CombustivelController();
    private final OcorrenciaController ocorrenciaController = new OcorrenciaController();
    private final ManutencaoController manutencaoController = new ManutencaoController();
    private final EmpenhoController empenhoController = new EmpenhoController();
    private final LicitacaoController licitacaoController = new LicitacaoController();

    public DashboardResumo carregarResumo() {
        DashboardResumo r = new DashboardResumo();

        r.setTotalEquipamentos(equipamentoController.listar().size());
        r.setEquipamentosAtivos(equipamentoController.listarAtivos().size());
        r.setEquipamentosInativos(equipamentoController.listarInativos().size());
        r.setTotalCombustiveis(combustivelController.listar().size());
        r.setCombustiveisBaixoEstoque(combustivelController.listarComEstoqueBaixo().size());
        r.setOcorrenciasAbertas(ocorrenciaController.listarPorStatus("Aberta").size());
        r.setManutencoes(manutencaoController.listar().size());

        LocalDate inicioAno = LocalDate.now().withDayOfYear(1);
        LocalDate hoje = LocalDate.now();
        r.setTotalEmpenhos(empenhoController.totalValorPorPeriodo(inicioAno, hoje));
        r.setSaldoEmpenhos(empenhoController.totalSaldoPorPeriodo(inicioAno, hoje));
        r.setTotalLicitacoes(licitacaoController.listarPorPeriodo(inicioAno, hoje).size());

        return r;
    }
}
