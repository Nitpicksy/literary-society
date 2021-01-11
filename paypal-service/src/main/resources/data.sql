-- SUBSCRIPTION PLANS

-- Default LU Merchant
-- merchant_client_id je AZRZ5NKCNB4ZkLdmx-o_SZIL3lTbGaKd0tiX2dyVaNMfEqSIMVZJPNjgcZKmbGLNUy17Z6gOi9jZIj4R
-- merchant_client_secret je EGU9nvBhzJPHupEsi6cFTIkDfE4uKIz7fnQZsXpvWkMh5InMVlbwjGqp3AFJdBI9qeLH51I4WCuluWOL
insert into subscription_plan (cancelurl, frequency_count, frequency_unit, merchant_client_id,
                                              merchant_client_secret, plan_description, plan_id, plan_name, price,
                                              product_category, product_name, product_type, successurl)
values ('https://www.literary-society.com:3000', 3, 'MONTH',
        'WsF6Xfi0WrmBIMun/Qi8d4AMwXZA/r8ejhVHtHIVbCiCnAbOv1inom6qyu7Njo8lNlcVGcYpxO6u7gB8OixpDzmgRoI19y7KUEd4/azJgKGe/iRSXL7pK8YOUc8nX2Wl',
        'YxyRJCOcz5l4yE/fJp+jOkANlQ7FRo//OVT8BCd6QT8jRld+G1CqKBmFsJbUwhAd6LQ7smrODHM17+LA0luuB90nlZ/v7+XtvbcL/WmUP/Ge/iRSXL7pK8YOUc8nX2Wl',
        'Create publication requests and publish books via Literary Society.', 'P-74S64158P6034971TL75S45Y',
        'Writer Membership', 2000.0, 'BOOKS_MANUSCRIPTS', 'Book Publishing Option', 'DIGITAL',
        'https://www.literary-society.com:3000/subscription/success');

-- merchant_client_id je AZRZ5NKCNB4ZkLdmx-o_SZIL3lTbGaKd0tiX2dyVaNMfEqSIMVZJPNjgcZKmbGLNUy17Z6gOi9jZIj4R
-- merchant_client_secret je EGU9nvBhzJPHupEsi6cFTIkDfE4uKIz7fnQZsXpvWkMh5InMVlbwjGqp3AFJdBI9qeLH51I4WCuluWOL
insert into subscription_plan (cancelurl, frequency_count, frequency_unit, merchant_client_id,
                                              merchant_client_secret, plan_description, plan_id, plan_name, price,
                                              product_category, product_name, product_type, successurl)
values ('https://www.literary-society.com:3000', 1, 'MONTH',
        'WsF6Xfi0WrmBIMun/Qi8d4AMwXZA/r8ejhVHtHIVbCiCnAbOv1inom6qyu7Njo8lNlcVGcYpxO6u7gB8OixpDzmgRoI19y7KUEd4/azJgKGe/iRSXL7pK8YOUc8nX2Wl',
        'YxyRJCOcz5l4yE/fJp+jOkANlQ7FRo//OVT8BCd6QT8jRld+G1CqKBmFsJbUwhAd6LQ7smrODHM17+LA0luuB90nlZ/v7+XtvbcL/WmUP/Ge/iRSXL7pK8YOUc8nX2Wl',
        'Buy books with discount from all Literary Society merchants.', 'P-3B8868172K211273WL75S46A',
        'Reader Membership', 1200.0, 'BOOKS_MANUSCRIPTS', 'Book Purchasing Discount', 'DIGITAL',
        'https://www.literary-society.com:3000/subscription/success');
