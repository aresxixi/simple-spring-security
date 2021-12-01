package com.example.simplespringsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests(configurer-> {
//            configurer
//                    .antMatchers("/").permitAll()
//                    .anyRequest().authenticated();
//        });
//        http.formLogin(configurer-> {
//            configurer
////                    .loginPage("/login")
//                    .failureHandler((e1,e2,e3)->{
//                        System.out.println(">>>>>>>> login failure");
//                    })
//                    .successHandler((e1,e2,e3)->{
//                        System.out.println(">>>>>>>> login success");
//                    })
//                    .successForwardUrl("/user")
//                    .failureForwardUrl("/user/list");
//
//        });
//        http.logout(configurer-> {
//
//        });
    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
