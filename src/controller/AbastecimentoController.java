package controller;

import dao.AbastecimentoDAO;
import model.Abastecimento;
import java.util.List;

public class AbastecimentoController {

    private AbastecimentoDAO dao;

    public AbastecimentoController() {
        dao = new AbastecimentoDAO();
    }

    public void salvar(Abastecimento a) {
        dao.salvar(a);
    }

    public List<Abastecimento> listar() {
        return dao.listar();
    }

    public void atualizar(Abastecimento a) {
        dao.atualizar(a);
    }

    public void deletar(int id) {
        dao.deletar(id);
    }
}