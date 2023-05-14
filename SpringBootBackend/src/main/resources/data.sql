DELETE FROM rlibrarydb.book_authors;
DELETE FROM rlibrarydb.book_subjects;
DELETE FROM rlibrarydb.loan;
DELETE FROM rlibrarydb.author;
DELETE FROM rlibrarydb.book;
DELETE FROM rlibrarydb.bookonshelve;
DELETE FROM rlibrarydb.bookrequest;
DELETE FROM rlibrarydb.office;
DELETE FROM rlibrarydb.publisher;
DELETE FROM rlibrarydb.subject;
DELETE FROM rlibrarydb.user;

INSERT INTO `rlibrarydb`.`user`(`id`,`email`,`first_name`,`name`,`password`,`role`, `active`)
VALUES
    (1,"kurt.clemens@inetum-realdolmen.world","Kurt","Clemens","test123","ROLE_ADMIN", true),
    (2,"leander.coevoet@inetum-realdolmen.world","Leander","Coevoet","test123","ROLE_ADMIN", true),
    (3,"otis.clemens@inetum-realdolmen.world","Otis","Clemens","test123","ROLE_USER", true),
    (4,"helen.wilks@inetum-realdolmen.world","Helen","Wilks","test123","ROLE_USER", true),
    (5,"yana.clemens@inetum-realdolmen.world","Yana","Clemens","test123","ROLE_USER", false),
    (6,"sky.clemens@inetum-realdolmen.world","Sky","Clemens","test123","ROLE_USER", true);

INSERT INTO `rlibrarydb`.`office`(`id`,`city`,`name`,`number`,`postal_code`,`street`)
VALUES
    (1,"Leuven","Leuven","11","3001","Technologielaan"),
    (2,"Beersel","Huizingen","42","1654","A. Vaucampuslaan"),
    (3,"Gent","Gent","4","9050","Gaston Crommenlaan"),
    (4,"Lummen","Lummen","52","3560","Bosstraat"),
    (5,"Kontich","Kontich","26","2550","Prins Boudewijnlaan"),
    (6,"Bergen","Bergen","30","7000","Avenue Victor Maistriau");

INSERT INTO `rlibrarydb`.`publisher`(`id`,`name`)
VALUES
    (1,"It Revolution Press"),
    (2,"The MIT Press"),
    (3,"O'Reilly Media"),
    (4,"PFH Publishing"),
    (5,"Rheinwerk verlag GmbH"),
    (6,"Apress"),
    (7,"Packt Publishing");


INSERT INTO `rlibrarydb`.`subject`(`id`,`technology_name`)
VALUES
    (1,"Blazor"),
    (2,".NET Maui"),
    (3,"gRPC"),
    (4,"GraphQL"),
    (5,"ASP.NET Core"),
    (6,"Java"),
    (7,"Information Management"),
    (8,"Business Management"),
    (9,"Engineering"),
    (10,"Data Modeling"),
    (11,"MySQL"),
    (12,"Knowledge Management");

INSERT INTO `rlibrarydb`.`author`(`id`,`date_of_birth`,`first_name`,`name`)
VALUES
    (1,"1971-01-11T00:00:00","Gene","Kim"),
    (2,"1970-05-25T00:00:00","Martin","Kleppmann"),
    (3,"1976-09-16T00:00:00","Gerald C.","Kane"),
    (4,"1968-02-24T00:00:00","Anh","Nguyen Philips"),
    (5,"1955-03-06T00:00:00","Jonathan R.","Copulsky"),
    (6,"1960-03-01T00:00:00","Garth R.","Andrus"),
    (7,"1985-06-29T00:00:00","David","Papp"),
    (8,"1967-05-11T00:00:00","Allen","Downey"),
    (9,"1972-10-05T00:00:00","Chris","Mayfield"),
    (10,"1981-01-01T00:00:00","Christian","Ullenboom"),
    (11,"1958-12-16T00:00:00","Les","Jackson"),
    (12,"1969-08-25T00:00:00","Mark J.","Price"),
    (13,"1977-01-01T00:00:00","Kevin","Behre"),
    (14,"1975-10-10T00:00:00","George","Spafford");

