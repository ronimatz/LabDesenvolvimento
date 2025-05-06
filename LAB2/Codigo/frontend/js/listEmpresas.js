listarEmpresas();
async function listarEmpresas() {
    const response = await fetch('http://localhost:8080/empresas-parceiras');
    const empresas = await response.json();

    const container = document.getElementById('empresas-container');
    container.innerHTML = '';

    empresas.forEach(empresa => {
        const empresaBloco = document.createElement('div');
        empresaBloco.classList.add('empresa-bloco');

        const empresaTable = document.createElement('table');
        empresaTable.classList.add('empresa-table');

        const empresaHeader = document.createElement('thead');
        empresaHeader.innerHTML = `
            <tr>
                <th>Nome</th>
                <th>Email</th>
                <th>CNPJ</th>
                <th>Ações</th>
            </tr>
        `;
        empresaTable.appendChild(empresaHeader);

        const empresaBody = document.createElement('tbody');
        empresaBody.innerHTML = `
            <tr>
                <td>${empresa.nome}</td>
                <td>${empresa.email}</td>
                <td>${empresa.cnpj}</td>
                <td>
                    <button onclick='abrirModalEdicao(${empresa.id})'>Editar</button>
                    <button onclick="deletarEmpresa(${empresa.id})">Deletar</button>
                </td>
            </tr>
        `;
        empresaTable.appendChild(empresaBody);
        empresaBloco.appendChild(empresaTable);

        // Tabela de vantagens
        const vantagensTable = document.createElement('table');
        vantagensTable.classList.add('vantagens-table');

        const vantagensHeader = document.createElement('thead');
        vantagensHeader.innerHTML = `
            <tr>
                <th>Foto</th>
                <th>Descrição</th>
                <th>Valor</th>
                <th>Desconto</th>
            </tr>
        `;
        vantagensTable.appendChild(vantagensHeader);

        const vantagensBody = document.createElement('tbody');
        empresa.vantagens.forEach(v => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><img src="data:image/jpeg;base64,${v.fotoProduto}" width="60" height="60"></td>
                <td>${v.descricao}</td>
                <td>R$ ${v.valor}</td>
                <td>${v.desconto}%</td>
            `;
            vantagensBody.appendChild(row);
        });
        vantagensTable.appendChild(vantagensBody);

        empresaBloco.appendChild(vantagensTable);
        container.appendChild(empresaBloco);
    });
}

async function deletarEmpresa(id) {
    if (confirm("Tem certeza que deseja deletar esta empresa?")) {
        await fetch(`http://localhost:8080/empresas-parceiras/${id}`, {
            method: 'DELETE',
        });
        listarEmpresas();
    }
}

// Modal
async function abrirModalEdicao(id) {
    const response = await fetch(`http://localhost:8080/empresas-parceiras/${id}`);
    const empresa = await response.json();

    document.getElementById('empresa-id').value = empresa.id;
    document.getElementById('empresa-email').value = empresa.email;
    document.getElementById('empresa-senha').value = empresa.senha;
    document.getElementById('empresa-nome').value = empresa.nome;
    document.getElementById('empresa-cnpj').value = empresa.cnpj;

    const container = document.getElementById('vantagens-container');
    container.innerHTML = '';

    empresa.vantagens.forEach(vantagem => adicionarCampoVantagem(vantagem));

    document.getElementById('modal-edicao').style.display = 'block';
}

function fecharModal() {
    document.getElementById('modal-edicao').style.display = 'none';
}

let contadorVantagens = 0; // Para gerar IDs únicos para novas vantagens

