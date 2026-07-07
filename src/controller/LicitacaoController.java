package controller;

import dao.LicitacaoDAO;
import model.Licitacao;

import java.time.LocalDate;
import java.util.List;

public class LicitacaoController {

    private final LicitacaoDAO dao;

    public LicitacaoController() {
        this.dao = new LicitacaoDAO();
    }

    public void salvar(Licitacao licitacao) {
        validar(licitacao);
        dao.salvar(licitacao);
    }

    public void atualizar(Licitacao licitacao) {
        if (licitacao == null || licitacao.getId() <= 0) {
            throw new IllegalArgumentException("Licitação inválida para atualização.");
        }
        validar(licitacao);
        dao.atualizar(licitacao);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID da licitação inválido.");
        }
        dao.deletar(id);
    }

    public List<Licitacao> listar() {
        return dao.listar();
    }

    public Licitacao buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dao.buscarPorId(id);
    }

    public Licitacao buscarPorNumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return null;
        }
        return dao.buscarPorNumero(numero.trim());
    }

    public List<Licitacao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.listarPorPeriodo(inicio, fim);
    }

    public List<Licitacao> listarPorModalidade(String modalidade) {
        if (modalidade == null || modalidade.trim().isEmpty()) {
            return listar();
        }
        return dao.listarPorModalidade(modalidade.trim());
    }

    public List<Licitacao> listarPorEmpresa(String empresa) {
        if (empresa == null || empresa.trim().isEmpty()) {
            return listar();
        }
        return dao.listarPorEmpresa(empresa.trim());
    }

    public double totalValorPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.totalValorPorPeriodo(inicio, fim);
    }

    private void validar(Licitacao licitacao) {
        if (licitacao == null) {
            throw new IllegalArgumentException("Licitação não pode ser nula.");
        }
        if (licitacao.getNumero() == null || licitacao.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o número da licitação.");
        }
        if (licitacao.getObjeto() == null || licitacao.getObjeto().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o objeto da licitação.");
        }
        if (licitacao.getValor() < 0) {
            throw new IllegalArgumentException("Valor da licitação não pode ser negativo.");
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
