import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login, LoginRequest } from './authService';
import './App.css';

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<LoginRequest>({
    email: '',
    senha: '',
    tipoUsuario: 'ALUNO'
  });
  const [mensagem, setMensagem] = useState<string>('');
  const [erro, setErro] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setErro('');
    setMensagem('');
    setLoading(true);

    try {
      const response = await login(formData);
      localStorage.setItem('token', response.token);
      localStorage.setItem('usuario', JSON.stringify(response.usuario));
      
      setMensagem('Login realizado com sucesso!');
      
      // Redireciona com base no tipo de usuário
      switch (response.usuario.tipo) {
        case 'ALUNO':
          navigate('/aluno');
          break;
        case 'PROFESSOR':
          navigate('/professor');
          break;
        case 'EMPRESA':
          navigate('/empresa');
          break;
      }
    } catch (error) {
      setErro(`Erro ao realizar login: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h2>Login</h2>
      {mensagem && <div className="success-message">{mensagem}</div>}
      {erro && <div className="error-message">{erro}</div>}
      
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="tipoUsuario">Tipo de Usuário:</label>
          <select
            id="tipoUsuario"
            name="tipoUsuario"
            value={formData.tipoUsuario}
            onChange={handleChange}
            required
          >
            <option value="ALUNO">Aluno</option>
            <option value="PROFESSOR">Professor</option>
            <option value="EMPRESA">Empresa Parceira</option>
          </select>
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

        <button 
          type="submit" 
          className="submit-button"
          disabled={loading}
        >
          {loading ? 'Entrando...' : 'Entrar'}
        </button>
      </form>

      <div className="cadastro-section">
        <p>Não tem uma conta?</p>
        <a 
          href="/selecao-cadastro" 
          className="cadastro-link"
          onClick={(e) => {
            e.preventDefault();
            navigate('/selecao-cadastro');
          }}
        >
          Cadastre-se
        </a>
      </div>
    </div>
  );
};

export default Login; 