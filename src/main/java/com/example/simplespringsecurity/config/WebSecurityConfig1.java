package com.example.simplespringsecurity.config;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class WebSecurityConfig1 extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/", "/home").permitAll().anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll()
                .and().openidLogin()
                .and().headers()
                .and().cors().configurationSource(new UrlBasedCorsConfigurationSource())
                .and().sessionManagement()
                .and().portMapper()
                .and().jee()
                .and().x509()
                .and().rememberMe()
                .and().requestCache()
                .and().exceptionHandling()
                .and().securityContext()
                .and().servletApi()
                .and().logout()
                .and().anonymous().authorities()
                .and().saml2Login()
                .and().oauth2Login()
                .and().oauth2Client()
                .and().oauth2ResourceServer()
                .and().requiresChannel()
                .and().httpBasic()
                .and().requestMatchers().and().antMatcher("").regexMatcher("").requestMatcher(null);

        http.authorizeRequests(configurer -> {
            configurer
                    .antMatchers("/", "/home")
                    .permitAll()
                    .anyRequest()
                    .authenticated();
        });

        http.formLogin(configurer -> {
            configurer
                    .loginPage("/login")
                    .usernameParameter("")
                    .passwordParameter("")
                    .successForwardUrl("")
                    .failureForwardUrl("")
                    .permitAll();
        });

        http.logout(configurer -> {
            configurer
                    .logoutUrl("/logout")
                    .permitAll()
                    .permitAll();
        });

        http.oauth2Login(configurer -> {
            configurer
                    .loginPage("")
                    .loginProcessingUrl("")
                    .tokenEndpoint()
                    .accessTokenResponseClient(new DefaultAuthorizationCodeTokenResponseClient())
                    .and()
                    .redirectionEndpoint()
                    .baseUri("")
                    .and()
                    .authorizationEndpoint()
                    .authorizationRequestRepository(new HttpSessionOAuth2AuthorizationRequestRepository())
                    .authorizationRequestResolver(new DefaultOAuth2AuthorizationRequestResolver(null, ""))
                    .baseUri("")
                    .and()
                    .userInfoEndpoint()
                    .userAuthoritiesMapper(new NullAuthoritiesMapper())
                    .oidcUserService(new OidcUserService())
                    .userService(new DefaultOAuth2UserService())
                    .and()
                    .authorizedClientRepository(new HttpSessionOAuth2AuthorizedClientRepository())
                    .authorizedClientService(new InMemoryOAuth2AuthorizedClientService(null))
                    .clientRegistrationRepository(new InMemoryClientRegistrationRepository());
        });

        http.oauth2Client(configurer ->{
            configurer
                    .authorizedClientRepository(new HttpSessionOAuth2AuthorizedClientRepository())
                    .authorizedClientService(new InMemoryOAuth2AuthorizedClientService(null))
                    .clientRegistrationRepository(new InMemoryClientRegistrationRepository())
                    .authorizationCodeGrant()
                    .accessTokenResponseClient(new DefaultAuthorizationCodeTokenResponseClient())
                    .authorizationRequestRepository(new HttpSessionOAuth2AuthorizationRequestRepository())
                    .authorizationRequestResolver(new DefaultOAuth2AuthorizationRequestResolver(null, ""));
        });

        http.oauth2ResourceServer(configurer ->{
            configurer
                    .bearerTokenResolver(new DefaultBearerTokenResolver())
                    .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                    .authenticationEntryPoint(new BasicAuthenticationEntryPoint())
                    .authenticationManagerResolver(new JwtIssuerAuthenticationManagerResolver())
                    .jwt()
                    .authenticationManager(new ProviderManager())
                    .decoder(new NimbusJwtDecoder(new DefaultJWTProcessor<>()))
                    .jwkSetUri("")
                    .jwtAuthenticationConverter(new JwtBearerTokenAuthenticationConverter())
                    .and()
                    .opaqueToken()
                    .authenticationManager(new ProviderManager())
                    .introspectionClientCredentials("", "")
                    .introspectionUri("null")
                    .introspector(new NimbusOpaqueTokenIntrospector("", "",""));
        });

        http
                .antMatcher("")
                .mvcMatcher("")
                .regexMatcher("")
                .requestMatcher(AnyRequestMatcher.INSTANCE);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web
                .addSecurityFilterChainBuilder(null)
                .debug(true)
                .expressionHandler(null)
                .httpFirewall(new StrictHttpFirewall())
                .postBuildAction(null)
                .privilegeEvaluator(null)
                .securityInterceptor(null)
                .ignoring().anyRequest();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth
                .authenticationEventPublisher(null)
                .authenticationProvider(null)
                .eraseCredentials(false)
                .parentAuthenticationManager(null)
                .inMemoryAuthentication()
                .and()
                .jdbcAuthentication()
                .and()
                .ldapAuthentication()
                .and()
                .userDetailsService(null)
                .and();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
