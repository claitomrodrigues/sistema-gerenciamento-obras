package controller;

import dao.ManutencaoDAO;
import model.Manutencao;
import java.util.List;

public class ManutencaoController {

    private ManutencaoDAO dao;

    public ManutencaoController() {
        dao = new ManutencaoDAO();
    }

    public void salvar(Manutencao m) {
        dao.salvar(m);
    }

    public List<Manutencao> listar() {
        return dao.listar();
    }

    public void atualizar(Manutencao m) {
        dao.atualizar(m);
    }

    public void deletar(int id) {
        dao.deletar(id);
    }
}