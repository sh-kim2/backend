package com.scalablescripts.auth.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CodeVerifier codeVerifier() {
        return new DefaultCodeVerifier(new DefaultCodeGenerator(), new SystemTimeProvider());
    }

    @Bean
    public GoogleIdTokenVerifier googleVerifier(@Value("${application.security.google-client-id}") String googleClientID) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientID+".apps.googleusercontent.com"))
                .build();

//        setAudience()는 android_client_id(백엔드용으로 추정) 대신 web_client_id(프론트 엔드용)를 허용하는 것 같습니다 .
//        다시 한 번 aud와 azp가 다음과 같기 때문에 이는 가능성이 높습니다. 어떤 이유로 신비롭게 전환되었습니다.
//        뿐만 아니라 Arrays.asList()를 사용할 때 문자열 중 하나가 웹 클라이언트 ID이면 작동하지만 setAudience()를 함께 제거하면 작동합니다.

    }

}
