package vinimrs.agendacontatos.services;

import vinimrs.agendacontatos.model.Contato;
import vinimrs.agendacontatos.model.Telefone;
import vinimrs.agendacontatos.repository.ContatoRepository;

import java.io.IOException;
import java.util.*;

public class ContatoServices {

  private final ContatoRepository contatoRepository = new ContatoRepository();
  private final TelefoneServices telefoneServices = new TelefoneServices();

  public void cadastrarContato() {
    System.out.print("\n>>>> Adicionar contato <<<<\n");
    System.out.println("---- Informações do contato ----");
    Scanner scanner = new Scanner(System.in);
    System.out.print("Digite o nome do contato: ");
    String nome = scanner.nextLine();
    while (nome.isEmpty()) {
      System.out.println("O nome não pode ser vazio.");
      System.out.print("Digite o nome do contato: ");
      nome = scanner.nextLine();
    }

    System.out.print("Digite o sobrenome do contato: ");
    String sobrenome = scanner.nextLine();
    while (sobrenome.isEmpty()) {
      System.out.println("O sobrenome não pode ser vazio.");
      System.out.print("Digite o sobrenome do contato: ");
      sobrenome = scanner.nextLine();
    }

    try {
      while (contatoRepository.nameAlrealdyInUse(nome, sobrenome)) {
        System.out.println("O nome já está em uso.");
        System.out.print("Digite o nome do contato: ");
        nome = scanner.nextLine();
        while (nome.isEmpty()) {
          System.out.println("O nome não pode ser vazio.");
          System.out.print("Digite o nome do contato: ");
          nome = scanner.nextLine();
        }
        System.out.print("Digite o sobrenome do contato: ");
        sobrenome = scanner.nextLine();
        while (sobrenome.isEmpty()) {
          System.out.println("O sobrenome não pode ser vazio.");
          System.out.print("Digite o sobrenome do contato: ");
          sobrenome = scanner.nextLine();
        }
      }

    } catch (Exception e) {
      System.out.println("Erro ao verificar se o nome já está em uso.");
    }

    Contato contato = new Contato(nome, sobrenome);
    try {
      contatoRepository.save(contato);
      System.out.println("Informações do contato salvas com sucesso!");
    } catch (Exception e) {
      System.out.println("Erro ao salvar contato");
    }

    int telefonesAdicionados = 0;
    String opcao = "";
    System.out.println("---- Telefone(s) ----");
    while (!Objects.equals(opcao, "2")) {
        if(telefoneServices.adicionarTelefone(contato))
          telefonesAdicionados++;

      if(telefonesAdicionados == 3)
        break;
      System.out.print("\nDeseja adicionar mais um telefone (Atual: " + telefonesAdicionados + ", Máximo: 3)? (1 - Sim | 2 - Não): ");
      opcao = scanner.nextLine();
    }

    System.out.println("Contato cadastrado com sucesso!");

  }

  public void editarContato() {
    listarContatos();
    System.out.print("\n>>>> Editar contato <<<<\n");
    Scanner scanner = new Scanner(System.in);
    System.out.print("Digite o ID do contato para editar: ");
    Long id = scanner.nextLong();
    try {
      Contato contato = contatoRepository.getById(id);
      if (contato == null) {
        System.out.println("Contato não encontrado");
      } else {
        System.out.println("* Se nao desejar mudar somente pressione Enter *");
        System.out.println("---- Informações do contato ----");
        scanner.nextLine();

        System.out.print("Digite o novo nome do contato (Atual: " + contato.getNome() + "): ");
        String nome = scanner.nextLine();
        if(nome.isEmpty())
          nome = contato.getNome();
        System.out.print("Digite o novo sobrenome do contato (Atual: " + contato.getSobreNome() + "): ");
        String sobrenome = scanner.nextLine();
        if(sobrenome.isEmpty())
          sobrenome = contato.getSobreNome();

        try {
          if(Objects.equals(contato.getNome(), nome) && Objects.equals(contato.getSobreNome(), sobrenome)) {
            System.out.println("Nenhuma alteração foi feita.");
          } else {
            while (contatoRepository.nameAlrealdyInUse(nome, sobrenome)) { // somente verifica se mudou o nome ou o sobrenome
              System.out.println("O nome já está em uso.");
              System.out.print("Digite o novo nome do contato (Atual: " + contato.getNome() + ") : ");
              nome = scanner.nextLine();
              if(nome.isEmpty())
                nome = contato.getNome();

              System.out.print("Digite o novo sobrenome do contato (Atual: " + contato.getSobreNome() + "): ");
              sobrenome = scanner.nextLine();
              if(sobrenome.isEmpty())
                sobrenome = contato.getSobreNome();
            }
            if(!Objects.equals(contato.getNome(), nome) || !Objects.equals(contato.getSobreNome(), sobrenome)) {
              contato.setNome(nome);
              contato.setSobreNome(sobrenome);
              contatoRepository.save(contato);
              System.out.println("Informações do contato editadas com sucesso!");
            }
          }
        } catch (Exception e) {
          System.out.println("Erro ao verificar se o nome já está em uso." + e.getMessage());
        }

        telefoneServices.gerenciarTelefones(contato);

      }
    } catch (Exception e) {
      System.out.println("Erro ao editar contato");
    }
  }

  public void listarContatos () {
    System.out.print("\n>>>> Contatos <<<<\n");
    System.out.println("ID | Nome");
    try {
      // Imprime na ordem crescente de ID
      contatoRepository.getAll().stream().sorted(Comparator.comparing(Contato::getId)).forEach(contato -> {
        System.out.println(contato.getId() + "  | " + contato.getNome());
      });
    } catch (Exception e) {
      System.out.println("Erro ao listar contatos" + e.getMessage());
    }
  }

  public void detalharContatos () {
    System.out.print("\n>>>> Sua Agenda de Contatos <<<<\n");
    try {
      String sair = "";
      Scanner scanner = new Scanner(System.in);
      do {
        contatoRepository.getAll().forEach(contato -> {
          System.out.print("ID: " + contato.getId() + "            ");
          System.out.print("Nome: " + contato.getNome() + " " + contato.getSobreNome() + "            ");
          System.out.print("Telefone(s): ");
          contato.getTelefones().forEach(telefone -> {
            System.out.print("(" + telefone.getDdd() + ") " + telefone.getNumero() + "  ");
          });
          System.out.print("\n");
        });
        System.out.print("\nDigite qualquer coisa para sair: ");
        sair = scanner.nextLine();
      } while (sair.isEmpty());

    } catch (Exception e) {
      System.out.println("Erro ao listar contatos" + e.getMessage());
    }
  }

  public void removerContato () {
    listarContatos();
    System.out.print("\n>>>> Remover contato <<<<\n");
    Scanner scanner = new Scanner(System.in);
    System.out.print("Digite o ID do contato para remover: ");
    Long id = scanner.nextLong();
    try {
      boolean removido = contatoRepository.removeById(id, false);
      if (!removido) {
        System.out.println("Contato não encontrado");
      } else {
        System.out.println("Contato removido com sucesso!");
      }
    } catch (Exception e) {
      System.out.println("Erro ao remover contato");
    }
  }

}
