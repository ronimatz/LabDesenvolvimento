function adicionarVantagem() {
    const container = document.getElementById('vantagensContainer');
  
    const div = document.createElement('div');
    div.className = 'vantagem';
  
    div.innerHTML = `
      <label>Descrição:</label>
      <input type="text" class="descricao">
  
      <label>Valor:</label>
      <input type="number" class="valor" step="0.01">
  
      <label>Desconto:</label>
      <input type="number" class="desconto" step="0.01">
  
      <label>Foto (base64 opcional):</label>
      <input type="file" class="fotoProduto" accept="image/*">
    `;
  
    container.appendChild(div);
  }
  
  document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('empresaForm');
    const mensagemElement = document.getElementById('mensagem');

    function exibirMensagem(texto, tipo) {
        mensagemElement.textContent = texto;
        mensagemElement.className = `mensagem ${tipo}`;
        mensagemElement.style.display = 'block';
    }

    function limparMensagem() {
        mensagemElement.textContent = '';
        mensagemElement.className = 'mensagem';
        mensagemElement.style.display = 'none';
    }

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        limparMensagem();

        const formData = {
            email: form.email.value,
            senha: form.senha.value,
            nome: form.nome.value,
            cnpj: form.cnpj.value
        };

        try {
            const response = await fetch('http://localhost:8080/empresas-parceiras/cadastro', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                exibirMensagem('Empresa cadastrada com sucesso!', 'sucesso');
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 2000); // Redireciona após 2 segundos
            } else {
                const data = await response.text();
                try {
                    // Tenta fazer o parse do erro como JSON
                    const errorObj = JSON.parse(data);
                    if (errorObj.errors) {
                        // Se houver múltiplos erros de validação
                        const errorMessages = errorObj.errors.map(error => error.defaultMessage || error.message).join('\n');
                        exibirMensagem(errorMessages, 'erro');
                    } else if (errorObj.message) {
                        // Se for um único erro
                        exibirMensagem(errorObj.message, 'erro');
                    } else {
                        // Se for um objeto de erro mas sem formato específico
                        exibirMensagem(JSON.stringify(errorObj), 'erro');
                    }
                } catch (e) {
                    // Se não for JSON, mostra o texto do erro diretamente
                    exibirMensagem(data, 'erro');
                }
            }
        } catch (error) {
            exibirMensagem('Erro na conexão com o servidor.', 'erro');
            console.error(error);
        }
    });
});
  
  function fileToBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
      reader.readAsDataURL(file);
    });
  }
  