function adicionarVantagem() {
    const container = document.getElementById('vantagensContainer');
  
    const div = document.createElement('div');
    div.className = 'vantagem';
  
    div.innerHTML = `
      <label>Descrição:</label>
      <input type="text" class="descricao" required>
  
      <label>Valor:</label>
      <input type="number" class="valor" step="0.01" required>
  
      <label>Desconto:</label>
      <input type="number" class="desconto" step="0.01" required>
  
      <label>Foto (base64 opcional):</label>
      <input type="file" class="fotoProduto" accept="image/*">
    `;
  
    container.appendChild(div);
  }
  
  document.getElementById('empresaForm').addEventListener('submit', async function (event) {
    event.preventDefault();
  
    const vantagensElements = document.querySelectorAll('.vantagem');
    const vantagens = [];
  
    for (const el of vantagensElements) {
      const descricao = el.querySelector('.descricao').value;
      const valor = parseFloat(el.querySelector('.valor').value);
      const desconto = parseFloat(el.querySelector('.desconto').value);
      const fotoFile = el.querySelector('.fotoProduto').files[0];
  
      let fotoProduto = null;
      if (fotoFile) {
        const base64 = await fileToBase64(fotoFile);
        fotoProduto = base64.split(',')[1]; 
      }
  
      
      vantagens.push({ descricao, valor, desconto, fotoProduto });
    }
  
    const empresa = {
      email: document.getElementById('email').value,
      senha: document.getElementById('senha').value,
      nome: document.getElementById('nome').value,
      cnpj: document.getElementById('cnpj').value,
      vantagens
    };
  
    try {
      const response = await fetch('http://localhost:8080/empresas-parceiras', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(empresa)
      });
  
      if (!response.ok) {
        const error = await response.text();
        alert('Erro: ' + error);
        return;
      }
  
      alert('Empresa cadastrada com sucesso!');
      location.reload();
  
    } catch (error) {
      console.error(error);
      alert('Erro ao cadastrar empresa.');
    }
  });
  
  function fileToBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
      reader.readAsDataURL(file);
    });
  }
  