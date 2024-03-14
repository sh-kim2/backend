create table if not exists user_tbl
(
    id         identity     not null,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(255) not null,
    password   varchar(255) not null
);

alter table if exists user_tbl
    add constraint if not exists uq_email unique (email);

create table if not exists token
(
    id            identity     not null,
    refresh_token VARCHAR(255) NOT NULL,
    issue_at      TIMESTAMP    NOT NULL,
    expired_at    TIMESTAMP    NOT NULL,
    user_tbl          BIGINT       NOT NULL,
    constraint fk_token_user_tbl foreign key (USER_TBL) references user_tbl (id)
);

create table if not exists password_recovery
(
    id       identity     not null,
    token    varchar(255) not null,
    user_tbl bigint       not null,
    constraint fk_password_recovery_user_tbl foreign key (user_tbl) references user_tbl (id)
);

alter table if exists user_tbl
    add column if not exists tfa_secret varchar(255) default '';

create table if not exists items
(
    id           identity     not null,
    name         varchar(50)  not null,
    img_path     varchar(100) null,
    price        int          null,
    discount_per int          null
);

create table if not exists carts
(
    id           identity     not null,
    user_tbl     BIGINT       NOT NULL,
    items        BIGINT       NOT NULL,
    constraint fk_carts_user_tbl foreign key (USER_TBL) references user_tbl (id),
    constraint fk_carts_items foreign key (items) references items (id)
);

create table if not exists orders
(
    id           identity     not null,
    user_tbl     BIGINT       NOT NULL,
    name        varchar(50)  not null,
    address     varchar(500) not null,
    payment     varchar(10)  not null,
    card_number varchar(16)  null,
    item_list       varchar(500) not null
);
