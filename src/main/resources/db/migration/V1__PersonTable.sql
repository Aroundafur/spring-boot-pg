CREATE TABLE person
(
 username varchar(20) NOT NULL,
 dateOfBirth varchar(10) NOT NULL ,
 PRIMARY KEY (username)
);

INSERT INTO person(username, dateOfBirth) VALUES ('AnnaJones', '2020-05-19');