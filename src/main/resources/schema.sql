-- drop table IF EXISTS Users;
-- drop table IF EXISTS Images;
-- drop table IF EXISTS Items;

CREATE TABLE IF NOT EXISTS Users
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username        varchar(50),
    email           varchar(50)                             not null,
    image_id        bigint,
    user_role       varchar,
    "password"      text,
    user_image      bigint,
    is_enabled      bool,
    activation_code text,

    UNIQUE (email),
    constraint USERS_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Images
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    filename     varchar(200),
    size         bigint,
    content_type varchar(50),
    bytes        bytea,
    user_id      bigint,

    constraint IMAGES_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Items
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title       varchar(200),
    description text,
    price       bigint,
    owner_id    integer,
    posted      TIMESTAMP WITHOUT TIME ZONE,
    image_id    bigint,

    constraint ITEMS_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Images_items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    filename     varchar(200),
    size         bigint,
    content_type varchar(50),
    bytes        bytea,
    item_id      bigint,

    constraint IMAGES_ITEMS_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Images_items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    filename     varchar(200),
    size         bigint,
    content_type varchar(50),
    bytes        bytea,
    item_id      bigint,

    constraint IMAGES_ITEMS_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Users_images
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    filename     varchar(200),
    size         bigint,
    content_type varchar(50),
    path         text,
    user_id      bigint,

    constraint Users_images_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Items_images
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    filename     varchar(200),
    size         bigint,
    content_type varchar(50),
    path         text,
    item_id      bigint,

    constraint Items_images_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Verification_token
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token       varchar(255),
    user_id     bigint,
    expiry_date TIMESTAMP WITHOUT TIME ZONE,

    constraint Verification_token_PK
        primary key (id)
);

CREATE TABLE IF NOT EXISTS Confirmation_token
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    confirmation_token varchar(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE,
    user_id            bigint,

    constraint Confirmation_token_PK
        primary key (id)
);