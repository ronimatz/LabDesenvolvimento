document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');
    const userName = localStorage.getItem('userName');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    // Verifica se o usuário é aluno
    if (userRole !== 'ALUNO') {
        alert('Acesso não autorizado');
        window.location.href = 'login.html';
        return;
    }

    try {
        // Buscar informações do aluno
        const response = await fetch('http://localhost:8080/aluno/info', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Falha ao carregar informações do aluno');
        }

        const aluno = await response.json();

        // Atualiza a interface com os dados
        document.getElementById('welcomeMessage').textContent = `Olá, ${aluno.nome}!`;
        document.getElementById('saldoMoedas').textContent = aluno.saldoMoedas;

    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar informações do aluno');
    }
});