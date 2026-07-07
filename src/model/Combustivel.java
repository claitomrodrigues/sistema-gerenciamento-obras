package model;

public class Combustivel extends Registro {

    private static final long serialVersionUID = 1L;

    private String tipo;
    private double litros;
    private double estoqueMinimo;

    public Combustivel() {}

    public Combustivel(String tipo, double litros, double estoqueMinimo) {
        this.tipo = tipo;
        this.litros = litros;
        this.estoqueMinimo = estoqueMinimo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getLitros() {
        return litros;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }

    public double getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(double estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public boolean isEstoqueBaixo() {
        return litros <= estoqueMinimo;
    }

    @Override
    public String toString() {
        if (id > 0) {
            return id + " - " + texto(tipo);
        }
        return texto(tipo);
    }

    private String texto(String valor) {
        return valor == null || valor.isBlank() ? "Sem tipo" : valor;
    }
}
