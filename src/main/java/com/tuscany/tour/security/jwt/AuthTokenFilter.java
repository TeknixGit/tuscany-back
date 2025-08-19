package com.tuscany.tour.security.jwt;

import com.tuscany.tour.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

   // La classe AuthTokenFilter agit comme un filtre de sécurité
   // pour intercepter les requêtes HTTP, valider les tokens JWT,
   // et configurer le contexte de sécurité de l'application.
   // Cela permet de sécuriser les endpoints en s'assurant
   // que seules les requêtes authentifiées peuvent accéder
   // aux ressources protégées.

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    // Le filtre AuthTokenFilter est enregistré comme un bean Spring,
    // ce qui permet à Spring de le gérer et de l'injecter
    // dans le contexte de sécurité de l'application.

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {

            String jwt = parseJwt(request); // extraction of jwt (json web token) from the request
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt); // get username from Jwt
                System.out.println("Username from JWT: " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // search the user details using username


                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                // create authentification object that contain , the user details + authorities

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // This line sets additional details about the authentication, such as the remote IP address and session ID
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // By setting the authentication, the application ensures that the user
                // is recognized as authenticated for the rest of the request lifecycle.


            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }
}
