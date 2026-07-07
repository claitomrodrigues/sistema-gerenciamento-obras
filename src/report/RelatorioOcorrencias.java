package report;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import controller.OcorrenciaController;
import model.Ocorrencia;

import java.time.LocalDate;
import java.util.List;

public class RelatorioOcorrencias {

    private final OcorrenciaController controller = new OcorrenciaController();

    public String gerarPorPeriodo(LocalDate inicio, LocalDate fim) throws Exception {
        String titulo = "RELATÓRIO DE OCORRÊNCIAS";
        String arquivo = PdfUtils.caminhoArquivo("ocorrencias");
        List<Ocorrencia> lista = controller.listarPorPeriodo(inicio, fim);

        Document doc = PdfUtils.novoDocumento(arquivo, titulo);
        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.periodo(inicio, fim));

        PdfPTable tabela = PdfUtils.tabela(8, 26, 38, 14, 14);
        tabela.addCell(PdfUtils.th("ID"));
        tabela.addCell(PdfUtils.th("Equipamento"));
        tabela.addCell(PdfUtils.th("Descrição"));
        tabela.addCell(PdfUtils.th("Status"));
        tabela.addCell(PdfUtils.th("Data"));

        for (Ocorrencia o : lista) {
            tabela.addCell(PdfUtils.tdCenter(String.valueOf(o.getId())));
            tabela.addCell(PdfUtils.td(o.getEquipamento() == null ? "" : o.getEquipamento().getNome()));
            tabela.addCell(PdfUtils.td(o.getDescricao()));
            tabela.addCell(PdfUtils.tdCenter(o.getStatus()));
            tabela.addCell(PdfUtils.tdCenter(PdfUtils.data(o.getData())));
        }

        doc.add(tabela);
        doc.add(PdfUtils.resumo("Total de registros: " + lista.size()));
        doc.close();
        return arquivo;
    }
}
