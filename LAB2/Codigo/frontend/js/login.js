document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const errorMessages = document.getElementById('errorMessages');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        errorMessages.innerHTML = ''; // limpa erros anteriores

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

            const data = await response.json();

            if (!response.ok) {
                if (data.error) {
                    // erro de autenticação
                    errorMessages.innerHTML = `<p>${data.error}</p>`;
                } else {
                    // erro de validação
                    for (const campo in data) {
                        const msg = document.createElement('p');
                        msg.textContent = data[campo];
                        errorMessages.appendChild(msg);
                    }
                }
                return;
            }

            // Login bem-sucedido
            localStorage.setItem('token', data.token);
            localStorage.setItem('userRole', data.role);
            localStorage.setItem('userName', data.nome);

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
            errorMessages.innerHTML = `<p>${error.message || 'Erro ao fazer login'}</p>`;
        }
    });
});