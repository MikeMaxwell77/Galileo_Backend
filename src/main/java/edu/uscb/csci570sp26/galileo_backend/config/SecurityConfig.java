package edu.uscb.csci570sp26.galileo_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import edu.uscb.csci570sp26.galileo_backend.security.JwtAuthenticationFilter;
import edu.uscb.csci570sp26.galileo_backend.security.JwtUtil;

import java.util.List;

@Configuration
public class SecurityConfig {
	
	 private final JwtUtil jwtUtil;

	    public SecurityConfig(JwtUtil jwtUtil) {
	        this.jwtUtil = jwtUtil;
	    }
	
	// We will at some point need to configure this further 
	// A helpful gist from dr. Canada: https://gist.github.com/doctorcanada/36e5640977e4fa0a3f371d3a34e28df7
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF since we're using JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ Stateless sessions
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers(HttpMethod.PUT, "/bookmark/**").hasAuthority("USER")
            	.requestMatchers(HttpMethod.POST, "/bookmark/**").hasAuthority("USER")
                .anyRequest().permitAll() // ✅ Allow all requests for now
            ) 
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(httpBasic -> httpBasic.disable()) // Disable HTTP Basic Auth
            .formLogin(login -> login.disable()); // Disable Form Login

            System.out.println("✅ SecurityConfig initialized with Role-Based Access Control (RBAC)");

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5174")); // ✅ Allow frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}    