import java.util.Date;

public class Matricula {
    private Date periodoRealizacao;

    public Matricula() {
    }

    public Matricula(Date periodoRealizacao) {
        this.periodoRealizacao = periodoRealizacao;
    }

    public Date getPeriodoRealizacao() {
        return periodoRealizacao;
    }

    public void setPeriodoRealizacao(Date periodoRealizacao) {
        this.periodoRealizacao = periodoRealizacao;
    }
}
