package entities;
import java.util.ArrayList;
import java.util.List;

public class Disciplina {
    private String nome;
    private boolean obrigatoria;
    private boolean ativa;
    private Professor professor;
    private List<Aluno> alunos = new ArrayList<>();
    private Curso curso;

    private static final int MAX_ALUNOS = 60;
    private static final int MIN_ALUNOS = 3;


    public Disciplina() {
    }


    public Disciplina(Curso curso,String nome, boolean obrigatoria, boolean ativa, Professor professor) {
        this.nome = nome;
        this.obrigatoria = obrigatoria;
        this.ativa = ativa;
        this.professor = professor;
        this.curso = curso;

        professor.addDisciplina(this);
    }


    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isObrigatoria() {
        return this.obrigatoria;
    }

    public boolean getObrigatoria() {
        return this.obrigatoria;
    }

    public void setObrigatoria(boolean obrigatoria) {
        this.obrigatoria = obrigatoria;
    }

    public boolean isAtiva() {
        return this.ativa;
    }

    public boolean getAtiva() {
        return this.ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public Professor getProfessor() {
        return this.professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
        professor.addDisciplina(this);
    }

    public List<Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }
    
    public void addAluno(Aluno a) {
        this.alunos.add(a);
    }

    public void removeAluno(Aluno a) {
        this.alunos.remove(a);
    }

    public Curso getCurso() {
        return this.curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void verficaAlunosMatriculados() {
        if (alunos.size() <= MIN_ALUNOS) {
            this.ativa = false;
        }

    }

    public void capacidadeMax() {
        if  (alunos.size() >= MAX_ALUNOS) {
            this.ativa = false;
            System.out.println("❌ A disciplina atingiu o número máximo de alunos.");
        }
       
    }

    

}