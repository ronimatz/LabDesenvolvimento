import axios from 'axios';

const API_URL = 'http://localhost:8080/usuarios';

export interface Usuario {
  email: string;
  senha: string;
  tipo: 'ALUNO' | 'EMPRESA';
}

export const criarUsuario = async (usuario: Usuario) => {
  try {
    const response = await axios.post(API_URL, usuario);
    return response.data;
  } catch (error) {
    console.error('Erro ao criar usuário:', error);
    throw error;
  }
}; 