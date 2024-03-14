package com.scalablescripts.auth.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "CARTS")
@Getter
@Setter
@Entity
public class Cart {


//    @Id
//    @SequenceGenerator(name = "pay_appln_data_id_seq", sequenceName = "pay_appln_data_id_seq")
//    @GeneratedValue(strategy = SEQUENCE, generator = "pay_appln_data_id_seq")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userTbl;

    @Column
    private Long items;
}
