insert into account (first_name, last_name, city, country, email, balance) values ('Petar', 'Simic', 'Novi Sad', 'Serbia', 'petarSimic@maildrop.cc', 4000);
insert into account (first_name, last_name, city, country, email, balance) values ('Tamara', 'Simic', 'Novi Sad', 'Serbia', 'simaSimic@maildrop.cc', 3500);
insert into account (first_name, last_name, city, country, email, balance) values ('Mira', 'Simic', 'Novi Sad', 'Serbia', 'miraSimic@maildrop.cc', 4000);

--PAN broj je: 5123457252525252, securityCode je 256, card holder name je PERA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('4bae9c68115e485421f998a878b0119219801a6904eefd2a7b56a668ae1c376e3e2113f10a3ee63c0a689d6cdde95b8605ca6842ff837f437c8673665f8e4312', '$2a$12$73hN9XaqJ707.kf2Raba7.CCG6PsGBzIRxAqzQVO8/y1rWh.qN6eO', '5f2b90178624a73e6c02180ff842c3dcff3cbee52b1b6665006443947d2f6e6dedd3d788f2693b0231b7af11596eddf017d73f0cdf247376405c59cb31584124', '2020-12-25',1);

--PAN broj je: 7123457777777777, securityCode je 456, card holder name je TAMARA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('14d5553f1c4ea0e2fd85dc42b568d99765542955d7b2c03d9230f77de1cf5c64d0c2b5ee3d945221d6a301ece3b66838bf26e605e83bf29dfd35c416cdabbe46',
'$2y$12$cdV2DdSzoXr8HrvTFdb4We2ivJXLzoZb9DpMKVXKoWC7nKiqOOxu.',
 '27869b1ccda55717d167bcda149acff4ff8880f36514a2c3e12313dc1f3b2715ec3a5c0a8ddb5df1bc2509f31a274ea842100729bcd23aeaff3ae3e401618c51', '2023-04-25',2);

--PAN broj je: 6123457777777777, securityCode je 456, card holder name je TAMARA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('6eaf3d3cda2fd8a32b4dba996584b028f017aea304d2eb22cff68bba6ea4dd08eb998e720233a9c6a4b7a18e7c25cfc0ef7bf3eb9fe464cbc42d97dfad4191c5',
'$2y$12$cdV2DdSzoXr8HrvTFdb4We2ivJXLzoZb9DpMKVXKoWC7nKiqOOxu.',
 '27869b1ccda55717d167bcda149acff4ff8880f36514a2c3e12313dc1f3b2715ec3a5c0a8ddb5df1bc2509f31a274ea842100729bcd23aeaff3ae3e401618c51', '2020-04-25',3);

--8888888888888888
--sifra je perica1
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Laguna', 'Novi Sad', 'Srbija', 'laguna@maildrop.cc', 100000,
 '1dcae25c2de2bbab00044e500962d7cf40f9c1bbf19d508af0d1a3a9799462573af4b246d5ec3f3d7286b368f5a85b5fbb3db31ded47f3770e1c79fd5b15532', '$2y$12$nUQcibFKSiCXM7o9K3fYLuedSA0how/xjScCGGHpOtjqXNGD1NFc6');

--8888888888888889
--password je perica2
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Vulkan', 'Novi Sad', 'Srbija', 'vulkan@maildrop.cc', 100000,
'91f30b2d7007cd72372ba0761549dca5f8c7dc3f6b104cadf1741a467250a9abd8063ba1136e11d13d2f3ceee82c17f2ac41c632d5fb57c9db8d841ba99b63b3', '$2y$12$eXrOWDTddWPKYU77BTZpM.1O0ZYAZ9GgVugZi5yK/5eRUT9EYy0xm');
