package com.eaisign.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String nom;
  @NotBlank
  @Size(min = 3, max = 20)
  private String prenom;

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  @NotBlank
  @Size(min = 3, max = 20)
  private String num_telephone;
  @NotBlank
  @Size(min = 3, max = 20)
  private String piece_justicatif;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;
@NotBlank
private String password;
  private Set<String> role;



  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getNum_telephone() {
    return num_telephone;
  }

  public void setNum_telephone(String num_telephone) {
    this.num_telephone = num_telephone;
  }

  public String getPiece_justicatif() {
    return piece_justicatif;
  }

  public void setPiece_justicatif(String piece_justicatif) {
    this.piece_justicatif = piece_justicatif;
  }

  public Set<String> getRole() {
    return this.role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }
}
