package report;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import controller.LicitacaoController;
import model.Licitacao;

import java.time.LocalDate;
import java.util.List;

public class RelatorioLicitacoes {

    private final LicitacaoController controller = new LicitacaoController();

    public String gerarPorPeriodo(LocalDate inicio, LocalDate fim) throws Exception {
        String titulo = "RELATÓRIO DE LICITAÇÕES";
        String arquivo = PdfUtils.caminhoArquivo("licitacoes");
        List<Licitacao> lista = controller.listarPorPeriodo(inicio, fim);
        double total = controller.totalValorPorPeriodo(inicio, fim);

        Document doc = PdfUtils.novoDocumento(arquivo, titulo);
        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.periodo(inicio, fim));

        PdfPTable tabela = PdfUtils.tabela(8, 14, 30, 16, 20, 16, 14);
        tabela.addCell(PdfUtils.th("ID"));
        tabela.addCell(PdfUtils.th("Número"));
        tabela.addCell(PdfUtils.th("Objeto"));
        tabela.addCell(PdfUtils.th("Modalidade"));
        tabela.addCell(PdfUtils.th("Empresa"));
        tabela.addCell(PdfUtils.th("Valor"));
        tabela.addCell(PdfUtils.th("Data"));

        for (Licitacao l : lista) {
            tabela.addCell(PdfUtils.tdCenter(String.valueOf(l.getId())));
            tabela.addCell(PdfUtils.tdCenter(l.getNumero()));
            tabela.addCell(PdfUtils.td(l.getObjeto()));
            tabela.addCell(PdfUtils.td(l.getModalidade()));
            tabela.addCell(PdfUtils.td(l.getEmpresa()));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.moeda(l.getValor())));
            tabela.addCell(PdfUtils.tdCenter(PdfUtils.data(l.getData())));
        }

        doc.add(tabela);
        doc.add(PdfUtils.resumo(
                "Total de registros: " + lista.size(),
                "Valor total: " + PdfUtils.moeda(total)
        ));
        doc.close();
        return arquivo;
    }
}
