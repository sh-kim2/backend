package com.scalablescripts.auth.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "orders")
@Getter
@Setter
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userTbl;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String address;

    @Column(length = 10, nullable = false)
    private String payment;

    @Column(length = 16)
    private String cardNumber;

    /** items 라는 테이블이 존재하기 때문에 item_list 컬럼으로 만들었다. */
    @Column(name="item_list", length = 500, nullable = false)
    private String items;


}
