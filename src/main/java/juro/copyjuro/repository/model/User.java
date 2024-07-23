package juro.copyjuro.repository.model;

import jakarta.persistence.*;
import juro.copyjuro.dto.user.UserRole;
import juro.copyjuro.repository.converter.EnumListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

    @Convert(converter = EnumListConverter.class)
    private List<UserRole> roles;
}
