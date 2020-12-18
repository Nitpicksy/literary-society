insert into company (uri, common_name, company_name, error_url, failed_url, success_url)
values ('http://localhost:8090', 'literary-society', 'Nitpicksy LU 1', 'https://localhost:3000/payment/error',
        'https://localhost:3000/payment/failed', 'https://localhost:3000/payment/success');


insert into merchant (name, company_id)
values ('Vulkan', 1);

insert into merchant (name, company_id)
values ('Laguna', 1);

insert into merchant (name, company_id)
values ('Lom', 1);

insert into merchant (name, company_id)
values ('Logos', 1);


insert into payment_method (common_name, name, subscription, status, uri)
values ('bank', 'Credit Card', false, 'APPROVED', 'https://localhost:8090/api');

insert into payment_method (common_name, name, subscription, status, uri)
values ('paypal', 'Paypal', true, 'APPROVED', 'https://localhost:8200/api');

insert into payment_method (common_name, name, subscription, status, uri)
values ('bitcoin', 'Bitcoin', false, 'APPROVED', 'https://localhost:8300/api');


insert into company_payment_methods (company_id, payment_method_id)
values (1, 1);

insert into company_payment_methods (company_id, payment_method_id)
values (1, 2);

insert into company_payment_methods (company_id, payment_method_id)
values (1, 3);


--Vulkan merchantId and merchantPassword
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id)
values ('merchantId',
        '91f30b2d7007cd72372ba0761549dca5f8c7dc3f6b104cadf1741a467250a9abd8063ba1136e11d13d2f3ceee82c17f2ac41c632d5fb57c9db8d841ba99b63b3',
        1, 1);

insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id)
values ('merchantPassword', '$2y$12$eXrOWDTddWPKYU77BTZpM.1O0ZYAZ9GgVugZi5yK/5eRUT9EYy0xm', 1, 1);

--Laguna merchantId and merchantPassword
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id)
values ('merchantId',
        '1dcae25c2de2bbab00044e500962d7cf40f9c1bbf19d508af0d1a3a9799462573af4b246d5ec3f3d7286b368f5a85b5fbb3db31ded47f3770e1c79fd5b15532',
        2, 1);

insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id)
values ('merchantPassword', '$2y$12$nUQcibFKSiCXM7o9K3fYLuedSA0how/xjScCGGHpOtjqXNGD1NFc6', 2, 1);