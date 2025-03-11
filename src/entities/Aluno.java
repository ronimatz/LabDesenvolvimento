package entities;

import java.util.ArrayList;
import java.util.List;



public class Aluno extends User {
    private List<Matricula> historicoMatriculas;

    public Aluno(String nome, String email, String senha) {
        super(nome, email, senha);
        this.historicoMatriculas = new ArrayList<>();
    }

    public List<Matricula> getHistoricoMatriculas() {
        for (Matricula m : historicoMatriculas) {
            System.out.println("Semestre: " + m.getSemestre().getIdentificador());
        }
        return historicoMatriculas;
    }

    /**
     * Retorna a matrícula do semestre indicado, ou cria uma nova se ainda não existir.
     */
    public Matricula getMatriculaDoSemestre(Semestre semestre) {
        for (Matricula m : historicoMatriculas) {
            if (m.getSemestre().getIdentificador().equals(semestre.getIdentificador())) {
                return m;
            }
        }
        Matricula nova = new Matricula(this, semestre); 
        historicoMatriculas.add(nova);
        return nova;
    }

    /**
     * Exibe o histórico de todas as disciplinas cursadas por semestre.
     */
    public void exibirHistorico() {
        for (Matricula m : historicoMatriculas) {
            System.out.println("Semestre: " + m.getSemestre().getIdentificador());
            for (Disciplina d : m.getDisciplinas()) {
                System.out.println(" - " + d.getNome());
            }
        }
    }
}