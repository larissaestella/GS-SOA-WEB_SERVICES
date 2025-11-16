package br.com.nexuswork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_course_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","course_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCourseProgress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="course_id", nullable=false)
    private Long courseId;

    @Column(nullable=false)
    private double progress = 0.0;

    @Column(nullable=false)
    private boolean completed = false;

    @Column(name="completed_at")
    private LocalDateTime completedAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
