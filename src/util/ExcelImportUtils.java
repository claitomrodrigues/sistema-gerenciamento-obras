package util;

import auditoria.AuditoriaService;
import controller.*;
import model.*;
import org.apache.poi.ss.usermodel.*;
import ui.components.Notifications;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class ExcelImportUtils {
    private ExcelImportUtils() {}

    private static final DateTimeFormatter BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void importarEquipamentos(Component parent, Runnable aposImportar) {
        importar(parent, aposImportar, "Equipamentos", linhas -> {
            EquipamentoController controller = new EquipamentoController(); int importados = 0;
            for (String[] l : linhas) { if (l.length < 2 || pareceCabecalho(l[0])) continue;
                Equipamento e = new Equipamento(); e.setNome(valor(l,0)); e.setTipo(valor(l,1)); e.setPlaca(valor(l,2)); e.setKmAtual(numero(valor(l,3))); e.setHorasUso(numero(valor(l,4))); e.setAtivo(!isFalso(valor(l,5)));
                if (!e.getNome().isBlank() && !e.getTipo().isBlank()) { controller.salvar(e); importados++; }
            } return importados;
        });
    }

    public static void importarCombustiveis(Component parent, Runnable aposImportar) {
        importar(parent, aposImportar, "Combustíveis", linhas -> {
            CombustivelController controller = new CombustivelController(); int importados = 0;
            for (String[] l : linhas) { if (l.length < 1 || pareceCabecalho(l[0])) continue;
                Combustivel c = new Combustivel(); c.setTipo(valor(l,0)); c.setLitros(numero(valor(l,1))); c.setEstoqueMinimo(numero(valor(l,2)));
                if (!c.getTipo().isBlank()) { controller.salvar(c); importados++; }
            } return importados;
        });
    }

    public static void importarAbastecimentos(Component parent, Runnable aposImportar) {
        importar(parent, aposImportar, "Abastecimentos", linhas -> {
            AbastecimentoController controller = new AbastecimentoController(); EquipamentoController eqCtrl = new EquipamentoController(); CombustivelController cbCtrl = new CombustivelController(); int importados = 0;
            for (String[] l : linhas) { if (l.length < 4 || pareceCabecalho(l[0])) continue;
                Equipamento eq = eqCtrl.buscarPorId(inteiro(valor(l,0))); Combustivel cb = cbCtrl.buscarPorId(inteiro(valor(l,1))); if (eq == null || cb == null) continue;
                Abastecimento a = new Abastecimento(); a.setEquipamento(eq); a.setCombustivel(cb); a.setLitros(numero(valor(l,2))); a.setData(data(valor(l,3)));
                controller.salvar(a); importados++;
            } return importados;
        });
    }

    public static void importarManutencoes(Component parent, Runnable aposImportar) {
        importar(parent, aposImportar, "Manutenções", linhas -> {
            ManutencaoController controller = new ManutencaoController(); EquipamentoController eqCtrl = new EquipamentoController(); int importados = 0;
            for (String[] l : linhas) { if (l.length < 2 || pareceCabecalho(l[0])) continue;
                Equipamento eq = eqCtrl.buscarPorId(inteiro(valor(l,0))); if (eq == null) continue;
                Manutencao m = new Manutencao(); m.setEquipamento(eq); m.setDescricao(valor(l,1)); m.setRevisaoAtual(numero(valor(l,2))); m.setProximaRevisao(numero(valor(l,3))); m.setData(data(valor(l,4)));
                controller.salvar(m); importados++;
            } return importados;
        });
    }

    public static void importarEmpenhos(Component parent, Runnable aposImportar) {
        importar(parent, aposImportar, "Empenhos", linhas -> {
            EmpenhoController controller = new EmpenhoController(); int importados = 0;
            for (String[] l : linhas) { if (l.length < 1 || pareceCabecalho(l[0])) continue;
                Empenho e = new Empenho(); e.setNumero(valor(l,0)); e.setDescricao(valor(l,1)); e.setCategoria(valor(l,2)); e.setValor(numero(valor(l,3))); e.setSaldo(numero(valor(l,4))); e.setFornecedor(valor(l,5)); e.setData(data(valor(l,6)));
                if (!e.getNumero().isBlank()) { controller.salvar(e); importados++; }
            } return importados;
        });
    }

    public static void importarLicitacoes(Component parent, Runnable aposImportar) {
        importar(parent, aposImportar, "Licitações", linhas -> {
            LicitacaoController controller = new LicitacaoController(); EmpenhoController empCtrl = new EmpenhoController(); int importados = 0;
            for (String[] l : linhas) { if (l.length < 2 || pareceCabecalho(l[0])) continue;
                Licitacao lic = new Licitacao(); lic.setNumero(valor(l,0)); lic.setObjeto(valor(l,1)); lic.setModalidade(valor(l,2)); lic.setEmpresa(valor(l,3)); lic.setValor(numero(valor(l,4))); lic.setData(data(valor(l,5)));
                int empId = inteiro(valor(l,6)); if (empId > 0) lic.setEmpenho(empCtrl.buscarPorId(empId));
                if (!lic.getNumero().isBlank() && !lic.getObjeto().isBlank()) { controller.salvar(lic); importados++; }
            } return importados;
        });
    }

    public static void importarOcorrencias(Component parent, Runnable aposImportar) {
        importar(parent, aposImportar, "Ocorrências", linhas -> {
            OcorrenciaController controller = new OcorrenciaController(); EquipamentoController eqCtrl = new EquipamentoController(); int importados = 0;
            for (String[] l : linhas) { if (l.length < 2 || pareceCabecalho(l[0])) continue;
                Equipamento eq = eqCtrl.buscarPorId(inteiro(valor(l,0))); if (eq == null) continue;
                Ocorrencia o = new Ocorrencia(); o.setEquipamento(eq); o.setDescricao(valor(l,1)); o.setData(data(valor(l,2))); o.setStatus(valor(l,3).isBlank()?"Aberta":valor(l,3));
                controller.salvar(o); importados++;
            } return importados;
        });
    }

    private interface Importador { int importar(List<String[]> linhas) throws Exception; }

    private static void importar(Component parent, Runnable aposImportar, String modulo, Importador importador) {
        File arquivo = escolherArquivo(parent); if (arquivo == null) return;
        try {
            List<String[]> linhas = lerPlanilha(arquivo);
            if (linhas.isEmpty()) throw new IllegalArgumentException("A planilha está vazia.");
            int importados = importador.importar(linhas);
            AuditoriaService.registrar("IMPORTAR_PLANILHA", modulo, arquivo.getAbsolutePath());
            if (aposImportar != null) aposImportar.run();
            if (importados == 0) Notifications.error(parent, "Nenhum registro válido foi encontrado para importar em " + modulo + ".");
            else Notifications.success(parent, importados + " registros importados em " + modulo + ".");
        } catch (Exception ex) {
            Notifications.error(parent, "Erro ao importar " + modulo + ": " + mensagemRaiz(ex));
        }
    }

    private static File escolherArquivo(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Importar planilha");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("Planilhas (*.xlsx, *.xls, *.csv)", "xlsx", "xls", "csv", "tsv", "txt"));
        return chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }

    private static List<String[]> lerPlanilha(File arquivo) throws Exception {
        String nome = arquivo.getName().toLowerCase(Locale.ROOT);
        if (nome.endsWith(".xlsx") || nome.endsWith(".xls")) return lerExcelComPoi(arquivo);
        return lerCsv(arquivo);
    }

    private static List<String[]> lerExcelComPoi(File arquivo) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        DataFormatter formatter = new DataFormatter(new Locale("pt", "BR"));
        try (InputStream in = new BufferedInputStream(new FileInputStream(arquivo)); Workbook workbook = WorkbookFactory.create(in)) {
            if (workbook.getNumberOfSheets() == 0) return linhas;
            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (Row row : sheet) {
                int ultima = row.getLastCellNum();
                if (ultima < 0) continue;
                String[] valores = new String[ultima]; boolean possuiConteudo = false;
                for (int i = 0; i < ultima; i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    String texto = formatarCelula(cell, formatter, evaluator);
                    valores[i] = texto;
                    if (!texto.isBlank()) possuiConteudo = true;
                }
                if (possuiConteudo) linhas.add(valores);
            }
        }
        return linhas;
    }

    private static String formatarCelula(Cell cell, DataFormatter formatter, FormulaEvaluator evaluator) {
        if (cell == null) return "";
        if (DateUtil.isCellDateFormatted(cell)) {
            try { return cell.getLocalDateTimeCellValue().toLocalDate().format(BR); }
            catch (Exception ignored) { }
        }
        return formatter.formatCellValue(cell, evaluator).trim();
    }

    private static List<String[]> lerCsv(File arquivo) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String sep = line.contains(";") ? ";" : (line.contains("\t") ? "\t" : ",");
                linhas.add(line.split(sep, -1));
            }
        }
        return linhas;
    }

    private static String mensagemRaiz(Throwable ex) {
        Throwable atual = ex;
        while (atual.getCause() != null) atual = atual.getCause();
        return atual.getMessage() == null || atual.getMessage().isBlank() ? atual.getClass().getSimpleName() : atual.getMessage();
    }

    private static String valor(String[] linha, int i) { if (i >= linha.length || linha[i] == null) return ""; return linha[i].replace("\uFEFF", "").replace("\"", "").trim(); }
    private static int inteiro(String s) { return (int)Math.round(numero(s)); }
    private static double numero(String s) {
        if (s == null || s.isBlank()) return 0;
        String n = s.trim().replace("R$", "").replace(" ", "");
        if (n.contains(",")) n = n.replace(".", "").replace(',', '.');
        try { return Double.parseDouble(n); } catch (NumberFormatException e) { return 0; }
    }
    private static LocalDate data(String s) {
        if (s == null || s.isBlank()) return LocalDate.now();
        try { return LocalDate.parse(s.trim()); } catch (Exception ignored) { }
        try { return LocalDate.parse(s.trim(), BR); } catch (Exception ignored) { }
        try { return LocalDate.of(1899,12,30).plusDays((long)numero(s)); } catch (Exception ignored) { }
        return LocalDate.now();
    }
    private static boolean isFalso(String s) { if (s == null) return false; String t=s.trim().toLowerCase(Locale.ROOT); return t.equals("não")||t.equals("nao")||t.equals("0")||t.equals("false")||t.equals("inativo"); }
    private static boolean pareceCabecalho(String s) { if (s == null) return true; String t = s.trim().toLowerCase(Locale.ROOT); return t.equals("id") || t.equals("nome") || t.equals("tipo") || t.equals("numero") || t.equals("número") || t.contains("equipamento") || t.contains("combust"); }
}
