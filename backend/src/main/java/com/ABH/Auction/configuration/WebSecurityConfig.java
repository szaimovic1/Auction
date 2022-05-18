package com.ABH.Auction.configuration;

import com.ABH.Auction.authorization.CustomOAuth2UserService;
import com.ABH.Auction.authorization.CustomSuccessHandler;
import com.ABH.Auction.filters.JwtFilter;
import com.ABH.Auction.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtEntryPoint jwtEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .and()
            .authorizeRequests()
            .antMatchers(
                    "/",
                "/error",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
            ).permitAll()
            .antMatchers("/api/v*/product/bid",
                    "/api/v*/product/bidders",
                    "/api/v*/product/product-bid",
                    "/api/v*/product/recommended"
             ).authenticated()
             .antMatchers(
                 HttpMethod.POST, "/api/v*/product"
             ).authenticated()
             .antMatchers(
                     "/api/v*/user/admin/**"
             ).hasAuthority("ROLE_ADMIN")
            .antMatchers(
                     "/api/v*/registration/**",
                     "/api/v*/login/**",
                     "/oauth2/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/v3/api-docs",
                    "/api/v*/category/**",
                    "/api/v*/product/**",
                    "/api/v*/size/**",
                    "/api/v*/color/**",
                    "/api/v*/payment/update-payment",
                    "/ws/**",
                    "/app/**"
            ).permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin()
                .usernameParameter("email")
                .permitAll()
                .defaultSuccessUrl("/")
            .and()
            .oauth2Login()
                .failureUrl("/error")
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(customSuccessHandler)
            .and()
            .logout().permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
