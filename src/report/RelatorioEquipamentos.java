package report;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import controller.EquipamentoController;
import model.Equipamento;

import java.util.List;

public class RelatorioEquipamentos {

    private final EquipamentoController controller = new EquipamentoController();

    public String gerarGeral() throws Exception {
        return gerar("RELATÓRIO DE EQUIPAMENTOS", controller.listar(), "equipamentos");
    }

    public String gerarAtivos() throws Exception {
        return gerar("RELATÓRIO DE EQUIPAMENTOS ATIVOS", controller.listarAtivos(), "equipamentos_ativos");
    }

    public String gerarInativos() throws Exception {
        return gerar("RELATÓRIO DE EQUIPAMENTOS INATIVOS", controller.listarInativos(), "equipamentos_inativos");
    }

    private String gerar(String titulo, List<Equipamento> lista, String prefixo) throws Exception {
        String arquivo = PdfUtils.caminhoArquivo(prefixo);
        Document doc = PdfUtils.novoDocumento(arquivo, titulo);

        doc.add(PdfUtils.titulo(titulo));
        doc.add(PdfUtils.blocoInfo("Descrição", "Este relatório apresenta a relação de equipamentos cadastrados no sistema da Secretaria Municipal de Obras."));

        PdfPTable tabela = PdfUtils.tabela(8, 28, 18, 18, 14, 14);
        tabela.addCell(PdfUtils.th("ID"));
        tabela.addCell(PdfUtils.th("Nome"));
        tabela.addCell(PdfUtils.th("Tipo"));
        tabela.addCell(PdfUtils.th("Placa"));
        tabela.addCell(PdfUtils.th("KM"));
        tabela.addCell(PdfUtils.th("Status"));

        if (lista == null || lista.isEmpty()) {
            tabela.addCell(PdfUtils.vazio("Nenhum registro encontrado para os filtros informados.", 6));
        }

        for (Equipamento e : lista) {
            tabela.addCell(PdfUtils.tdCenter(String.valueOf(e.getId())));
            tabela.addCell(PdfUtils.td(e.getNome()));
            tabela.addCell(PdfUtils.td(e.getTipo()));
            tabela.addCell(PdfUtils.tdCenter(e.getPlaca()));
            tabela.addCell(PdfUtils.tdRight(PdfUtils.numero(e.getKmAtual())));
            tabela.addCell(PdfUtils.tdCenter(e.isAtivo() ? "Ativo" : "Inativo"));
        }

        doc.add(tabela);
        long ativos = lista.stream().filter(Equipamento::isAtivo).count();
        long inativos = lista.size() - ativos;
        doc.add(PdfUtils.resumo(
                "Total de registros: " + lista.size(),
                "Equipamentos ativos: " + ativos,
                "Equipamentos inativos: " + inativos
        ));
        doc.close();
        return arquivo;
    }
}
