package report;

public class PrefeituraConfig {

    private String estado = "ESTADO DO RIO GRANDE DO SUL";
    private String prefeitura = "PREFEITURA MUNICIPAL DE SÃO VICENTE DO SUL";
    private String secretaria = "SECRETARIA MUNICIPAL DE OBRAS";
    private String cnpj = "";
    private String endereco = "São Vicente do Sul - RS - Brasil";
    private String cidade = "São Vicente do Sul";
    private String telefone = "";
    private String email = "";
    private String caminhoLogo = "";
    private String nomeResponsavel = "Responsável";
    private String cargoResponsavel = "Secretaria Municipal de Obras";

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = limpar(estado); }
    public String getPrefeitura() { return prefeitura; }
    public void setPrefeitura(String prefeitura) { this.prefeitura = limpar(prefeitura); }
    public String getSecretaria() { return secretaria; }
    public void setSecretaria(String secretaria) { this.secretaria = limpar(secretaria); }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = limpar(cnpj); }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = limpar(endereco); }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = limpar(cidade); }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = limpar(telefone); }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = limpar(email); }
    public String getCaminhoLogo() { return caminhoLogo; }
    public void setCaminhoLogo(String caminhoLogo) { this.caminhoLogo = limpar(caminhoLogo); }
    public String getNomeResponsavel() { return nomeResponsavel; }
    public void setNomeResponsavel(String nomeResponsavel) { this.nomeResponsavel = limpar(nomeResponsavel); }
    public String getCargoResponsavel() { return cargoResponsavel; }
    public void setCargoResponsavel(String cargoResponsavel) { this.cargoResponsavel = limpar(cargoResponsavel); }

    public String getContatoInstitucional() {
        StringBuilder contato = new StringBuilder();
        if (!telefone.isBlank()) contato.append(telefone);
        if (!email.isBlank()) {
            if (contato.length() > 0) contato.append(" | ");
            contato.append(email);
        }
        return contato.toString();
    }

    private String limpar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
