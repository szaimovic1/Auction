drop table if exists color_assignment;
drop table if exists size_assignment;
drop table if exists image;
drop table if exists bid;
drop table if exists product;
drop table if exists color;
drop table if exists size;
drop table if exists category;
drop table if exists role_assignment;
drop table if exists role;
drop table if exists app_user cascade;
drop table if exists shipment;
drop table if exists address;
drop table if exists credit_card;
drop table if exists phone_number;
--for tokens

create table role
(
    role_id bigserial primary key,
    role varchar(255) unique
);

create table address
(
    address_id bigserial primary key,
    street varchar(255) not null,
    city varchar(255) not null,
    zip_code bigint not null,
    state varchar(255),
    country varchar(255) not null
);

create table credit_card
(
    card_id bigserial primary key,
    name varchar(255) not null,
    number varchar(255) not null,
    expiration_month integer not null,
    expiration_year integer not null,
    cvc_cvv integer not null
);

create table phone_number
(
    phone_id bigserial primary key,
    number varchar(255) not null,
    is_valid boolean
);

create table shipment
(
    shipment_id bigserial primary key,
    address_id bigint references address,
    phone_id bigint references phone_number,
    email varchar(255)
);

create table app_user
(
    user_id bigserial primary key,
    address_id bigint references address,
    card_id bigint references credit_card,
    phone_id bigint references phone_number,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    email varchar(255) unique,
    password varchar(255),
    image varchar(255),
    is_enabled boolean,
    date_of_birth timestamp
);

create table role_assignment
(
    user_id bigserial references app_user,
    role_id bigserial references role,
    constraint role_assignment_id primary key (user_id, role_id)
);

create table category
(
    category_id bigserial primary key,
    subcategory_id bigint references category,
    name varchar(20) not null
);

create table product
(
    product_id bigserial primary key,
    seller_id bigserial references app_user,
    category_id bigint references category,
    shipment_id bigint references shipment,
    name varchar(20) not null,
    details varchar(255),
    initial_price numeric(10, 2) not null,
    start_date timestamp not null,
    end_date timestamp not null
);

create table size
(
    size_id bigserial primary key,
    size varchar(255) unique
);

create table color
(
    color_id bigserial primary key,
    color varchar(255) unique
);

create table size_assignment
(
    product_id bigserial references product,
    size_id bigserial references size,
    constraint size_assignment_id primary key (product_id, size_id)
);

create table color_assignment
(
    product_id bigserial references product,
    color_id bigserial references color,
    constraint color_assignment_id primary key (product_id, color_id)
);

create table image
(
    image_id bigserial primary key,
    product_id bigserial references product,
    image_url varchar(255) not null
);

create table bid
(
    bid_id bigserial primary key,
    product_id bigserial references product,
    customer_id bigserial references app_user,
    value numeric(10, 2) not null,
    bid_time timestamp not null
);


insert into role(role_id, role) values (1, 'ROLE_USER');
insert into role(role_id, role) values (2, 'ROLE_ADMIN');

insert into address(address_id, street, city, zip_code, state, country)
values (1, 'Ul Dervis ef. Korkuta', 'Travnik', 72270, 'FBiH', 'BiH');

