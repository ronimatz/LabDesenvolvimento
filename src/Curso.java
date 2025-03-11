public class Curso {
    private String nome;
    private int numCreditos;

    public Curso() {
    }

    public Curso(String nome, int numCreditos) {
        this.nome = nome;
        this.numCreditos = numCreditos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumCreditos() {
        return numCreditos;
    }

    public void setNumCreditos(int numCreditos) {
        this.numCreditos = numCreditos;
    }
}
