document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');
    const userName = localStorage.getItem('userName');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    // Verifica se o usuário é professor
    if (userRole !== 'PROFESSOR') {
        alert('Acesso não autorizado');
        window.location.href = 'login.html';
        return;
    }

    // Adiciona evento de logout
    document.getElementById('logoutBtn').addEventListener('click', () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userName');
        window.location.href = 'login.html';
    });

    try {
        // Buscar informações do professor
        const response = await fetch('http://localhost:8080/professor/info', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Falha ao carregar informações do professor');
        }

        const professor = await response.json();
        document.getElementById('welcomeMessage').textContent = `Olá, professor ${userName}!`;
        document.getElementById('saldoMoedas').textContent = professor.saldoMoedas;

    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar informações do professor');
    }
}); 