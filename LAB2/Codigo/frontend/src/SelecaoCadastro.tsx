import React from 'react';
import { useNavigate } from 'react-router-dom';
import './App.css';

const SelecaoCadastro: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="container">
      <h2>Escolha seu tipo de cadastro</h2>
      <div className="cadastro-options">
        <div className="cadastro-option" onClick={() => navigate('/cadastro-aluno')}>
          <h3>Aluno</h3>
          <p>Cadastre-se como aluno para encontrar oportunidades de estágio</p>
          <button className="cadastro-button">Cadastrar como Aluno</button>
        </div>

        <div className="cadastro-option" onClick={() => navigate('/cadastro-empresa')}>
          <h3>Empresa</h3>
          <p>Cadastre sua empresa para oferecer oportunidades de estágio</p>
          <button className="cadastro-button">Cadastrar como Empresa</button>
        </div>
      </div>
    </div>
  );
};

export default SelecaoCadastro; 