document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    // Verifica se o usuário é admin
    if (userRole !== 'ADMIN') {
        exibirMensagem('Acesso não autorizado', 'erro');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
        return;
    }

    // Adiciona evento de logout
    document.getElementById('logoutBtn').addEventListener('click', () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
        window.location.href = 'login.html';
    });

    // Carregar instituições existentes
    await loadInstituicoes();

    // Event listeners para os formulários
    document.getElementById('instituicaoForm').addEventListener('submit', handleInstituicaoSubmit);
    document.getElementById('departamentoForm').addEventListener('submit', handleDepartamentoSubmit);
    document.getElementById('professorForm').addEventListener('submit', handleProfessorSubmit);

    // Event listener para atualizar departamentos quando uma instituição é selecionada
    document.getElementById('instituicaoProfessorSelect').addEventListener('change', loadDepartamentos);

    // Adicionar event listeners para o formulário de curso
    document.getElementById('instituicaoCursoSelect').addEventListener('change', loadDepartamentosCurso);
    document.getElementById('cursoForm').addEventListener('submit', handleCursoSubmit);
});

// Função para exibir mensagens estilizadas
function exibirMensagem(texto, tipo) {
    const mensagemElement = document.getElementById('mensagem');
    mensagemElement.textContent = texto;
    mensagemElement.className = `mensagem ${tipo}`;
    mensagemElement.style.display = 'block';
    
    // Auto-ocultar mensagens de sucesso após 3 segundos
    if (tipo === 'sucesso') {
        setTimeout(() => {
            limparMensagem();
        }, 3000);
    }
}

function limparMensagem() {
    const mensagemElement = document.getElementById('mensagem');
    mensagemElement.textContent = '';
    mensagemElement.className = 'mensagem';
    mensagemElement.style.display = 'none';
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
        updateInstituicoesSelects(instituicoes);
        updateInstituicoesList(instituicoes);
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao carregar instituições', 'erro');
    }
}

function updateInstituicoesSelects(instituicoes) {
    const selects = ['instituicaoSelect', 'instituicaoProfessorSelect', 'instituicaoCursoSelect'];
    
    selects.forEach(selectId => {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Selecione a Instituição</option>';
        
        instituicoes.forEach(instituicao => {
            const option = document.createElement('option');
            option.value = instituicao.id;
            option.textContent = instituicao.nome;
            select.appendChild(option);
        });
    });
}

function updateInstituicoesList(instituicoes) {
    const container = document.getElementById('instituicoesList');
    container.innerHTML = '';
    
    instituicoes.forEach(instituicao => {
        const instituicaoDiv = document.createElement('div');
        instituicaoDiv.className = 'instituicao-item';
        instituicaoDiv.innerHTML = `
            <h3>${instituicao.nome}</h3>
            <p>CNPJ: ${instituicao.cnpj}</p>
            <div class="departamentos">
                ${instituicao.departamentos ? instituicao.departamentos.map(dep => `
                    <div class="departamento-item">
                        <h4>${dep.nome}</h4>
                        <div class="professores">
                            ${dep.professores ? dep.professores.map(prof => `
                                <span class="professor-item">${prof.nome}</span>
                            `).join('') : ''}
                        </div>
                    </div>
                `).join('') : '<p>Nenhum departamento cadastrado</p>'}
            </div>
        `;
        container.appendChild(instituicaoDiv);
    });
}

