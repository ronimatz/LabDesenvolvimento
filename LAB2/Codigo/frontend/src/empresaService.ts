import axios from 'axios';
import { criarUsuario, Usuario } from './usuarioService';

const API_URL = 'http://localhost:8080/empresas';

export interface Empresa {
  razaoSocial: string;
  cnpj: string;
  endereco: string;
  telefone: string;
  email: string;
  senha: string;
  areaAtuacao: string;
}

export const criarEmpresa = async (empresa: Empresa): Promise<void> => {
  try {
    // Primeiro, criar o usuário
    const usuario: Usuario = {
      email: empresa.email,
      senha: empresa.senha,
      tipo: 'EMPRESA'
    };
    await criarUsuario(usuario);

    // Depois, criar a empresa
    await axios.post(API_URL, empresa);
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data?.message || 'Erro ao criar empresa');
    }
    throw error;
  }
};

export const listarEmpresas = async (): Promise<Empresa[]> => {
  try {
    const response = await axios.get(API_URL);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data?.message || 'Erro ao listar empresas');
    }
    throw error;
  }
};

export const buscarEmpresaPorId = async (id: string): Promise<Empresa> => {
  try {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(error.response?.data?.message || 'Erro ao buscar empresa');
    }
    throw error;
  }
}; 