insert into app_user(user_id, first_name, last_name, email, is_enabled, image)
values (1, 'Sara', 'Zaimovic', 'sarazaimovic@gmail.com', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824853/kkb9g4govk935s7mbom3.webp');
insert into app_user(user_id, address_id, first_name, last_name, email, password, is_enabled, image, date_of_birth)
values (2, 1, 'Mehdi', 'Zaimovic', 'mehdizaimovic@gmail.com', '$2a$10$UkfYm1g0KxDDreKTVVfDneZN1GHxA.I61lqYOoH.DXgzonngreObe', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824958/rtlvehydqbfdc9c2jivq.jpg', '2004-01-16');
insert into app_user(user_id, first_name, last_name, email, is_enabled, image)
values (3, 'Sara1', 'Zaimovic', 'sarazaimovic1@gmail.com', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824853/kkb9g4govk935s7mbom3.webp');
insert into app_user(user_id, first_name, last_name, email, password, is_enabled, image)
values (4, 'Mehdi1', 'Zaimovic', 'mehdizaimovic1@gmail.com', '$2a$10$UkfYm1g0KxDDreKTVVfDneZN1GHxA.I61lqYOoH.DXgzonngreObe', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824958/rtlvehydqbfdc9c2jivq.jpg');
insert into app_user(user_id, first_name, last_name, email, is_enabled, image)
values (5, 'Sara2', 'Zaimovic', 'sarazaimovic2@gmail.com', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824853/kkb9g4govk935s7mbom3.webp');
insert into app_user(user_id, first_name, last_name, email, password, is_enabled, image)
values (6, 'Mehdi2', 'Zaimovic', 'mehdizaimovic2@gmail.com', '$2a$10$UkfYm1g0KxDDreKTVVfDneZN1GHxA.I61lqYOoH.DXgzonngreObe', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824958/rtlvehydqbfdc9c2jivq.jpg');
insert into app_user(user_id, first_name, last_name, email, is_enabled, image)
values (7, 'Sara3', 'Zaimovic', 'sarazaimovic3@gmail.com', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824853/kkb9g4govk935s7mbom3.webp');
insert into app_user(user_id, first_name, last_name, email, password, is_enabled, image)
values (8, 'Mehdi3', 'Zaimovic', 'mehdizaimovic3@gmail.com', '$2a$10$UkfYm1g0KxDDreKTVVfDneZN1GHxA.I61lqYOoH.DXgzonngreObe', TRUE, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824958/rtlvehydqbfdc9c2jivq.jpg');

insert into role_assignment (user_id, role_id) values (1,1);
insert into role_assignment (user_id, role_id) values (2,1);


insert into category (category_id, subcategory_id, name)
values (1, null, 'Women');
insert into category (category_id, subcategory_id, name)
values (2, null, 'Men');
insert into category (category_id, subcategory_id, name)
values (3, null, 'Kids');
insert into category (category_id, subcategory_id, name)
values (4, null, 'Home');
insert into category (category_id, subcategory_id, name)
values (5, null, 'Art');
insert into category (category_id, subcategory_id, name)
values (6, null, 'Computers');

insert into category (category_id, subcategory_id, name)
values (7, 1, 'Accessories');
insert into category (category_id, subcategory_id, name)
values (8, 1, 'Bags');
insert into category (category_id, subcategory_id, name)
values (9, 1, 'Clothes');
insert into category (category_id, subcategory_id, name)
values (10, 1, 'Bad & Bath');
insert into category (category_id, subcategory_id, name)
values (11, 1, 'Swimming costume');
insert into category (category_id, subcategory_id, name)
values (12, 1, 'Spot Tops & Shoes');

insert into category (category_id, subcategory_id, name)
values (13, 2, 'Accessories');
insert into category (category_id, subcategory_id, name)
values (14, 2, 'Clothes');
insert into category (category_id, subcategory_id, name)
values (15, 2, 'Sleepwear');
insert into category (category_id, subcategory_id, name)
values (16, 2, 'Swimming costume');
insert into category (category_id, subcategory_id, name)
values (17, 2, 'Shoes');

insert into category (category_id, subcategory_id, name)
values (18, 3, 'Toys');
insert into category (category_id, subcategory_id, name)
values (19, 3, 'Clothes');
insert into category (category_id, subcategory_id, name)
values (20, 3, 'Sleepwear');
insert into category (category_id, subcategory_id, name)
values (21, 3, 'Swimwear');
insert into category (category_id, subcategory_id, name)
values (22, 3, 'Shoes');

insert into category (category_id, subcategory_id, name)
values (23, 4, 'Decor');
insert into category (category_id, subcategory_id, name)
values (24, 4, 'Furniture');
insert into category (category_id, subcategory_id, name)
values (25, 4, 'Appliances');

insert into category (category_id, subcategory_id, name)
values (26, 6, 'PC');
insert into category (category_id, subcategory_id, name)
values (27, 6, 'Phones');
insert into category (category_id, subcategory_id, name)
values (28, 6, 'Peripheral');
insert into category (category_id, subcategory_id, name)
values (29, 6, 'Accessories');


insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (1, 2, 7, 'Pearl bracelet', 'Pretty, feminine, for special occasions.', 50, '2022-02-21 11:00:00', '2022-06-25 11:00:00');
insert into image (image_id, product_id, image_url)
values (1, 1, 'https://res.cloudinary.com/abh-auction/image/upload/v1648822423/p5yxyymtk3mtai19q4tu.png');
insert into image (image_id, product_id, image_url)
values (2, 1, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823078/r4uhestpgxvpfg8idbr2.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (2, 1, 9, 'Skinny jeans', 'Fits like a glove.', 20, '2022-02-18 11:15:00', '2022-06-02 11:30:00');
insert into image (image_id, product_id, image_url)
values (3, 2, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823200/djfdisdxkxc8w8p5xsfd.jpg');
insert into image (image_id, product_id, image_url)
values (4, 2, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823304/pbouzexpfsodgw22btmh.png');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (3, 1, 15, 'Pajama', 'The most comfortable for the best sleep.', 30.55, '2022-02-20 13:30:00', '2022-06-20 11:30:00');
insert into image (image_id, product_id, image_url)
values (5, 3, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823399/qmimxpjthvtixfdkgr3b.jpg');
insert into image (image_id, product_id, image_url)
values (6, 3, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823460/ohfaphcweufrhbefl1fx.jpg');
insert into image (image_id, product_id, image_url)
values (7, 3, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823522/gexe2jfnmytvaq0mlssf.webp');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (4, 1, 26, 'PC', 'Great specifications.', 1000, '2022-02-19 11:30:00', '2022-06-26 11:30:00');
insert into image (image_id, product_id, image_url)
values (8, 4, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823590/jelx4ypoyg4jsuhber6n.jpg');
insert into image (image_id, product_id, image_url)
values (9, 4, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823688/zlfudee1sfhm1ajdprgw.webp');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (5, 2, 18, 'Doll house', 'Every little girl dreams about such an amazing piece.', 100, '2022-02-15 17:30:00', '2022-06-27 11:30:00');
insert into image (image_id, product_id, image_url)
values (10, 5, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824184/nml81hyvlmc0gn9lv1cz.jpg');
insert into image (image_id, product_id, image_url)
values (11, 5, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824306/d4zclkypukwxduvqwsem.jpg');
insert into image (image_id, product_id, image_url)
values (12, 5, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824357/gd94npqgnsevpzhvq9ga.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (6, 2, 18, 'Swing', 'Beautiful swing for your kids.', 200, '2022-02-17 12:30:00', '2022-06-25 10:30:00');
insert into image (image_id, product_id, image_url)
values (13, 6, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824430/egqnpmg5fiah2u17neuj.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (7, 2, 18, 'Car', 'Learn to drive early on.', 300.6, '2022-02-27 12:30:00', '2022-06-25 13:30:00');
insert into image (image_id, product_id, image_url)
values (14, 7, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824499/bzb4jue7woyicfgp3szo.webp');
insert into image (image_id, product_id, image_url)
values (15, 7, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824593/mdrq1h9xiloftosk6xbl.webp');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (8, 2, 18, 'A Doll', 'Charming to play with and have tea parties.', 10, '2022-02-16 10:30:00', '2022-06-26 09:30:00');
insert into image (image_id, product_id, image_url)
values (16, 8, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824656/xjh5wrxezhwydsgr0yga.jpg');
insert into image (image_id, product_id, image_url)
values (17, 8, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824747/r0y8barnidkppfn7uyzr.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (9, 1, 7, 'Pearl bracelet 2', 'Pretty, feminine, for special occasions.', 50, '2022-02-21 11:00:00', '2022-06-25 11:00:00');
insert into image (image_id, product_id, image_url)
values (18, 9, 'https://res.cloudinary.com/abh-auction/image/upload/v1648822423/p5yxyymtk3mtai19q4tu.png');
insert into image (image_id, product_id, image_url)
values (19, 9, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823078/r4uhestpgxvpfg8idbr2.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (10, 1, 9, 'Skinny jeans 2', 'Fits like a glove.', 20, '2022-02-18 11:15:00', '2022-06-02 11:30:00');
insert into image (image_id, product_id, image_url)
values (20, 10, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823200/djfdisdxkxc8w8p5xsfd.jpg');
insert into image (image_id, product_id, image_url)
values (21, 10, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823304/pbouzexpfsodgw22btmh.png');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (11, 1, 15, 'Pajama 2', 'The most comfortable for the best sleep.', 30.55, '2022-02-20 13:30:00', '2022-06-20 11:30:00');
insert into image (image_id, product_id, image_url)
values (22, 11, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823399/qmimxpjthvtixfdkgr3b.jpg');
insert into image (image_id, product_id, image_url)
values (23, 11, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823460/ohfaphcweufrhbefl1fx.jpg');
insert into image (image_id, product_id, image_url)
values (24, 11, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823522/gexe2jfnmytvaq0mlssf.webp');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (12, 1, 26, 'PC 2', 'Great specifications.', 1000, '2022-02-19 11:30:00', '2022-06-26 11:30:00');
insert into image (image_id, product_id, image_url)
values (25, 12, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823590/jelx4ypoyg4jsuhber6n.jpg');
insert into image (image_id, product_id, image_url)
values (26, 12, 'https://res.cloudinary.com/abh-auction/image/upload/v1648823688/zlfudee1sfhm1ajdprgw.webp');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (13, 2, 18, 'Doll house 2', 'Every little girl dreams about such an amazing piece.', 100, '2022-02-15 17:30:00', '2022-06-27 11:30:00');
insert into image (image_id, product_id, image_url)
values (27, 13, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824184/nml81hyvlmc0gn9lv1cz.jpg');
insert into image (image_id, product_id, image_url)
values (28, 13, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824306/d4zclkypukwxduvqwsem.jpg');
insert into image (image_id, product_id, image_url)
values (29, 13, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824357/gd94npqgnsevpzhvq9ga.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (14, 2, 18, 'Swing 2', 'Beautiful swing for your kids.', 200, '2022-02-17 12:30:00', '2022-06-25 10:30:00');
insert into image (image_id, product_id, image_url)
values (30, 14, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824430/egqnpmg5fiah2u17neuj.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (15, 2, 18, 'Car 2', 'Learn to drive early on.', 300.6, '2022-02-27 12:30:00', '2022-06-25 13:30:00');
insert into image (image_id, product_id, image_url)
values (31, 15, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824499/bzb4jue7woyicfgp3szo.webp');
insert into image (image_id, product_id, image_url)
values (32, 15, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824593/mdrq1h9xiloftosk6xbl.webp');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (16, 2, 18, 'A Doll 2', 'Charming to play with and have tea parties.', 10, '2022-02-16 10:30:00', '2022-06-26 09:30:00');
insert into image (image_id, product_id, image_url)
values (33, 16, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824747/r0y8barnidkppfn7uyzr.jpg');
insert into image (image_id, product_id, image_url)
values (34, 16, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824656/xjh5wrxezhwydsgr0yga.jpg');

insert into product (product_id, seller_id, category_id, name, details, initial_price, start_date, end_date)
values (17, 2, 18, 'A Doll inactive', 'Charming to play with and have tea parties.', 10, '2022-02-16 10:30:00', '2022-04-01 09:30:00');
insert into image (image_id, product_id, image_url)
values (35, 17, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824747/r0y8barnidkppfn7uyzr.jpg');
insert into image (image_id, product_id, image_url)
values (36, 17, 'https://res.cloudinary.com/abh-auction/image/upload/v1648824656/xjh5wrxezhwydsgr0yga.jpg');


insert into size(size_id, size) values (1, 'Small');
insert into size(size_id, size) values (2, 'Medium');
insert into size(size_id, size) values (3, 'Large');
insert into size(size_id, size) values (4, 'Extra Large');

insert into color(color_id, color) values (1, 'Black');
insert into color(color_id, color) values (2, 'White');
insert into color(color_id, color) values (3, 'Red');
insert into color(color_id, color) values (4, 'Blue');
insert into color(color_id, color) values (5, 'Green');
insert into color(color_id, color) values (6, 'Orange');


insert into size_assignment (product_id, size_id) values (1,1);
insert into size_assignment (product_id, size_id) values (1,2);
insert into color_assignment (product_id, color_id) values (1,2);

insert into size_assignment (product_id, size_id) values (2,1);
insert into size_assignment (product_id, size_id) values (2,2);
insert into size_assignment (product_id, size_id) values (2,3);
insert into size_assignment (product_id, size_id) values (2,4);
insert into color_assignment (product_id, color_id) values (2,1);
insert into color_assignment (product_id, color_id) values (2,4);

insert into size_assignment (product_id, size_id) values (3,1);
insert into size_assignment (product_id, size_id) values (3,4);
insert into color_assignment (product_id, color_id) values (3,4);
insert into color_assignment (product_id, color_id) values (3,5);
insert into color_assignment (product_id, color_id) values (3,6);

insert into size_assignment (product_id, size_id) values (4,2);
insert into color_assignment (product_id, color_id) values (4,1);

insert into size_assignment (product_id, size_id) values (5,3);
insert into size_assignment (product_id, size_id) values (5,4);
insert into color_assignment (product_id, color_id) values (5,3);
insert into color_assignment (product_id, color_id) values (5,4);
insert into color_assignment (product_id, color_id) values (5,5);

insert into size_assignment (product_id, size_id) values (6,2);
insert into size_assignment (product_id, size_id) values (6,3);
insert into color_assignment (product_id, color_id) values (6,1);
insert into color_assignment (product_id, color_id) values (6,2);
insert into color_assignment (product_id, color_id) values (6,3);
insert into color_assignment (product_id, color_id) values (6,4);
insert into color_assignment (product_id, color_id) values (6,5);
insert into color_assignment (product_id, color_id) values (6,6);

insert into size_assignment (product_id, size_id) values (7,4);
insert into color_assignment (product_id, color_id) values (7,1);
insert into color_assignment (product_id, color_id) values (7,2);

insert into size_assignment (product_id, size_id) values (8,3);
insert into color_assignment (product_id, color_id) values (8,3);

insert into size_assignment (product_id, size_id) values (9,1);
insert into size_assignment (product_id, size_id) values (9,2);
insert into color_assignment (product_id, color_id) values (9,2);

insert into size_assignment (product_id, size_id) values (10,1);
insert into size_assignment (product_id, size_id) values (10,2);
insert into size_assignment (product_id, size_id) values (10,3);
insert into size_assignment (product_id, size_id) values (10,4);
insert into color_assignment (product_id, color_id) values (10,1);
insert into color_assignment (product_id, color_id) values (10,4);

insert into size_assignment (product_id, size_id) values (11,1);
insert into size_assignment (product_id, size_id) values (11,4);
insert into color_assignment (product_id, color_id) values (11,4);
insert into color_assignment (product_id, color_id) values (11,5);
insert into color_assignment (product_id, color_id) values (11,6);

insert into size_assignment (product_id, size_id) values (12,2);
insert into color_assignment (product_id, color_id) values (12,1);

insert into size_assignment (product_id, size_id) values (13,3);
insert into size_assignment (product_id, size_id) values (13,4);
insert into color_assignment (product_id, color_id) values (13,3);
insert into color_assignment (product_id, color_id) values (13,4);
insert into color_assignment (product_id, color_id) values (13,5);

insert into size_assignment (product_id, size_id) values (14,2);
insert into size_assignment (product_id, size_id) values (14,3);
insert into color_assignment (product_id, color_id) values (14,1);
insert into color_assignment (product_id, color_id) values (14,2);
insert into color_assignment (product_id, color_id) values (14,3);
insert into color_assignment (product_id, color_id) values (14,4);
insert into color_assignment (product_id, color_id) values (14,5);
insert into color_assignment (product_id, color_id) values (14,6);

insert into size_assignment (product_id, size_id) values (15,4);
insert into color_assignment (product_id, color_id) values (15,1);
insert into color_assignment (product_id, color_id) values (15,2);

insert into size_assignment (product_id, size_id) values (16,3);
insert into color_assignment (product_id, color_id) values (16,3);

insert into size_assignment (product_id, size_id) values (17,3);
insert into color_assignment (product_id, color_id) values (17,3);


insert into image (image_id, product_id, image_url)
values (37, 1, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825067/nxa6wojgoqgvi84z76g6.jpg');
insert into image (image_id, product_id, image_url)
values (38, 1, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825106/jighcuobg0j4zaau7gho.jpg');
insert into image (image_id, product_id, image_url)
values (39, 1, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825155/ilkdkxlbcd2repoc9zq2.jpg');

insert into image (image_id, product_id, image_url)
values (40, 2, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825201/qyjvnynxaflopw89xpj4.webp');
insert into image (image_id, product_id, image_url)
values (41, 2, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825238/ptmztyb5ybkhi8iyqrlw.webp');
insert into image (image_id, product_id, image_url)
values (42, 2, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825288/iwnzgggqplmy24ikyl3t.webp');

insert into image (image_id, product_id, image_url)
values (43, 3, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825348/lm1518u8ohjuxkjbghsn.jpg');

insert into image (image_id, product_id, image_url)
values (44, 4, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825393/drqjip5ocd3tocithp16.jpg');

insert into image (image_id, product_id, image_url)
values (45, 5, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825447/fyeavsoqyvmfkiimdqfw.webp');
insert into image (image_id, product_id, image_url)
values (46, 5, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825482/z2vdueiiz0mveeszynvn.jpg');

insert into image (image_id, product_id, image_url)
values (47, 6, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825547/r44rforho2ckbbwzfgxl.webp');
insert into image (image_id, product_id, image_url)
values (48, 6, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825582/t9xsoezv67yjnj1lnrmw.webp');
insert into image (image_id, product_id, image_url)
values (49, 6, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825608/b7tnle6aibaw8qijsvxq.webp');
insert into image (image_id, product_id, image_url)
values (50, 6, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825640/qsdqwrkuw0x68apws26b.webp');
insert into image (image_id, product_id, image_url)
values (51, 6, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825670/e5sygbinptli4m2mt2vs.webp');

insert into image (image_id, product_id, image_url)
values (52, 7, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825751/ejckpibrw1jvbrshevaq.jpg');
insert into image (image_id, product_id, image_url)
values (53, 7, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825779/yljmq0bfqx7nzsvpkiba.jpg');
insert into image (image_id, product_id, image_url)
values (54, 7, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825819/hwpxmqbql1kpesgwnpiz.jpg');

insert into image (image_id, product_id, image_url)
values (55, 8, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825857/xsjcpflpam4cgwyprfkx.jpg');
insert into image (image_id, product_id, image_url)
values (56, 8, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825886/bhqrgjf9owt3714nsyg7.jpg');
insert into image (image_id, product_id, image_url)
values (57, 8, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825917/asjuvhhiqr97lxoblhok.jpg');
insert into image (image_id, product_id, image_url)
values (58, 8, 'https://res.cloudinary.com/abh-auction/image/upload/v1648825950/wcw7puvppkko3wy5gmas.jpg');


insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (1, 1, 1, 55, '2022-02-25 11:10:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (2, 1, 2, 65, '2022-02-25 12:10:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (3, 1, 3, 65.5, '2022-02-27 11:10:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (4, 1, 4, 80, '2022-03-01 16:35:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (14, 1, 5, 81, '2022-03-02 11:10:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (15, 1, 6, 82, '2022-03-02 17:10:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (16, 1, 7, 83, '2022-03-15 11:10:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (17, 1, 8, 90, '2022-03-16 11:10:00');

insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (5, 2, 1, 25, '2022-02-18 11:25:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (6, 2, 2, 28, '2022-02-18 11:26:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (7, 2, 3, 29, '2022-02-18 11:30:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (8, 2, 4, 50, '2022-02-18 11:35:00');;
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (9, 2, 5, 51, '2022-02-18 11:45:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (10, 2, 6, 52, '2022-02-18 11:46:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (11, 2, 7, 53.67, '2022-02-18 12:00:00');

insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (12, 3, 2, 40, '2022-03-13 13:30:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (13, 3, 1, 100, '2022-03-14 13:30:00');

insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (18, 4, 1, 1050, '2022-02-20 11:30:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (19, 4, 2, 1300, '2022-02-21 12:30:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (20, 4, 5, 1500, '2022-02-22 17:30:00');

insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (21, 5, 2, 105, '2022-02-15 18:30:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (22, 5, 1, 120, '2022-02-15 18:35:00');
insert into bid (bid_id, product_id, customer_id, value, bid_time)
values (23, 5, 3, 130, '2022-02-15 18:40:00');


