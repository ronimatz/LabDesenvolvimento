package entities;
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

    public boolean fecharPeriodoMatricula(Curso curso) {
        this.periodoMatriculaAberto = false;
        curso.verificarTodasDisciplinas();

        return periodoMatriculaAberto;
    }


    @Override
    public String toString() {
        return identificador;
    }
}