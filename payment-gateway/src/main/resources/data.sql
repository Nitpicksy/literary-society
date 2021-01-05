insert into company (uri, common_name, company_name, email, error_url, failed_url, success_url, certificate_name,
                     status, enabled)
values ('http://localhost:8090', 'literary-society', 'Nitpicksy LU 1', 'literary@maildrop.cc',
        'https://localhost:3000/payment/error', 'https://localhost:3000/payment/failed',
        'https://localhost:3000/payment/success', 'literary.crt', 'APPROVED', true);

insert into merchant (name, company_id)
values ('LU One Merchant', 1);

insert into merchant (name, company_id)
values ('Vulkan', 1);

insert into merchant (name, company_id)
values ('Laguna', 1);

insert into merchant (name, company_id)
values ('Lom', 1);

insert into merchant (name, company_id)
values ('Logos', 1);

insert into merchant (name, company_id)
values ('Amazon', 1);


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
values ('clientId', 'Client Id', 'text', 3);
insert into data (attribute_json_name, name, attribute_type, payment_method_id)
values ('clientSecret', 'Client Secret', 'password', 3);


-- DATA FOR PAYMENT

-- Default LU Merchant - Only PayPal data
-- For Bank: merchantId and merchantPassword
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantId', '48629d6f8ac6048d1f17266bd732324c79c26bfcacd6db69852a7b2500f94422e8d6c221e759b4e685b25959c9bed9dd5fb6a309a412a522a210c0f4e39de895', 1, 1, 1);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantPassword', '2cbcb5fcca516d90d4cd1b65c5bc844d67955b41e8ba66cdffef8dc894c981d044c09fcc1f3e04baf1475401ab321ed813fb668ccb369b2f70788a3c70793da3', 1, 1, 2);

-- For PayPal: merchantClientId and merchantClientSecret
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientId', 'AYfQea8-7YvonXN8LxkI-jigeTQuw3lg_ixCM3MabqDjGx8egncKmCiIHH05XQ0C1Azev1IlgA86XGwP', 1, 2, 3);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientSecret', 'EAK4iy7MfWLcw6QE71jlOR4nhm34ylFKlhHuL2CDsC2weGokdaUt54uZSwQO2pVge9ZaQGyOMQ1SktAl', 1,
        2, 4);

-- Vulkan
-- For Bank: merchantId and merchantPassword
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantId',
        '91f30b2d7007cd72372ba0761549dca5f8c7dc3f6b104cadf1741a467250a9abd8063ba1136e11d13d2f3ceee82c17f2ac41c632d5fb57c9db8d841ba99b63b3',
        2, 1, 1);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantPassword', '2ab816119860a4f2ea41ef58181cc965d2dc87c2bf3f5c39ffa66bb6c8e2ee7b454fe47d62be94bcf0ab09889757580756fbc4e32609ac639740aede43f22280', 2, 1, 2);

-- For PayPal: merchantClientId and merchantClientSecret
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientId', 'Af7nTAdvuAjb3_n5gzUTsfQag0ZCkF-qs6ASwFdMfLKzZ_6GWVNCB9TdTpXVAsKZBeYHSVGRqZx1h68R', 2, 2, 3);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientSecret', 'EF60H0GD454RHXIpemtrBAi1w6VmeGjmc_sY7C8aa0YaEwLp_Xv2sJTSIpLkVuqaSXdSAQ72VAWiu3MY', 2,
        2, 4);

-- Laguna
-- For Bank: merchantId and merchantPassword
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantId',
        '1dcae25c2de2bbab00044e500962d7cf40f9c1bbf19d508af0d1a3a9799462573af4b246d5ec3f3d7286b368f5a85b5fbb3db31ded47f3770e1c79fd5b15532',
        3, 1, 1);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantPassword', '4af7be4654250d64edffba14b763276fe428d9bd718f42c1362d27fd88f5bbd54d93ebc5905b5668a2db2c1c4de9d5c323e845cd60549efb2dd60456b59d5224', 3, 1, 2);

-- For PayPal: merchantClientId and merchantClientSecret
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientId', 'ARPDqaQtYU6ilCpwG0IXT35OMXGTjsA3K-QFYLItx5oRcJUOqZ527Z7BfNs6sS3yu45kdINghGRLR8vV', 3, 2, 3);
insert into data_for_payment (attribute_name, attribute_value, merchant_id, payment_method_id, data_id)
values ('merchantClientSecret', 'ECBW7VI6Tzi23LnYdLEvnvEW5fm7drD6kvgVRMD2hnTOk9f9AJSqJaD-8p0aLrOWMBdoKGM9wIoPby4j', 3,
        2, 4);