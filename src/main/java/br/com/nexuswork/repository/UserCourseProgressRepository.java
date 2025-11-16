package br.com.nexuswork.repository;

import br.com.nexuswork.entity.UserCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {
    Optional<UserCourseProgress> findByUserIdAndCourseId(Long userId, Long courseId);
    List<UserCourseProgress> findByUserId(Long userId);
    List<UserCourseProgress> findByUserIdAndCompletedTrue(Long userId);

}
