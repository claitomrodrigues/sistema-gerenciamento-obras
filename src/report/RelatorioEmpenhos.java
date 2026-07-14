package report;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import controller.EmpenhoController;
import model.Empenho;

import java.time.LocalDate;
import java.util.List;

public class RelatorioEmpenhos {

    private final EmpenhoController controller = new EmpenhoController();

    public String gerarPorPeriodo(LocalDate inicio, LocalDate fim) throws Exception {
        String titulo = "RELATÓRIO DE EMPENHOS";
        String arquivo = PdfUtils.caminhoArquivo("empenhos");
        List<Empenho> lista = controller.listarPorPeriodo(inicio, fim);
        double totalValor = controller.totalValorPorPeriodo(inicio, fim);
        double totalSaldo = controller.totalSaldoPorPeriodo(inicio, fim);

        Document doc = PdfUtils.novoDocumento(arquivo, titulo);
        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.periodo(inicio, fim));

        PdfPTable tabela = PdfUtils.tabela(10, 18, 22, 24, 16, 16, 14);
        tabela.addCell(PdfUtils.th("ID"));
        tabela.addCell(PdfUtils.th("Número"));
        tabela.addCell(PdfUtils.th("Categoria"));
        tabela.addCell(PdfUtils.th("Fornecedor"));
        tabela.addCell(PdfUtils.th("Valor"));
        tabela.addCell(PdfUtils.th("Saldo"));
        tabela.addCell(PdfUtils.th("Data"));

        if (lista == null || lista.isEmpty()) {
            tabela.addCell(PdfUtils.vazio("Nenhum registro encontrado para os filtros informados.", 7));
        }

        for (Empenho e : lista) {
            tabela.addCell(PdfUtils.tdCenter(String.valueOf(e.getId())));
            tabela.addCell(PdfUtils.tdCenter(e.getNumero()));
            tabela.addCell(PdfUtils.td(e.getCategoria()));
            tabela.addCell(PdfUtils.td(e.getFornecedor()));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.moeda(e.getValor())));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.moeda(e.getSaldo())));
            tabela.addCell(PdfUtils.tdCenter(PdfUtils.data(e.getData())));
        }

        doc.add(tabela);
        doc.add(PdfUtils.resumo(
                "Total de registros: " + lista.size(),
                "Valor total: " + PdfUtils.moeda(totalValor),
                "Saldo total: " + PdfUtils.moeda(totalSaldo)
        ));
        doc.close();
        return arquivo;
    }
}
