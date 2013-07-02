DELETE FROM oferty;
DELETE FROM typy_domow;
DELETE FROM adres;



INSERT INTO typy_domow VALUES (1, 'wolnostojacy');
SELECT * FROM typy_domow;

INSERT INTO adres VALUES(1, 'Krakow', 'Florianska', 10, 20.55, 20.55);
SELECT * FROM adres;

INSERT INTO oferty VALUES
(1, 200000, 100, current_date, 1, 1, 'Opis', 'cyprian');
SELECT * FROM oferty;



INSERT INTO typy_domow VALUES (2, 'mieszkanie');
SELECT * FROM typy_domow;

INSERT INTO adres VALUES(2, 'Tarnow', 'Krakowska', 10, 20.55, 20.55);
SELECT * FROM adres;

INSERT INTO oferty VALUES
(2, 400000, 60, current_date, 2, 2, 'Opis', 'cyprian');