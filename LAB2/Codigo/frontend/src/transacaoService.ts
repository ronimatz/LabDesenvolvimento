import axios, { AxiosError } from 'axios';

const API_URL = 'http://localhost:3000/api/transacoes';

export interface Transacao {
  idProfessor: string;
  idAluno: string;
  quantidadeMoedas: number;
  motivo: string;
}

export interface Aluno {
  id: string;
  nome: string;
  cpf: string;
  saldoMoedas: number;
}

export interface RespostaErro {
  message: string;
  status: number;
}

export const listarAlunos = async (): Promise<Aluno[]> => {
  try {
    const response = await axios.get<Aluno[]>(`${API_URL}/alunos`);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      const erro = error as AxiosError<RespostaErro>;
      throw new Error(erro.response?.data?.message || 'Erro ao listar alunos');
    }
    throw new Error('Erro desconhecido ao listar alunos');
  }
};

export const realizarTransacao = async (transacao: Transacao): Promise<void> => {
  try {
    await axios.post<void>(API_URL, transacao);
  } catch (error) {
    if (axios.isAxiosError(error)) {
      const erro = error as AxiosError<RespostaErro>;
      throw new Error(erro.response?.data?.message || 'Erro ao realizar transação');
    }
    throw new Error('Erro desconhecido ao realizar transação');
  }
}; 