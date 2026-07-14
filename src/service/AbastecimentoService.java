package service;

import dao.AbastecimentoDAO;
import model.Abastecimento;

import java.time.LocalDate;
import java.util.List;

public class AbastecimentoService {

    private final AbastecimentoDAO dao;

    public AbastecimentoService() {
        this.dao = new AbastecimentoDAO();
    }

    public void salvar(Abastecimento abastecimento) {
        validar(abastecimento);
        dao.salvar(abastecimento);
    }

    public void atualizar(Abastecimento abastecimento) {
        if (abastecimento == null || abastecimento.getId() <= 0) {
            throw new IllegalArgumentException("Abastecimento inválido para atualização.");
        }
        validar(abastecimento);
        dao.atualizar(abastecimento);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do abastecimento inválido.");
        }
        dao.deletar(id);
    }

    public List<Abastecimento> listar() {
        return dao.listar();
    }

    public Abastecimento buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dao.buscarPorId(id);
    }

    public List<Abastecimento> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.listarPorPeriodo(inicio, fim);
    }

    public List<Abastecimento> listarPorEquipamento(int equipamentoId) {
        if (equipamentoId <= 0) {
            throw new IllegalArgumentException("Equipamento inválido.");
        }
        return dao.listarPorEquipamento(equipamentoId);
    }

    public List<Abastecimento> listarPorCombustivel(int combustivelId) {
        if (combustivelId <= 0) {
            throw new IllegalArgumentException("Combustível inválido.");
        }
        return dao.listarPorCombustivel(combustivelId);
    }

    public double totalLitrosPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.totalLitrosPorPeriodo(inicio, fim);
    }

    private void validar(Abastecimento abastecimento) {
        if (abastecimento == null) {
            throw new IllegalArgumentException("Abastecimento não pode ser nulo.");
        }
        if (abastecimento.getEquipamento() == null || abastecimento.getEquipamento().getId() <= 0) {
            throw new IllegalArgumentException("Informe o equipamento.");
        }
        if (abastecimento.getCombustivel() == null || abastecimento.getCombustivel().getId() <= 0) {
            throw new IllegalArgumentException("Informe o combustível.");
        }
        if (abastecimento.getLitros() <= 0) {
            throw new IllegalArgumentException("Informe uma quantidade de litros válida.");
        }
        if (abastecimento.getData() == null) {
            throw new IllegalArgumentException("Informe a data do abastecimento.");
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
