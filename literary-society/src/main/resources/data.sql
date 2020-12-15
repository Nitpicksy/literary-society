-- **************************
-- Password for all users:
-- Nitpicksy.2020
-- **************************

-- Genres
insert into genre (name)
values ('Romani');
insert into genre (name)
values ('Fantastika');
insert into genre (name)
values ('Poezija');
insert into genre (name)
values ('Putopisi');


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


-- Merchants
insert into users (type, name, city, country, email, username, password, status, enabled, supports_payment_methods)
values ('MERCHANT', 'Laguna', 'Beograd', 'Srbija', 'laguna@maildrop.cc', 'laguna',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true);
insert into users (type, name, city, country, email, username, password, status, enabled, supports_payment_methods)
values ('MERCHANT', 'Vulkan', 'Novi Sad', 'Srbija', 'vulkan@maildrop.cc', 'vulkan',
        '$2a$12$ZsxtTnQCQxQJLq0tvgRPzO8CMnbcxu8VMGD5QIj9C7zTxoin6Dykm', 'ACTIVE', true, true);


-- Images
insert into image (data)
values ('na-drini-cuprija.jpg');
insert into image (data)
values ('moby-dick.jpg');
insert into image (data)
values ('naucite-bootstrap-4.jpg');
insert into image (data)
values ('i-leoni-di-sicilia.jpg');
insert into image (data)
values ('covek-koji-je-voleo-pse.jpg');
insert into image (data)
values ('a-young-doctors-notebook.jpg');
insert into image (data)
values ('javascript-fp.jpg');
insert into image (data)
values ('na-drini-cuprija.jpg');


-- Books
insert into book (title, writers_names, image_id, status, synopsis)
values ('Na Drini ćuprija', 'Ivo Andrić', 1, 'IN_STORES',
        'Sudbina Mehmed-paše Sokolovića predodredila je da njegov život bude prekinut pre otelotvorenja njegove ideje na javi. On je napustio ovaj svet a veliki most na Drini je ostao kao simbol trajanja. Oko njega će se isplesti bezbroj priča o ljudima, njihovim sudbinama i istorijskim dešavanjima.');
insert into book (title, writers_names, image_id, status, synopsis)
values ('Moby Dick', 'Herman Melville', 2, 'IN_STORES',
        'When the young Ishmael gets on board Captain Ahab''s whaling ship, little does he suspect that the mission on which he is about to embark is the fulfilment of his master''s obsessive desire for revenge on Moby Dick, a white whale who has already claimed countless human victims and destroyed many fleets. With some sinister crew members in their midst and the hazardous conditions of the sea to contend with, the expedition becomes increasingly dangerous the closer it gets to its quarry.');
insert into book (title, writers_names, image_id, status, synopsis)
values ('Naučite Bootstrap 4', 'Benjamin Jakobus, Jason Marah', 3, 'IN_STORES',
        'Ova knjiga će vam pomoći da upotrebite i prilagodite Bootstrap za kreiranje privlačnih veb sajtova koji odgovaraju vašim potrebama. Kreiraćete prilagođeni Bootstrap veb sajt korišćenjem različitih pristupa za prilagođavanje radnog okvira. Koristićete ključne funkcije Bootstrapa i brzo ćete otkriti različite načine na koje Bootstrap može da vam pomogne da kreirate veb interfejse.');
insert into book (title, writers_names, image_id, status, synopsis)
values ('I leoni di Sicilia', 'Stefania Auci', 4, 'IN_STORES',
        'Roman o porodici Florio, nekrunisanim kraljevima Sicilije. Bila jednom jedna porodica koja se suprotstavila postojećem. Porodica koja je rizikovala sve. Porodica koja je ušla u legendu. Uplićući priču o društvenom i trgovačkom uspehu Floriovih sa njihovim burnim životima, tokom najnemirnijih godina u italijanskoj istoriji, Stefanija Auči razmotava klupko jedne neverovatne porodične sage.');
insert into book (title, writers_names, image_id, status, synopsis)
values ('Čovek koji je voleo pse', 'Leonardo Padura', 5, 'IN_STORES',
        'Kroz razuđenu epsku  sliku najvažnijih događanja u svetu u prve četiri decenije XX veka — čiji odjeci u romanu živo pulsiraju gotovo do kraja veka — u širokom geografskom rasponu od današnjeg Kazahstana, preko Turske, nekoliko evropskih zemalja, pa do prekookeanskih Sjedinjenih Država, Meksika i Kube, pratimo životopis jednog revolucionara i njegovog ubice, dva lika zadojena istim idealima, ali potpuno suprotno ostvarivanim. Između njih i sa njima, na različite načine, nalazi se pripovedač, Kubanac.');
insert into book (title, writers_names, image_id, status, synopsis)
values ('A Young Doctor''s Notebook', 'Mikhail Bulgakov', 6, 'IN_STORES',
        'Using a sharply realistic and humorous style, Bulgakov reveals his doubts about his own competence and the immense burden of responsibility, as he deals with a superstitious and poorly educated people struggling to enter the modern age.');
insert into book (title, writers_names, image_id, status, synopsis)
values ('JavaScript funkc. programiranje', 'Federico Kereki', 7, 'IN_STORES',
        'Funkcionalno programiranje je paradigma za razvoj softvera sa boljim performansama. Ono pomaže da napišete sažet kod i kod koji se može testirati. Da biste podigli vaše programske veštine na viši nivo, ova sveobuhvatna knjiga će vam pomoći da iskoristite mogućnosti funkcionalnog programiranja u JavaScriptu i da napišete visokoodržive i testirane aplikacije za Veb i server pomoću funkcionalnog JavaScripta.');
insert into book (title, writers_names, image_id, status, synopsis)
values ('Na Drini ćuprija', 'Ivo Andrić', 8, 'IN_STORES',
        'Sudbina Mehmed-paše Sokolovića predodredila je da njegov život bude prekinut pre otelotvorenja njegove ideje na javi. On je napustio ovaj svet a veliki most na Drini je ostao kao simbol trajanja. Oko njega će se isplesti bezbroj priča o ljudima, njihovim sudbinama i istorijskim dešavanjima.');


-- Publishing Infos
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788610034226', 352, 'Beograd', '2020-03-07', 'Vulkan izdavaštvo', 935.00, 10, 1, 7);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9781847496447', 704, 'London', '2017-05-11', 'Alma Books', 0.00, 0, 2, 7);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788673105260', 354, 'Subotica', '2018-09-14', 'Kompjuter biblioteka', 1760.00, 20, 3, 6);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788842931539', 448, 'Rim', '2019-04-05', 'Editrice Nord', 1170.00, 10, 4, 6);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788652138586', 632, 'Novi Sad', '2018-10-11', 'Laguna', 1530.00, 15, 5, 7);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9781847492869', 160, 'Moskva', '2012-07-03', 'Alma Books', 0.00, 0, 6, 7);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788673105505', 470, 'Subotica', '2020-06-12', 'Kompjuter biblioteka', 2310.00, 20, 7, 6);
insert into publishing_info(isbn, number_of_pages, publisher_city, publication_date, publisher, price, discount,
                            book_id, merchant_id)
values ('9788610034226', 352, 'Beograd', '2020-03-07', 'Vulkan izdavaštvo', 890.00, 5, 8, 6);

