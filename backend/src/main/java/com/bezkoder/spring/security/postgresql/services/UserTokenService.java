package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.exception.ResourceNotFoundException;
import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.models.UserToken;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.repository.UserTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTokenService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;

    public void saveUserToken(Long userId, String token) {
        UserToken userToken = new UserToken();
        userToken.setUser(new User(userId));
        userToken.setToken(token);
        userTokenRepository.save(userToken);
    }

    @Transactional
    public void deleteUserToken(Long userId) {
        if (userTokenRepository.findByUser_Id(userId).isEmpty()) {
            throw new ResourceNotFoundException("No token found for user ID: " + userId);
        }
        userTokenRepository.deleteByUserId(userId);
    }

    public String getTokenByUserId(Long userId) {
        UserToken userToken = userTokenRepository.findByUserId(userId);
        return userToken != null ? userToken.getToken() : null; // Retorna el token o null si no existe
    }

    // Método para obtener los tokens de todos los moderadores
    public List<String> findAllModeratorsTokens() {
        // Primero buscamos todos los usuarios con el rol de MODERATOR
        List<User> moderators = userRepository.findByRoles_Name(ERole.ROLE_MODERATOR);

        // Luego buscamos los tokens asociados a esos usuarios
        List<String> tokens = new ArrayList<>();
        for (User moderator : moderators) {
            List<UserToken> userTokens = userTokenRepository.findByUser_Id(moderator.getId());
            tokens.addAll(userTokens.stream().map(UserToken::getToken).collect(Collectors.toList()));
        }

        return tokens; // Devolvemos la lista de tokens
    }

}