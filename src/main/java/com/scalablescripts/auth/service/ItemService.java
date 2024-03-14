package com.scalablescripts.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.scalablescripts.auth.data.*;
import com.scalablescripts.auth.error.*;
import dev.samstevens.totp.code.CodeVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ItemService {

    private final UserRepo userRepo;
    private final ItemRepo itemRepo;

    private final String refreshTokenSecret;
    private final Long refreshTokenValidity;

    private final GoogleIdTokenVerifier googleVerifier;

    @Autowired
    public ItemService(UserRepo userRepo,
                       ItemRepo itemRepo,
                       @Value("${application.security.refresh-token-secret}") String refreshTokenSecret,
                       @Value("${application.security.refresh-token-validity}") Long refreshTokenValidity,
                       GoogleIdTokenVerifier googleVerifier) {
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
        this.refreshTokenSecret = refreshTokenSecret;
        this.refreshTokenValidity = refreshTokenValidity;
        this.googleVerifier = googleVerifier;
    }


    public List<Item> items() {
        List<Item> items = itemRepo.findAll();

        return items;
    }
}
