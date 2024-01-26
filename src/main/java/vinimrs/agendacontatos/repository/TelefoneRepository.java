package vinimrs.agendacontatos.repository;

import vinimrs.agendacontatos.model.Telefone;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TelefoneRepository {

  private final String path = "src/main/java/vinimrs/agendacontatos/repository/telefone.txt";

  // Salva um telefone no arquivo telefone.txt, usando BufferedWriter
  public Telefone save(Telefone telefone) throws IOException {
    if(telefone.getId() == null) telefone.setId(getNextId());
    else if (getById(telefone.getId()) != null) { // se o telefone já existe, remove ele e adiciona o novo (atualizacao)
      removeById(telefone.getId());
    }

    BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
    writer.write(telefone.getId() + "," + telefone.getContatoId() + "," + telefone.getDdd() + "," + telefone.getNumero());
    writer.newLine();
    writer.close();

    return telefone;
  }

  // Pega o próximo id do telefone.txt
  public Long getNextId() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    long id = 0L;
    while ((line = reader.readLine()) != null) {
      String[] telefone = line.split(",");
      id = Math.max(Long.parseLong(telefone[0]), id);
    }
    reader.close();
    return id + 1;
  }

  public List<Telefone> getAll() throws IOException {
    List<Telefone> telefones = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println(line);
      String[] telefone = line.split(",");
      telefones.add(new Telefone(Long.parseLong(telefone[0]), Long.parseLong(telefone[1]), telefone[2], Long.parseLong(telefone[3])));
    }
    reader.close();
    return telefones;
  }

  public Telefone getById(Long id) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] telefone = line.split(",");
      if (Long.parseLong(telefone[0]) == id) {
        return new Telefone(Long.parseLong(telefone[0]), Long.parseLong(telefone[1]), telefone[2], Long.parseLong(telefone[3]));
      }
    }
    reader.close();
    return null;
  }

  public List<Telefone> getAllByContatoId(Long contatoId) throws IOException {
    List<Telefone> telefones = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] telefone = line.split(",");
      if (Long.parseLong(telefone[1]) == contatoId) {
        telefones.add(new Telefone(Long.parseLong(telefone[0]), Long.parseLong(telefone[1]), telefone[2], Long.parseLong(telefone[3])));
      }
    }
    reader.close();
    return telefones;
  }

  public boolean alreadyRegistered(Telefone telefone) throws IOException {
    boolean alreadyRegistered = false;
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while ((line = reader.readLine()) != null && !alreadyRegistered) {
      String[] registrado = line.split(",");
      if (registrado[2].equals(telefone.getDdd()) && Long.parseLong(registrado[3]) == telefone.getNumero()) {
        alreadyRegistered = true;
      }
    }
    reader.close();
    return alreadyRegistered;
  }

  public boolean removeByContatoId(Long contatoId) throws IOException {
    boolean removed = false;

    File file = new File(path);
    File tempFile = new File(path+".tmp");

    BufferedReader reader = new BufferedReader(new FileReader(file));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String line;
    while((line = reader.readLine()) != null){
      String[] telefone = line.split(",");
      if(Long.parseLong(telefone[1]) != contatoId){
        writer.write(line + System.lineSeparator());
      } else {
        removed = true;
      }
    }
    writer.close();
    reader.close();
    file.delete();
    tempFile.renameTo(file);

    return removed;
  }

  public boolean removeById(Long id) throws IOException {
    boolean removed = false;

    File file = new File(path);
    File tempFile = new File(path+".tmp");

    BufferedReader reader = new BufferedReader(new FileReader(file));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String line;
    while((line = reader.readLine()) != null){
      String[] telefone = line.split(",");
      if(Long.parseLong(telefone[0]) != id){
        writer.write(line + System.lineSeparator());
      } else {
        removed = true;
      }
    }
    writer.close();
    reader.close();
    file.delete();
    tempFile.renameTo(file);

    return removed;
  }
}
