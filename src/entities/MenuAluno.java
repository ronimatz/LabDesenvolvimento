package entities;
import java.util.Scanner;

public class MenuAluno {

    private Scanner scanner;
    private Aluno aluno;
    private Curso curso;
    private Semestre semestre;
    private Matricula matricula;

    public MenuAluno(Aluno aluno, Curso curso, Semestre semestre) {
        this.scanner = new Scanner(System.in);
        this.aluno = aluno;
        this.curso = curso;
        this.semestre = semestre;
        this.matricula = aluno.getMatriculaDoSemestre(semestre);
    }

    public void exibirMenu() {
        int opcao;
        do {
            exibirOpcoes();
            opcao = lerInteiro();

            processarOpcao(opcao);

        } while (opcao != 0);
    }

    private void exibirOpcoes() {
        System.out.println("\n--- Menu de Matrícula ---");
        if (semestre.isPeriodoMatriculaAberto()) {
            System.out.println("1 - Matricular-se em disciplina");
            System.out.println("2 - Cancelar matrícula em disciplina");
        }
        System.out.println("3 - Visualizar disciplinas matriculadas");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                if (semestre.isPeriodoMatriculaAberto()) {
                    matricularEmDisciplina();
                } else {
                    System.out.println("O período de matrícula está fechado.");
                }
                break;
            case 2:
                if (semestre.isPeriodoMatriculaAberto()) {
                    cancelarMatricula();
                } else {
                    System.out.println("O período de matrícula está fechado.");
                }
                break;
            case 3:
                visualizarDisciplinasMatriculadas();
                break;
            case 0:
                System.out.println("Saindo do menu...");
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
        }
    }

    private void listarDisciplinasDisponiveis() {
        System.out.println("\n--- Disciplinas Disponíveis ---");
        int index = 1;
        for (Disciplina disciplina : curso.getDisciplinas()) {
            System.out.println(index + " - " + disciplina.getNome());
            index++;
        }
    }

    private void matricularEmDisciplina() {
        listarDisciplinasDisponiveis();
        System.out.print("Escolha o número da disciplina para se matricular: ");
        int escolha = lerInteiro();

        if (escolha > 0 && escolha <= curso.getDisciplinas().size()) {
            Disciplina disciplinaEscolhida = curso.getDisciplinas().get(escolha - 1);
            matricula.matricularEmDisciplina(disciplinaEscolhida);
        } else {
            System.out.println("Opção inválida!");
        }
    }

    private void cancelarMatricula() {
        System.out.println("\n--- Disciplinas Matriculadas ---");
        int index = 1;
        for (Disciplina disciplina : matricula.getDisciplinas()) {
            System.out.println(index + " - " + disciplina.getNome());
            index++;
        }

        System.out.print("Escolha o número da disciplina para cancelar a matrícula: ");
        int escolha = lerInteiro();

        if (escolha > 0 && escolha <= matricula.getDisciplinas().size()) {
            Disciplina disciplinaEscolhida = matricula.getDisciplinas().get(escolha - 1);
            matricula.cancelarMatricula(disciplinaEscolhida);
        } else {
            System.out.println("Opção inválida!");
        }
    }

    private void visualizarDisciplinasMatriculadas() {
        System.out.println("\n--- Disciplinas Matriculadas ---");
        for (Disciplina disciplina : matricula.getDisciplinas()) {
            System.out.println("- " + disciplina.getNome());
        }
    }

    private int lerInteiro() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida! Digite um número.");
            scanner.next(); // Limpa entrada inválida
        }
        return scanner.nextInt();
    }
}
