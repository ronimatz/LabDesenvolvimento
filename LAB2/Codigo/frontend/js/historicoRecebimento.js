document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');

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
        const response = await fetch('http://localhost:8080/aluno/historico-recebimentos', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Falha ao carregar histórico');
        }

        const historico = await response.json();
        const tbody = document.querySelector('#historicoRecebimentoTableBody');

        historico.forEach(transacao => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${transacao.professorNome}</td>
                <td>${new Date(transacao.data).toLocaleDateString()}</td>
                <td>${transacao.motivo}</td>
                <td>${transacao.quantidade}</td>
            `;
            tbody.appendChild(row);
        });

    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar histórico de recebimento');
    }
});