package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrdemServico extends Registro {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String numero;
    private Equipamento equipamento;
    private String descricao;
    private String localServico;
    private String responsavel;
    private String prioridade = "NORMAL";
    private String status = "ABERTA";
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;
    private String observacoes;

    public String getNumero() { return numero; }
    public Equipamento getEquipamento() { return equipamento; }
    public void setEquipamento(Equipamento equipamento) { this.equipamento = equipamento; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getLocalServico() { return localServico; }
    public void setLocalServico(String localServico) { this.localServico = localServico; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }
    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public boolean isAberta() {
        return !"CONCLUIDA".equalsIgnoreCase(status) && !"CANCELADA".equalsIgnoreCase(status);
    }

    public String getDataAberturaFormatada() {
        return dataAbertura == null ? "" : dataAbertura.format(FORMATO_DATA);
    }

    public String getDataConclusaoFormatada() {
        return dataConclusao == null ? "" : dataConclusao.format(FORMATO_DATA);
    }

    @Override
    public String toString() {
        String numeroExibicao = numero == null || numero.isBlank() ? "Sem número" : numero;
        String descricaoExibicao = descricao == null || descricao.isBlank() ? "Sem descrição" : descricao;
        return numeroExibicao + " - " + descricaoExibicao;
    }
}
