package vinimrs.agendacontatos.services;

import vinimrs.agendacontatos.model.Telefone;
import vinimrs.agendacontatos.repository.TelefoneRepository;

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

  public boolean atualizarTelefone(Telefone telefone) {
    try {
      telefoneRepository.save(telefone);
      return true;
    } catch (Exception e) {
      System.out.println("Erro ao atualizar o telefone.");
      return false;
    }
  }
}
