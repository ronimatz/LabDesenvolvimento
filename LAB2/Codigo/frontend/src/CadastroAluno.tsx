import React, { useState, useEffect } from 'react';
import { criarAluno, listarInstituicoes, listarCursos } from './alunoService';
import './App.css';

interface AlunoForm {
  nome: string;
  cpf: string;
  rg: string;
  curso: string;
  instituicaoEnsino: string;
  email: string;
  senha: string;
  confirmarSenha: string;
}

const CadastroAluno: React.FC = () => {
  const [formData, setFormData] = useState<AlunoForm>({
    nome: '',
    cpf: '',
    rg: '',
    curso: '',
    instituicaoEnsino: '',
    email: '',
    senha: '',
    confirmarSenha: ''
  });

  const [instituicoes, setInstituicoes] = useState<string[]>([]);
  const [cursos, setCursos] = useState<string[]>([]);
  const [mensagem, setMensagem] = useState<string>('');
  const [erro, setErro] = useState<string>('');

  useEffect(() => {
    const carregarInstituicoes = async () => {
      try {
        const data = await listarInstituicoes();
        setInstituicoes(data);
      } catch (error) {
        setErro(`Erro ao carregar instituições: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
      }
    };
    carregarInstituicoes();
  }, []);

  const handleInstituicaoChange = async (event: React.ChangeEvent<HTMLSelectElement>) => {
    const instituicaoId = event.target.value;
    setFormData({ ...formData, instituicaoEnsino: instituicaoId, curso: '' });
    
    try {
      const cursosData = await listarCursos();
      setCursos(cursosData);
    } catch (error) {
      setErro(`Erro ao carregar cursos: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
    }
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setErro('');
    setMensagem('');

    if (formData.senha !== formData.confirmarSenha) {
      setErro('As senhas não coincidem');
      return;
    }

    try {
      await criarAluno(formData);
      setMensagem('Aluno cadastrado com sucesso!');
      setFormData({
        nome: '',
        cpf: '',
        rg: '',
        curso: '',
        instituicaoEnsino: '',
        email: '',
        senha: '',
        confirmarSenha: ''
      });
    } catch (error) {
      setErro(`Erro ao cadastrar aluno: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
    }
  };

  return (
    <div className="container">
      <h2>Cadastro de Aluno</h2>
      {mensagem && <div className="success-message">{mensagem}</div>}
      {erro && <div className="error-message">{erro}</div>}
      
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="nome">Nome:</label>
          <input
            type="text"
            id="nome"
            name="nome"
            value={formData.nome}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="cpf">CPF:</label>
          <input
            type="text"
            id="cpf"
            name="cpf"
            value={formData.cpf}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="rg">RG:</label>
          <input
            type="text"
            id="rg"
            name="rg"
            value={formData.rg}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="email">E-mail:</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="senha">Senha:</label>
          <input
            type="password"
            id="senha"
            name="senha"
            value={formData.senha}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="confirmarSenha">Confirmar Senha:</label>
          <input
            type="password"
            id="confirmarSenha"
            name="confirmarSenha"
            value={formData.confirmarSenha}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="instituicaoEnsino">Instituição de Ensino:</label>
          <select
            id="instituicaoEnsino"
            name="instituicaoEnsino"
            value={formData.instituicaoEnsino}
            onChange={handleInstituicaoChange}
            required
          >
            <option value="">Selecione uma instituição</option>
            {instituicoes.map((inst, index) => (
              <option key={index} value={inst}>{inst}</option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="curso">Curso:</label>
          <select
            id="curso"
            name="curso"
            value={formData.curso}
            onChange={handleChange}
            required
            disabled={!formData.instituicaoEnsino}
          >
            <option value="">Selecione um curso</option>
            {cursos.map((curso, index) => (
              <option key={index} value={curso}>{curso}</option>
            ))}
          </select>
        </div>

        <button type="submit" className="submit-button">Cadastrar</button>
      </form>
    </div>
  );
};

export default CadastroAluno; 