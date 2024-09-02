package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm.service.CustomUserDetailService;

@Configuration
public class SecurityConfigration {

    @Autowired
    CustomUserDetailService customUserDetailService;

    // @Bean
    // public InMemoryUserDetailsManager mudm()
    // {
        
    //     UserDetails user1 = User.withDefaultPasswordEncoder() //Tempprary static user
    //     .username("ranjan")
    //     .password("123456")
    //     .roles("USER")
    //     .build();
        
    //     InMemoryUserDetailsManager udsr = new InMemoryUserDetailsManager(user1);

    //     return udsr;

    // }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       
        httpSecurity.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorize->{
                        authorize.requestMatchers("/user/**").authenticated();
                        authorize.anyRequest().permitAll();
        });

        httpSecurity.formLogin(form->
        
            form.loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/user/dashboard", true)
            .usernameParameter("email")
            .passwordParameter("password")
            .permitAll()
        
            ).logout((logout) -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout=true"));

        return  httpSecurity.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
