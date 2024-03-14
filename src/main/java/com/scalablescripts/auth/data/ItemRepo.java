package com.scalablescripts.auth.data;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Integer> {

    List<Item> findByIdIn(List<Long> ids);
}
