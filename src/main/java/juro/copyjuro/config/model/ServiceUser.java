package juro.copyjuro.config.model;

import juro.copyjuro.dto.user.UserRole;
import juro.copyjuro.repository.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUser implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    private List<GrantedAuthority> authorities;

    public static ServiceUser of(User user) {
        return ServiceUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities(
                        AuthorityUtils.createAuthorityList(
                                user.getRoles()
                                        .stream()
                                        .map(UserRole::name)
                                        .toList()
                                        .toArray(new String[0])))
                .build();

    }
}