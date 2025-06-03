document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const loginData = {
            email: document.getElementById('email').value,
            senha: document.getElementById('senha').value
        };

        try {
            const response = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });

            if (!response.ok) {
                throw new Error('Credenciais inválidas');
            }

            const data = await response.json();
            localStorage.setItem('token', data.token);
            localStorage.setItem('userRole', data.role);
            localStorage.setItem('userName', data.nome);

            // Redireciona baseado no papel do usuário
            switch (data.role) {
                case 'ADMIN':
                    window.location.href = 'adminDashboard.html';
                    break;
                case 'PROFESSOR':
                    window.location.href = 'dashboardProfessor.html';
                    break;
                case 'ALUNO':
                    window.location.href = 'dashboard-aluno.html';
                    break;
                case 'EMPRESA_PARCEIRA':
                    window.location.href = 'dashboard-empresa.html';
                    break;
                default:
                    throw new Error('Tipo de usuário desconhecido');
            }

        } catch (error) {
            console.error('Erro:', error);
            alert(error.message || 'Erro ao fazer login');
        }
    });
}); 