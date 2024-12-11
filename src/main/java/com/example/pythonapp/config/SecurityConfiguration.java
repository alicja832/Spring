package com.example.pythonapp.config;
import com.example.pythonapp.details.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.pythonapp.filter.JwtFilter;
import  org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private AppUserDetailsService appUserDetailsService;
    private final JwtFilter jwtFilter;
    private final DaoAuthenticationProvider authProvider;
    
    @Autowired
    public SecurityConfiguration(JwtFilter jwtFilter) {

        this.jwtFilter = jwtFilter;
        this.authProvider = new DaoAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String permittedList [] = {"exercise/one/**","user/","user/ranking","user/token","user/teacher","user/code","exercise/out","solution/programming/check","solution/abc/check","user/authenticate","solution/programming/test"};
              return http
                      .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(permittedList).permitAll()
                                .requestMatchers(HttpMethod.GET,"exercise/programming").permitAll()
                                .requestMatchers(HttpMethod.GET,"exercise/abc").permitAll()
                                .requestMatchers(HttpMethod.POST,"exercise/programming").hasRole("TEACHER")
                                .requestMatchers(HttpMethod.POST,"exercise/abc").hasRole("TEACHER")
                                .anyRequest().authenticated())
                      .authenticationProvider(authProvider)
                      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                ).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {


        authProvider.setUserDetailsService(appUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


}
