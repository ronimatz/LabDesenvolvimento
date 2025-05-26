document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');

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

    await carregarAlunos();
    document.getElementById('envioForm').addEventListener('submit', enviarMoedas);
});

// Função para carregar a lista de alunos
async function carregarAlunos() {
    const token = localStorage.getItem('token');
    
    try {
        const response = await fetch('http://localhost:8080/alunos', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Falha ao carregar alunos');
        }

        const alunos = await response.json();
        const selectAluno = document.getElementById('alunoSelect');
        selectAluno.innerHTML = '<option value="">Selecione um aluno</option>';

        alunos.forEach(aluno => {
            const option = document.createElement('option');
            option.value = aluno.id;
            option.textContent = `${aluno.nome} (${aluno.email})`;
            selectAluno.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar lista de alunos');
    }
}

// Função para enviar moedas
async function enviarMoedas(event) {
    event.preventDefault();
    const token = localStorage.getItem('token');
    
    const envioData = {
        alunoId: parseInt(document.getElementById('alunoSelect').value),
        quantidade: parseInt(document.getElementById('quantidade').value),
        motivo: document.getElementById('motivo').value
    };

    try {
        const response = await fetch('http://localhost:8080/professor/enviar-moedas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(envioData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Falha ao enviar moedas');
        }

        alert('Moedas enviadas com sucesso!');
        document.getElementById('envioForm').reset();
        window.location.href = 'dashboardProfessor.html';
    } catch (error) {
        console.error('Erro:', error);
        alert(error.message || 'Erro ao enviar moedas');
    }
} 