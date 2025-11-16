package br.com.nexuswork.controller;

import br.com.nexuswork.dto.CourseDTO;
import br.com.nexuswork.entity.Course;
import br.com.nexuswork.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody CourseDTO dto) {
        Course c = courseService.create(dto);
        return ResponseEntity.ok(c);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> get(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }
}