INSERT INTO `rlibrarydb`.`bookonshelve`(`id`,`available`,`date_added`,`office_id`)
VALUES
    (1,false,"2023-04-12T08:00:00",1),
    (2,false,"2023-04-12T08:01:00",2),
    (3,false,"2023-04-12T08:02:00",3),
    (4,true,"2023-04-12T08:03:00",1),
    (5,false,"2023-04-12T08:04:00",1),
    (6,false,"2023-04-12T08:05:00",1),
    (7,true,"2023-04-12T08:06:00",2),
    (8,false,"2023-04-12T08:07:00",2);

INSERT INTO `rlibrarydb`.`book`(`id`,`description`,`isbn`,`language`,`pages`,`published_year`,`title`,`book_on_shelve_id`,`publisher_id`, `url`)
VALUES
    (1,"Build practical projects with Blazor, .NET MAUI, gRPC, GraphQL, and other enterprise technologies","978-1801813433","English","814","2022","Apps and Services with .NET 7","1","7", "https://m.media-amazon.com/images/I/41UKLcZEilL._SX404_BO1,204,203,200_.jpg"),
    (2,"Hands-On Building, Testing, and Deploying","978-1484262542","English","482","2020","The Complete ASP.NET Core 3 API Tutorial","2","6", "https://m.media-amazon.com/images/I/61uwlQ9dWKL._AC_UY327_FMwebp_QL65_.jpg"),
    (3,"This is the up-to-date, practical guide to Java","978-1493222957","English","1126","2023","Java: The Comprehensive Guide","3","5", "https://m.media-amazon.com/images/I/71I6-TD0QWL._AC_UY327_FMwebp_QL65_.jpg"),
    (4,"A hands-on introduction to computer science and programming used by many universities and high schools around the world","978-1492072508","English","305","2020","Think Java: How to Think Like A Computer Scientist","4","3","https://m.media-amazon.com/images/I/71l-rN1SR1L._AC_UY327_FMwebp_QL65_.jpg"),
    (5,"In this book you will gain an understanding of how IT systems and tools can offer your organization the best chance to succeed","978-0986821301","English","98","2011","IT Survival Guide","5","4","https://m.media-amazon.com/images/I/51HLhzHqjqL._AC_UY327_FMwebp_QL65_.jpg"),
    (6,"A book in the Management on the Cutting Edge series, published in cooperation with MIT Sloan Management Review","978-0262039680","English","280","2019","The Technology Fallacy","6","2","https://m.media-amazon.com/images/I/81oW4KnW6sL._AC_UY327_FMwebp_QL65_.jpg"),
    (7,"In this practical and comprehensive guide, author Martin Kleppmann helps you navigate this diverse landscape by examining the pros and cons of various technologies for processing and storing data","978-1449373320","English","611","2017","Designing Data-Intensive Applications","7","3","https://m.media-amazon.com/images/I/91JAIKQUkYL._AC_UY327_FMwebp_QL65_.jpg"),
    (8,"The Phoenix Project is a must read for business and IT executives who are struggling with the growing complexity of IT","B078Y98RG8","English","537","2018","The Phoenix Project","8","1","https://m.media-amazon.com/images/I/81ZMvLDtmIL._AC_UY327_FMwebp_QL65_.jpg");

INSERT INTO `rlibrarydb`.`book_authors`(`books_id`,`authors_id`)
VALUES
    (1,12),
    (2,11),
    (3,10),
    (4,8),
    (4,9),
    (5,7),
    (6,3),
    (6,4),
    (6,5),
    (6,6),
    (7,2),
    (8,1),
    (8,13),
    (8,14);

INSERT INTO `rlibrarydb`.`book_subjects`(`books_id`,`subjects_id`)
VALUES
    (1,1),
    (1,2),
    (1,3),
    (1,4),
    (2,5),
    (3,6),
    (4,6),
    (5,7),
    (6,8),
    (6,9),
    (7,10),
    (7,11),
    (8,12);

