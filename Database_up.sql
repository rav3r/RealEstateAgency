DROP TABLE ulubione;
DROP TABLE obrazki;
DROP TABLE tagoferta;
DROP TABLE oferty;
DROP TABLE users;
DROP TABLE adres;
DROP TABLE tagi;
DROP TABLE typy_umow;
DROP TABLE typy_domow;


--------------------------------------------
CREATE TABLE typy_domow
(
	id_typu_domu serial PRIMARY KEY,
	typ_domu VARCHAR(25) NOT NULL
);
--------------------------------------------

CREATE TABLE typy_umow
(
	id_typu_umowy serial PRIMARY KEY,
	typ_umowy VARCHAR(30) NOT NULL
);


--------------------------------------------
CREATE TABLE tagi
(
	id_tagu serial PRIMARY KEY,
	tresc VARCHAR(40) NOT NULL
);

--------------------------------------------

CREATE TABLE adres
(
	id_adresu serial PRIMARY KEY,
	wojewodztwo VARCHAR(30) NOT NULL,
	miasto VARCHAR(30) NOT NULL,
	kod VARCHAR(6) NOT NULL,
	ulica VARCHAR(30) NOT NULL,
	nr_domu INTEGER NOT NULL,
	nr_mieszkania INTEGER
);

--------------------------------------------

CREATE TABLE users
(
	id_user serial PRIMARY KEY,
	sessiod_id VARCHAR(32),
	imie VARCHAR(20) NOT NULL,
	nazwisko VARCHAR(30) NOT NULL,
	telefon VARCHAR(15) NOT NULL,
	mail VARCHAR(30) NOT NULL,
	pesel VARCHAR(11) NOT NULL,
	id_adresu INTEGER REFERENCES adres(id_adresu) NOT NULL
);

--------------------------------------------

CREATE TABLE oferty
(
	id_oferty serial PRIMARY KEY,
	cena INTEGER NOT NULL,
	powierzchnia INTEGER NOT NULL,
	data_dodania DATE NOT NULL,
	id_typu_domu INTEGER REFERENCES typy_domow(id_typu_domu) NOT NULL,
	id_typu_umowy INTEGER REFERENCES typy_umow(id_typu_umowy) NOT NULL,
	id_adresu INTEGER REFERENCES adres(id_adresu) NOT NULL,
	opis VARCHAR(500),
	uwagi VARCHAR(200),
	rynek VARCHAR(15) NOT NULL CHECK(rynek in ('pierwotny', 'wtorny')), --pierwotny/wtory - check
	wlasciciel INTEGER REFERENCES users(id_user) NOT NULL
);

--------------------------------------------

CREATE TABLE tagoferta
(
	id_tagu INTEGER REFERENCES tagi(id_tagu) NOT NULL,
	id_oferty INTEGER REFERENCES oferty(id_oferty) NOT NULL,
	PRIMARY KEY (id_tagu, id_oferty)
);

--------------------------------------------

CREATE TABLE obrazki
(
	id_obrazka serial PRIMARY KEY,
	id_oferty INTEGER REFERENCES oferty(id_oferty) NOT NULL,
	link VARCHAR(150) NOT NULL
);

--------------------------------------------

CREATE TABLE ulubione
(
	id_oferty INTEGER REFERENCES oferty(id_oferty) NOT NULL,
	id_user INTEGER REFERENCES users(id_user) NOT NULL,
	PRIMARY KEY (id_oferty, id_user)
);