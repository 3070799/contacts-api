package com.ohol.pavel.contactsapi.config.security;

import com.ohol.pavel.contactsapi.security.jwt.JWTAuthenticationEntryPoint;
import com.ohol.pavel.contactsapi.security.jwt.JWTConfigurer;
import com.ohol.pavel.contactsapi.rest.RestConstants;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author Pavel Ohol
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTConfigurer jwtConfigurer;

    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                    .csrf().disable()
                        .exceptionHandling()
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                    .authorizeRequests()
                        .antMatchers("/webjars/**").permitAll()
                        .antMatchers("/v2/api-docs").permitAll()
                        .antMatchers("/swagger-ui.html").permitAll()
                        .antMatchers("/swagger-resources/**").permitAll()
                        .antMatchers(RestConstants.Auth.LOGIN).permitAll()
                        .antMatchers(RestConstants.Auth.REGISTER_USER).permitAll()
                        .antMatchers(RestConstants.Contact.CONTACTS_IMAGES_ROOT + "/**").permitAll()
                        .antMatchers("/").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .httpBasic().disable()
                    .formLogin().disable()
                    .logout().disable()
                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
