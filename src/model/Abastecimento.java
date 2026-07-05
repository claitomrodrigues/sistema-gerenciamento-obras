package model;

import java.time.LocalDate;

public class Abastecimento extends Registro {

    private Equipamento equipamento;
    private Combustivel combustivel;
    private double litros;
    private LocalDate data;
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
	public Abastecimento(Equipamento equipamento, Combustivel combustivel, double litros, LocalDate data) {
		super();
		this.equipamento = equipamento;
		this.combustivel = combustivel;
		this.litros = litros;
		this.data = data;
	}
	public Abastecimento() {
		super();
	}
	
}
