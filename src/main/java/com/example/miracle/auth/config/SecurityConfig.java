package com.example.miracle.auth.config;

import com.example.miracle.auth.jwt.JwtFilter;
import com.example.miracle.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, JwtFilter jwtFilter) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
//        corsConfiguration.setAllowedOrigins(List.of("*"));
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/login", "/registration", "/error", "/", "/items/**", "/activate/**").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
//                .formLogin().loginPage("http://localhost:3000/").defaultSuccessUrl("/", true)
                .formLogin().loginPage("http://178.20.41.50/").defaultSuccessUrl("/", true)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

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
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/items/**", "/login", "/registration", "/activate/**");
    }
}
