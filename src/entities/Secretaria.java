package entities;

import java.util.ArrayList;
import java.util.List;

public class Secretaria extends User {

    List<Curso> cursos = new ArrayList<>();

    public Secretaria() {
    }

    public Secretaria(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void addCurso(Curso c) {
        this.cursos.add(c);
    }

    public void removeCurso(Curso c) {
        this.cursos.remove(c);
    }

    public void gerarCurriculo() {
        for (Curso c : cursos) {
            System.out.println();
            System.out.println(c.getNome());
            System.out.println("DISCIPLINAS:");
            for (Disciplina d : c.getDisciplinas()) {
                System.out.println(d.getNome());
                System.out.println("Professor: " + d.getProfessor().getNome());
                System.out.println("ALUNOS: ");
                for (Aluno a : d.getAlunos()) {
                    System.out.println(a.getNome());
                }
                System.out.println();
        }
    }

}
}