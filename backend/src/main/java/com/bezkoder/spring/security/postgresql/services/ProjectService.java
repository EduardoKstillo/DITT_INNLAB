package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.Objets.InviteRequest;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectDTO;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectMemberDTO;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectWithMembersDTO;
import com.bezkoder.spring.security.postgresql.exception.ResourceNotFoundException;
import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.repository.InvitationRepository;
import com.bezkoder.spring.security.postgresql.repository.LoanRequestRepository;
import com.bezkoder.spring.security.postgresql.repository.ProjectRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRequestRepository loanRequestRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Project createProject(ProjectDTO projectDTO) {
        Long leaderId = projectDTO.getLeaderId();
        Optional<User> leader = userRepository.findById(leaderId);

        if (leader.isPresent()) {
            List<Project> activeProjects = projectRepository.findActiveProjectsByLeaderId(leaderId);
            if (!activeProjects.isEmpty()) {
                throw new IllegalArgumentException("Error: Ya tienes un proyecto activo y no puedes crear otro.");
            }

            Project project = new Project();
            project.setName(projectDTO.getName());
            project.setDescription(projectDTO.getDescription());
            project.setLeader(leader.get());
            project.setStatus(ProjectStatus.ACTIVO);
            project = projectRepository.save(project);

            return project;
        } else {
            throw new IllegalArgumentException("Error: No se pudo encontrar el usuario.");
        }
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con id: " + id));
    }

    @Transactional
    public Project updateProject(Long id, ProjectDTO projectDTO) {
        Project project = getProjectById(id);
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());

        if (projectDTO.getLeaderId() != null) {
            User leader = userRepository.findById(projectDTO.getLeaderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + projectDTO.getLeaderId()));
            project.setLeader(leader);
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }

    public List<Project> getProjectsByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Project> leaderProjects = projectRepository.findByLeaderId(userId);
            List<Project> memberProjects = projectRepository.findByMemberId(userId);
            return Stream.concat(leaderProjects.stream(), memberProjects.stream())
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + userId);
        }
    }

    @Transactional
    public void addMemberToProject(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        project.addMember(user);
        projectRepository.save(project);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        project.removeMember(user);
        projectRepository.save(project);
    }

    public List<Project> findByLeaderId(Long leaderId) {
        return projectRepository.findByLeaderId(leaderId);
    }

    public List<LoanRequest> getLoanRequestsByProject(Long projectId) {
        Project project = getProjectById(projectId);
        return loanRequestRepository.findByProjectId(projectId);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Set<User> getMembersByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));
        return project.getMembers();
    }

    public ProjectWithMembersDTO getProjectWithMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + projectId));

        List<ProjectMemberDTO> memberDTOs = project.getMembers().stream()
                .map(user -> {
                    ProjectMemberDTO dto = new ProjectMemberDTO();
                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    return dto;
                })
                .collect(Collectors.toList());

        ProjectWithMembersDTO projectDTO = new ProjectWithMembersDTO();
        projectDTO.setId(project.getId());
        projectDTO.setLeaderName( project.getLeader().getEmail());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setMembers(memberDTOs);

        return projectDTO;
    }
}
