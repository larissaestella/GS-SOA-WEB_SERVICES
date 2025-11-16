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

    @PostMapping
    public ResponseEntity<CompletionResultDTO> update(@Valid @RequestBody ProgressDTO dto) {
        var result = progressService.updateProgress(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCourseProgress>> getProgressByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(progressService.getProgressByUser(userId));
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    public ResponseEntity<UserCourseProgress> getProgressByCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(progressService.getProgressByCourse(userId, courseId));
    }

    @GetMapping
    public ResponseEntity<List<UserCourseProgress>> listAll() {
        return ResponseEntity.ok(progressService.listAll());
    }
}
