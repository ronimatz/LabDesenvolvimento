async function listarAlunos() {
    try {
        const response = await fetch("http://localhost:8080/alunos");
        const alunos = await response.json();

        const alunosDiv = document.getElementById('alunosList');
        alunosDiv.innerHTML = ''; // Limpa antes de exibir

        alunos.forEach(aluno => {
            const alunoCard = document.createElement('div');
            alunoCard.innerHTML = `
                <p><strong>Nome:</strong> ${aluno.nome}</p>
                <p><strong>Email:</strong> ${aluno.email}</p>
                <p><strong>CPF:</strong> ${aluno.cpf}</p>
                <p><strong>RG:</strong> ${aluno.rg}</p>
                <p><strong>Curso:</strong> ${aluno.curso}</p>
                <button onclick="editarAluno(${aluno.id})">Editar</button>
                <button onclick="excluirAluno(${aluno.id})">Excluir</button>
                <hr>
            `;
            alunosDiv.appendChild(alunoCard);
        });
    } catch (error) {
        console.error("Erro ao buscar alunos:", error);
    }
}

// Carregar a lista de alunos assim que a página for carregada
document.addEventListener('DOMContentLoaded', listarAlunos);

// Função para excluir aluno
async function excluirAluno(id) {
    if (!confirm("Tem certeza que deseja excluir este aluno?")) return;

    try {
        const response = await fetch(`http://localhost:8080/alunos/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert("Aluno excluído com sucesso.");
            listarAlunos(); // Atualiza a lista
        } else {
            alert("Erro ao excluir aluno.");
        }
    } catch (error) {
        console.error("Erro ao excluir:", error);
    }
}

function abrirModal(aluno) {
    document.getElementById("editarId").value = aluno.id;
    document.getElementById("editarNome").value = aluno.nome;
    document.getElementById("editarEmail").value = aluno.email;
    document.getElementById("editarSenha").value = ""; // vazio para não sobrescrever sem querer
    document.getElementById("editarCpf").value = aluno.cpf;
    document.getElementById("editarRg").value = aluno.rg;

    if (aluno.endereco) {
        document.getElementById("editarRua").value = aluno.endereco.rua || "";
        document.getElementById("editarNumero").value = aluno.endereco.numero || "";
        document.getElementById("editarComplemento").value = aluno.endereco.complemento || "";
        document.getElementById("editarBairro").value = aluno.endereco.bairro || "";
        document.getElementById("editarCidade").value = aluno.endereco.cidade || "";
        document.getElementById("editarEstado").value = aluno.endereco.estado || "";
    }

    document.getElementById("editarModal").style.display = "block";
}

function fecharModal() {
    document.getElementById("editarModal").style.display = "none";
  }

  function editarAluno(id) {
    // Encontrar o aluno pelo ID na lista de alunos renderizados
    fetch(`http://localhost:8080/alunos/${id}`)
        .then(response => response.json())
        .then(aluno => {
            abrirModal(aluno);
        })
        .catch(error => {
            console.error("Erro ao buscar aluno:", error);
            alert("Erro ao buscar dados do aluno para edição.");
        });
}


document.getElementById("formEditarAluno").addEventListener("submit", async function (e) {
    e.preventDefault(); 

    const id = document.getElementById("editarId").value;
    const nome = document.getElementById("editarNome").value;
    const email = document.getElementById("editarEmail").value;
    const senha = document.getElementById("editarSenha").value;
    const cpf = document.getElementById("editarCpf").value;
    const rg = document.getElementById("editarRg").value;

    const endereco = {
        rua: document.getElementById("editarRua").value,
        numero: parseInt(document.getElementById("editarNumero").value) || 0,
        complemento: document.getElementById("editarComplemento").value,
        bairro: document.getElementById("editarBairro").value,
        cidade: document.getElementById("editarCidade").value,
        estado: document.getElementById("editarEstado").value,
    };

    const alunoDTO = {
        nome,
        email,
        senha,
        cpf,
        rg,
        endereco
    };

    try {
        const response = await fetch(`http://localhost:8080/alunos/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(alunoDTO)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Erro ao atualizar aluno");
        }

        alert("Aluno atualizado com sucesso!");
        fecharModal();
        listarAlunos();
    } catch (error) {
        alert("Erro ao salvar alterações: " + error.message);
        console.error(error);
    }
});