
package com.jee.spring_labs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private LogoutSuccessHandler oauthLogoutSuccessHandler;

    @Autowired
    private BCryptPasswordEncoder encoder;

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
                .antMatchers("housing/resources/**");
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PreLoginFilter authenticationFilter() throws Exception {
        PreLoginFilter filter = new PreLoginFilter(successHandler, failureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new EncodingFilter(), ChannelProcessingFilter.class);

        http
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .cors()
                .disable()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/statistics/").permitAll()
                .antMatchers("/admin/**", "/admin", "/userApplications/admin/**").hasAuthority("ADMIN")
                .antMatchers("/users/**", "/requests/**", "/buildings/**").permitAll()
                .antMatchers("/owner/**", "/owner", "/userApplications/owner/**").hasAuthority("OWNER")
                .antMatchers("/tenant/**", "/tenant", "/userApplications/tenant/**").hasAuthority("TENANT")
                .antMatchers("/notification/**").authenticated()
                .antMatchers("/exportBill/**").authenticated()
                .antMatchers("/home/login").anonymous()
                .antMatchers("/home/register").anonymous()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .and()
                .formLogin()
                .loginPage("http://localhost:8000/housing/home/login")
                .loginProcessingUrl("/home/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .logoutUrl("/home/logout")
                .logoutSuccessHandler(oauthLogoutSuccessHandler)
                .logoutRequestMatcher(new AntPathRequestMatcher("/home/logout"))
                .logoutSuccessUrl("http://localhost:8000/housing/home");
               // .invalidateHttpSession(true);
    }

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/
}

