package com.scalablescripts.auth.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    List<Order> findByUserTblOrderByIdDesc(Long userId);

}
