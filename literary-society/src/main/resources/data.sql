-- **************************
-- Password for all users:
-- Nitpicksy.2020
-- **************************

-- Genres
insert into genre (name)
values ('Roman');
insert into genre (name)
values ('Klasik');
insert into genre (name)
values ('Lektira');
insert into genre (name)
values ('Putopis');

-- LU One Token for Payment Gateway
-- TOKEN eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJsaXRlcmFyeS1zb2NpZXR5Iiwic3ViIjoibGl0ZXJhcnktc29jaWV0eSIsImF1ZCI6IndlYiIsImlhdCI6MTYxMjM4ODMxNywiZXhwIjoxNjEyOTkzMTE3LCJyb2xlIjoiUk9MRV9DT01QQU5ZIiwicGVybWlzc2lvbnMiOlt7ImlkIjozLCJuYW1lIjoiQ1JFQVRFX09SREVSIn1dfQ.GBbSglrJn5UMn-ESwJu0CeHOnZjZP75zvcdLTBeNNd7hmAF0_OJGrRrmyd2BrtRJBzsgvwA40Z5xxiz1ig-s1Q
-- REFRESH TOKEN eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJsaXRlcmFyeS1zb2NpZXR5Iiwic3ViIjoibGl0ZXJhcnktc29jaWV0eSIsImF1ZCI6IndlYiIsImlhdCI6MTYxMjM4ODMxNywiZXhwIjoxNjEzNTk3OTE3fQ.Ea4GXgn6eYWHVrdl47h3NpRe4LhhubJZUG6VYYWbsdnyx9BX7VVI9g3k9qxMIdJyO7isF-zhvgd3BYSX738yow
insert into jwttoken (token, refresh_token)
values ('tq9jbO5bmiw3Zqcefb2i/KuN7vFt5oFBRzi7zLjXIuh6vI7Yg7NR+6gPWWsBz2mAlhrFeZ/Swn94lgLOVICNAAgcG6mQZx0oLCJcx89ItbIztg+QJA3+Zsi+ctw2hlAW+jaSiq78R/cRPDb6e1ZFpBRpssD5DmYoJTjDHOQj5XcUWPWPHfDoRf/bP0M45raVq/JlcsEGj3ZuOxoloaNtxehUpvCCN+DGsN76rrP3K9Me7IJdfdpYLS6WnvBBFCvKp6EZpYOTfbIO7V64ZDyA3zCe6bORbopMuU84BMt6Eky3spo66QGeMNiuu+A920yWpKGCdACUmhEuK64XGYqpdw7tvo174S0AEbZ+9jNvVvtz8fWBGX2W/OUK0j73yU2hZAR+uPnxLHE2xyOeaH7orbH0WcoBQjon09veevZLcAeILDkxyPKLfWy5tYHFIB2U',
        'tq9jbO5bmiw3Zqcefb2i/KuN7vFt5oFBRzi7zLjXIuh6vI7Yg7NR+6gPWWsBz2mAlhrFeZ/Swn94lgLOVICNAAgcG6mQZx0oLCJcx89ItbIztg+QJA3+Zsi+ctw2hlAW+jaSiq78R/cRPDb6e1ZFpBRpssD5DmYoJTjDHOQj5XccUlGGQCI7i5QhQY1ACUeH4KfpOBsy1HQyxMxwX/cKi0O2RvMsieGahpbxMtezb3Jc6cr+jQNFN4rG0MQb0LpkgWeXXvW8iTKY7oh/VYSQt/utTNAyXiNk6SdmvPhnes2apMvmbiEcogB/8eDnsQ2s');


-- LU One Subscription plans
insert into subscription_plan (cancelurl, frequency_count, frequency_unit, plan_description, plan_name, price,
                               product_category, product_name, product_type, successurl)
values ('https://www.literary-society.com:3000', 3, 'MONTH',
        'Create publication requests and publish books via Literary Society.', 'Writer Membership', 2000.0,
        'BOOKS_MANUSCRIPTS', 'Book Publishing Option', 'DIGITAL',
        'https://www.literary-society.com:3000/subscription/success');
insert into subscription_plan (cancelurl, frequency_count, frequency_unit, plan_description, plan_name, price,
                               product_category, product_name, product_type, successurl)
