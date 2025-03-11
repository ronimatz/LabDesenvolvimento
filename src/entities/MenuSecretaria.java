package entities;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MenuSecretaria {
    private Scanner scanner;
    private Secretaria secretaria;

    public MenuSecretaria(Secretaria secretaria) {
        this.scanner = new Scanner(System.in);
        this.secretaria = secretaria;
    }

    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n--- Menu da Secretaria ---");
            System.out.println("1 - Gerar currículo do curso (CSV)");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            
            opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    gerarCurriculoCSV();
                    break;
                case 0:
                    System.out.println("Saindo do menu...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void gerarCurriculoCSV() {
        String nomeArquivo = "curriculo_cursos.csv";
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.append("Curso,Disciplina,Professor\n");
            for (Curso curso : secretaria.getCursos()) {
                for (Disciplina disciplina : curso.getDisciplinas()) {
                    writer.append(curso.getNome()).append(",")
                          .append(disciplina.getNome()).append(",")
                          .append(disciplina.getProfessor().getNome()).append("\n");
                }
            }
            System.out.println("Currículo gerado com sucesso em " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao gerar o currículo: " + e.getMessage());
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
