@echo off
echo ========================================
echo Sistema de Gestao - Iniciando Banco de Dados
echo ========================================
echo.

echo Verificando Docker...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Docker nao encontrado!
    echo Por favor, instale o Docker Desktop primeiro.
    pause
    exit /b 1
)

echo Docker encontrado!
echo.

echo Parando containers antigos (se existirem)...
docker-compose down

echo.
echo Iniciando PostgreSQL...
docker-compose up -d postgres

echo.
echo Aguardando PostgreSQL inicializar (15 segundos)...
timeout /t 15 /nobreak

echo.
echo Verificando status...
docker-compose ps

echo.
echo ========================================
echo Banco de dados iniciado com sucesso!
echo ========================================
echo.
echo Informacoes de conexao:
echo   Host: localhost
echo   Porta: 5432
echo   Database: projeto
echo   Usuario: admin
echo   Senha: password
echo.
echo Para verificar logs: docker-compose logs -f postgres
echo Para parar: docker-compose down
echo.
pause

