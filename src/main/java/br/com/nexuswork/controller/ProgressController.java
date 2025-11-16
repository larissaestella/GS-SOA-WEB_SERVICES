package br.com.nexuswork.controller;

import br.com.nexuswork.dto.CompletionResultDTO;
import br.com.nexuswork.dto.ProgressDTO;
import br.com.nexuswork.entity.UserCourseProgress;
import br.com.nexuswork.service.ProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class ProgressController {

    private final ProgressService progressService;

    // ----------------------------
    // 1) Atualizar progresso
    // ----------------------------
    @PostMapping
    public ResponseEntity<CompletionResultDTO> update(@Valid @RequestBody ProgressDTO dto) {
        var result = progressService.updateProgress(dto);
        return ResponseEntity.ok(result);
    }

    // ----------------------------
    // 2) Buscar progresso de todos os cursos do usuário
    // ----------------------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCourseProgress>> getProgressByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(progressService.getProgressByUser(userId));
    }

    // ----------------------------
    // 3) Buscar progresso de um curso específico
    // ----------------------------
    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<UserCourseProgress> getProgressByCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(progressService.getProgressByCourse(userId, courseId));
    }

    // ----------------------------
    // 4) (Opcional) Buscar todos os registros
    // ----------------------------
    @GetMapping
    public ResponseEntity<List<UserCourseProgress>> listAll() {
        return ResponseEntity.ok(progressService.listAll());
    }
}
