package com.scalablescripts.auth.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Spring Security 6.1.0부터는 메서드 체이닝의 사용을 지양하고 람다식을 통해 함수형으로 설정하게 지향하고 있습니다.
        // http.csrf().disable(); ==> spring boot 3에서는 http.csrf(AbstractHttpConfigurer::disable)
        // 세션토큰  비활성화
            http
                    .cors(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                //.requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/**").permitAll()
//                .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
        );


//        http.formLogin(fL ->
//                fL.loginPage("/members/login")
//                        .defaultSuccessUrl("/")
//                        .usernameParameter("email")
//                        .failureUrl("/members/login/error")
//        );
//        http.logout(lOut -> {
//            lOut.invalidateHttpSession(true)
//                    .clearAuthentication(true)
//                    .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
//                    .logoutSuccessUrl("/")
//                    .permitAll();
//        });
//
//
//        http.exceptionHandling((exceptionConfig) ->
//                exceptionConfig.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//        );
//        //http.httpBasic(withDefaults());

        return http.build();
    }


}