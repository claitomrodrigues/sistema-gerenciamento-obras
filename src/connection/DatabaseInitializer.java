package connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

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

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(equipamento);
                stmt.execute(combustivel);
                stmt.execute(empenho);
                stmt.execute(abastecimento);
                stmt.execute(manutencao);
                stmt.execute(ocorrencia);
                stmt.execute(licitacao);
                criarTabelaOrdemServico(stmt);

                criarTabelasDeSistema(stmt);
                atualizarEstrutura(stmt);
                criarIndicesParaRelatorios(stmt);
                conn.commit();
            } catch (Exception e) {
                rollback(conn);
                throw e;
            }
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível inicializar o banco de dados em "
                            + ConnectionFactory.getDatabasePath(), e);
        }
    }

    private static void criarTabelaOrdemServico(Statement stmt) {
        executar(stmt, "CREATE TABLE IF NOT EXISTS ordem_servico ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "numero TEXT NOT NULL UNIQUE,"
                + "equipamento_id INTEGER,"
                + "descricao TEXT NOT NULL,"
                + "local_servico TEXT,"
                + "responsavel TEXT,"
                + "prioridade TEXT NOT NULL DEFAULT 'NORMAL',"
                + "status TEXT NOT NULL DEFAULT 'ABERTA',"
                + "data_abertura TEXT NOT NULL,"
                + "data_conclusao TEXT,"
                + "observacoes TEXT,"
                + "FOREIGN KEY (equipamento_id) REFERENCES equipamento(id))");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_os_status ON ordem_servico(status)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_os_data_abertura ON ordem_servico(data_abertura)");
    }

    private static void criarTabelasDeSistema(Statement stmt) {
        executar(stmt, "CREATE TABLE IF NOT EXISTS auditoria_log ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "data_hora TEXT NOT NULL,"
                + "usuario TEXT,"
                + "acao TEXT NOT NULL,"
                + "modulo TEXT,"
                + "detalhes TEXT)");

        executar(stmt, "CREATE TABLE IF NOT EXISTS configuracao_sistema ("
                + "chave TEXT PRIMARY KEY,"
                + "valor TEXT)");

        executar(stmt, "CREATE TABLE IF NOT EXISTS schema_version ("
                + "id INTEGER PRIMARY KEY CHECK (id = 1),"
                + "versao INTEGER NOT NULL,"
                + "atualizado_em TEXT NOT NULL)");

        executar(stmt, "INSERT OR IGNORE INTO schema_version(id, versao, atualizado_em) "
                + "VALUES (1, 5, datetime('now'))");
        executar(stmt, "UPDATE schema_version SET versao=5, atualizado_em=datetime('now') WHERE id=1 AND versao < 5");

        executar(stmt, "CREATE TABLE IF NOT EXISTS usuario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "login TEXT NOT NULL UNIQUE,"
                + "senha TEXT NOT NULL,"
                + "perfil TEXT NOT NULL DEFAULT 'operador',"
                + "ativo INTEGER NOT NULL DEFAULT 1)");

        executar(stmt, "INSERT OR IGNORE INTO usuario(nome, login, senha, perfil, ativo) "
                + "VALUES ('Administrador', 'admin', 'sha256$EOxwaLfRJNIEPbimtiR0Xg==$d88be65a9882e7e9ece7438c71ed92c0b74416c7bcd3e5dae17defafbb9e91c3', 'administrador', 1)");

        executar(stmt, "CREATE TABLE IF NOT EXISTS permissao_modulo ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "perfil TEXT NOT NULL,"
                + "modulo TEXT NOT NULL,"
                + "pode_visualizar INTEGER NOT NULL DEFAULT 1,"
                + "pode_cadastrar INTEGER NOT NULL DEFAULT 0,"
                + "pode_editar INTEGER NOT NULL DEFAULT 0,"
                + "pode_excluir INTEGER NOT NULL DEFAULT 0,"
                + "pode_exportar INTEGER NOT NULL DEFAULT 1,"
                + "pode_importar INTEGER NOT NULL DEFAULT 0,"
                + "UNIQUE(perfil, modulo))");

        criarPermissaoPadrao(stmt, "administrador", 1, 1, 1, 1, 1, 1);
        criarPermissaoPadrao(stmt, "secretario", 1, 1, 1, 1, 1, 1);
        criarPermissaoPadrao(stmt, "operador", 1, 1, 1, 0, 1, 1);
        criarPermissaoPadrao(stmt, "visualizador", 1, 0, 0, 0, 1, 0);
    }

    private static void criarPermissaoPadrao(Statement stmt, String perfil, int visualizar, int cadastrar, int editar, int excluir, int exportar, int importar) {
        String[] modulos = {"equipamento","combustivel","abastecimento","manutencao","ocorrencia","licitacao","empenho","relatorios","dashboard","configuracoes","auditoria","usuarios","backup","ordem_servico"};
        for (String modulo : modulos) {
            executar(stmt, "INSERT OR IGNORE INTO permissao_modulo(perfil,modulo,pode_visualizar,pode_cadastrar,pode_editar,pode_excluir,pode_exportar,pode_importar) VALUES ('"
                    + perfil + "','" + modulo + "'," + visualizar + "," + cadastrar + "," + editar + "," + excluir + "," + exportar + "," + importar + ")");
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
        adicionarColuna(stmt, "ordem_servico", "equipamento_id", "INTEGER");
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
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_auditoria_data ON auditoria_log(data_hora)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_auditoria_modulo ON auditoria_log(modulo)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_usuario_login ON usuario(login)");
        executar(stmt, "CREATE INDEX IF NOT EXISTS idx_permissao_perfil_modulo ON permissao_modulo(perfil, modulo)");
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
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao atualizar a estrutura do banco.", e);
        }
    }

    private static void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // A exceção original é preservada.
        }
    }
}