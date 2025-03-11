package application;

import entities.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Criação dos objetos iniciais
        Curso c1 = new Curso("Engenharia de Software", 80);
        Aluno a1 = new Aluno("João", "q8o5w@example.com", "1234");
        Secretaria sec1 = new Secretaria("Secretaria", "sec@gmail.com", "123");
        sec1.addCurso(c1);
        Professor p1 = new Professor("Joaquim", "q8o5w@example.com", "1234");

        c1.addDisciplina(new Disciplina(c1, "POO", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "BD", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "POO2", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "POO3", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "BD2", true, true, p1));
        c1.addDisciplina(new Disciplina(c1, "BD3", false, true, p1));

        Semestre s1 = new Semestre("2025.1");

        // Obtém ou cria a matrícula do aluno para o semestre
        Matricula m1 = a1.getMatriculaDoSemestre(s1);

        // Registra o sistema de cobrança como observador da matrícula
        Cobranca cobranca = new Cobranca();
        m1.addMatriculaListener(cobranca);

        // Abre o período de matrícula
        s1.abrirPeriodoMatricula();

        // Criação dos menus
        MenuAluno menuAluno = new MenuAluno(a1, c1, s1);
        MenuProfessor menuProfessor = new MenuProfessor(p1);
        MenuSecretaria menuSecretaria = new MenuSecretaria(sec1);

        int opcao;
        do {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1 - Acessar como Aluno");
            System.out.println("2 - Acessar como Professor");
            System.out.println("3 - Acessar como Secretaria");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida! Digite um número.");
                scanner.next();
            }
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    menuAluno.exibirMenu();
                    break;
                case 2:
                    menuProfessor.exibirMenu();
                    break;
                case 3:
                    menuSecretaria.exibirMenu();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}