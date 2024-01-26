package vinimrs.agendacontatos.model;

import java.util.List;

public class Contato {
  private Long id;
  private String nome;
  private String sobreNome;
  private List<Telefone> telefones;

  public Contato(String nome, String sobreNome) {
    this.nome = nome;
    this.sobreNome = sobreNome;
  }

  public Contato(String nome, String sobreNome, List<Telefone> telefones) {
    this.nome = nome;
    this.sobreNome = sobreNome;
    this.telefones = telefones;
  }

  public Contato(Long id, String nome, String sobreNome) {
    this.id = id;
    this.nome = nome;
    this.sobreNome = sobreNome;
  }

  public Contato(Long id, String nome, String sobreNome, List<Telefone> telefones) {
    this.id = id;
    this.nome = nome;
    this.sobreNome = sobreNome;
    this.telefones = telefones;
  }

  public void addTelefone(Telefone telefone) {
    this.telefones.add(telefone);
  }

  public void removeTelefone(Telefone telefone) {
    this.telefones.remove(telefone);
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getSobreNome() {
    return sobreNome;
  }

  public List<Telefone> getTelefones() {
    return telefones;
  }

  public void setId(Long nextId) {
    this.id = nextId;
  }

  public void setTelefones(List<Telefone> telefones) {
    this.telefones = telefones;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setSobreNome(String sobreNome) {
    this.sobreNome = sobreNome;
  }
}