package controller;

import dao.EquipamentoDAO;
import model.Equipamento;
import java.util.List;

public class EquipamentoController {

    private EquipamentoDAO dao;

    public EquipamentoController() {
        dao = new EquipamentoDAO();
    }

    public void salvar(Equipamento e) {
        dao.salvar(e);
    }

    public List<Equipamento> listar() {
        return dao.listar();
    }

    public void atualizar(Equipamento e) {
        dao.atualizar(e);
    }

    public void deletar(int id) {
        dao.deletar(id);
    }
}