package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.user.UserRequestDTO;
import com.bezkoder.spring.security.postgresql.dto.user.UserResponseDTO;
import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.SignupRequest;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Crear un nuevo usuario a partir de UserRequestDTO
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = mapToEntity(userRequestDTO);
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    // Obtener un usuario por su ID y devolver un DTO
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    // Obtener todos los usuarios y devolver una lista de DTOs
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Actualizar un usuario
    @Transactional
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO UserRequestDTO) {

        System.out.println(UserRequestDTO);
        // Buscar al usuario existente por ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Verificar si el correo electrónico ya está en uso por otro usuario
        if (userRepository.existsByEmail(UserRequestDTO.getEmail()) &&
                !user.getEmail().equals(UserRequestDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Actualizar campos
        user.setFirstName(UserRequestDTO.getFirstName());
        user.setLastName(UserRequestDTO.getLastName());
        user.setUniversity(UserRequestDTO.getUniversity());
        user.setEmail(UserRequestDTO.getEmail());
        user.setPhone(UserRequestDTO.getPhone());

        // Actualizar contraseña solo si se proporciona
        if (UserRequestDTO.getPassword() != null && !UserRequestDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(UserRequestDTO.getPassword()));
        }

        // Actualizar roles si es necesario
        Set<String> strRoles = UserRequestDTO.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles != null) {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        } else {
            // Si no se proporcionan roles, se asigna el rol de usuario por defecto
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }



    // Eliminar un usuario
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Buscar usuarios por coincidencia parcial de email
    public List<UserResponseDTO> searchUsersByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversión

    private User mapToEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setUniversity(userRequestDTO.getUniversity());
        user.setPhone(userRequestDTO.getPhone());
        user.setDni(userRequestDTO.getDni());
        return user;
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setUniversity(user.getUniversity());
        userResponseDTO.setPhone(user.getPhone());
        userResponseDTO.setDni(user.getDni());

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name()) // Convertir el enum ERole a String
                .collect(Collectors.toSet());
        userResponseDTO.setRoles(roles);

        return userResponseDTO;
    }
}