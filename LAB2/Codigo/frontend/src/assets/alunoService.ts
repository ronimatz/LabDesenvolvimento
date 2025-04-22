import axios from 'axios';

const API_URL = 'http://localhost:8080/alunos';

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

export const criarAluno = async (aluno: any) => {
  try {
    const response = await axios.post(API_URL, aluno);
    return response.data;
  } catch (error) {
    console.error('Erro ao criar aluno:', error);
    throw error;
  }
};

export const atualizarAluno = async (id: number, aluno: any) => {
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
    // Simulação de dados, substitua pela chamada real ao backend
    return ['Instituição A', 'Instituição B', 'Instituição C'];
  } catch (error) {
    console.error('Erro ao listar instituições:', error);
    throw error;
  }
};

export const listarCursos = async (instituicaoId: string) => {
  try {
    // Simulação de dados, substitua pela chamada real ao backend
    return ['Curso 1', 'Curso 2', 'Curso 3'];
  } catch (error) {
    console.error('Erro ao listar cursos:', error);
    throw error;
  }
}; 