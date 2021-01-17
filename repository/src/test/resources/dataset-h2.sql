INSERT INTO tag (name) VALUES
    ('tag1'),
    ('tag2'),
    ('tag3'),
    ('tag4'),
    ('tag5')
;

INSERT INTO gift_certificate (name, description, price, duration, create_date) VALUES
    ('gift1', 'gift1 description', 1.99, 1, now()),
    ('gift2', 'gift2 description', 2.99, 2, now()),
    ('gift3', 'gift3 description', 3.99, 3, now()),
    ('gift4', 'gift4 description', 4.99, 4, now()),
    ('gift5', 'gift5 description', 5.99, 5, now())
;

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1),
    (3, 1),
    (3, 2),
    (4, 5),
    (5, 4)
;
