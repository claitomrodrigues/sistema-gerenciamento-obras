package controller;

import dao.CombustivelDAO;
import model.Combustivel;
import java.util.List;

public class CombustivelController {

    private CombustivelDAO dao;

    public CombustivelController() {
        dao = new CombustivelDAO();
    }

    public void salvar(Combustivel c) {
        dao.salvar(c);
    }

    public List<Combustivel> listar() {
        return dao.listar();
    }

    public void atualizar(Combustivel c) {
        dao.atualizar(c);
    }

    public void deletar(int id) {
        dao.deletar(id);
    }
}