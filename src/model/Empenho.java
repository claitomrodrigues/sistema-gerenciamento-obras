package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Empenho extends Registro {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String numero;
    private String descricao;
    private String categoria;
    private double valor;
    private double saldo;
    private String fornecedor;
    private LocalDate data;

    public Empenho() {}

    public Empenho(String numero, String descricao, String categoria, double valor, double saldo, String fornecedor, LocalDate data) {
        this.numero = numero;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
        this.saldo = saldo;
        this.fornecedor = fornecedor;
        this.data = data;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDataFormatada() {
        return data == null ? "" : data.format(FORMATO_DATA);
    }

    public boolean possuiSaldo() {
        return saldo > 0;
    }

    @Override
    public String toString() {
        if (id > 0) {
            return id + " - " + texto(numero);
        }
        return texto(numero);
    }

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "Sem número" : valor;
    }
}
