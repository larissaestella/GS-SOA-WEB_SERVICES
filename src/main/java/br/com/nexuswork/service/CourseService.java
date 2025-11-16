package br.com.nexuswork.service;

import br.com.nexuswork.dto.CourseDTO;
import br.com.nexuswork.entity.Course;
import br.com.nexuswork.exception.ResourceNotFoundException;
import br.com.nexuswork.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course create(CourseDTO dto) {
        Course c = Course.builder()
                .title(dto.title())
                .description(dto.description())
                .difficulty(dto.difficulty())
                .points(dto.points())
                .build();
        return courseRepository.save(c);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));
    }


}
