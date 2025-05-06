import React, { useState, useEffect } from 'react';
import { buscarExtratoAluno, listarVantagensDisponiveis, adquirirVantagem, Extrato, Vantagem } from './alunoService';
import { getUsuario } from './authService';
import './styles/TelaAluno.css';

const TelaAluno: React.FC = () => {
  const [extrato, setExtrato] = useState<Extrato | null>(null);
  const [vantagens, setVantagens] = useState<Vantagem[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState<string>('');
  const [mensagem, setMensagem] = useState<string>('');

  useEffect(() => {
    const carregarDados = async () => {
      try {
        const usuario = getUsuario();
        if (!usuario) {
          throw new Error('Usuário não autenticado');
        }
        
        const [extratoData, vantagensData] = await Promise.all([
          buscarExtratoAluno(Number(usuario.id)),
          listarVantagensDisponiveis()
        ]);
        setExtrato(extratoData);
        setVantagens(vantagensData);
      } catch (error) {
        setErro('Erro ao carregar dados. Por favor, tente novamente.');
        console.error('Erro:', error);
      } finally {
        setLoading(false);
      }
    };

    carregarDados();
  }, []);

  const handleAdquirirVantagem = async (vantagemId: number) => {
    try {
      setErro('');
      setMensagem('');
      const usuario = getUsuario();
      if (!usuario) {
        throw new Error('Usuário não autenticado');
      }
      
      await adquirirVantagem(Number(usuario.id), vantagemId);
      setMensagem('Vantagem adquirida com sucesso!');
      
      // Recarregar dados
      const [extratoData, vantagensData] = await Promise.all([
        buscarExtratoAluno(Number(usuario.id)),
        listarVantagensDisponiveis()
      ]);
      setExtrato(extratoData);
      setVantagens(vantagensData);
    } catch (error) {
      setErro('Erro ao adquirir vantagem. Por favor, tente novamente.');
      console.error('Erro:', error);
    }
  };

  if (loading) {
    return <div className="loading">Carregando...</div>;
  }

  return (
    <div className="aluno-container">
      <div className="aluno-header">
        <h2>Área do Aluno</h2>
      </div>

      {mensagem && <div className="success-message">{mensagem}</div>}
      {erro && <div className="error-message">{erro}</div>}

      <div className="saldo-section">
        <div className="saldo-card">
          <span className="saldo-valor">{extrato?.saldoMoedas || 0}</span>
          <span className="saldo-label">Moedas Disponíveis</span>
        </div>
      </div>

      <div className="vantagens-section">
        <h3>Vantagens Disponíveis</h3>
        <div className="vantagens-grid">
          {vantagens.map((vantagem) => (
            <div key={vantagem.id} className="vantagem-card">
              <h4>{vantagem.titulo}</h4>
              <p>{vantagem.descricao}</p>
              <div className="vantagem-info">
                <span className="vantagem-empresa">{vantagem.empresa}</span>
                <span className="vantagem-custo">{vantagem.custoMoedas} moedas</span>
              </div>
              <button
                className="adquirir-button"
                onClick={() => handleAdquirirVantagem(vantagem.id)}
                disabled={!extrato || extrato.saldoMoedas < vantagem.custoMoedas}
              >
                {!extrato || extrato.saldoMoedas < vantagem.custoMoedas
                  ? 'Saldo Insuficiente'
                  : 'Adquirir'}
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default TelaAluno; 