package com.eaisign.payload.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String nom;
  private String prenom;
  private String email;
  private String num_telephone;
  private String piece_justicatif;
  private List<String> roles;
  private String refreshToken;

  public JwtResponse(String accessToken, Long id, String nom,String prenom, String email,String num_telephone,String piece_justicatif, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.nom = nom;
    this.prenom=prenom;
    this.email = email;
    this.roles = roles;
    this.num_telephone=num_telephone;
    this.piece_justicatif=piece_justicatif;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public List<String> getRoles() {
    return roles;
  }
}
