package service;

import dao.EquipamentoDAO;
import model.Equipamento;

import java.util.List;

public class EquipamentoService {

    private final EquipamentoDAO dao;

    public EquipamentoService() {
        this.dao = new EquipamentoDAO();
    }

    public void salvar(Equipamento equipamento) {
        validar(equipamento);
        dao.salvar(equipamento);
    }

    public void atualizar(Equipamento equipamento) {
        if (equipamento == null || equipamento.getId() <= 0) {
            throw new IllegalArgumentException("Equipamento inválido para atualização.");
        }
        validar(equipamento);
        dao.atualizar(equipamento);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do equipamento inválido.");
        }
        dao.deletar(id);
    }

    public List<Equipamento> listar() {
        return dao.listar();
    }

    public List<Equipamento> listarAtivos() {
        return dao.listarAtivos();
    }

    public List<Equipamento> listarInativos() {
        return dao.listarInativos();
    }

    public List<Equipamento> buscarPorNomeOuPlaca(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listar();
        }
        return dao.buscarPorNomeOuPlaca(termo.trim());
    }

    public Equipamento buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dao.buscarPorId(id);
    }

    private void validar(Equipamento equipamento) {
        if (equipamento == null) {
            throw new IllegalArgumentException("Equipamento não pode ser nulo.");
        }
        if (equipamento.getNome() == null || equipamento.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do equipamento.");
        }
        if (equipamento.getTipo() == null || equipamento.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o tipo do equipamento.");
        }
        if (equipamento.getKmAtual() < 0) {
            throw new IllegalArgumentException("KM atual não pode ser negativo.");
        }
        if (equipamento.getHorasUso() < 0) {
            throw new IllegalArgumentException("Horas de uso não podem ser negativas.");
        }
    }
}
