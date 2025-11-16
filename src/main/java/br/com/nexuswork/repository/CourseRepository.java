package br.com.nexuswork.repository;

import br.com.nexuswork.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {}
