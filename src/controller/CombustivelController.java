package controller;

import dao.CombustivelDAO;
import model.Combustivel;

import java.util.List;

public class CombustivelController {

    private final CombustivelDAO dao;

    public CombustivelController() {
        this.dao = new CombustivelDAO();
    }

    public void salvar(Combustivel combustivel) {
        validar(combustivel);
        dao.salvar(combustivel);
    }

    public void atualizar(Combustivel combustivel) {
        if (combustivel == null || combustivel.getId() <= 0) {
            throw new IllegalArgumentException("Combustível inválido para atualização.");
        }
        validar(combustivel);
        dao.atualizar(combustivel);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do combustível inválido.");
        }
        dao.deletar(id);
    }

    public List<Combustivel> listar() {
        return dao.listar();
    }

    public List<Combustivel> listarComEstoqueBaixo() {
        return dao.listarComEstoqueBaixo();
    }

    public Combustivel buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dao.buscarPorId(id);
    }

    public Combustivel buscarPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return null;
        }
        return dao.buscarPorTipo(tipo.trim());
    }

    private void validar(Combustivel combustivel) {
        if (combustivel == null) {
            throw new IllegalArgumentException("Combustível não pode ser nulo.");
        }
        if (combustivel.getTipo() == null || combustivel.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o tipo do combustível.");
        }
        if (combustivel.getLitros() < 0) {
            throw new IllegalArgumentException("Quantidade de litros não pode ser negativa.");
        }
        if (combustivel.getEstoqueMinimo() < 0) {
            throw new IllegalArgumentException("Estoque mínimo não pode ser negativo.");
        }
    }
}
