package entities;
import java.util.ArrayList;
import java.util.List;

public class Matricula {
    private Aluno aluno;
    private Semestre semestre;
    private List<Disciplina> disciplinas;

    private Integer numObrigatorias;
    private Integer numOptativas;

    public Matricula(Aluno aluno, Semestre semestre) {
        this.aluno = aluno;
        this.semestre = semestre;
        this.disciplinas = new ArrayList<>();
        this.numObrigatorias = 0;
        this.numOptativas = 0;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }
    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public Integer getNumObrigatorias() {
        return this.numObrigatorias;
    }

    public void setNumObrigatorias(Integer numObrigatorias) {
        this.numObrigatorias = numObrigatorias;
    }

    public Integer getNumOptativas() {
        return this.numOptativas;
    }

    public void setNumOptativas(Integer numOptativas) {
        this.numOptativas = numOptativas;
    }


    private void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
        if (disciplina.isObrigatoria()) {
            numObrigatorias++;
        }
        else{
            numOptativas++;
        }
    }

    public void removerDisciplina(Disciplina disciplina) {
        disciplinas.remove(disciplina);
        if (disciplina.isObrigatoria()) {
            numObrigatorias--;  
        } else {
            numOptativas--;  
        }
    }

    public void matricularEmDisciplina(Disciplina disciplina) {
        if (!semestre.isPeriodoMatriculaAberto()) {
            System.out.println("Matrícula não permitida. Período de matrícula está fechado.");
            return;
        }

        if (disciplinas.contains(disciplina)) {
            System.out.println("Já está matriculado nessa disciplina.");
            return;
        }

        disciplina.capacidadeMax();

        if (disciplina.isObrigatoria() && numObrigatorias >= 4) {
            System.out.println("Limite de disciplinas obrigatórias atingido.");
            return;
        }

        if (!disciplina.isObrigatoria() && numOptativas >= 2) {
            System.out.println("Limite de disciplinas optativas atingido.");
            return;
        }

        disciplina.addAluno(aluno);
        adicionarDisciplina(disciplina);
        System.out.println("Matriculado com sucesso em " + disciplina.getNome());
    }

    public void cancelarMatricula(Disciplina disciplina) {
        if (!semestre.isPeriodoMatriculaAberto()) {
            System.out.println("Cancelamento não permitido. Período de matrícula está fechado.");
            return;
        }

        if (!disciplinas.contains(disciplina)) {
            System.out.println("Não está matriculado nessa disciplina.");
            return;
        }

        disciplina.removeAluno(aluno);
        removerDisciplina(disciplina);
        System.out.println("Matrícula cancelada da disciplina " + disciplina.getNome());
    }
}