values ('https://www.literary-society.com:3000', 1, 'MONTH',
        'Buy books with discount from all Literary Society merchants.', 'Reader Membership', 1200.0,
        'BOOKS_MANUSCRIPTS', 'Book Purchasing Discount', 'DIGITAL',
        'https://www.literary-society.com:3000/subscription/success');


-- Writers
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('WRITER', 'Ivo', 'Andrić', 'Travnik', 'Bosna i Hercegovina', 'andric@maildrop.cc', 'andric',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('WRITER', 'Branisav', 'Nušić', 'Beograd', 'Srbija', 'nusic@maildrop.cc', 'nusic',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);

-- Editors
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('User', 'Jovana', 'Jovanović', 'Kragujevac', 'Srbija', 'jovanaj@maildrop.cc', 'jovanaj',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('User', 'John', 'Parker', 'London', 'Ujedinjeno kraljevstvo', 'johnp@maildrop.cc', 'johnp',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('User', 'Oleg', 'Aleksandrov', 'Sankt Petersburg', 'Rusija', 'olega@maildrop.cc', 'olega',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);

-- Reader
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled, is_beta_reader)
values ('READER', 'Sima', 'Simic', 'Kragujevac', 'Srbija', 'simas@maildrop.cc', 'simas',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, false);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled, is_beta_reader)
values ('READER', 'Pera', 'Peric', 'London', 'Ujedinjeno kraljevstvo', 'perap@maildrop.cc', 'perap',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, false);

-- Beta-readers
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled, is_beta_reader, penalty)
values ('READER', 'Teodora', 'Todorovic', 'Novi Sad', 'Srbija', 'teodorat@maildrop.cc', 'teodorat',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true, 0);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled, is_beta_reader, penalty)
values ('READER', 'John', 'John', 'London', 'Ujedinjeno kraljevstvo', 'johnj@maildrop.cc', 'johnj',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true, 0);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled, is_beta_reader, penalty)
values ('READER', 'Olega', 'Aleksandrov', 'Sankt Petersburg', 'Rusija', 'olegaa@maildrop.cc', 'olegaa',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true, 0);

insert into beta_reader_genre (beta_reader_id, genre_id)
values (8, 1);
insert into beta_reader_genre (beta_reader_id, genre_id)
values (8, 2);
insert into beta_reader_genre (beta_reader_id, genre_id)
values (8, 3);
insert into beta_reader_genre (beta_reader_id, genre_id)
values (9, 1);
insert into beta_reader_genre (beta_reader_id, genre_id)
values (9, 3);
insert into beta_reader_genre (beta_reader_id, genre_id)
values (10, 1);
insert into beta_reader_genre (beta_reader_id, genre_id)
values (10, 4);

-- Merchants

-- default LU 1 merchant
-- Client Id: AYfQea8-7YvonXN8LxkI-jigeTQuw3lg_ixCM3MabqDjGx8egncKmCiIHH05XQ0C1Azev1IlgA86XGwP
-- Client Secret: EAK4iy7MfWLcw6QE71jlOR4nhm34ylFKlhHuL2CDsC2weGokdaUt54uZSwQO2pVge9ZaQGyOMQ1SktAl
insert into users (type, name, city, country, email, username, password, status, enabled, supports_payment_methods)
values ('MERCHANT', 'LU One Merchant', 'Beograd', 'Srbija', 'lu-1-merchant@maildrop.cc', 'lu-1-merchant',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true);

-- Client Id: ARPDqaQtYU6ilCpwG0IXT35OMXGTjsA3K-QFYLItx5oRcJUOqZ527Z7BfNs6sS3yu45kdINghGRLR8vV
-- Client Secret: ECBW7VI6Tzi23LnYdLEvnvEW5fm7drD6kvgVRMD2hnTOk9f9AJSqJaD-8p0aLrOWMBdoKGM9wIoPby4j
insert into users (type, name, city, country, email, username, password, status, enabled, supports_payment_methods)
values ('MERCHANT', 'Laguna', 'Beograd', 'Srbija', 'laguna@maildrop.cc', 'laguna',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true);
-- Client Id: Af7nTAdvuAjb3_n5gzUTsfQag0ZCkF-qs6ASwFdMfLKzZ_6GWVNCB9TdTpXVAsKZBeYHSVGRqZx1h68R
-- Client Secret: EF60H0GD454RHXIpemtrBAi1w6VmeGjmc_sY7C8aa0YaEwLp_Xv2sJTSIpLkVuqaSXdSAQ72VAWiu3MY
insert into users (type, name, city, country, email, username, password, status, enabled, supports_payment_methods)
values ('MERCHANT', 'Vulkan', 'Novi Sad', 'Srbija', 'vulkan@maildrop.cc', 'vulkan',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true);

