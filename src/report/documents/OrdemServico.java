package report.documents;

import com.lowagie.text.Document;
import report.PdfUtils;
import report.ReportConfig;

public class OrdemServico {

    public String gerar(String equipamento, String servico, String responsavel, String observacoes) throws Exception {
        String titulo = "ORDEM DE SERVIÇO";
        String arquivo = PdfUtils.caminhoArquivo("ordem_servico");
        Document doc = PdfUtils.novoDocumento(arquivo, titulo);

        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.blocoInfo("Equipamento", PdfUtils.nvl(equipamento)));
        doc.add(PdfUtils.blocoInfo("Serviço solicitado", PdfUtils.nvl(servico)));
        doc.add(PdfUtils.blocoInfo("Responsável", PdfUtils.nvl(responsavel)));
        doc.add(PdfUtils.subtitulo("Observações"));
        doc.add(PdfUtils.paragrafo(PdfUtils.nvl(observacoes)));
        PdfUtils.assinatura(doc, ReportConfig.getPrefeitura().getCidade());

        doc.close();
        return arquivo;
    }
}
