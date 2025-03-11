package entities;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Professor(String nome, String senha, String login) {
        super(nome, senha, login);
    }


    public List<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public void addDisciplina(Disciplina d) {
        this.disciplinas.add(d);
    }

    public void listarAlunosMatriculados() {
        for (Disciplina disciplina : disciplinas) {
            System.out.println("Disciplina: " + disciplina.getNome());
            for (Aluno aluno : disciplina.getAlunos()) {
                System.out.println("Alunos");
                System.out.println(aluno.getNome() + " - " + aluno.getEmail());
            }
            System.out.println();
        }
    }
    

}