insert into users (type, name, city, country, email, username, password, status, enabled, supports_payment_methods)
values ('MERCHANT', 'Amazon', 'Novi Sad', 'Srbija', 'amazon@maildrop.cc', 'amazon',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, false);

-- LECTURERS
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('User', 'Milica', 'Todorovic', 'Kragujevac', 'Srbija', 'milicat@maildrop.cc', 'milicat',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled, is_beta_reader, penalty)
values ('User', 'John', 'John', 'London', 'Ujedinjeno kraljevstvo', 'johnJohn1@maildrop.cc', 'johnJohn1',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true, 0);

-- Committee members
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('User', 'Eržebet', 'Guliver', 'Kruševac', 'Srbija', 'communist1@maildrop.cc', 'bor1',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('User', 'Milovan', 'Ranisavljevic', 'London', 'Ujedinjeno kraljevstvo', 'communist2@maildrop.cc', 'bor2',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);
insert into users (type, first_name, last_name, city, country, email, username, password, status,
                   enabled)
values ('User', 'INSANE MAN', 'PUSKIN', 'Sankt Petersburg', 'Rusija', 'communist3@maildrop.cc', 'bor3',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true);


-- Memberships
insert into membership (expiration_date, is_subscribed, price, merchant_id, user_id)
values ('2021-04-30', false, 2000.00, 11, 1);


-- Images
insert into image (data)
values ('na-drini-cuprija.jpg');
insert into image (data)
values ('mobi-dik.jpg');
insert into image (data)
values ('majstor-i-margarita.jpg');
insert into image (data)
values ('komo.jpg');
insert into image (data)
values ('covek-koji-je-voleo-pse.jpg');
insert into image (data)
values ('ana-karenjina.jpg');
insert into image (data)
values ('hajduci.jpg');
insert into image (data)
values ('na-drini-cuprija.jpg');


-- Books
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Na Drini ćuprija', 'Ivo Andrić', 1, 'IN_STORES',
        'Sudbina Mehmed-paše Sokolovića predodredila je da njegov život bude prekinut pre otelotvorenja njegove ideje na javi. On je napustio ovaj svet a veliki most na Drini je ostao kao simbol trajanja. Oko njega će se isplesti bezbroj priča o ljudima, njihovim sudbinama i istorijskim dešavanjima.',
        2);
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Mobi Dik', 'Herman Melvil', 2, 'IN_STORES',
        'Uzbudljiva priča o kitolovcima, tragična ispovest i složena alegorija, ovo je knjiga koja obuhvata sve aspekte ljudskog postojanja – od telesnog do metafizičkog. Duboko promišljen i s velikim brojem burnih epizoda, Mobi Dik predstavlja vrhunsko ostvarenje američke književnosti XIX veka.',
        4);
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Majstor i Margarita', 'Mihail Bulgakov', 3, 'IN_STORES',
        'Dolazak tajanstvenog Volanda i njegove svite – neobičnog gospodina u kariranim pantalonama i neponovljivog mačka Behemota – u Staljinovu Moskvu iniciraće čitav niz događaja sa tragikomičnim zapletom i surovim posledicama: svako će biti prinuđen da se suoči sa istinom o sebi.',
        1);
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Komo', 'Srđan Valjarević', 4, 'IN_STORES',
        'Jedan se pisac, koji trenutno ne piše, našao u sredini koja nije njegova, u idiličnom kraju tako različitom od Beograda iz kojega je stigao, tu susreće neke ljude, pije, šeta prirodom, planinari, konverzira koliko je potrebno, i to je uglavnom sve. No, taj minimalizam s kojim se opisuje svakodnevica, nonšalancija s kojom se usput dodiruju razne teme i međuljudski odnosi, doista su rijetko uspjeli, do te mjere da čitatelja ne ostavljaju na miru dok se ne dočepa zadnje stranice.',
        4);
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Čovek koji je voleo pse', 'Leonardo Padura', 5, 'IN_STORES',
        'Kroz razuđenu epsku  sliku najvažnijih događanja u svetu u prve četiri decenije XX veka — čiji odjeci u romanu živo pulsiraju gotovo do kraja veka — u širokom geografskom rasponu od današnjeg Kazahstana, preko Turske, nekoliko evropskih zemalja, pa do prekookeanskih Sjedinjenih Država, Meksika i Kube, pratimo životopis jednog revolucionara i njegovog ubice, dva lika zadojena istim idealima, ali potpuno suprotno ostvarivanim. Između njih i sa njima, na različite načine, nalazi se pripovedač, Kubanac.',
        1);
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Ana Karenjina', 'Lav Tolstoj', 6, 'IN_STORES',
        'Čini se da Ana Karenjina ima sve što se poželeti može – lepa je, bogata, omiljena u društvu, a njen sin je jednostavno obožava. Međutim, Ana ipak oseća da je život suviše prazan, sve dok ne sretne naočitog i šarmantnog oficira, grofa Vronskog. Njihova veza postaće pravi skandal u visokom društvu i izazvaće ogorčenost i zavist ljudi koji okružuju ovo dvoje ljubavnika.',
        3);
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Hajduci', 'Branislav Nušić', 7, 'IN_STORES',
        'Reč je o uzbudljivoj, iz dečje perspektive ispripovedanoj, avanturističkoj priči u čijem središtu je četa nestašnih dečaka, odbeglih u hajduke. Iako su njihobve zgode I nezgode, a sa tim u vezi i komične epizode, ono što zaokuplja pažnju najmlađih čitalaca, šire gledano ovaj roman govori o pobuni mladih protiv sveta odraslih, podsećajući pritom ove potonje da su i oni nekada bili deca, te da se dečjim nestašlucima nasmeju, jer dečji smeh je najveća radost starosti.',
        3);
