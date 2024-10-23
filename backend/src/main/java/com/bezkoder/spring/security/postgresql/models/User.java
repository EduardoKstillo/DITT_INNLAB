package com.bezkoder.spring.security.postgresql.models;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table( name = "users", 
        uniqueConstraints = { 
          @UniqueConstraint(columnNames = "dni"),
          @UniqueConstraint(columnNames = "email") 
        })
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @NotBlank
  @Size(max = 50)
  private String firstName;

  @NotBlank
  @Size(max = 50)
  private String lastName;

  @NotBlank
  @Size(max = 50)
  private String university;

  @Size(max = 20)
  private String phone;

  @NotBlank
  @Size(max = 20)
  private String dni;

  @Size(max = 255)
  private String photo;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "leader")
  @JsonIgnore
  private Set<Project> leaderProjects = new HashSet<>(); // Proyectos donde el usuario es líder

  @ManyToMany(mappedBy = "members")
  @JsonIgnore
  private Set<Project> memberProjects = new HashSet<>(); // Proyectos donde el usuario es miembro

  public User(Long id) {
    this.id = id;
  }

  public void addLeaderProject(Project project) {
    this.leaderProjects.add(project);
    project.setLeader(this);
  }

  public void removeLeaderProject(Project project) {
    this.leaderProjects.remove(project);
    if (project.getLeader() != null && project.getLeader().equals(this)) {
      project.setLeader(null);
    }
  }

  public void addMemberProject(Project project) {
    this.memberProjects.add(project);
    project.getMembers().add(this);
  }

  public void removeMemberProject(Project project) {
    this.memberProjects.remove(project);
    project.getMembers().remove(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id != null && id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
