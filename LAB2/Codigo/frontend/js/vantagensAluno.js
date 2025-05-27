// Elementos do DOM
const vantagensGrid = document.getElementById('vantagens-grid');
const modal = document.getElementById('modal-confirmacao');
const modalFoto = document.getElementById('modal-foto');
const modalNome = document.getElementById('modal-nome');
const modalPreco = document.getElementById('modal-preco');
const modalSaldoAtual = document.getElementById('modal-saldo-atual');
const modalSaldoApos = document.getElementById('modal-saldo-apos');
const confirmarBtn = document.getElementById('confirmar-btn');
const cancelarBtn = document.getElementById('cancelar-btn');
const saldoMoedasElement = document.getElementById('saldoMoedas');

let alunoSaldo = 0;
let vantagemSelecionada = null;

// Carregar dados do aluno
async function carregarDadosAluno() {
    try {
        const response = await fetch('http://localhost:8080/alunos/info', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        
        if (!response.ok) throw new Error('Erro ao carregar dados do aluno');
        
        const aluno = await response.json();
        alunoSaldo = aluno.saldoMoedas;
        saldoMoedasElement.textContent = alunoSaldo;
        
        return aluno;
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar dados do aluno');
    }
}

// Carregar vantagens disponíveis
async function carregarVantagens() {
    try {
        const response = await fetch('http://localhost:8080/empresas-parceiras', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        
        if (!response.ok) throw new Error('Erro ao carregar vantagens');
        
        const empresas = await response.json();
        const todasVantagens = empresas.flatMap(empresa => 
            empresa.vantagens.map(vantagem => ({
                ...vantagem,
                empresaNome: empresa.nome
            }))
        );
        
        renderizarVantagens(todasVantagens);
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar vantagens');
    }
}

// Renderizar vantagens na grid
function renderizarVantagens(vantagens) {
    vantagensGrid.innerHTML = vantagens.map(vantagem => `
        <div class="vantagem-card" onclick="abrirModal(${JSON.stringify(vantagem).replace(/"/g, '&quot;')})">
            <img src="data:image/jpeg;base64,${vantagem.fotoProduto}" alt="${vantagem.descricao}">
            <div class="vantagem-info">
                <h3>${vantagem.descricao}</h3>
                <p>Valor: ${vantagem.valor} moedas</p>
                <p>Empresa: ${vantagem.empresaNome}</p>
                <button>Resgatar</button>
            </div>
        </div>
    `).join('');
}

// Abrir modal de confirmação
function abrirModal(vantagem) {
    vantagemSelecionada = vantagem;
    modalFoto.src = `data:image/jpeg;base64,${vantagem.fotoProduto}`;
    modalNome.textContent = vantagem.descricao;
    modalPreco.textContent = `${vantagem.valor} moedas`;
    modalSaldoAtual.textContent = `${alunoSaldo} moedas`;
    modalSaldoApos.textContent = `${alunoSaldo - vantagem.valor} moedas`;
    modal.style.display = 'block';
}

// Fechar modal
function fecharModal() {
    modal.style.display = 'none';
    vantagemSelecionada = null;
}

// Confirmar resgate
async function confirmarResgate() {
    if (!vantagemSelecionada) return;

    try {
        const response = await fetch('http://localhost:8080/alunos/resgatar-vantagem', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify({
                vantagemId: vantagemSelecionada.id
            })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }

        alert('Vantagem resgatada com sucesso!');
        fecharModal();
        await carregarDadosAluno(); // Atualizar saldo
    } catch (error) {
        console.error('Erro:', error);
        alert(error.message || 'Erro ao resgatar vantagem');
    }
}

// Event listeners
cancelarBtn.addEventListener('click', fecharModal);
confirmarBtn.addEventListener('click', confirmarResgate);

// Fechar modal quando clicar fora
window.addEventListener('click', (event) => {
    if (event.target === modal) {
        fecharModal();
    }
});

// Inicialização
document.addEventListener('DOMContentLoaded', async () => {
    await carregarDadosAluno();
    await carregarVantagens();
}); 