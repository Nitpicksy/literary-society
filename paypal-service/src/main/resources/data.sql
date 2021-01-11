-- SUBSCRIPTION PLANS

-- Default LU Merchant
insert into subscription_plan (cancelurl, frequency_count, frequency_unit, merchant_client_id,
                                              merchant_client_secret, plan_description, plan_id, plan_name, price,
                                              product_category, product_name, product_type, successurl)
values ('https://www.literary-society.com:3000', 3, 'MONTH',
        'AZRZ5NKCNB4ZkLdmx-o_SZIL3lTbGaKd0tiX2dyVaNMfEqSIMVZJPNjgcZKmbGLNUy17Z6gOi9jZIj4R',
        'EGU9nvBhzJPHupEsi6cFTIkDfE4uKIz7fnQZsXpvWkMh5InMVlbwjGqp3AFJdBI9qeLH51I4WCuluWOL',
        'Create publication requests and publish books via Literary Society.', 'P-74S64158P6034971TL75S45Y',
        'Writer Membership', 2000.0, 'BOOKS_MANUSCRIPTS', 'Book Publishing Option', 'DIGITAL',
        'https://www.literary-society.com:3000/subscription/success');
insert into subscription_plan (cancelurl, frequency_count, frequency_unit, merchant_client_id,
                                              merchant_client_secret, plan_description, plan_id, plan_name, price,
                                              product_category, product_name, product_type, successurl)
values ('https://www.literary-society.com:3000', 1, 'MONTH',
        'AZRZ5NKCNB4ZkLdmx-o_SZIL3lTbGaKd0tiX2dyVaNMfEqSIMVZJPNjgcZKmbGLNUy17Z6gOi9jZIj4R',
        'EGU9nvBhzJPHupEsi6cFTIkDfE4uKIz7fnQZsXpvWkMh5InMVlbwjGqp3AFJdBI9qeLH51I4WCuluWOL',
        'Buy books with discount from all Literary Society merchants.', 'P-3B8868172K211273WL75S46A',
        'Reader Membership', 1200.0, 'BOOKS_MANUSCRIPTS', 'Book Purchasing Discount', 'DIGITAL',
        'https://www.literary-society.com:3000/subscription/success');
