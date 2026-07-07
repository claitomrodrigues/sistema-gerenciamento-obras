package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Abastecimento extends Registro {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Equipamento equipamento;
    private Combustivel combustivel;
    private double litros;
    private LocalDate data;

    public Abastecimento() {}

    public Abastecimento(Equipamento equipamento, Combustivel combustivel, double litros, LocalDate data) {
        this.equipamento = equipamento;
        this.combustivel = combustivel;
        this.litros = litros;
        this.data = data;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public Combustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Combustivel combustivel) {
        this.combustivel = combustivel;
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
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
        return nomeEquipamento + " - " + litros + " L";
    }
}
