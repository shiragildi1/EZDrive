
package com.ezdrive.ezdrive.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    @Value("${app.cors.allowed-origins:http://localhost:3000,http://127.0.0.1:3000,https://ezdrive-client.onrender.com}")
    private String allowedOriginsCsv;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(
                    "/api/auth/**",
                    "/api/otp/**",
                    "/api/user/me",
                    "/api/game-sessions/**",
                    "/api/profile/**",
                    "/api/ranking/**",
                    "/game-sessions/**",
                    "/game-sessions/startMemory",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/question/xml",
                    "/agent/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);

        // ← התאמה מלאה ל-Origin (חשוב עם credentials: include)
        cfg.setAllowedOrigins(Arrays.asList(allowedOriginsCsv.split("\\s*,\\s*")));

        // אל תגבילי כותרות – כך preflight לא ייפול
        cfg.addAllowedHeader("*");

        // בצד לקוח צריך רק את Set-Cookie
        cfg.setExposedHeaders(Arrays.asList("Set-Cookie"));

        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}

