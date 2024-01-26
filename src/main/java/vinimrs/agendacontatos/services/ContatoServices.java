package vinimrs.agendacontatos.services;

import vinimrs.agendacontatos.model.Contato;
import vinimrs.agendacontatos.model.Telefone;
import vinimrs.agendacontatos.repository.ContatoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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

    String opcao = "";
    List<Telefone> telefones = new ArrayList<>();
    System.out.println("---- Telefone(s) ----");
    while (!Objects.equals(opcao, "2")) {

      System.out.print("Digite o DDD do contato (XX): ");
      String ddd = scanner.nextLine();
      while (ddd.length() != 2) {
        System.out.println("O DDD deve estar no formato XX.");
        System.out.print("Digite o DDD do contato (XX): ");
        ddd = scanner.nextLine();
      }
      System.out.print("Digite o número do contato (9XXXXXXXX): ");
      String numero = scanner.nextLine();
      while (numero.charAt(0) != '9' || numero.length() != 9) {
        System.out.println("O número deve estar no formato 9XXXXXXXX.");
        System.out.print("Digite o número do contato (9XXXXXXXX): ");
        numero = scanner.nextLine();
      }

      if(telefoneServices.jaExisteTelefone(ddd, Long.parseLong(numero))) {
        System.out.println("O telefone já está cadastrado.");
        if(telefones.isEmpty())
          continue;
      } else {
        telefones.add(new Telefone(ddd, Long.parseLong(numero)));
        System.out.println("Telefone cadastrado com sucesso!");
      }

      System.out.print("\nDeseja adicionar mais um telefone? (1 - Sim | 2 - Não): ");
      opcao = scanner.nextLine();
    }


    Contato contato = new Contato(nome, sobrenome, telefones);
    try {
      contatoRepository.save(contato);
      System.out.println("Contato salvo com sucesso!");
    } catch (Exception e) {
      System.out.println("Erro ao salvar contato");
    }
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
        int mudou = 0;
        System.out.println("* Se nao desejar mudar pressione Enter *");
        System.out.println("---- Informações do contato ----");
        scanner.nextLine();

        System.out.print("Digite o novo nome do contato (Atual: " + contato.getNome() + ") : ");
        String nome = scanner.nextLine();
        if(nome.isEmpty())
          nome = contato.getNome();
        else
          mudou += 1;
        System.out.print("Digite o novo sobrenome do contato (Atual: " + contato.getSobreNome() + "): ");
        String sobrenome = scanner.nextLine();
        if(sobrenome.isEmpty())
          sobrenome = contato.getSobreNome();
        else
          mudou += 1;

        try {
          while (mudou == 2 && contatoRepository.nameAlrealdyInUse(nome, sobrenome)) {
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

        } catch (Exception e) {
          System.out.println("Erro ao verificar se o nome já está em uso." + e.getMessage());
        }

        String opcao = "";
        List<Telefone> telefones = contato.getTelefones();
        System.out.println("---- Telefone(s) ----");
        while (!Objects.equals(opcao, "s")) {
          for(int i = 0; i < telefones.size(); i++) {
            System.out.println(i+1 + "- (" + telefones.get(i).getDdd() + ") "+ telefones.get(i).getNumero());
          }

          mudou = 0;
          System.out.print("\nDigite o ID do telefone para editar ou 's' para sair: ");
          opcao = scanner.nextLine();

          if(Objects.equals(opcao, "s"))
            break;

          int idTelefone = Integer.parseInt(opcao) - 1;
          if(idTelefone < 0 || idTelefone >= telefones.size()) {
            System.out.println("Telefone não encontrado");
            continue;
          }

          System.out.print("Digite o novo DDD do contato (Atual: " + telefones.get(idTelefone).getDdd() + "): ");
          String ddd = scanner.nextLine();
          if(ddd.isEmpty())
            ddd = telefones.get(idTelefone).getDdd();
          else if(ddd.length() == 2 && !ddd.equals(telefones.get(idTelefone).getDdd()))
            mudou += 1;
          while (ddd.length() != 2) {
            System.out.println("O DDD deve estar no formato XX.");
            System.out.print("Digite o novo DDD do contato (Atual: " + telefones.get(idTelefone).getDdd() + "): ");
            ddd = scanner.nextLine();
            if(ddd.isEmpty())
              ddd = telefones.get(idTelefone).getDdd();
            else if(ddd.length() == 2 && !ddd.equals(telefones.get(idTelefone).getDdd()))
              mudou += 1;
          }

          System.out.print("Digite o novo número do contato (Atual: " + telefones.get(idTelefone).getNumero() + "): ");
          String numero = scanner.nextLine();
          if(numero.isEmpty())
            numero = String.valueOf(telefones.get(idTelefone).getNumero());
          else if(numero.charAt(0) == '9' && numero.length() == 9 && !numero.equals(String.valueOf(telefones.get(idTelefone).getNumero())))
            mudou += 1;
          while (numero.charAt(0) != '9' || numero.length() != 9) {
            System.out.println("O número deve estar no formato 9XXXXXXXX.");
            System.out.print("Digite o novo número do contato (Atual: " + telefones.get(idTelefone).getNumero() + "): ");
            numero = scanner.nextLine();
            if(numero.isEmpty())
              numero = String.valueOf(telefones.get(idTelefone).getNumero());
            else if(numero.charAt(0) == '9' && numero.length() == 9 && !numero.equals(String.valueOf(telefones.get(idTelefone).getNumero())))
              mudou += 1;
          }

          if(mudou > 0 && telefoneServices.jaExisteTelefone(ddd, Long.parseLong(numero))) {
            System.out.println("O telefone já está cadastrado.");
            if(telefones.isEmpty())
              continue;
          } else {
            telefones.set(idTelefone, new Telefone(contato.getId(), ddd, Long.parseLong(numero)));
            telefoneServices.atualizarTelefone(telefones.get(idTelefone));
            System.out.println("Telefone atualizado com sucesso!");
          }
        }

        contato.setNome(nome);
        contato.setSobreNome(sobrenome);
        contatoRepository.save(contato);
        System.out.println("Contato editado com sucesso!");

      }
    } catch (Exception e) {
      System.out.println("Erro ao editar contato");
    }
  }

      public void listarContatos () {
        System.out.print("\n>>>> Contatos <<<<\n");
        System.out.println("ID | Nome");
        try {
          contatoRepository.getAll().forEach(contato -> {
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
