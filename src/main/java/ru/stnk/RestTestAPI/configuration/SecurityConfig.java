package ru.stnk.RestTestAPI.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select email,password,enable_user from users where email=?")
                .authoritiesByUsernameQuery("select user_email,role_name from user_roles where user_email=?")
                .passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/reg-start").permitAll()
                .antMatchers(HttpMethod.POST, "/reg-start").permitAll()
                .antMatchers(HttpMethod.GET, "/add-role").permitAll()
                .antMatchers(HttpMethod.GET, "/hello").authenticated()
                .antMatchers("/logout").authenticated()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .disable()
                .formLogin()
                .disable()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("SESSION");

    }
}