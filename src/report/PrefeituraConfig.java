package report;

public class PrefeituraConfig {

    private String estado = "ESTADO DO RIO GRANDE DO SUL";
    private String prefeitura = "PREFEITURA MUNICIPAL DE SÃO VICENTE DO SUL";
    private String secretaria = "SECRETARIA MUNICIPAL DE OBRAS";
    private String endereco = "São Vicente do Sul - RS - Brasil";
    private String cidade = "São Vicente do Sul";
    private String caminhoLogo = null;
    private String nomeResponsavel = "Responsável";
    private String cargoResponsavel = "Secretaria Municipal de Obras";

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPrefeitura() { return prefeitura; }
    public void setPrefeitura(String prefeitura) { this.prefeitura = prefeitura; }

    public String getSecretaria() { return secretaria; }
    public void setSecretaria(String secretaria) { this.secretaria = secretaria; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getCaminhoLogo() { return caminhoLogo; }
    public void setCaminhoLogo(String caminhoLogo) { this.caminhoLogo = caminhoLogo; }

    public String getNomeResponsavel() { return nomeResponsavel; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = nomeResponsavel; }

    public String getCargoResponsavel() { return cargoResponsavel; }
    public void setCargoResponsavel(String cargoResponsavel) { this.cargoResponsavel = cargoResponsavel; }
}
