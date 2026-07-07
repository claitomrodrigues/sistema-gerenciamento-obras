package controller;

import dao.OcorrenciaDAO;
import model.Ocorrencia;

import java.time.LocalDate;
import java.util.List;

public class OcorrenciaController {

    private final OcorrenciaDAO dao;

    public OcorrenciaController() {
        this.dao = new OcorrenciaDAO();
    }

    public void salvar(Ocorrencia ocorrencia) {
        validar(ocorrencia);
        dao.salvar(ocorrencia);
    }

    public void atualizar(Ocorrencia ocorrencia) {
        if (ocorrencia == null || ocorrencia.getId() <= 0) {
            throw new IllegalArgumentException("Ocorrência inválida para atualização.");
        }
        validar(ocorrencia);
        dao.atualizar(ocorrencia);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da ocorrência inválido.");
        }
        dao.deletar(id);
    }

    public List<Ocorrencia> listar() {
        return dao.listar();
    }

    public Ocorrencia buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dao.buscarPorId(id);
    }

    public List<Ocorrencia> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.listarPorPeriodo(inicio, fim);
    }

    public List<Ocorrencia> listarPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return listar();
        }
        return dao.listarPorStatus(status.trim());
    }

    public List<Ocorrencia> listarPorEquipamento(int equipamentoId) {
        if (equipamentoId <= 0) {
            throw new IllegalArgumentException("Equipamento inválido.");
        }
        return dao.listarPorEquipamento(equipamentoId);
    }

    private void validar(Ocorrencia ocorrencia) {
        if (ocorrencia == null) {
            throw new IllegalArgumentException("Ocorrência não pode ser nula.");
        }
        if (ocorrencia.getEquipamento() == null || ocorrencia.getEquipamento().getId() <= 0) {
            throw new IllegalArgumentException("Informe o equipamento.");
        }
        if (ocorrencia.getDescricao() == null || ocorrencia.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descrição da ocorrência.");
        }
        if (ocorrencia.getStatus() == null || ocorrencia.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o status da ocorrência.");
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
