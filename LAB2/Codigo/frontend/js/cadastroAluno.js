document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    await loadInstituicoes();
    document.getElementById('instituicaoSelect').addEventListener('change', loadCursos);
    document.getElementById('alunoForm').addEventListener('submit', handleSubmit);
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
        const select = document.getElementById('instituicaoSelect');
        
        instituicoes.forEach(instituicao => {
            const option = document.createElement('option');
            option.value = instituicao.id;
            option.textContent = instituicao.nome;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar instituições');
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
        alert('Erro ao carregar cursos');
    }
}

async function handleSubmit(e) {
    e.preventDefault();
  
    const aluno = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        cpf: document.getElementById('cpf').value,
        rg: document.getElementById('rg').value,
        rua: document.getElementById('rua').value,
        bairro: document.getElementById('bairro').value,
        numero: parseInt(document.getElementById('numero').value),
        complemento: document.getElementById('complemento').value,
        cidade: document.getElementById('cidade').value,
        estado: document.getElementById('uf').value,
        instituicaoId: parseInt(document.getElementById('instituicaoSelect').value),
        cursoId: parseInt(document.getElementById('cursoSelect').value)
    };
  
    try {
        const response = await fetch("http://localhost:8080/auth/cadastro/aluno", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(aluno)
        });
  
        const data = await response.json();
  
        if (response.ok) {
            document.getElementById('mensagem').innerText = "Aluno cadastrado com sucesso!";
            document.getElementById('alunoForm').reset();
        } else {
            document.getElementById('mensagem').innerText = data.message || "Erro ao cadastrar aluno.";
        }
    } catch (error) {
        document.getElementById('mensagem').innerText = "Erro na requisição.";
        console.error(error);
    }
}