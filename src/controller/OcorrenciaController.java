package controller;

import dao.OcorrenciaDAO;
import model.Ocorrencia;
import java.util.List;

public class OcorrenciaController {

    private OcorrenciaDAO dao;

    public OcorrenciaController() {
        dao = new OcorrenciaDAO();
    }

    public void salvar(Ocorrencia o) {
        dao.salvar(o);
    }

    public List<Ocorrencia> listar() {
        return dao.listar();
    }

    public void atualizar(Ocorrencia o) {
        dao.atualizar(o);
    }

    public void deletar(int id) {
        dao.deletar(id);
    }
}