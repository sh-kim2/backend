package com.scalablescripts.auth.data;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    List<Cart> findByUserTbl(Long userId);

    Cart findByUserTblAndItems(Long userId, Long itemId);

    void deleteByUserTbl(Long userId);
}
