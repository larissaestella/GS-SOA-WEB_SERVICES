package br.com.nexuswork.controller;

import br.com.nexuswork.dto.UserDTO;
import br.com.nexuswork.entity.User;
import br.com.nexuswork.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody UserDTO dto) {
        User created = userService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<?> getPoints(@PathVariable Long id) {
        var u = userService.findById(id);
        return ResponseEntity.ok(u);
    }

}
