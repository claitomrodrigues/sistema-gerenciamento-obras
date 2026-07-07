package controller;

import dao.EmpenhoDAO;
import model.Empenho;

import java.time.LocalDate;
import java.util.List;

public class EmpenhoController {

    private final EmpenhoDAO dao;

    public EmpenhoController() {
        this.dao = new EmpenhoDAO();
    }

    public void salvar(Empenho empenho) {
        validar(empenho);
        dao.salvar(empenho);
    }

    public void atualizar(Empenho empenho) {
        if (empenho == null || empenho.getId() <= 0) {
            throw new IllegalArgumentException("Empenho inválido para atualização.");
        }
        validar(empenho);
        dao.atualizar(empenho);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do empenho inválido.");
        }
        dao.deletar(id);
    }

    public List<Empenho> listar() {
        return dao.listar();
    }

    public Empenho buscarPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return dao.buscarPorId(id);
    }

    public Empenho buscarPorNumero(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return null;
        }
        return dao.buscarPorNumero(numero.trim());
    }

    public List<Empenho> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.listarPorPeriodo(inicio, fim);
    }

    public List<Empenho> listarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            return listar();
        }
        return dao.listarPorCategoria(categoria.trim());
    }

    public List<Empenho> listarComSaldo() {
        return dao.listarComSaldo();
    }

    public double totalValorPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.totalValorPorPeriodo(inicio, fim);
    }

    public double totalSaldoPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return dao.totalSaldoPorPeriodo(inicio, fim);
    }

    private void validar(Empenho empenho) {
        if (empenho == null) {
            throw new IllegalArgumentException("Empenho não pode ser nulo.");
        }
        if (empenho.getNumero() == null || empenho.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o número do empenho.");
        }
        if (empenho.getValor() < 0) {
            throw new IllegalArgumentException("Valor do empenho não pode ser negativo.");
        }
        if (empenho.getSaldo() < 0) {
            throw new IllegalArgumentException("Saldo do empenho não pode ser negativo.");
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
