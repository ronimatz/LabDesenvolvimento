package application;

import entities.Aluno;
import entities.Curso;
import entities.Disciplina;
import entities.Matricula;
import entities.Professor;
import entities.Secretaria;
import entities.Semestre;

public class App {
    public static void main(String[] args) {
        Curso c1 = new Curso("Engenharia de Software", 80);
        Aluno a1 = new Aluno("João", "q8o5w@example.com", "1234");

        Secretaria sec1 = new Secretaria("Secretaria", "sec@gmail.com", "123");
        sec1.addCurso(c1);
        Professor p1 = new Professor("Joaquim", "q8o5w@example.com", "1234");
        c1.addDisciplina(new Disciplina(c1, "POO", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "BD", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "POO2", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "POO3", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "BD2", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "BD3", false, true, p1));

        Semestre s1 = new Semestre("2025.1");

       Matricula m1 = a1.getMatriculaDoSemestre(s1);
       a1.getHistoricoMatriculas();

       m1.matricularEmDisciplina(c1.getDisciplinas().get(0));
       s1.abrirPeriodoMatricula();
       m1.matricularEmDisciplina(c1.getDisciplinas().get(0));
       m1.matricularEmDisciplina(c1.getDisciplinas().get(1));
       m1.matricularEmDisciplina(c1.getDisciplinas().get(2));
       m1.matricularEmDisciplina(c1.getDisciplinas().get(3));

       System.out.println(m1.getNumObrigatorias());
       m1.matricularEmDisciplina(c1.getDisciplinas().get(4));

   
       c1.getDisciplinas().get(0).setProfessor(p1);
       c1.getDisciplinas().get(1).setProfessor(p1);

       System.out.println(p1.getDisciplinas().size());
       p1.listarAlunosMatriculados();

       m1.cancelarMatricula(c1.getDisciplinas().get(3));
       System.out.println(m1.getDisciplinas());

       sec1.gerarCurriculo();

      

    }
}