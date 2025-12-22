-- Script de inicialização do banco de dados
-- Este script será executado automaticamente quando o container PostgreSQL for criado

-- Criar as tabelas serão criadas automaticamente pelo JPA
-- Este arquivo está aqui para garantir que o banco esteja pronto

-- Verificar conexão
SELECT 'Banco de dados projeto inicializado com sucesso!' AS status;

-- Dados de exemplo (opcional - descomente se quiser dados iniciais)
/*
-- Inserir clientes de exemplo
INSERT INTO cliente (nome, cpf, email, data_cadastro) VALUES
('João Silva', '12345678901', 'joao.silva@email.com', CURRENT_DATE),
('Maria Santos', '98765432109', 'maria.santos@email.com', CURRENT_DATE),
('Pedro Oliveira', '45678912345', 'pedro.oliveira@email.com', CURRENT_DATE);

-- Inserir pedidos de exemplo
INSERT INTO pedido (numero_pedido, descricao, valor_total, data_pedido, cliente_id) VALUES
('PED-001', 'Pedido de teste 1', 1500.00, CURRENT_DATE, 1),
('PED-002', 'Pedido de teste 2', 2300.50, CURRENT_DATE, 1),
('PED-003', 'Pedido de teste 3', 750.00, CURRENT_DATE, 2);
*/

