package com.inorg.config;

import com.inorg.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());*/
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());*/
        http
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Collections.singletonList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))

                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers( "/contact","/register","/user")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .csrf(csrfConfig->csrfConfig.disable())
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/accounts").hasAuthority("WRITE")
                        .requestMatchers("/balance").hasAnyAuthority("READ", "WRITE")
                        .requestMatchers("/loans").hasAuthority("READ")
                        .requestMatchers("/cards").hasAuthority("READ")
//                        .requestMatchers("/user").authenticated()
//                .requestMatchers("/accounts", "/balance", "/loans", "/cards").authenticated()

                        .requestMatchers("/notices", "/contact", "/error","/register","/user").permitAll());
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
////        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
////        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
//        http
//                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                        CorsConfiguration config = new CorsConfiguration();
//                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//                        config.setAllowedMethods(Collections.singletonList("*")); //GET,PUT,POST,DELETE
//                        config.setAllowCredentials(true);
//                        config.setExposedHeaders(Collections.singletonList("Authorization"));
//                        config.setAllowedHeaders(Collections.singletonList("*"));
//                        config.setMaxAge(3600L);
//                        return config;
//                    }
//                }))
//
//                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
//                        .ignoringRequestMatchers("/contact","/register","/login")
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                )
//                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new AuthoritiesLoggingAfterFilter(),BasicAuthenticationFilter.class)
//                .addFilterBefore(new RequestValidationBeforeFilter(),BasicAuthenticationFilter.class)
//                .addFilterAfter(new JwtTokenGeneratorFilter(),BasicAuthenticationFilter.class)
//                .addFilterBefore(new JwtTokenValidatorFilter(),BasicAuthenticationFilter.class)
//                .authorizeHttpRequests((requests)->{
//            requests
//                    .requestMatchers("/accounts").hasAuthority("READ")
//                    .requestMatchers("/balance").hasAnyAuthority("READ","WRITE")
//                    .requestMatchers("/loans").hasAuthority("READ")
//                    .requestMatchers("/cards").hasAuthority("READ")
////                    .requestMatchers("/login").authenticated()
////                    .requestMatchers("/card").hasRole("ROLE_ADMIN")
//  //                  .requestMatchers("/accounts","/balance","/loans","/cards").authenticated()
//                    .requestMatchers("/notices","/contact","/error","/register","/login").permitAll();
//        });
//        http.formLogin(withDefaults());
//        http.httpBasic(withDefaults());
//        return http.build();
//    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        UserDetails user= User.withUsername("user").
//                            password("{noop}password").authorities("READ").build();
//
//        UserDetails admin= User.withUsername("admin").
//                password("{bcrypt}$2a$12$UpBRboHAga.quAHJHZA6tOeQYw0zV0T.dHe6kEq/0VEyn9Hb2KQOu").authorities("ADMIN").build();
////inorg@123
//        return new InMemoryUserDetailsManager(user, admin);
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//        return new MessageDigestPasswordEncoder()
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
