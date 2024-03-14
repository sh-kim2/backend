package com.scalablescripts.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.scalablescripts.auth.data.*;
import com.scalablescripts.auth.error.UnauthenticatedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    private final CartRepo cartRepo;

    private final String refreshTokenSecret;
    private final Long refreshTokenValidity;

    private final GoogleIdTokenVerifier googleVerifier;

    @Autowired
    public OrderService(UserRepo userRepo,
                        OrderRepo orderRepo, CartRepo cartRepo,
                        @Value("${application.security.refresh-token-secret}") String refreshTokenSecret,
                        @Value("${application.security.refresh-token-validity}") Long refreshTokenValidity,
                        GoogleIdTokenVerifier googleVerifier) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.refreshTokenSecret = refreshTokenSecret;
        this.refreshTokenValidity = refreshTokenValidity;
        this.googleVerifier = googleVerifier;
    }


    public List<Order> getOrder(String refreshToken) {

        var refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);
        // 로그인을 안했으면 카트등록이 안되게 막아야 한다. front 단에서 막음.
        var user = userRepo.findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(refreshJwt.getUserId(), refreshJwt.getToken(), refreshJwt.getExpiration())
                .orElseThrow(UnauthenticatedError::new);

        var orders = orderRepo.findByUserTblOrderByIdDesc(user.getId());
        return orders;
    }

    public void pushOrders(String name, String address, String payment, String cardNumber, String items, String refreshToken) {

        var refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);
        // 로그인을 안했으면 카트등록이 안되게 막아야 한다. front 단에서 막음.
        var user = userRepo.findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(refreshJwt.getUserId(), refreshJwt.getToken(), refreshJwt.getExpiration())
                .orElseThrow(UnauthenticatedError::new);

        Order newOrder = new Order();

        newOrder.setUserTbl(user.getId());
        newOrder.setName(name);
        newOrder.setAddress(address);
        newOrder.setPayment(payment);
        newOrder.setCardNumber(cardNumber);
        newOrder.setItems(items);

        orderRepo.save(newOrder);
        cartRepo.deleteByUserTbl(user.getId());
    }

}
