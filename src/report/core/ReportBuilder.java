package report.core;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import report.PdfUtils;

import java.util.ArrayList;
import java.util.List;

public class ReportBuilder {

    private final String titulo;
    private final String prefixoArquivo;
    private String descricao;
    private final List<String> resumos = new ArrayList<>();
    private PdfPTable tabela;

    public ReportBuilder(String titulo, String prefixoArquivo) {
        this.titulo = titulo;
        this.prefixoArquivo = prefixoArquivo;
    }

    public ReportBuilder descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public ReportBuilder tabela(PdfPTable tabela) {
        this.tabela = tabela;
        return this;
    }

    public ReportBuilder resumo(String texto) {
        this.resumos.add(texto);
        return this;
    }

    public String gerar() throws Exception {
        String arquivo = PdfUtils.caminhoArquivo(prefixoArquivo);
        Document doc = PdfUtils.novoDocumento(arquivo, titulo);

        doc.add(PdfUtils.titulo(titulo));
        if (descricao != null && !descricao.isBlank()) {
            doc.add(PdfUtils.blocoInfo("Descrição", descricao));
        }

        if (tabela != null) {
            doc.add(tabela);
        }

        if (!resumos.isEmpty()) {
            doc.add(PdfUtils.resumo(resumos.toArray(new String[0])));
        }

        doc.close();
        return arquivo;
    }
}
