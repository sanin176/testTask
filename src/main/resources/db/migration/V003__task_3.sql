create table invoice
(
    id                  bigserial       NOT NULL,
    invoice_id          varchar(200)    NOT NULL,
    login               varchar(200)    NOT NULL,
    created_date        timestamp       NOT NULL,
    created_by          varchar(200)    NOT NULL,
    last_modified_date  timestamp       NOT NULL,
    last_modified_by    varchar(200)    NOT NULL,

    CONSTRAINT pk__invoice_id PRIMARY KEY (id)
);
