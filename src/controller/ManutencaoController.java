package controller;

import dao.ManutencaoDAO;
import model.Manutencao;

import java.time.LocalDate;
import java.util.List;

public class ManutencaoController {

    private final ManutencaoDAO dao;

    public ManutencaoController() {
        this.dao = new ManutencaoDAO();
    }

    public void salvar(Manutencao manutencao) {
        validar(manutencao);
        dao.salvar(manutencao);
    }

    public void atualizar(Manutencao manutencao) {
        if (manutencao == null || manutencao.getId() <= 0) {
            throw new IllegalArgumentException("Manutenção inválida para atualização.");
        }
        validar(manutencao);
        dao.atualizar(manutencao);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da manutenção inválido.");
        }
        dao.deletar(id);
    }

    public List<Manutencao> listar() {
        return dao.listar();
    }

    public Manutencao buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dao.buscarPorId(id);
    }

    public List<Manutencao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.listarPorPeriodo(inicio, fim);
    }

    public List<Manutencao> listarPorEquipamento(int equipamentoId) {
        if (equipamentoId <= 0) {
            throw new IllegalArgumentException("Equipamento inválido.");
        }
        return dao.listarPorEquipamento(equipamentoId);
    }

    public List<Manutencao> listarProximasRevisoes(double limite) {
        if (limite < 0) {
            throw new IllegalArgumentException("Limite de revisão inválido.");
        }
        return dao.listarProximasRevisoes(limite);
    }

    private void validar(Manutencao manutencao) {
        if (manutencao == null) {
            throw new IllegalArgumentException("Manutenção não pode ser nula.");
        }
        if (manutencao.getEquipamento() == null || manutencao.getEquipamento().getId() <= 0) {
            throw new IllegalArgumentException("Informe o equipamento.");
        }
        if (manutencao.getDescricao() == null || manutencao.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descrição da manutenção.");
        }
        if (manutencao.getRevisaoAtual() < 0) {
            throw new IllegalArgumentException("Revisão atual não pode ser negativa.");
        }
        if (manutencao.getProximaRevisao() < 0) {
            throw new IllegalArgumentException("Próxima revisão não pode ser negativa.");
        }
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Informe a data inicial e final.");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data inicial não pode ser maior que a data final.");
        }
    }
}
