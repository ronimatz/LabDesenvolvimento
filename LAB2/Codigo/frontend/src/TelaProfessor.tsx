import React, { useState, useEffect } from 'react';
import { listarAlunos, realizarTransacao, Aluno } from './transacaoService';
import { getUsuario } from './authService';
import './styles/TelaProfessor.css';

interface TransferenciaForm {
  idAluno: string;
  quantidadeMoedas: number;
  motivo: string;
}

const TelaProfessor: React.FC = () => {
  const [alunos, setAlunos] = useState<Aluno[]>([]);
  const [formData, setFormData] = useState<TransferenciaForm>({
    idAluno: '',
    quantidadeMoedas: 0,
    motivo: ''
  });
  const [mensagem, setMensagem] = useState<string>('');
  const [erro, setErro] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [searchTerm, setSearchTerm] = useState<string>('');

  useEffect(() => {
    const carregarAlunos = async () => {
      try {
        const data = await listarAlunos();
        setAlunos(data);
      } catch (error) {
        setErro(`Erro ao carregar alunos: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
      }
    };
    carregarAlunos();
  }, []);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setErro('');
    setMensagem('');
    setLoading(true);

    try {
      const usuario = getUsuario();
      if (!usuario) {
        throw new Error('Usuário não autenticado');
      }
      
      await realizarTransacao({
        idProfessor: usuario.id,
        idAluno: formData.idAluno,
        quantidadeMoedas: Number(formData.quantidadeMoedas),
        motivo: formData.motivo
      });

      setMensagem('Transferência realizada com sucesso!');
      setFormData({
        idAluno: '',
        quantidadeMoedas: 0,
        motivo: ''
      });

      // Atualizar lista de alunos
      const data = await listarAlunos();
      setAlunos(data);
    } catch (error) {
      setErro(`Erro ao realizar transferência: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
    } finally {
      setLoading(false);
    }
  };

  const filteredAlunos = alunos.filter(aluno => 
    aluno.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
    aluno.cpf.includes(searchTerm)
  );

  return (
    <div className="professor-container">
      <div className="professor-header">
        <h2>Área do Professor</h2>
      </div>

      {mensagem && <div className="success-message">{mensagem}</div>}
      {erro && <div className="error-message">{erro}</div>}

      <div className="professor-content">
        <div className="transferencia-section">
          <h3>Transferência de Moedas</h3>
          <form onSubmit={handleSubmit} className="form">
            <div className="form-group">
              <label htmlFor="idAluno">Aluno:</label>
              <select
                id="idAluno"
                name="idAluno"
                value={formData.idAluno}
                onChange={handleChange}
                required
              >
                <option value="">Selecione um aluno</option>
                {alunos.map(aluno => (
                  <option key={aluno.id} value={aluno.id}>
                    {aluno.nome} (CPF: {aluno.cpf}) - Saldo: {aluno.saldoMoedas} moedas
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="quantidadeMoedas">Quantidade de Moedas:</label>
              <input
                type="number"
                id="quantidadeMoedas"
                name="quantidadeMoedas"
                value={formData.quantidadeMoedas}
                onChange={handleChange}
                min="1"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="motivo">Motivo da Transferência:</label>
              <textarea
                id="motivo"
                name="motivo"
                value={formData.motivo}
                onChange={handleChange}
                required
                rows={4}
              />
            </div>

            <button 
              type="submit" 
              className="submit-button"
              disabled={loading}
            >
              {loading ? 'Processando...' : 'Realizar Transferência'}
            </button>
          </form>
        </div>

        <div className="alunos-section">
          <h3>Lista de Alunos</h3>
          <div className="search-box">
            <input
              type="text"
              placeholder="Buscar por nome ou CPF..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
          </div>
          
          <div className="alunos-grid">
            {filteredAlunos.map(aluno => (
              <div key={aluno.id} className="aluno-card">
                <h4>{aluno.nome}</h4>
                <p>CPF: {aluno.cpf}</p>
                <div className="saldo-info">
                  <span className="saldo-valor">{aluno.saldoMoedas}</span>
                  <span className="saldo-label">moedas</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default TelaProfessor; 