CREATE TABLE tag (
        id BIGSERIAL NOT NULL,
        name VARCHAR(32) NOT NULL,
    UNIQUE(name),
    PRIMARY KEY (id)
);

CREATE TABLE gift_certificate (
        id BIGSERIAL NOT NULL,
        name VARCHAR(32) NOT NULL,
        description VARCHAR(64) NOT NULL,
        price DECIMAL(12,2) NOT NULL,
        duration INT NOT NULL,
        create_date TIMESTAMP NOT NULL,
        last_update_date TIMESTAMP NULL,
    UNIQUE(name),
    PRIMARY KEY (id)
);

CREATE TABLE gift_certificate_tag (
        gift_certificate_id BIGINT NOT NULL,
        tag_id BIGINT NOT NULL,
    CONSTRAINT fk_gift_certificate_tag_id FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id) ON DELETE CASCADE,
    CONSTRAINT fk_tag_gift_certificate_id FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (gift_certificate_id, tag_id)
);
