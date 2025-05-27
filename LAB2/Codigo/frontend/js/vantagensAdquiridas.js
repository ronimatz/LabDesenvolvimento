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
        const response = await fetch('http://localhost:8080/aluno/vantagens-adquiridas', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Falha ao carregar vantagens');
        }

        const vantagens = await response.json();
        const tbody = document.querySelector('#vantagensTableBody');

        vantagens.forEach(vantagem => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${vantagem.empresa}</td>
                <td>${vantagem.descricao}</td>
                <td>${new Date(vantagem.dataAquisicao).toLocaleDateString()}</td>
                <td>${vantagem.custo}</td>
                <td>${vantagem.status}</td>
            `;
            tbody.appendChild(row);
        });

    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar vantagens adquiridas');
    }
});