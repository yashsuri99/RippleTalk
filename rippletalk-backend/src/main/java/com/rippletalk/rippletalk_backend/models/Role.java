package com.rippletalk.rippletalk_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "role_id")
  private Integer role_id;

  private String authority;

  public Role() {
    super();
  }

  public Role(Integer role_id, String authority) {
    this.role_id = role_id;
    this.authority = authority;
  }

  public Integer getRole_id() {
    return role_id;
  }

  public void setRole_id(Integer role_id) {
    this.role_id = role_id;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  @Override
  public String toString() {
    return "Role [role_id=" + role_id + ", authority=" + authority + "]";
  }

}
