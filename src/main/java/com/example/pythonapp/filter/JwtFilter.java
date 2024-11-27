package com.example.pythonapp.filter;
import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.model.enums.Role;
import com.example.pythonapp.repository.StudentRepository;
import com.example.pythonapp.repository.TeacherRepository;
import com.example.pythonapp.repository.UserRepository;
import com.example.pythonapp.jwt.JWTToken;
import com.example.pythonapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Component
@EnableWebSecurity
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTToken jwt;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorization = httpServletRequest.getHeader("Authorization");
        String token = null;
        String userName = null;

        if (authorization!=null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            userName = jwt.getUsernameFromToken(token);
        }

        if (userName!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userEntity = userRepository.findByName(userName).orElseThrow(UserNotFoundException::new);
            String role = teacherRepository.findByName(userName).isPresent() ? "ROLE_"+ Role.TEACHER : "ROLE_"+Role.STUDENT;
            UserDetails userDetails
                    = new User(userEntity.getName(),userEntity.getPassword(), List.of(new SimpleGrantedAuthority(role))) ;
            if (jwt.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null, List.of(new SimpleGrantedAuthority(role)));

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails( httpServletRequest)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }


    }


}
