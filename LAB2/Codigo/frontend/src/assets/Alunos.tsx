import React, { useState, useEffect } from 'react';
import { listarInstituicoes, listarCursos } from './alunoService';

interface AlunoProps {
  nome: string;
  cpf: string;
  rg: string;
  curso: string;
  instituicaoEnsino: string;
}

const Aluno: React.FC<AlunoProps> = ({ nome, cpf, rg, curso, instituicaoEnsino }) => {
  const [instituicoes, setInstituicoes] = useState<string[]>([]);
  const [cursos, setCursos] = useState<string[]>([]);

  useEffect(() => {
    const fetchInstituicoes = async () => {
      const data = await listarInstituicoes();
      setInstituicoes(data);
    };
    fetchInstituicoes();
  }, []);

  const handleInstituicaoChange = async (event: React.ChangeEvent<HTMLSelectElement>) => {
    const instituicaoId = event.target.value;
    const cursosData = await listarCursos(instituicaoId);
    setCursos(cursosData);
  };

  return (
    <div className="aluno">
      <h2>Informações do Aluno</h2>
      <p><strong>Nome:</strong> {nome}</p>
      <p><strong>CPF:</strong> {cpf}</p>
      <p><strong>RG:</strong> {rg}</p>
      <div>
        <label>Instituição de Ensino:</label>
        <select onChange={handleInstituicaoChange} defaultValue="">
          <option value="" disabled>Selecione uma instituição</option>
          {instituicoes.map((inst, index) => (
            <option key={index} value={inst}>{inst}</option>
          ))}
        </select>
      </div>
      <div>
        <label>Curso:</label>
        <select defaultValue="">
          <option value="" disabled>Selecione um curso</option>
          {cursos.map((curso, index) => (
            <option key={index} value={curso}>{curso}</option>
          ))}
        </select>
      </div>
    </div>
  );
};

export default Aluno; 