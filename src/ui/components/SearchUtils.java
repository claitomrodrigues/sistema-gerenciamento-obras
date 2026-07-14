package ui.components;

import java.awt.Dimension;
import java.text.Normalizer;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ui.theme.Theme;

/** Utilitário de pesquisa reutilizável para tabelas Swing. */
public final class SearchUtils {
    private SearchUtils() {}

    public static JTextField criarPesquisa(JTable tabela) {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(280, 36));
        campo.setFont(Theme.FONT);
        campo.setToolTipText("Pesquisar em todas as colunas da tabela");
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        garantirSorter(tabela);

        campo.getDocument().addDocumentListener(new DocumentListener() {
            private void filtrar() {
                // Obtém o sorter atual da tabela. Isso evita perder o filtro caso
                // alguma rotina visual recrie ou substitua o RowSorter.
                TableRowSorter<TableModel> sorter = garantirSorter(tabela);
                String pesquisa = normalizar(campo.getText());

                if (pesquisa.isEmpty()) {
                    sorter.setRowFilter(null);
                    return;
                }

                sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                        for (int coluna = 0; coluna < entry.getValueCount(); coluna++) {
                            Object valor = entry.getValue(coluna);
                            if (valor != null && normalizar(String.valueOf(valor)).contains(pesquisa)) {
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }

            @Override public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        return campo;
    }

    @SuppressWarnings("unchecked")
    private static TableRowSorter<TableModel> garantirSorter(JTable tabela) {
        if (tabela.getRowSorter() instanceof TableRowSorter<?>) {
            return (TableRowSorter<TableModel>) tabela.getRowSorter();
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabela.getModel());
        tabela.setRowSorter(sorter);
        return sorter;
    }

    private static String normalizar(String texto) {
        if (texto == null) return "";
        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return semAcentos.trim().toLowerCase(Locale.ROOT);
    }
}
