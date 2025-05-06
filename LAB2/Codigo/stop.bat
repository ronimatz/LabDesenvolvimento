@echo off
echo Encerrando o projeto Moeda Estudantil...

echo Encerrando o frontend...
taskkill /F /IM node.exe

echo Encerrando o backend...
taskkill /F /IM java.exe

echo Limpando arquivos temporários...
cd backend\moedaEstudantil
mvnw.cmd clean

echo Projeto encerrado com sucesso! 