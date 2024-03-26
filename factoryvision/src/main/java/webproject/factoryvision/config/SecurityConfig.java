package webproject.factoryvision.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import webproject.factoryvision.domain.token.JwtAuthenticationFilter;


@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebMvc
@CrossOrigin
@Configuration
public class SecurityConfig implements WebMvcConfigurer{

    @Bean
    public BCryptPasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final CorsConfig corsConfig;
    private final String[] allowedUrls = {"/", "/swagger-ui/**", "/v3/**", "/factoryvision/login", "/factoryvision/signup", "/factoryvision/upload"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(allowedUrls).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                //
//                .addFilter(corsConfig.corsFilter())
                .build();
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
//                .allowedOrigins("*")
                .allowedOriginPatterns("http://localhost:3002", "http://localhost:5002", "http://localhost:8080")
                .allowedMethods("OPTIONS","GET","POST","PUT","DELETE")
//                .allowedHeaders("*");
                .allowedHeaders("Authorization", "Content-Type");

    }
}
    
