insert into account (first_name, last_name, city, country, email, balance) values ('Petar', 'Simic', 'Novi Sad', 'Serbia', 'petarSimic@maildrop.cc', 4000);
insert into account (first_name, last_name, city, country, email, balance) values ('Tamara', 'Simic', 'Novi Sad', 'Serbia', 'simaSimic@maildrop.cc', 3500);
insert into account (first_name, last_name, city, country, email, balance) values ('Mira', 'Simic', 'Novi Sad', 'Serbia', 'miraSimic@maildrop.cc', 4000);

--PAN broj je: 5123457252525252, securityCode je 777, card holder name je ISIDORA POPOV
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('KJoA3190DNNCPjBnX9T47a4IzcZobi92iT4HLQbOxiQ=', 'a7j5UdgQs9MVLtUUYYnVZQ==', 'dE5e7W/vMi+Ts3NVUdHGtA==','2023-12-25',1);

--PAN broj je: 7123457777777777, securityCode je 456, card holder name je TAMARA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('f9c4Su1hXCRberHJl9w0eq4IzcZobi92iT4HLQbOxiQ=', '1VBKxkzTPGz3AZb30pAsaQ==', 'xVVsRPizUJNMKglIlcYT8w==', '2023-04-25',2);

--PAN broj je: 6123457777777777, securityCode je 333, card holder name je JOVAN TODOROVIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('N6Nt+bve7MDWKOKahNCOTq4IzcZobi92iT4HLQbOxiQ=', '0y3xd7vNfCEi8jQ/kkxuuA==', 'kkSxkb7JdxNCK78l8bXpPw==', '2020-04-25',3);
