package controller;

import dao.EmpenhoDAO;
import model.Empenho;
import java.util.List;

public class EmpenhoController {

    private EmpenhoDAO dao;

    public EmpenhoController() {
        dao = new EmpenhoDAO();
    }

    public void salvar(Empenho e) {
        dao.salvar(e);
    }

    public List<Empenho> listar() {
        return dao.listar();
    }

    public void atualizar(Empenho e) {
        dao.atualizar(e);
    }

    public void deletar(int id) {
        dao.deletar(id);
    }
}