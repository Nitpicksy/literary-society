insert into company (uri, common_name, company_name, email, error_url, failed_url, success_url, certificate_name,
                     status, enabled)
values ('http://localhost:8090', 'literary-society', 'Nitpicksy LU 1', 'literary@maildrop.cc',
        'https://www.literary-society.com:3000/payment/error', 'https://www.literary-society.com:3000/payment/failed',
        'https://www.literary-society.com:3000/payment/success', 'literary.crt', 'APPROVED', true);

insert into merchant (name, company_id, supports_payment_methods)
values ('LU One Merchant', 1, true);

insert into merchant (name, company_id, supports_payment_methods)
values ('Vulkan', 1, true);

insert into merchant (name, company_id, supports_payment_methods)
values ('Laguna', 1, true);

insert into merchant (name, company_id, supports_payment_methods)
values ('Lom', 1, true);

insert into merchant (name, company_id, supports_payment_methods)
values ('Logos', 1, true);

insert into merchant (name, company_id, supports_payment_methods)
values ('Amazon', 1, true);


insert into payment_method (common_name, name, subscription, status, uri, email, certificate_name)
values ('bank', 'Credit Card', false, 'APPROVED', 'https://localhost:8090/api', 'bank@maildrop.cc', 'bank.crt');

insert into payment_method (common_name, name, subscription, status, uri, email, certificate_name)
values ('paypal', 'Paypal', true, 'APPROVED', 'https://localhost:8200/api', 'paypal@maildrop.cc', 'paypal.crt');

insert into payment_method (common_name, name, subscription, status, uri, email, certificate_name)
values ('bitcoin', 'Bitcoin', false, 'APPROVED', 'https://localhost:8300/api', 'bitcoin@maildrop.cc', 'bitcoin.crt');


insert into company_payment_methods (company_id, payment_method_id)
values (1, 1);

insert into company_payment_methods (company_id, payment_method_id)
values (1, 2);

insert into company_payment_methods (company_id, payment_method_id)
values (1, 3);


--DATA
--Bank
insert into data (attribute_json_name, name, attribute_type, payment_method_id)
values ('merchantId', 'Merchant Id', 'text', 1);
insert into data (attribute_json_name, name, attribute_type, payment_method_id)
values ('merchantPassword', 'Merchant Password', 'password', 1);

--PayPal
insert into data (attribute_json_name, name, attribute_type, payment_method_id)
values ('merchantClientId', 'Merchant client Id', 'text', 2);
insert into data (attribute_json_name, name, attribute_type, payment_method_id)
values ('merchantClientSecret', 'Merchant Client Secret', 'password', 2);

--Bitcoin
insert into data (attribute_json_name, name, attribute_type, payment_method_id)
values ('merchantToken', 'Merchant API Token', 'password', 3);


-- DATA FOR PAYMENT

-- Default LU Merchant
-- For Bank: merchantId and merchantPassword
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantId','znRXaWARTj/GLNy7B2RXaHMw/p9gSQCbGMsWZmQHHZc=',1, 1, 1);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantPassword', '3eNe4BSkd8W8Dnf3vJwsBQ==',  1, 1, 2);

-- For PayPal: merchantClientId and merchantClientSecret
-- merchantClientId je AZRZ5NKCNB4ZkLdmx-o_SZIL3lTbGaKd0tiX2dyVaNMfEqSIMVZJPNjgcZKmbGLNUy17Z6gOi9jZIj4R
-- merchantClientSecret je EGU9nvBhzJPHupEsi6cFTIkDfE4uKIz7fnQZsXpvWkMh5InMVlbwjGqp3AFJdBI9qeLH51I4WCuluWOL
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientId', 'HjrUQgJ3UlgMAA8IxiuLp8M7y3j0LcqN6qW26wtgiw3kq/v/7sgQH6vflrM9fdogUDuzOsUiLE45Vyf2rMOCNpPSgT5BaA1blEYJDQ0PwD9zMP6fYEkAmxjLFmZkBx2X', 1, 2, 3);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientSecret', '1bMOqvW9nhI12qIWoNcS0JQFqysx5oH/70/RNpHNpNmP8wEMOYv3Sl4tf6IFRecCY67shanoz8rCSnVB086qswNjHSLjjT+lIRVq3EiMc+FzMP6fYEkAmxjLFmZkBx2X', 1,2, 4);

