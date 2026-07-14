package report;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;

public class ReportPageEvent extends PdfPageEventHelper {

    private final String titulo;

    public ReportPageEvent(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            Rectangle page = document.getPageSize();
            PrefeituraConfig cfg = ReportConfig.getPrefeitura();

            desenharMarcaDagua(writer, page, cfg);
            desenharCabecalho(writer, document, page, cfg);
            desenharRodape(writer, document, page, cfg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void desenharCabecalho(PdfWriter writer, Document document, Rectangle page, PrefeituraConfig cfg) throws Exception {
        PdfContentByte cb = writer.getDirectContent();

        PdfPTable header = new PdfPTable(3);
        header.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        header.setWidths(new float[]{18, 64, 18});

        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setFixedHeight(72f);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Image logo = carregarLogo(cfg);
        if (logo != null) {
            logo.scaleToFit(62, 62);
            logoCell.addElement(logo);
        } else {
            Paragraph semLogo = new Paragraph("LOGO", PdfUtils.FONT_SMALL);
            semLogo.setAlignment(Element.ALIGN_CENTER);
            logoCell.addElement(semLogo);
        }

        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_CENTER);
        p.setLeading(11f);
        p.add(new Phrase(cfg.getEstado() + "\n", PdfUtils.FONT_BOLD));
        p.add(new Phrase(cfg.getPrefeitura() + "\n", PdfUtils.FONT_BOLD));
        p.add(new Phrase(cfg.getSecretaria() + "\n", PdfUtils.FONT_BOLD));
        String identificacao = PdfUtils.nvl(cfg.getEndereco());
        if (!cfg.getCnpj().isBlank()) identificacao += " | CNPJ: " + cfg.getCnpj();
        p.add(new Phrase(identificacao, PdfUtils.FONT_SMALL));
        textCell.addElement(p);

        PdfPCell codCell = new PdfPCell();
        codCell.setBorder(Rectangle.NO_BORDER);
        codCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        codCell.setVerticalAlignment(Element.ALIGN_TOP);
        Paragraph cod = new Paragraph();
        cod.setAlignment(Element.ALIGN_RIGHT);
        cod.setLeading(9f);
        cod.add(new Phrase("Documento\n", PdfUtils.FONT_SMALL_BOLD));
        cod.add(new Phrase(codigoDocumento() + "\n", PdfUtils.FONT_SMALL));
        cod.add(new Phrase(PdfUtils.dataHora(LocalDateTime.now()), PdfUtils.FONT_SMALL));
        codCell.addElement(cod);

        header.addCell(logoCell);
        header.addCell(textCell);
        header.addCell(codCell);
        header.writeSelectedRows(0, -1, document.leftMargin(), page.getHeight() - 24, cb);

        cb.saveState();
        cb.setColorStroke(PdfUtils.PRIMARY);
        cb.setLineWidth(1.1f);
        cb.moveTo(document.leftMargin(), page.getHeight() - 98);
        cb.lineTo(page.getWidth() - document.rightMargin(), page.getHeight() - 98);
        cb.stroke();
        cb.restoreState();
    }

    private void desenharRodape(PdfWriter writer, Document document, Rectangle page, PrefeituraConfig cfg) {
        PdfContentByte cb = writer.getDirectContent();

        cb.saveState();
        cb.setColorStroke(new Color(190, 190, 190));
        cb.setLineWidth(0.6f);
        cb.moveTo(document.leftMargin(), 42);
        cb.lineTo(page.getWidth() - document.rightMargin(), 42);
        cb.stroke();
        cb.restoreState();

        Phrase footerLeft = new Phrase(cfg.getPrefeitura() + " | " + cfg.getSecretaria(), PdfUtils.FONT_SMALL);
        String centro = cfg.getCidade();
        if (!cfg.getContatoInstitucional().isBlank()) centro += " | " + cfg.getContatoInstitucional();
        Phrase footerCenter = new Phrase(centro, PdfUtils.FONT_SMALL);
        Phrase footerRight = new Phrase("Página " + writer.getPageNumber(), PdfUtils.FONT_SMALL);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, footerLeft, document.leftMargin(), 29, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footerCenter, page.getWidth() / 2, 18, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, footerRight, page.getWidth() - document.rightMargin(), 29, 0);
    }

    private void desenharMarcaDagua(PdfWriter writer, Rectangle page, PrefeituraConfig cfg) {
        try {
            Image logo = carregarLogo(cfg);
            if (logo == null) return;

            PdfContentByte under = writer.getDirectContentUnder();
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.055f);
            gs.setStrokeOpacity(0.055f);

            float tamanho = 360f;
            logo.scaleToFit(tamanho, tamanho);
            float x = (page.getWidth() - logo.getScaledWidth()) / 2;
            float y = (page.getHeight() - logo.getScaledHeight()) / 2;
            logo.setAbsolutePosition(x, y);

            under.saveState();
            under.setGState(gs);
            under.addImage(logo);
            under.restoreState();
        } catch (Exception ignored) {
        }
    }

    private Image carregarLogo(PrefeituraConfig cfg) {
        try {
            if (cfg.getCaminhoLogo() != null && !cfg.getCaminhoLogo().isBlank()) {
                File arquivo = new File(cfg.getCaminhoLogo());
                if (arquivo.exists()) return Image.getInstance(arquivo.getAbsolutePath());
            }

            URL resource = ReportPageEvent.class.getResource("/img/logo.png");
            if (resource != null) return Image.getInstance(resource);
        } catch (Exception ignored) {
        }
        return null;
    }

    private String codigoDocumento() {
        String base = titulo == null ? "REL" : titulo.replaceAll("[^A-Za-z0-9]", "");
        if (base.length() > 4) base = base.substring(0, 4);
        return base.toUpperCase() + "-" + System.currentTimeMillis() % 100000;
    }
}
