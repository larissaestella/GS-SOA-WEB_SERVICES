package br.com.nexuswork.service;

import br.com.nexuswork.dto.CompletionResultDTO;
import br.com.nexuswork.dto.ProgressDTO;
import br.com.nexuswork.entity.Course;
import br.com.nexuswork.entity.User;
import br.com.nexuswork.entity.UserCourseProgress;
import br.com.nexuswork.exception.ResourceNotFoundException;
import br.com.nexuswork.repository.UserCourseProgressRepository;
import br.com.nexuswork.repository.UserRepository;
import br.com.nexuswork.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserCourseProgressRepository progressRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final GamificationService gamificationService;


    // ============================================================
    // 1) UPDATE PROGRESS — seu método original (mantido 100%)
    // ============================================================
    @Transactional
    public CompletionResultDTO updateProgress(ProgressDTO dto) {

        User user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Course course = courseRepo.findById(dto.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));

        var existing = progressRepo.findByUserIdAndCourseId(dto.userId(), dto.courseId());

        // --- CASO 1: progresso ainda não existe (primeira vez)
        if (existing.isEmpty()) {

            UserCourseProgress p = UserCourseProgress.builder()
                    .userId(dto.userId())
                    .courseId(dto.courseId())
                    .progress(dto.progress())
                    .completed(dto.progress() >= 100.0)
                    .completedAt(dto.progress() >= 100.0 ? LocalDateTime.now() : null)
                    .updatedAt(LocalDateTime.now())
                    .build();

            progressRepo.save(p);

            // SE o curso foi concluído, aplica gamificação
            if (p.isCompleted()) {
                return handleCompletion(user, course, "Curso concluído");
            }

            return new CompletionResultDTO(
                    user.getId(), course.getId(), 0,
                    user.getPoints(), user.getLevel(), user.getLevel(),
                    false, "Progresso atualizado"
            );
        }

        // --- CASO 2: progresso existe
        UserCourseProgress p = existing.get();
        boolean wasCompletedBefore = p.isCompleted();

        p.setProgress(dto.progress());
        p.setUpdatedAt(LocalDateTime.now());

        // SE agora concluiu pela primeira vez
        if (dto.progress() >= 100.0 && !wasCompletedBefore) {
            p.setCompleted(true);
            p.setCompletedAt(LocalDateTime.now());
            progressRepo.save(p);
            return handleCompletion(user, course, "Curso concluído");
        }

        progressRepo.save(p);
        return new CompletionResultDTO(
                user.getId(), course.getId(), 0,
                user.getPoints(), user.getLevel(), user.getLevel(),
                false, "Progresso atualizado"
        );


    }


    // ============================================================
    // MÉTODO AUXILIAR — aplicar pontos + calcular nível
    // ============================================================
    private CompletionResultDTO handleCompletion(User user, Course course, String message) {

        int pointsGained = (course.getPoints() != null && course.getPoints() > 0)
                ? course.getPoints()
                : course.getDifficulty() * 50;

        int oldPoints = user.getPoints();
        int oldLevel = user.getLevel();

        int newPoints = oldPoints + pointsGained;
        int newLevel = gamificationService.levelFromPoints(newPoints);

        user.setPoints(newPoints);
        user.setLevel(newLevel);
        userRepo.save(user);

        return new CompletionResultDTO(
                user.getId(), course.getId(),
                pointsGained, newPoints,
                oldLevel, newLevel,
                newLevel > oldLevel,
                message
        );
    }


    // ============================================================
    // 2) LISTAR TODOS OS PROGRESSOS
    // ============================================================
    public List<UserCourseProgress> listAll() {
        return progressRepo.findAll();
    }


    // ============================================================
    // 3) LISTAR TODOS OS CURSOS E PROGRESSOS DO USUÁRIO
    // ============================================================
    public List<UserCourseProgress> getProgressByUser(Long userId) {
        return progressRepo.findByUserId(userId);
    }


    // ============================================================
    // 4) BUSCAR PROGRESSO DO USUÁRIO EM UM CURSO ESPECÍFICO
    // ============================================================
    public UserCourseProgress getProgressByCourse(Long userId, Long courseId) {
        return progressRepo.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Progresso não encontrado"));
    }
}
