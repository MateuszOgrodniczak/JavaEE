package com.jee.spring_labs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new EncodingFilter(), ChannelProcessingFilter.class);

        http
                .authorizeRequests()
                .antMatchers("/admin/**", "/admin", "/userRequests/admin/**").hasAuthority("ADMIN")
                .antMatchers("/users/**", "/requests/**", "/buildings/**").hasAuthority("ADMIN")
                .antMatchers("/owner/**", "/owner", "/userRequests/owner/**").hasAuthority("OWNER")
                .antMatchers("/tenant/**", "/tenant", "/userRequests/tenant/**").hasAuthority("TENANT")
                .antMatchers("/home/login").anonymous()
                .antMatchers("/home/register").anonymous()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/home/login")
                .loginProcessingUrl("/home/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .logoutUrl("/home/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/home/logout"))
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true);

    }
}
