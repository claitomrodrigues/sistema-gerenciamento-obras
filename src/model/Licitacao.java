package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Licitacao extends Registro {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String numero;
    private String objeto;
    private String modalidade;
    private String empresa;
    private double valor;
    private LocalDate data;
    private Empenho empenho;

    public Licitacao() {}

    public Licitacao(String numero, String objeto, String modalidade, String empresa, double valor, LocalDate data, Empenho empenho) {
        this.numero = numero;
        this.objeto = objeto;
        this.modalidade = modalidade;
        this.empresa = empresa;
        this.valor = valor;
        this.data = data;
        this.empenho = empenho;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Empenho getEmpenho() {
        return empenho;
    }

    public void setEmpenho(Empenho empenho) {
        this.empenho = empenho;
    }

    public String getDataFormatada() {
        return data == null ? "" : data.format(FORMATO_DATA);
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
