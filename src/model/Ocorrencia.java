package model;

import java.time.LocalDate;

public class Ocorrencia extends Registro {

    private Equipamento equipamento;
    private String descricao;
    private LocalDate data;
    private String status;
	public Ocorrencia(Equipamento equipamento, String descricao, LocalDate data, String status) {
		super();
		this.equipamento = equipamento;
		this.descricao = descricao;
		this.data = data;
		this.status = status;
	}
	public Ocorrencia() {
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
    
    
}