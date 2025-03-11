package entities;
import java.util.Scanner;

public class MenuProfessor {
    private Scanner scanner;
    private Professor professor;

    public MenuProfessor(Professor professor) {
        this.scanner = new Scanner(System.in);
        this.professor = professor;
    }

    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n--- Menu do Professor ---");
            System.out.println("1 - Listar alunos matriculados");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    listarAlunosMatriculados();
                    break;
                case 0:
                    System.out.println("Saindo do menu...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void listarAlunosMatriculados() {
        System.out.println("\n--- Alunos Matriculados nas Disciplinas ---");
        for (Disciplina disciplina : professor.getDisciplinas()) {
            System.out.println("Disciplina: " + disciplina.getNome());
            if (disciplina.getAlunos().isEmpty()) {
                System.out.println("Nenhum aluno matriculado.");
            } else {
                for (Aluno aluno : disciplina.getAlunos()) {
                    System.out.println("- " + aluno.getNome() + " (" + aluno.getEmail() + ")");
                }
            }
            System.out.println();
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
