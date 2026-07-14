# Manual do Usuário - Sistema de Gestão de Obras e Frota

## Acesso inicial
- Login padrão: `admin`
- Senha padrão: `admin`

Troque ou crie novos usuários na tela **Usuários** antes de usar em produção.

## Principais telas
- **Dashboard:** resumo executivo do sistema.
- **Equipamentos:** cadastro de máquinas, veículos e implementos.
- **Combustíveis:** cadastro e acompanhamento de estoque.
- **Abastecimentos:** registro de abastecimentos por equipamento.
- **Manutenções:** controle de revisões e serviços.
- **Ocorrências:** registro de problemas e pendências.
- **Licitações e Empenhos:** controle administrativo e financeiro.
- **Relatórios:** geração de PDFs.
- **Auditoria:** consulta das ações registradas no sistema.
- **Configurações:** dados da prefeitura, relatórios e backup.

## Backup
- O botão **Backup** gera uma cópia manual do banco.
- Ao fechar o sistema, uma cópia automática é criada na pasta `backup/automaticos`.
- O botão **Restaurar Backup** substitui o banco atual pelo arquivo escolhido.

## Exportação
As principais tabelas possuem o botão **Exportar CSV**. O arquivo pode ser aberto no Excel ou LibreOffice Calc.

## Observação de segurança
O login atual é funcional para demonstração e uso local. Para produção, recomenda-se aplicar criptografia de senha.
