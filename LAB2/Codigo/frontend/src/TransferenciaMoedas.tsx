import React, { useState, useEffect } from 'react';
import { listarAlunos, realizarTransacao, Aluno } from './transacaoService';
import './App.css';

interface TransferenciaForm {
  idAluno: string;
  quantidadeMoedas: number;
  motivo: string;
}

const TransferenciaMoedas: React.FC = () => {
  const [alunos, setAlunos] = useState<Aluno[]>([]);
  const [formData, setFormData] = useState<TransferenciaForm>({
    idAluno: '',
    quantidadeMoedas: 0,
    motivo: ''
  });
  const [mensagem, setMensagem] = useState<string>('');
  const [erro, setErro] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

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
      // TODO: Substituir pelo ID do professor logado
      const idProfessor = '1';
      
      await realizarTransacao({
        idProfessor,
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

  const alunoSelecionado = alunos.find(aluno => aluno.id === formData.idAluno);

  return (
    <div className="container">
      <h2>Transferência de Moedas</h2>
      {mensagem && <div className="success-message">{mensagem}</div>}
      {erro && <div className="error-message">{erro}</div>}
      
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

        {alunoSelecionado && (
          <div className="aluno-info">
            <h3>Informações do Aluno</h3>
            <p>Nome: {alunoSelecionado.nome}</p>
            <p>CPF: {alunoSelecionado.cpf}</p>
            <p>Saldo atual: {alunoSelecionado.saldoMoedas} moedas</p>
          </div>
        )}

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
  );
};

export default TransferenciaMoedas; 