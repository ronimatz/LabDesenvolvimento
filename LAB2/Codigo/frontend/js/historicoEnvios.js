document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/professor/historico-envios', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Falha ao carregar histórico');
        }

        const historico = await response.json();
        const tbody = document.querySelector('#historicoTableBody');

        historico.forEach(transacao => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${transacao.alunoNome}</td>
                <td>${new Date(transacao.data).toLocaleDateString()}</td>
                <td>${transacao.motivo}</td>
                <td>${transacao.quantidade}</td>
            `;
            tbody.appendChild(row);
        });

    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar histórico de envios');
    }
}); 