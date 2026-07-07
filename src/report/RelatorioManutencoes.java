package report;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import controller.ManutencaoController;
import model.Manutencao;

import java.time.LocalDate;
import java.util.List;

public class RelatorioManutencoes {

    private final ManutencaoController controller = new ManutencaoController();

    public String gerarPorPeriodo(LocalDate inicio, LocalDate fim) throws Exception {
        String titulo = "RELATÓRIO DE MANUTENÇÕES";
        String arquivo = PdfUtils.caminhoArquivo("manutencoes");
        List<Manutencao> lista = controller.listarPorPeriodo(inicio, fim);

        Document doc = PdfUtils.novoDocumento(arquivo, titulo);
        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.periodo(inicio, fim));

        PdfPTable tabela = PdfUtils.tabela(8, 28, 34, 12, 12, 14);
        tabela.addCell(PdfUtils.th("ID"));
        tabela.addCell(PdfUtils.th("Equipamento"));
        tabela.addCell(PdfUtils.th("Descrição"));
        tabela.addCell(PdfUtils.th("Atual"));
        tabela.addCell(PdfUtils.th("Próxima"));
        tabela.addCell(PdfUtils.th("Data"));

        for (Manutencao m : lista) {
            tabela.addCell(PdfUtils.tdCenter(String.valueOf(m.getId())));
            tabela.addCell(PdfUtils.td(m.getEquipamento() == null ? "" : m.getEquipamento().getNome()));
            tabela.addCell(PdfUtils.td(m.getDescricao()));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.numero(m.getRevisaoAtual())));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.numero(m.getProximaRevisao())));
            tabela.addCell(PdfUtils.tdCenter(PdfUtils.data(m.getData())));
        }

        doc.add(tabela);
        doc.add(PdfUtils.resumo("Total de registros: " + lista.size()));
        doc.close();
        return arquivo;
    }
}
