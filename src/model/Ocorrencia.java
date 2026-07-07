package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Ocorrencia extends Registro {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Equipamento equipamento;
    private String descricao;
    private LocalDate data;
    private String status;

    public Ocorrencia() {}

    public Ocorrencia(Equipamento equipamento, String descricao, LocalDate data, String status) {
        this.equipamento = equipamento;
        this.descricao = descricao;
        this.data = data;
        this.status = status;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataFormatada() {
        return data == null ? "" : data.format(FORMATO_DATA);
    }

    @Override
    public String toString() {
        String nomeEquipamento = equipamento == null ? "Equipamento não informado" : equipamento.toString();
        return nomeEquipamento + " - " + texto(status);
    }

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "Sem status" : valor;
    }
}
