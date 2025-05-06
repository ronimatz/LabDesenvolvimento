import axios, { AxiosError } from 'axios';

const API_URL = 'http://localhost:3000/api/auth';

export interface LoginRequest {
  email: string;
  senha: string;
  tipoUsuario: 'ALUNO' | 'PROFESSOR' | 'EMPRESA';
}

export interface LoginResponse {
  token: string;
  usuario: {
    id: string;
    nome: string;
    email: string;
    tipo: 'ALUNO' | 'PROFESSOR' | 'EMPRESA';
  };
}

export interface RespostaErro {
  message: string;
  status: number;
}

export const login = async (credenciais: LoginRequest): Promise<LoginResponse> => {
  try {
    const response = await axios.post<LoginResponse>(`${API_URL}/login`, credenciais);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      const erro = error as AxiosError<RespostaErro>;
      throw new Error(erro.response?.data?.message || 'Erro ao realizar login');
    }
    throw new Error('Erro desconhecido ao realizar login');
  }
};

export const logout = (): void => {
  localStorage.removeItem('token');
  localStorage.removeItem('usuario');
};

export const getToken = (): string | null => {
  return localStorage.getItem('token');
};

export const getUsuario = (): LoginResponse['usuario'] | null => {
  const usuarioStr = localStorage.getItem('usuario');
  return usuarioStr ? JSON.parse(usuarioStr) : null;
};

export const isAutenticado = (): boolean => {
  return !!getToken();
}; 