package model;

public class Equipamento extends Registro {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String tipo;
    private String placa;
    private double kmAtual;
    private double horasUso;
    private boolean ativo = true;

    public Equipamento() {}

    public Equipamento(String nome, String tipo, String placa, double kmAtual, double horasUso, boolean ativo) {
        this.nome = nome;
        this.tipo = tipo;
        this.placa = placa;
        this.kmAtual = kmAtual;
        this.horasUso = horasUso;
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public double getKmAtual() {
        return kmAtual;
    }

    public void setKmAtual(double kmAtual) {
        this.kmAtual = kmAtual;
    }

    public double getHorasUso() {
        return horasUso;
    }

    public void setHorasUso(double horasUso) {
        this.horasUso = horasUso;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getStatusTexto() {
        return ativo ? "Ativo" : "Inativo";
    }

    @Override
    public String toString() {
        if (id > 0) {
            return id + " - " + texto(nome);
        }
        return texto(nome);
    }

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "Sem nome" : valor;
    }
}