INSERT INTO `rlibrarydb`.`bookrequest`(`id`,`approved`,`author`,`date_requested`,`mail_send`,`publisher`,`purchased`,`reason`,`subject`,`title`,`user_id`)
VALUES
    (1,0,"Aristeidis Bampakos","2023-04-12T15:30:00",0,"Packt Publishing",0,"","Angular","Learning Angular","1"),
    (2,0,"David Herron","2023-04-12T15:31:00",0,"Packt Publishing",0,"","Node.js","Node.js Web Development","1"),
    (3,0,"Philip Ackermann","2023-04-12T15:32:00",0,"Rheinwerk Computing",0,"","JavaScirpt","JavaScript The Comprehensive Guide","3"),
    (4,1,"Christian Ulleboom","2023-03-12T15:30:00",1,"Packt Publishing",1,"Good sugestion","Java","Java: The Comprehensive Guide","4"),
    (5,0,"Paul Hollywood","2023-04-12T15:39:00",1,"Bloomsburry UK",0,"Not an IT related book","Baking","How To Bake","6");

INSERT INTO `rlibrarydb`.`loan`(`id`,`date_borrowed`,`date_returned`,`date_to_return`,`extended`,`book_on_shelve_id`,`user_id`)
VALUES
    (1,"2022-10-12T15:30:00","2022-11-09T15:29:00","2022-11-12T15:30:00",0,1,1),
    (2,"2022-11-09T15:30:00","2022-11-30T15:29:00","2023-12-09T15:30:00",0,4,1),
    (3,"2023-11-30T15:30:00","2022-12-29T15:30:00","2023-12-30T15:30:00",0,5,1),
    (4,"2023-01-12T15:30:00","2023-02-05T15:30:00","2023-04-12T15:30:00",0,7,3),
    (5,"2023-02-20T15:30:00",null,"2023-03-20T15:30:00",0,8,3),
    (6,"2023-03-01T15:30:00",null,"2023-04-01T15:30:00",0,3,4),
    (7,"2023-03-31T15:30:00",null,"2023-04-30T15:30:00",0,6,1),
    (8,"2023-04-30T15:30:00",null,"2023-05-30T15:30:00",0,1,1),
    (9,"2023-05-31T15:30:00",null,"2023-06-30T15:30:00",0,3,1),
    (10,"2023-02-20T15:30:00",null,"2023-03-20T15:30:00",0,5,1),
    (11,"2023-02-12T15:30:00","2023-02-25T15:30:0","2023-03-12T15:30:00",0,1,1),
    (12,"2023-03-30T15:30:00","2023-04-28T15:30:0","2023-04-30T15:30:00",0,2,2),
    (13,"2023-04-12T15:30:00",null,"2023-05-12T15:30:00",0,2,4);