function adicionarCampoVantagem(vantagem = {}) {
    const container = document.getElementById('vantagens-container');
    const div = document.createElement('div');
    div.classList.add('vantagem-bloco');
    const vantagemId = vantagem.id ? `vantagem-${vantagem.id}` : `nova-vantagem-${contadorVantagens++}`;
    div.dataset.vantagemId = vantagemId; // Armazena o ID da vantagem

    const descricaoLabel = document.createElement('label');
    descricaoLabel.textContent = 'Descrição:';
    const descricaoInput = document.createElement('input');
    descricaoInput.type = 'text';
    descricaoInput.classList.add('vantagem-descricao');
    descricaoInput.value = vantagem.descricao || '';

    const valorLabel = document.createElement('label');
    valorLabel.textContent = 'Valor:';
    const valorInput = document.createElement('input');
    valorInput.type = 'number';
    valorInput.classList.add('vantagem-valor');
    valorInput.value = vantagem.valor || 0;
    valorInput.step = '0.01';

    const descontoLabel = document.createElement('label');
    descontoLabel.textContent = 'Desconto (%):';
    const descontoInput = document.createElement('input');
    descontoInput.type = 'number';
    descontoInput.classList.add('vantagem-desconto');
    descontoInput.value = vantagem.desconto || 0;
    descontoInput.step = '0.01';

    const fotoLabel = document.createElement('label');
    fotoLabel.textContent = 'Foto:';
    const fotoInput = document.createElement('input');
    fotoInput.type = 'file';
    fotoInput.classList.add('vantagem-foto');

    const removerButton = document.createElement('button');
    removerButton.textContent = 'Remover';
    removerButton.type = 'button';
    removerButton.onclick = function() {
        container.removeChild(div);
    };

    div.appendChild(descricaoLabel);
    div.appendChild(descricaoInput);
    div.appendChild(valorLabel);
    div.appendChild(valorInput);
    div.appendChild(descontoLabel);
    div.appendChild(descontoInput);
    div.appendChild(fotoLabel);
    div.appendChild(fotoInput);
    if (vantagem.id) {
        div.appendChild(removerButton);
    }

    container.appendChild(div);
}

async function salvarEdicao() {
    const empresaId = document.getElementById('empresa-id').value;
    const email = document.getElementById('empresa-email').value;
    const senha = document.getElementById('empresa-senha').value;
    const nome = document.getElementById('empresa-nome').value;
    const cnpj = document.getElementById('empresa-cnpj').value;

    const vantagemBlocos = document.querySelectorAll('#vantagens-container .vantagem-bloco');
    const vantagensParaAdicionar = [];
    const vantagensParaAtualizar = [];

    for (const bloco of vantagemBlocos) {
        const descricaoInput = bloco.querySelector('.vantagem-descricao');
        const valorInput = bloco.querySelector('.vantagem-valor');
        const descontoInput = bloco.querySelector('.vantagem-desconto');
        const fotoInput = bloco.querySelector('.vantagem-foto');
        const vantagemId = bloco.dataset.vantagemId;
        const file = fotoInput ? fotoInput.files[0] : null;
        const base64 = file ? await toBase64(file) : null;

        const vantagemData = {
            descricao: descricaoInput.value,
            valor: parseFloat(valorInput.value),
            desconto: parseFloat(descontoInput.value),
        };

        if (base64) {
            vantagemData.fotoProduto = base64;
        }

        if (vantagemId && vantagemId.startsWith('vantagem-')) {
            vantagemData.id = parseInt(vantagemId.split('-')[1]);
            vantagensParaAtualizar.push(vantagemData);
        } else if (vantagemId && vantagemId.startsWith('nova-')) {
            vantagensParaAdicionar.push(vantagemData);
        }
    }

    // Enviar as edições da empresa
    const empresaDTO = { email, senha, nome, cnpj };
    await fetch(`http://localhost:8080/empresas-parceiras/${empresaId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(empresaDTO)
    });

    // Adicionar novas vantagens
    for (const vantagem of vantagensParaAdicionar) {
        await fetch(`http://localhost:8080/empresas-parceiras/${empresaId}/vantagens`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(vantagem)
        });
    }

    // Editar vantagens existentes
    for (const vantagem of vantagensParaAtualizar) {
        await fetch(`http://localhost:8080/empresas-parceiras/vantagens/${vantagem.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(vantagem)
        });
    }

    fecharModal();
    listarEmpresas();
    alert("Empresa atualizada com sucesso!");
}

function toBase64(file) {
    return new Promise((resolve, reject) => {
        if (!file) return resolve(null);
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result.split(',')[1]);
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}