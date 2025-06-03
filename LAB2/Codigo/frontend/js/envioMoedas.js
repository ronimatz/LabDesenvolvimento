document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    // Verifica se o usuário é professor
    if (userRole !== 'PROFESSOR') {
        exibirMensagem('Acesso não autorizado');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
        return;
    }

    await carregarAlunos();
    document.getElementById('envioForm').addEventListener('submit', enviarMoedas);
});

// Função para exibir mensagem
function exibirMensagem(texto, tipo = 'erro') {
    const mensagemDiv = document.getElementById('mensagem');
    mensagemDiv.textContent = texto;
    mensagemDiv.className = `mensagem ${tipo}`;
    mensagemDiv.style.display = 'block';
    
    // Auto-hide para mensagens de sucesso
    if (tipo === 'sucesso') {
        setTimeout(() => {
            limparMensagem();
        }, 3000);
    }
}

// Função para limpar mensagem
function limparMensagem() {
    const mensagemDiv = document.getElementById('mensagem');
    mensagemDiv.style.display = 'none';
    mensagemDiv.textContent = '';
    mensagemDiv.className = 'mensagem';
}

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
        exibirMensagem('Erro ao carregar lista de alunos');
    }
}

// Função para enviar moedas
async function enviarMoedas(event) {
    event.preventDefault();
    limparMensagem();
    
    const token = localStorage.getItem('token');
    
    const envioData = {
        alunoId: parseInt(document.getElementById('alunoSelect').value) || null,
        quantidade: parseInt(document.getElementById('quantidade').value) || null,
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
            const data = await response.text();
            try {
                const errorObj = JSON.parse(data);
                
                if (typeof errorObj === 'object' && errorObj !== null) {
                    // Se for um objeto de erros de validação (campo: mensagem)
                    if (Object.keys(errorObj).some(key => typeof errorObj[key] === 'string')) {
                        const errorMessages = Object.values(errorObj).join('\n');
                        exibirMensagem(errorMessages);
                    }
                    // Se houver uma propriedade 'error' ou 'message'
                    else if (errorObj.error) {
                        exibirMensagem(errorObj.error);
                    } else if (errorObj.message) {
                        exibirMensagem(errorObj.message);
                    }
                    // Se for um array de erros
                    else if (Array.isArray(errorObj)) {
                        const errorMessages = errorObj.map(error => error.message || error.defaultMessage || error).join('\n');
                        exibirMensagem(errorMessages);
                    } else {
                        exibirMensagem('Erro ao processar dados');
                    }
                } else {
                    exibirMensagem(errorObj.toString());
                }
            } catch (e) {
                // Se não for JSON válido, mostra o texto do erro diretamente
                exibirMensagem(data || 'Erro desconhecido');
            }
            return;
        }

        exibirMensagem('Moedas enviadas com sucesso!', 'sucesso');
        document.getElementById('envioForm').reset();
        
        // Redirecionar após sucesso
        setTimeout(() => {
            window.location.href = 'dashboardProfessor.html';
        }, 2000);
        
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro na conexão com o servidor');
    }
} 