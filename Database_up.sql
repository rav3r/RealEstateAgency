DROP TABLE ulubione;
DROP TABLE oferty;
DROP TABLE users;
DROP TABLE adres;
DROP TABLE typy_domow;


--------------------------------------------
CREATE TABLE typy_domow
(
	id_typu_domu serial PRIMARY KEY,
	typ_domu VARCHAR(25) NOT NULL
);
--------------------------------------------


CREATE TABLE adres
(
	id_adresu serial PRIMARY KEY,
	miasto VARCHAR(30) NOT NULL,
	ulica VARCHAR(30) NOT NULL,
	nr_domu INTEGER NOT NULL,
	dl_geog NUMERIC(15,10),
	szer_geog NUMERIC(15,10)
);

--------------------------------------------

CREATE TABLE users
(
    login VARCHAR(32) PRIMARY KEY   NOT NULL,
    md5password VARCHAR(32)         NOT NULL,
	session_id VARCHAR(32)                      DEFAULT NULL,
	imie VARCHAR(20)                NOT NULL    DEFAULT 'John',
	nazwisko VARCHAR(30)            NOT NULL    DEFAULT 'Doe',
	telefon VARCHAR(15)             NOT NULL    DEFAULT '123-123-412',
	mail VARCHAR(30)                NOT NULL    DEFAULT 'jdoe@example.com'
);

--------------------------------------------

CREATE TABLE oferty
(
	id_oferty serial PRIMARY KEY,
	cena INTEGER NOT NULL,
	powierzchnia INTEGER NOT NULL,
	data_dodania DATE NOT NULL,
	id_typu_domu INTEGER REFERENCES typy_domow(id_typu_domu) NOT NULL,
	id_adresu INTEGER REFERENCES adres(id_adresu) NOT NULL,
	opis VARCHAR(500),
	wlasciciel VARCHAR(32) REFERENCES users(login) NOT NULL
);

--------------------------------------------

CREATE TABLE ulubione
(
	id_oferty INTEGER REFERENCES oferty(id_oferty) NOT NULL,
	login VARCHAR(32) REFERENCES users(login) NOT NULL,
	PRIMARY KEY (id_oferty, login)
);


INSERT INTO typy_domow(typ_domu) VALUES ('wolnostojacy');
INSERT INTO typy_domow(typ_domu) VALUES ('mieszkanie');
INSERT INTO typy_domow(typ_domu) VALUES ('kamienica');
INSERT INTO typy_domow(typ_domu) VALUES ('zabudowa szeregowa');
INSERT INTO typy_domow(typ_domu) VALUES ('domek letniskowy');
