package vinimrs.agendacontatos.repository;

import vinimrs.agendacontatos.model.Contato;
import vinimrs.agendacontatos.model.Telefone;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoRepository {
  private final String path = "src/main/java/vinimrs/agendacontatos/repository/contato.txt";
  private final TelefoneRepository telefoneRepository = new TelefoneRepository();

  public Contato save(Contato contato) throws IOException {
    // pega o id do contato e incrementa
    if(contato.getId() == null) contato.setId(getNextId());
    else if (getById(contato.getId()) != null) { // se o contato já existe, remove ele e adiciona o novo (atualizacao)
      removeById(contato.getId(), true);
    }

    BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
    writer.write(contato.getId() + "," + contato.getNome() + "," + contato.getSobreNome());
    writer.newLine();
    writer.close();

    if(contato.getTelefones() == null) return contato;

    for(Telefone telefone : contato.getTelefones()) {
      telefone.setContatoId(contato.getId());
      telefoneRepository.save(telefone);
    }

    return contato;
  }

  public Long getNextId() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    long id = 0L;
    while ((line = reader.readLine()) != null) {
      String[] contato = line.split(",");
      id = Math.max(Long.parseLong(contato[0]), id);
    }
    reader.close();
    return id + 1;
  }

  public List<Contato> getAll() throws IOException {
    List<Contato> contatos = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] contato = line.split(",");

      Contato novoContato = new Contato(Long.parseLong(contato[0]), contato[1], contato[2]);
      novoContato.setTelefones(telefoneRepository.getAllByContatoId(novoContato.getId()));
      contatos.add(novoContato);
    }
    reader.close();
    return contatos;
  }

  public Contato getById(Long id) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] contato = line.split(",");
      if (Long.parseLong(contato[0]) == id) {
        Contato contato1 = new Contato(Long.parseLong(contato[0]), contato[1], contato[2]);
        contato1.setTelefones(telefoneRepository.getAllByContatoId(contato1.getId()));
        return contato1;
      }
    }
    reader.close();
    return null;
  }

  public boolean removeByName(String nome) throws IOException {
    boolean removed = false;

    File file = new File(path);
    File tempFile = new File(path+".tmp");

    BufferedReader reader = new BufferedReader(new FileReader(file));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String line;
    while((line = reader.readLine()) != null){
      String[] contato = line.split(",");
      if(!contato[1].equals(nome)){
        writer.write(line + System.lineSeparator());
      } else {
        removed = true;
      }
    }

    reader.close();
    writer.close();

    if(!file.delete()) {
      System.out.println("Não foi possível deletar o arquivo");
      return false;
    };

    if(!tempFile.renameTo(file)) {
      System.out.println("Não foi possível renomear o arquivo");
    };

    return removed;
  }

  public boolean removeById(Long id, boolean update) throws IOException {
    boolean removed = false;

    File file = new File(path);
    File tempFile = new File(path+".tmp");

    BufferedReader reader = new BufferedReader(new FileReader(file));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String line;
    while((line = reader.readLine()) != null){
      String[] contato = line.split(",");
      if(!contato[0].equals(id.toString())){
        writer.write(line + System.lineSeparator());
      } else {
        removed = true;
      }
    }

    reader.close();
    writer.close();

    if(!file.delete()) {
      System.out.println("Não foi possível deletar o arquivo");
      return false;
    };

    if(!tempFile.renameTo(file)) {
      System.out.println("Não foi possível renomear o arquivo");
    };

    if(removed && !update) return telefoneRepository.removeByContatoId(id);

    return removed;
  }

  public boolean nameAlrealdyInUse(String nome, String sobreNome) throws IOException {
    boolean alreadyRegistered = false;
    BufferedReader reader = new BufferedReader(new FileReader(path));

    String line;
    while((line = reader.readLine()) != null){
      String[] contato = line.split(",");
      if(contato[1].equals(nome) && contato[2].equals(sobreNome)){
        alreadyRegistered = true;
      }
    }

    reader.close();
    return alreadyRegistered;
  }


}
