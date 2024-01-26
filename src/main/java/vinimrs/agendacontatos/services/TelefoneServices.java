package vinimrs.agendacontatos.services;

import vinimrs.agendacontatos.model.Contato;
import vinimrs.agendacontatos.model.Telefone;
import vinimrs.agendacontatos.repository.TelefoneRepository;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class TelefoneServices {

  private final TelefoneRepository telefoneRepository = new TelefoneRepository();

  public boolean jaExisteTelefone(String ddd, Long numero) {
    try {
      return telefoneRepository.alreadyRegistered(new Telefone(ddd, numero));
    } catch (Exception e) {
      System.out.println("Erro ao verificar se o telefone já está cadastrado.");
      return true;
    }
  }

  public void atualizarTelefone(Telefone telefone) {
    try {
      telefoneRepository.save(telefone);
    } catch (Exception e) {
      System.out.println("Erro ao atualizar o telefone.");
    }
  }

  public boolean adicionarTelefone(Contato contato) {
    if(contato.getTelefones() != null && contato.getTelefones().size() == 3) {
      System.out.println("O contato já possui 3 telefones cadastrados.");
      return false;
    }

    Scanner scanner = new Scanner(System.in);
    System.out.println("---- Adicionar Telefone ----");
    System.out.print("Digite o DDD do contato (XX): ");
    String ddd = scanner.nextLine();
    if(ddd.isEmpty())
      ddd = "00";
    while (ddd.length() != 2) {
      System.out.println("O DDD deve estar no formato XX.");
      System.out.print("Digite o DDD do contato (XX): ");
      ddd = scanner.nextLine();
      if(ddd.isEmpty())
        ddd = "00";
    }
    System.out.print("Digite o número do contato (9XXXXXXXX): ");
    String numero = scanner.nextLine();
    if(numero.isEmpty())
      numero = "000000000";
    while (numero.charAt(0) != '9' || numero.length() != 9) {
      System.out.println("O número deve estar no formato 9XXXXXXXX.");
      System.out.print("Digite o número do contato (9XXXXXXXX): ");
      numero = scanner.nextLine();
      if(numero.isEmpty())
        numero = "000000000";
    }

    if(jaExisteTelefone(ddd, Long.parseLong(numero))) {
      System.out.println("O telefone já está cadastrado.");
      return false;
    } else {
      Telefone novoTelefone = new Telefone(contato.getId(), ddd, Long.parseLong(numero));
      contato.addTelefone(novoTelefone);
      atualizarTelefone(novoTelefone);
      System.out.println("Telefone cadastrado com sucesso!");
      return true;
    }

  }

  public void removerTelefone(Contato contato) {
    if(contato.getTelefones().size() == 1) {
      System.out.println("O contato não deve ficar sem número.");
      return;
    }
    Scanner scanner = new Scanner(System.in);
    String opcao = "";
    List<Telefone> telefones = contato.getTelefones();
    int opcaoSair = telefones.size() + 1;
    System.out.println("---- Remover Telefone ----");
    if(telefones.isEmpty()) {
      System.out.println("Nenhum telefone cadastrado.");
      return;
    }
    for(int i = 0; i < telefones.size(); i++) {
      System.out.println(i+1 + "- (" + telefones.get(i).getDdd() + ") "+ telefones.get(i).getNumero());
      if(i == telefones.size() - 1)
        System.out.println(i+2 + "- Sair");
    }

    System.out.print("\nDigite o ID do telefone para remover ou " + opcaoSair + " para sair: ");
    opcao = scanner.nextLine();

    if(Objects.equals(opcao, String.valueOf(opcaoSair)))
      return;

    int idTelefone = Integer.parseInt(opcao) - 1;
    if(idTelefone < 0 || idTelefone >= telefones.size()) {
      System.out.println("Telefone não encontrado");
      return;
    }
    try {
      telefoneRepository.removeById(telefones.get(idTelefone).getId());
      contato.removeTelefone(telefones.get(idTelefone));
      System.out.println("Telefone removido com sucesso!");
    } catch (Exception e) {
      System.out.println("Erro ao remover o telefone." + e.getMessage());
    }

  }

  public void editarTelefone(Contato contato) {
    Scanner scanner = new Scanner(System.in);
    String opcao = "";
    int opcaoSair = contato.getTelefones().size() + 1;
    List<Telefone> telefones = contato.getTelefones();
    for (int i = 0; i < telefones.size(); i++) {
      System.out.println(i + 1 + "- (" + telefones.get(i).getDdd() + ") " + telefones.get(i).getNumero());
      if (i == telefones.size() - 1)
        System.out.println(i + 2 + "- Sair");
    }

    System.out.print("\nDigite o ID do telefone para editar ou " + opcaoSair + " para sair: ");
    opcao = scanner.nextLine();

    if (Objects.equals(opcao, String.valueOf(opcaoSair)))
      return;

    int idTelefone = Integer.parseInt(opcao) - 1;
    if (idTelefone < 0 || idTelefone >= telefones.size()) {
      System.out.println("Telefone não encontrado");
      return;
    }

    System.out.print("Digite o novo DDD do contato (Atual: " + telefones.get(idTelefone).getDdd() + "): ");
    String ddd = scanner.nextLine();
    if (ddd.isEmpty())
      ddd = telefones.get(idTelefone).getDdd();

    while (ddd.length() != 2) {
      System.out.println("O DDD deve estar no formato XX.");
      System.out.print("Digite o novo DDD do contato (Atual: " + telefones.get(idTelefone).getDdd() + "): ");
      ddd = scanner.nextLine();
      if (ddd.isEmpty())
        ddd = telefones.get(idTelefone).getDdd();
    }

    System.out.print("Digite o novo número do contato (Atual: " + telefones.get(idTelefone).getNumero() + "): ");
    String numero = scanner.nextLine();
    if (numero.isEmpty())
      numero = String.valueOf(telefones.get(idTelefone).getNumero());

    while (numero.charAt(0) != '9' || numero.length() != 9) {
      System.out.println("O número deve estar no formato 9XXXXXXXX.");
      System.out.print("Digite o novo número do contato (Atual: " + telefones.get(idTelefone).getNumero() + "): ");
      numero = scanner.nextLine();
      if (numero.isEmpty())
        numero = String.valueOf(telefones.get(idTelefone).getNumero());
    }

    if (Objects.equals(telefones.get(idTelefone).getDdd(), ddd) && Objects.equals(telefones.get(idTelefone).getNumero(), Long.parseLong(numero))) {
      System.out.println("Nenhuma alteração foi feita.");
      return;
    }

    if (jaExisteTelefone(ddd, Long.parseLong(numero))) {
      System.out.println("O telefone já está cadastrado.");
    } else {
      telefones.set(idTelefone, new Telefone(telefones.get(idTelefone).getId(), contato.getId(), ddd, Long.parseLong(numero)));
      atualizarTelefone(telefones.get(idTelefone));
      System.out.println("Telefone atualizado com sucesso!");
    }
  }

  public void listarTelefones(Contato contato) {
    List<Telefone> telefones = contato.getTelefones();
    System.out.println("\n>>>> Telefones de " + contato.getNome() + " <<<<");
    if(telefones.isEmpty()) {
      System.out.println("Nenhum telefone cadastrado.");
      return;
    }
    for(int i = 0; i < telefones.size(); i++) {
      System.out.println(i+1 + "- (" + telefones.get(i).getDdd() + ") "+ telefones.get(i).getNumero());
    }
  }

  public void gerenciarTelefones(Contato contato) {
    Scanner scanner = new Scanner(System.in);
    int opcao = 0;
    System.out.println("---- Editar Telefone(s) ----");

    while (opcao != 4) {
      listarTelefones(contato);

      System.out.print( "\n>>>> Menu <<<<\n" +
              "1 - Adicionar telefone\n" +
              "2 - Remover telefone\n" +
              "3 - Editar telefone\n" +
              "4 - Sair\n" +
              "\n----------------\n" +
              "Opção: ");
      opcao = scanner.nextInt();
      switch (opcao) {
        case 1:
          adicionarTelefone(contato);
          break;
        case 2:
          removerTelefone(contato);
          break;
        case 3:
          editarTelefone(contato);
          break;
        case 4:
          System.out.println("Saindo...");
          break;
        default:
          System.out.println("Opção inválida");
          break;

      }
    }
  }

}
