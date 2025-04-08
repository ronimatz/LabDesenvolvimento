package entities;

public class Cobranca implements MatriculaListener {

    @Override
    public void onMatriculaUpdated(Matricula matricula) {
        System.out.println("Cobranca: Gerando cobrança para o aluno " + matricula.getAluno().getNome() +
                           " no semestre " + matricula.getSemestre().getIdentificador());
        for (Disciplina d : matricula.getDisciplinas()) {
            System.out.println("Cobranca: Cobrando disciplina " + d.getNome());
        }
    }
}
