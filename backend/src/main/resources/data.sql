insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("dovla", "$2a$10$hctBLZeH2CG4qTaFLztqQumVRPs9RKlczyopnLFQrKKGMLo2hKzlq", "Vladimir", "Cvetanovic", "Novi Sad", "Srbija", "dovla@gmail.com", false, true, "ADMIN");
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("vukasin", "$2a$10$3u5vuAP6GogQOyE5JQMTOOBIVB874JoCxtcE2s15H1JBVidraPUCW", "Vukasin", "Jovic", "Novi Sad", "Srbija", "vukasin@gmail.com", false, true, "EDITOR");

insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("lazic", "$2a$10$LiTcSs1SsKYHcAjAQLejqe/VPI4YsxkHLEC8OaujOR6ShPM81sV4q", "Milan", "Lazic", "Novi Sad", "Srbija", "lazic@gmail.com", false, true, "REVIEWER");
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("djordjevic", "$2a$10$XbOz9hcDf83PMdfm1JeHRO9JwcMCtalZVC1oiCnbhT/0sVF4A8snC", "Nikola", "Djordjevic", "Novi Sad", "Srbija", "djordjevic@gmail.com", false, true, "REVIEWER");
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("malencic", "$2a$10$B/xUXG0aVTMB5Ppcvx6dO.ZdCYL.nSSNk01KWK2h7Wzo6ph9gJlu2", "Nikola", "Malencic", "Novi Sad", "Srbija", "malencic@gmail.com", false, true, "REVIEWER");

insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("milica", "$2a$10$iHLUEnk5gYMa50EclC.cEu0UTBIM2wyNAloS/59yC.MN/cAe88NeS", "Milica", "Makaric", "Novi Sad", "Srbija", "milica@gmail.com", false, true, "EDITOR");
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("andrijana", "$2a$10$TWPesMnqNm66Z9vNd/b5UudKvezOzPiuYGjI36MvonEzfvUaN8FRq", "Andrijana", "Jeremic", "Novi Sad", "Srbija", "andrijana@gmail.com", false, true, "EDITOR");



insert into role values (1, 'ROLE_USER');
insert into role values (2, 'ROLE_ADMIN');
insert into role values (3, 'ROLE_EDITOR');
insert into role values (4, 'ROLE_REVIEWER');
insert into role values (5, 'ROLE_AUTHOR');


/* admin privilegije */
insert into privilege values (1, 'SET_REVIEWER_TASK');
insert into privilege values (2, 'SET_REVIEWER');

insert into roles_privileges values (2,1);
insert into roles_privileges values (2,2);

insert into user_roles values ('dovla',2);
insert into user_roles values ('vukasin', 3);
insert into user_roles values ('lazic', 4);
insert into user_roles values ('djordjevic', 4);
insert into user_roles values ('malencic', 4);
insert into user_roles values ('milica', 3);
insert into user_roles values ('andrijana', 3);

insert into science_field values (1, 'Matematika');
insert into science_field values (2, 'Informatika');
insert into science_field values (3, 'Automatika');
insert into science_field values (4, 'Biologija');
insert into science_field values (5, 'Psihologija');


insert into user_sciencefields values ('vukasin', 1);
insert into user_sciencefields values ('vukasin', 2);
insert into user_sciencefields values ('vukasin', 3);

insert into user_sciencefields values ('lazic', 1);
insert into user_sciencefields values ('lazic', 4);
insert into user_sciencefields values ('lazic', 5);

insert into user_sciencefields values ('djordjevic', 1);
insert into user_sciencefields values ('djordjevic', 2);

insert into user_sciencefields values ('malencic', 1);
insert into user_sciencefields values ('malencic', 2);
insert into user_sciencefields values ('malencic', 3);
insert into user_sciencefields values ('malencic', 4);

insert into user_sciencefields values ('milica', 1);
insert into user_sciencefields values ('milica', 3);

insert into user_sciencefields values ('andrijana', 3);
insert into user_sciencefields values ('andrijana', 4);
insert into user_sciencefields values ('andrijana', 5);






