package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.user.UserRequestDTO;
import com.bezkoder.spring.security.postgresql.dto.user.UserResponseDTO;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        try {
            System.out.println(userRequestDTO);
            return userRepository.findById(id)
                    .map(user -> {
                        user.setFirstName(userRequestDTO.getFirstName());
                        user.setLastName(userRequestDTO.getLastName());
                        user.setEmail(userRequestDTO.getEmail());
                        if (userRequestDTO.getPassword() != null) {
                            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
                        }
                        user.setUniversity(userRequestDTO.getUniversity());
                        user.setPhone(userRequestDTO.getPhone());
                        user.setDni(userRequestDTO.getDni());
                        user.setBirthDate(userRequestDTO.getBirthDate());

                        if (userRequestDTO.getRoleIds() != null && !userRequestDTO.getRoleIds().isEmpty()) {
                            List<Role> roles = roleRepository.findAllById(userRequestDTO.getRoleIds());
                            user.setRoles(new HashSet<>(roles));
                        }

                        User updatedUser = userRepository.save(user);
                        return mapToDTO(updatedUser);
                    })
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
        } catch (Exception e) {
            // Aquí puedes imprimir el stacktrace para más detalles
            e.printStackTrace();
            throw new RuntimeException("Ocurrió un error inesperado al actualizar el usuario", e);
        }
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
        user.setBirthDate(userRequestDTO.getBirthDate());
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
        userResponseDTO.setBirthDate(user.getBirthDate());

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name()) // Convertir el enum ERole a String
                .collect(Collectors.toSet());
        userResponseDTO.setRoles(roles);

        return userResponseDTO;
    }
}