insert into book (title, writers_names, image_id, status, synopsis, genre_id)
values ('Na Drini ćuprija', 'Ivo Andrić', 8, 'IN_STORES',
        'Sudbina Mehmed-paše Sokolovića predodredila je da njegov život bude prekinut pre otelotvorenja njegove ideje na javi. On je napustio ovaj svet a veliki most na Drini je ostao kao simbol trajanja. Oko njega će se isplesti bezbroj priča o ljudima, njihovim sudbinama i istorijskim dešavanjima.',
        2);


-- Publishing Infos
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788610034226', 352, 'Beograd', '2020-03-07', 'Vulkan', 850.00, 0, 1, 13);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788652137619', 730, 'London', '2020-07-20', 'Laguna', 1100.00, 20, 2, 12);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788610013658', 375, 'Subotica', '2015-09-14', 'Vulkan', 1230.00, 25, 3, 13);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788652133796', 240, 'Kragujevac', '2019-10-18', 'Vulkan', 0.00, 0, 4, 13);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788652138586', 632, 'Madrid', '2018-10-11', 'Laguna', 1050.00, 15, 5, 12);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788652135554', 896, 'Moskva', '2012-04-15', 'Laguna', 990.00, 10, 6, 12);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788610032291', 208, 'Beograd', '2019-04-05', 'Vulkan', 400.00, 0, 7, 13);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788610034226', 352, 'Novi Sad', '2020-03-07', 'Laguna', 0.00, 0, 8, 12);


--- PDF Documents
insert into pdfdocument(created, name, book_id)
values ('2020-03-07 14:05:30', '2020-03-07_14-05-30_Na drini ćuprija.pdf', 1);
insert into pdfdocument(created, name, book_id)
values ('2020-07-20 11:23:47', '2020-07-20_11-23-47_Mobi Dik.pdf', 2);
insert into pdfdocument(created, name, book_id)
values ('2015-09-14 15:41:55', '2015-09-14_15-41-55_Majstor i Margarita.pdf', 3);
insert into pdfdocument(created, name, book_id)
values ('2019-10-18 12:39:18', '2019-10-18_12-39-18_Komo.pdf', 4);
insert into pdfdocument(created, name, book_id)
values ('2018-10-11 13:16:45', '2018-10-11_13-16-45_Čovek koji je voleo pse.pdf', 5);
insert into pdfdocument(created, name, book_id)
values ('2012-04-15 08:54:22', '2012-04-15_08-54-22_Ana Karenjina.pdf', 6);
insert into pdfdocument(created, name, book_id)
values ('2019-04-05 10:09:14', '2019-04-05_10-09-14_Hajduci.pdf', 7);
insert into pdfdocument(created, name, book_id)
values ('2020-08-19 09:12:15', '2020-08-19_09-12-15_Na drini ćuprija.pdf', 8);

