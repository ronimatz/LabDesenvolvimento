import React from 'react'
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom'
import './App.css'
import CadastroAluno from './CadastroAluno'
import CadastroEmpresa from './CadastroEmpresa'
import TransferenciaMoedas from './TransferenciaMoedas'
import { isAutenticado, getUsuario, logout } from './authService'
import Login from './Login'
import SelecaoCadastro from './SelecaoCadastro'
import TelaAluno from './TelaAluno'

// Componente de rota protegida
const ProtectedRoute: React.FC<{ children: React.ReactNode, allowedTypes: string[] }> = ({ children, allowedTypes }) => {
  if (!isAutenticado()) {
    return <Navigate to="/login" />
  }

  const usuario = getUsuario()
  if (!usuario || !allowedTypes.includes(usuario.tipo)) {
    return <Navigate to="/login" />
  }

  return <>{children}</>
}

function App() {
  const handleLogout = () => {
    logout()
    window.location.href = '/login'
  }

  return (
    <Router>
      <div className="App">
        {isAutenticado() && (
          <nav className="nav">
            <Link to="/" className="nav-link">Home</Link>
            <button onClick={handleLogout} className="nav-link">Sair</button>
          </nav>
        )}

        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/selecao-cadastro" element={<SelecaoCadastro />} />
          
          <Route path="/" element={
            <ProtectedRoute allowedTypes={['ALUNO', 'PROFESSOR', 'EMPRESA']}>
              <div className="container">
                <h1>Bem-vindo ao Sistema de Moeda Estudantil</h1>
                <p>Selecione uma opção no menu acima</p>
              </div>
            </ProtectedRoute>
          } />

          <Route path="/cadastro-aluno" element={<CadastroAluno />} />
          <Route path="/cadastro-empresa" element={<CadastroEmpresa />} />

          <Route path="/aluno" element={
            <ProtectedRoute allowedTypes={['ALUNO']}>
              <TelaAluno />
            </ProtectedRoute>
          } />

          <Route path="/transferencia-moedas" element={
            <ProtectedRoute allowedTypes={['PROFESSOR']}>
              <TransferenciaMoedas />
            </ProtectedRoute>
          } />

          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
