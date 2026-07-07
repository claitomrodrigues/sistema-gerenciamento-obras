package report;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PdfUtils {

    public static final Color PRIMARY = new Color(23, 37, 84);
    public static final Color PRIMARY_LIGHT = new Color(238, 242, 248);
    public static final Color TABLE_HEADER = new Color(229, 233, 242);
    public static final Color TABLE_BORDER = new Color(185, 191, 202);
    public static final Color TEXT_MUTED = new Color(90, 90, 90);

    public static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);
    public static final Font FONT_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.BLACK);
    public static final Font FONT_TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, PRIMARY);
    public static final Font FONT_HEADER = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, PRIMARY);
    public static final Font FONT_SMALL = FontFactory.getFont(FontFactory.HELVETICA, 7, TEXT_MUTED);
    public static final Font FONT_SMALL_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, TEXT_MUTED);

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATA_HORA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private PdfUtils() {}

    public static Document novoDocumento(String arquivo, String titulo) throws Exception {
        Document doc = new Document(PageSize.A4, 36, 36, 112, 52);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(arquivo));
        writer.setPageEvent(new ReportPageEvent(titulo));
        doc.open();
        return doc;
    }

    public static String caminhoArquivo(String prefixo) {
        String nome = prefixo + "_" + System.currentTimeMillis() + ".pdf";
        return new File(ReportConfig.getPastaRelatorios(), nome).getAbsolutePath();
    }

    public static Paragraph titulo(String texto) {
        Paragraph p = new Paragraph(texto, FONT_TITLE);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingBefore(2f);
        p.setSpacingAfter(10f);
        return p;
    }

    public static Paragraph subtitulo(String texto) {
        Paragraph p = new Paragraph(texto, FONT_BOLD);
        p.setSpacingBefore(10f);
        p.setSpacingAfter(6f);
        return p;
    }

    public static Paragraph paragrafo(String texto) {
        Paragraph p = new Paragraph(nvl(texto), FONT_NORMAL);
        p.setAlignment(Element.ALIGN_JUSTIFIED);
        p.setLeading(13.5f);
        p.setSpacingAfter(7f);
        return p;
    }

    public static PdfPTable blocoInfo(String rotulo, String valor) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{22, 78});
        table.setWidthPercentage(100);
        table.setSpacingBefore(4f);
        table.setSpacingAfter(8f);
        table.addCell(labelCell(rotulo));
        table.addCell(valueCell(valor));
        return table;
    }

    public static PdfPTable periodo(LocalDate inicio, LocalDate fim) throws DocumentException {
        return blocoInfo("Período", data(inicio) + " até " + data(fim));
    }

    public static PdfPTable tabela(float... larguras) throws DocumentException {
        PdfPTable table = new PdfPTable(larguras);
        table.setWidthPercentage(100);
        table.setWidths(larguras);
        table.setSpacingBefore(8f);
        table.setSpacingAfter(10f);
        table.getDefaultCell().setBorderColor(TABLE_BORDER);
        return table;
    }

    public static PdfPCell th(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(nvl(texto), FONT_HEADER));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(TABLE_HEADER);
        cell.setBorderColor(TABLE_BORDER);
        cell.setPaddingTop(6f);
        cell.setPaddingBottom(6f);
        cell.setPaddingLeft(4f);
        cell.setPaddingRight(4f);
        return cell;
    }

    public static PdfPCell td(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(nvl(texto), FONT_NORMAL));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(TABLE_BORDER);
        cell.setPaddingTop(4f);
        cell.setPaddingBottom(4f);
        cell.setPaddingLeft(4f);
        cell.setPaddingRight(4f);
        return cell;
    }

    public static PdfPCell tdCenter(String texto) {
        PdfPCell cell = td(texto);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    public static PdfPCell tdRight(String texto) {
        PdfPCell cell = td(texto);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    public static PdfPTable resumo(String... linhas) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8f);
        table.setSpacingAfter(8f);

        PdfPCell titulo = new PdfPCell(new Phrase("RESUMO", FONT_HEADER));
        titulo.setBackgroundColor(PRIMARY_LIGHT);
        titulo.setBorderColor(TABLE_BORDER);
        titulo.setPadding(6f);
        table.addCell(titulo);

        if (linhas != null) {
            for (String linha : linhas) {
                PdfPCell c = new PdfPCell(new Phrase(nvl(linha), FONT_NORMAL));
                c.setBorderColor(TABLE_BORDER);
                c.setPadding(5f);
                table.addCell(c);
            }
        }
        return table;
    }

    private static PdfPCell labelCell(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(nvl(texto) + ":", FONT_SMALL_BOLD));
        cell.setBackgroundColor(PRIMARY_LIGHT);
        cell.setBorderColor(TABLE_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    private static PdfPCell valueCell(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(nvl(texto), FONT_SMALL));
        cell.setBorderColor(TABLE_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    public static String data(LocalDate data) {
        return data == null ? "" : data.format(DATA_BR);
    }

    public static String dataHora(LocalDateTime dataHora) {
        return dataHora == null ? "" : dataHora.format(DATA_HORA_BR);
    }

    public static String moeda(double valor) {
        return MOEDA.format(valor);
    }

    public static String numero(double valor) {
        return String.format(new Locale("pt", "BR"), "%.2f", valor);
    }

    public static String nvl(String valor) {
        return valor == null ? "" : valor;
    }

    public static void assinatura(Document doc, String cidade) throws DocumentException {
        PrefeituraConfig cfg = ReportConfig.getPrefeitura();
        doc.add(new Paragraph("\n\n"));
        Paragraph local = new Paragraph((cidade == null || cidade.isBlank() ? "São Vicente do Sul" : cidade) + ", " + data(LocalDate.now()) + ".", FONT_NORMAL);
        local.setAlignment(Element.ALIGN_RIGHT);
        doc.add(local);
        doc.add(new Paragraph("\n\n"));

        Paragraph linha = new Paragraph("________________________________________", FONT_NORMAL);
        linha.setAlignment(Element.ALIGN_CENTER);
        doc.add(linha);

        Paragraph nome = new Paragraph(nvl(cfg.getNomeResponsavel()).isBlank() ? "Responsável" : cfg.getNomeResponsavel(), FONT_BOLD);
        nome.setAlignment(Element.ALIGN_CENTER);
        doc.add(nome);

        Paragraph cargo = new Paragraph(nvl(cfg.getCargoResponsavel()), FONT_SMALL);
        cargo.setAlignment(Element.ALIGN_CENTER);
        doc.add(cargo);
    }
}
