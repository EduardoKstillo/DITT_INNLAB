package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.Objets.InviteRequest;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectDTO;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectWithMembersDTO;
import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.payload.request.LoginRequest;
import com.bezkoder.spring.security.postgresql.payload.response.JwtResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.InvitationRepository;
import com.bezkoder.spring.security.postgresql.repository.LoanRequestRepository;
import com.bezkoder.spring.security.postgresql.repository.ProjectRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;
import com.bezkoder.spring.security.postgresql.services.EmailService;
import com.bezkoder.spring.security.postgresql.services.ProjectService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = {"http://localhost:8100/", "http://vps-zap907917-1.zap-srv.com", "http://proyectos-vri.unsa.edu.pe:9090"})
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        Project project = projectService.createProject(projectDTO);
        return ResponseEntity.ok(new MessageResponse("Proyecto creado exitosamente"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        Project updatedProject = projectService.updateProject(id, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new MessageResponse("Proyecto eliminado exitosamente"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getProjectsByUser(@PathVariable Long userId) {
        List<Project> projects = projectService.getProjectsByUser(userId);
        return ResponseEntity.ok(projects);
    }

    // Gets projects from a leader
    @GetMapping("/leader/{leaderId}")
    public ResponseEntity<List<Project>> getProjectsByLeaderId(@PathVariable Long leaderId) {
        List<Project> projects = projectService.findByLeaderId(leaderId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<?> addMemberToProject(@PathVariable Long projectId, @RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        projectService.addMemberToProject(projectId, userId);
        return ResponseEntity.ok(new MessageResponse("Miembro añadido exitosamente"));
    }

    @DeleteMapping("/{projectId}/members")
    public ResponseEntity<?> removeMemberFromProject(@PathVariable Long projectId, @RequestParam Long userId) {
        projectService.removeMemberFromProject(projectId, userId);
        return ResponseEntity.ok(new MessageResponse("Miembro eliminado exitosamente"));
    }
    @GetMapping("/{projectId}/loan-requests")
    public ResponseEntity<List<LoanRequest>> getLoanRequestsByProject(@PathVariable Long projectId) {
        List<LoanRequest> loanRequests = projectService.getLoanRequestsByProject(projectId);
        return ResponseEntity.ok(loanRequests);
    }

    @GetMapping("/{projectId}/members")
    public Set<User> getProjectMembers(@PathVariable Long projectId) {
        System.out.println("id del proyecto : " + projectId);
        return projectService.getMembersByProjectId(projectId);
    }

    @GetMapping("/{projectId}/with-members")
    public ProjectWithMembersDTO getProjectWithMembers(@PathVariable Long projectId) {
        return projectService.getProjectWithMembers(projectId);
    }
}
