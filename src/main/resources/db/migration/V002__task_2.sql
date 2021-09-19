create table geolocation
(
    id                  bigserial       NOT NULL,
    device_id           varchar(200)    NOT NULL,
    latitude            bigint          NOT NULL,
    longitude           bigint          NOT NULL,
    created_date        timestamp       NOT NULL,
    created_by          varchar(200)    NOT NULL,
    last_modified_date  timestamp       NOT NULL,
    last_modified_by    varchar(200)    NOT NULL,
    login               varchar(200)    NOT NULL,

    CONSTRAINT pk__geolocation_id PRIMARY KEY (id)
);