-- For Bitcoin: merchantToken
-- merchantToken je xtQ8ain9XYBGv64fsVf3maGm8NzDe_NJHwyZJXJC
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantToken','XkS0CGAC7y37MLSz5UvVrOkHaJSrcWEynG9KGIKDVXtqeqBWrPAzd+gccai5NCo+',1, 3, 5);

-- Vulkan
-- For Bank: merchantId and merchantPassword
-- merchantId je 8888888888888889
-- merchantPassword je merchant2
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantId','hicitSlQatJeuNkuJ+Cq4XMw/p9gSQCbGMsWZmQHHZc=', 2, 1, 1);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantPassword', 'oHk9uPXFVxN77xIdiSA41A==', 2, 1, 2);

-- For PayPal: merchantClientId and merchantClientSecret
-- merchantClientId je Af7nTAdvuAjb3_n5gzUTsfQag0ZCkF-qs6ASwFdMfLKzZ_6GWVNCB9TdTpXVAsKZBeYHSVGRqZx1h68R
-- merchantClientSecret je EF60H0GD454RHXIpemtrBAi1w6VmeGjmc_sY7C8aa0YaEwLp_Xv2sJTSIpLkVuqaSXdSAQ72VAWiu3MY
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientId', 'ZhDqYXnp6f08ZSk8fhDqiXUUijfLSLOUNsMDhtCHeRYpF2ZwLmW75lCImvuV9SZvuQLkgmr7DrU/doAyWbd+DD0SK8xkTTerpm4YONqr561zMP6fYEkAmxjLFmZkBx2X', 2, 2,3);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientSecret', 'gaPuXYOoJbl6ius3H9cM3SiR+1WCM6twEnz4ZZwOMN4fj02diM26ECJEUCOYTGby+GRia/BMiJ9TzgTBt8sa5jXR51TVtzvFgLnaeDYo4OBzMP6fYEkAmxjLFmZkBx2X', 2, 2, 4);

-- For Bitcoin: merchantToken
-- merchantToken je xtQ8ain9XYBGv64fsVf3maGm8NzDe_NJHwyZJXJC
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantToken','XkS0CGAC7y37MLSz5UvVrOkHaJSrcWEynG9KGIKDVXtqeqBWrPAzd+gccai5NCo+', 2, 3, 5);


-- Laguna
-- For Bank: merchantId and merchantPassword
-- merchantId je 8888888888888888
-- merchantPassword je merchant1
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantId','MCCrFmY/JEiq7Skdpgc7nXMw/p9gSQCbGMsWZmQHHZc=', 3, 1, 1);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantPassword', 'S5Vuko/c7Tq0WNP7kvt2aQ==', 3, 1, 2);

-- For PayPal: merchantClientId and merchantClientSecret
-- merchantClientId je ARPDqaQtYU6ilCpwG0IXT35OMXGTjsA3K-QFYLItx5oRcJUOqZ527Z7BfNs6sS3yu45kdINghGRLR8vV
-- merchantClientSecret je ECBW7VI6Tzi23LnYdLEvnvEW5fm7drD6kvgVRMD2hnTOk9f9AJSqJaD-8p0aLrOWMBdoKGM9wIoPby4j
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientId', 'peqGo1stDYXqMsel3LPL1Xv4XRLDuMeij6ROyRQDLquJctqb2dgY6o/Zp9rHXy6U7spWS6YGm+X1g2SmKPPLTzydegBBzleOFfNpuIrLOhdzMP6fYEkAmxjLFmZkBx2X', 3, 2, 3);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientSecret', 'hty26ltyxHZ7itEDMr9SXE2ezIDDVgsJLdDJxldV9Q8Z1XGAiR/ywdNKAs8jPBunbJBcJyezSvQcAAtgAgEv1efFBFenLOm+A1UbL9dYmolzMP6fYEkAmxjLFmZkBx2X', 3, 2, 4);

-- For Bitcoin: merchantToken
-- merchantToken je xtQ8ain9XYBGv64fsVf3maGm8NzDe_NJHwyZJXJC
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantToken', 'XkS0CGAC7y37MLSz5UvVrOkHaJSrcWEynG9KGIKDVXtqeqBWrPAzd+gccai5NCo+', 3, 3, 5);

-- SUBSCRIPTION PLANS

-- Default LU Merchant
insert into subscription_plan (company_common_name, company_plan_id, paypal_plan_id)
values ('literary-society', 1, 'P-74S64158P6034971TL75S45Y');
insert into subscription_plan (company_common_name, company_plan_id, paypal_plan_id)
values ('literary-society', 2, 'P-3B8868172K211273WL75S46A');