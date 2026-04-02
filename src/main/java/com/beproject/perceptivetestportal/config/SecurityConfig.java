package com.beproject.perceptivetestportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.beproject.perceptivetestportal.security.CustomUserDetailsService;
import com.beproject.perceptivetestportal.security.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    

    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            "/assets/**", 
            "/css/**", 
            "/js/**", 
            "/models/**", 
            "/favicon.ico", 
            "/error"
        );
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. UI Pages & Controller Mappings
                .requestMatchers("/", "/index.html", "/login.html", "/signup.html", "/error").permitAll()
                .requestMatchers("/teacher-dashboard.html", "/student-dashboard.html", "/create-test.html", 
                "/generate-questions.html", "/question-bank.html", "/manage-test.html", "/quick-start-quiz.html", 
                "/exam-room.html", "/instructions.html", "/test-summary.html", "/result.html").permitAll()
                
                // Allow the paths defined in WelcomeController
                .requestMatchers("/teacher/**").permitAll() 
                .requestMatchers("/student/**").permitAll() 

                // 2. Static Resources (Added /models/** to ensure AI proctoring files load)
                .requestMatchers("/css/**", "/js/**", "/assets/**", "/static/**", "/favicon.ico", "/models/**").permitAll()

                // 3. Auth APIs
                .requestMatchers("/auth/**", "/api/auth/**").permitAll()

                // 4. Specific Student APIs (Must be evaluated BEFORE wildcard rules)
                .requestMatchers("/api/questions/generate-quiz").hasRole("STUDENT")
                .requestMatchers("/api/tests/submit-quiz").hasRole("STUDENT")
                .requestMatchers("/api/student/**").hasRole("STUDENT")
                

                // 5. Protected Teacher APIs (Wildcards are evaluated LAST)
                .requestMatchers("/api/tests/**", "/tests/**").hasRole("TEACHER")
                .requestMatchers("/api/questions/**", "/questions/**").hasRole("TEACHER")
                .requestMatchers("/api/ai/**").hasRole("TEACHER")
                
                // All others
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
    



}