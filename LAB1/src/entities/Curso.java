package entities;
import java.util.ArrayList;
import java.util.List;

public class Curso {
    private String nome;
    private List<Disciplina> disciplinas = new ArrayList<>();
    private Integer creditos;


    public Curso() {
    }


    public Curso(String nome, Integer creditos) {
        this.nome = nome;
        this.creditos = creditos;
    }


    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public void addDisciplina(Disciplina d) {
        this.disciplinas.add(d);
    }

    public Integer getCreditos() {
        return this.creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public void verificarTodasDisciplinas() {
        for (Disciplina d : disciplinas) {
            d.verficaAlunosMatriculados();
        }
    }



}