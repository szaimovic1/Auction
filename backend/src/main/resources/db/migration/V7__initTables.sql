drop table if exists wishlist_assignment;
drop table if exists wishlist;

create table wishlist
(
    wishlist_id bigserial primary key,
    user_id bigint references app_user
);

create table wishlist_assignment
(
    product_id bigint references product,
    wishlist_id bigint references wishlist,
    constraint wishlist_assignment_id primary key (product_id, wishlist_id)
);
