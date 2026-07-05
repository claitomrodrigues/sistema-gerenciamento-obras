package model;

import java.time.LocalDate;

public class Manutencao extends Registro {

    private Equipamento equipamento;
    private String descricao;
    private double revisaoAtual;
    private double proximaRevisao;
    private LocalDate data;
	public Manutencao(Equipamento equipamento, String descricao, double revisaoAtual, double proximaRevisao,
			LocalDate data) {
		super();
		this.equipamento = equipamento;
		this.descricao = descricao;
		this.revisaoAtual = revisaoAtual;
		this.proximaRevisao = proximaRevisao;
		this.data = data;
	}
	public Manutencao() {
		super();
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
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
    
    
}
