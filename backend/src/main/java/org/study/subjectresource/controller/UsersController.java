package org.study.subjectresource.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.study.subjectresource.dto.UsersDto;
import org.study.subjectresource.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDto> save(@RequestBody UsersDto usersDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.save(usersDto));
    }
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsersDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findAll());
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsersDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.findById(id));
    }
    @GetMapping("/total")
    public ResponseEntity<Long> total() {
        return ResponseEntity.ok(usersService.getTotalUsers());
    }

    @PatchMapping("/{id}/promote")
    public ResponseEntity<String> toAdmin(@PathVariable Long id) {
        try {
            usersService.toAdmin(id);
            return ResponseEntity.ok("L'utilisateur a été promu au rôle ADMIN avec succès");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usersService.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    }
