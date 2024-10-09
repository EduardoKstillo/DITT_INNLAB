package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.models.EntryExitRecord;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.EntryExitRecordRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/entryexit")
public class EntryExitController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryExitRecordRepository entryExitRecordRepository;
    //marca entrada de usuario
    @PostMapping("/marcarentrada")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> markEntry(@Valid @RequestBody EntryExitRecord entryExitRecord) {
        User user = userRepository.findById(entryExitRecord.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        entryExitRecord.setTimestamp(new Date());
        entryExitRecord.setEntry(true);
        entryExitRecord.setUser(user);

        entryExitRecordRepository.save(entryExitRecord);
        return ResponseEntity.ok(new MessageResponse("Entry marked successfully!"));
    }
    //marca salida del usuario. Guarda en la bd , se tiene que evniar los datos del usuario .
    @PostMapping("/marcarsalida")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> markExit(@Valid @RequestBody EntryExitRecord entryExitRecord) {
        User user = userRepository.findById(entryExitRecord.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        entryExitRecord.setTimestamp(new Date());
        entryExitRecord.setEntry(false);
        entryExitRecord.setUser(user);

        entryExitRecordRepository.save(entryExitRecord);
        return ResponseEntity.ok(new MessageResponse("Exit marked successfully!"));
    }
}