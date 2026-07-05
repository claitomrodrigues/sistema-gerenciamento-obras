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
                ativo INTEGER NOT NULL
            );
        """;

        String combustivel = """
            CREATE TABLE IF NOT EXISTS combustivel (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tipo TEXT NOT NULL,
                litros REAL NOT NULL,
                estoqueMinimo REAL NOT NULL
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
                revisaoAtual REAL,
                proximaRevisao REAL,
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
                descricao TEXT NOT NULL,
                vencimento TEXT,
                observacoes TEXT
            );
        """;

        String empenho = """
            CREATE TABLE IF NOT EXISTS empenho (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                numero TEXT NOT NULL,
                descricao TEXT,
                categoria TEXT,
                valor REAL,
                saldo REAL,
                fornecedor TEXT,
                data TEXT
            );
        """;

        try (
            Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()
        ) {
            stmt.execute(equipamento);
            stmt.execute(combustivel);
            stmt.execute(abastecimento);
            stmt.execute(manutencao);
            stmt.execute(ocorrencia);
            stmt.execute(licitacao);
            stmt.execute(empenho);

            System.out.println("Banco inicializado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}