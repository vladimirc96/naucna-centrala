insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ("dovla", "$2a$10$hctBLZeH2CG4qTaFLztqQumVRPs9RKlczyopnLFQrKKGMLo2hKzlq", "Vladimir", "Cvetanovic", "Novi Sad", "Srbija", "dovla@gmail.com", false, true, "ADMIN");

insert into role values (1, 'ROLE_USER');
insert into role values (2, 'ROLE_ADMIN');
insert into role values (3, 'ROLE_EDITOR');
insert into role values (4, 'ROLE_REVIEWER');
insert into role values (5, 'ROLE_AUTHOR');


/* admin privilegije */
insert into privilege values (1, 'SET_REVIEWER_TASK');

insert into roles_privileges values (2,1);

insert into user_roles values ('dovla',2);

insert into science_field values (1, 'Matematika');
insert into science_field values (2, 'Informatika');
insert into science_field values (3, 'Automatika');