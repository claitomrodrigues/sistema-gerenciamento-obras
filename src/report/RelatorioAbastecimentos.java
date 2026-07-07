package report;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import controller.AbastecimentoController;
import model.Abastecimento;

import java.time.LocalDate;
import java.util.List;

public class RelatorioAbastecimentos {

    private final AbastecimentoController controller = new AbastecimentoController();

    public String gerarPorPeriodo(LocalDate inicio, LocalDate fim) throws Exception {
        String titulo = "RELATÓRIO DE ABASTECIMENTOS";
        String arquivo = PdfUtils.caminhoArquivo("abastecimentos");
        List<Abastecimento> lista = controller.listarPorPeriodo(inicio, fim);
        double total = controller.totalLitrosPorPeriodo(inicio, fim);

        Document doc = PdfUtils.novoDocumento(arquivo, titulo);
        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.periodo(inicio, fim));

        PdfPTable tabela = PdfUtils.tabela(10, 32, 22, 16, 20);
        tabela.addCell(PdfUtils.th("ID"));
        tabela.addCell(PdfUtils.th("Equipamento"));
        tabela.addCell(PdfUtils.th("Combustível"));
        tabela.addCell(PdfUtils.th("Litros"));
        tabela.addCell(PdfUtils.th("Data"));

        for (Abastecimento a : lista) {
            tabela.addCell(PdfUtils.tdCenter(String.valueOf(a.getId())));
            tabela.addCell(PdfUtils.td(a.getEquipamento() == null ? "" : a.getEquipamento().getNome()));
            tabela.addCell(PdfUtils.td(a.getCombustivel() == null ? "" : a.getCombustivel().getTipo()));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.numero(a.getLitros())));
            tabela.addCell(PdfUtils.tdCenter(PdfUtils.data(a.getData())));
        }

        doc.add(tabela);
        doc.add(PdfUtils.resumo(
                "Total de registros: " + lista.size(),
                "Total de litros abastecidos: " + PdfUtils.numero(total) + " L"
        ));
        doc.close();
        return arquivo;
    }
}
