package com.tecsup.aopserva;

import com.tecsup.aopserva.handlers.LoginSuccessHandler;
import com.tecsup.aopserva.services.JpaUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccessHandler successHandler;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Permitir acceso público al login y recursos estáticos
                        .requestMatchers("/login", "/css/**", "/images/**", "/js/**").permitAll()

                        // Solo ADMIN puede crear, editar o eliminar cursos
                        .requestMatchers("/form/**", "/eliminar/**").hasRole("ADMIN")

                        // Los demás endpoints son accesibles por cualquier usuario autenticado
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error_403")
                )

                .formLogin(form -> form
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .httpBasic(httpBasic -> {});

        return http.build();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
