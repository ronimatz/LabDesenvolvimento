document.getElementById('alunoForm').addEventListener('submit', async function (e) {
    e.preventDefault();
  
    const aluno = {
      nome: document.getElementById('nome').value,
      email: document.getElementById('email').value,
      senha: document.getElementById('senha').value,
      cpf: document.getElementById('cpf').value,
      rg: document.getElementById('rg').value,
      endereco: {
        rua: document.getElementById('rua').value,
        bairro: document.getElementById('bairro').value,
        numero: parseInt(document.getElementById('numero').value),
        complemento: document.getElementById('complemento').value,
        cidade: document.getElementById('cidade').value,
        estado: document.getElementById('estado').value
      },
      instituicaoId: parseInt(document.getElementById('instituicaoId').value),
      cursoId: parseInt(document.getElementById('cursoId').value)
    };
  
    try {
      const response = await fetch("http://localhost:8080/alunos", {
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
  });