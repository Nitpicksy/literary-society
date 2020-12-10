insert into account (first_name, last_name, city, country, email, balance) values ('Petar', 'Simic', 'Novi Sad', 'Serbia', 'petarSimic@maildrop.cc', 1000);
insert into account (first_name, last_name, city, country, email, balance) values ('Sima', 'Simic', 'Novi Sad', 'Serbia', 'simaSimic@maildrop.cc', 500);

--PAN broj je: 5225252525252222, securityCode je 256, card holder name je PERA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id)
values ('ebb07fee59d585f3fd077b9eb479f5dcb507ae58a65759c8b672855e62e016dc1572bc7d101f375fb12dc1d6bacfd6da0e8df027897bed9a8662591f0d6e2fda', '$2y$12$NbNMcl5tspF4..1lUcBk6.rykZihvAViVpqonwQx5AKCGSzh75VLW', '5f2b90178624a73e6c02180ff842c3dcff3cbee52b1b6665006443947d2f6e6dedd3d788f2693b0231b7af11596eddf017d73f0cdf247376405c59cb31584124', '2020-12-25',1);
--PAN broj je: 2552522222222222, securityCode je 456, card holder name je SIMA SIMIC
insert into credit_card (pan, security_code, card_holder_name, expiration_date,account_id) values ('767912c9e5a19d6e2d4d378346ff044e0d30c729c6ab20a9b9766e2ea825aa03031cc0e76976bb073cab36c4c8956b3ae06c1f0ab7fcf34a3b3a717305318d51', '$2y$12$cdV2DdSzoXr8HrvTFdb4We2ivJXLzoZb9DpMKVXKoWC7nKiqOOxu.', '31f592ce30b6062ba1c7bba6c06eb678c364c29f61efc389edec63fec9bc980430a07b2b1161cab407bf078df9eea079ef04fc177c01a7ac856b6d2425aecc99', '2019-04-25',2);

--8888888888888888
--sifra je perica1
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Laguna', 'Novi Sad', 'Srbija', 'laguna@maildrop.cc', 100000,
 '1dcae25c2de2bbab00044e500962d7cf40f9c1bbf19d508af0d1a3a9799462573af4b246d5ec3f3d7286b368f5a85b5fbb3db31ded47f3770e1c79fd5b15532', '$2y$12$nUQcibFKSiCXM7o9K3fYLuedSA0how/xjScCGGHpOtjqXNGD1NFc6');

--8888888888888889
--password je perica2
insert into merchant (name,city, country, email, balance, merchant_id, merchant_password) values ('Vulkan', 'Novi Sad', 'Srbija', 'vulkan@maildrop.cc', 100000,
'91f30b2d7007cd72372ba0761549dca5f8c7dc3f6b104cadf1741a467250a9abd8063ba1136e11d13d2f3ceee82c17f2ac41c632d5fb57c9db8d841ba99b63b3', '$2y$12$eXrOWDTddWPKYU77BTZpM.1O0ZYAZ9GgVugZi5yK/5eRUT9EYy0xm');
