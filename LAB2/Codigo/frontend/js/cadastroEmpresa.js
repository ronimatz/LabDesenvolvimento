document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('cadastroEmpresaForm');
    const cnpjInput = document.getElementById('cnpj');
    const telefoneInput = document.getElementById('telefone');

    // Máscara para CNPJ
    cnpjInput.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 14) value = value.slice(0, 14);
        
        if (value.length > 12) {
            value = value.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2}).*/, '$1.$2.$3/$4-$5');
        } else if (value.length > 8) {
            value = value.replace(/^(\d{2})(\d{3})(\d{3})(\d*).*/, '$1.$2.$3/$4');
        } else if (value.length > 5) {
            value = value.replace(/^(\d{2})(\d{3})(\d*).*/, '$1.$2.$3');
        } else if (value.length > 2) {
            value = value.replace(/^(\d{2})(\d*).*/, '$1.$2');
        }
        
        e.target.value = value;
    });

    // Máscara para telefone
    telefoneInput.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) value = value.slice(0, 11);
        
        if (value.length > 6) {
            value = value.replace(/^(\d{2})(\d{5})(\d*).*/, '($1) $2-$3');
        } else if (value.length > 2) {
            value = value.replace(/^(\d{2})(\d*).*/, '($1) $2');
        }
        
        e.target.value = value;
    });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        // Formata o CNPJ se não estiver formatado
        let cnpj = cnpjInput.value;
        if (!cnpj.includes('.')) {
            cnpj = cnpj.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2}).*/, '$1.$2.$3/$4-$5');
        }

        // Remove formatação apenas do telefone
        const telefone = telefoneInput.value.replace(/\D/g, '');

        const formData = {
            email: form.email.value,
            senha: form.senha.value,
            cnpj: cnpj,
            nomeFantasia: form.nomeFantasia.value,
            descricao: form.descricao.value,
            endereco: form.endereco.value,
            telefone: telefone
        };

        try {
            const response = await fetch('http://localhost:8080/empresas-parceiras/cadastro', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert('Empresa cadastrada com sucesso!');
                window.location.href = 'login.html';
            } else {
                const error = await response.text();
                throw new Error(error);
            }
        } catch (error) {
            alert('Erro ao cadastrar empresa: ' + error.message);
        }
    });
}); 