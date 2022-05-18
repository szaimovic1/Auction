SELECT setval('address_address_id_seq', (
  SELECT MAX(address_id) FROM address
), true);

SELECT setval('bid_bid_id_seq', (
  SELECT MAX(bid_id) FROM bid
), true);

SELECT setval('category_category_id_seq', (
  SELECT MAX(category_id) FROM category
), true);

SELECT setval('color_color_id_seq', (
  SELECT MAX(color_id) FROM color
), true);

SELECT setval('image_image_id_seq', (
  SELECT MAX(image_id) FROM image
), true);

SELECT setval('phone_number_phone_id_seq', (
  SELECT MAX(phone_id) FROM phone_number
), true);

SELECT setval('product_product_id_seq', (
  SELECT MAX(product_id) FROM product
), true);

SELECT setval('role_role_id_seq', (
  SELECT MAX(role_id) FROM role
), true);

SELECT setval('shipment_shipment_id_seq', (
  SELECT MAX(shipment_id) FROM shipment
), true);

SELECT setval('size_size_id_seq', (
  SELECT MAX(size_id) FROM size
), true);

SELECT setval('app_user_user_id_seq', (
  SELECT MAX(user_id) FROM app_user
), true);
