package model;

import java.time.LocalDate;

public class Licitacao extends Registro {

    private String descricao;
    private LocalDate vencimento;
    private String observacoes;

    public Licitacao() {}

    public Licitacao(String descricao, LocalDate vencimento, String observacoes) {
        this.descricao = descricao;
        this.vencimento = vencimento;
        this.observacoes = observacoes;
    }

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDate vencimento) {
		this.vencimento = vencimento;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
    
    
}