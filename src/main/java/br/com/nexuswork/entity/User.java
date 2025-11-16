package br.com.nexuswork.entity;

import br.com.nexuswork.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, unique=true)
    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserRole role = UserRole.COLLABORATOR;

    @Column(nullable=false)
    private int level = 1;

    @Column(nullable=false)
    private int points = 0;
}
