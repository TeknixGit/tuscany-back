package com.tuscany.tour.config;

import com.tuscany.tour.models.AppRole;
import com.tuscany.tour.models.Role;
import com.tuscany.tour.models.User;
import com.tuscany.tour.repository.RoleRepository;
import com.tuscany.tour.security.jwt.JwtUtils;
import com.tuscany.tour.security.services.UserDetailsImpl;
import com.tuscany.tour.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    RoleRepository roleRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    String username;
    String idAttributeKey;

    @Override

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
        String registrationId = oAuth2Token.getAuthorizedClientRegistrationId(); // google, github, facebook
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        String email = (String) attributes.getOrDefault("email", "");
        String name = (String) attributes.getOrDefault("name", "");
        String username;
        String idAttributeKey;

        switch (registrationId) {
            case "google":
                username = email.split("@")[0];
                idAttributeKey = "sub";
                break;
            case "github":
                username = (String) attributes.getOrDefault("login", "");
                idAttributeKey = "id";
                break;
            case "facebook":
                username = name.toLowerCase().replaceAll("\\s+", ".");
                idAttributeKey = "id";
                break;
            default:
                throw new IllegalArgumentException("Unsupported OAuth provider: " + registrationId);
        }

        if (email.isEmpty()) {
            throw new RuntimeException("Email is required from OAuth2 provider");
        }

        //System.out.println("OAUTH SUCCESS: " + registrationId + " -> " + email + " : " + username);

        userService.findByEmail(email)
                .ifPresentOrElse(user -> {
                    DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                            List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
                            attributes,
                            idAttributeKey
                    );
                    updateSecurityContext(oauthUser, registrationId, user.getRole().getRoleName().name());
                }, () -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUserName(username);
                    newUser.setSignUpMethod(registrationId);

                    Role role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Default role not found"));
                    newUser.setRole(role);

                    userService.registerUser(newUser);

                    DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                            List.of(new SimpleGrantedAuthority(role.getRoleName().name())),
                            attributes,
                            idAttributeKey
                    );
                    updateSecurityContext(oauthUser, registrationId, role.getRoleName().name());
                });

        // JWT token creation
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<SimpleGrantedAuthority> authorities = oauthUser.getAuthorities().stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .collect(Collectors.toSet());

        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName().name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(
                user.getUserId(),
                username,
                email,
                null,
                false,
                authorities
        );

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build().toUriString();

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void updateSecurityContext(DefaultOAuth2User oauthUser, String provider, String roleName) {
        Authentication securityAuth = new OAuth2AuthenticationToken(
                oauthUser,
                List.of(new SimpleGrantedAuthority(roleName)),
                provider
        );
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
    }
}