package com.example.miracle.auth.config;

import com.example.miracle.auth.entity.JwtFilter;
import com.example.miracle.auth.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${api.url}")
    private String apiUrl;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        auth -> auth
                                .antMatchers("/login", "/registration", "/error", "/", "/items/**", "/activate/**").permitAll()
                                .antMatchers("/api/auth/login", "/api/auth/token", "/user").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                ).build();
    }


//
//    private final UserDetailsServiceImpl userDetailsServiceImpl;
//    private final JwtFilter jwtFilter;
//
//    @Value("${api.url}")
//    private String apiUrl;
//
//    @Autowired
//    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, JwtFilter jwtFilter) {
//        this.userDetailsServiceImpl = userDetailsServiceImpl;
//        this.jwtFilter = jwtFilter;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
//        corsConfiguration.setAllowedOrigins(List.of("*"));
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setExposedHeaders(List.of("Authorization"));

//        http
//                .csrf().disable()
//                .cors().and()
//                .authorizeRequests()
//                .antMatchers("/admin").hasRole("ADMIN")
//                .antMatchers("/login", "/registration", "/error", "/", "/items/**", "/activate/**").permitAll()
//                .anyRequest().hasAnyRole("USER", "ADMIN")
//                .and()
//                .formLogin().loginPage(apiUrl).defaultSuccessUrl("/", true)
//                .formLogin().loginPage("http://178.20.41.50/").defaultSuccessUrl("/", true)
//                .and()
//                .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsServiceImpl)
//                .passwordEncoder(getPasswordEncoder());
//    }

//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
////        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/login", "http://localhost:8090/login", "http://178.20.41.50"));
//        configuration.setAllowedOriginPatterns(List.of("*"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//
//        return source;
//    }

    // Turn off some endpoints from spring security filters
//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring().antMatchers("/items/**", "/login", "/registration", "/activate/**");
//    }
}
