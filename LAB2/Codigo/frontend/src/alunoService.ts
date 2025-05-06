import axios from 'axios';
import { criarUsuario, Usuario } from './usuarioService';

const API_URL = 'http://localhost:8080/alunos';

export interface Aluno {
  id?: number;
  nome: string;
  cpf: string;
  rg: string;
  curso: string;
  instituicaoEnsino: string;
  email: string;
  senha: string;
}

export interface Extrato {
  id: number;
  saldoMoedas: number;
  transacoes: Transacao[];
}

export interface Transacao {
  id: number;
  valor: number;
  tipo: 'CREDITO' | 'DEBITO';
  descricao: string;
  data: string;
}

export interface Vantagem {
  id: number;
  titulo: string;
  descricao: string;
  custoMoedas: number;
  empresa: string;
}

export const listarAlunos = async () => {
  try {
    const response = await axios.get(API_URL);
    return response.data;
  } catch (error) {
    console.error('Erro ao listar alunos:', error);
    throw error;
  }
};

export const buscarAlunoPorId = async (id: number) => {
  try {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error('Erro ao buscar aluno:', error);
    throw error;
  }
};

export const criarAluno = async (aluno: Aluno) => {
  try {
    // Primeiro, criar o usuário
    const usuario: Usuario = {
      email: aluno.email,
      senha: aluno.senha,
      tipo: 'ALUNO'
    };
    await criarUsuario(usuario);

    // Depois, criar o aluno
    const response = await axios.post(API_URL, aluno);
    return response.data;
  } catch (error) {
    console.error('Erro ao criar aluno:', error);
    throw error;
  }
};

export const atualizarAluno = async (id: number, aluno: Aluno) => {
  try {
    const response = await axios.put(`${API_URL}/${id}`, aluno);
    return response.data;
  } catch (error) {
    console.error('Erro ao atualizar aluno:', error);
    throw error;
  }
};

export const deletarAluno = async (id: number) => {
  try {
    await axios.delete(`${API_URL}/${id}`);
  } catch (error) {
    console.error('Erro ao deletar aluno:', error);
    throw error;
  }
};

export const listarInstituicoes = async () => {
  try {
    // Simulação de dados, substituir pela chamada real ao backend
    return ['Instituição A', 'Instituição B', 'Instituição C'];
  } catch (error) {
    console.error('Erro ao listar instituições:', error);
    throw error;
  }
};

export const listarCursos = async () => {
  try {
    // Simulação de dados, substituir pela chamada real ao backend
    return ['Curso 1', 'Curso 2', 'Curso 3'];
  } catch (error) {
    console.error('Erro ao listar cursos:', error);
    throw error;
  }
};

export const buscarExtratoAluno = async (id: number): Promise<Extrato> => {
  try {
    const response = await axios.get(`${API_URL}/${id}/extrato`);
    return response.data;
  } catch (error) {
    console.error('Erro ao buscar extrato do aluno:', error);
    throw error;
  }
};

export const listarVantagensDisponiveis = async (): Promise<Vantagem[]> => {
  try {
    const response = await axios.get(`${API_URL}/vantagens`);
    return response.data;
  } catch (error) {
    console.error('Erro ao listar vantagens:', error);
    throw error;
  }
};

export const adquirirVantagem = async (alunoId: number, vantagemId: number): Promise<void> => {
  try {
    await axios.post(`${API_URL}/${alunoId}/vantagens/${vantagemId}`);
  } catch (error) {
    console.error('Erro ao adquirir vantagem:', error);
    throw error;
  }
}; 