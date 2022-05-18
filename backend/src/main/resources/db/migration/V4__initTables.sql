drop table if exists review;

create table review
(
    review_id bigserial primary key,
    seller_id bigint references app_user,
    customer_id bigint references app_user,
    rating integer CHECK (rating > 0 AND rating < 6) not null
);

insert into review(seller_id, customer_id, rating)
values (2, 1, 4);

insert into review(seller_id, customer_id, rating)
values (2, 3, 4);

insert into review(seller_id, customer_id, rating)
values (2, 5, 4);

insert into review(seller_id, customer_id, rating)
values (2, 6, 5);

insert into review(seller_id, customer_id, rating)
values (2, 7, 1);
