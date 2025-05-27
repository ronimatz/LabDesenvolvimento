document.addEventListener('DOMContentLoaded', async () => {
    // Simulando dados do aluno para desenvolvimento
    const alunoMock = {
        nome: "Aluno Teste",
        saldoMoedas: 0
    };

    // Atualiza a interface com os dados mockados
    document.getElementById('welcomeMessage').textContent = `Olá, ${alunoMock.nome}!`;
    document.getElementById('saldoMoedas').textContent = alunoMock.saldoMoedas;
}); 