package juro.copyjuro.config.model;

import lombok.Builder;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * information about token that is custom
 */
public class UserAuthenticationToken extends AbstractAuthenticationToken {
     //* represents the authenticated user. typically, serviceUser would be a custom user details implementation containing user information.
    private final ServiceUser serviceUser;
    // Represents credentials like a password or token, though in this case, it's set and then erased for security reasons.
    private final String credentials;

    @Builder
    public UserAuthenticationToken(
            ServiceUser serviceUser,
            String credentials,
            Collection<? extends GrantedAuthority> authorities,
            //Additional details about the authentication request(IP or session info)
            Map<String, String> details
    ) {
        super(authorities);
        this.credentials = credentials;
        this.serviceUser = serviceUser;
        super.setDetails(details);
        //Erases credentails to prevent them from being stored in memory, enhancing security.
        super.eraseCredentials();
        //Marks the authentication token as authenticated.
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    //returns the serviceUser which represents the principal(the authenticated user)
    @Override
    public ServiceUser getPrincipal() {
        return this.serviceUser;
    }

    @Override
    public Map<String, String> getDetails() {
        return (Map<String, String>)super.getDetails();
    }
}
