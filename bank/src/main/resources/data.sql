insert into account (first_name, last_name, city, country, email, balance)
values ('Petar', 'Simic', 'Novi Sad', 'Serbia', 'petarSimic@maildrop.cc', 4000);
insert into account (first_name, last_name, city, country, email, balance)
values ('Petar', 'Peric', 'Novi Sad', 'Serbia', 'petarPeric@maildrop.cc', 5500);
insert into account (first_name, last_name, city, country, email, balance)
values ('Sima', 'Simic', 'Novi Sad', 'Serbia', 'simaSimic@maildrop.cc', 1500);

--PAN broj je: 5123456252525252, securityCode je 256, card holder name je PERA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('d6b8a4360a29876fa3f32769a5b5682e390f1d8eb0d49f3469d1aa17df9cef0f5a8843464db10e9377f93afc7fb0e99816f9f968941aebdbc279290101b177be',
 '$2y$12$NbNMcl5tspF4..1lUcBk6.rykZihvAViVpqonwQx5AKCGSzh75VLW', '5f2b90178624a73e6c02180ff842c3dcff3cbee52b1b6665006443947d2f6e6dedd3d788f2693b0231b7af11596eddf017d73f0cdf247376405c59cb31584124',
  '2022-12-25',1);

--PAN broj je: 3123456777777777, securityCode je 256, card holder name je PETAR PERIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date, account_id)
values ('e8bf48b1f3e17bd78d18aedaf8d6456a32002972e369781d8fda982e8aa845fbe586948eb955828d72b019388d7bae706ef5bcd3e4989e2706f4bfa1384fdfcf',
        '$2y$12$NbNMcl5tspF4..1lUcBk6.rykZihvAViVpqonwQx5AKCGSzh75VLW',
        'c26f1a74fc5e8dc6be3fd8bf8434d9c4163510c319348b2c00bd62ec50f155c8c8060b4ad308ec91dca1c548b30d620f9a3d928515420a66126c89db68522c2a',
        '2025-06-25', 2);

--PAN broj je: 5123456252525253, securityCode je 456, card holder name je SIMA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date, account_id)
values ('dd642550b81c3e299d759d41a63f3fb22091f155f49dffb133a0695a2991cc6f04bb2597c8347a60693287d6d709630a65efa9c30c9db77bc4678a252c1548f4',
        '$2y$12$cdV2DdSzoXr8HrvTFdb4We2ivJXLzoZb9DpMKVXKoWC7nKiqOOxu.',
        '31f592ce30b6062ba1c7bba6c06eb678c364c29f61efc389edec63fec9bc980430a07b2b1161cab407bf078df9eea079ef04fc177c01a7ac856b6d2425aecc99',
        '2020-08-25', 3);

--8888888888888888
--sifra je merchant1
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Laguna', 'Novi Sad', 'Srbija', 'laguna@maildrop.cc', 100000,
 'MCCrFmY/JEiq7Skdpgc7nXMw/p9gSQCbGMsWZmQHHZc=','S5Vuko/c7Tq0WNP7kvt2aQ==');

--8888888888888889
--password je merchant2
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Vulkan', 'Novi Sad', 'Srbija', 'vulkan@maildrop.cc', 100000,
'hicitSlQatJeuNkuJ+Cq4XMw/p9gSQCbGMsWZmQHHZc=','oHk9uPXFVxN77xIdiSA41A==');

--9999999999999999
--sifra je merchant
 insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Default LU Merchant', 'Novi Sad', 'Srbija', 'merchant@maildrop.cc', 100000,
'znRXaWARTj/GLNy7B2RXaHMw/p9gSQCbGMsWZmQHHZc=', '3eNe4BSkd8W8Dnf3vJwsBQ==');
