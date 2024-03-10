package com.nestorurquiza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@EnableWebSecurity
@EnableJdbcHttpSession
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenRepository jwtTokenRepository; // Add this line

    public SecurityConfig(@Lazy UserDetailsService userDetailsService, JwtTokenRepository jwtTokenRepository) { // Modify this constructor
        this.userDetailsService = userDetailsService;
        this.jwtTokenRepository = jwtTokenRepository; // Initialize the jwtTokenRepository
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtTokenRepository jwtTokenRepository) {
        return new CustomJwtDecoder(jwtTokenRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/api/login", "/api/oauth2/token").permitAll()
                .anyRequest().authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // Use this to avoid redirection and instead return 401
                .and()
            .formLogin()
                .loginProcessingUrl("/api/login")
                .successHandler((request, response, authentication) -> response.setStatus(HttpStatus.OK.value())) // Customize on success
                .failureHandler((request, response, exception) -> response.setStatus(HttpStatus.UNAUTHORIZED.value())) // Customize on failure
                .permitAll()
                .and()
            .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
            .logout()
                .logoutUrl("/api/logout")
                .and()
            .oauth2ResourceServer()
                .jwt();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}

