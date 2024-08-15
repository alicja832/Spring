package com.example.demoggggg.filter;
import com.example.demoggggg.jwt.MyJwt;
import com.example.demoggggg.model.UserEntity;
import com.example.demoggggg.repository.UserRepository;
import jakarta.servlet.ServletOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Order(Integer.MIN_VALUE)
@Component
@EnableWebSecurity
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private MyJwt jwt;
    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        String userName = null;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=null;
        System.out.println("ath"+authorization);
        if (null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            userName = jwt.getUsernameFromToken(token);
        }

        if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userEntity = userRepository.findByName(userName);
            System.out.println(userEntity.getRole());
            UserDetails userDetails
                    = new User(userEntity.getName(),userEntity.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" +userEntity.getRole()))) ;


            if (jwt.validateToken(token, userDetails)) {
                usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails( httpServletRequest)
                );
                System.out.println(usernamePasswordAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        try {
            // Logika filtra
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            // Logowanie błędów
            e.printStackTrace();
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
        if (null != authorization && authorization.startsWith("Bearer ")) {
            //System.out.println(usernamePasswordAuthenticationToken.getDetails());
           // SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
           // System.out.println(SecurityContextHolder.getContext().getAuthentication()+"Co tam dalej");
        }

    }


}