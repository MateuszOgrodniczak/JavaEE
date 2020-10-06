/*
package com.jee.spring_labs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Bean
    public UserInfoRestTemplateCustomizer customHeader() {
        return restTemplate ->
                restTemplate.getInterceptors().add(new AuthorizationRequestInterceptor());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new EncodingFilter(), ChannelProcessingFilter.class);
              //  .addFilterBefore(new AccessTokenFilter(), ChannelProcessingFilter.class);

        http
                .authorizeRequests()
                .antMatchers("/statistics/").permitAll()
                .antMatchers("/admin/**", "/admin", "/userApplications/admin/**").hasAuthority("ADMIN")
                .antMatchers("/users/**", "/requests/**", "/buildings/**").permitAll()
                .antMatchers("/owner/**", "/owner", "/userApplications/owner/**").hasAuthority("OWNER")
                .antMatchers("/tenant/**", "/tenant", "/userApplications/tenant/**").hasAuthority("TENANT")
               // .antMatchers("/notification/**").authenticated()
                .antMatchers("/home/login").anonymous()
                .antMatchers("/home/register").anonymous()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/resources/**").permitAll();
                //.and()
               // .formLogin()
               // .loginPage("/home/login");
    }

*/
/*    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("housing/resources/**");
    }*//*

}
*/

