package br.com.nexuswork.controller;

import br.com.nexuswork.dto.AIQuestionDTO;
import br.com.nexuswork.service.AIRecommendationService;
import br.com.nexuswork.service.UserService;
import br.com.nexuswork.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class AIController {
    private final AIRecommendationService aiService;
    private final UserService userService;
    private final CourseService courseService;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@Valid @RequestBody AIQuestionDTO dto) {
        userService.findById(dto.userId());
        courseService.findById(dto.courseId());
        String answer = aiService.askQuestionAboutCourse(dto.userId(), dto.courseId(), dto.question(), dto.courseContext());
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<String> recommendations(@PathVariable Long userId) {
        var u = userService.findById(userId);
        String rec = aiService.recommendForUser(u.getPoints(), u.getLevel());
        return ResponseEntity.ok(rec);
    }
}
