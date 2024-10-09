package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Setter
@Getter
@Entity
@Table(name = "projects")
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 50)
  private String name;

  @NotBlank
  @Size(max = 200)
  private String description;

  @ManyToOne
  @JoinColumn(name = "leader_id")
  @JsonIgnore
  private User leader;

  @ManyToMany
  @JoinTable(name = "project_members", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  @JsonIgnore
  private Set<User> members = new HashSet<>(); // Miembros del proyecto

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private Set<LoanRequest> loanRequests = new HashSet<>(); // Solicitudes de préstamos asociadas al proyecto

  @Enumerated(EnumType.STRING)
  private ProjectStatus status;

  // Métodos para agregar y eliminar miembros del proyecto
  public void addMember(User user) {
    this.members.add(user);
    user.getMemberProjects().add(this);
  }

  public void removeMember(User user) {
    this.members.remove(user);
    user.getMemberProjects().remove(this);
  }
  public String getLeaderName() {
    return leader != null ? leader.getEmail() : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Project project = (Project) o;
    return id != null && id.equals(project.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}