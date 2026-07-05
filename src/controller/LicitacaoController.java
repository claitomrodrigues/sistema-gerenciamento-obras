package controller;

import dao.LicitacaoDAO;
import model.Licitacao;
import java.util.List;

public class LicitacaoController {

    private LicitacaoDAO dao;

    public LicitacaoController() {
        dao = new LicitacaoDAO();
    }

    public void salvar(Licitacao l) {
        dao.salvar(l);
    }

    public List<Licitacao> listar() {
        return dao.listar();
    }

    public void atualizar(Licitacao l) {
        dao.atualizar(l);
    }

    public void deletar(int id) {
        dao.deletar(id);
    }
}