package vinimrs.agendacontatos;

import vinimrs.agendacontatos.services.ContatoServices;

import java.util.Scanner;

public class App {

    private static final ContatoServices contatoServices = new ContatoServices();

    public static void main(String[] args) {
        System.out.print("##################\n" +
                        "##### AGENDA #####\n" +
                        "##################");

        Scanner scanner = new Scanner(System.in);
        int option = 0;
        while (option != 5) {
            contatoServices.listarContatos();

            System.out.print("\n>>>> Menu <<<<\n" +
                "1 - Adicionar contato\n" +
                "2 - Remover contato\n" +
                "3 - Editar contato\n" +
                "4 - Detalhar contatos\n" +
                "5 - Sair\n" +
                "\n----------------\n" +
                "Opção: ");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    contatoServices.cadastrarContato();
                    break;
                case 2:
                    contatoServices.removerContato();
                    break;
                case 3:
                    contatoServices.editarContato();
                    break;
                case 4:
                    contatoServices.detalharContatos();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
                    break;
            }
        }
    }
}
