drop table if exists payment;

create table payment
(
    payment_id bigserial primary key,
    payment_intent_id varchar(255) unique not null,
    payment_intent_client_secret varchar(255) unique not null,
    payer_id bigint references app_user,
    product_id bigint references product,
    price numeric(10, 2) not null,
    is_finalized boolean
);


insert into product (seller_id, category_id, name, details, initial_price, start_date, end_date)
values (2, 18, 'Car inactive', 'Learn to drive early on.', 300.6, '2022-02-27 12:30:00', '2022-03-25 13:30:00');
insert into image (product_id, image_url)
values ((SELECT product_id FROM product WHERE name = 'Car inactive'), 'https://res.cloudinary.com/abh-auction/image/upload/v1648824499/bzb4jue7woyicfgp3szo.webp');
insert into image (product_id, image_url)
values ((SELECT product_id FROM product WHERE name = 'Car inactive'), 'https://res.cloudinary.com/abh-auction/image/upload/v1648824593/mdrq1h9xiloftosk6xbl.webp');

insert into product (seller_id, category_id, name, details, initial_price, start_date, end_date)
values (2, 7, 'Bracelet inactive', 'Pretty, feminine, for special occasions.', 50, '2022-02-21 11:00:00', '2022-03-25 11:00:00');
insert into image (product_id, image_url)
values ((SELECT product_id FROM product WHERE name = 'Bracelet inactive'), 'https://res.cloudinary.com/abh-auction/image/upload/v1648822423/p5yxyymtk3mtai19q4tu.png');
insert into image (product_id, image_url)
values ((SELECT product_id FROM product WHERE name = 'Bracelet inactive'), 'https://res.cloudinary.com/abh-auction/image/upload/v1648823078/r4uhestpgxvpfg8idbr2.jpg');

insert into product (seller_id, category_id, name, details, initial_price, start_date, end_date)
values (3, 15, 'Pajama inactive', 'The most comfortable for the best sleep.', 30.55, '2022-02-20 13:30:00', '2022-03-20 11:30:00');
insert into image (product_id, image_url)
values ((SELECT product_id FROM product WHERE name = 'Pajama inactive'), 'https://res.cloudinary.com/abh-auction/image/upload/v1648823399/qmimxpjthvtixfdkgr3b.jpg');
insert into image (product_id, image_url)
values ((SELECT product_id FROM product WHERE name = 'Pajama inactive'), 'https://res.cloudinary.com/abh-auction/image/upload/v1648823460/ohfaphcweufrhbefl1fx.jpg');
insert into image (product_id, image_url)
values ((SELECT product_id FROM product WHERE name = 'Pajama inactive'), 'https://res.cloudinary.com/abh-auction/image/upload/v1648823522/gexe2jfnmytvaq0mlssf.webp');


insert into bid (product_id, customer_id, value, bid_time)
values (17, 4, 40, '2022-03-01 13:30:00');

insert into bid (product_id, customer_id, value, bid_time)
values ((SELECT product_id FROM product WHERE name = 'Car inactive'), 4, 301, '2022-03-05 13:30:00');

insert into bid (product_id, customer_id, value, bid_time)
values ((SELECT product_id FROM product WHERE name = 'Bracelet inactive'), 4, 55, '2022-02-25 13:30:00');

insert into bid (product_id, customer_id, value, bid_time)
values ((SELECT product_id FROM product WHERE name = 'Pajama inactive'), 4, 42, '2022-03-15 13:30:00');
