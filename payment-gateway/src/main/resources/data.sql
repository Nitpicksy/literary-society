insert into company (uri, common_name, company_name, error_url, failed_url, success_url)
values ('http://localhost:8090', 'nitpicksy.com', 'Nitpicksy LU 1', 'http://localhost:8090/payment/error',
        'http://localhost:8090/payment/failed', 'http://localhost:8090/payment/success');


insert into merchant (name, company_id)
values ('Nolit', 1);

insert into merchant (name, company_id)
values ('Lom', 1);

insert into merchant (name, company_id)
values ('Clio', 1);

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