-- INSERT INTO `rlibrarydb`.`user`(`id`,`email`,`first_name`,`name`,`password`,`role`)
-- VALUES
--     (7,"kurt.clemens@inetum-realdolmen.world","Kurt","Clemens","test123","ROLE_ADMIN"),
--     (8,"leander.coevoet@inetum-realdolmen.world","Leander","Coevoet","test123","ROLE_ADMIN"),
--     (9,"otis.clemens@inetum-realdolmen.world","Otis","Clemens","test123","ROLE_USER"),
--     (10,"helen.wilks@inetum-realdolmen.world","Helen","Wilks","test123","ROLE_USER"),
--     (11,"yana.clemens@inetum-realdolmen.world","Yana","Clemens","test123","ROLE_USER"),
--     (12,"sky.clemens@inetum-realdolmen.world","Sky","Clemens","test123","ROLE_USER");
--
-- INSERT INTO `rlibrarydb`.`office`(`id`,`city`,`name`,`number`,`postal_code`,`street`)
-- VALUES
--     (7,"Leuven","Leuven","11","3001","Technologielaan"),
--     (8,"Beersel","Huizingen","42","1654","A. Vaucampuslaan"),
--     (9,"Gent","Gent","4","9050","Gaston Crommenlaan"),
--     (10,"Lummen","Lummen","52","3560","Bosstraat"),
--     (11,"Kontich","Kontich","26","2550","Prins Boudewijnlaan"),
--     (12,"Bergen","Bergen","30","7000","Avenue Victor Maistriau");
--
-- INSERT INTO `rlibrarydb`.`publisher`(`id`,`name`)
-- VALUES
--     (8,"It Revolution Press"),
--     (9,"The MIT Press"),
--     (10,"O'Reilly Media"),
--     (11,"PFH Publishing"),
--     (12,"Rheinwerk verlag GmbH"),
--     (13,"Apress"),
--     (14,"Packt Publishing");
--
--
-- INSERT INTO `rlibrarydb`.`subject`(`id`,`technology_name`)
-- VALUES
--     (13,"Blazor"),
--     (14,".NET Maui"),
--     (15,"gRPC"),
--     (16,"GraphQL"),
--     (17,"ASP.NET Core"),
--     (18,"Java"),
--     (19,"Information Management"),
--     (20,"Business Management"),
--     (21,"Engineering"),
--     (22,"Data Modeling"),
--     (23,"MySQL"),
--     (24,"Knowledge Management");
--
-- INSERT INTO `rlibrarydb`.`author`(`id`,`date_of_birth`,`first_name`,`name`)
-- VALUES
--     (15,"1971-01-11T00:00:00","Gene","Kim"),
--     (16,"1970-05-25T00:00:00","Martin","Kleppmann"),
--     (17,"1976-09-16T00:00:00","Gerald C.","Kane"),
--     (18,"1968-02-24T00:00:00","Anh","Nguyen Philips"),
--     (19,"1955-03-06T00:00:00","Jonathan R.","Copulsky"),
--     (20,"1960-03-01T00:00:00","Garth R.","Andrus"),
--     (21,"1985-06-29T00:00:00","David","Papp"),
--     (22,"1967-05-11T00:00:00","Allen","Downey"),
--     (23,"1972-10-05T00:00:00","Chris","Mayfield"),
--     (24,"1981-01-01T00:00:00","Christian","Ullenboom"),
--     (25,"1958-12-16T00:00:00","Les","Jackson"),
--     (26,"1969-08-25T00:00:00","Mark J.","Price"),
--     (27,"1977-01-01T00:00:00","Kevin","Behre"),
--     (28,"1975-10-10T00:00:00","George","Spafford");
--
-- INSERT INTO `rlibrarydb`.`bookonshelve`(`id`,`available`,`date_added`,`office_id`)
-- VALUES
--     (9,true,"2023-04-12T08:00:00",1),
--     (10,false,"2023-04-12T08:01:00",2),
--     (11,false,"2023-04-12T08:02:00",3),
--     (12,true,"2023-04-12T08:03:00",1),
--     (13,true,"2023-04-12T08:04:00",1),
--     (14,false,"2023-04-12T08:05:00",1),
--     (15,true,"2023-04-12T08:06:00",2),
--     (16,false,"2023-04-12T08:07:00",2);
--
-- INSERT INTO `rlibrarydb`.`book`(`id`,`description`,`isbn`,`language`,`pages`,`published_year`,`title`,`book_on_shelve_id`,`publisher_id`)
-- VALUES
--     (9,"Build practical projects with Blazor, .NET MAUI, gRPC, GraphQL, and other enterprise technologies","978-1801813433","English","814","2022","Apps and Services with .NET 7","1","7"),
--     (10,"Hands-On Building, Testing, and Deploying","978-1484262542","English","482","2020","The Complete ASP.NET Core 3 API Tutorial","2","6"),
--     (11,"This is the up-to-date, practical guide to Java","978-1493222957","English","1126","2023","Java: The Comprehensive Guide","3","5"),
--     (12,"A hands-on introduction to computer science and programming used by many universities and high schools around the world","978-1492072508","English","305","2020","Think Java: How to Think Like A Computer Scientist","4","3"),
--     (13,"In this book you will gain an understanding of how IT systems and tools can offer your organization the best chance to succeed","978-0986821301","English","98","2011","IT Survival Guide","5","4"),
--     (14,"A book in the Management on the Cutting Edge series, published in cooperation with MIT Sloan Management Review","978-0262039680","English","280","2019","The Technology Fallacy","6","2"),
--     (15,"In this practical and comprehensive guide, author Martin Kleppmann helps you navigate this diverse landscape by examining the pros and cons of various technologies for processing and storing data","978-1449373320","English","611","2017","Designing Data-Intensive Applications","7","3"),
--     (16,"The Phoenix Project is a must read for business and IT executives who are struggling with the growing complexity of IT","B078Y98RG8","English","537","2018","The Phoenix Project","8","1");
