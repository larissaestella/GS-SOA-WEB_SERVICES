package br.com.nexuswork.service;

import br.com.nexuswork.dto.UserDTO;
import br.com.nexuswork.entity.User;
import br.com.nexuswork.enums.UserRole;
import br.com.nexuswork.exception.ResourceNotFoundException;
import br.com.nexuswork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GamificationService gamificationService;
    private final BCryptPasswordEncoder passwordEncoder;

    public User create(UserDTO dto) {

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password() != null ? passwordEncoder.encode(dto.password()) : null)
                .role(UserRole.COLLABORATOR)   // sempre colaborador ao criar
                .level(1)                      // nível inicial
                .points(0)                     // pontos iniciais
                .build();

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public void addPoints(Long userId, int pointsToAdd) {
        User u = findById(userId);
        int newPoints = u.getPoints() + pointsToAdd;

        u.setPoints(newPoints);
        u.setLevel(gamificationService.levelFromPoints(newPoints));

        userRepository.save(u);
    }
}
