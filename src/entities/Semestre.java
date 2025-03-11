package entities;

import java.util.HashSet;
import java.util.Set;

public class Semestre {
    private String identificador; // Ex: "2025.1", "2025.2"
    private boolean periodoMatriculaAberto;

    public Semestre(String identificador) {
        this.identificador = identificador;
        this.periodoMatriculaAberto = false; 
    }

    public String getIdentificador() {
        return identificador;
    }

    public boolean isPeriodoMatriculaAberto() {
        return periodoMatriculaAberto;
    }

    public boolean abrirPeriodoMatricula() {
        return this.periodoMatriculaAberto = true;
    }



        public boolean fecharPeriodoMatricula(Curso curso, Semestre semestreAtual) {
    this.periodoMatriculaAberto = false;
    curso.verificarTodasDisciplinas();

    Set<Aluno> alunosProcessados = new HashSet<>();

    for (Disciplina disciplina : curso.getDisciplinas()) {
        for (Aluno aluno : disciplina.getAlunos()) {
            // Garante que não cobrará o mesmo aluno mais de uma vez
            if (!alunosProcessados.contains(aluno)) {
                alunosProcessados.add(aluno);

                for (Matricula matricula : aluno.getHistoricoMatriculas()) {
                    if (matricula.getSemestre().equals(semestreAtual)) {
                        matricula.notificarCobranca(); // dispara a cobrança
                    }
                }
            }
        }
    }

    return periodoMatriculaAberto;
}

    @Override
    public String toString() {
        return identificador;
    }
}