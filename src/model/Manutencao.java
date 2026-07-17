package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Manutencao extends Registro {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Equipamento equipamento;
    private String descricao;
    private double revisaoAtual;
    private double proximaRevisao;
    private double valor;
    private LocalDate data;

    public Manutencao() {
    }

    public Manutencao(Equipamento equipamento, String descricao, double revisaoAtual,
                      double proximaRevisao, LocalDate data) {
        this(equipamento, descricao, revisaoAtual, proximaRevisao, 0d, data);
    }

    public Manutencao(Equipamento equipamento, String descricao, double revisaoAtual,
                      double proximaRevisao, double valor, LocalDate data) {
        this.equipamento = equipamento;
        this.descricao = descricao;
        this.revisaoAtual = revisaoAtual;
        this.proximaRevisao = proximaRevisao;
        this.valor = valor;
        this.data = data;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getRevisaoAtual() {
        return revisaoAtual;
    }

    public void setRevisaoAtual(double revisaoAtual) {
        this.revisaoAtual = revisaoAtual;
    }

    public double getProximaRevisao() {
        return proximaRevisao;
    }

    public void setProximaRevisao(double proximaRevisao) {
        this.proximaRevisao = proximaRevisao;
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

    public String getDataFormatada() {
        return data == null ? "" : data.format(FORMATO_DATA);
    }

    @Override
    public String toString() {
        String nomeEquipamento = equipamento == null ? "Equipamento não informado" : equipamento.toString();
        return nomeEquipamento + " - " + texto(descricao);
    }

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "Sem descrição" : valor;
    }
}
