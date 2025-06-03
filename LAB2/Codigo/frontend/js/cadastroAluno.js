document.addEventListener('DOMContentLoaded', async () => {
    await loadInstituicoes();
    document.getElementById('instituicaoSelect').addEventListener('change', loadCursos);
    document.getElementById('alunoForm').addEventListener('submit', handleSubmit);
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

// Função para limpar o CPF (remover pontos e traço)
function cleanCPF(cpf) {
    return cpf.replace(/\D/g, '');
}

async function loadInstituicoes() {
    try {
        const response = await fetch('http://localhost:8080/admin/instituicoes', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        if (!response.ok) throw new Error('Falha ao carregar instituições');

        const instituicoes = await response.json();
        const select = document.getElementById('instituicaoSelect');
        
        instituicoes.forEach(instituicao => {
            const option = document.createElement('option');
            option.value = instituicao.id;
            option.textContent = instituicao.nome;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao carregar instituições');
    }
}

async function loadCursos() {
    const instituicaoId = document.getElementById('instituicaoSelect').value;
    const cursoSelect = document.getElementById('cursoSelect');
    cursoSelect.innerHTML = '<option value="">Selecione o Curso</option>';
    
    if (!instituicaoId) return;

    try {
        const response = await fetch(`http://localhost:8080/admin/instituicoes/${instituicaoId}/cursos`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        if (!response.ok) throw new Error('Falha ao carregar cursos');

        const cursos = await response.json();
        cursos.forEach(curso => {
            const option = document.createElement('option');
            option.value = curso.id;
            option.textContent = curso.nome;
            cursoSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao carregar cursos');
    }
}

async function handleSubmit(e) {
    e.preventDefault();
    limparMensagem();
  
    const aluno = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        cpf: cleanCPF(document.getElementById('cpf').value),
        rg: document.getElementById('rg').value,
        rua: document.getElementById('rua').value,
        bairro: document.getElementById('bairro').value,
        numero: parseInt(document.getElementById('numero').value) || null,
        complemento: document.getElementById('complemento').value,
        cidade: document.getElementById('cidade').value,
        estado: document.getElementById('uf').value,
        instituicaoId: parseInt(document.getElementById('instituicaoSelect').value) || null,
        cursoId: parseInt(document.getElementById('cursoSelect').value) || null
    };
  
    try {
        const response = await fetch("http://localhost:8080/auth/cadastro/aluno", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(aluno)
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

        const result = await response.json();
        exibirMensagem("Aluno cadastrado com sucesso!", 'sucesso');
        document.getElementById('alunoForm').reset();
        
        // Limpar os selects
        document.getElementById('cursoSelect').innerHTML = '<option value="">Selecione o Curso</option>';
        
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem("Erro na conexão com o servidor");
    }
}