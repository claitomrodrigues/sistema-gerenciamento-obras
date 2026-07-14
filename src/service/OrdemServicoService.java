package service;

import dao.OrdemServicoDAO;
import model.OrdemServico;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class OrdemServicoService {

    private static final Set<String> PRIORIDADES = Set.of("BAIXA", "NORMAL", "ALTA", "URGENTE");
    private static final Set<String> STATUS = Set.of("ABERTA", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA");

    private final OrdemServicoDAO dao;

    public OrdemServicoService() {
        this.dao = new OrdemServicoDAO();
    }

    public void salvar(OrdemServico ordem) {
        validar(ordem);
        dao.salvar(ordem);
    }

    public void atualizar(OrdemServico ordem) {
        if (ordem == null || ordem.getId() <= 0) {
            throw new IllegalArgumentException("Ordem de serviço inválida para atualização.");
        }
        validar(ordem);
        dao.atualizar(ordem);
    }

    public void deletar(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID da ordem de serviço inválido.");
        dao.deletar(id);
    }

    public List<OrdemServico> listar() { return dao.listar(); }
    public List<OrdemServico> listarAbertas() { return dao.listarAbertas(); }
    public List<OrdemServico> listarPorEquipamento(int equipamentoId) { return dao.listarPorEquipamento(equipamentoId); }
    public OrdemServico buscarPorId(int id) { return id <= 0 ? null : dao.buscarPorId(id); }

    public void concluir(OrdemServico ordem) {
        if (ordem == null || ordem.getId() <= 0) {
            throw new IllegalArgumentException("Ordem de serviço inválida.");
        }
        ordem.setStatus("CONCLUIDA");
        ordem.setDataConclusao(LocalDate.now());
        atualizar(ordem);
    }

    private void validar(OrdemServico ordem) {
        if (ordem == null) throw new IllegalArgumentException("Ordem de serviço não pode ser nula.");
        if (vazio(ordem.getNumero())) throw new IllegalArgumentException("Informe o número da ordem de serviço.");
        if (vazio(ordem.getDescricao())) throw new IllegalArgumentException("Informe a descrição do serviço.");
        if (ordem.getDataAbertura() == null) throw new IllegalArgumentException("Informe a data de abertura.");

        ordem.setNumero(ordem.getNumero().trim());
        ordem.setDescricao(ordem.getDescricao().trim());
        ordem.setPrioridade(normalizar(ordem.getPrioridade(), "NORMAL"));
        ordem.setStatus(normalizar(ordem.getStatus(), "ABERTA"));

        if (!PRIORIDADES.contains(ordem.getPrioridade())) {
            throw new IllegalArgumentException("Prioridade inválida.");
        }
        if (!STATUS.contains(ordem.getStatus())) {
            throw new IllegalArgumentException("Status inválido.");
        }
        if (ordem.getDataConclusao() != null && ordem.getDataConclusao().isBefore(ordem.getDataAbertura())) {
            throw new IllegalArgumentException("A conclusão não pode ser anterior à abertura.");
        }
        if ("CONCLUIDA".equals(ordem.getStatus()) && ordem.getDataConclusao() == null) {
            ordem.setDataConclusao(LocalDate.now());
        }
    }

    private String normalizar(String valor, String padrao) {
        return vazio(valor) ? padrao : valor.trim().toUpperCase();
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
