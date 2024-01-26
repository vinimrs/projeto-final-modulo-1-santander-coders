package vinimrs.agendacontatos.model;

import java.util.UUID;

public class Telefone {
  private Long id;
  private String ddd;
  private Long numero;
  private Long contatoId; // adicionado para relacionar o telefone com o contato

  public Telefone(Long contactId, String ddd, Long numero) {
    this.ddd = ddd;
    this.numero = numero;
    this.contatoId = contactId;
  }

  public Telefone(String ddd, Long numero) {
    this.ddd = ddd;
    this.numero = numero;
  }

  public Telefone(Long id, Long contactId, String ddd, Long numero) {
    this.id = id;
    this.ddd = ddd;
    this.numero = numero;
    this.contatoId = contactId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getDdd() {
    return ddd;
  }

  public Long getNumero() {
    return numero;
  }

  public Long getContatoId() {
    return contatoId;
  }

  public void setContatoId(Long contatoId) {
    this.contatoId = contatoId;
  }
}