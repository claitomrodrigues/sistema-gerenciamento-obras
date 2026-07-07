package connection;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void inicializar() {

        String equipamento = """
            CREATE TABLE IF NOT EXISTS equipamento (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                tipo TEXT NOT NULL,
                placa TEXT,
                kmAtual REAL DEFAULT 0,
                horasUso REAL DEFAULT 0,
                ativo INTEGER NOT NULL DEFAULT 1
            );
        """;

        String combustivel = """
            CREATE TABLE IF NOT EXISTS combustivel (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo TEXT NOT NULL,
                litros REAL NOT NULL DEFAULT 0,
                estoqueMinimo REAL NOT NULL DEFAULT 0
            );
        """;

        String empenho = """
            CREATE TABLE IF NOT EXISTS empenho (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                numero TEXT NOT NULL,
                descricao TEXT,
                categoria TEXT,
                valor REAL DEFAULT 0,
                saldo REAL DEFAULT 0,
                fornecedor TEXT,
                data TEXT
            );
        """;

        String abastecimento = """
            CREATE TABLE IF NOT EXISTS abastecimento (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                equipamento_id INTEGER NOT NULL,
                combustivel_id INTEGER NOT NULL,
                litros REAL NOT NULL,
                data TEXT NOT NULL,
                FOREIGN KEY (equipamento_id) REFERENCES equipamento(id),
                FOREIGN KEY (combustivel_id) REFERENCES combustivel(id)
            );
        """;

        String manutencao = """
            CREATE TABLE IF NOT EXISTS manutencao (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                equipamento_id INTEGER NOT NULL,
                descricao TEXT,
                revisaoAtual REAL DEFAULT 0,
                proximaRevisao REAL DEFAULT 0,
                data TEXT,
                FOREIGN KEY (equipamento_id) REFERENCES equipamento(id)
            );
        """;

        String ocorrencia = """
            CREATE TABLE IF NOT EXISTS ocorrencia (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                equipamento_id INTEGER NOT NULL,
                descricao TEXT,
                data TEXT,
                status TEXT,
                FOREIGN KEY (equipamento_id) REFERENCES equipamento(id)
            );
        """;

        String licitacao = """
            CREATE TABLE IF NOT EXISTS licitacao (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                numero TEXT,
                objeto TEXT,
                modalidade TEXT,
                empresa TEXT,
                valor REAL DEFAULT 0,
                data TEXT,
                empenho_id INTEGER,
                descricao TEXT,
                vencimento TEXT,
                observacoes TEXT,
                FOREIGN KEY (empenho_id) REFERENCES empenho(id)
            );
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute(equipamento);
            stmt.execute(combustivel);
            stmt.execute(empenho);
            stmt.execute(abastecimento);
            stmt.execute(manutencao);
            stmt.execute(ocorrencia);
            stmt.execute(licitacao);

            atualizarEstrutura(stmt);
            criarIndicesParaRelatorios(stmt);

            System.out.println("Banco inicializado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void atualizarEstrutura(Statement stmt) {
        adicionarColuna(stmt, "equipamento", "ativo", "INTEGER DEFAULT 1");

        adicionarColuna(stmt, "combustivel", "estoqueMinimo", "REAL DEFAULT 0");

        adicionarColuna(stmt, "empenho", "descricao", "TEXT");
        adicionarColuna(stmt, "empenho", "categoria", "TEXT");
        adicionarColuna(stmt, "empenho", "valor", "REAL DEFAULT 0");
        adicionarColuna(stmt, "empenho", "saldo", "REAL DEFAULT 0");
        adicionarColuna(stmt, "empenho", "fornecedor", "TEXT");
        adicionarColuna(stmt, "empenho", "data", "TEXT");

        adicionarColuna(stmt, "licitacao", "numero", "TEXT");
        adicionarColuna(stmt, "licitacao", "objeto", "TEXT");
        adicionarColuna(stmt, "licitacao", "modalidade", "TEXT");
        adicionarColuna(stmt, "licitacao", "empresa", "TEXT");
        adicionarColuna(stmt, "licitacao", "valor", "REAL DEFAULT 0");
        adicionarColuna(stmt, "licitacao", "data", "TEXT");
        adicionarColuna(stmt, "licitacao", "empenho_id", "INTEGER");
        adicionarColuna(stmt, "licitacao", "descricao", "TEXT");
        adicionarColuna(stmt, "licitacao", "vencimento", "TEXT");
        adicionarColuna(stmt, "licitacao", "observacoes", "TEXT");
    }

    private static void criarIndicesParaRelatorios(Statement stmt) {
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_abastecimento_data ON abastecimento(data)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_abastecimento_equipamento ON abastecimento(equipamento_id)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_abastecimento_combustivel ON abastecimento(combustivel_id)");

        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_manutencao_data ON manutencao(data)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_manutencao_equipamento ON manutencao(equipamento_id)");

        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_ocorrencia_data ON ocorrencia(data)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_ocorrencia_status ON ocorrencia(status)");

        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_licitacao_data ON licitacao(data)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_licitacao_vencimento ON licitacao(vencimento)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_licitacao_empenho ON licitacao(empenho_id)");

        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_empenho_data ON empenho(data)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_empenho_categoria ON empenho(categoria)");
    }

    private static void adicionarColuna(Statement stmt, String tabela, String coluna, String tipo) {
        try {
            stmt.execute("ALTER TABLE " + tabela + " ADD COLUMN " + coluna + " " + tipo);
        } catch (Exception ignored) {
            // coluna já existe
        }
    }

    private static void executar(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}