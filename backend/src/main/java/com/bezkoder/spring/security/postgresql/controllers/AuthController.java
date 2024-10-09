package com.bezkoder.spring.security.postgresql.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bezkoder.spring.security.postgresql.dto.TokenDTO;
import com.bezkoder.spring.security.postgresql.services.UserTokenService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.LoginRequest;
import com.bezkoder.spring.security.postgresql.payload.request.SignupRequest;
import com.bezkoder.spring.security.postgresql.payload.response.JwtResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(origins = {"http://localhost:8100/", "http://vps-zap907917-1.zap-srv.com", "http://proyectos-vri.unsa.edu.pe:9090"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  private UserTokenService userTokenService;

  @Autowired
  JwtUtils jwtUtils;

  @Value("${reniec.api.url}")
    private String reniecApiUrl;

  @Value("${reniec.api.token}")
    private String reniecApiToken;

  @PostMapping("/register-device")
  public ResponseEntity<Void> registerToken(@RequestBody TokenDTO tokenDTO) {
    if (tokenDTO == null || tokenDTO.getUserId() == null || tokenDTO.getToken() == null) {
      return ResponseEntity.badRequest().build();
    }

    Long userId = tokenDTO.getUserId();
    String token = tokenDTO.getToken();
    userTokenService.saveUserToken(userId, token);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/logout-device/{userId}")
  public ResponseEntity<Void> logoutDevice(@PathVariable Long userId) {
    userTokenService.deleteUserToken(userId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    System.out.println(loginRequest.getEmail());

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity
        .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

    if (userRepository.existsByDni(signUpRequest.getDni())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }


    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = User.builder()
                    .firstName(signUpRequest.getFirstName())
                    .lastName(signUpRequest.getLastName())
                    .university(signUpRequest.getUniversity())
                    .email(signUpRequest.getEmail())
                    .dni(signUpRequest.getDni())
                    .password(encoder.encode(signUpRequest.getPassword()))
                    .build();

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
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
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }


  @GetMapping("/dni-verification")
  public ResponseEntity<?> verifyDni(@RequestParam String dni) {
    String apiUrl = reniecApiUrl + "/api/v1/dni/" + dni + "?token=" + reniecApiToken;

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Object> response = restTemplate.getForEntity(apiUrl, Object.class);

    if (response.getStatusCode() == HttpStatus.OK) {
        return ResponseEntity.ok(response.getBody());
    } else {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: DNI not found in Reniec."));
    }
  }



}
