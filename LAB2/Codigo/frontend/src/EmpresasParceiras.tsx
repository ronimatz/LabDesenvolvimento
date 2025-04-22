// src/pages/EmpresasParceiras.tsx
import React, { useState } from 'react'
import './App.css'

interface EmpresaParceira {
  id: number
  nome: string
  email: string
  cnpj: string
  vantagens: string
}

const EmpresasParceiras = () => {
  const [empresas, setEmpresas] = useState<EmpresaParceira[]>([])
  const [form, setForm] = useState<Omit<EmpresaParceira, 'id'>>({
    nome: '',
    email: '',
    cnpj: '',
    vantagens: '',
  })
  const [editId, setEditId] = useState<number | null>(null)
  const [nextId, setNextId] = useState<number>(1)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = () => {
    if (editId === null) {
      const novaEmpresa: EmpresaParceira = {
        id: nextId,
        ...form,
      }
      setEmpresas([...empresas, novaEmpresa])
      setNextId(nextId + 1)
    } else {
      const empresasAtualizadas = empresas.map((empresa) =>
        empresa.id === editId ? { id: editId, ...form } : empresa
      )
      setEmpresas(empresasAtualizadas)
      setEditId(null)
    }
    setForm({ nome: '', email: '', cnpj: '', vantagens: '' })
  }

  const handleEdit = (empresa: EmpresaParceira) => {
    setEditId(empresa.id)
    setForm({ nome: empresa.nome, email: empresa.email, cnpj: empresa.cnpj, vantagens: empresa.vantagens })
  }

  const handleDelete = (id: number) => {
    setEmpresas(empresas.filter((empresa) => empresa.id !== id))
  }

  return (
    <div className="container">
      <h2>Empresas Parceiras</h2>

      <div className="form">
        <input name="nome" placeholder="Nome da Empresa" value={form.nome} onChange={handleChange} />
        <input name="email" placeholder="Email" value={form.email} onChange={handleChange} />
        <input name="cnpj" placeholder="CNPJ" value={form.cnpj} onChange={handleChange} />
        <input name="vantagens" placeholder="Vantagens (ex: 10% desconto...)" value={form.vantagens} onChange={handleChange} />
        <button onClick={handleSubmit}>{editId === null ? 'Cadastrar' : 'Atualizar'}</button>
      </div>

      <div className="lista">
        {empresas.map((empresa) => (
          <div key={empresa.id} className="card">
            <h3>{empresa.nome}</h3>
            <p><strong>Email:</strong> {empresa.email}</p>
            <p><strong>CNPJ:</strong> {empresa.cnpj}</p>
            <p><strong>Vantagens:</strong> {empresa.vantagens}</p>
            <div className="botoes">
              <button onClick={() => handleEdit(empresa)}>Editar</button>
              <button onClick={() => handleDelete(empresa.id)}>Excluir</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default EmpresasParceiras