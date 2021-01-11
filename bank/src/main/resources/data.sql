insert into account (first_name, last_name, city, country, email, balance)
values ('Petar', 'Simic', 'Novi Sad', 'Serbia', 'petarSimic@maildrop.cc', 4000);
insert into account (first_name, last_name, city, country, email, balance)
values ('Petar', 'Peric', 'Novi Sad', 'Serbia', 'petarPeric@maildrop.cc', 5500);
insert into account (first_name, last_name, city, country, email, balance)
values ('Sima', 'Simic', 'Novi Sad', 'Serbia', 'simaSimic@maildrop.cc', 1500);

--PAN broj je: 5123456252525252, securityCode je 256, card holder name je PERA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('UkI+nnxTPPXFvvrIukGokItfXaeMDkSspo69lQh1Eio=','Lkb95inURn6JlIWhw4fYkw==','CMefBs2jN6IyVohfpVmqiQ==', '2022-12-25',1);

--PAN broj je: 3123456777777777, securityCode je 355, card holder name je PETAR PERIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date, account_id)
values ('ldNIfMzU6P3GdPIgDxKWq4tfXaeMDkSspo69lQh1Eio=','Bb1xpGJ4u2ZQwQi7993O+w==',
        'ff3HruLSQ0PFGPym98C8Dg==', '2025-06-25', 2);

--PAN broj je: 5123456252525253, securityCode je 456, card holder name je SIMA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date, account_id)
values ('QBqJIO4W8ClGy8wCLcuMu4tfXaeMDkSspo69lQh1Eio=', '1z67nb15XfUkXpzPAq0SjA==',
        'yRS34wgb1quWE+ms+TXr1w==','2020-08-25', 3);

--8888888888888888
--sifra je merchant1
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Laguna', 'Novi Sad', 'Srbija', 'laguna@maildrop.cc', 100000,
 '/kMcCMjiJCt+3cwDFw/NqItfXaeMDkSspo69lQh1Eio=','sJy4zjVv4esDc/hDnvND7Q==');

--8888888888888889
--password je merchant2
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Vulkan', 'Novi Sad', 'Srbija', 'vulkan@maildrop.cc', 100000,
'0Xj8Un/aQPQeaN+GBJTYn4tfXaeMDkSspo69lQh1Eio=','rgAHm9WmDOyBviSQkyymaw==');

--9999999999999999
--sifra je merchant
 insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Default LU Merchant', 'Novi Sad', 'Srbija', 'merchant@maildrop.cc', 100000,
'js+O0RjwKUwnH/qod9SIdYtfXaeMDkSspo69lQh1Eio=', 'OyghaxVva3bgfesmOxnEFw==');
