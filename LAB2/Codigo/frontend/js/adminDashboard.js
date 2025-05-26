document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    // Verifica se o usuário é admin
    if (userRole !== 'ADMIN') {
        alert('Acesso não autorizado');
        window.location.href = 'login.html';
        return;
    }

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
        alert('Erro ao carregar instituições');
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
        
        let html = `
            <div class="instituicao-nome">${instituicao.nome}</div>
            <div class="departamentos-list">
        `;

        if (instituicao.departamentos && instituicao.departamentos.length > 0) {
            instituicao.departamentos.forEach(departamento => {
                html += `
                    <div class="departamento-item">
                        ${departamento.nome}
                        <div class="professores-list">
                `;

                if (departamento.professores && departamento.professores.length > 0) {
                    departamento.professores.forEach(professor => {
                        html += `<div class="professor-item">${professor.nome}</div>`;
                    });
                }

                html += `</div></div>`;
            });
        } else {
            html += '<div class="departamento-item">Nenhum departamento cadastrado</div>';
        }

        html += '</div>';
        instituicaoDiv.innerHTML = html;
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
        alert('Erro ao carregar departamentos');
    }
}

async function handleInstituicaoSubmit(e) {
    e.preventDefault();
    
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

        if (!response.ok) throw new Error('Falha ao cadastrar instituição');

        alert('Instituição cadastrada com sucesso!');
        e.target.reset();
        await loadInstituicoes();
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao cadastrar instituição');
    }
}

async function handleDepartamentoSubmit(e) {
    e.preventDefault();
    
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

        if (!response.ok) throw new Error('Falha ao cadastrar departamento');

        alert('Departamento cadastrado com sucesso!');
        e.target.reset();
        await loadInstituicoes();
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao cadastrar departamento');
    }
}

function formatCPF(cpf) {
    // Remove all non-numeric characters
    return cpf.replace(/[^\d]/g, '');
}

async function handleProfessorSubmit(e) {
    e.preventDefault();
    
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
            const errorData = await response.json();
            throw new Error(errorData.message || 'Falha ao cadastrar professor');
        }

        alert('Professor cadastrado com sucesso!');
        e.target.reset();
        await loadInstituicoes();
    } catch (error) {
        console.error('Erro:', error);
        alert(error.message || 'Erro ao cadastrar professor');
    }
}

// Carregar departamentos para o formulário de curso
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
        alert('Erro ao carregar departamentos');
    }
}

// Cadastrar novo curso
async function handleCursoSubmit(e) {
    e.preventDefault();

    const cursoData = {
        nome: document.getElementById('nomeCurso').value,
        departamentoId: parseInt(document.getElementById('departamentoCursoSelect').value)
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
            throw new Error('Falha ao cadastrar curso');
        }

        alert('Curso cadastrado com sucesso!');
        document.getElementById('cursoForm').reset();
        await loadInstituicoes(); // Recarrega a lista de instituições para mostrar o novo curso
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao cadastrar curso: ' + error.message);
    }
} 