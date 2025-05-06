import React, { useState } from 'react';
import { criarEmpresa } from './empresaService';
import { validarCNPJ, formatarCNPJ } from './utils/validacao';
import './App.css';

interface EmpresaForm {
  razaoSocial: string;
  cnpj: string;
  endereco: string;
  telefone: string;
  email: string;
  senha: string;
  confirmarSenha: string;
  areaAtuacao: string;
}

const CadastroEmpresa: React.FC = () => {
  const [formData, setFormData] = useState<EmpresaForm>({
    razaoSocial: '',
    cnpj: '',
    endereco: '',
    telefone: '',
    email: '',
    senha: '',
    confirmarSenha: '',
    areaAtuacao: ''
  });

  const [mensagem, setMensagem] = useState<string>('');
  const [erro, setErro] = useState<string>('');
  const [cnpjValido, setCnpjValido] = useState<boolean>(true);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = event.target;
    
    if (name === 'cnpj') {
      const cnpjFormatado = formatarCNPJ(value);
      const cnpjNumerico = value.replace(/[^\d]/g, '');
      const valido = cnpjNumerico.length <= 14 && (cnpjNumerico.length < 14 || validarCNPJ(cnpjNumerico));
      
      setCnpjValido(valido);
      setFormData({ ...formData, [name]: cnpjFormatado });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    setErro('');
    setMensagem('');

    if (formData.senha !== formData.confirmarSenha) {
      setErro('As senhas não coincidem');
      return;
    }

    const cnpjNumerico = formData.cnpj.replace(/[^\d]/g, '');
    if (!validarCNPJ(cnpjNumerico)) {
      setErro('CNPJ inválido');
      return;
    }

    try {
      await criarEmpresa({
        ...formData,
        cnpj: cnpjNumerico
      });
      setMensagem('Empresa cadastrada com sucesso!');
      setFormData({
        razaoSocial: '',
        cnpj: '',
        endereco: '',
        telefone: '',
        email: '',
        senha: '',
        confirmarSenha: '',
        areaAtuacao: ''
      });
    } catch (error) {
      setErro(`Erro ao cadastrar empresa: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
    }
  };

  return (
    <div className="container">
      <h2>Cadastro de Empresa</h2>
      {mensagem && <div className="success-message">{mensagem}</div>}
      {erro && <div className="error-message">{erro}</div>}
      
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label htmlFor="razaoSocial">Razão Social:</label>
          <input
            type="text"
            id="razaoSocial"
            name="razaoSocial"
            value={formData.razaoSocial}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="cnpj">CNPJ:</label>
          <input
            type="text"
            id="cnpj"
            name="cnpj"
            value={formData.cnpj}
            onChange={handleChange}
            placeholder="00.000.000/0000-00"
            required
            className={!cnpjValido ? 'input-error' : ''}
          />
          {!cnpjValido && formData.cnpj.length > 0 && (
            <span className="error-text">CNPJ inválido</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="endereco">Endereço:</label>
          <input
            type="text"
            id="endereco"
            name="endereco"
            value={formData.endereco}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="telefone">Telefone:</label>
          <input
            type="tel"
            id="telefone"
            name="telefone"
            value={formData.telefone}
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
          <label htmlFor="areaAtuacao">Área de Atuação:</label>
          <select
            id="areaAtuacao"
            name="areaAtuacao"
            value={formData.areaAtuacao}
            onChange={handleChange}
            required
          >
            <option value="">Selecione uma área</option>
            <option value="TI">Tecnologia da Informação</option>
            <option value="Engenharia">Engenharia</option>
            <option value="Saude">Saúde</option>
            <option value="Educacao">Educação</option>
            <option value="Comercio">Comércio</option>
            <option value="Servicos">Serviços</option>
            <option value="Outros">Outros</option>
          </select>
        </div>

        <button 
          type="submit" 
          className="submit-button"
          disabled={!cnpjValido}
        >
          Cadastrar
        </button>
      </form>
    </div>
  );
};

export default CadastroEmpresa; 