async function loadDepartamentos() {
    const instituicaoId = document.getElementById('instituicaoProfessorSelect').value;
    const departamentoSelect = document.getElementById('departamentoSelect');
    departamentoSelect.innerHTML = '<option value="">Selecione o Departamento</option>';
    
    if (!instituicaoId) return;

    try {
        const response = await fetch(`http://localhost:8080/admin/instituicoes/${instituicaoId}/departamentos`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (!response.ok) throw new Error('Falha ao carregar departamentos');

        const departamentos = await response.json();
        departamentos.forEach(departamento => {
            const option = document.createElement('option');
            option.value = departamento.id;
            option.textContent = departamento.nome;
            departamentoSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao carregar departamentos', 'erro');
    }
}

async function handleInstituicaoSubmit(e) {
    e.preventDefault();
    limparMensagem();
    
    const instituicaoData = {
        nome: document.getElementById('nomeInstituicao').value,
        cnpj: document.getElementById('cnpj').value
    };

    try {
        const response = await fetch('http://localhost:8080/admin/instituicoes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(instituicaoData)
        });

        if (!response.ok) {
            const errorData = await response.text();
            try {
                const errorObj = JSON.parse(errorData);
                if (errorObj.errors) {
                    const errorMessages = errorObj.errors
                        .map(error => error.defaultMessage || error.message)
                        .join('\n');
                    exibirMensagem(errorMessages, 'erro');
                } else if (errorObj.message) {
                    exibirMensagem(errorObj.message, 'erro');
                } else {
                    exibirMensagem('Falha ao cadastrar instituição', 'erro');
                }
            } catch (e) {
                exibirMensagem(errorData, 'erro');
            }
            return;
        }

        exibirMensagem('Instituição cadastrada com sucesso!', 'sucesso');
        e.target.reset();
        await loadInstituicoes();
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao cadastrar instituição', 'erro');
    }
}

async function handleDepartamentoSubmit(e) {
    e.preventDefault();
    limparMensagem();
    
    const departamentoData = {
        nome: document.getElementById('nomeDepartamento').value,
        instituicaoId: document.getElementById('instituicaoSelect').value
    };

    try {
        const response = await fetch('http://localhost:8080/admin/departamentos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(departamentoData)
        });

        if (!response.ok) {
            const errorData = await response.text();
            try {
                const errorObj = JSON.parse(errorData);
                if (errorObj.errors) {
                    const errorMessages = errorObj.errors
                        .map(error => error.defaultMessage || error.message)
                        .join('\n');
                    exibirMensagem(errorMessages, 'erro');
                } else if (errorObj.message) {
                    exibirMensagem(errorObj.message, 'erro');
                } else {
                    exibirMensagem('Falha ao cadastrar departamento', 'erro');
                }
            } catch (e) {
                exibirMensagem(errorData, 'erro');
            }
            return;
        }

        exibirMensagem('Departamento cadastrado com sucesso!', 'sucesso');
        e.target.reset();
        await loadInstituicoes();
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao cadastrar departamento', 'erro');
    }
}

function formatCPF(cpf) {
    return cpf.replace(/[^\d]/g, '');
}

async function handleProfessorSubmit(e) {
    e.preventDefault();
    limparMensagem();
    
    const professorData = {
        nome: document.getElementById('nomeProfessor').value,
        email: document.getElementById('emailProfessor').value,
        cpf: formatCPF(document.getElementById('cpfProfessor').value),
        senha: document.getElementById('senhaProfessor').value,
        departamentoId: document.getElementById('departamentoSelect').value,
        instituicaoId: document.getElementById('instituicaoProfessorSelect').value
    };

    try {
        const response = await fetch('http://localhost:8080/admin/professores', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(professorData)
        });

        if (!response.ok) {
            const errorData = await response.text();
            try {
                const errorObj = JSON.parse(errorData);
                if (errorObj.errors) {
                    const errorMessages = errorObj.errors
                        .map(error => error.defaultMessage || error.message)
                        .join('\n');
                    exibirMensagem(errorMessages, 'erro');
                } else if (errorObj.message) {
                    exibirMensagem(errorObj.message, 'erro');
                } else {
                    exibirMensagem('Falha ao cadastrar professor', 'erro');
                }
            } catch (e) {
                exibirMensagem(errorData, 'erro');
            }
            return;
        }

        exibirMensagem('Professor cadastrado com sucesso!', 'sucesso');
        e.target.reset();
        await loadInstituicoes();
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao cadastrar professor', 'erro');
    }
}

async function loadDepartamentosCurso() {
    const instituicaoId = document.getElementById('instituicaoCursoSelect').value;
    const departamentoSelect = document.getElementById('departamentoCursoSelect');
    departamentoSelect.innerHTML = '<option value="">Selecione o Departamento</option>';
    
    if (!instituicaoId) return;

    try {
        const response = await fetch(`http://localhost:8080/admin/instituicoes/${instituicaoId}/departamentos`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        
        if (!response.ok) throw new Error('Falha ao carregar departamentos');

        const departamentos = await response.json();
        departamentos.forEach(departamento => {
            const option = document.createElement('option');
            option.value = departamento.id;
            option.textContent = departamento.nome;
            departamentoSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao carregar departamentos', 'erro');
    }
}

async function handleCursoSubmit(e) {
    e.preventDefault();
    limparMensagem();

    const cursoData = {
        nome: document.getElementById('nomeCurso').value,
        departamentoId: parseInt(document.getElementById('departamentoCursoSelect').value),
        cargaHoraria: parseInt(document.getElementById('cargaHorariaCurso').value)
    };

    try {
        const response = await fetch('http://localhost:8080/admin/cursos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(cursoData)
        });

        if (!response.ok) {
            const errorData = await response.text();
            try {
                const errorObj = JSON.parse(errorData);
                if (errorObj.errors) {
                    const errorMessages = errorObj.errors
                        .map(error => error.defaultMessage || error.message)
                        .join('\n');
                    exibirMensagem(errorMessages, 'erro');
                } else if (errorObj.message) {
                    exibirMensagem(errorObj.message, 'erro');
                } else {
                    exibirMensagem('Falha ao cadastrar curso', 'erro');
                }
            } catch (e) {
                exibirMensagem(errorData, 'erro');
            }
            return;
        }

        exibirMensagem('Curso cadastrado com sucesso!', 'sucesso');
        document.getElementById('cursoForm').reset();
        await loadInstituicoes();
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao cadastrar curso', 'erro');
    }
}

async function distribuirMoedas() {
    limparMensagem();
    
    try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/admin/distribuir-moedas', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Erro ao distribuir moedas');
        }

        exibirMensagem('Moedas distribuídas com sucesso para todos os professores!', 'sucesso');
    } catch (error) {
        console.error('Erro:', error);
        exibirMensagem('Erro ao distribuir moedas. Por favor, tente novamente.', 'erro');
    }
} 