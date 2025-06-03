// Elementos do DOM
const saldoElement = document.getElementById('saldo');
const vantagensContainer = document.getElementById('vantagens-container');
const modalConfirmacao = document.getElementById('modal-confirmacao');
const modalCupom = document.getElementById('modal-cupom');
const vantagemNome = document.getElementById('vantagem-nome');
const vantagemValor = document.getElementById('vantagem-valor');
const vantagemEmpresa = document.getElementById('vantagem-empresa');
const saldoApos = document.getElementById('saldo-apos');
const cupomCodigo = document.getElementById('cupom-codigo');

// Variáveis globais
let vantagemSelecionada = null;
let saldoAtual = 0;

// Carregar dados do aluno e vantagens ao iniciar
document.addEventListener('DOMContentLoaded', async () => {
    await carregarSaldoAluno();
    await carregarVantagens();
});

// Carregar saldo do aluno
async function carregarSaldoAluno() {
    try {
        const response = await fetch('/alunos/info');
        const data = await response.json();
        saldoAtual = data.saldoMoedas;
        saldoElement.textContent = saldoAtual;
    } catch (error) {
        console.error('Erro ao carregar saldo:', error);
    }
}

// Carregar lista de vantagens
async function carregarVantagens() {
    try {
        const response = await fetch('/empresas-parceiras');
        const empresas = await response.json();
        
        vantagensContainer.innerHTML = '';
        
        empresas.forEach(empresa => {
            empresa.vantagens.forEach(vantagem => {
                const vantagemElement = criarElementoVantagem(vantagem, empresa);
                vantagensContainer.appendChild(vantagemElement);
            });
        });
    } catch (error) {
        console.error('Erro ao carregar vantagens:', error);
    }
}

// Criar elemento HTML para uma vantagem
function criarElementoVantagem(vantagem, empresa) {
    const div = document.createElement('div');
    div.className = 'vantagem-card';
    
    if (vantagem.fotoProduto) {
        const img = document.createElement('img');
        img.src = `data:image/jpeg;base64,${vantagem.fotoProduto}`;
        img.alt = vantagem.descricao;
        div.appendChild(img);
    }

    const info = document.createElement('div');
    info.className = 'vantagem-info';
    info.innerHTML = `
        <h3>${vantagem.descricao}</h3>
        <p>Valor: ${vantagem.valor} moedas</p>
        <p>Empresa: ${empresa.nome}</p>
        <button class="btn-resgatar" ${saldoAtual < vantagem.valor ? 'disabled' : ''}>
            Resgatar
        </button>
    `;

    div.appendChild(info);

    // Adicionar evento de clique para resgatar
    const btnResgatar = info.querySelector('.btn-resgatar');
    btnResgatar.addEventListener('click', () => iniciarResgate(vantagem, empresa));

    return div;
}

// Iniciar processo de resgate (Etapa 1)
function iniciarResgate(vantagem, empresa) {
    vantagemSelecionada = { ...vantagem, empresa };
    
    // Preencher modal de confirmação
    vantagemNome.textContent = vantagem.descricao;
    vantagemValor.textContent = vantagem.valor;
    vantagemEmpresa.textContent = empresa.nome;
    saldoApos.textContent = saldoAtual - vantagem.valor;

    // Exibir modal de confirmação
    modalConfirmacao.style.display = 'block';
}

// Configurar eventos dos botões do modal de confirmação
document.getElementById('confirmar-resgate').addEventListener('click', confirmarResgate);
document.getElementById('cancelar-resgate').addEventListener('click', () => {
    modalConfirmacao.style.display = 'none';
});

// Confirmar resgate (Etapa 2)
async function confirmarResgate() {
    try {
        const response = await fetch('/alunos/resgatar-vantagem', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                vantagemId: vantagemSelecionada.id
            })
        });

        if (!response.ok) {
            throw new Error('Erro ao resgatar vantagem');
        }

        const resultado = await response.json();
        
        // Atualizar saldo
        saldoAtual = resultado.saldoAposResgate;
        saldoElement.textContent = saldoAtual;

        // Fechar modal de confirmação
        modalConfirmacao.style.display = 'none';

        // Exibir cupom (Etapa 3)
        exibirCupom(resultado.cupomGerado);
        
        // Recarregar vantagens para atualizar botões
        await carregarVantagens();
    } catch (error) {
        console.error('Erro ao confirmar resgate:', error);
        alert('Erro ao resgatar vantagem. Tente novamente.');
    }
}

// Exibir cupom gerado (Etapa 3)
function exibirCupom(cupom) {
    cupomCodigo.textContent = cupom;
    modalCupom.style.display = 'block';
}

// Configurar evento do botão fechar do modal de cupom
document.getElementById('fechar-cupom').addEventListener('click', () => {
    modalCupom.style.display = 'none';
}); 