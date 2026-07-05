package model;

public class Combustivel extends Registro {

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

    
    
}