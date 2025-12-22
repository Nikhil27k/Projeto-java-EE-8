@echo off
echo ========================================
echo Sistema de Gestao - Build e Deploy
echo ========================================
echo.

echo Limpando build anterior...
call mvn clean

echo.
echo Compilando projeto...
call mvn package

if %errorlevel% neq 0 (
    echo.
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build concluido com sucesso!
echo ========================================
echo.
echo Arquivo WAR gerado em: target\gestao-clientes-pedidos.war
echo.
echo Proximos passos:
echo   1. Copie o arquivo WAR para a pasta deployments do WildFly
echo   2. Ou faca o deploy via console de administracao
echo.
echo Exemplo:
echo   copy target\gestao-clientes-pedidos.war C:\wildfly\standalone\deployments\
echo.
pause

