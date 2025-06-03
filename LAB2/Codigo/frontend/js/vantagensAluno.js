// Elementos do DOM
const vantagensGrid = document.getElementById('vantagens-grid');
const modal = document.getElementById('confirmModal');
const vantagemNomeModal = document.getElementById('vantagemNomeModal');
const vantagemEmpresaModal = document.getElementById('vantagemEmpresaModal');
const vantagemValorModal = document.getElementById('vantagemValorModal');
const saldoAposModal = document.getElementById('saldoAposModal');
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
        <div class="vantagem-card">
            <img src="data:image/jpeg;base64,${vantagem.fotoProduto}" alt="${vantagem.descricao}">
            <div class="vantagem-info">
                <h3>${vantagem.descricao}</h3>
                <p>Valor: ${vantagem.valor} moedas</p>
                <p>Empresa: ${vantagem.empresaNome}</p>
                <button class="comprar-btn" onclick="abrirModal(${JSON.stringify(vantagem).replace(/"/g, '&quot;')})">
                    <span class="cart-icon">🛒</span>
                    Resgatar
                </button>
            </div>
        </div>
    `).join('');
}

// Abrir modal de confirmação
function abrirModal(vantagem) {
    vantagemSelecionada = vantagem;
    vantagemNomeModal.textContent = vantagem.descricao;
    vantagemEmpresaModal.textContent = vantagem.empresaNome;
    vantagemValorModal.textContent = `${vantagem.valor} moedas`;
    saldoAposModal.textContent = `${alunoSaldo - vantagem.valor} moedas`;
    modal.style.display = 'block';
}

// Fechar modal
function fecharModal() {
    modal.style.display = 'none';
    vantagemSelecionada = null;
}

// Confirmar compra
async function confirmarCompra() {
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