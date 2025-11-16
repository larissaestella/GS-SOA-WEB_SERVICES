CREATE TABLE IF NOT EXISTS user_course_progress (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  progress DECIMAL(5,2) DEFAULT 0,
  completed BOOLEAN DEFAULT FALSE,
  completed_at DATETIME DEFAULT NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_progress_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_progress_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
  UNIQUE KEY ux_user_course (user_id, course_id)
);
