package com.scalablescripts.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.scalablescripts.auth.data.*;
import com.scalablescripts.auth.error.EmailAlreadyExistsError;
import com.scalablescripts.auth.error.InvalidCredentialsError;
import com.scalablescripts.auth.error.InvalidLinkError;
import com.scalablescripts.auth.error.UnauthenticatedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class CartService {

    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final ItemRepo itemRepo;
    private final String refreshTokenSecret;
    private final Long refreshTokenValidity;

    private final GoogleIdTokenVerifier googleVerifier;


    @Autowired
    public CartService(UserRepo userRepo,
                       CartRepo cartRepo,
                       ItemRepo itemRepo,
                       @Value("${application.security.refresh-token-secret}") String refreshTokenSecret,
                       @Value("${application.security.refresh-token-validity}") Long refreshTokenValidity,
                       GoogleIdTokenVerifier googleVerifier) {
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.refreshTokenSecret = refreshTokenSecret;
        this.refreshTokenValidity = refreshTokenValidity;
        this.googleVerifier = googleVerifier;
    }

    public List<Item> cartItems(String refreshToken) {

        var refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);

        var user = userRepo.findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(refreshJwt.getUserId(), refreshJwt.getToken(), refreshJwt.getExpiration())
                .orElseThrow(UnauthenticatedError::new);

        List<Cart> carts = cartRepo.findByUserTbl(user.getId());
        List<Long> itemIds = carts.stream().map(Cart::getItems).toList();
        List<Item> items = itemRepo.findByIdIn(itemIds);


        return items;
    }

    public void pushCartItem(Long itemId, String refreshToken) {

        var refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);
        // 로그인을 안했으면 카트등록이 안되게 막아야 한다. front 단에서 막음.
        var user = userRepo.findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(refreshJwt.getUserId(), refreshJwt.getToken(), refreshJwt.getExpiration())
                .orElseThrow(UnauthenticatedError::new);

        Cart cart = cartRepo.findByUserTblAndItems(user.getId(), itemId);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUserTbl(user.getId());
            newCart.setItems(itemId);
            cartRepo.save(newCart);
        }
    }



    public void removeCartItem(Long itemId, String refreshToken) {

        var refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);

        var user = userRepo.findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(refreshJwt.getUserId(), refreshJwt.getToken(), refreshJwt.getExpiration())
                .orElseThrow(UnauthenticatedError::new);


        Cart cart = cartRepo.findByUserTblAndItems(user.getId(), itemId);

        cartRepo.delete(cart);
    }

}
