package report;

import com.lowagie.text.Document;

public class ParecerTecnico {

    public String gerar(String assunto, String interessado, String texto) throws Exception {
        String titulo = "PARECER TÉCNICO";
        String arquivo = PdfUtils.caminhoArquivo("parecer_tecnico");
        Document doc = PdfUtils.novoDocumento(arquivo, titulo);

        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.blocoInfo("Assunto", PdfUtils.nvl(assunto)));
        doc.add(PdfUtils.blocoInfo("Interessado", PdfUtils.nvl(interessado)));
        doc.add(PdfUtils.subtitulo("1. RELATÓRIO"));
        doc.add(PdfUtils.paragrafo(PdfUtils.nvl(texto)));
        doc.add(PdfUtils.subtitulo("2. CONCLUSÃO"));
        doc.add(PdfUtils.paragrafo("Diante das informações apresentadas, encaminha-se o presente documento para conhecimento e providências cabíveis."));
        PdfUtils.assinatura(doc, ReportConfig.getPrefeitura().getCidade());

        doc.close();
        return arquivo;
    }
}
