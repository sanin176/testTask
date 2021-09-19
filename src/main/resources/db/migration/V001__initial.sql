-- Account Schema --
CREATE TYPE account_role AS ENUM ('ROLE_OWNER',
    'ROLE_ADMIN',
    'ROLE_USER');

CREATE TABLE account
(
    id                  bigserial                                       NOT NULL,
    login               varchar(255)                                    NOT NULL,
    name                varchar(255)                                    NOT NULL,
    password            varchar(255)                                    NOT NULL,
    surname             varchar(255)                                    NOT NULL,
    last_login_at       timestamp     default CURRENT_TIMESTAMP         NOT NULL,
    created_date        timestamp     default CURRENT_TIMESTAMP         NOT NULL,
    role                account_role  default 'ROLE_USER'::account_role NOT NULL,
    customer_invoice_id varchar(255)                                    NOT NULL,

    CONSTRAINT pk__account_id PRIMARY KEY (id)
);
