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

        PdfPTable tabela = PdfUtils.tabela(7, 21, 27, 10, 10, 13, 12);
        tabela.addCell(PdfUtils.th("ID"));
        tabela.addCell(PdfUtils.th("Equipamento"));
        tabela.addCell(PdfUtils.th("Descrição"));
        tabela.addCell(PdfUtils.th("Atual"));
        tabela.addCell(PdfUtils.th("Próxima"));
        tabela.addCell(PdfUtils.th("Valor"));
        tabela.addCell(PdfUtils.th("Data"));

        if (lista == null || lista.isEmpty()) {
            tabela.addCell(PdfUtils.vazio("Nenhum registro encontrado para os filtros informados.", 7));
        }

        double total = 0d;
        for (Manutencao m : lista) {
            tabela.addCell(PdfUtils.tdCenter(String.valueOf(m.getId())));
            tabela.addCell(PdfUtils.td(m.getEquipamento() == null ? "" : m.getEquipamento().getNome()));
            tabela.addCell(PdfUtils.td(m.getDescricao()));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.numero(m.getRevisaoAtual())));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.numero(m.getProximaRevisao())));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.moeda(m.getValor())));
            tabela.addCell(PdfUtils.tdCenter(PdfUtils.data(m.getData())));
            total += m.getValor();
        }

        doc.add(tabela);
        doc.add(PdfUtils.resumo(
                "Total de registros: " + lista.size(),
                "Valor total das manutenções: " + PdfUtils.moeda(total)
        ));
        doc.close();
        return arquivo;
    }
}
