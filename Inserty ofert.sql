DELETE FROM oferty;
DELETE FROM typy_domow;
DELETE FROM typy_umow;
DELETE FROM tagi;
DELETE FROM adres;


INSERT INTO typy_domow VALUES (1, 'wolnostojacy');
SELECT * FROM typy_domow;

INSERT INTO typy_umow VALUES (1, 'cywilno-prawna');
SELECT * FROM typy_umow;

INSERT INTO tagi VALUES (1, 'maly');
INSERT INTO tagi VALUES (2, 'duzy');
SELECT * FROM tagi;

INSERT INTO adres VALUES(1, 'malopolskie', 'Krakow', '30-000', 'Florianska', 10);
SELECT * FROM adres;

INSERT INTO oferty VALUES
(1, 200000, 100, current_date, 1, 1, 1, 'Opis', 'Uwagi', 'pierwotny', 'cyprian');
SELECT * FROM oferty;



INSERT INTO typy_domow VALUES (2, 'mieszkanie');
SELECT * FROM typy_domow;

INSERT INTO adres VALUES(2, 'malopolskie', 'Tarnow', '36-666', 'Krakowska', 10);
SELECT * FROM adres;

INSERT INTO oferty VALUES
(2, 400000, 60, current_date, 2, 1, 2, 'Opis', 'Uwagi', 'pierwotny', 'cyprian');
SELECT * FROM oferty;

--SELECT * FROM users;