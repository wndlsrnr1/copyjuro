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

/**
 * UserDetails 역할
 * 정보를 User정보를 Security에 넣어주는 역할
 * 확장을 통해 더 많은 정보를 넣을 수 있다.
 * User를 Spring Security가 관리 하게 해준다.
 */
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

    public static ServiceUser user(User user) {
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

    public static ServiceUser guest() {
        return ServiceUser.builder()
                .id(null)
                .username(null)
                .password(null)
                .email(null)
                .authorities(AuthorityUtils.createAuthorityList(UserRole.GUEST.name()))
                .build();
    }
}